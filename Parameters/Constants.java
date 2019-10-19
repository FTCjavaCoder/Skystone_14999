package Skystone_14999.Parameters;

import android.content.Context;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import Skystone_14999.OpModes.BasicOpMode;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class Constants {

    public HashMap<String, ParameterHM> pHM = new HashMap();

    // Define the static power levels, power/speed limits, and initial positions for motors and servos
    //public final double DRIVE_POWER_LIMIT = 0.75;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit
    //public final double ROTATE_POWER_LIMIT = 0.25;//clockwise rotation power/speed to be converted to individual motor powers/speeds

    public final double SLIDE_POWER_LIMIT = 0.6;// was 0.6
    public final double TURN_POWER =  0.40;
    //public final double TELEOP_DRIVE_POWER_LIMIT =  0.65;

    //    final double OMNI_EFFICIENCY = 1/1.35;// unitless - factor that's needed to reduce total wheel travel to match forward/back and right/left motion, doesn't apply for rotate
    public final double ROBOT_INCH_TO_MOTOR_DEG = 360 / (3.875 * 3.14159); // units deg/inch - 360 deg. / wheel circumference (Wheel diameter x pi)
    public final int NUMBER_OF_JACK_STAGES = 2 * 3;// ASSUMING 3 PAIRS OF PIECES PER SIDE
    public final double L = 5.04;//
    public final double W0 = 999;//
    public final double H0 = 999;//
    public final double MOTOR_DEG_TO_LEAD = 0/360; //Replace 0 with the distance between the ridges of the lead screw
    public final double SLIDE_INCH_TO_MOTOR_DEG = (3 / 3) * 360; // (3 screw rev / inch) * ( 1 motor rev / 3 screw rev) * 360 degrees / motor rev
    // 1/3 gear ratio 16 turns per inch screw
    // DERIVATION alpha = 2*AL/D; AL = arc length = wheel travel in inches, D = wheel diameter, alpha = wheel angle in radians
    // AL is input so conversion = 2/D * 180/pi (convert to degrees
    // alpha = AL * (360 / (D*pi))
    public final int DEGREES_TO_COUNTS = 1440 / 360; // units counts/degree - based on 1440 per 1 revolution
    public final double ROBOT_DEG_TO_WHEEL_INCH = 18.270918668 * 3.14159 / 360;// units of inch/degree - Robot rotation circumference [(wheel base (diagonal)] * pi/360 deg
    // DERIVATION AL = theta * RTD/2; AL = arc length = wheel travel in inches, RTD = robot turning diameter, theta = robot angle in radians
    // theta is input so conversion = RTD/2 * pi/180 (convert input in degrees to radians)
    // AL = theta * (RTD * pi/360)
    //public final int MOVE_TOL = 8;// tolerance for motor reaching final positions in drive methods
    public final double GAIN = 0.5;
    public final double IMU_TOL = 320;
    public final double IMU_ROTATE_TOL = 2;
    public final int SLIDE_TOL = 20;
    public final double ARM_POSITION_THRESHOLD =  0.1; // Threshold in gamepad stick coordinates 0 - 1
    public final int ARM_POSITION_INCREMENT =  3; // Increment in degrees

    public final double stowServoMarker = 0.15; //0.2
    public final double deployServoMarker = 0.7; //0.75
    public final double markerTimeDelay = 0.5;

    public final double SLIDE_ROTATE_POWER_LIMIT = 0.75;
    public final double SLIDE_EXTEND_POWER_LIMIT = 0.75;
    public final double SLIDE_ROTATE_DEG_TO_MOTOR_DEG = 4;
    public final double SLIDE_EXTEND_INCH_TO_MOTOR_DEG = 2 * (1/0.011); //gear ratio x degrees per inch
    public final double LINEAR_SLIDE_POWER_LIMIT = 0.5;
    public final double LINEAR_SLIDE_EXTEND_INCREMENT = 0.05; //previously 0.25
    public final int LINEAR_SLIDE_EXTEND_MIN = -16;
    public final int LINEAR_SLIDE_EXTEND_MAX = 0;
    public final double LINEAR_SLIDE_ROTATE_INCREMENT = 0.5; //previously 0.1
    public final int LINEAR_SLIDE_ROTATE_MIN = -150;// WAS -110
    public final int LINEAR_SLIDE_ROTATE_MAX = 45; // IS GOOD
    public final double BOX_SERVO_INCREMENT = 0.005;

    public final double STICK_DEADZONE = 0.1;
    public final double SET_EXTEND_1 = -16; //-16/-16
    public final double SET_ROTATE_1 = -23; //-93/-23
    public final double MINBOX_SETPOS_1 = 0.75; //1/0.75
    public final double SET_EXTEND_2 = -14.2;//-16/-15.3/-14.2
    public final double SET_ROTATE_2 = -130; //-70/-114.5/-125/-130
    public final double MINBOX_SETPOS_2 = 1; //0.75/1
    public final double SET_ROTATE_3 = -35;
    public final double MINBOX_SETPOS_3 = 1;
    public final double SET_EXTEND_4 = -6.625;
    public final double SET_ROTATE_4 = -100;


    /*
     * Gripper and wrist disabled, by not calling the the properties
     */

    //double adjustedFwd = 1.01587302;
    public final double adjustedRotate = 1.36;
    public final double adjustedRight = 1.22163309;
    public final double targetColorRange[] = {15, 30};
    public final double SCALE_FACTOR = 255;
    public final double saturationTolerance = 0.55;
    public final double cubeKnockOffDistance = 12;


    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    public static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;
    public static final String VUFORIA_KEY = " AUtTfjH/////AAAAGalBbST5Vkv8kqIoszZrsYOCBYcUVlFwYJ2rvrvVgm4ie/idQYx2x++SWi3UMEJnuP7Zww+cqOgyLepotRs9ppGxpCDcintIz0pIixMr+bievjJUDzdn0PyAJ6QUZK3xzoqDkTY1R99qvRORmTTqCx2/rGfYPlcOpdL5oWdhQsUatHyneF/eiWGBincPqBx3JfVwZnscF/B7J+u76YA4VTWLl8bl3iu26IYXMZE0zi7Pk+s9/pRQCCrLcxsvns4ouiSg3n61Z+jv8aS6y9ncwDdg0nm/XwDeiIrworkIPcPTW73LKpzX/63C1HprikXUJ+fm1eAkCfNy06n9SNTq20jxc7NXtGVUoE+WbNGmE4yb ";


    public Constants() {
        //Empty Constructor
    }

    public void defineParameters() {

        pHM.put("drivePowerLimit", new ParameterHM(0.75, ParameterHM.instanceType.powerLimit));

        pHM.put("rotatePowerLimit", new ParameterHM(0.25, ParameterHM.instanceType.powerLimit));

        pHM.put("teleOpDrivePowerLimit", new ParameterHM(0.65, ParameterHM.instanceType.powerLimit));

        pHM.put("jackPowerLimit", new ParameterHM(0.25, ParameterHM.instanceType.powerLimit));

        pHM.put("moveTol", new ParameterHM(8, ParameterHM.instanceType.toleranceCounts));

    }// Define initial values for HashMap parameters

    public void writeToPhone(String fileName, BasicOpMode om) {
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

    public void readFromPhone(String fileName, BasicOpMode om) {
        Context c = om.hardwareMap.appContext;

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

                om.fileWasRead = true;

                om.telemetry.addData("Parameter Name", "%s", s);
                om.telemetry.addData("Value", "%.2f", v);
                om.telemetry.addData("Type", "%s", t);
                om.telemetry.addData("Range?", "%s", hr);
                om.telemetry.addData("Min", "%.2f", min);
                om.telemetry.addData("Max", "%.2f", max);
                om.telemetry.addData("Increment", "%.2f", inc);
                om.telemetry.addLine("/////////////////////////////");

                om.idle();
            }

            is.close();
        }
        catch(Exception e) {

            Log.e("Exception", e.toString());

            om.fileWasRead = false;

            om.telemetry.addData("Exception","%s", e.toString());
            om.telemetry.update();
        }
    }

    public void editHashMap(BasicOpMode om) {

        for(String s : pHM.keySet()) {

            while(!(om.gamepad1.x || om.gamepad1.b) && om.opModeIsActive()) {
                // X to EDIT || B to SKIP
                om.telemetry.addData("Parameter Name", "%s", s);
                om.telemetry.addData("Value", "%.2f", pHM.get(s).value);
                om.telemetry.addData("Type", "%s", pHM.get(s).paramType);
                om.telemetry.addData("Range?", "%s", pHM.get(s).hasRange);
                om.telemetry.addData("Min", "%.2f", pHM.get(s).min);
                om.telemetry.addData("Max", "%.2f", pHM.get(s).max);
                om.telemetry.addData("Increment", "%.2f", pHM.get(s).increment);
                om.telemetry.addLine("X to EDIT || B to SKIP");
                om.telemetry.update();
            }
            if(om.gamepad1.x) {

                while(!om.gamepad1.y && om.opModeIsActive()) {

                    om.telemetry.addData("Parameter Name", "%s", s);
                    om.telemetry.addData("Value", "%.2f", pHM.get(s).value);
                    om.telemetry.addData("Increment", "%.2f", pHM.get(s).increment);
                    om.telemetry.addLine("Right Bumper to increase, Left Bumper to decrease");
                    om.telemetry.addLine("Press Y to accept value");
                    om.telemetry.update();

                    if(om.gamepad1.right_bumper) {

                        pHM.get(s).increaseParameter();
                        om.sleep(300);
                    }
                    if(om.gamepad1.left_bumper) {

                        pHM.get(s).decreaseParameter();
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