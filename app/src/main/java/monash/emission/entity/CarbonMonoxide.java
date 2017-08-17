package monash.emission.entity;

/**
 * Created by Ranger on 2017/8/13.
 */

public class CarbonMonoxide extends Entity{

    public static int MW = 28;
    public static int EW = 12;
    public static String name = "CarbonMonoxide";

    public CarbonMonoxide() {
        super(MW, EW, name);
    }
}
