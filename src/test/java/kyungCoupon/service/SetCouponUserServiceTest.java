package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.domain.network.Header;
import kyungCoupon.exception.AlreadyHasCouponException;
import kyungCoupon.util.OftenUsedFunction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class SetCouponUserServiceTest {

    private SetCouponUserService setCouponUserService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        setCouponUserService = new SetCouponUserService(couponRepository, userRepository);
    }

    @Test
    public void setCouponToUser(){

        String email = "test@gmail.com";
        Coupon coupon = Coupon.builder()
                .couponNum("fewf23ffewf23fw")
                .id(1L)
                .expDate("20200623")
                .user(null)
                .build();
        List<Coupon> couponList = new ArrayList<>();
        User user = User.builder()
                .id(1L)
                .email(email)
                .build();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(couponRepository
                .findByUserIdAndUseYNAndExpDateGreaterThanEqual(user.getId(), "N", "20200621"))
                .willReturn(couponList);
        given(couponRepository
                .findFirstByUserIdIsNullAndExpDateGreaterThan(OftenUsedFunction.getTodayDate()))
                .willReturn(coupon);

        Header<String> coupon2 = setCouponUserService.setCouponUser(1L);

        assertThat(coupon2.getData(), is("fewf2-3ffew-f23fw"));
    }


    @Test(expected = AlreadyHasCouponException.class)
    public void alreadyHasCouponException() throws Exception {

        String email = "test@gmail.com";
        Coupon coupon = Coupon.builder()
                .couponNum("fewf23ffewf23fw")
                .id(1L)
                .expDate("20200620")
                .user(null)
                .build();
        List<Coupon> couponList = new ArrayList<>();
        couponList.add(coupon);
        User user = User.builder()
                .id(1L)
                .email(email)
                .build();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(couponRepository.findByUserIdAndUseYNAndExpDateGreaterThanEqual(user.getId(),"N", OftenUsedFunction.getTodayDate()))
                .willReturn(couponList);
        given(couponRepository.findFirstByUserIdIsNullAndExpDateGreaterThan(OftenUsedFunction.getTodayDate())).willReturn(coupon);
        setCouponUserService.setCouponUser(1L);


        verify(couponRepository).save(any());

    }


}