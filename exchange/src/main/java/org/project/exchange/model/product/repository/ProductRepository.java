package org.project.exchange.model.product.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.project.exchange.model.list.Lists;
import org.project.exchange.model.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository{
    @PersistenceContext
    private EntityManager em;

    public void save(Product product){
        em.persist(product);
    }
    //상세,edit
    public Optional<Product> findById(Long product_id){
        return Optional.ofNullable(em.find(Product.class, product_id));
    }
    //list 상관없이 모든 Product
    public List<Product> findAll(){
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }
    public List<Object[]> findAllByUser(Long userId) {
        return em.createQuery(
                        "SELECT p, l.currencyFrom.id FROM Product p JOIN p.lists l WHERE l.user.userId = :userId",
                        Object[].class)
                .setParameter("userId", userId)
                .getResultList();
    }
    //list별 product
    public List<Product> findByListId(Long listId){
        return em.createQuery("SELECT p FROM Product p WHERE p.lists.id = :listId AND p.deletedYn = false", Product.class)
                .setParameter("listId", listId)
                .getResultList();
    }

    //특정 product 삭제
    public void delete(Product product){
        product.setDeletedYn(true);
    }

    //선택 product 삭제
    public void deleteByIds(List<Long> ids) {
        em.createQuery("DELETE FROM Product p WHERE p.id IN :ids")
                .setParameter("ids", ids)
                .executeUpdate();
    }

    public void deleteByListId(Long listId) {
        em.createQuery("DELETE FROM Product p WHERE p.lists.id = :listId")
                .setParameter("listId", listId)
                .executeUpdate();
    }

    public double sumOriginPrice(Long listId) {
        Double sum = (Double) em.createQuery(
                        "SELECT SUM(p.originPrice) FROM Product p WHERE p.lists.id = :listId AND p.deletedYn = false")
                .setParameter("listId", listId)
                .getSingleResult();
        return sum != null ? sum : 0.0;
    }

    public long countAllProduct() {
        Long count = (Long) em.createQuery("SELECT COUNT(i) FROM Product i")
                .getSingleResult();
        return count != null ? count : 0L;
    }

    public long countAllProductByListId(Long listId) {
        Long count = (Long) em.createQuery(
                        "SELECT COUNT(i) FROM Product i WHERE i.lists.id = :listId AND i.deletedYn = false")
                .setParameter("listId", listId)
                .getSingleResult();
        return count != null ? count : 0L;
    }

    public void deleteAllByLists(Lists lists) {
    em.createQuery("DELETE FROM Product p WHERE p.lists = :lists")
        .setParameter("lists", lists)
        .executeUpdate();
}

}
