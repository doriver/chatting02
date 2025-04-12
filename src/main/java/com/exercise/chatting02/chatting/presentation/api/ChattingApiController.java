package com.exercise.chatting02.chatting.presentation.api;

import com.exercise.chatting02.chatting.application.ChatParticipanceService;
import com.exercise.chatting02.chatting.application.ChatRoomService;
import com.exercise.chatting02.chatting.application.MentorService;
import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.presentation.dto.request.RoomCreateRequest;
import com.exercise.chatting02.common.annotation.CurrentUser;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.exercise.chatting02.user.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/chatting")
@RequiredArgsConstructor
public class ChattingApiController {

    private final MentorService mentorService;
    private final ChatParticipanceService chatParticipanceService;

    /*
                단톡방 입장하기
        접근범위(권한) : 로그인
        단톡방 목록에서 단톡방별로, 버튼(참여)을 눌러야만 동작하게 설계되있음
     */
    @PostMapping("/participants/rooms/{roomId}")
    public String enterRoom(@PathVariable("roomId") long roomId
                            , @CurrentUser User user) {
        chatParticipanceService.userEnterRoom(roomId, user.getId());
        return "redirect:/view/chatting/rooms/" + roomId;
    }

    /*
                채팅방 나가기
        접근범위(권한) : 로그인
        단톡방에서 버튼(나가기)을 눌러야만 동작하게 설계되있음
     */
    @PatchMapping("/participants/rooms/{roomId}")
    @ResponseBody
    public void exitRoom(@PathVariable("roomId") long roomId, @CurrentUser User user) {
        chatParticipanceService.chatterExitRoom(roomId, user.getId());
    }

    /*
                채팅방 종료
        접근범위(권한) : mentor
        단톡방에서 해당방을 개설한 멘토만 동작 가능
     */
    @PatchMapping("/rooms/{roomId}")
    @ResponseBody
    public void endRoom(@PathVariable("roomId") long roomId, @CurrentUser User user) {
        mentorService.mentorEndRoom(user.getId(), roomId);
    }

    /*
                단체 채팅방 생성
        접근범위(권한) : mentor
     */
    @PostMapping("/rooms")
    @ResponseBody
    public void createRoom(@Valid @RequestBody RoomCreateRequest roomCreateRequest
                            , @CurrentUser User user) {
        mentorService.mentorCreateRoom(user.getId()
                , roomCreateRequest.getRoomName(), roomCreateRequest.getUserLimit());
    }

}
