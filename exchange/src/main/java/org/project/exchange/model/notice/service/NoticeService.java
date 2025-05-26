package org.project.exchange.model.notice.service;

import lombok.RequiredArgsConstructor;
import org.project.exchange.model.notice.Dto.NoticeRequestDto;
import org.project.exchange.model.notice.Dto.NoticeResponseDto;
import org.project.exchange.model.notice.Notice;
import org.project.exchange.model.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    public NoticeResponseDto createNotice(NoticeRequestDto noticeRequestDto) {
         Notice newNotice = new Notice(noticeRequestDto.title, noticeRequestDto.content);
         noticeRepository.save(newNotice);
         return new NoticeResponseDto(newNotice);
    }

    public List<NoticeResponseDto> getAllNotice() {
        List<NoticeResponseDto> notices = noticeRepository.findAll()
                .stream()
                .map(NoticeResponseDto::new)
                .collect((Collectors.toList()));
        return notices;
    }

    public NoticeResponseDto updateNotice(Long id, NoticeRequestDto noticeRequestDto) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
        Notice updatedNotice = notice.update(noticeRequestDto.title, noticeRequestDto.content);
        return new NoticeResponseDto(updatedNotice);
    }


    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
        noticeRepository.delete(notice);
    }
}
