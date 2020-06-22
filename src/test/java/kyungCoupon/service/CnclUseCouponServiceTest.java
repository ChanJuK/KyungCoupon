package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.exception.CouponNotExistsException;
import kyungCoupon.util.OftenUsedFunction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class CnclUseCouponServiceTest {

    private CnclUseCouponService cnclUseCouponService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        cnclUseCouponService = new CnclUseCouponService(couponRepository, userRepository);
    }

    @Test(expected = CouponNotExistsException.class)
    public void couponNotExistsException() throws Exception {

        String couponNum = "fewf23ffewf23fw";
        User user = User.builder()
                .id(1L)
                .email("test")
                .build();
        Coupon coupon = null;
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(couponRepository.findByCouponNum(couponNum)).willReturn(Optional.ofNullable(coupon));
        cnclUseCouponService.cnclUseCoupon(couponNum,1L);


    }

    @Test
    public void cnclCoupon() {

        String couponNum = "fewf23ffewf23fw";
        User user = User.builder()
                .id(1L)
                .email("test")
                .build();
        Coupon coupon = Coupon.builder()
                .couponNum(couponNum)
                .id(1L)
                .expDate("20200630")
                .user(user)
                .useYN("Y")
                .useDate("20200621")
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(couponRepository.findByCouponNum(couponNum)).willReturn(Optional.of(coupon));
        Coupon rstCoupon = cnclUseCouponService.cnclUseCoupon(couponNum,1L);


        verify(couponRepository).save(any());

    }
}