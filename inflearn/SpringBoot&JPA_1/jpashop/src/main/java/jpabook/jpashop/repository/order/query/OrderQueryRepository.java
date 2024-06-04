package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    //====== 주문 조회 V4: JPA에서 DTO 직접 조회 ====//
    //컬렉션 별도 조회, 루프를 돌리자마자 쿼리 날림
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

    //====== 주문 조회 V5: 컬렉션 조회 최적화(JPA에서 DTO 직접 조회) ====//
    //쿼리를 한번 날리고 메모리에서 맵으로 가져와 값을 세팅 -> 쿼리 두번 나감(최적화)
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders(); //쿼리 나감

        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId()))); //루프 돌면서 메모리 맵에 올려둔 곳에다 찾아서 뽑음

        return result;

    }


    //루트 조회
    private static List<Long> toOrderIds(List<OrderQueryDto> result) { //refactoring: option+command+m
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }

    //주문 데이터 만큼 맵에 데이터 올리기
    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList(); //쿼리 나감

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }
}
