package kyungCoupon.domain;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"couponList"})
public class User {

    //id
    @Id
    @GeneratedValue
    @Setter
    @Getter
    private Long id;

    //이메일
    @NotEmpty
    @Setter
    @Getter
    private String email;

    //비번
    @NotEmpty
    @Setter
    @Getter
    private String password;

    //고객 이름
    @NotEmpty
    @Setter
    @Getter
    private String userName;


    //가입일자
    @Setter
    @Getter
    private String signInDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Coupon> couponList;

    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
        if (this.couponList != null && this.couponList.size() > 0) {
            for (Coupon c : couponList) { c.setUser(this); }
        }
    }

    public List<Coupon> getCouponList(){
        return this.couponList;
    }




}
