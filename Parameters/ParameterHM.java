package Skystone_14999.Parameters;

public class ParameterHM {

//Power Limits
/** ParamHN(value, power_limit) **/

    public enum instanceType {powerLimit, counts}

    public boolean hasRange = true;
    public double min;
    public double max;
    public double increment;
    public double value;

    public ParameterHM(double inputValue, instanceType type) {

        switch (type) {

            case powerLimit :
                value = inputValue;
                min = -1;
                max = 1;
                increment = 0.05;
                break;

            case counts :
                value = inputValue;
                hasRange = false;
                increment = 100;
                break;
        }

    }

}
