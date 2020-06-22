package kyungCoupon.service;

import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.exception.EmailNotExistsException;
import kyungCoupon.exception.PasswordInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LogInService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public LogInService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* 로그인시 아이디이메일과 비번이 맞는지 검증하는 함수
     * @Param User input
     * @return User
     * */
    public User authenticate(String email, String password) {

        //이 이메일 회원가입한 고객인지 검증
        User user = userRepository.findByEmail(email).orElseThrow(
                () ->  new EmailNotExistsException(email));

        //비밀번호가 일지한지 거증
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new PasswordInvalidException();
        }

        return user;
    }



}
