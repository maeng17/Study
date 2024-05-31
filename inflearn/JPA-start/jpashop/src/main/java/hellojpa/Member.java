package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

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

    @ManyToOne //다대일 단방향. 연관관계의 주인처럼
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) //insert 업데이트 x -> 읽기 전용 매핑
    private Team team;

    @OneToOne //ManyToOne과 비슷
    @JoinColumn(name="LOCKER_ID")
    private Locker locker;

    //다대다
//    @ManyToMany
//    @JoinTable(name = "MEMBER_TABLE")
//    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();
}
