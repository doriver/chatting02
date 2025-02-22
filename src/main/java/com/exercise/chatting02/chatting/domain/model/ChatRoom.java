package com.exercise.chatting02.chatting.domain.model;

import com.exercise.chatting02.user.domain.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 단톡방 개설자
    @ManyToOne
    @JoinColumn(name = "mento_id")
    @NotNull
    private User mentor;

    // 단톡방 이름
    @NotNull
    private String roomName;

    // 참여 제한 인원
    private int userLimit;

    // 생성 시간
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime createdAt;

    // 종료 시간
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endedAt;

    @Builder
    public ChatRoom(User mentor, String roomName, int userLimit, LocalDateTime endedAt) {
        this.mentor = mentor;
        this.roomName = roomName;
        this.userLimit = userLimit;
        this.endedAt = endedAt;
    }

    public void stampEndTime(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
