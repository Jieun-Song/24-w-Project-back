package org.project.exchange.model.list.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ListsRepositoryImpl implements ListsRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long countAllList() {
            return (long) em.createQuery("SELECT COUNT(i) FROM Lists i")
                    .getSingleResult();

    }

    @Override
    public List<Lists> findAllByUserId(User user) {
        return em.createQuery("SELECT i FROM Lists i WHERE i.user = :user", Lists.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public void deleteAllByUser(User user) {
        List<Lists> lists = findAllByUserId(user);
        for (Lists list : lists) {
            em.remove(em.contains(list) ? list : em.merge(list));
        }
    }

}


