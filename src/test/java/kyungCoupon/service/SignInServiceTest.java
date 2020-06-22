package kyungCoupon.service;

import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.exception.EmailExistsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SignInServiceTest {
    private SignInService signInService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        signInService = new SignInService(userRepository, passwordEncoder);
    }


    @Test(expected= EmailExistsException.class)
    public void signInWithExistedEmail() throws Exception {

        User mockUser = User.builder().build();
        String email = "test@gmail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));
        signInService.signIn(User.builder()
                .email("test@gmail.com")
                .userName("test")
                .password("test")
                .build()
        );
        verify(userRepository,never()).save(any());

    }
}