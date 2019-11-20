package Skystone_14999.Parameters;

public class OptionAutonomous {

    public enum name {skyStoneOutside, skyStoneInside, skyStoneOutsideUnmoved, skyStoneInsideUnmoved, foundationOutside, foundationInside}

    public int optionNumber = -1;
    public String description = "None";
    public name optionName;

    public OptionAutonomous(name option) {

        switch (option) {

            case skyStoneOutside :
                optionName = option;
                optionNumber = 1;
                description = "SkyStone side. Crosses bridge on outside of field near perimeter wall. PLaces SkyStone on moved Foundation. Parks outside as well.";
                break;

            case skyStoneInside :
                optionName = option;
                optionNumber = 2;
                description = "SkyStone side. Crosses bridge on inside of field away near center bridge. Places SkyStone on moved Foundation. Parks inside as well.";
                break;

            case skyStoneOutsideUnmoved :
                optionName = option;
                optionNumber = 3;
                description = "SkyStone side. Crosses bridge on outside of field near perimeter wall. PLaces SkyStone on unmoved Foundation. Parks outside as well.";
                break;

            case skyStoneInsideUnmoved :
                optionName = option;
                optionNumber = 4;
                description = "SkyStone side. Crosses bridge on inside of field away near center bridge. PLaces SkyStone on unmoved Foundation. Parks inside as well.";
                break;

            case foundationOutside :
                optionName = option;
                optionNumber = 5;
                description = "Foundation side. . Parks outside.";
                break;

            case foundationInside :
                optionName = option;
                optionNumber = 6;
                description = "Foundation side. . Parks inside.";
                break;

        }

    }


}