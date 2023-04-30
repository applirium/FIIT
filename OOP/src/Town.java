import java.util.ArrayList;
public abstract class Town
{
    private int energy;
    private int happiness;
    private int development;
    private String name;
    private int people;
    private Map_Location map;
    private String townType;
    Town() {
        this.energy = 30;
        this.people = 5;
        this.development = 1;
        this.happiness = 100;
    }
    public void infoTown() {
        System.out.println(this.townType);
        System.out.println("Coordinates: "+this.map.getX()+", "+this.map.getY()+", "+this.map.getZ());
        System.out.println("Development: "+this.development);
        System.out.println("Energy: "+this.energy);
        System.out.println("Name: "+this.name);
        System.out.println("Happiness: "+this.happiness);
        System.out.println("People: "+this.people);
    }

    public void IO(Entity entity,ArrayList<Comodity> demandList, int gameDay) {
    }
    public int getHappiness() {
        return happiness;
    }
    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
    public int getDevelopment() {
        return development;
    }
    public void setDevelopment(int development) {
        this.development = development;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPeople() {
        return people;
    }
    public void setPeople(int people) {
        this.people = people;
    }
    public Map_Location getMap() {
        return map;
    }
    public void setMap(Map_Location map) {
        this.map = map;
    }
    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public String getTownType() {
        return townType;
    }
    public void setTownType(String townType) {
        this.townType = townType;
    }
    public ArrayList<Comodity> getDemand() {return null;}
    public void setDemand(ArrayList<Comodity> demand) {}
    public void setDemand(Comodity demand,int index) {}
    public void setDemand(int index,int quantity) {}
    public int getReward() {return 0;}
    public void setReward(int reward) {}
    public Comodity getProduction(){return null;}
    public void setProduction(Comodity production) {}
    public int getProductionPerTick() {return 0;}
    public void setProductionPerTick(int productionPerTick) {}
}