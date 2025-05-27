package org.project.exchange.model.list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.currency.repository.CurrencyRepository;
import org.project.exchange.model.list.Dto.*;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.list.repository.ListsRepository;
import org.project.exchange.model.product.repository.ProductRepository;
import org.project.exchange.model.user.User;
import org.project.exchange.model.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ListsService {
    private final ListsRepository listsRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final ProductRepository productRepository;

    public List<ListsResponseDto> showAllLists(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        return listsRepository.findAllByUserId(user)
                .stream()
                .map(ListsResponseDto::new)
                .collect(Collectors.toList());
    }
    public CreateListResponseDto createList(CreateListRequestDto requestDto) {
        log.debug("📥 createList() 호출됨");
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Currency currencyFrom = currencyRepository.findById(requestDto.getCurrencyIdFrom())
                .orElseThrow(() -> new IllegalArgumentException("환전이 될 통화가 존재하지 않습니다."));
        Currency currencyTo = currencyRepository.findById(requestDto.getCurrencyIdTo())
                .orElseThrow(() -> new IllegalArgumentException("환전이 되는 통화가 존재하지 않습니다."));

        LocalDateTime createdAt = LocalDateTime.now();
        long listCount = listsRepository.countAllListByUser(requestDto.getUserId())+1;
        String listName = "리스트" + listCount;

        Lists newLists = new Lists(listName, createdAt, requestDto.getLocation(), user, currencyFrom, currencyTo);
        log.debug("💾서비스 리스트 객체 생성: name={}, createdAt={}, user={}, from={}, to={}", listName, createdAt, user.getUserId(), currencyFrom, currencyTo);
        listsRepository.save(newLists);
        log.debug("✅ 리스트 저장 완료, ID={}", newLists.getListId());
        return new CreateListResponseDto(newLists);
    }

    public CreateListResponseDto saveWithName(CreateListWithNameRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Currency currencyFrom = currencyRepository.findById(requestDto.getCurrencyIdFrom())
                .orElseThrow(() -> new IllegalArgumentException("환전이 될 통화가 존재하지 않습니다."));
        Currency currencyTo = currencyRepository.findById(requestDto.getCurrencyIdTo())
                .orElseThrow(() -> new IllegalArgumentException("환전이 되는 통화가 존재하지 않습니다."));

        LocalDateTime createdAt = LocalDateTime.now();

        Lists newLists = new Lists(requestDto.getName() ,createdAt, requestDto.getLocation(),
                user, currencyFrom, currencyTo);

        listsRepository.save(newLists);

        return new CreateListResponseDto(newLists);
    }

    public void deleteList(Long id) {
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        lists.setDeletedYn(true);
        listsRepository.save(lists);
    }

    public double getTotal(Long id) {
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        return productRepository.sumOriginPrice(id);
    }

    //List수정
    public UpdateResponse updateList(UpdateRequest requestDto) {
        Lists lists = listsRepository.findById(requestDto.getListId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        lists.setName(requestDto.getName());
        lists.setLocation(requestDto.getLocation());
        lists.setCurrencyFrom(currencyRepository.findById(requestDto.getCurrencyIdFrom()));
        lists.setCurrencyTo(currencyRepository.findById(requestDto.getCurrencyIdTo()));
        listsRepository.save(lists);
        return new UpdateResponse(lists);
    }

    public ListsResponseDto showList(Long id) {
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        return new ListsResponseDto(lists);
    }

    // 유저의 해당 날짜에 해당하는 리스트를 가져오는 메서드
    public List<ListWithProductsDto> getListsByDate(Long userId, String startDate, String endDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);

        return listsRepository.findByUserAndCreatedAtBetween(user, start, end)
                .stream()
                .map(ListWithProductsDto::new)
                .collect(Collectors.toList());
        }

}
