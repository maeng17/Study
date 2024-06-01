package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    //orphanRemoval = true 컬렉션에서 빠지면 삭제됨
    private List<Child> childList = new ArrayList<>();

    //양방향 연관관계 생성
    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }
}

/*
영속성 전이: CASCADE
 - 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들도 싶을 때
 - ex) 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장.

CASCADE의 종류
• ALL: 모두 적용
• PERSIST: 영속
• REMOVE: 삭제
• MERGE: 병합
• REFRESH: REFRESH
• DETACH: DETACH


하나의 엔티티만 연관되어 있을 떄 사용
하나의 소유자가 있을 때 사용
그러지 않으면 운영이 힘들어짐
단일 엔티티에만 종속적일 떄 사용

-------------------------
고아 객체
• 고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티
를 자동으로 삭제
• orphanRemoval = true

 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
• 참조하는 곳이 하나일 때 사용해야함!
• 특정 엔티티가 개인 소유할 때 사용
• @OneToOne, @OneToMany만 가능
• 참고: 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화 하면, 부모를 제거할 때 자식도 함께 제거된다.
        이것은 CascadeType.REMOVE처럼 동작한다.


영속성 전이 + 고아 객체, 생명주기
• CascadeType.ALL + orphanRemoval=true
• 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화,
    em.remove()로 제거
• 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
• 도메인 주도 설계(DDD)의 Aggregate Root개념을 구현할 때 유용
 */