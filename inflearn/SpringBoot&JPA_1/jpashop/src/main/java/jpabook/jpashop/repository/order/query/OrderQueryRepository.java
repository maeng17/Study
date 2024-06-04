package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    //컬렉션 별도 조회,
    // QueryL 루트1번, 컬렉션 N번
    // 단건 조회에서 많이 사용
    public List<OrderQueryDto> findOrderQueryDtos() {
        //루트 조회(toOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders(); //query 1번 -> N개

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrdersItems(o.getOrderId()); // Query N번
            o.setOrderItems(orderItems);
        });
        return result;
    }

    //1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
    public List<OrderQueryDto> findOrders(){
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o"+
                        " join o.member m"+
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }


    //1:N 관계인 orderItems 조회
    private List<OrderItemQueryDto> findOrdersItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
