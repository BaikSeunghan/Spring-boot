package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order로 해버리면 order by와 겹치기때문에 orders로 설정한다.
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계이기때문에 Order(Many), Member(One)
    @JoinColumn(name = "member_id") // mapping을 뭘로할것인지 설정해준다, FK이름이 member_id이다
    // 양방향 연관관계일 경우, 연관관계의 주인을 정해야 한다. 주로 외래키가 있는 쪽으로 정한다.
    // Order.member가 주인이기때문에 그대로 두면된다. 설정할 곳은 Member쪽
    private Member member;

    // cascade 설정: Order가 persist가 되면 List안에 들어가있는 orderItems까지
    // 한번에 persist 강제실행
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)   // 1:1 관계인경우 어디에 FK를 둬고 상관은 없다. 영한님 같은 경우는
    // cascade 하면 delivery까지 한번에 persist실행
    // Access의 비중이 높은 곳에 둔다. 때문에 이번 프로젝트에서는
    // Delivery보다 Order에 비중이 높기때문에 Order에 둔다.
    // 객체끼리 서로 갖고있기 때문에 연관관계 주인을 설정해줘야한다. 주인 설정은 FK와 가까이있는 곳에 둔다.
    // PK를 Order에 두었기때문에 Order에 설정한다.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간, 시간&분 다 나온다

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // == 연관관계 편의 메서드 == // : 양뱡향일때 사용하면 좋다.
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

    //==생성 메서드==//  - 복잡한 생성보다는 생성 메서드가 있는게 편하다
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비지니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {  // 배송이 이미 되버린 경우
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==// - 계산이 필요할 때
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
