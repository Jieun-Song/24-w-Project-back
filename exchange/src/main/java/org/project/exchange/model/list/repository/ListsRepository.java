package org.project.exchange.model.list.repository;


import org.project.exchange.model.list.Lists;
import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListsRepository extends JpaRepository<Lists, Long>, ListsRepositoryCustom{

    @Query("SELECT COUNT(l) FROM Lists l WHERE l.user.userId = :userId")
    Integer countAllListByUser(@Param("userId") Long userId);
}
