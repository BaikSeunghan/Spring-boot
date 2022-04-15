package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

// 단순 테스트가 아니라, JPA가 실제로 도는것을 확인하기 위해 Spring과 결합해서 테스트를 진행
// 메모리 모드로 DB까지 엮어서 테스트
@RunWith(SpringRunner.class)    // JUnit 실행할때 스프링이 같이 엮어서 실행할래!
@SpringBootTest // SpringBoot를 띄운 상태로 테스트를 하겠다! 이게 없으면 @Autowired 작동안된다.
@Transactional  // 이게 있으면 기본적으로 롤백을 해버리기때문에, 커밋이 안되서 insert문이 안나온다.
// 나오게하려면 메소드에 @Rollback(false)설정을 해준다. 이러면 DB에도 변경된다.
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //give
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //give
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);
//        이런식으로 하면 지저분하니까 @Test에 설정을 해준다
//        try{
//            memberService.join(member2);    // 예외가 발생해야 한다!!
//        }catch (IllegalStateException e){
//            return;
//        }
        //then
        fail("예외가 발생해야 한다.");
    }


}