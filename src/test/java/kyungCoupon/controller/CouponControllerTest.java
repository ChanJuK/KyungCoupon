package kyungCoupon.controller;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.network.Enums;
import kyungCoupon.domain.network.Header;
import kyungCoupon.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CouponController.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenerateCouponService generateCouponService;
    @MockBean
    private SelectCouponService selectCouponService;
    @MockBean
    private SetCouponUserService setCouponUserService;
    @MockBean
    private UseCouponService useCouponService;
    @MockBean
    private CnclUseCouponService cnclUseCouponService;

    @Test
    public void generateCouponNum() throws Exception {
        mvc.perform(post("/generateCoupon/"+ 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void getCouponExpiredToday() throws Exception {

        List<Coupon> couponList = new ArrayList<>();
        couponList.add(Coupon.builder()
                .id(1L)
                .couponNum("fhjdsk243j")
                .expDate("20200619")
                .build());

        given(this.selectCouponService.getCouponExpiredToday())
                .willReturn(Header.response(couponList, Enums.SUCCESS.getId(), Enums.SUCCESS.getDescription()));

        mvc.perform(get("/getCouponExpiredToday"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"couponNum\":\"fhjdsk243j\"")));
    }



}