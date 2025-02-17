package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.domain.model.ChatMessage;
import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatMessageRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
    @Autowired private ChatRoomRepository chatRoom02Repository;
    @Autowired private ChatParticipantRepository chatParticipant02Repository;
    @Autowired private ChatMessageRepository chatMessage02Repository;
    @Autowired private UserRepository userRepository;

    /*
        채팅메시지 저장하기
     */
    public void saveMessage(long roomId, long senderId, String message) {
        User user01 = userRepository.findById(senderId).orElse(null);
        ChatRoom chatRoom = chatRoom02Repository.findById(roomId).orElse(null);
        ChatParticipant chatParticipant02 = chatParticipant02Repository.findByChatterAndRoomAndExitAt(user01, chatRoom, null).orElse(null);

        if (chatParticipant02 != null && chatRoom != null) {
            ChatMessage dbChatMessage = ChatMessage.builder()
                    .room(chatRoom).sender(chatParticipant02).message(message)
                    .build();
            chatMessage02Repository.save(dbChatMessage);
        }
    }

}
