package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.domain.network.Header;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;


public class SelectCouponServiceTest {
    private SelectCouponService selectCouponService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CouponRepository couponRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        selectCouponService = new SelectCouponService(couponRepository, userRepository);
    }

    @Test
    public void getCouponExpiredToday(){

        List<Coupon> couponList = new ArrayList<>();
        Coupon coupon = Coupon.builder()
                .couponNum("couponNum")
                .id(1L)
                .expDate("20200620")
                .user(null)
                .build();
        couponList.add(coupon);
        given(couponRepository.findByExpDate("20200620")).willReturn(couponList);

    }

    @Test
    public void getMyCouponInfo() {

        String email = "test@gmail.com";
        String couponNum = "111112222233333";
        User user = User.builder()
                .id(1L)
                .email(email)
                .userName("test")
                .password("test")
                .build();
        List<Coupon> couponList = new ArrayList<>();
        Coupon coupon = Coupon.builder()
                .couponNum(couponNum)
                .user(user)
                .build();
        couponList.add(coupon);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(couponRepository.findByUserId(user.getId())).willReturn(couponList);
        Header<Coupon> couponHeader = selectCouponService.getMyCouponInfo(user.getId());

        assertThat(couponHeader.getDataList().get(0).getCouponNum(), is(couponNum));

    }




}