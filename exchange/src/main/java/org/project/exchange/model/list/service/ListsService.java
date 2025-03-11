package org.project.exchange.model.list.service;

import lombok.RequiredArgsConstructor;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.currency.repository.CurrencyRepository;
import org.project.exchange.model.list.Dto.CreateRequest;
import org.project.exchange.model.list.Dto.ListsResponseDto;
import org.project.exchange.model.list.Dto.UpdateRequest;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ListsService {
    private final ListsRepository listsRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final ProductRepository productRepository;
    public List<ListsResponseDto> showAllLists() {
        return listsRepository.findAll()
                .stream()
                .map(lists -> new ListsResponseDto(lists))
                .collect(Collectors.toList());
    }
    public Lists createList(CreateRequest requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Currency currency = currencyRepository.findById(requestDto.getCurrencyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 통화가 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();
        long listCount = listsRepository.countAllList()+1;
        String listName = "리스트" + listCount;

        Lists newLists = new Lists(listName,now, requestDto.getLocation(), user, currency);

        return listsRepository.save(newLists);
    }
    public void deleteList(Long id) {
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        lists.setDeletedYn(true);
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
        lists.setCurrency(currencyRepository.findById(requestDto.getCurrencyId()));
        return listsRepository.save(lists);
    }
}
