package kyungCoupon.domain.network;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Enums {

    SUCCESS(0, "success","정상처리되었습니다."),
    SYSTEM_ERROR(9, "system_error", "에러가 발생하여 정상처리되지 않았습니다.");

    private Integer id;
    private String title;
    private String description;

}
