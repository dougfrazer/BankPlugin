public class BankLocation extends Location {

    String name;
    double distance;

    public BankLocation(String s) {
        name = s;
    }

    public BankLocation(String s, double X, double Y, double Z) {
        name = s;
        this.x = X;
        this.y = Y;
        this.z = Z;
    }

}
