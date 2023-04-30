import java.util.*;
public class Game
{
    private int gameDay;
    private ArrayList<Town> townList;       //list vsetkych aktivnych miest
    private ArrayList<Entity> entityList;   //list vsetkych aktivnych entit
    private ArrayList<ArrayList<Town>> dividedTownList; //list rozdelenych vsetkych miest PRODUCTION - POPULATION
    private ArrayList<Town> path;               //list miest naplanovanej cesty
    private ArrayList<ArrayList<Town>> pathHistory;     //list miest naplanovanej cesty
    private ArrayList<Comodity> demandList;     //list vsetkych pozadovancych komodit
    private ArrayList<Comodity>  comodityListAll;       //list vsetkych existujucich komodit
    Random random = new Random();
    Scanner scanner = new Scanner(System.in);
    public static void main(String[] args)
    {
        Game game = new Game();
        game.gameStart();
    }
    public void gameStart() {
        boolean start = true;
        gameDay = 1;
        townList = new ArrayList<>();
        path = null;
        pathHistory = new ArrayList<>();
        entityList = new ArrayList<>();
        comodityListAll = new ArrayList<>();
        demandList = new ArrayList<>();
        dividedTownList = new ArrayList<>();
        comodityListAll.add(new Comodity("Wood",3));        //natvrdo vytvorenie comodityListAll
        comodityListAll.add(new Comodity("Water",3));
        comodityListAll.add(new Comodity("Copper",5));
        comodityListAll.add(new Comodity("Iron",5));
        comodityListAll.add(new Comodity("Steel",7));
        comodityListAll.add(new Comodity("Processor",12));
        comodityListAll.add(new Comodity("Gene",21));
        comodityListAll.add(new Comodity("Robotics",40));

        for(int i = 0; i<comodityListAll.size(); i++)
        {
            if(i < 2)
                dividedTownList.add(new ArrayList<>());

            this.demandList.add(new Comodity(comodityListAll.get(i).getComodityType(),0));  //vytvorenie divided listu a inventar demand listu
        }
        while(start)
        {
            System.out.println("Choose your game difficulty Easy / Moderate / Hard");       //obtiaznost

            String gameDifficulty = scanner.nextLine().toLowerCase();
            switch(gameDifficulty)
            {
                case "easy"->{
                    start = startSettings(25,1,1,10,300);    //parametre ako sa vytvori hra pomocou obtiaznosti
                    gameLoopDecisionLogic(20,1,200,300,70,5,50);
                }
                case "moderate"->{
                    start = startSettings(100,2,2,7,500);
                    gameLoopDecisionLogic(50,2,100,500,80,5,70);
                }
                case "hard"->{
                    start = startSettings(250,3,3,5,1000);
                    gameLoopDecisionLogic(100,3,50,1000,90,5,100);
                }
                default -> System.out.println("Invalid Input");
            }
        }
    }
    public boolean startSettings(int startRadius,int populationTownStart,int banditStart,int startingGeneration, int maxStartDemand) {
        for(int i = 0; i < 8; i++)
        {
            Comodity TownInventory = new Comodity(comodityListAll.get(i).getComodityType(),0);
            Town townProduction = new Production_Town(startRadius,TownInventory,startingGeneration);

            dividedTownList.get(0).add(townProduction);
            townList.add(townProduction);       //vytvorenie zaciatocnych miest a entit
        }

        for(int i = 0; i < populationTownStart; i++)
        {
            Town townPopulation = new Population_Town(startRadius,comodityListAll);

            dividedTownList.get(1).add(townPopulation);
            townList.add(townPopulation);
        }

        entityList.add(new Player(dividedTownList.get(1),comodityListAll));

        for(int i = 0; i < banditStart; i++)
        {
            entityList.add(new Bandits(dividedTownList.get(0)));
        }

        demandList(maxStartDemand);
        return false;
    }
    public void gameLoopDecisionLogic(int maxRadiusDistance, int degeneration,int maxProductionInventory,int maxStartDemand,int evolutionFactor, int startingGeneration,int demandEvolving) {
        while(dividedTownList.get(1).size() != 0)
        {
            if(path == null)
            {
                System.out.print("Day: "+gameDay+" ");
                infoComodity(demandList);
            }

            System.out.println("Choose a decision - To plan / To action / To get informations / Help to get explanation");      //loop planovania a hrania
            switch (scanner.nextLine().toLowerCase())
            {
                case "plan" ->{
                    pathSettingsIO(maxRadiusDistance, maxProductionInventory, degeneration);
                    Town lastTown = dividedTownList.get(1).get(0);

                    for(Town town: dividedTownList.get(1))
                    {
                        if(entityList.get(0).getMap() == town.getMap())
                        {
                            lastTown = town;
                            break;
                        }
                    }
                    int i = 0;
                    for (Town town : path) {
                        if(!lastTown.getName().equals(town.getName()))
                        {
                            System.out.println("Day " + (gameDay + i) + " player will travel from " + lastTown.getName() + " to " + town.getName());
                            lastTown = town;
                        }
                        i++;
                    }
                }
                case "action" -> {

                    try{
                        for(Town town: path)    //zbehnutie celej naplanovanej cesty
                        {
                            ActionPlay(town,maxProductionInventory,degeneration);
                            postActionEvolution(maxRadiusDistance,evolutionFactor,startingGeneration);
                        }
                        reset(maxStartDemand,demandEvolving);
                    }
                    catch(NullPointerException e){
                        System.out.println("Cannot act without path");
                    }

                }
                case "info" -> {
                    infoComodity(demandList);               //informacie o entitach a mestach
                    infoComodity(comodityListAll);
                    infoEntityAll(entityList);
                    infoTownAll(townList);
                }
                case "help" -> {
                    System.out.println("Plan = creating potential route");
                    System.out.println("Action = playing last planned route");
                }
                default -> System.out.println("Invalid Input");
            }
        }
        System.out.println("All your population towns died, cannot deliver");
        System.out.println("Days survived "+ gameDay +"\nGame over");
    }
    public void postActionEvolution(int maxRadiusDistance,int evolutionFactor,int startingGeneration) {
        for(Town town: townList)
        {
            if(town.getTownType().equals("Population"))
            {
                if(town.getEnergy() > evolutionFactor)          //vytvaranie novych miest
                {
                    Town newTown = new Population_Town(comodityListAll,town,maxRadiusDistance);       //vytvorenie noveho mesta v radiuse mesta, kde boli splnene podmienky
                    townList.add(newTown);
                    dividedTownList.get(0).add(newTown);
                }
                if(town.getPeople() == 0)
                {
                    System.out.println("Town " + town.getName() + " died.");
                    dividedTownList.get(1).remove(town);
                    townList.remove(town);
                }
            }

        }
    }
    public void reset(int maxStartDemand,int demandEvolving) {
        demandList(maxStartDemand += demandEvolving);
        dividedTownList.get(0).clear();
        dividedTownList.get(1).clear();
        path = null;
        for(Town town: townList)
        {
            try
            {
                town.getProduction().setComodityIO(0);
            }
            catch (Exception ignored){}

            if(town.getTownType().equals("Production"))
            {
                dividedTownList.get(0).add(town);
            }
            else {
                dividedTownList.get(1).add(town);
            }
        }
    }
    public void pathSettingsIO(int maxRadiusDistance,int maxProductionInventory,int degeneration) {
        do {
            System.out.println("Choose to create path - Money path / Proserity path / Fuel path / Exploring path / Help to get explanation");           //typy naplanovatelnych ciest
            String input = scanner.nextLine().toLowerCase();
            switch (input) {
                case "money path", "prosperity path", "saving path", "fuel path", "exploring path" -> pathSettings(maxProductionInventory,degeneration,populationPath(input, maxRadiusDistance));          //podla typu vygenerovana cesta
                case "help" -> {
                    System.out.println("Money path = Route with maximum profit");
                    System.out.println("Prosperity path = Route focusing on highly developed cities");
                    System.out.println("Saving path = Route focusing on poorly developed cities");
                    System.out.println("Fuel path = Route with minimum distance");
                    System.out.println("Exploring path = Route focusing on remote cities");
                }
                default -> System.out.println("Invalid Input");
            }
        }
        while(path == null);
    }
    public ArrayList<Town> populationPath(String input,int maxRadiusDistance) {
        ArrayList<Town> temp = new ArrayList<>();
        ArrayList<Town> townCopyPop = new ArrayList<>(dividedTownList.get(1));

        for(int i = townCopyPop.size() - 1; i >= 0 ; i--)
        {
            Town tempLoop1 = townCopyPop.get(i);
            for (int j = i ; j >= 0; j--)
            {
                Town tempLoop2 = townCopyPop.get(j);
                switch (input)
                {
                    case "money path" -> {
                        if (tempLoop2.getReward() > tempLoop1.getReward())
                            tempLoop1 = tempLoop2;
                    }
                    case "prosperity path" -> {
                        if (tempLoop2.getDevelopment() < tempLoop1.getDevelopment())
                            tempLoop1 = tempLoop2;
                    }
                    case "saving path" -> {
                        if (tempLoop2.getDevelopment() > tempLoop1.getDevelopment())
                            tempLoop1 = tempLoop2;
                    }
                    case "fuel path" ->{
                        if(entityList.get(0).getMap().distance(tempLoop2.getMap()) > entityList.get(0).getMap().distance(tempLoop1.getMap()))
                            tempLoop1 = tempLoop2;
                    }
                    case "exploreing path" ->{
                        if (tempLoop2.getDevelopment() > tempLoop1.getDevelopment())
                            if (tempLoop2.getMap().radius(townList, maxRadiusDistance) > tempLoop1.getMap().radius(townList, maxRadiusDistance))
                                tempLoop1 = tempLoop2;
                    }
                }
            }
            temp.add(tempLoop1);
            townCopyPop.remove(tempLoop1);
        }
        return temp;
    }
    //TODO upravenie robenia cesty
    //TODO pocitanie celkovej vzdialenosti cesty
    public void pathSettings(int maxProductionInventory,int degeneration, ArrayList<Town> townPop) {
        path = new ArrayList<>();
        ArrayList<Town> townCopyProd = new ArrayList<>(dividedTownList.get(0));

        for (Town town : townPop) {             //loop populacnymi mestami
            for (int i = 0; i < townCopyProd.size(); i++)    // loop medzi produkcnymi mestami a inventarmi
            {
                if (town.getDemand().get(i).getComodityIO() != 0 && town.getDemand().get(i).getComodityType().equals(townCopyProd.get(i).getProduction().getComodityType()))  //ak sa pozadovana surovina rovna s produkciou
                {
                    float result = ((float) town.getDemand().get(i).getComodityIO() - townCopyProd.get(i).getProduction().getComodityIO()) / townCopyProd.get(i).getProductionPerTick();
                    float maxLoads = (float) entityList.get(0).getCar().getCarryingCapacity() / townCopyProd.get(i).getProductionPerTick();

                    result = (float) Math.ceil(result);
                    maxLoads = (float) Math.ceil(maxLoads);

                    while (result > maxLoads) {
                        for (int j = 0; j < (int) maxLoads; j++) {
                            path.add(dividedTownList.get(0).get(i));
                        }
                        path.add(town);
                        result -= maxLoads;
                    }

                    for (int j = 0; j < (int) result; j++) {
                        path.add(dividedTownList.get(0).get(i));
                    }
                    path.add(town);
                }
            }
        }
        pathHistory.add(path);
    }
    public void demandList(int maxDemand) {
        int oneComodit, numberOfComodits = dividedTownList.get(1).size() * comodityListAll.size();

        for(Town town: dividedTownList.get(1))  //loop medzi vsetkymi populacnymi mestami
        {
            for(int i = comodityListAll.size()-1; i >= 0 ; i--) //loop medzi produkovanymi komoditami
            {
                if(numberOfComodits <= 2)
                    oneComodit = Math.round((float)(maxDemand / numberOfComodits));

                else
                {
                    oneComodit =  Math.round((float)(maxDemand / numberOfComodits)) + random.nextInt(-10,10);
                    if(oneComodit < 0)
                        oneComodit = 0;
                }

                town.setDemand(i, oneComodit);   //nastavenie inventaru mesta na vypocitanu hodnotu
                town.setReward(oneComodit * comodityListAll.get(i).getComodityIO() + town.getReward());  //nastavenie odmeny pre danu komoditu
                demandList.get(i).setComodityIO(demandList.get(i).getComodityIO() + oneComodit);         //pridanie to do listu, kde je sucet poziadaviek pre vsetky mesta

                maxDemand -= oneComodit;
                numberOfComodits--;
            }
        }
    }
    public void ActionPlay(Town town, int maxProductionInventory, int degeneration) {
        oneDay(maxProductionInventory,degeneration,townList);
        for(int i = 0; i < entityList.size(); i++)      //planovanie pohybu pohyb hraca do daneho mesta
        {
            Entity entity = entityList.get(i);
            if(i == 0)
                entity.action(town, demandList,gameDay);
            else
                entity.action(townList, demandList,gameDay);
        }
    }
    public void oneDay(int maxProductionInventory,int degeneration,ArrayList<Town> townList) {
        for (Town town: townList) {
            if (town.getEnergy() > 0)
                town.setEnergy(town.getEnergy() - degeneration);        //odpocet energie
            else if (town.getPeople() > 0)
                town.setPeople(town.getPeople() - degeneration);        //umieranie ludi
            try
            {
                town.getProduction().setComodityIO(Math.min(town.getProductionPerTick() + town.getProduction().getComodityIO(), maxProductionInventory));
            }
            catch (NullPointerException ignored)
            {
                town.setReward(town.getReward() - degeneration);        //ak sa do inverntaru mesta zmensti produkia zvysi sa, ak nie zvysi sa na maximum
            }
        }
        gameDay += 1;
    }
    public int sumOfComodity(ArrayList<Comodity> comodity) {
        int temp = 0;
        for(Comodity oneComodity: comodity)
        {
            temp += oneComodity.getComodityIO();
        }
        return temp;
    }
    public void infoComodity(ArrayList<Comodity> comodity) {
        for (Comodity item:comodity)
        {
            System.out.print("| " +item.getComodityType() + " " + item.getComodityIO() +" |");
        }
        System.out.println("\n");
    }
    public void infoEntityAll(ArrayList<Entity> entities) {
        for(Entity entity: entities)
        {
            entity.infoEntity();
        }
    }
    public void infoTownAll(ArrayList<Town> towns) {
        for(Town town: towns)
        {
            town.infoTown();
        }
    }
}