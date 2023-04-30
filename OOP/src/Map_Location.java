import java.util.ArrayList;
import java.util.Random;
public class Map_Location {
    private final int x;
    private final int y;
    private final int z;
    //TODO kniznice transport to game
    public Random random = new Random();
    Map_Location(int startRadius) {     //uplne nahodna generacia
        this.x = random.nextInt(-startRadius,startRadius);
        this.y = random.nextInt(-startRadius,startRadius);
        this.z = random.nextInt(-startRadius,startRadius);
    }
    Map_Location(ArrayList<Town> towns) {    //nahodna generacia z vsetkych miest
        int temp = random.nextInt(0,towns.size());
        this.x = towns.get(temp).getMap().getX();
        this.y = towns.get(temp).getMap().getY();
        this.z = towns.get(temp).getMap().getZ();
    }
    Map_Location(Town town,int maxRadiusDistance) {      //nahodna generacia v radiuse
        this.x = town.getMap().getX() + random.nextInt(-maxRadiusDistance,maxRadiusDistance);
        this.y = town.getMap().getX() + random.nextInt(-maxRadiusDistance,maxRadiusDistance);
        this.z = town.getMap().getX() + random.nextInt(-maxRadiusDistance,maxRadiusDistance);
    }
    public double distance(Map_Location x) {         //vypocet vzdialenosti
        double X = Math.pow(x.getX()-this.getX(),2);
        double Y = Math.pow(x.getY()-this.getY(),2);
        double Z = Math.pow(x.getZ()-this.getZ(),2);
        double sum = Math.round(Math.sqrt(X+Y+Z)*100);
        return sum/100;
    }
    public double slope(Map_Location x) {           //vypocet sklonu cesty
        double X = Math.pow(x.getX() - this.getX(), 2);
        double Y = Math.pow(x.getY() - this.getY(), 2);
        double Z = Math.pow(x.getZ() - this.getZ(), 2);

        double sum = Math.round(Math.toDegrees(Math.acos(Math.sqrt(X + Y) / Math.sqrt(X + Y + Z))) * 100);
        if (this.getZ() > x.getZ())
            return -sum / 100;
        else
            return sum / 100;
    }
    public int radius(ArrayList<Town> list,int diameter) {
        int temp = 0;
        for(Town town: list)
        {
            if(this.getX() - town.getMap().getX() <= diameter && this.getY() - town.getMap().getY() <= diameter && this.getZ() - town.getMap().getZ() <= diameter)
                temp++;
        }
        return temp;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
}