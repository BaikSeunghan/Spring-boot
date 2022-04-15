package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service    // 비즈니스 로직, 트랜잭션 처리
@Transactional(readOnly = true) // 조회하는 쪽에서는 조금더 성능을 최적화
// Class 레벨에 @Transactional 쓰면 public 메소드들이 실행될때 걸려들어간다.
// JPA에서 모든 데이터의 변경이나 로직들은 가급적이면 트랜잭션 안에서 다 실행되어야한다.
// 그래야 LAZY 로딩도 다 실행이되는것이다.
@RequiredArgsConstructor // final이 있는 필드만을 가지고 생성자를 만들어준다. (A)의 임무를 대신 해줄 수 있다.
public class MemberService {

    // 생성자주입하면 변경될일이없기때문에 final선언, 선언의 이점: 컴파일 오류로 실수를 줄일수 있다.
    private final MemberRepository memberRepository;

//    (A)
//    @Autowired  // 생성자로 주입받는게 가장 좋다. 생성자가 하나만 있는 경우에는 어노테이션없이 된다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional  // 메소드 자체에 걸었기때문에 기본 @Transactional 실행
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    /**
     * 회원 전체 조회
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /**
     * 단건 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
