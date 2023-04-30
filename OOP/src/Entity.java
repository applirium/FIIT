import java.util.ArrayList;
import java.util.Random;

public abstract class Entity {
    private Map_Location map;
    private String entityType;
    Entity(ArrayList<Town> towns) {
        this.map = new Map_Location(towns);
    }
    public void move(ArrayList<Town> towns) {        //nahodny pohyb po mestach
        int randomTownIncluding = new Random().nextInt(0,towns.size());
        this.map = towns.get(randomTownIncluding).getMap();
    }
    public void move(Town town) {           //pohyb do daneho mesta
        this.map = town.getMap();
    }
    public void action(Town town,ArrayList<Comodity> demandList, int gameDay) {
    }
    public void action(ArrayList<Town> towns,ArrayList<Comodity> demandList, int gameDay) {

    }
    public void infoEntity() {
        System.out.println(this.entityType);
        System.out.println("Coordinates: "+this.getMap().getX()+", "+this.getMap().getY()+", "+this.getMap().getZ());
    }
    public Map_Location getMap() {
        return map;
    }
    public void setMap(Map_Location map) {
        this.map = map;
    }
    public String getEntityType() {
        return entityType;
    }
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    public int getMoney() {return 0;}
    public void setMoney(int money) {}
    public Car getCar() {
        return null;
    }
    public void setCar(Car car) {}
}