package kyungCoupon.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email){

        super("this email is already exsits [email : " + email + "]");
    }

}
