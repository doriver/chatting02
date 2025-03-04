package com.exercise.chatting02.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 채팅
    FAIL_ROOM_CREATE(HttpStatus.INTERNAL_SERVER_ERROR, "단톡방 생성에 실패했습니다.")
    ,MENTOR_CAN_CREATE_ROOM(HttpStatus.FORBIDDEN, "멘토만이 단톡방을 생성할수 있습니다.")
    ,MENTOR_CAN_END_ROOM(HttpStatus.FORBIDDEN, "멘토만이 단톡방을 종료할수 있습니다.")
    ,ROOM_MENTOR_CAN_END(HttpStatus.FORBIDDEN, "단톡방의 멘토만이 해당방을 종료할수 있습니다.")
    ,FAIL_ENTER_ROOM(HttpStatus.INTERNAL_SERVER_ERROR, "단톡방 입장에 실패했습니다.")
    ,FAIL_EXIT_ROOM(HttpStatus.INTERNAL_SERVER_ERROR, "단톡방 퇴장에 실패했습니다.")
    ,FAIL_END_ROOM(HttpStatus.INTERNAL_SERVER_ERROR, "단톡방 종료에 실패했습니다.")
    ,ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 단톡방입니다.")

    // 유저
    ,INVALID_ACCESS_LOGIN(HttpStatus.FORBIDDEN, "로그인이 필요합니다.")
    ,FAIL_SIGN_UP(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다.")
    ,FAIL_SIGN_IN(HttpStatus.INTERNAL_SERVER_ERROR, "로그인에 실패했습니다.")

    //
    , FAIL_JSON_CONVERT(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류(json변환 실패)")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
    /*
        // 검색
    INVALID_SEARCH_FIELD(HttpStatus.BAD_REQUEST, "잘못된 검색 속성입니다.")
     */