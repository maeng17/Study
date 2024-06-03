package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //상품 등록
    public void save(Item item) {
        if(item.getId() == null) {
            em.persist(item); //새상품 등록
        } else {
            //준영속 엔티티 수정 방법 - 1) 병합(merge) 사용
            em.merge(item); // 수정 -> 강제 업데이트
        }
    }

    //한개 상품 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    //전체 상품 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
