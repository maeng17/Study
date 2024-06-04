package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    //주문 조회 V1: 엔티티 직접 노출
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems(); // orderItems 강제초기화
//            for(OrderItem orderItem : orderItems) {
//                orderItem.getItem().getName(); // orderItem의 name 강제 초기화
//            }
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    //주문 조회 V2: 엔티티를 DTO로 변환 -> 컬렉션 사용 시 쿼리 더 많이 나감
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List <OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    //주문 조회 V3: 페치 조인 최적화(엔티티 -> DTO 변환) - 쿼리 한번 출려구
    //Hibernate 6이전: order_item의 수만큼 데이터가 늘어남
    //Hibernate 6: 페치 조인 사용 시 자동으로 중복 제거를 하도록 변경
    // -> 일대다 패치조인시 페이징 불가능
    //h2 애플리케이션 상에서는 중복 처리 안되서 데이터 2배 입력
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        for (Order order : orders) {
            System.out.println("order ref = " + order);
            System.out.println("order_id= " + order.getId());
        }
        List <OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    //주문 조회 V3.1: 페이징과 한계 돌파(엔티티를 DTO로 변환)
    // 쿼리 3번 출력(최적화)
    //h2 애플리케이션 상에서는 중복 없는 데이터 전송 -> 효율적
    //ToOne 관계는 페치 조인해도 페이징에 영향을 주지 않는다. 따라서 ToOne 관계는 페치조인으로 쿼리 수 를 줄이고 해결하고,
    // 나머지는 `hibernate.default_batch_fetch_size` 로 최적화
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limt", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        for (Order order : orders) {
            System.out.println("order ref = " + order);
            System.out.println("order_id= " + order.getId());
        }
        List <OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return result;
    }

    //주문 조회 V4: JPA에서 DTO 직접 조회, 쿼리 여러번 나감
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }





        @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        //private List<OrderItem> orderItems; //엔티티가 변하면 API 스펙이 변함 ->  dto변환 해야함
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
//            order.getOrderItems().stream().forEach(o -> o.getItem().getName()); //강제 초기화해야 orderItems 값 들어옴
//            orderItems = order.getOrderItems(); //단독 사용시 null -> 엔티티이기 때문
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());

        }
    }


    @Getter
    static class OrderItemDto{
        private String itemName; //상품명
        private int orderPrice; //주문 가격
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }

    }


    }
