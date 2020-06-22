package kyungCoupon.controller;

import kyungCoupon.domain.User;
import kyungCoupon.service.LogInService;
import kyungCoupon.service.SignInService;
import kyungCoupon.util.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.net.URI;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private SignInService signInService;
    @MockBean
    private LogInService logInService;

    @Test
    public void signInInValidTest() throws Exception {

        URI url = new URI("/signIn");
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\", \"password\":\"\",\"userName\":\"\"}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void signValidTest() throws Exception {

        URI url = new URI("/signIn");
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@gmail.com\", \"password\":\"test\",\"userName\":\"test\"}"))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void logInTest() throws Exception {

        String email = "test@gmail.com";
        String pw = "test";

        User mockUser = User.builder()
                .id(1L)
                .userName("test")
                .email(email)
                .password("test").build();

        given(logInService.authenticate(email, pw)).willReturn(mockUser);

        given(jwtUtil.createToken(mockUser))
                .willReturn("header.payload.signature");

        mvc.perform(post("/logIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@gmail.com\", \"password\":\"test\"}"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("{\"accessToken\":\"header.payload.signature\"}")));



        verify(logInService).authenticate(eq(email), eq(pw));

    }


}