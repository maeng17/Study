package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

//    @OneToMany(mappedBy = "team") // 어떤 변수랑 연결되어 있는지, team에 의해 매핑되어짐. 읽기만 가능

    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<>();

//    public void addMember(Member member) {
//        member.setTeam(this);
//        members.add(member);
//    }
}

/*
mappedBy
객체와 테이블이 관계를 맺는 차이
• 객체 연관관계 = 2개 (각각의 참조값 필요)
 - 회원 -> 팀 연관관계 1개(단방향)
 - 팀 -> 회원 연관관계 1개(단방향)
  -> 단방향 두개를 양방향이라 부르는 것.

• 테이블 연관관계 = 1개
 - 회원 <-> 팀의 연관관계 1개(양방향)
  ->외래키 하나로 두 테이블 연관관계 관리

---
양방향 매핑 규칙
 - 객체의 두 관계중 하나를 연관관계의 주인으로 지정
 - 주인은 mappedBy 속성 사용X
 - 연관관계의 주인만이 외래 키를 관리(등록, 수정) 주인이 아닌쪽은 읽기만 가능
 - 주인이 아니면 mappedBy 속성으로 주인 지정
 - 외래키가 있는 곳을 주인으로 하기
 - 여기서는 Member.team이 연관관계의 주인
*/
