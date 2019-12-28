package Skystone_14999.Parameters;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import Skystone_14999.OpModes.BasicOpMode;

public class OpModeParamFunctions {

    public HashMap<String, OpModeParam> omp = new HashMap();
    public OpModeParamFunctions(){
        //Empty constructor

    }
    public void defineParameters() {
        //Create the OpMpdes and place in the hashMap

        omp.put("RedStoneIn", new OpModeParam("Red", "Front", "In", "Moved", -180, 0,"N"));
        omp.put("RedStoneOut", new OpModeParam("Red", "Front", "Out", "Moved", -180, 0,"N"));
        omp.put("RedFoundIn", new OpModeParam("Red", "Back", "In", "None", -180, 1,"N"));
        omp.put("RedFoundOut", new OpModeParam("Red", "Back", "Out", "None", -180, 1,"N"));

        omp.put("BlueStoneIn", new OpModeParam("Blue", "Front", "In", "Moved", 0, 0,"N"));
        omp.put("BlueStoneOut", new OpModeParam("Blue", "Front", "Out", "Moved", 0, 0,"N"));
        omp.put("BlueFoundIn", new OpModeParam("Blue", "Back", "In", "None", 0, 1,"N"));
        omp.put("BlueFoundOut", new OpModeParam("Blue", "Back", "Out", "None", 0, 1,"N"));

    }// Define initial values for HashMap parameters

    public void loadFromCSV(String fileName, BasicOpMode om) {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
            String s = null;
            boolean readHeader = false; //skip initial row as the header
            while ((s = csvReader.readLine()) != null) {
                String[] data = s.split("\t");

                if(readHeader) {
                    omp.put(data[0], new OpModeParam(data[1], data[2], data[3], data[4], Double.parseDouble(data[5]), Integer.parseInt(data[6]), data[7]));
                    om.telemetry.addData("OpModeName", "%s", data[0]);
                    om.telemetry.addData("Team Color", "%s", omp.get(data[0]).teamColor);
                    om.telemetry.addData("Start Side", "%s", omp.get(data[0]).startSide);
                    om.telemetry.addData("Bridge Side", "%s", omp.get(data[0]).bridgeSide);
                    om.telemetry.addData("SkyStone Drop", "%s", omp.get(data[0]).SkyStoneDrop);
                    om.telemetry.addData("Start Direction", "%.1f", omp.get(data[0]).startDirection);
                    om.telemetry.addData("Case Select", "%d", omp.get(data[0]).caseSelect);
                    om.telemetry.addData("Selected OpMode", "%s", omp.get(data[0]).selected);
                    om.telemetry.addLine("==================================");
                    om.telemetry.update();
                }
                readHeader = true; //skip initial row because its teh header
            }

            csvReader.close();
        }catch(Exception e) {

            om.telemetry.addData("Exception","%s", e.toString());
            om.telemetry.update();
        }
    }

    public void writeToPhone(String fileName, BasicOpMode om) {
        Context c = om.hardwareMap.appContext;

        try {

            OutputStreamWriter osw = new OutputStreamWriter(c.openFileOutput(fileName, c.MODE_PRIVATE));

            for(String s : omp.keySet()) {

                osw.write(s + "\n");
                om.telemetry.addData("OpMode Name", "%s", s);
                osw.write(omp.get(s).teamColor + "\n");
                om.telemetry.addData("Team Color", "%s", omp.get(s).teamColor);
                osw.write(omp.get(s).startSide + "\n");
                om.telemetry.addData("Start Side", "%s", omp.get(s).startSide);
                osw.write(omp.get(s).bridgeSide + "\n");
                om.telemetry.addData("Bridge Side", "%s", omp.get(s).bridgeSide);
                osw.write(omp.get(s).SkyStoneDrop + "\n");
                om.telemetry.addData("SkyStone Drop", "%s", omp.get(s).SkyStoneDrop);
                osw.write(omp.get(s).startDirection + "\n");
                om.telemetry.addData("Start Direction", "%.1f", omp.get(s).startDirection);
                osw.write(omp.get(s).caseSelect + "\n");
                om.telemetry.addData("Case Select", "%d", omp.get(s).caseSelect);
                osw.write(omp.get(s).selected + "\n");
                om.telemetry.addData("Selected OpMode", "%s", omp.get(s).selected );
                om.telemetry.update();

            }

            osw.close();
        }
        catch(Exception e) {

            Log.e("Exception", e.toString());

            om.telemetry.addData("Exception","%s", e.toString());
            om.telemetry.update();
        }
    }
    public void readFromPhone(String fileName, BasicOpMode om) {
        Context c = om.hardwareMap.appContext;

        try {
            InputStream is = c.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String s;
            while((s = br.readLine())!= null) {
            //s reads the HashMap key which is the OpMode name or short description

//        public String teamColor =  "Red";       // options Red/Blue
//        public String startSide = "Front";      // options Front/Back
//        public String bridgeSide = "In";        // options In/Out
//        public String SkyStoneDrop = "NotMoved";   // options NotMoved/Moved
//        public double startDirection = 0;       // options 0/180 degrees
//        public int caseSelect = 0;              // options 0,1,2,3,4... max number of cases
                String tc = br.readLine();
                String ss = br.readLine();
                String bs = br.readLine();
                String ssd = br.readLine();
                double sd = Double.parseDouble(br.readLine());
                int cs = Integer.parseInt(br.readLine());
                String sel = br.readLine();
                omp.put(s, new OpModeParam(tc, ss, bs, ssd, sd, cs, sel));

                om.loadFile = true;

                om.telemetry.addData("OpMode Name", "%s", s);
                om.telemetry.addData("Team Color", "%s", tc);
                om.telemetry.addData("Start Side", "%s", ss);
                om.telemetry.addData("Bridge Side", "%s", bs);
                om.telemetry.addData("SkyStone Drop", "%s", ssd);
                om.telemetry.addData("Start Direction", "%.1f", sd);
                om.telemetry.addData("Case Select", "%d", cs);
                om.telemetry.addData("Selected OpMode", "%s", sel);
                om.telemetry.addLine("/////////////////////////////");

                om.idle();
            }

            is.close();
        }
        catch(Exception e) {

            Log.e("Exception", e.toString());

            om.loadFile = false;

            om.telemetry.addData("Exception","%s", e.toString());
            om.telemetry.update();
        }
    }
    public void loadFromPC(String filename, BasicOpMode om){
        try{
            FileReader in = new FileReader(filename);
            BufferedReader br = new BufferedReader(in);
            String s;
            om.telemetry.addData("Reading from"," %s", filename);

            while((s = br.readLine()) != null){
                om.loadFile = true;
                //s reads the HashMap key which is the OpMode name or short description

//        public String teamColor =  "Red";       // options Red/Blue
//        public String startSide = "Front";      // options Front/Back
//        public String bridgeSide = "In";        // options In/Out
//        public String SkyStoneDrop = "NotMoved";   // options NotMoved/Moved
//        public double startDirection = 0;       // options 0/180 degrees
//        public int caseSelect = 0;              // options 0,1,2,3,4... max number of cases
                String tc = br.readLine();
                String ss = br.readLine();
                String bs = br.readLine();
                String ssd = br.readLine();
                double sd = Double.parseDouble(br.readLine());
                int cs = Integer.parseInt(br.readLine());
                String sel = br.readLine();


                if(omp.get(s) != null){
                    omp.put(s, new OpModeParam(tc, ss, bs, ssd, sd, cs, sel));
                    om.telemetry.addData("Updating OpMode","Key= %s",s);

                }
                else if(omp.get(s) == null){
                    omp.put(s, new OpModeParam(tc, ss, bs, ssd, sd, cs, sel));
                    om.telemetry.addData("Adding OpMode","Key= %s",s);

                }

                om.telemetry.addData("Team Color", "%s", tc);
                om.telemetry.addData("Start Side", "%s", ss);
                om.telemetry.addData("Bridge Side", "%s", bs);
                om.telemetry.addData("SkyStone Drop", "%s", ssd);
                om.telemetry.addData("Start Direction", "%.1f", sd);
                om.telemetry.addData("Case Select", "%d", cs);
                om.telemetry.addData("Selected OpMode", "%s", sel);
                om.telemetry.addLine("______________________________________________");
//                om.pressXtoContinue();
            }
            om.telemetry.update();
            in.close();

        }catch(Exception e){
//            Log.e("Exception", e.toString());
            om.loadFile = false;
            om.telemetry.addData("File Does Not Exist","%s"," ");
            om.telemetry.addData("Setting Flag", "%s",om.loadFile);
            om.telemetry.update();
        }

    }

    public void saveToFile(String filename, BasicOpMode om){

        try{
            FileOutputStream fos = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            for(String s : omp.keySet()){
                om.telemetry.addLine(" ");
                osw.write(s + "\n");
                om.telemetry.addData("OpMode Name", "%s", s);
                osw.write(omp.get(s).teamColor + "\n");
                om.telemetry.addData("Team Color", "%s", omp.get(s).teamColor);
                osw.write(omp.get(s).startSide + "\n");
                om.telemetry.addData("Start Side", "%s", omp.get(s).startSide);
                osw.write(omp.get(s).bridgeSide + "\n");
                om.telemetry.addData("Bridge Side", "%s", omp.get(s).bridgeSide);
                osw.write(omp.get(s).SkyStoneDrop + "\n");
                om.telemetry.addData("SkyStone Drop", "%s", omp.get(s).SkyStoneDrop);
                osw.write(omp.get(s).startDirection + "\n");
                om.telemetry.addData("Start Direction", "%.1f", omp.get(s).startDirection);
                osw.write(omp.get(s).caseSelect + "\n");
                om.telemetry.addData("Case Select", "%d", omp.get(s).caseSelect);
                osw.write(omp.get(s).selected + "\n");
                om.telemetry.addData("Selected OpMode", "%s", omp.get(s).selected );

                om.telemetry.addLine("______________________________________________");
                om.telemetry.update();

//
            }
            osw.close();
        }catch(Exception e){
//            Log.e("Exception", e.toString());
            System.out.println("There was an Error Writing");
        }
    }
    public void editHashMap(BasicOpMode om) {

        for(String s : omp.keySet()) {

            while(!(om.gamepad1.x || om.gamepad1.b) && om.opModeIsActive()) {
                // X to EDIT || B to SKIP
                om.telemetry.addData("OpMode Name", "%s", s);
//                om.telemetry.addData("Team Color", "%s", omp.get(s).teamColor);
//                om.telemetry.addData("Start Side", "%s", omp.get(s).startSide);
//                om.telemetry.addData("Bridge Side", "%s", omp.get(s).bridgeSide);
//                om.telemetry.addData("SkyStone Drop", "%s", omp.get(s).SkyStoneDrop);
//                om.telemetry.addData("Start Direction", "%.1f", omp.get(s).startDirection);
//                om.telemetry.addData("Case Select", "%d", omp.get(s).caseSelect);
                om.telemetry.addData("Selected OpMode", "%s", omp.get(s).selected);
                om.telemetry.addLine("X to EDIT || B to SKIP");
                om.telemetry.update();
            }
            if(om.gamepad1.x) {

                while(!om.gamepad1.y && om.opModeIsActive()) {

                    om.telemetry.addData("OpMode Name", "%s", s);
                    om.telemetry.addData("Team Color", "%s", omp.get(s).teamColor);
                    om.telemetry.addData("Start Side", "%s", omp.get(s).startSide);
                    om.telemetry.addData("Bridge Side", "%s", omp.get(s).bridgeSide);
                    om.telemetry.addData("SkyStone Drop", "%s", omp.get(s).SkyStoneDrop);
                    om.telemetry.addData("Start Direction", "%.1f", omp.get(s).startDirection);
                    om.telemetry.addData("Case Select", "%d", omp.get(s).caseSelect);
                    om.telemetry.addData("Selected OpMode", "%s", omp.get(s).selected);
                    om.telemetry.addLine("X to EDIT || B to SKIP");
                    om.telemetry.update();
                    om.telemetry.addLine("Right Bumper to set SELECTED to 'Y', Left Bumper to set to 'N'");
                    om.telemetry.addLine("Press Y to accept value");
                    om.telemetry.update();

                    if(om.gamepad1.right_bumper) {

                        omp.get(s).setSelect("Y");
                        om.sleep(300);
                    }
                    if(om.gamepad1.left_bumper) {

                        omp.get(s).setSelect("N");
                        om.sleep(300);
                    }
                }
            }
            if(om.gamepad1.b) {

                om.telemetry.addData("Skipped","%s", s);
                om.telemetry.update();
                om.sleep(500);
            }

        }
    }

}
