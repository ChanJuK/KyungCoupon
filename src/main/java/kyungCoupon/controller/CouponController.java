package kyungCoupon.controller;


import io.jsonwebtoken.Claims;
import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.network.Header;
import kyungCoupon.service.*;
import kyungCoupon.util.OftenUsedFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.naming.AuthenticationException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class CouponController {

    @Autowired
    private GenerateCouponService generateCouponService;
    @Autowired
    private SelectCouponService selectCouponService;
    @Autowired
    private SetCouponUserService setCouponUserService;
    @Autowired
    private UseCouponService useCouponService;
    @Autowired
    private CnclUseCouponService cnclUseCouponService;


    /*
    1.램덤한 코드의 쿠폰을 N개 생성하여 데이터베이스테 보관하는 API
    @input Bigdecimal couponCnt
    @output
    실행 방법:
    http POST localhost:8080/generateCoupon/1
    * */
    @PostMapping("/generateCoupon/{couponCnt}")
    public ResponseEntity<?> generateCoupon(@PathVariable("couponCnt") BigDecimal couponCnt)
            throws URISyntaxException {
        //1일동안 유효한 쿠폰발급
        String expDate = OftenUsedFunction.getSystemDate(OftenUsedFunction.VALID_DAY_CNT);
        
        int cnt = generateCouponService.generateCouponNum(couponCnt, expDate);

        URI uri = new URI("/generateCoupon"+couponCnt);
        return ResponseEntity.created(uri).body("{"+cnt+"개의 쿠폰이 생성완료함.}");
    }


    /*
    2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API
    @input Authentication
    @output String couponNum
    실행 방법 :
    1. 로그인해서 토큰생성 http POST localhost:8080/logIn email=test@gmail.com password=test
    2. http PATCH localhost:8080/setCouponUser "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWQiOjIxLCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwidXNlck5hbWUiOiJ0ZXN0In0.7J3tJZ9G1gg0P1nAwBLlC9JbSjrfSGZIfQHqXeRuIrQ"
    * */
    @PatchMapping("/setCouponUser")
    public Header<String> setCouponUser(
            Authentication authentication) throws AuthenticationException {
        Claims claims = (Claims) authentication.getPrincipal();

        return setCouponUserService.setCouponUser(claims.get("id", Long.class));
    }

    /*
    3. 사용자에게 지급된 쿠폰을 조회하는 API
    @input Authentication
    @output List<Coupon>
    실행 방법 :
    1. 로그인해서 토큰생성 http POST localhost:8080/logIn email=test@gmail.com password=test
    2. http GET localhost:8080/getMyCouponInfo "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWQiOjIxLCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwidXNlck5hbWUiOiJ0ZXN0In0.I24WbN0xWoeUa1Xk8rlSC-ZKr4ZXJGxUiIQax2gsoho"
    * */
    @GetMapping("/getMyCouponInfo")
    public Header<Coupon> getMyCouponInfo(Authentication authentication) throws AuthenticationException {
        Claims claims = (Claims) authentication.getPrincipal();

        return selectCouponService.getMyCouponInfo(claims.get("id",Long.class));
    }


    /*
    4. 지급된 쿠폰 중 하나를 사용하는 API
    @input Authentication, String couponNum
    @output Coupon
    실행 방법:
    1. 로그인해서 토큰생성 http POST localhost:8080/logIn email=test@gmail.com password=test
    2. 어떤 쿠폰을 보유하고있는지 조회 http GET localhost:8080/getMyCouponInfo "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MyIsImlkIjoxMTEsImVtYWlsIjoidGVzdDNAZ21haWwuY29tIiwidXNlck5hbWUiOiJ0ZXN0MyJ9.5Eq28gYzsF1InsUDS-DS7dfTuBjIG68tSbQ7MBQSFSE"
    3. 조회된 쿠폰번호 복사 후 실행
    http PATCH localhost:8080/useCoupon/QhHeP7zIVkccqmd "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWQiOjIxLCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwidXNlck5hbWUiOiJ0ZXN0In0.89hgDrARgDXVPPkk4ZO2ozfHntk0rColFav8TnLlSto"
     * */
    @PatchMapping("/useCoupon/{couponNum}")
    public Header<Coupon> useCoupon(
            Authentication authentication,
            @PathVariable("couponNum") String couponNum) throws AuthenticationException {
        Claims claims = (Claims) authentication.getPrincipal();


        return useCouponService.useCoupon(couponNum, claims.get("id", Long.class));
    }



    /*
    5. 지급된 쿠폰중 하나를 사용 취소하는 API(취소된 쿠폰 재사용가능)
    @input Authentication, String couponNum
    @output Coupon
    실행 방법:
    1. 로그인해서 토큰생성 http POST localhost:8080/logIn email=test@gmail.com password=test
    2. 어떤 쿠폰을 보유하고있는지 조회 http GET localhost:8080/getMyCouponInfo "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MyIsImlkIjoxMTEsImVtYWlsIjoidGVzdDNAZ21haWwuY29tIiwidXNlck5hbWUiOiJ0ZXN0MyJ9.5Eq28gYzsF1InsUDS-DS7dfTuBjIG68tSbQ7MBQSFSE"
    3. 조회된 쿠폰번호 복사 후 실행
    http PATCH localhost:8080/cnclUseCoupon/QhHeP7zIVkccqmd "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWQiOjIxLCJlbWFpbCI6InRlc3RAZ21haWwuY29tIiwidXNlck5hbWUiOiJ0ZXN0In0.fsq5qk5LeLJs1Y9ayWO0XFPfm18KdOkoQfBTgHv68ZI"
    * */
    @PatchMapping("/cnclUseCoupon/{couponNum}")
    public Header<Coupon> cnclUseCoupon(
            Authentication authentication,
            @PathVariable("couponNum") String couponNum) throws AuthenticationException {
        Claims claims = (Claims) authentication.getPrincipal();


        return cnclUseCouponService.cnclUseCoupon(couponNum, claims.get("id", Long.class));
    }



    /*
    6. 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회 API
    @input
    @output List<Coupon>
    실행 방법:
    http GET localhost:8080/getCouponExpiredToday
    * */
    @GetMapping("/getCouponExpiredToday")
    public Header<Coupon> getCouponExpiredToday(){

        return selectCouponService.getCouponExpiredToday();
    }





}
