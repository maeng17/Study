package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id @GeneratedValue
    private Long id;

    private String name;

//    @ManyToMany(mappedBy = "products")
//    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();
}


/*
다대다 매핑
 - 편리해 보이지만 실무에서 사용 불가능
 - 이상한 쿼리가 추가적으로 발생
 - 사용안하는 것이 좋음

 => 연결 테이블용 엔티티추가(연결테이블을 엔티티로)
    OneToMany & ManyToOne 으로 바꾸기

*/