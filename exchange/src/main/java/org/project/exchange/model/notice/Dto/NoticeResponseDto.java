package org.project.exchange.model.notice.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.exchange.model.notice.Notice;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class NoticeResponseDto {
    private Long notice_id;
    private String title;
    private String content;
    private LocalDate date;

    public NoticeResponseDto(Notice notice) {
        this.notice_id = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.date = notice.getDate();
    }
}
