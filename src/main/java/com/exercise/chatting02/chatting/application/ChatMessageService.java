package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.domain.dto.ChatMessageRedisDTO;
import com.exercise.chatting02.chatting.domain.model.ChatMessage;
import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatMessageRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    private final RedisTemplate<String, Object> chatMessageRedisTemplate;

    /*
        채팅메시지 redis에 저장
     */
    public void saveMessageRedis(long roomId, ChatMessageRedisDTO chatMessageRedisDTO) {
        String key = "room:" + roomId;

        // 메시지를 JSON 직렬화하여 List에 추가
        chatMessageRedisTemplate.opsForList().rightPush(key, chatMessageRedisDTO);
    }

    /*
        채팅 메시지 목록을 Redis에서 가져오기
    */
    public List<ChatMessageRedisDTO> getMessagesFromRedis(long roomId) {
        String key = "room:" + roomId;

        // Redis List 값 가져오기
        List<Object> messages = chatMessageRedisTemplate.opsForList().range(key, 0, -1);

        // JSON 역직렬화하여 List<ChatMessageRedisDTO>로 변환
        return messages.stream()
                .map(obj -> (ChatMessageRedisDTO) obj)
                .collect(Collectors.toList());
    }

    /*
        채팅방 종료시 RDB에 한꺼번에 저장
     */
    public void saveAllMessagesRDB(long roomId) {
        List<ChatMessageRedisDTO> messagesFromRedis = getMessagesFromRedis(roomId);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);

        List<ChatMessage> messageList = new LinkedList<>();
        Map<Long, ChatParticipant> tmpSender = new HashMap<>();
        for (ChatMessageRedisDTO chatMessageRedisDTO : messagesFromRedis) {
            Long senderId = chatMessageRedisDTO.getSenderId();
            ChatParticipant chatParticipant = null;
            if (tmpSender.containsKey(senderId)) {
                chatParticipant = tmpSender.get(senderId);
            } else {
                User user = userRepository.findById(senderId).orElse(null);
                chatParticipant = chatParticipantRepository.findByChatterAndRoomAndExitAt(user, chatRoom, null).orElse(null);
                tmpSender.put(senderId, chatParticipant);
            }
            ChatMessage dbChatMessage = ChatMessage.builder()
                    .room(chatRoom).sender(chatParticipant).message(chatMessageRedisDTO.getMessage()).sendAt(chatMessageRedisDTO.getSendedAt())
                    .build();
            messageList.add(dbChatMessage);
        }

        chatMessageRepository.saveAll(messageList);

        chatMessageRedisTemplate.delete("room:" + roomId);

    }

    /*
        개별 채팅메시지 MySQL에 저장
        save관련해서, getReferenceById()로 최적화 하는것 고려
     */
    public void saveMessageRDB(long roomId, long senderId, String message) {
        User user = userRepository.findById(senderId).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatterAndRoomAndExitAt(user, chatRoom, null).orElse(null);

        if (chatParticipant != null && chatRoom != null) {
            ChatMessage dbChatMessage = ChatMessage.builder()
                    .room(chatRoom).sender(chatParticipant).message(message).sendAt(LocalDateTime.now())
                    .build();
            chatMessageRepository.save(dbChatMessage);
        } else {
            log.info("잘못된 채팅메시지 저장 시도 roomId={}, senderId={}", roomId, senderId);
        }
    }
}
