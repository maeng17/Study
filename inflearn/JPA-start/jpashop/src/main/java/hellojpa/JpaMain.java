package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

//객체지향 쿼리 언어
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            //JPQL
//            List<Member> result = em.createQuery(
//                    "select m From Member m where m.name like 'kim'",
//                    Member.class
//            ).getResultList();
//            for (Member member: result){
//                System.out.println("member = " + member);
//            }

//            //Criteria 사용 준비
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Member> query = cb.createQuery(Member.class);
//
//            Root<Member> m = query.from(Member.class);
//
//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "kim"));
//            List<Member> resultList = em.createQuery(cq).getResultList();

            //Native SQL
            List<Member> resultList = em.createNativeQuery("select MEMBER_ID, city, street, zipcode, USERNAME from MEMBER", Member.class)
                            .getResultList();

            for(Member member1 : resultList) {
                System.out.println("member1: " + member1);
            }

            Member member = new Member();
            member.setName("user");
            em.persist(member);

            //flush -> commit, query날라올 떄 동작
            em.flush(); //JDBC 직접 사용, SpringJdbcTemplate 등 강제 flush 필요

            tx.commit();
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
JPQL
- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
- SQL을 추상화해서 특정 데이터베이스 SQL에 의존X
- JPQL을 한마디로 정의하면 객체 지향 SQL
- 동적쿼리를 만들기 어려움

Criteria
- 장점: 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
- 단점: 너무 복잡하고 실용성이 없다.
- 동적쿼리 깔끔하게 나옴, 짜기 좋음
- 오타 시 임포트 오류
- 유지보수 어려움

QueryDSL
• 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
• JPQL 빌더 역할
• 컴파일 시점에 문법 오류를 찾을 수 있음
• 동적쿼리 작성 편리함
• 단순하고 쉬움
• 실무 사용 권장
 */