package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //생성 메서드 protected로 제약
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    //orderV1실행 시 LAZY에 의해  Member member = new ByteBuddyInterceptor();로 처리됨
    @JoinColumn(name = "member_id") //연관관계 주인
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //order 저장시 orderItems 값도 저장
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //order 저장시 delivery_id 값도 저장
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태 [ORDER, CANCEL]

    // === 연관관계 메서드 === //
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // === 생성 메서드 === //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // === 비즈니스 로직 === //
    //주문 취소 -> 변경 내역 감지해서 JSP에서 자동 업데이트 쿼리 날라감
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    // === 조회 로직 === //
    //전체 주문 가격
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;

//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
    }
}
