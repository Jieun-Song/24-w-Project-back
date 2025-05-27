package org.project.exchange.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.global.api.ApiResponse;
import org.project.exchange.model.list.Dto.*;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.list.service.ListsService;
import org.project.exchange.model.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ListsController {
    private final ListsService listsService;

    //새로운 리스트 추가(로그인한 상태에서)
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CreateListResponseDto>> createList(@RequestBody CreateListRequestDto requestDto) {
        log.info("📥 컨트롤러 createList 호출됨: userId={}, currencyFrom={}, currencyTo={}, location={}",  requestDto.getUserId(), requestDto.getCurrencyIdFrom(), requestDto.getCurrencyIdTo(), requestDto.getLocation());
        Long userId = getCurrentUserId();
        requestDto.setUserId(userId);
        CreateListResponseDto newLists = listsService.createList(requestDto);
        log.info("📥 컨트롤러 createList 완료: 새 리스트 ID={}", newLists.getListId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessWithMessage(newLists, "리스트 추가 성공"));
    }

    //새로운 리스트 추가 + 이름
    @PostMapping("/add/name")
    public ResponseEntity<ApiResponse<CreateListResponseDto>> createListWithName(@RequestBody CreateListWithNameRequestDto requestDto) {

        CreateListResponseDto newLists = listsService.saveWithName(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessWithMessage(newLists, "리스트 추가 성공"));
    }

    // 현재 로그인한 사용자의 모든 리스트 조회 (JWT 인증 필수)
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ListsResponseDto>>> getAllLists() {
        Long userId = getCurrentUserId(); // 현재 로그인한 사용자 ID 가져오기
        List<ListsResponseDto> lists = listsService.showAllLists(userId);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(lists, "리스트 조회 성공"));
    }

    //특정 리스트 불러오기
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ListsResponseDto>> getLists(@PathVariable("id") Long id) {
        ListsResponseDto lists = listsService.showList(id);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(lists, "리스트 조회 성공"));
    }

    //특정 리스트 삭제
    @PatchMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteList(@PathVariable("id") Long id) {
        listsService.deleteList(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.createSuccessWithMessage(null, "리스트 삭제 성공"));
    }

    //총금액표시
    @GetMapping("/total/{id}")
    public ResponseEntity<ApiResponse<Double>> getTotal(@PathVariable("id") Long id) {
        double total = listsService.getTotal(id);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(total, "총금액 조회 성공"));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<UpdateResponse>> updateList(@RequestBody UpdateRequest requestDto) {
        UpdateResponse updatedLists = listsService.updateList(requestDto);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(updatedLists, "리스트 변경 성공"));
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("유효한 인증 정보가 없습니다.");
        }
        Object principal = authentication.getPrincipal();

        log.info("Principal Type: {}", principal.getClass().getName());
        log.info("Principal Value: {}", principal);

        if (principal instanceof UserDetails userDetails) {
            log.info("Extracted userId (from UserDetails): {}", userDetails.getUsername());
            return Long.parseLong(userDetails.getUsername()); // 예: "1"
        }

        if (principal instanceof String) {
            try {
                log.info("Extracted userId (from String): {}", principal);
                return Long.parseLong((String) principal); // 예: "1"
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("유효하지 않은 사용자 ID 형식: " + principal);
            }
        }

        throw new IllegalArgumentException("알 수 없는 인증 정보 타입: " + principal.getClass().getName());
    }

    // 현재 로그인한 사용자의 해당 날짜 별 리스트 조회
    @GetMapping("/date")
    public ResponseEntity<ApiResponse<List<ListWithProductsDto>>> getListsByDate(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        Long userId = getCurrentUserId();
        List<ListWithProductsDto> lists = listsService.getListsByDate(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(lists, "리스트 조회 성공"));
    }

    // 특정 날짜에 해당하는 리스트 조회 (예: 2025-05-20 하루만 조회)
    @GetMapping("/date/one")
    public ResponseEntity<ApiResponse<List<ListWithProductsDto>>> getListsBySingleDate(
            @RequestParam("date") String date) {
        Long userId = getCurrentUserId();
        // 하루만 조회하므로 startDate == endDate
        List<ListWithProductsDto> lists = listsService.getListsByDate(userId, date, date);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(lists, "특정 날짜 리스트 조회 성공"));
    }

}

