package kyungCoupon.domain;

import kyungCoupon.domain.network.reqResIO.CouponAPI;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends CrudRepository<Coupon, Long> {
    List<Coupon> findByExpDate(String today);

    Coupon findFirstByUserIdIsNullAndExpDateGreaterThan(String today);

    Coupon save(Coupon coupon);

    List<Coupon> findByUserId(Long id);

    Optional<Coupon> findByCouponNum(String couponNum);

    List<Coupon> findByAndUserIdIsNotNullAndExpDateAndUseYN(String date3Day, String n);

    List<Coupon> findByUserIdAndUseYNAndExpDateGreaterThanEqual(Long id, String n, String todayDate);
}
