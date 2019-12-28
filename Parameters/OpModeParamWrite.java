package Skystone_14999.Parameters;

import java.io.IOException;
import java.io.InputStreamReader;

import Skystone_14999.OpModes.BasicOpMode;


public class OpModeParamWrite extends BasicOpMode {
    public static String loadFileName = "OpModeHashMapFile.txt";
    public static InputStreamReader cin = null;

    public void checkoutFiles()throws IOException {
        telemetry.addData("Status", "Starting Code");
        telemetry.update();


        telemetry.addData("Reading Hash Map from File", "%s", loadFileName);
        telemetry.update();

        ompf.loadFromPC(loadFileName, this);

        if (!loadFile) {
            //HashMap hasn't been created, need to create file
            telemetry.addLine("File Did Not Exist, Creating Hash Map and saving to File");
            telemetry.update();

            ompf.defineParameters();
            ompf.saveToFile(loadFileName, this);
        }

        telemetry.addLine("Reading Hash Map from File to New Map");
        telemetry.addLine(String.format("Reading from", "%s", loadFileName));
        telemetry.update();
        ompf.loadFromPC(loadFileName, this);





    }
    public void editHashMap()throws IOException {
        try {
            cin = new InputStreamReader(System.in);
            char c = ' ';
            for(String s : ompf.omp.keySet()) {

                // X to EDIT || B to SKIP
                telemetry.addData("OpMode Name", "%s", s);
//                om.telemetry.addData("Team Color", "%s", omp.get(s).teamColor);
//                om.telemetry.addData("Start Side", "%s", omp.get(s).startSide);
//                om.telemetry.addData("Bridge Side", "%s", omp.get(s).bridgeSide);
//                om.telemetry.addData("SkyStone Drop", "%s", omp.get(s).SkyStoneDrop);
//                om.telemetry.addData("Start Direction", "%.1f", omp.get(s).startDirection);
//                om.telemetry.addData("Case Select", "%d", omp.get(s).caseSelect);
                telemetry.addData("Selected OpMode", "%s", ompf.omp.get(s).selected);
                telemetry.addLine("Enter 'E' to EDIT or 'S' to SKIP:");
                telemetry.update();
                while(c!='E' && c != 'S'){
                    c = (char) cin.read();
                }
                telemetry.addLine(String.format("Input recorded as %s\n",c));
                telemetry.update();

                if(c =='E') {

                    telemetry.addData("OpMode Name", "%s", s);
                    telemetry.addData("Team Color", "%s", ompf.omp.get(s).teamColor);
                    telemetry.addData("Start Side", "%s", ompf.omp.get(s).startSide);
                    telemetry.addData("Bridge Side", "%s", ompf.omp.get(s).bridgeSide);
                    telemetry.addData("SkyStone Drop", "%s", ompf.omp.get(s).SkyStoneDrop);
                    telemetry.addData("Start Direction", "%.1f", ompf.omp.get(s).startDirection);
                    telemetry.addData("Case Select", "%d", ompf.omp.get(s).caseSelect);
                    telemetry.addData("Selected OpMode", "%s", ompf.omp.get(s).selected);
                    telemetry.addLine("Enter 'Y' to SELECT,  'N' UNSELECT or 'C' to CONFIRM");
                    telemetry.update();
                    while(c!='Y' && c != 'N' && c != 'C') {
                        c = (char) cin.read();
                    }
                    telemetry.addLine(String.format("Input recorded as %s\n",c));
                    telemetry.update();
                    if(c == 'Y') {

                        ompf.omp.get(s).setSelect("Y");
                    }
                    if(c == 'N') {

                        ompf.omp.get(s).setSelect("N");
                    }
                    if(c == 'C') {

                        telemetry.addLine("Selection Confirmed");
                        telemetry.update();
                    }

                }

                if(c == 'S') {

                    telemetry.addData("Skipped","%s", s);
                    telemetry.addData("Selected OpMode", "%s", ompf.omp.get(s).selected);
                    telemetry.addLine("____________________________");
                    telemetry.update();
                }
                else {
                    telemetry.addData("Edited: ", "%s", s);
                    telemetry.addData("Selected OpMode", "%s", ompf.omp.get(s).selected);
                    telemetry.addLine("____________________________");
                    telemetry.update();
                }
                c = 'X';

            }
        } catch(Exception e) {
            telemetry.addData("Exception","%s", e.toString());
            telemetry.update();
        } finally {
            if (cin != null) {
                cin.close();
            }
        }
    }
    public static void main(String[] args) throws IOException {
// Code to setup the main program that runs offline, none of this is robot code
        OpModeParamWrite OMPW = new OpModeParamWrite();
        OMPW.testModeActive = true;//Declare Test Mode
        OMPW.checkoutFiles();

//        OMPW.telemetry.addLine(String.format("Changing the Selected OpMode"));
//        OMPW.editHashMap();
//        OMPW.fileNameEdited ="OpModeHashMapFile2.txt";
//
//        OMPW.telemetry.addLine(String.format("Writing Hash Map to %s", OMPW.fileNameEdited));
//        OMPW.ompf.saveToFile(OMPW.fileNameEdited, OMPW);

        OMPW.telemetry.addLine("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        OMPW.telemetry.addLine(" ");
        OMPW.telemetry.addLine(String.format("Loading From CSV File"));
        OMPW.telemetry.addLine(" ");
        OMPW.telemetry.addLine("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        OMPW.telemetry.update();

        OMPW.ompf.loadFromCSV("OpModeCSV.csv", OMPW);

        OMPW.fileNameEdited ="OpModeHashMapFile2.txt";
        OMPW.telemetry.addLine(String.format("Writing Hash Map to %s", OMPW.fileNameEdited));
        OMPW.telemetry.update();
        OMPW.ompf.saveToFile(OMPW.fileNameEdited, OMPW);

        OMPW.telemetry.addLine("Program Complete");
        OMPW.telemetry.update();
    }
}