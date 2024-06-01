package jpql;

import jakarta.persistence.*;

import java.util.List;

public class Jpamain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Member member = new Member();
        member.setUsername("member1");
        em.persist(member);

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

        //위 보다 아래 코드가 더 good
        Member result = em.createQuery("SELECT m FROM Member m where m.username = :username", Member.class)
                        .setParameter("username", "member1")
                        .getSingleResult();
        System.out.println("result: " + result.getUsername());

        em.flush();
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