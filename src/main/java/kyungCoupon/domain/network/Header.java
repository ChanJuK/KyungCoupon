package kyungCoupon.domain.network;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Header<T> {

    //처리결과 코드 0.정상 9.시스템오류
    private String resultCode;

    //처리결과 응답 메시지
    private String description;

    //data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    //list data size
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int dataListSize;
    //list data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> dataList;


    //
    public static <T> Header<T> response(int rstCode, String msg){
        return (Header<T>) Header.builder()
                .resultCode(String.valueOf(rstCode))
                .description(msg)
                .build();
    }

    public static <T> Header<T> response(T data, int rstCode, String msg){
        return (Header<T>) Header.builder()
                .resultCode(String.valueOf(rstCode))
                .description(msg)
                .data(data)
                .build();
    }

    public static <T> Header<T> response(List<T> dataList, int rstCode, String msg){
        return (Header<T>) Header.builder()
                .resultCode(String.valueOf(rstCode))
                .description(msg)
                .dataList((List<Object>) dataList)
                .dataListSize(dataList.size())
                .build();
    }


}
