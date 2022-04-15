package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;    // 회원명

    @Embedded   // 내장 타입을 포함했다는 어노테이션
    private Address address;

    @OneToMany(mappedBy = "member")  // 하나의 회원이 여러개의 오더를 할수있기때문에 @OneTomany.
    // 나는 주인이 아니에요. 조회하기 위한 거울일 뿐이에요.@mappdBy
    // "member" 뜻 : Order테이블에 있는 member필드에 의해서 mapping된거야
    // 따라서 여기에 값을 넣는다고해서 Orders테이블에 FK값이 변경되지않는다
    private List<Order> orders = new ArrayList<>();






}
