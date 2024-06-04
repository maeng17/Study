package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime ordereDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime ordereDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.ordereDate = ordereDate;
        this.orderStatus = orderStatus;
        this.address = address;

    }
}
