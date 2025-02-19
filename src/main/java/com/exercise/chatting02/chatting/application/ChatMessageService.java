package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.domain.model.ChatMessage;
import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatMessageRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService {
    @Autowired private ChatRoomRepository chatRoomRepository;
    @Autowired private ChatParticipantRepository chatParticipantRepository;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private UserRepository userRepository;

    /*
        채팅메시지 저장하기
     */
    public void saveMessage(long roomId, long senderId, String message) {
        User user = userRepository.findById(senderId).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatterAndRoomAndExitAt(user, chatRoom, null).orElse(null);

        if (chatParticipant != null && chatRoom != null) {
            ChatMessage dbChatMessage = ChatMessage.builder()
                    .room(chatRoom).sender(chatParticipant).message(message)
                    .build();
            chatMessageRepository.save(dbChatMessage);
        } else {
//            throw new ExpectedException(ErrorCode.);
        }
    }
}
