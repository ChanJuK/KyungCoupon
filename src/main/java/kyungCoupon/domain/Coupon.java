package kyungCoupon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user"})
@Table(indexes = {
        @Index(name = "IDX_COUPON_NUM", unique=true, columnList = "id,couponNum, userId")
})
@IdClass(CouponId.class)
public class Coupon {

    //일련번호 -pk
    @Id
    @GeneratedValue
    private Long id;

    //쿠폰번호 - pk
    @Id
    private String couponNum;


    //만료일자
    private String expDate;

    //사용여부
    private String useYN;

    //사용일자
    private String useDate;

    //취소여부
    private String cnclYN;

    //취소일자
    private String cnclDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;


    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coupon) {
            Coupon c = (Coupon) obj;
            return (this.couponNum == c.couponNum);
        }
        return false;
    }

}
