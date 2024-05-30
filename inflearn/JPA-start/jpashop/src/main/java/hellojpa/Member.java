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
}
