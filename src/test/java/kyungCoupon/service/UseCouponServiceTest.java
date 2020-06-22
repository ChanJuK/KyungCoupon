package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.exception.CouponNotExistsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class UseCouponServiceTest {
    private UseCouponService useCouponService;

    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        useCouponService = new UseCouponService(couponRepository, userRepository);
    }

    @Test
    public void useCouponTest() throws Exception {

        String couponNum = "fewf23ffewf23fw";
        User user = User.builder()
                .id(1L)
                .userName("낭아러")
                .email("test@gmail.com")
                .build();
        Coupon coupon = Coupon.builder()
                .couponNum(couponNum)
                .id(1L)
                .expDate("20200630")
                .user(user)
                .build();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(couponRepository.findByCouponNum(couponNum)).willReturn(Optional.of(coupon));
        useCouponService.useCoupon(couponNum, user.getId());


        verify(couponRepository).save(any());

    }


    @Test(expected = CouponNotExistsException.class)
    public void couponNotExistsException() throws Exception {

        String couponNum = "fewf23ffewf23fw";
        Coupon coupon = Coupon.builder()
                .couponNum(couponNum)
                .id(1L)
                .expDate("20200619")
                .user(null)
                .build();
        User user = User.builder()
                .id(1L)
                .userName("낭아러")
                .email("test@gmail.com")
                .build();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(couponRepository.findByCouponNum(couponNum)).willReturn(Optional.of(coupon));
        useCouponService.useCoupon(couponNum, user.getId());



    }

}