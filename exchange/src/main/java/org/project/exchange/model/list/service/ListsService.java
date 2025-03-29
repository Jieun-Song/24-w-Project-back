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

import java.time.LocalDateTime;
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
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Currency currencyFrom = currencyRepository.findById(requestDto.getCurrencyIdFrom())
                .orElseThrow(() -> new IllegalArgumentException("환전이 될 통화가 존재하지 않습니다."));
        Currency currencyTo = currencyRepository.findById(requestDto.getCurrencyIdTo())
                .orElseThrow(() -> new IllegalArgumentException("환전이 되는 통화가 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();
        long listCount = listsRepository.countAllListByUser(requestDto.getUserId())+1;
        String listName = "리스트" + listCount;

        Lists newLists = new Lists(listName,now, requestDto. getLocation(), user, currencyFrom, currencyTo);

        listsRepository.save(newLists);

        return new CreateListResponseDto(newLists);
    }

    public CreateListResponseDto saveWithName(CreateListWithNameRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Currency currencyFrom = currencyRepository.findById(requestDto.getCurrencyIdFrom())
                .orElseThrow(() -> new IllegalArgumentException("환전이 될 통화가 존재하지 않습니다."));
        Currency currencyTo = currencyRepository.findById(requestDto.getCurrencyIdTo())
                .orElseThrow(() -> new IllegalArgumentException("환전이 되는 통화가 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();

        Lists newLists = new Lists(requestDto.getName() ,now, requestDto.getLocation(),
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
    public Lists updateList(Long id, UpdateRequest requestDto) {
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        lists.setName(requestDto.getName());
        lists.setLocation(requestDto.getLocation());
        lists.setCurrencyFrom(currencyRepository.findById(requestDto.getCurrencyIdFrom()));
        lists.setCurrencyTo(currencyRepository.findById(requestDto.getCurrencyIdTo()));
        return listsRepository.save(lists);
    }

    public ListsResponseDto showList(Long id) {
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        return new ListsResponseDto(lists);
    }
}
