package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.chatting.presentation.dto.response.ChatRoomInfoResponse;
import com.exercise.chatting02.chatting.presentation.dto.response.ParticipantDTO;
import com.exercise.chatting02.chatting.presentation.dto.response.RoomViewResponse;
import com.exercise.chatting02.common.ToDto;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ToDto toDto;

    /*
        아직 종료안된 단체톡방들 select
     */
    public List<ChatRoom> getChatRoomList() {
        List<ChatRoom> roomList = chatRoomRepository.findAllByEndedAt(null);
        return roomList;
    }

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



}
