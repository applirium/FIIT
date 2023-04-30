public class Comodity {
    private final String comodityType; // wood - water - copper - iron - steel - processors - gene - robotics
    private int comodityIO;
    Comodity(String type,int number) {
        this.comodityIO = number;
        this.comodityType = type;
    }
    public String getComodityType() {
        return comodityType;
    }
    public int getComodityIO() {
        return comodityIO;
    }
    public void setComodityIO(int comodityIO) {
        this.comodityIO = comodityIO;
    }
}