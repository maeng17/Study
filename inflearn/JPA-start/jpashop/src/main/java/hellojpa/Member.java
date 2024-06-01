package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    //임베디드 타입
    //기간
    @Embedded
    private Period workPeriod;

    //주소
    @Embedded
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city",
            column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street",
                    column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode",
                    column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

    //값타임 컬렉션
    @ElementCollection
    @CollectionTable(name="FAVORITE_FOOD", joinColumns =
        @JoinColumn(name = "MEMBER_ID"))
    @Column(name="FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name="ADDRESS", joinColumns =
//    @JoinColumn(name = "MEMBER_ID"))
//    private List<Address> addressesHistory = new ArrayList<>();

    //값타입 컬렉션 대안 - 값타입컬렉션 보다 활용 가능성 높음, 실무 사용 용이
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="MEMBER_ID")
    private List<AddressEntity> addressesHistory = new ArrayList<>();

}

/*
@AttributeOverride: 속성 재정의
 - 한 엔티티에서 같은 값 타입을 사용할 수 있게 해줌
 - @AttributeOverrides, @AttributeOverride를 사용해서 컬러 명 속성을 재정의
 */

/*
값 타입 컬렉션
 - 값 타입을 하나 이상 저장할 때 사용
 - @ElementCollection, @CollectionTable 사용
 - 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다.
 - 컬렉션을 저장하기 위한 별도의 테이블이 필요함
 */