package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        //code

        //데이터 변경 작업은 transaction 내에서 진행 되어야 함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //생성
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("HelloName");
//            em.persist(member); // 저장

            //찾기
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());

            //수정
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("HelloJPA");

            //JPQL : 객체 제한 쿼리
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    .getResultList();
//            for (Member member : result) {
//                System.out.println("member.name = " + member.getName());
//            }

//            //비영속
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA");
//
//            //영속
//            System.out.println("====before====");
//            em.persist(member);
//            System.out.println("====after====");
//
//            //1차 캐시에서 조회
//            Member findMember = em.find(Member.class, 101L);
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());

//            //똑같은 쿼리를 조회했기 때문에 쿼리 한번만 생성 - 영속 엔티티의 동일성 보장
//            Member findMember1 = em.find(Member.class, 101L);
//            Member findMember2 = em.find(Member.class, 101L);
//
//            System.out.println("result = " + (findMember1 == findMember2));

//            //엔티티 등록 - 쓰기 지연
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//
//            em.persist(member1);
//            em.persist(member2); //영속 컨택스트에 쿼리 쌓임
//            System.out.println("==================");

//            //엔티티 수정 - 변경 감지
//            //영속성 1차 캐시에 들어오면 스냅샷을 찍어놓고 값을 변경하면 엔티티와 스냅샷을 비교해서 다르면 변경
//            Member member = em.find(Member.class, 150L);
//            member.setName("ZZZZZ"); //수정 시 자동 업데이트
//
//            // em.persist(member); 사용 X
//            System.out.println("==================");

//            //플러시 : 영속성 컨텍스트 변경내용 db 동기화
//            Member member = new Member(200L, "member200");
//            em.persist(member);
//            em.flush(); // 바로 db 반영
//            System.out.println("==================");

//            //영속 -> 준영속
//            Member member = em.find(Member.class, 150L);
//            member.setName("AAAAA");
//            em.clear(); // 1차 캐시 지우기
//
//            em.detach(member); // 영속성 컨텍스트에서 관리하지 않음 -> 준영속

            Member member1 = new Member();
            member1.setUsername("C");
            Member member2 = new Member();
            member2.setUsername("C");
            Member member3 = new Member();
            member3.setUsername("C");

            System.out.println("==================");
            em.persist(member1);
//            em.persist(member2);
//            em.persist(member3);
            System.out.println("member.id = " + member1.getId());
            System.out.println("member.id = " + member2.getId());
            System.out.println("member.id = " + member3.getId());
            System.out.println("==================");



            tx.commit(); // 커밋할 때 DB에 쿼리 보냄
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
