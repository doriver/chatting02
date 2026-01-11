package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.model.EndRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.chatting.domain.repository.EndRoomRepository;
import com.exercise.chatting02.chatting.presentation.dto.response.ChatRoomInfoResponse;
import com.exercise.chatting02.chatting.presentation.dto.response.ParticipantDTO;
import com.exercise.chatting02.chatting.presentation.dto.response.RoomViewResponse;
import com.exercise.chatting02.common.utils.ToDto;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;


import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final EndRoomRepository endRoomRepository;

    private final ChatMessageService chatMessageService;
    private final ToDto toDto;

    /*
        채팅방 목록화면, 채팅방List
     */
    public List<ChatRoomInfoResponse> getChatRoomListView() {

        List<ChatRoom> chatRoomEntityList = chatRoomRepository.findAllByEndedAt(null);
        List<ChatRoomInfoResponse> chatRoomList = new LinkedList<>();

        for (ChatRoom chatRoomEntity :chatRoomEntityList) {
            chatRoomList.add(
                    toDto.chatRoomEntityToDto(chatRoomEntity));
        }
        return chatRoomList;
    }

    public void roomViewSetting(Long roomId, Model model) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ExpectedException(ErrorCode.ROOM_NOT_FOUND));

        List<ChatParticipant> chatParticipantList = chatParticipantRepository.findAllByRoomAndExitAt(room, null);
        List<ParticipantDTO> participantList = new LinkedList<>();
        for (ChatParticipant participant: chatParticipantList) {
            participantList.add(
                    toDto.chatParticipantEntityToDto(participant));
        }
        RoomViewResponse roomViewResponse = toDto.chatRoomAndParticipantsToDto(room, participantList);

        model.addAttribute("roomView", roomViewResponse);
//        model.addAttribute("chatterList", chatterList);
//        model.addAttribute("room", room.get());

    }

    @Async("roomEndTaskExecutor")
    @Transactional
    public void taskForEndRoom(long roomId, ChatRoom chatRoom, LocalDateTime endTime, EndRoom endRoom) {

        // 채팅메시지들 RDB에 한꺼번에 저장
        chatMessageService.saveAllMessagesRDB(roomId);

        // 채팅방 참석자들 나가는 시간 입력
        List<ChatParticipant> chatterList = chatParticipantRepository.findAllByRoomAndExitAt(chatRoom, null);
        for (ChatParticipant chatter : chatterList) {
            chatter.stampExitTime(endTime);
        }
        chatParticipantRepository.saveAll(chatterList);

        // 비동기쪽에서 데이터가 제대로 저장됐음을 알리는 역할
        endRoom.stampExcuted(LocalDateTime.now());
        endRoomRepository.save(endRoom);
    }



}
