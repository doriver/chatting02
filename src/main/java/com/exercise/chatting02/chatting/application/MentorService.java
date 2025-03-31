package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.application.messaging.RoomChangeEvent;
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

    private final ChatMessageService chatMessageService;

    private final RoomChangeEvent roomChangeEvent;

    /*
        멘토가 단톡방 생성
     */
    public void mentorCreateRoom(long userId, String roomName, int userLimit) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null && user.getRole().name() == "MENTOR") {
            ChatRoom room = ChatRoom.builder()
                    .mentor(user).roomName(roomName).userLimit(userLimit)
                    .build();

            ChatRoom createdRoom = null;
            try {
                createdRoom = chatRoomRepository.save(room);
            } catch (Exception e) {
                throw new ExpectedException(ErrorCode.FAIL_ROOM_CREATE);
            }
            try {
                roomChangeEvent.roomCreation(createdRoom);
            } catch (Exception ignored) {}

        } else {
            throw new ExpectedException(ErrorCode.MENTOR_CAN_CREATE_ROOM);
        }
    }

    /*
        개설자가 채팅방 종료
     */
    @Transactional
    public void mentorEndRoom(Long userId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ExpectedException(ErrorCode.ROOM_NOT_FOUND));

        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getRole().name() == "MENTOR") {
            if (userId == chatRoom.getMentor().getId()) {
                LocalDateTime endTime = LocalDateTime.now();

                try {
                    // 채팅메시지들 RDB에 한꺼번에 저장
                    chatMessageService.saveAllMessagesRDB(roomId);

                    // 채팅방 종료시간 입력
                    chatRoom.stampEndTime(endTime);
                    chatRoomRepository.save(chatRoom);

                    // 채팅방 참석자들 나가는 시간 입력
                    List<ChatParticipant> chatterList = chatParticipantRepository.findAllByRoomAndExitAt(chatRoom, null);
                    for (ChatParticipant chatter : chatterList) {
                        chatter.stampExitTime(endTime);
                    }
                    chatParticipantRepository.saveAll(chatterList);
                } catch (Exception e) {
                    throw new ExpectedException(ErrorCode.FAIL_END_ROOM);
                }

                try {
                    roomChangeEvent.roomEnd(roomId);
                } catch (Exception ignored) {}

            } else {
                throw new ExpectedException(ErrorCode.ROOM_MENTOR_CAN_END);
            }
        } else {
            throw new ExpectedException(ErrorCode.MENTOR_CAN_END_ROOM);
        }


    }
}
