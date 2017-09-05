package monash.emission.entity;

/**
 * Created by 80576 on 2017/9/3.
 */

public class SortingEntity {
    private String name;
    private double value;

    public SortingEntity(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
