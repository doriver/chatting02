package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.chatting.presentation.dto.message.ParticipantMessage;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatParticipanceService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate strTemplate;

    /*
        채팅방 참석자가, 해당방 나가는 기능
    */
    public void chatterExitRoom(long roomId, long chatterId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        User participant = userRepository.findById(chatterId).orElse(null);

        if (participant != null && chatRoom != null) {
            Optional<ChatParticipant> attendance = chatParticipantRepository.findByChatterAndRoomAndExitAt(participant, chatRoom, null);
            if (!attendance.isEmpty()) {
                ChatParticipant chatParticipant = attendance.get();
                chatParticipant.stampExitTime(LocalDateTime.now()); // 여기까지하면 update될줄 알았는데, 안됨
                chatParticipantRepository.save(chatParticipant);

                long chatter01Id = chatParticipant.getId();
                String chatterName = chatParticipant.getChatter().getNickname();

                ParticipantMessage participantDTO = ParticipantMessage.builder()
                        .chatterId(chatter01Id).chatterName(chatterName).access(0)
                        .build();

                // 해당 방에 퇴장 알리기
                String destination = "/chatRoom/" + roomId + "/door";
                messagingTemplate.convertAndSend(destination, participantDTO);

                // 채팅방 목록에서 현재인원 1 감소
                strTemplate.convertAndSend("participant/down", roomId+"");
            }
        }
    }

    /*
        사용자가 단톡방 입장하기
     */
    public void userEnterRoom(long rid, long uid) {
        // 일단은 단톡방으로 무조건 넘어가는 로직만 있음
        // 여러가지 경우를 고려해야할수도( 단톡방 없는경우 등 )
        ChatRoom chatRoom = chatRoomRepository.findById(rid).orElse(null);
        User participant = userRepository.findById(uid).orElse(null);
        // 로그인 여부까지 체크하면 좋음

        Optional<ChatParticipant> attendance = chatParticipantRepository.findByChatterAndRoomAndExitAt(participant, chatRoom, null);
        if (attendance.isEmpty()) {
            if (participant != null && chatRoom != null) {
                ChatParticipant chatter = ChatParticipant.builder()
                        .room(chatRoom).chatter(participant)
                        .build();
                ChatParticipant savedChatter = chatParticipantRepository.save(chatter);

                long chatterId = savedChatter.getId();
                String chatterName = savedChatter.getChatter().getNickname();

                ParticipantMessage participantDTO = ParticipantMessage.builder()
                        .chatterId(chatterId).chatterName(chatterName).access(1)
                        .build();

                // 해당 방에 입장 알리기
                messagingTemplate.convertAndSend(
                        "/chatRoom/" + rid + "/door", participantDTO);

                // 채팅방 목록에서 현재인원 1 증가
                strTemplate.convertAndSend("participant/up", rid+"");

            } else {
                // throw new Exception();
            }
        }
    }
}
