package org.project.exchange.model.list.repository;

import org.project.exchange.model.list.Lists;
import org.project.exchange.model.user.User;

import java.util.List;

public interface ListsRepositoryCustom {
    long countAllList();

    List<Lists> findAllByUserId(User user);
}
