package com.exercise.chatting02.chatting.presentation.view;


import com.exercise.chatting02.chatting.application.ChatRoomService;
import com.exercise.chatting02.chatting.domain.model.ChatParticipant;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.domain.repository.ChatParticipantRepository;
import com.exercise.chatting02.chatting.domain.repository.ChatRoomRepository;
import com.exercise.chatting02.chatting.presentation.dto.response.ChatRoomInfoResponse;
import com.exercise.chatting02.common.annotation.CurrentUser;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.exercise.chatting02.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/view/chatting")
@RequiredArgsConstructor
public class ChattingViewController {

    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    @Value("${server.port}") private String serverPort;

    /*
        단톡방 목록 화면
        접근범위(권한) : all
     */
    @GetMapping("/list")
    public String chatListView(Model model, @CurrentUser User user) {
        List<ChatRoomInfoResponse> chatRoomList = chatRoomService.getChatRoomListView();
        model.addAttribute("chatRoomList", chatRoomList);
        if (user != null) {
            Long userId = user.getId();
            model.addAttribute("userId", userId);
        }
        return "chatting/chatList";
    }

    /*
        단통방 화면
        접근범위(권한) : 로그인
        @PostMapping("/participant/{rid}") enterRoom() 에서만 접근가능 하도록 설계됨
     */
    @GetMapping("/rooms/{roomId}")
    public String chatView(@PathVariable("roomId") long roomId, @CurrentUser User user, Model model) {
        chatRoomService.roomViewSetting(roomId, model);

        model.addAttribute("userId", user.getId());
        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("serverPort", serverPort);

        return "chatting/chatRoom";
    }

}
