package com.exercise.chatting02.chatting.application;

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
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;


    /*
        채팅메시지 redis에 저장
     */
    public void saveMessageRedis() {

    }


    /*
        채팅메시지 MySQL에 저장
        save관련해서, getReferenceById()로 최적화 하는것 고려
     */
    public void saveMessageRDB(long roomId, long senderId, String message) {
        User user = userRepository.findById(senderId).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatterAndRoomAndExitAt(user, chatRoom, null).orElse(null);

        if (chatParticipant != null && chatRoom != null) {
            ChatMessage dbChatMessage = ChatMessage.builder()
                    .room(chatRoom).sender(chatParticipant).message(message)
                    .build();
            chatMessageRepository.save(dbChatMessage);
        } else {
            log.info("잘못된 채팅메시지 저장 시도 roomId={}, senderId={}", roomId, senderId);
        }
    }
}
