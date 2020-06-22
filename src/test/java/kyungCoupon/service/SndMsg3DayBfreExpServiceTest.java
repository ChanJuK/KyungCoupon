package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

public class SndMsg3DayBfreExpServiceTest {
    private SndMsg3DayBfreExpService sndMsg3DayBfreExpService;


    @Mock
    private CouponRepository couponRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        sndMsg3DayBfreExpService = new SndMsg3DayBfreExpService(couponRepository);
    }


    @Test
    public void setSndMsg3DayBfreExp(){

        String date = "20200623";
        User user = User.builder()
                .id(1L)
                .userName("낭아러")
                .email("test@gmail.com")
                .build();
        List<Coupon> couponList = new ArrayList<>();
        Coupon coupon = Coupon.builder()
                .id(1L)
                .couponNum("dddddfffffggggg")
                .user(user)
                .expDate("20200623")
                .build();
        couponList.add(coupon);
        couponList.add(coupon);
        given(couponRepository.findByAndUserIdIsNotNullAndExpDateAndUseYN("20200626", "N"))
                .willReturn(couponList);

        BigDecimal rstCnt= sndMsg3DayBfreExpService.sndMsgToCustomers(date);

        assertThat(rstCnt, is(new BigDecimal("2")));
    }
}