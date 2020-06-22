package kyungCoupon.exception;

public class EmailNotExistsException extends RuntimeException {
    public EmailNotExistsException(String email){
        super("this email is not exists : "+email);

    }
}
