package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// 해당 어노테이션을 통해 @Compont 스캔 적용대상이 되어 자동으로 빈이 생성된다.
// JPA를 직접 사용하는 계층, 엔티티 매니저 사용
// JPA 예외를 스프링 기반 예외로 예외 변환
@Repository
// 여기서도 em에서 @PersistenceContext를 @Autowired로 바꾸고,생성자만들
// 면서 주입할수있고
// @RequiredArgsConstructor로 일관성있게 해줄수있다.
public class MemberRepository {

    // JPA가 제공하는 표준 어노테이션
    @PersistenceContext // 스프링부트가 해당 어노테이션이 붙은 객체를 찾아서 주입해준다
    private EntityManager em;

    public void save(Member member) {
        // JPA가 member를 정하는 로직, 트랜잭션이 커밋되는 시점에 DB에 insert쿼리가 날라가면서 반영된다.
        em.persist(member);
    }
    public Member findOne(Long id) {
        return em.find(Member.class, id);   // id값에 해당하는 Member를 찾아서 return 해주는 로직
                                            // 첫번째에는 타입, 두번째는 PK
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
        //        List<Member> resultList = em.createQuery("select m from Member m", Member.class).
        //                                  getResultList();
    }
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
