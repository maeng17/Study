package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

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

            //고급매핑(@MappedSuperclass)
            Member member = new Member();
            member.setName("user");
            member.setCreateBy("kim");
            member.setCreateDate(LocalDateTime.now());

            em.persist(member);

            em.flush(); //영속성 컨텍스트 날리고
            em.clear(); //영속성 컨텍스트 깔끔하게

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}

/*
--> 양방향 매핑 시 양방향 모두 세팅해 주는 것이 맞다.
    순수 객체 상태를 고려해서 항상 양쪽에 값을 설정


일대다 양방향 보다 다대일 양방향 이용할 것

 */