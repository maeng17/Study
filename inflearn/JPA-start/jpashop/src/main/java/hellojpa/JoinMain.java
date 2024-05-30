package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JoinMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //팀 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            //회원 저장
            Member member = new Member();
            member.setName("member1");
            //team.getMembers().add(member); //단독 사용시, TEAM_ID=null, 연관관계의 주인이 Member.team이기 때문
            member.setTeam(team);
            em.persist(member);

            team.addMember(member);
            // -> addMember or setTeam 둘중 하나 사용 -> 에러 날 수도

            //영속성 컨텍스트 아닌 DB에서 가져오는 쿼리 보고싶을 때 -> db에서 깔끔하게 값 가져옴
            em.flush();
            em.clear();

//            Member findMember = em.find(Member.class, member.getId());

//            Team findTeam = findMember.getTeam();
//            System.out.println("findTeam: " + findTeam.getName());

//            List<Member> members = findMember.getTeam().getMembers();
//            for(Member m : members) {
//                System.out.println("m = " +m.getName());
//            }

            System.out.println("==============");
            Team findTeam = em.find(Team.class, team.getId()); //1차 캐시 -> 영속성 컨텍스트에 들어가 있음
            List<Member> members = findTeam.getMembers();
            for(Member m : members) {
                System.out.println("m = " +m.getName()); // 로딩된 1차캐시 출력
            }
            System.out.println("==============");

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


 */