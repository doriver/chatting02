package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.application.messaging.RoomChangeEvent;
import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.model.EndRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.chatting.domain.repository.EndRoomRepository;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final EndRoomRepository endRoomRepository;

    private final ChatRoomService chatRoomService;
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
        if (user != null && user.getRole().name().equals("MENTOR")) {
            if (userId.equals(chatRoom.getMentor().getId())) {
                LocalDateTime endTime = LocalDateTime.now();
                EndRoom endRoom = null;
                try {
                    // 채팅방 종료에서, 비동기 작업을 관리할 데이터
                    endRoom = endRoomRepository.save(new EndRoom(roomId, endTime));

                    // 채팅방 종료시간 입력
                    chatRoom.stampEndTime(endTime);
                    chatRoomRepository.save(chatRoom);

                    // websocket연결 끊기 + 채팅방 목록view에서 방 삭제
                    roomChangeEvent.roomEnd(roomId);
                } catch (RuntimeException e) {
                    throw new ExpectedException(ErrorCode.FAIL_END_ROOM);
                }

                // 비동기 처리(나머지 작업들)
                chatRoomService.taskForEndRoom(roomId, chatRoom, endTime, endRoom);

            } else {
                throw new ExpectedException(ErrorCode.ROOM_MENTOR_CAN_END);
            }
        } else {
            throw new ExpectedException(ErrorCode.MENTOR_CAN_END_ROOM);
        }


    }
}
