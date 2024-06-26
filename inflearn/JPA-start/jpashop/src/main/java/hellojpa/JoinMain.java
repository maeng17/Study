package hellojpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JoinMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //연관관계 매핑 기초
//            //팀 저장
//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            //회원 저장
//            Member member = new Member();
//            member.setName("member1");
//            //team.getMembers().add(member); //단독 사용시, TEAM_ID=null, 연관관계의 주인이 Member.team이기 때문
//            member.setTeam(team);
//            em.persist(member);
//
//            team.addMember(member);
//            // -> addMember or setTeam 둘중 하나 사용 -> 에러 날 수도
//
//            //영속성 컨텍스트 아닌 DB에서 가져오는 쿼리 보고싶을 때 -> db에서 깔끔하게 값 가져옴
//            em.flush();
//            em.clear();

//            Member findMember = em.find(Member.class, member.getId());

//            Team findTeam = findMember.getTeam();
//            System.out.println("findTeam: " + findTeam.getName());

//            List<Member> members = findMember.getTeam().getMembers();
//            for(Member m : members) {
//                System.out.println("m = " +m.getName());
//            }
//
//            System.out.println("==============");
//            Team findTeam = em.find(Team.class, team.getId()); //1차 캐시 -> 영속성 컨텍스트에 들어가 있음
//            List<Member> members = findTeam.getMembers();
//            for(Member m : members) {
//                System.out.println("m = " +m.getName()); // 로딩된 1차캐시 출력
//            }
//            System.out.println("==============");

//            //다양한 연관관계 매핑
//            //멤버 저장
//            Member member = new Member();
//            member.setName("member1");
//            em.persist(member);
//
//            Team team = new Team();
//            team.setName("teamA");
//            //
//            team.getMembers().add(member);
//
//            em.persist(team);

//            //고급 매핑(상속관계)
//            Movie movie = new Movie();
//            movie.setDirecctor("A");
//            movie.setActor("B");
//            movie.setName("바람과함꼐뭐뭐뫄");
//            movie.setPrice(10000);
//
//            em.persist(movie);
//
//            em.flush(); //영속성 컨텍스트 날리고
//            em.clear(); //영속성 컨텍스트 깔끔하게
//
//            Movie findMovie = em.find(Movie.class, movie.getId()); //innerjoin으로 알아서 가지고 옴
//            System.out.println("findMovie: " + findMovie);

//            //고급매핑(@MappedSuperclass)
//            Member member = new Member();
//            member.setName("user");
//            member.setCreateBy("kim");
//            member.setCreateDate(LocalDateTime.now());
//
//            em.persist(member);
//
//            em.flush(); //영속성 컨텍스트 날리고
//            em.clear(); //영속성 컨텍스트 깔끔하게

//            //프록시
//            Member member1 = new Member();
//            member1.setName("hello1");
//            em.persist(member1);
//
//            Member member2 = new Member();
//            member2.setName("hello2");
//            em.persist(member2);
//
//            em.flush();
//            em.clear();

////            Member findMember = em.find(Member.class, member.getId());
//            Member findMember = em.getReference(Member.class, member.getId()); //값이 필요할 떄 쿼리 출력
//            System.out.println("findMember = " + findMember.getClass()); //Hibernate가 가짜로 만든 클래스 -> Proxy class
//            System.out.println("find member.id: " + findMember.getId());
//            System.out.println("find member.name: " + findMember.getName());
//            // 값요청 -> 영속성 콘텍스트에 초기화 요청 -> db 조회 -> 실제 entity 생성하여 값 가져옴
//            System.out.println("find member.name: " + findMember.getName()); // 두번째 요청시 이미 생성 했으므로 값만 가져옴

//            Member m1 = em.find(Member.class, member1.getId());
////            Member m2 = em.find(Member.class, member2.getId()); //true
//            Member m2 = em.getReference(Member.class, member2.getId()); //false
//            System.out.println("m2 = " + m2.getClass());

//            System.out.println("m1 == m2 : " + (m1.getClass() == m2.getClass()));
//
//            logic(m1, m2);

//            Member reference = em.getReference(Member.class, member1.getId());
//            System.out.println("m1 = " + m1.getClass());
//            System.out.println("reference = " + reference.getClass());
//            System.out.println("m1 == reference : " + (m1 == reference)); // 영속성 컨텍스트에 이미 있으면 실제 entity 반환. PK가 똑같으면 항상 true 반환
//
//            Member findMember = em.find(Member.class, member2.getId());
//            System.out.println("m2 = " + m2.getClass());
//            System.out.println("findMember = " + findMember.getClass()); //proxy로 조회 -> 프록시가 한번 조회되면 em.find에서도 프록시 반환  (m2 == findMember)가 무조건 true가 나와야 하기 때문
//
//            System.out.println("m2 == findMember : " + (m2 == findMember));

//            //영속성 컨텍스트 끌 경우
//            //em.detach(m2); //영속성 컨텍스트 없어서 noSession 에러
//            em.close();
//            //em.clear(); // 영속성 컨텍스트 새로 만들어져서 noSession 에러
//            System.out.println("m2 = " + m2.getName());

//            //프록시 확인
//            //초기화 여부 확인
//            m2.getName(); //프록시 초기화 시 true
//            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(m2)); //초기화 안되있으면 false
//
//            //클래스 확인
//            Member refMember = em.getReference(Member.class, member1.getId());
//            System.out.println("refMember = " + refMember.getClass());
//
//
//            // 프록시 강제 초기화
//            Hibernate.initialize(refMember);
//            refMember.getName(); //강제 초기화

//            //지연 로딩
//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Team teamB = new Team();
//            teamB.setName("teamB");
//            em.persist(teamB);
//
//            Member member1 = new Member();
//            member1.setName("member1");
//            member1.setTeam(team);
//            em.persist(member1);
//
//            Member member2 = new Member();
//            member2.setName("member2");
//            member2.setTeam(teamB);
//            em.persist(member2);
//
//            em.flush();
//            em.clear();
//
////            Member m = em.find(Member.class, member1.getId());
////
////            System.out.println("m = " + m.getTeam().getClass());
////
////            System.out.println("================================");
////            System.out.println("teamName = " + m.getTeam().getName()); //RAZY: 초기화, EAGER: teamA출력
////            System.out.println("=================================");
//
//            List<Member> members = em.createQuery("select m from Member m", Member.class)
//                    .getResultList(); //JPQL시 EAGER에서는 쿼리가 n+1 나감, LAZY에서는 프록시로 나가서 정상출력

//            //영속성 전이
//            Child child1 = new Child();
//            Child child2 = new Child();
//
//            Parent parent = new Parent();
//            parent.addChild(child1);
//            parent.addChild(child2);
//
//            em.persist(parent);
////            em.persist(child1); //cascade = CascadeType.ALL 자동으로 child persist
////            em.persist(child2);
//
//            em.flush();
//            em.clear();
//
//            //고아객체
//            Parent findParent = em.find(Parent.class, parent.getId());
//            //findParent.getChildList().remove(0); //child 쿼리 하나가 지워짐
//            em.remove(findParent); //자식까지 전부 삭제, child,parent 모두 삭제

//            //임베디드
//            Member member = new Member();
//            member.setName("hello");
//            member.setHomeAddress(new Address("city", "street", "zip"));
//            member.setWorkPeriod(new Period());
//            em.persist(member);

//            //값타입과 불변 객체
//            Address address = new Address("city", "street", "zip");
//            Member member = new Member();
//            member.setName("hello");
//            member.setHomeAddress(address);
//            em.persist(member);
//
//            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());
//
//            Member member1 = new Member();
//            member1.setName("hello");
//            //member1.setHomeAddress(address); // member, member1 모두 바뀜 -> 임베디드 타입 값타입을 공유했기 때문
//            member1.setHomeAddress(copyAddress); //  --> 값타입 복사해서 사용해야한다.
//            em.persist(member1);
//
////            member.getHomeAddress().setCity("newCity");
//
//            //불변객체에서 값 변경
//            Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode());
//            member.setHomeAddress(newAddress);

            //값타임 컬렉션
            Member member = new Member();
            member.setName("member1");
            member.setHomeAddress(new Address("Homecity", "street", "10000"));
            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("피자");

//            member.getAddressesHistory().add(new Address("old1", "street", "10000"));
//            member.getAddressesHistory().add(new Address("old2", "street", "10000"));
            //값타입 컬렉션 대안 사용
            member.getAddressesHistory().add(new AddressEntity("old1", "street", "10000"));
            member.getAddressesHistory().add(new AddressEntity("old2", "street", "10000"));

            em.persist(member); //값타입 컬렉션도 함께 persist -> 테이블 생성

            em.flush();
            em.clear();

            System.out.println("================START================");
            Member findMember = em.find(Member.class, member.getId());

//            //- 기본값으로 지연로딩전략 사용
//            List<Address> addressHistory = findMember.getAddressesHistory();
//            for(Address address : addressHistory) {
//                System.out.println("sddress = " + address.getCity());
//            }
//
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for(String favoriteFood : favoriteFoods) {
//                System.out.println("favoriteFood = " + favoriteFood);
//            }

//            //homeCity -> newCity
////            findMember.getHomeAddress().setCity("newCity"); //불변객체이므로 setCity 사용불가
//            Address a = findMember.getHomeAddress();
//            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode())); //새로운 인스턴스로 교체
//
//            //치킨 -> 한식
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");

//            findMember.getAddressesHistory().remove(new Address("old1", "street", "10000"));
//            //equals를 이용해서 제거 -> 값을 제대로 입력해야함 / getAddressesHistory관련 값을 모두 제거후 다시 생성한다.
//            findMember.getAddressesHistory().add(new Address("newCity1", "street", "10000"));
//            // ->값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(Member m1, Member m2) { //프록시로 넘어오는지 실제로 넘어오는지 알 수 없음
                                                        // -> == 비교 X,  대신 instance of 사용
        System.out.println("m1 == m2 : " + (m1 instanceof Member));
        System.out.println("m1 == m2 : " + (m2 instanceof Member));
    }
}

/*
--> 양방향 매핑 시 양방향 모두 세팅해 주는 것이 맞다.
    순수 객체 상태를 고려해서 항상 양쪽에 값을 설정


일대다 양방향 보다 다대일 양방향 이용할 것

 */