import java.util.ArrayList;
public class Player extends Entity {
    private int money;
    private Car car;
    Player(ArrayList<Town> towns, ArrayList<Comodity> inventory) {
        super(towns);
        this.setEntityType("Player");
        this.money = 0;
        this.car = new Car(1,inventory);
    }
    @Override
    public void action(Town town,ArrayList<Comodity> demandList, int gameDay) {
        this.move(town);
        town.IO(this,demandList, gameDay);
    }
    @Override
    public void infoEntity() {
        super.infoEntity();
        System.out.println("Money: "+this.money);
        System.out.println("Car max carrying capacity: "+this.car.getCarryingCapacity());
        System.out.print("Inventory: ");
        for (int i = 0; i < this.car.getActualInventory().size(); i++)
        {
            System.out.print("| " + this.car.getActualInventory().get(i).getComodityType() + " " + this.car.getActualInventory().get(i).getComodityIO() +" |");
        }
        System.out.println("\n");
    }
    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
    }
}