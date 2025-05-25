package org.project.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.project.exchange.global.api.ApiResponse;
import org.project.exchange.model.notice.Dto.NoticeRequestDto;
import org.project.exchange.model.notice.Dto.NoticeResponseDto;
import org.project.exchange.model.notice.service.NoticeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<ApiResponse<NoticeResponseDto>> createNotice(@RequestBody NoticeRequestDto noticeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessWithMessage(noticeService.createNotice(noticeRequestDto), "공지사항 생성 성공"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NoticeResponseDto>>> getAllNotice() {
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(noticeService.getAllNotice(), "공지사항 조회 성공"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> updateNotice(@PathVariable("id") Long id, @RequestBody NoticeRequestDto noticeRequestDto) {
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(noticeService.updateNotice(id, noticeRequestDto), "공지사항 수정 성공"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable("id") Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

}
