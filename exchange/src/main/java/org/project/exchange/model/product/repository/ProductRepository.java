package org.project.exchange.model.product.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    //list별 product
    public List<Product> findByListId(Long listId){
        return em.createQuery("SELECT p FROM Product p WHERE p.lists.id = :listId", Product.class)
                .setParameter("listId", listId)
                .getResultList();
    }

    //특정 product 삭제
    public void delete(Product product){
        em.remove(product);
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
        return (double) em.createQuery("SELECT SUM(p.originPrice) FROM Product p WHERE p.lists.id = :listId")
                .setParameter("listId", listId)
                .getSingleResult();
    }

    public long countAllProduct() {
        return (long) em.createQuery("SELECT COUNT(i) FROM Product i")
                .getSingleResult();
    }

    public long countAllProductByListId(Long listId) {
        return (long) em.createQuery("SELECT COUNT(i) FROM Product i WHERE i.lists.listId = :listId")
                .setParameter("listId", listId)
                .getSingleResult();
    }
}
