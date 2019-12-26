package Skystone_14999.Parameters;

public class ParameterHM {

    public enum instanceType {powerLimit, counts, toleranceCounts, distanceInches, rotationDegrees, servoPosition}

    public boolean hasRange = true;
    public double min = -999;
    public double max = 999;
    public double increment = 0;
    public double value = 0;
    public instanceType paramType;

    public ParameterHM(double inputValue, instanceType type) {

        switch (type) {

            case powerLimit :
                value = inputValue;
                min = -1;
                max = 1;
                increment = 0.01;
                paramType = type;
                break;

            case counts :
                value = inputValue;
                hasRange = false;
                increment = 100;
                paramType = type;
                break;

            case toleranceCounts :
                value = inputValue;
                hasRange = false;
                increment = 1;
                paramType = type;
                break;

            case distanceInches :
                value = inputValue;
                hasRange = false;
                increment = .5;
                paramType = type;
                break;

            case rotationDegrees :
                value = inputValue;
                hasRange = false;
                increment = 5;
                paramType = type;
                break;

            case servoPosition :
                value = inputValue;
                hasRange = true;
                min = 0;
                max = 1;
                increment = .1;
                paramType = type;
                break;
        }

    }

    public void clipParameter() {

        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }
    }

    public void increaseParameter() {

        value += increment;
        clipParameter();
    }

    public void decreaseParameter() {

        value -= increment;
        clipParameter();
    }

    public void setParameter(double inputValue) {

        value = inputValue;
        clipParameter();
    }

    public int integerParameter() {
        int intValue;

     intValue = (int) Math.round(value);

     return intValue;
    }

}