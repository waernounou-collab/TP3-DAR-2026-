package commonPackage;

import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    public String operation;
    public double a;
    public double b;

    public Request(String operation, double a, double b) {
        this.operation = operation;
        this.a = a;
        this.b = b;
    }
}