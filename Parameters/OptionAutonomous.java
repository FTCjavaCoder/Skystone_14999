package Skystone_14999.Parameters;

public class OptionAutonomous {

    public enum name {skyStoneOutside, skyStoneInside, skyStoneOutsideUnmoved, skyStoneInsideUnmoved, foundationOutside, foundationInside}

    public int optionNumber = -1;
    public String description = "None";
    public name optionName;

    public OptionAutonomous(double inputValue, name option) {

        switch (option) {

            case skyStoneOutside :
                description = "SkyStone side. Crosses bridge on outside of field near perimeter wall. PLaces SkyStone on moved Foundation. Parks outside as well.";
                optionNumber = -1;
                optionName = option;
                break;

            case skyStoneInside :
                description = "SkyStone side. Crosses bridge on inside of field away near center bridge. Places SkyStone on moved Foundation. Parks inside as well.";
                optionNumber = -1;
                optionName = option;
                break;

            case skyStoneOutsideUnmoved :
                description = "SkyStone side. Crosses bridge on outside of field near perimeter wall. PLaces SkyStone on unmoved Foundation. Parks outside as well.";
                optionNumber = -1;
                optionName = option;
                break;

            case skyStoneInsideUnmoved :
                description = "SkyStone side. Crosses bridge on inside of field away near center bridge. PLaces SkyStone on unmoved Foundation. Parks inside as well.";
                optionNumber = -1;
                optionName = option;
                break;

            case foundationOutside :
                description = "Foundation side. . Parks outside as well.";
                optionNumber = -1;
                optionName = option;
                break;

            case foundationInside :
                description = "Foundation side. . Parks inside as well.";
                optionNumber = -1;
                optionName = option;
                break;

        }

    }

//    public void clipParameter() {
//
//        if (value > max) {
//            value = max;
//        }
//        if (value < min) {
//            value = min;
//        }
//    }
//
//    public void increaseParameter() {
//
//        value += increment;
//        clipParameter();
//    }
//
//    public void decreaseParameter() {
//
//        value -= increment;
//        clipParameter();
//    }
//
//    public void setParameter(double inputValue) {
//
//        value = inputValue;
//        clipParameter();
//    }
//
//    public int integerParameter() {
//        int intValue;
//
//     intValue = (int) Math.round(value);
//
//     return intValue;
//    }

}