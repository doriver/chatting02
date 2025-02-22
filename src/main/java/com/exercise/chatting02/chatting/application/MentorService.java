package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.chatting.presentation.dto.response.ChatRoomInfoResponse;
import com.exercise.chatting02.common.ToDto;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ToDto toDto;

    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate strTemplate;

    /*
        멘토가 단톡방 생성
     */
    @Transactional
    public void mentorCreateRoom(long userId, String roomName, int userLimit) {

        User user = userRepository.findById(userId).orElse(null);

        if (user != null && user.getRole().name() == "MENTOR") {
            ChatRoom room = ChatRoom.builder()
                    .mentor(user).roomName(roomName).userLimit(userLimit)
                    .build();
            ChatRoom createdRoom = chatRoomRepository.save(room);

            ChatRoomInfoResponse chatRoomInfoResponse = toDto.chatRoomEntityToDto(createdRoom);
            // 채팅방 목록 화면에 생성된 채팅방 추가
            String message = convertToJson(chatRoomInfoResponse);
            strTemplate.convertAndSend("room/creation", message);
        } else {
            throw new ExpectedException(ErrorCode.MENTOR_CAN_CREATE_ROOM);
        }
    }

    private String convertToJson(ChatRoomInfoResponse chatRoomInfoResponse) {
        try {
            return new ObjectMapper().writeValueAsString(chatRoomInfoResponse);
        } catch (Exception e) {
            throw new ExpectedException(ErrorCode.FAIL_JSON_CONVERT);
        }
    }

    /*
        개설자가 채팅방 종료
     */
    public void mentorEndRoom(Long userId, Long roomId) {
        if (roomId != null) {
            ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
            if (userId == chatRoom.getMentor().getId()) {
                LocalDateTime endTime = LocalDateTime.now();

                // 채팅방 종료시간 입력
                chatRoom.stampEndTime(endTime);
                chatRoomRepository.save(chatRoom);

                // 채팅방 참석자들 나가는 시간 입력
                List<ChatParticipant> chatterList = chatParticipantRepository.findAllByRoomAndExitAt(chatRoom, null);
                for (ChatParticipant chatter : chatterList) {
                    chatter.stampExitTime(endTime);
                }
                chatParticipantRepository.saveAll(chatterList);

                // 해당 방 종료 알리기( 채팅방에 연결된 웹소켓 통신 종료시키기 )
                String destination = "/chatRoom/" + roomId + "/door";
                messagingTemplate.convertAndSend(destination, "DISCONNECT");

                // 채팅방 목록에서 해당방 삭제
                strTemplate.convertAndSend("room/end", roomId+"");
            }
        }
    }
}
