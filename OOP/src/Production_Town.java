import java.util.ArrayList;
import java.util.Random;

public class Production_Town extends Town
{
    private Comodity production;
    private int productionPerTick;
    Production_Town(Comodity comodity,int startingGeneration) {    //generacia mesta na bez polohy
        super();
        this.setTownType("Production");
        this.setName("Production"+ new Random().nextInt(999));
        this.production = comodity;
        this.productionPerTick = startingGeneration;
    }
    Production_Town(int startRadius, Comodity comodity,int startingGeneration) {    //generacia mesta na zaciatku hry
        this(comodity,startingGeneration);
        this.setMap(new Map_Location(startRadius));
        this.productionPerTick = startingGeneration;
    }
    Production_Town(Comodity comodity,Town town,int startingGeneration,int maxRadiusDistance) { //generacia mesta v priebehu hry
        this(comodity,startingGeneration);
        this.setMap(new Map_Location(town,maxRadiusDistance));
        town.setPeople(town.getPeople() - 5);
    }
    @Override
    public void infoTown() {
        super.infoTown();
        System.out.println("Type of production: "+this.production.getComodityType());
        System.out.println("Production per tick: "+this.productionPerTick);
        System.out.println("Actual inventory: " +this.production.getComodityIO()+"\n");
    }
    @Override
    public void IO(Entity entity, ArrayList<Comodity> demandList, int gameDay) {     //transfering comodits town -> car
        int productionMax, inventorySum,input;
        Car car = entity.getCar();                                          //Object of car
        for(int i = 0; i < car.getActualInventory().size(); i++)
        {
            Comodity inverntory = car.getActualInventory().get(i);
            if(inverntory.getComodityType().equals(this.production.getComodityType()))  //finding index of produced comodity
            {
                inventorySum = 0;
                for(Comodity sameInventory: car.getActualInventory())            //chcek ci sucet sa zmesti do inventaru
                {
                    inventorySum += sameInventory.getComodityIO();
                }

                productionMax = this.production.getComodityIO();
                if(demandList.get(i).getComodityIO() <= inverntory.getComodityIO() + productionMax)
                    productionMax = demandList.get(i).getComodityIO() - inverntory.getComodityIO();            //uprava produkcie aby sa do inventaru nebrali suroviny, ktore nie su pozadovane

                if(inventorySum + productionMax <= car.getCarryingCapacity() && inverntory.getComodityIO() + productionMax <= demandList.get(i).getComodityIO())     //chcek ci sucet sa zmesti do inventaru a nie je hranicny s demand listom
                {
                    input = productionMax;
                    inverntory.setComodityIO(inverntory.getComodityIO() + productionMax);
                    this.production.setComodityIO(this.production.getComodityIO() - productionMax);
                }
                else
                {
                    input = car.getCarryingCapacity() - inventorySum ;
                    this.production.setComodityIO(this.production.getComodityIO() + inventorySum - car.getCarryingCapacity());
                    inverntory.setComodityIO(inverntory.getComodityIO() - inventorySum + car.getCarryingCapacity());      //ak nie max
                }

                if(input != 0)
                    System.out.println("Day "+gameDay+ " Player picked up "+ input +" "+ this.production.getComodityType() + " from "+this.getName());
                break;
            }
        }
    }
    public Comodity getProduction() {
        return production;
    }
    public void setProduction(Comodity production) {
        this.production = production;
    }
    public int getProductionPerTick() {
        return productionPerTick;
    }
    public void setProductionPerTick(int productionPerTick) {
        this.productionPerTick = productionPerTick;
    }
}