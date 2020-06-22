package kyungCoupon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequestDTO {
    //이메일
    private String email;

    //비번
    private String password;
}
