package org.project.exchange.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.list.Dto.ListsRequestDto;
import org.project.exchange.model.list.Dto.ListsResponseDto;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.list.service.ListsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ListsController {
    private final ListsService listsService;

    //새로운 리스트 추가
    @PostMapping("/add")
    public ResponseEntity<Lists> createList(@RequestBody ListsRequestDto requestDto) {
        Lists newLists = listsService.createList(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLists);
    }

    //모든 리스트 불러오기
    @GetMapping
    public ResponseEntity<List<ListsResponseDto>> getAllLists() {
        List<ListsResponseDto> lists = listsService.showAllLists();
        return ResponseEntity.ok(lists);
    }

    //특정 리스트 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        listsService.deleteList(id);
        return ResponseEntity.noContent().build();//204 No Content반환
    }

    //총금액표시
    @GetMapping("/total/{id}")
    public ResponseEntity<Double> getTotal(@PathVariable Long id) {
        double total = listsService.getTotal(id);
        return ResponseEntity.ok(total);
    }

    //환율 변경
    @PatchMapping("/currency/update/{id}")
    public ResponseEntity<Lists> updateCurrency(@PathVariable Long id, @RequestBody ListsRequestDto listsRequestDto) {
        Lists updatedLists = listsService.updateList(id, listsRequestDto);
        return ResponseEntity.ok(updatedLists);
    }
}

