package kyungCoupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmailNotExistsException.class)
    public String emailNotExistsException(){
        return "{}";
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmailExistsException.class)
    public String emailExistsException(){
        return "{}";
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(CouponAllUsedException.class)
    public String couponAllUsedException(){
        return "{}";
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(AlreadyHasCouponException.class)
    public String alreadyHasCouponException(){
        return "{}";
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(CouponNotExistsException.class)
    public String couponNotExistsException(){
        return "{}";
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(PasswordInvalidException.class)
    public String passwordInvalidException(){
        return "{}";
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(AuthenticationException.class)
    public String authenticationException(){
        return "{}";
    }
}
