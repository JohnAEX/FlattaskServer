package hwr.sem4.csa.exceptions;

public class OutOfIdsException extends Exception {
    String message;

    public OutOfIdsException(String message)
    {
        super(message);
    }

}
