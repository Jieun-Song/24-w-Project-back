package org.project.exchange.model.list.repository;

import org.project.exchange.model.list.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListsRepository extends JpaRepository<Lists, Long> {
}


