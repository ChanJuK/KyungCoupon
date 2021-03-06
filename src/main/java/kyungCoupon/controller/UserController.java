package kyungCoupon.controller;

import kyungCoupon.domain.SessionRequestDTO;
import kyungCoupon.domain.SessionResponseDTO;
import kyungCoupon.domain.User;
import kyungCoupon.domain.network.Enums;
import kyungCoupon.domain.network.Header;
import kyungCoupon.service.LogInService;
import kyungCoupon.service.SignInService;
import kyungCoupon.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
public class UserController {

    @Autowired
    private SignInService signInService;
    @Autowired
    private LogInService logInService;
    @Autowired
    private JwtUtil jwtUtil;


    /*고객 가입하는 컨트롤러
    @input User input
    @output
    가입방법 : email=?, password=?, userName=? 이렇게 입력하면 가입된다.
    http POST localhost:8080/signIn email=test@gmail.com password=test userName=test
    * */
    @PostMapping("/signIn")
    public Header<?> signIn(@Valid @RequestBody User input) throws URISyntaxException {

        return signInService.signIn(input);
    }


    /* 로그인 함수, 토큰을 발급함.
    @input SessionRequestDTO input
    @output
    방법 : 가입하때 입력한 아이디와 비밀번호를 입력한다.
    http POST localhost:8080/logIn email=test3@gmail.com password=test
     * */
    @PostMapping("/logIn")
    public Header<SessionResponseDTO> logIn(@Valid @RequestBody SessionRequestDTO input) throws URISyntaxException {

        User user = logInService.authenticate(input.getEmail(), input.getPassword());

        String token = jwtUtil.createToken(user);

        SessionResponseDTO sessionResponseDTO = SessionResponseDTO.builder()
                .accessToken(token)
                .build();
        return Header.response(sessionResponseDTO, Enums.SUCCESS.getId(), user.getUserName()+"님, 환영합니다.");
    }

}
