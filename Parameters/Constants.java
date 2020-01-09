package Skystone_14999.Parameters;

import android.content.Context;
import android.util.Log;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import Skystone_14999.OpModes.BasicOpMode;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class Constants {

    public HashMap<String, ParameterHM> pHM = new HashMap();
//    public HashMap<String, OptionAutonomous> aOHM = new HashMap();

    // Define the static power levels, power/speed limits, and initial positions for motors and servos
    public double DRIVE_POWER_LIMIT = 1.0;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit
    public double ROTATE_POWER_LIMIT = 1.0;//clockwise rotation power/speed to be converted to individual motor powers/speeds
    public double DRIVE_POWER_MINIMUM = 0.1;
    public double STEERING_POWER_LIMIT = 0.5;
    public double STEERING_POWER_GAIN = 0.1;
    public double POWER_GAIN = 0.2;
    public double ROTATE_POWER_GAIN = 0.02;
    public double IMU_ROTATE_TOL = 1.0;
    public double IMU_DISTANCE_TOL = 1.0;

    public int MOVE_TOL = 30;// tolerance for motor reaching final positions in drive methods

    public double TELEOP_DRIVE_POWER_LIMIT = 1.0;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
    public double TELEOP_ROTATE_POWER_LIMIT = 1.0;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop

    public double JACK_POWER_LIMIT = 0.75;
    public double SLIDE_POWER_LIMIT = 0.6;// was 0.6

//    public final double TURN_POWER =  0.40;

    public double forwardFirstMove = 14;
    public double skystoneExtraBack = 8;

    public double doRotateMethod = 0;

    public double skystoneExtraSideways = 0;
    public double skystoneExtraStoneGrab = 0;

    public double adjustVuforiaPhone = 0;

    public final double ROBOT_INCH_TO_MOTOR_DEG = 360 / (3.875 * 3.14159); // units deg/inch - 360 deg. / wheel circumference (Wheel diameter x pi)
    public final int NUMBER_OF_JACK_STAGES = 3;// ASSUMING 3 PAIRS OF PIECES PER SIDE
    public final double MOTOR_DEG_TO_LEAD = 0.315/360; //Replace 0 with the distance between the ridges of the lead screw
    public final double SLIDE_INCH_TO_MOTOR_DEG = (3 / 3) * 360; // (3 screw rev / inch) * ( 1 motor rev / 3 screw rev) * 360 degrees / motor rev
    // 1/3 gear ratio 16 turns per inch screw
    // DERIVATION alpha = 2*AL/D; AL = arc length = wheel travel in inches, D = wheel diameter, alpha = wheel angle in radians
    // AL is input so conversion = 2/D * 180/pi (convert to degrees
    // alpha = AL * (360 / (D*pi))
    public final int DEGREES_TO_COUNTS = 1440 / 360; // units counts/degree - based on 1440 per 1 revolution
    public final double ROBOT_DEG_TO_WHEEL_INCH = 16.904807 * 3.14159 / 360;//NR wheel center to center 16.904807// units of inch/degree -- Robot rotation circumference [(wheel base (diagonal)] * pi/360 deg
    // DERIVATION AL = theta * RTD/2; AL = arc length = wheel travel in inches, RTD = robot turning diameter, theta = robot angle in radians
    // theta is input so conversion = RTD/2 * pi/180 (convert input in degrees to radians)
    // AL = theta * (RTD * pi/360)
    public double GAIN = 0.5;

    /*
     * Gripper and wrist disabled, by not calling the the properties
     */

    public final double adjForward = 0.964;// may simply be the difference between the two robots wheel diameters
    public final double adjRotate = 1.236;//NOT used because of using IMU to rotate to angle
    public final double adjRight = 1.079;//

    public static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = FRONT;// was BACK
    public static final String VUFORIA_KEY = " AUtTfjH/////AAAAGalBbST5Vkv8kqIoszZrsYOCBYcUVlFwYJ2rvrvVgm4ie/idQYx2x++SWi3UMEJnuP7Zww+cqOgyLepotRs9ppGxpCDcintIz0pIixMr+bievjJUDzdn0PyAJ6QUZK3xzoqDkTY1R99qvRORmTTqCx2/rGfYPlcOpdL5oWdhQsUatHyneF/eiWGBincPqBx3JfVwZnscF/B7J+u76YA4VTWLl8bl3iu26IYXMZE0zi7Pk+s9/pRQCCrLcxsvns4ouiSg3n61Z+jv8aS6y9ncwDdg0nm/XwDeiIrworkIPcPTW73LKpzX/63C1HprikXUJ+fm1eAkCfNy06n9SNTq20jxc7NXtGVUoE+WbNGmE4yb ";


    public Constants() {
        //Empty Constructor
    }

    public void defineParameters() {

        pHM.put("drivePowerLimit", new ParameterHM( 1.0, ParameterHM.instanceType.powerLimit));// was 0.75

        pHM.put("drivePowerMinimum", new ParameterHM( 0.1, ParameterHM.instanceType.powerLimit));//was 0.2

        pHM.put("rotatePowerLimit", new ParameterHM(1.0, ParameterHM.instanceType.powerLimit));// was 0.75

        pHM.put("powerGain", new ParameterHM(0.2, ParameterHM.instanceType.powerLimit));// new

        pHM.put("rotatePowerGain", new ParameterHM(0.02, ParameterHM.instanceType.powerLimit));// was 0.05

        pHM.put("IMURotateTol", new ParameterHM(1.0, ParameterHM.instanceType.rotationDegrees));// was 2.0

        pHM.put("IMUDistanceTol", new ParameterHM(1.0, ParameterHM.instanceType.distanceInches));// new

        pHM.put("steeringPowerGain", new ParameterHM(0.1, ParameterHM.instanceType.powerLimit));// new

        pHM.put("steeringPowerLimit", new ParameterHM(0.5, ParameterHM.instanceType.powerLimit));// new

        pHM.put("teleOpDrivePowerLimit", new ParameterHM(1.0, ParameterHM.instanceType.powerLimit));// was 0.55

        pHM.put("teleOpRotatePowerLimit", new ParameterHM(1.0, ParameterHM.instanceType.powerLimit));// was 0.40

        pHM.put("jackPowerLimit", new ParameterHM(1.0, ParameterHM.instanceType.powerLimit));// was 0.75

        pHM.put("slidePowerLimit", new ParameterHM(0.40, ParameterHM.instanceType.powerLimit));// was 0.40

        pHM.put("moveTol", new ParameterHM(30, ParameterHM.instanceType.toleranceCounts));// was !! 8 !!

        pHM.put("forwardFirstMove", new ParameterHM(14, ParameterHM.instanceType.distanceInches));// For forward before Vuforia in 2 stone (was forwardFirstMove)

        pHM.put("skystoneExtraBack", new ParameterHM(0, ParameterHM.instanceType.distanceInches));// For different backup distance to get to second Skystone

        pHM.put("doRotateMethod", new ParameterHM(0, ParameterHM.instanceType.toleranceCounts));// set to 1 to use IMURotate at the end of each IMUFwdRight move

        pHM.put("skystoneExtraSideways", new ParameterHM(0, ParameterHM.instanceType.distanceInches));//

        pHM.put("skystoneExtraStoneGrab", new ParameterHM(0, ParameterHM.instanceType.distanceInches));//

        pHM.put("adjustVuforiaPhone", new ParameterHM(0, ParameterHM.instanceType.distanceInches));// For different positions of phone to adjust values Vuforia uses to determine Left, Center, or Right

    }// Define initial values for HashMap parameters

    public void initParameters() {

        for(String s:pHM.keySet()) {

            if(s.equals("drivePowerLimit")) {
                DRIVE_POWER_LIMIT = pHM.get(s).value;
            }
            if(s.equals("drivePowerMinimum")) {
                DRIVE_POWER_MINIMUM = pHM.get(s).value;
            }
            if(s.equals("rotatePowerLimit")) {
                ROTATE_POWER_LIMIT = pHM.get(s).value;
            }
            if(s.equals("powerGain")) {
                POWER_GAIN = pHM.get(s).value;
            }
            if(s.equals("rotatePowerGain")) {
                ROTATE_POWER_GAIN = pHM.get(s).value;
            }
            if(s.equals("IMURotateTol")) {
                IMU_ROTATE_TOL = pHM.get(s).value;
            }
            if(s.equals("IMUDistanceTol")) {
                IMU_DISTANCE_TOL = pHM.get(s).value;
            }
            if(s.equals("steeringPowerLimit")) {
                STEERING_POWER_LIMIT = pHM.get(s).value;
            }
            if(s.equals("steeringPowerGain")) {
                STEERING_POWER_GAIN = pHM.get(s).value;
            }
            if(s.equals("teleOpDrivePowerLimit")) {
                TELEOP_DRIVE_POWER_LIMIT = pHM.get(s).value;
            }
            if(s.equals("teleOpRotatePowerLimit")) {
                TELEOP_ROTATE_POWER_LIMIT = pHM.get(s).value;
            }
            if(s.equals("jackPowerLimit")) {
                JACK_POWER_LIMIT = pHM.get(s).value;
            }
            if(s.equals("slidePowerLimit")) {
                SLIDE_POWER_LIMIT = pHM.get(s).value;
            }
            if(s.equals("moveTol")) {
                MOVE_TOL = pHM.get(s).integerParameter();
            }
            if(s.equals("forwardFirstMove")) {
                forwardFirstMove = pHM.get(s).value;
            }
            if(s.equals("skystoneExtraBack")) {
                skystoneExtraBack = pHM.get(s).value;
            }
            if(s.equals("doRotateMethod")) {
                doRotateMethod = pHM.get(s).value;
            }
            if(s.equals("skystoneExtraSideways")) {
                skystoneExtraSideways = pHM.get(s).value;
            }
            if(s.equals("skystoneExtraStoneGrab")) {
                skystoneExtraStoneGrab = pHM.get(s).value;
            }
            if(s.equals("adjustVuforiaPhone")) {
                adjustVuforiaPhone = pHM.get(s).value;
            }

        }
    }

    public void writeToPhone(String fileName, BasicOpMode om) {
        Context c = om.hardwareMap.appContext;

        try {

            OutputStreamWriter osw = new OutputStreamWriter(c.openFileOutput(fileName, c.MODE_PRIVATE));

            for(String s : pHM.keySet()) {

                osw.write(s + "\n");
//                om.telemetry.addData("Parameter Name", "%s", s);
                osw.write(pHM.get(s).value + "\n");
//                om.telemetry.addData("Value", "%.2f", pHM.get(s).value);
                osw.write(pHM.get(s).paramType + "\n");
//                om.telemetry.addData("Type", "%s", pHM.get(s).paramType);
                osw.write(pHM.get(s).hasRange + "\n");
//                om.telemetry.addData("Range?", "%s", pHM.get(s).hasRange);
                osw.write(pHM.get(s).min + "\n");
//                om.telemetry.addData("Min", "%.2f", pHM.get(s).min);
                osw.write(pHM.get(s).max + "\n");
//                om.telemetry.addData("Max", "%.2f", pHM.get(s).max);
                osw.write(pHM.get(s).increment + "\n");
//                om.telemetry.addData("Increment", "%.2f", pHM.get(s).increment);
//                om.telemetry.update();
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

                om.telemetry.addData("Parameter", "%s = %.2f", s, v);
//                om.telemetry.addData("Value", "%.2f", v);
//                om.telemetry.addData("Type", "%s", t);
//                om.telemetry.addData("Range?", "%s", hr);
//                om.telemetry.addData("Min", "%.2f", min);
//                om.telemetry.addData("Max", "%.2f", max);
//                om.telemetry.addData("Increment", "%.2f", inc);
                om.telemetry.addLine("---------------------------------");

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

    public void writeToFile(String fileName, BasicOpMode om) {

        try {

            FileOutputStream fos = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

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

//            Log.e("Exception", e.toString());

            om.telemetry.addData("Exception","%s", e.toString());
            om.telemetry.update();
        }
    }

    public void readFromFile(String fileName, BasicOpMode om) {

        try {


            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

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

            fr.close();
        }
        catch(Exception e) {

//            Log.e("Exception", e.toString());

            om.fileWasRead = false;

            om.telemetry.addData("Exception","%s", e.toString());
            om.telemetry.update();
        }
    }


    // AUTONOMOUS OPTIONS
//    public void defineAutoOptions() {
//
//        aOHM.put("skyStoneInside", new OptionAutonomous(OptionAutonomous.name.skyStoneInside));
//
//        aOHM.put("skyStoneInsideUnmoved", new OptionAutonomous(OptionAutonomous.name.skyStoneInsideUnmoved));
//
//        aOHM.put("skyStoneOutside", new OptionAutonomous(OptionAutonomous.name.skyStoneOutside));
//
//        aOHM.put("skyStoneOutsideUnmoved", new OptionAutonomous(OptionAutonomous.name.skyStoneOutsideUnmoved));
//
//        aOHM.put("foundationInside", new OptionAutonomous(OptionAutonomous.name.foundationInside));
//
//        aOHM.put("foundationOutside", new OptionAutonomous(OptionAutonomous.name.foundationOutside));
//
//    }// Define Autonomous options
//
//    public void writeToPhoneAO(String fileName, BasicOpMode om) {
//        Context c = om.hardwareMap.appContext;
//
//        try {
//
//            OutputStreamWriter osw = new OutputStreamWriter(c.openFileOutput(fileName, c.MODE_PRIVATE));
//
//            for (String s : aOHM.keySet()) {
//
//                osw.write(s + "\n");
//                om.telemetry.addData("Option Name", "%s", s);
//                osw.write(aOHM.get(s).optionNumber + "\n");
//                om.telemetry.addData("Number", "%.2f", aOHM.get(s).optionNumber);
//                osw.write(aOHM.get(s).description + "\n");
//                om.telemetry.addData("Description", "%s", aOHM.get(s).description);
//
//                osw.close();
//            }
//        }
//        catch(Exception e){
//
//                Log.e("Exception", e.toString());
//
//                om.telemetry.addData("Exception", "%s", e.toString());
//                om.telemetry.update();
//            }
//
//    }
//
//    public void readFromPhoneAO(String fileName, BasicOpMode om) {
//        Context c = om.hardwareMap.appContext;
//
//        try {
//            InputStream is = c.openFileInput(fileName);
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//
//            String s;
//            while((s = br.readLine())!= null) {
//
//                double n = Double.parseDouble(br.readLine());
//                String d = br.readLine();
//
//                switch (s) {
//
//                    case ("skyStoneOutside") :
//                        aOHM.put(s, new OptionAutonomous(OptionAutonomous.name.skyStoneOutside));
//                        break;
//                    case ("skyStoneInside") :
//                        aOHM.put(s, new OptionAutonomous(OptionAutonomous.name.skyStoneInside));
//                        break;
//
//                    case ("skyStoneOutsideUnmoved") :
//                        aOHM.put(s, new OptionAutonomous(OptionAutonomous.name.skyStoneOutsideUnmoved));
//                        break;
//
//                    case ("skyStoneInsideUnmoved") :
//                        aOHM.put(s, new OptionAutonomous(OptionAutonomous.name.skyStoneInsideUnmoved));
//                        break;
//
//                    case ("foundationOutside") :
//                        aOHM.put(s, new OptionAutonomous(OptionAutonomous.name.foundationOutside));
//                        break;
//
//                    case ("foundationInside") :
//                        aOHM.put(s, new OptionAutonomous(OptionAutonomous.name.foundationInside));
//                        break;
//                }
//
//                om.fileWasRead = true;
//
//                om.telemetry.addData("Option Name", "%s", s);
//                om.telemetry.addData("Number", "%.2f", n);
//                om.telemetry.addData("Description", "%s", d);
//
//                om.idle();
//            }
//
//            is.close();
//        }
//        catch(Exception e) {
//
//            Log.e("Exception", e.toString());
//
//            om.fileWasRead = false;
//
//            om.telemetry.addData("Exception","%s", e.toString());
//            om.telemetry.update();
//        }
//    }
//
//    public void editHashMapAO(BasicOpMode om) {
//
//        for(String s : aOHM.keySet()) {
//
//            while(!(om.gamepad1.x || om.gamepad1.b) && om.opModeIsActive()) {
//                // X to EDIT || B to SKIP
//                om.telemetry.addData("Option Name", "%s", s);
//                om.telemetry.addData("Number", "%.2f", aOHM.get(s).optionNumber);
//                om.telemetry.addData("Description", "%s", aOHM.get(s).description);
//
//                om.telemetry.addLine("X to Select || B to SKIP");
//                om.telemetry.update();
//            }
//            if(om.gamepad1.x) {
//
//                while(!om.gamepad1.y && !om.gamepad1.b && om.opModeIsActive()) {
//
//                    om.telemetry.addData("Option Name", "%s", s);
//                    om.telemetry.addData("Number", "%.2f", aOHM.get(s).optionNumber);
//                    om.telemetry.addData("Description", "%s", aOHM.get(s).description);
//                    om.telemetry.addLine("Press Y to confirm or B to skip");
//                    om.telemetry.update();
//
//                    if(om.gamepad1.y) {
//// {skyStoneOutside, skyStoneInside, skyStoneOutsideUnmoved, skyStoneInsideUnmoved, foundationOutside, foundationInside}
//                        if(aOHM.get(s).optionNumber == 1) {
//
//                            om.selected = "skyStoneOutside";
//                        }
//                        if(aOHM.get(s).optionNumber == 2) {
//
//                            om.selected = "skyStoneInside";
//                        }
//                        if(aOHM.get(s).optionNumber == 3) {
//
//                            om.selected = "skyStoneOutsideUnmoved";
//                        }if(aOHM.get(s).optionNumber == 4) {
//
//                            om.selected = "skyStoneInsideUnmoved";
//                        }
//                        if(aOHM.get(s).optionNumber == 5) {
//
//                            om.selected = "foundationOutside";
//                        }
//                        if(aOHM.get(s).optionNumber == 6) {
//
//                            om.selected = "foundationInside";
//                        }
//
//                        om.sleep(300);
//                    }
//                }
//            }
//            if(om.gamepad1.b) {
//
//                om.telemetry.addData("Skipped","%s", s);
//                om.telemetry.update();
//                om.sleep(500);
//            }
//
//        }
//    }

}