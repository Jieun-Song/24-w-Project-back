package org.project.exchange.model.list.repository;


import org.project.exchange.model.list.Lists;
import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ListsRepository extends JpaRepository<Lists, Long>, ListsRepositoryCustom{

    @Query("SELECT COUNT(l) FROM Lists l WHERE l.user.userId = :userId AND l.deletedYn = false")
    Integer countAllListByUser(@Param("userId") Long userId);

    List<Lists> findAllByUser(User user);

    @Query("SELECT l FROM Lists l LEFT JOIN FETCH l.products WHERE l.deletedYn = false AND l.user = :user AND l.createdAt BETWEEN :startDate AND :endDate")
    List<Lists> findByUserAndCreatedAtBetween(@Param("user") User user,
                                            @Param("startDate") LocalDateTime start,
                                            @Param("endDate") LocalDateTime end);

}
