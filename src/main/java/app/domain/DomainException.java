package app.domain;

public class DomainException extends RuntimeException {
    private final int code;

    public DomainException(int code, String message){
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
