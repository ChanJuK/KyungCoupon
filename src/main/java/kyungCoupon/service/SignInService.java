package kyungCoupon.service;

import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.domain.network.Enums;
import kyungCoupon.domain.network.Header;
import kyungCoupon.exception.EmailExistsException;
import kyungCoupon.util.OftenUsedFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class SignInService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public SignInService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /*회원 가입하는 서비스 함수
    * @Param User input
    * @return void
    * */
    public Header<?> signIn(User input) {

        //기존에 존재한 이메일인지 검증
        Optional<User> user = userRepository.findByEmail(input.getEmail());
        if(user.isPresent()){
            throw new EmailExistsException(input.getEmail());
        }

        //가입한 일자 세팅
        input.setSignInDate(OftenUsedFunction.getTodayDate());

        //비밀번호 암호화 세팅
        String pw = passwordEncoder.encode(input.getPassword());
        input.setPassword(pw);
        try{
            userRepository.save(input);
        }catch (Exception e){
            return Header.response(Enums.SYSTEM_ERROR.getId(), "가입시 저장 실패했습니다.");
        }

        return Header.response(Enums.SUCCESS.getId(), "가입 성공했습니다.");

    }
}
