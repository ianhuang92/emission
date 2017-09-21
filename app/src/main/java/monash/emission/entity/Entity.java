package monash.emission.entity;

/**
 * Created by Ranger on 2017/8/13.
 */

public abstract class Entity {
    public  int MW;
    public  int EW;
    public  String name;

    public Entity(int MW, int EW, String name) {
        this.MW = MW;
        this.EW = EW;
        this.name = name;
    }


}
