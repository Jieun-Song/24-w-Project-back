package org.project.exchange.model.list.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ListsRepositoryImpl implements ListsRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long countAllList() {
            return (long) em.createQuery("SELECT COUNT(i) FROM Lists i")
                    .getSingleResult();

    }
}


