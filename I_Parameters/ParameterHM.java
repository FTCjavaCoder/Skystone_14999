package Skystone_14999.I_Parameters;

public class ParameterHM {

//Power Limits
/** ParamHN(value, power_limit) **/

public enum instanceType {powerLimit, counts}

    public boolean hasRange = true;
    public double min;
    public double max;
    public double increment;

    public ParameterHM(double value, instanceType type) {

    switch (type) {

        case powerLimit :
            min = -1;
            max = 1;
            increment = 0.05;
            break;

        case counts :
            hasRange = false;
            increment = 100;
            break;
    }

    }

}
