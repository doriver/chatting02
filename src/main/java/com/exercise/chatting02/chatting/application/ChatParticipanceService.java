package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.application.messaging.ParticipantChangeEvent;
import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.chatting.presentation.dto.message.ParticipantMessage;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatParticipanceService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    private final ParticipantChangeEvent participantChangeEvent;


    /*
        채팅방 참석자가, 해당방 나가는 기능
    */
    public void chatterExitRoom(long roomId, long chatterId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ExpectedException(ErrorCode.ROOM_NOT_FOUND));
        User participant = userRepository.findById(chatterId).orElse(null);

        if (participant != null && chatRoom != null) {
            ChatParticipant chatParticipant = chatParticipantRepository.findByChatterAndRoomAndExitAt(participant, chatRoom, null).orElse(null);
            if (chatParticipant != null) {
                chatParticipant.stampExitTime(LocalDateTime.now());
                try {
                    chatParticipantRepository.save(chatParticipant);
                } catch (Exception e) {
                    throw new ExpectedException(ErrorCode.FAIL_EXIT_ROOM);
                }

                try {
                    participantChangeEvent.inAndOut(0, roomId
                            , chatParticipant.getId(), chatParticipant.getChatter().getNickname());
                } catch (Exception ignored) {   }
            }
        }
    }

    /*
        사용자가 단톡방 입장하기
     */
    public void userEnterRoom(long rid, long uid) {
        ChatRoom chatRoom = chatRoomRepository.findById(rid)
                .orElseThrow(() -> new ExpectedException(ErrorCode.ROOM_NOT_FOUND));

        User participant = userRepository.findById(uid).orElse(null);
        Optional<ChatParticipant> attendance = chatParticipantRepository.findByChatterAndRoomAndExitAt(participant, chatRoom, null);
        // 이미 참석해 있는경우는 변화x, 새롭게 참석하는 경우만 입장 로직 실행
        if (attendance.isEmpty()) {
            ChatParticipant chatter = ChatParticipant.builder()
                    .room(chatRoom).chatter(participant)
                    .build();

            ChatParticipant savedChatter = null;
            try {
                savedChatter = chatParticipantRepository.save(chatter);
            } catch (Exception e) {
                throw new ExpectedException(ErrorCode.FAIL_ENTER_ROOM);
            }
            try {
                participantChangeEvent.inAndOut(1, rid
                        , savedChatter.getId(), savedChatter.getChatter().getNickname());
            } catch (Exception ignored) {     }
        }
    }
}
