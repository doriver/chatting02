package com.exercise.chatting02.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormat {

    public String hourMinute(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시mm분");
        return ldt.format(formatter);
    }
}
