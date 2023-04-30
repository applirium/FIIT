import java.util.ArrayList;
import java.util.Random;
public class Population_Town extends Town
{
    private ArrayList<Comodity> demand;
    private int reward;
    Population_Town(ArrayList<Comodity> comodityDemandAll) {                                        //generacia mesta na bez polohy
        super();
        this.setTownType("Population");
        this.setName("Population"+ new Random().nextInt(999));
        this.demand = new ArrayList<>();
        for (Comodity comodity : comodityDemandAll) {
            this.demand.add(new Comodity(comodity.getComodityType(), 0));
        }
    }
    Population_Town(int startRadius,ArrayList<Comodity> comodityDemandAll) {                        //generacia mesta na zaciatku hry
        this(comodityDemandAll);
        this.setMap(new Map_Location(startRadius));
    }
    Population_Town(ArrayList<Comodity> comodityDemandAll,Town town,int maxRadiusDistance) {        //generacia mesta v priebehu hry
        this(comodityDemandAll);
        this.setMap(new Map_Location(town,maxRadiusDistance));
        town.setPeople(town.getPeople() - 5);
    }
    @Override
    public void infoTown() {
        super.infoTown();
        System.out.println("Reward: "+this.reward);
        System.out.print("Demand: ");
        for (Comodity comodity : this.demand) {
            System.out.print("| " +comodity.getComodityType() + " " + comodity.getComodityIO()+" |");
        }
        System.out.println("\n");
    }
    @Override
    public void IO(Entity entity, ArrayList<Comodity> demandList, int gameDay) {     //transfering comodits car -> town
        int input, demandSum = 0;
        for(Comodity demand: this.demand)           //celkovy pocet tovarov v jednom meste
        {
            demandSum += demand.getComodityIO();
        }
        for(int i = 0; i < entity.getCar().getActualInventory().size(); i++)        //loop medzi inventarmi
        {
            ArrayList<Comodity> carInventory = entity.getCar().getActualInventory();
            if(this.demand.get(i).getComodityIO() > carInventory.get(i).getComodityIO())
            {
                input = carInventory.get(i).getComodityIO();
                demandSum -= carInventory.get(i).getComodityIO();
                this.demand.get(i).setComodityIO(this.demand.get(i).getComodityIO() - carInventory.get(i).getComodityIO());
                demandList.get(i).setComodityIO(demandList.get(i).getComodityIO() - carInventory.get(i).getComodityIO());
                carInventory.get(i).setComodityIO(0);
            }
            else
            {
                input = this.demand.get(i).getComodityIO();
                demandSum -= this.demand.get(i).getComodityIO();
                carInventory.get(i).setComodityIO(carInventory.get(i).getComodityIO() - this.demand.get(i).getComodityIO());
                this.demand.get(i).setComodityIO(0);
                demandList.get(i).setComodityIO(0);
            }
            if(input != 0)
                System.out.println("Day "+gameDay+ " Player delivered "+ input +" "+ this.getDemand().get(i).getComodityType() + " to "+this.getName());
        }
        if(demandSum == 0)
        {
            entity.setMoney(entity.getMoney() + this.reward);
            this.reward = 0;
        }
    }
    public ArrayList<Comodity> getDemand() {
        return demand;
    }
    public void setDemand(ArrayList<Comodity> demand) {
        this.demand = demand;
    }
    public void setDemand(int index,int quantity) {
        this.demand.get(index).setComodityIO(quantity);
    }
    public int getReward() {
        return reward;
    }
    public void setReward(int reward) {
        this.reward = reward;
    }
}