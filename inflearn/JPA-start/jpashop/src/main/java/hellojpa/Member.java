package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

//    @Column(name="TEAM_ID")
//    private Long teamId;

    //연관관계 매핑 시 : 관계, 조인컬럼 설정
//    @ManyToOne //member=many team=one
//    @JoinColumn(name = "TEAN_ID")
//    private Team team;

    //addMember or setTeam 둘중 하나 사용 -> 에러 날 수도
//    public void setTeam(Team team) {
//        this.team = team;
//
//        team.getMembers().add(this); // 자신의 instance를 넣어줌
//        //member에서 team 을 세팅할 때  team에서도 같이 세팅해줌 --> 양방향 세팅
//    }

    @ManyToOne(fetch = FetchType.LAZY) //다대일 단방향. 연관관계의 주인처럼
    //fetch = FetchType.LAZY : 멤버 클래스만 db에서 조회,
    // LAZY: 지연로딩
    // EAGER: 즉시로딩 -> 성능저하로 실무에서 사용안하는 것이 좋음(쿼리가 너무 많이 생성됨)
//    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) //insert 업데이트 x -> 읽기 전용 매핑
    @JoinColumn
    private Team team;

    @OneToOne(fetch = FetchType.LAZY) //ManyToOne과 비슷
    @JoinColumn(name="LOCKER_ID")
    private Locker locker;

    //다대다
//    @ManyToMany
//    @JoinTable(name = "MEMBER_TABLE")
//    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

}

/*
즉시로딩이
 */