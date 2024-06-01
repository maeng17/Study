package jpql;

import jakarta.persistence.*;

import java.util.List;

public class Jpamain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

//        Member member = new Member();
//        member.setUsername("member1");
//        em.persist(member);
//
//        em.flush();
//        em.clear();

//        TypedQuery<Member> query1 = em.createQuery("SELECT m FROM Member m", Member.class);
//        TypedQuery<String> query2 = em.createQuery("SELECT m.username FROM Member m", String.class); //하나의 타입 이용시 TypeQuery 사용가능
//        Query query3 = em.createQuery("SELECT m.username, m.age FROM Member m"); //반환타입이 명확하지 않을 때
//
//        Member result = query1.getSingleResult(); //반환 값이 하나일 때, 단일 객체 반환
//        System.out.println("result: " + result);

        //파라미터 바인딩
//        TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m where m.username = :username", Member.class);
//        query.setParameter("username", "member1");
//        Member singleResult = query.getSingleResult();
//        System.out.println("singleResult = " + singleResult.getUsername());

//        //위 보다 아래 코드가 더 good
//        Member result = em.createQuery("SELECT m FROM Member m where m.username = :username", Member.class)
//                        .setParameter("username", "member1")
//                        .getSingleResult();
//        System.out.println("result: " + result.getUsername());

        //프로젝션
//        List<Member> result = em.createQuery("SELECT m FROM Member m", Member.class)
//                        .getResultList();
//        Member findMember = result.get(0);
//        findMember.setAge(20); // update -> 영속성 컨텍스트로 관리되어 데이터 반영됨

        //엔티티 프로젝션
//        List<Team> result = em.createQuery("SELECT m.team FROM Member m", Team.class) //join해서 쿼리 나감
//                        .getResultList();

        //임베디드 타입 프로젝션
//        List<Address> result = em.createQuery("SELECT o.address FROM Order o", Address.class) //어디소속인디 엔티티를 정해줘야 함
//                .getResultList();

        //스칼라 타입 프로젝션
//        List<Member> result = em.createQuery("select distinct m.username, m.age FROM Member m")
//                .getResultList();


        //여러값 조회
        //1) 쿼리 타입으로 조회
//        List<Member> resultList = em.createQuery("select distinct m.username, m.age FROM Member m")
//                .getResultList();
//
//        Object o = resultList.get(0);
//        Object[] result = (Object[]) o;
//        System.out.println("result: " + result[0]);
//        System.out.println("result: " + result[1]);

        //Object[] 타입으로 조회
//        List<Object[]> resultList = em.createQuery("select distinct m.username, m.age FROM Member m")
//                .getResultList();
//
//        Object[] result = resultList.get(0);
//        System.out.println("result: " + result[0]);
//        System.out.println("result: " + result[1]);

        //new 명령어로 조회
//        List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) FROM Member m", MemberDTO.class)
//                .getResultList();
//
//        MemberDTO memberDTO = result.get(0);
//        System.out.println("memberDTO: " + memberDTO.getUsername());
//        System.out.println("memberDTO: " + memberDTO.getAge());

        //페이징 API

        for(int i=0; i<100; i++){
            Member member = new Member();
            member.setUsername("member" + i);
            member.setAge(i);
            em.persist(member);
        }
        em.flush();
        em.clear();

        List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();

        System.out.println("result.size = ");
        for(Member member1 : result) {
            System.out.println("member1 = " + member1);
        }

        tx.commit();
        try {
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}

/*
JPQL 문법
• select m from Member as m where m.age > 18
• 엔티티와 속성은 대소문자 구분O (Member, age)
• JPQL 키워드는 대소문자 구분X (SELECT, FROM, where)
• 엔티티 이름 사용, 테이블 이름이 아님(Member)
• 별칭은 필수(m) (as는 생략가능)
 */