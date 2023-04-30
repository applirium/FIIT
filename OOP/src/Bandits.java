import java.util.ArrayList;
public class Bandits extends Entity {
    Bandits(ArrayList<Town> towns) {
        super(towns);
        this.setEntityType("Bandits");
    }
    //TODO celu logika bandita
    @Override
    public void action(ArrayList<Town> towns,ArrayList<Comodity> demandList, int gameDay) {
        this.move(towns);
    }
    @Override
    public void infoEntity() {
        super.infoEntity();
        System.out.println("\n");
    }
}