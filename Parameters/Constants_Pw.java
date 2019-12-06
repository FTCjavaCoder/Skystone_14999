package Skystone_14999.Parameters;

import android.content.Context;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import Skystone_14999.Parameters.ParameterHM;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

public class Constants_Pw  {

    public String hashMapFile = "HashMapFile.txt";
    public HashMap<String, ParameterHM> pHM = new HashMap();

    public final double TURN_POWER =  0.40;

    public void defineParameters() {

        pHM.put("drivePowerLimit", new ParameterHM(0.75, ParameterHM.instanceType.powerLimit));

        pHM.put("rotatePowerLimit", new ParameterHM(0.5, ParameterHM.instanceType.powerLimit));// was 0.25

        pHM.put("teleOpDrivePowerLimit", new ParameterHM(0.55, ParameterHM.instanceType.powerLimit));

        pHM.put("jackPowerLimit", new ParameterHM(0.75, ParameterHM.instanceType.powerLimit));

        pHM.put("slidePowerLimit", new ParameterHM(0.40, ParameterHM.instanceType.powerLimit));

        pHM.put("moveTol", new ParameterHM(30, ParameterHM.instanceType.toleranceCounts));// was !! 8 !!

    }

    public boolean readFromPhone(String fileName, OpMode om) {
        Context c = om.hardwareMap.appContext;
        boolean fileWasRead = false;

        try {
            InputStream is = c.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String s;
            while((s = br.readLine())!= null) {

                double v = Double.parseDouble(br.readLine());
                String t = br.readLine();
                String hr = br.readLine();
                double min = Double.parseDouble(br.readLine());
                double max = Double.parseDouble(br.readLine());
                double inc = Double.parseDouble(br.readLine());

                switch (t) {
                    case ("powerLimit") :
                        pHM.put(s, new ParameterHM(v, ParameterHM.instanceType.powerLimit));
                        break;
                    case ("counts") :
                        pHM.put(s, new ParameterHM(v, ParameterHM.instanceType.counts));
                        break;

                    case ("toleranceCounts") :
                        pHM.put(s, new ParameterHM(v, ParameterHM.instanceType.toleranceCounts));
                        break;

                    case ("distanceInches") :
                        pHM.put(s, new ParameterHM(v, ParameterHM.instanceType.distanceInches));
                        break;

                    case ("rotationDegrees") :
                        pHM.put(s, new ParameterHM(v, ParameterHM.instanceType.rotationDegrees));
                        break;

                    case ("servoPosition") :
                        pHM.put(s, new ParameterHM(v, ParameterHM.instanceType.servoPosition));
                        break;
                }

                fileWasRead = true;

                om.telemetry.addData("Parameter Name", "%s", s);
                om.telemetry.addData("Value", "%.2f", v);
                om.telemetry.addData("Type", "%s", t);
                om.telemetry.addData("Range?", "%s", hr);
                om.telemetry.addData("Min", "%.2f", min);
                om.telemetry.addData("Max", "%.2f", max);
                om.telemetry.addData("Increment", "%.2f", inc);
                om.telemetry.addLine("/////////////////////////////");
            }

            is.close();
        }
        catch(Exception e) {

            Log.e("Exception", e.toString());

            fileWasRead = false;

            om.telemetry.addData("Exception","%s", e.toString());
            om.telemetry.update();
        }

        return fileWasRead;
    }

    public void writeToPhone(String fileName, OpMode om) {
        Context c = om.hardwareMap.appContext;

        try {

            OutputStreamWriter osw = new OutputStreamWriter(c.openFileOutput(fileName, c.MODE_PRIVATE));

            for(String s : pHM.keySet()) {

                osw.write(s + "\n");
                om.telemetry.addData("Parameter Name", "%s", s);
                osw.write(pHM.get(s).value + "\n");
                om.telemetry.addData("Value", "%.2f", pHM.get(s).value);
                osw.write(pHM.get(s).paramType + "\n");
                om.telemetry.addData("Type", "%s", pHM.get(s).paramType);
                osw.write(pHM.get(s).hasRange + "\n");
                om.telemetry.addData("Range?", "%s", pHM.get(s).hasRange);
                osw.write(pHM.get(s).min + "\n");
                om.telemetry.addData("Min", "%.2f", pHM.get(s).min);
                osw.write(pHM.get(s).max + "\n");
                om.telemetry.addData("Max", "%.2f", pHM.get(s).max);
                osw.write(pHM.get(s).increment + "\n");
                om.telemetry.addData("Increment", "%.2f", pHM.get(s).increment);
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

    public void readOrWriteHashMap(OpMode mcc) {

        if (!(readFromPhone(hashMapFile, mcc))) {

            defineParameters();
            writeToPhone(hashMapFile, mcc);

            readFromPhone(hashMapFile, mcc);
            // TO DO: AVOID HAVING TO READ IF FILE WAS CREATED
        }
    }
}