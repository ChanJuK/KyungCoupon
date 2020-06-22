package kyungCoupon.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class CouponId implements Serializable {
    private Long id;

    private String couponNum;


}