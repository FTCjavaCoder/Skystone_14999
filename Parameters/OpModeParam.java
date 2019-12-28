package Skystone_14999.Parameters;

public class OpModeParam {

    public String teamColor =  "Red";       // options Red/Blue
    public String startSide = "Front";      // options Front/Back
    public String bridgeSide = "In";        // options In/Out
    public String SkyStoneDrop = "NotMoved";   // options NotMoved/Moved
    public double startDirection = 0;       // options 0/180 degrees
    public int caseSelect = 0;              // options 0,1,2,3,4... max number of cases
    public String selected = "N";           // options "Y"/"N"

    public OpModeParam() {


    }
    public OpModeParam(String tc, String ss, String bs, String ssd, double sd, int cs, String sel){
        //Constructor to make to parameter
        teamColor =  tc;        // options Red/Blue
        startSide = ss;         // options Front/Back
        bridgeSide = bs;        // options In/Out
        SkyStoneDrop = ssd;     // options NotMoved/Moved
        startDirection = sd;    // options 0/180 degrees
        caseSelect = cs;        // options 0,1,2,3,4... max number of cases
        selected = sel;  // options "Y"/"N"
    }

    public void setParams(String tc, String ss, String bs, String ssd, double sd, int cs,String sel){
        //Method to set the values of a parameter
        teamColor =  tc;        // options Red/Blue
        startSide = ss;         // options Front/Back
        bridgeSide = bs;        // options In/Out
        SkyStoneDrop = ssd;     // options NotMoved/Moved
        startDirection = sd;    // options 0/180 degrees
        caseSelect = cs;        // options 0,1,2,3,4... max number of cases
        selected = sel;  // options "Y"/"N"

    }
    public void setSelect(String sel){
        //Method to set the OpMpde tp the selected value
        selected = sel;  // options "Y"/"N"

    }

}