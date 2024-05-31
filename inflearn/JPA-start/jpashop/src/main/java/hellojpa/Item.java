package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn //DTYPE table에 생성 -> 권장 / 단일테이블 전략은 없어도 자동생성(필수생성)
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
