package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
XtoOne(ManyToOne, OneToOne)관계 성능최적화
 - Order
 - Order -> Member
 - Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    //간단 주문 조회: 엔티티 직접 노출 -> 지연로딩 성능 문제
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch()); //무한루프 -> 양방향 연관관계 한쪽은 jsonignore
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기환
        }
        return all;
    }

    //간단 주문 조회: 엔티티를 DTO로 변환 - 지연로딩으로 인해 쿼리가 너무 많이 호출됨(1 + N + N)
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        //ORDER 2개
        // N+1 -> 1 + 회원 N + 배송 N
        // 즉시 로딩(EARGR) 설정 -> 연관관계가 필요 없는 경우에도 데이터를 항상 조회하는 성능 문제 발생
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect((Collectors.toList()));

        return result;
    }

    //간단 주문 조회: fetch join 사용 (엔티티를 DTO로 변환) : 쿼리 한번만 호출
    // 외부의 모습을 건드리지 않고 내부에서 원하는 것만 패치 조인으로 추출 -> 재사용성 높음
    // -> select시 엔티티를 찍어서 줘야함. 셀렉트절의 양이 많음
    // 비즈니스 로직 사용 -> 데이터 변경 가능
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect((Collectors.toList()));

        return result;
    }

    //간단 주문 조회: JPA에서 DTO로 바로 조회: select 절의 양이 감소 -> 성능 최적화
    //JPQL 직접 작성 -> 리파지토리 재사용성 떨어짐
    // DTO로 조회 -> 데이터 변경 불가능(엔티티가 아니기 때문)
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
//        return orderRepository.findOrderDtos();
        return  orderSimpleQueryRepository.findOrderDtos();
    }



    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }
}
