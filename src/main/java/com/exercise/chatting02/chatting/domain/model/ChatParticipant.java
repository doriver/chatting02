package com.exercise.chatting02.chatting.domain.model;

import com.exercise.chatting02.user.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 소속된 단톡방
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    // 참석자 이름
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User chatter;

    // 입장시간
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false)
    private LocalDateTime enterAt;

    // 퇴장시간
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime exitAt;

    @Builder
    public ChatParticipant(ChatRoom room, User chatter, LocalDateTime exitAt) {
        this.room = room;
        this.chatter = chatter;
        this.exitAt = exitAt;
    }
}
