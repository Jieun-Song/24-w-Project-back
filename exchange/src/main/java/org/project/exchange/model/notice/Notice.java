package org.project.exchange.model.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor( access = AccessLevel.PROTECTED )
@Getter
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private Long noticeId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Builder(toBuilder = true)
    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = LocalDate.now();
    }

    public Notice update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }
}

