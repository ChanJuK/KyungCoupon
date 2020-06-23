package kyungCoupon.controller;

import kyungCoupon.service.SndMsg3DayBfreExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

@RestController
public class SndMsg3DayBfreExpController {

    @Autowired
    private SndMsg3DayBfreExpService sndMsg3DayBfreExpService;

    /*
    7.발급된 쿠폰중 만료 3일전 사용자에게 메세지 발송하는 기능
    (실제 발송이 아닌 로그에 프린트로 출력함)
    @input String expAftDate N일 이후 만료, 예) expAftDate=3, 3일이후에 만려
    @output N건이 전송되었습니다.
    http POST localhost:8080/SndMsg3DayBfreExp/20200621
    * */
    @PostMapping("/SndMsg3DayBfreExp/{excDate}")
    public ResponseEntity<?> SndMsg3DayBfreExp (@PathVariable("excDate") String excDate)
            throws URISyntaxException, ParseException {

        int sndCnt = sndMsg3DayBfreExpService.sndMsgToCustomers(excDate);

        URI uri = new URI("/SndMsg3DayBfreExp"+excDate);
        return ResponseEntity.created(uri).body("{"+sndCnt+ "건이 전송되었습니다.}");
    }


}
