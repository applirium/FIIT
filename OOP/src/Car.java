import java.util.ArrayList;

public class Car
{
    private int carryingCapacity;
    private ArrayList<Comodity> actualInventory;
    Car(int level,ArrayList<Comodity> inventory) {
        this.actualInventory = new ArrayList<>();
        for (Comodity comodity : inventory)
        {
            this.actualInventory.add(new Comodity(comodity.getComodityType(), 0));
        }

        switch (level) {
            case 1 -> {
                this.carryingCapacity = 30;
            }
            case 2 -> {
                this.carryingCapacity = 50;
            }
            case 3 -> {
                this.carryingCapacity = 100;
            }
        }
    }
    //TODO celu logika na fuel
    public double fuelConsumption() {
        return 0;
    }
    public int getCarryingCapacity() {
        return carryingCapacity;
    }
    public void setCarryingCapacity(int carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }
    public ArrayList<Comodity> getActualInventory() {
        return actualInventory;
    }
    public void setActualInventory(ArrayList<Comodity> actualInventory) {
        this.actualInventory = actualInventory;
    }
    public void setActualInventory(int index,int quantity) {
        this.actualInventory.get(index).setComodityIO(quantity);
    }
}