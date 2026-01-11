package com.exercise.chatting02.chatting.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_room", columnList = "roomId")
//        , @Index(name = "", columnList = "")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EndRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long roomId;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endedAt;

    private boolean excuted;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime excutedAt;

    public EndRoom(Long roomId, LocalDateTime endedAt) {
        this.roomId = roomId;
        this.endedAt = endedAt;
        this.excuted = false;
        this.excutedAt = null;
    }

    public void stampExcuted(LocalDateTime excutedTime) {
        this.excuted = true;
        this.excutedAt = excutedTime;
    }
}
