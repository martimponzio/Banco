package Banco;

public class BancoException extends RuntimeException {
    private final String code;

    public BancoException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}