package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

// 구현체를 가지고 할것이기 때문에 추상클래스로 설정
@Entity
@Getter @Setter
// 상속관계 mapping 이기때문에 중요한 작업을 해야한다. - 상속관계 전략
// 상속관계 전략은 부모클래스에 잡아줘야한다. 여기서는 Single Table 전략
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 한 Table에 다 때려박는거
@DiscriminatorColumn(name = "dtype") // 구분하기위한 칼럼, @DiscriminatorValue("B")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items") //
    private List<Category> categories;

    //==비즈니스 로직 추가(상품 엔티티 개발)==//
    // 도메인 주도 설계, 엔티티 자체에서 해결할 수 있는 것들은 주로, 엔티티안에 비즈니스 로직을 넣는 것이 좋다
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity -= quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
