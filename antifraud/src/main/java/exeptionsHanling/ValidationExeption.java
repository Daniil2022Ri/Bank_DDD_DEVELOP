package exeptionsHanling;

public class ValidationExeption extends RuntimeException {
    public ValidationExeption(String message) {
        super(message);
    }
}
