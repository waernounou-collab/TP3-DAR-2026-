package commonPackage;

import java.io.Serializable;

public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    public int clientNumber;
    public String message;
    public double result;
    public int operationCount;
    public boolean success;

    public Response(int clientNumber, String message,
                    double result, int operationCount,
                    boolean success) {

        this.clientNumber = clientNumber;
        this.message = message;
        this.result = result;
        this.operationCount = operationCount;
        this.success = success;
    }
}