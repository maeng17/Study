package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    @ManyToOne //member=many team=one
    @JoinColumn(name = "TEAN_ID")
    private Team team;

    //addMember or setTeam 둘중 하나 사용 -> 에러 날 수도
//    public void setTeam(Team team) {
//        this.team = team;
//
//        team.getMembers().add(this); // 자신의 instance를 넣어줌
//        //member에서 team 을 세팅할 때  team에서도 같이 세팅해줌 --> 양방향 세팅
//    }
}
