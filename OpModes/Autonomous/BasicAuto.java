package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import Skystone_14999.HarwareConfig.HardwareBilly;
import Skystone_14999.OpModes.BasicOpMode;
//import TestOpModesOffline.Telemetry;

@Autonomous(name="BasicAuto", group="Autonomous")
@Disabled
public class BasicAuto extends BasicOpMode {

    // motor position must be integer number of counts
    public int forwardPosition = 0;//Target position in forward direction for drive motors
    public int rightPosition = 0;//Target position in right direction for drive motors
    public int clockwisePosition = 0;//Target position in clockwise direction for drive motors
    public int slideDistance = 0;

    public double extraFwd = 0;
    public double stoneSideways = 0;
//    public double sideGrabStone = 11;// was 9 |||| was 18 {} decide how to make robo tmove forward and still grab stones
    public double foundationPosChange = 0;// 26 for unmoved Foundation, 0 for moved Foundation
    public double insideOutside = 0;// 0 for Inside, 24 for Outside
    public double foundationInOut = 0;// 0 for Inside, 26 for Outside
    public double foundationPush = 8;
    public double sideColor = 1;// + for Blue, - for Red, KEEP BLUE

    public double blockCamera = 0.525;
    public double extraFwdToBlock = 0;

    public boolean getSecondStone = true;

    public enum autoChoice {SkyStoneOutside, SkyStoneInside, SkyStoneOutsideUnmoved, SkyStoneInsideUnmoved}

    private static final float mmPerInch        = 25.4f;

    public double stoneYLocation;

    //Define all double variables
    public double start = 0;//timer variable to use for setting waits in the code
    public float hsvValues[] = {0F, 0F, 0F};
    public String stonePos = "Unknown";

    public boolean skystoneFound = false;

    public int stoneSelect = -1;// FOR BLUE: 0 nearest to bridge, 1 center, 2 farthest from bridge FOR RED: 0 farthest from bridge, 1 center, 2 nearest to bridge

    public double secondStoneBackup = 8;

    public double extraRedDistance = 0;// added because of relative difference of starting position of robot on Red

    public double stoneArmUnderBridgeBlue;// for blue oriented servo
    public double stoneArmDownBlue;// was 0.05 and 0
    public double rackOutBlue;//

    public double rackInitBlue;//
    public double rackInitRed;//

    public double stoneArmUnderBridgeRed;// for red oriented servo
    public double stoneArmDownRed;// was 0.7
    public double rackOutRed;// untested
//THIS IS JUST TO DECLARE THE VARIABLES YOU WILL ALSO NEED TO CHANGE THE VALUES IN THE INITIALIZE METHOD

    public double vuforiaWaitTime = 1.5;// was 1

    public double leftBound = 400;// in pixels defaults for TensorFlow
    public double rightBound = 900;// in pixels defaults for TensorFlow

    public boolean drivingMiniBot = false;

    public double detectionRotateSpeed = 0.1;

    public ElapsedTime runtime = new ElapsedTime(); //create a counter for elapsed time

    public boolean haveBlueFoundation = false;
    public boolean haveRedFoundation = false;
    public boolean haveBlueSkyStone1 = false;
    public boolean haveBlueSkyStone2 = false;
    public boolean haveRedSkyStone1 = false;
    public boolean haveRedSkyStone2 = false;
/** COMMENTED OUT FOR EXPERIMENTAL CODE

 //********** Added from OfflineOpModeLibs to BasicAuto forOfflineOpModeRunFile ******************
    public FieldConfiguration fc = new FieldConfiguration();
    boolean writeBF = false;
    boolean writeRF = false;
    boolean writeBS1 = false;
    boolean writeBS2 = false;
    boolean writeRS1 = false;
    boolean writeRS2 = false;

    boolean robotSeeStone = false;

    public int IMUCounter =0;
    public int size = 300;
 //********** Added from OfflineOpModeLibs to BasicAuto forOfflineOpModeRunFile ******************

 * END EXPERIMENTAL CODE
 * */

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    public VuforiaLocalizer vuforia;
    public boolean targetVisible = false;

    public VuforiaTrackables targetsSkyStone = null;

    public List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    public VuforiaTrackable stoneTarget = null;

    public static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    public static final String LABEL_FIRST_ELEMENT = "Stone";
    public static final String LABEL_SECOND_ELEMENT = "Skystone";

    public TFObjectDetector tfod;

    public double middleOfStone;

    public double angleWhenFound;

    public double zoneShift;

    @Override
    //    public void runOpMode() throws InterruptedException {
    public void runOpMode() {


    }
    public void runCode() {


    }

    public void initialize() {

        telemetry.addLine("NOT READY DON'T PRESS PLAY");
        telemetry.update();

        telemetry.addData(">", "Press Play to start");

        if (testModeActive) {

            readOrWriteHashMapOffline();
        }
        else {

            readOrWriteHashMap();
        }

        drivingMiniBot = false;
        //Values For Full Robot
        detectionRotateSpeed = 0.1 * (40.0/60.0);
        cons.DEGREES_TO_COUNTS = (1440.0/360.0) * (40.0/60.0);

        rackInitBlue = 0;//
        rackInitRed = 1;//
        stoneArmUnderBridgeBlue = 0.85;// for blue oriented servo
        stoneArmDownBlue = 0.20;// for blue oriented servo
        rackOutBlue = 0.75;//
        stoneArmUnderBridgeRed = 0.30;// for red oriented servo
        stoneArmDownRed = 0.80;// for red oriented servo untested
        rackOutRed = 0.25;// untested

        Billy.init(hardwareMap, testModeActive);

        //Motor configuration, recommend Not Changing - Set all motors to forward direction, positive = clockwise when viewed from shaft side
        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.jack.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.slide.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.jack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.jack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Billy.frontLeft.setTargetPosition(0);
        Billy.frontRight.setTargetPosition(0);
        Billy.backLeft.setTargetPosition(0);
        Billy.backRight.setTargetPosition(0);
        Billy.jack.setTargetPosition(0);

        //Set all motors to position mode (assumes that all motors have encoders on them)
        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.jack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);
        Billy.jack.setPower(0);
        Billy.slide.setPower(0);

        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);

        Billy.armServoBlue.setPosition(Billy.stoneArmInitBlue);
        Billy.rackServoBlue.setPosition(rackInitBlue);

        Billy.armServoRed.setPosition(Billy.stoneArmInitRed);
        Billy.rackServoRed.setPosition(rackInitRed);

        Billy.stoneServoLeft.setPosition(1);
        Billy.stoneServoRight.setPosition(1);

        if (testModeActive) {
            // DO NOTHING
        }
        else {

            targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

            stoneTarget = targetsSkyStone.get(0);
            stoneTarget.setName("Stone Target");
            allTrackables.add(targetsSkyStone.get(0));
        }

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)", forwardPosition, rightPosition, clockwisePosition);
        telemetry.addData(">", "Press Play to start");
        telemetry.update();//Update telemetry to update display

    }// sets: RUN_TO_POSITION, ZeroPowerBehaviour.BRAKE, and 0 power & targetPos

    public void initializeMiniBot() {

        telemetry.addLine("NOT READY DON'T PRESS PLAY");
        telemetry.update();

        telemetry.addData(">", "Press Play to start");

        if (testModeActive) {

            readOrWriteHashMapOffline();
        }
        else{

            readOrWriteHashMap();
        }

        drivingMiniBot = true;
        //Values For Mini Robot
        detectionRotateSpeed = 0.1;
        cons.DEGREES_TO_COUNTS = (1440.0/360.0);

        Billy.stoneArmInitBlue = 1;// for blue oriented servo was 0
        Billy.stoneArmInitRed = 1;// for red oriented servo
        stoneArmUnderBridgeBlue = 1;// for blue oriented servo was 0.25
        stoneArmDownBlue = 0.4;// for blue oriented servo
        stoneArmUnderBridgeRed = 0.75;// for red oriented servo
        stoneArmDownRed = 0.3;// for red oriented servo (was 0.5, could adjust servo horn)

        Billy.initMiniBot(hardwareMap, testModeActive);

        //Motor configuration, recommend Not Changing - Set all motors to forward direction, positive = clockwise when viewed from shaft side
        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Billy.frontLeft.setTargetPosition(0);
        Billy.frontRight.setTargetPosition(0);
        Billy.backLeft.setTargetPosition(0);
        Billy.backRight.setTargetPosition(0);

        //Set all motors to position mode (assumes that all motors have encoders on them)
        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);

        Billy.armServoBlue.setPosition(Billy.stoneArmInitBlue);
//        Billy.armServoRed.setPosition(stoneArmInitRed);

        if (testModeActive) {
            // DO NOTHING
        }
        else {

//            if (targetsSkyStone != null) {
                targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

                stoneTarget = targetsSkyStone.get(0);
                stoneTarget.setName("Stone Target");

                allTrackables.add(targetsSkyStone.get(0));
//            }
        }
        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)", forwardPosition, rightPosition, clockwisePosition);
        telemetry.addData(">", "Press Play to start");
        telemetry.update();//Update telemetry to update display

    }

    public void updateIMU() {

    }


    public void tensorFlowIdentifyLoop() {// TOP LEFT CORNER IS (0,0) SCREEN WIDTH OF 1280 PIXELS VIEW OF 38 INCHES WIDE
        OpenGLMatrix pose = null;
        VectorF translation = null;

        while (!gamepad1.dpad_up && opModeIsActive()) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                // getRecognitions should always return what's being seen
                List<Recognition> recognitionList = tfod.getRecognitions();
                if (recognitionList != null) {
                    telemetry.addData("# Object Detected", recognitionList.size());

                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : recognitionList) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,right (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getRight());
                        telemetry.addData(String.format("  top,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getTop(), recognition.getBottom());
                        telemetry.addData("Center Pixels", "recog(%d): %.3f",i,
                                (recognition.getRight()+recognition.getLeft())/2);
                    }
                }
            }
            telemetry.addLine("--------------------------");
            telemetry.addLine("Press dpad up to exit");
            telemetry.update();

        }


        if (tfod != null) {
            tfod.shutdown();
        }

    }

    public void vuforiaStoneIdentifyLoop() {

        OpenGLMatrix pose = null;
        VectorF translation = null;

        targetsSkyStone.activate();
        while (!gamepad1.dpad_up && opModeIsActive()) {

            // check all the trackable targets to see which one (if any) is visible.
            targetVisible = false;
            if (((VuforiaTrackableDefaultListener) stoneTarget.getListener()).isVisible()) {
                telemetry.addData("Visible Target", stoneTarget.getName());
                targetVisible = true;

                pose = ((VuforiaTrackableDefaultListener)stoneTarget.getListener()).getPose();
                if (pose != null) {
                    translation = pose.getTranslation();
                    telemetry.addData(stoneTarget.getName() + " X Translation", translation.get(0) / mmPerInch);
                    telemetry.addData(stoneTarget.getName() + " Y Translation", translation.get(1) / mmPerInch);

                }

            }
            if(!targetVisible) {
                telemetry.addData("Visible Target", "none");
            }
            telemetry.addLine("Press dpad up to exit");
            telemetry.update();
        }
        targetsSkyStone.deactivate();
    }

    public void identifySkystone() {
        OpenGLMatrix pose = null;
        VectorF translation = null;
        double stoneY;

        if (((VuforiaTrackableDefaultListener) stoneTarget.getListener()).isVisible()) {
            telemetry.addData("Visible Target", stoneTarget.getName());

            pose = ((VuforiaTrackableDefaultListener) stoneTarget.getListener()).getPose();
            if (pose != null) {
                translation = pose.getTranslation();
                stoneY = translation.get(1) / mmPerInch;
                telemetry.addData(stoneTarget.getName() + "-Translation", translation);

                skystoneFound = true;

                stoneYLocation = stoneY;
                telemetry.addData("translation of stone INCHES", (translation.get(1) / mmPerInch));

            }
        }
        else {telemetry.addLine("Skystone NOT Visible");}

    }

    public void determineSkystonePosition() {

        if (stoneYLocation <= (-4.0 + cons.adjustVuforiaPhone)) {// was <= -7.0                                                                                  // FRONT camera <= ((-100/mmPerInch) //|\\     was >= ((100 for BACK camera
            telemetry.addData("Skystone status", "Left");                                                                                                        // was RIGHT for camera on robot left
            stonePos = "Left";
            stoneSelect = 0;
        }
        if (stoneYLocation < (4.0 + cons.adjustVuforiaPhone) && stoneYLocation > (-4.0 + cons.adjustVuforiaPhone)) {// was < 2.0  && > -6.0       // FRONT camera < ((100/mmPerInch) && > ((-100/mmPerInch)
            telemetry.addData("Skystone status", "CENTER");
            stonePos = "Center";
            stoneSelect = 1;
        }
        if (stoneYLocation >= (4.0 + cons.adjustVuforiaPhone)) {  // was >= 2.0                                                                                      // FRONT camera >= ((100/mmPerInch) //|\\     was <= ((-100 for BACK camera
            telemetry.addData("Skystone status", "Right");                                                                                                      // was LEFT for camera on robot left
            stonePos = "Right";
            stoneSelect = 2;
        }
        if(!skystoneFound) /*NOT Visible*/ {

            if(sideColor == -1) {
                telemetry.addData("Skystone status", "Not Visible: Right");// was RIGHT for camera on robot left
                stonePos = "Right";
                stoneSelect = 2;
                telemetry.update();

            }
            else {
                telemetry.addData("Skystone status", "Not Visible: Left");// was RIGHT for camera on robot left
                stonePos = "Left";
                stoneSelect = 0;
                telemetry.update();
            }
        }
    }

    public void tensorFlowIdSkystone() {

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            // getRecognitions should always return what's being seen
            List<Recognition> recognitionList = tfod.getRecognitions();
            if (recognitionList != null) {
                telemetry.addData("# Object Detected", recognitionList.size());

                // step through the list of recognitions and display boundary info.

                for (int i = 0;i< recognitionList.size();i++) {

                    middleOfStone = (recognitionList.get(i).getRight() + recognitionList.get(i).getLeft()) / 2;

                    if (recognitionList.get(i).getLabel().equals("Skystone")) {
                        if(recognitionList.get(i).getWidth() < 10.0/cons.inchesPerPixel)
                            skystoneFound = true;

                            Billy.angleUnWrap();
                            angleWhenFound = Billy.robotHeading;
                    }

                    telemetry.addData(String.format("label (%d)", i), recognitionList.get(i).getLabel());
                    telemetry.addData(String.format("\tleft,right (%d)", i), "%.03f , %.03f",
                            recognitionList.get(i).getLeft(), recognitionList.get(i).getRight());
                    telemetry.addData(String.format("\ttop,bottom (%d)", i), "%.03f , %.03f",
                            recognitionList.get(i).getTop(), recognitionList.get(i).getBottom());
                    telemetry.addData("\twidth","(%d) = %.03f", i, recognitionList.get(i).getWidth());
                    telemetry.addData("\tMiddle Pixels", "recog(%d): %.3f",i, middleOfStone);
                }
            }
        }
    }

    public void determineSkystonePositionTF() {

        zoneShift = (Math.tan(angleWhenFound) * cons.distanceFromStones) / cons.inchesPerPixel;
        leftBound -= zoneShift;
        rightBound -= zoneShift;

        if (middleOfStone <= leftBound) {
            telemetry.addData("Skystone status", "Left");
            stonePos = "Left";
            stoneSelect = 0;
        }
        if (middleOfStone > leftBound && middleOfStone < rightBound) {
            telemetry.addData("Skystone status", "CENTER");
            stonePos = "Center";
            stoneSelect = 1;
        }
        if (middleOfStone >= rightBound) {
            telemetry.addData("Skystone status", "Right");
            stonePos = "Right";
            stoneSelect = 2;
        }
        if(!skystoneFound) /*NOT Visible*/ {

            if(sideColor == -1) {
                telemetry.addData("Skystone status", "Not Visible: Right");
                stonePos = "Right";
                stoneSelect = 2;
                telemetry.update();

            }
            else {
                telemetry.addData("Skystone status", "Not Visible: Left");
                stonePos = "Left";
                stoneSelect = 0;
                telemetry.update();
            }
        }
    }

    public void setStoneSideways() {

        if(sideColor == 1) {
            if(stonePos.equals("Left")) {

                stoneSideways = -9.0 + cons.skystoneExtraSideways;// was -10.0
                secondStoneBackup = 11.0 + cons.skystoneExtraBack;// was 13.0
            }
            if(stonePos.equals("Center")) {

                stoneSideways = -2.0 + cons.skystoneExtraSideways;// was -3.0
                secondStoneBackup = 13.0 + cons.skystoneExtraBack;// ws 15.0
            }
            if(stonePos.equals("Right")) {

                stoneSideways = 6.0 + cons.skystoneExtraSideways;// was 8.0
                secondStoneBackup = 13.0 + cons.skystoneExtraBack;// was 14.0
            }
        }

        if(sideColor == -1) {
            if(stonePos.equals("Right")) {

                stoneSideways = -15.0 + cons.skystoneExtraSideways;// was -14.0
                secondStoneBackup = 11.0 + cons.skystoneExtraBack;// was 10.0
            }
            if(stonePos.equals("Center")) {

                stoneSideways = -7.5 + cons.skystoneExtraSideways;//was -8.0
                secondStoneBackup = 10.0 + cons.skystoneExtraBack;// was 11.0
            }
            if(stonePos.equals("Left")) {

                stoneSideways = -0.5 + cons.skystoneExtraSideways;// was -2.0
                secondStoneBackup = 10.5 + cons.skystoneExtraBack;// was 7.5
            }
        }

    }

    public void vuforiaStoneLocateInches() {

        OpenGLMatrix pose = null;
        VectorF translation = null;
        double degreesToTurn = 0;

        //? Use local variable in place of cons.adjustVuforiaPhone that gets set to cons.adjustVuforiaPhone but then can be set to -1.0 for Blue only to shift camera zones to the left

        targetsSkyStone.activate();

        start = runtime.time();
        while ((runtime.time() - start < vuforiaWaitTime) && opModeIsActive() && !skystoneFound) {

            identifySkystone();

            telemetry.update();

        }

        determineSkystonePosition();

        telemetry.addData("SkystonePos", stonePos);
        telemetry.addData("Skystone Select", stoneSelect);
        telemetry.addData("Skystone Y Location", stoneYLocation);
//        pressAToContinue();

        targetsSkyStone.deactivate();

        setStoneSideways();

        telemetry.addData("SkystonePos:", stonePos);
        telemetry.addData("sideways to Skystone:", stoneSideways);
//        pressAToContinue();

    }

    public void vuforiaStoneLIRotate() {

        targetsSkyStone.activate();

        start = runtime.time();
        while ((runtime.time() - start < 1) && opModeIsActive() && !skystoneFound) {

            identifySkystone();

            telemetry.addData("Elapsed Time", runtime.time() - start);
            telemetry.update();
        }
        start = runtime.time();
        while ((runtime.time() - start < 1) && opModeIsActive() && !skystoneFound) {
            Billy.setMotorPower(0.1);

            identifySkystone();

            telemetry.addData("Elapsed Time", runtime.time() - start);
            telemetry.update();
        }
        Billy.setMotorPower(0);

        start = runtime.time();
        while ((runtime.time() - start < 2) && opModeIsActive() && !skystoneFound) {
            Billy.setMotorPower(-0.1);

            identifySkystone();

            telemetry.addData("Elapsed Time", runtime.time() - start);
            telemetry.update();
        }
        Billy.setMotorPower(0);

        determineSkystonePosition();

        telemetry.addData("translation of stone INCHES", stoneYLocation);
        telemetry.update();

        Billy.IMUDriveRotate(0, "Return to 0", this);

        telemetry.addData("SkystonePos", stonePos);
        telemetry.addData("Skystone Select", stoneSelect);
        telemetry.addData("Skystone Y Location", stoneYLocation);
//        pressAToContinue();

        targetsSkyStone.deactivate();

        setStoneSideways();

        telemetry.addData("SkystonePos:", stonePos);
        telemetry.addData("sideways to Skystone:", stoneSideways);
//        pressAToContinue();

    }

    public void tensorFlowStoneLIRotate() {

        start = runtime.time();
        while ((runtime.time() - start < 1) && opModeIsActive()/* && !skystoneFound*/) {

            tensorFlowIdSkystone();

            telemetry.addData("Elapsed Time", runtime.time() - start);
            telemetry.update();
        }
        start = runtime.time();
        while ((runtime.time() - start < 1) && opModeIsActive() && !skystoneFound) {
            Billy.setMotorPower(0.1);

            tensorFlowIdSkystone();

            telemetry.addData("Elapsed Time", runtime.time() - start);
            telemetry.update();
        }
        Billy.setMotorPower(0);

        start = runtime.time();
        while ((runtime.time() - start < 2) && opModeIsActive() && !skystoneFound) {
            Billy.setMotorPower(-0.1);

            tensorFlowIdSkystone();

            telemetry.addData("Elapsed Time", runtime.time() - start);
            telemetry.update();
        }
        Billy.setMotorPower(0);

        determineSkystonePositionTF();

        telemetry.addData("Skystone TF Center", middleOfStone);
        telemetry.update();

        Billy.IMUDriveRotate(0, "Return to 0", this);

        telemetry.addData("SkystonePos", stonePos);
        telemetry.addData("Skystone Select", stoneSelect);
        telemetry.addData("Skystone TF Center", middleOfStone);
        telemetry.addData("TF Zone Bounds","[L: %.02f, R: %.02f]",  leftBound, rightBound);

//        pressAToContinue();

        setStoneSideways();

        telemetry.addData("SkystonePos:", stonePos);
        telemetry.addData("sideways to Skystone:", stoneSideways);
//        pressAToContinue();

    }

    public void vuforiaStoneLocateOffline(int stoneSelect) {

        if(sideColor == 1) {
            if(stoneSelect == 0) {

                stoneSideways = -14 + cons.skystoneExtraSideways;// was -12
                stonePos = "Left";
            }
            if(stoneSelect == 1) {

                stoneSideways = -6 + cons.skystoneExtraSideways;// was -4
                stonePos = "Center";
            }
            if(stoneSelect == 2) {

                stoneSideways = 5 + cons.skystoneExtraSideways;// was 8
                stonePos = "Right";
            }
        }

        if(sideColor == -1) {
            if(stoneSelect == 0) {

                stoneSideways = -14 + cons.skystoneExtraSideways;// was -12
                stonePos = "Right";
            }
            if(stoneSelect == 1) {

                stoneSideways = -6 + cons.skystoneExtraSideways;// was -4
                stonePos = "Center";
            }
            if(stoneSelect == 2) {

                stoneSideways = 5 + cons.skystoneExtraSideways;// was 8
                stonePos = "Left";
            }
        }
        //Added loop to simulate teh timedelay of waiting for Vuforia
        for(int i=0;i<20;i++){
            Billy.angleUnWrap();//update position - not moving
            updateIMU();//update field positions while not moving
        }
        telemetry.addData("SkystonePos:", stonePos);
        telemetry.addData("sideways to Skystone:", stoneSideways);
//        pressAToContinue();

    }

//    public void findSkystoneSideways() {
//
//        start = runtime.time();
//        while ((runtime.time() - start < 1) && opModeIsActive()/* && !skystoneFound*/) {
//
//            tensorFlowIdSkystone();
//
//            telemetry.addData("Elapsed Time", runtime.time() - start);
//            telemetry.update();
//        }
//
//        nextStone();
//
//        start = runtime.time();
//        while ((runtime.time() - start < 1) && opModeIsActive() && !skystoneFound) {
//
//            tensorFlowIdSkystone();
//
//            telemetry.addData("Elapsed Time", runtime.time() - start);
//            telemetry.update();
//        }
//
//        nextStone();
//
//        // The third Stone must be Skystone if correctly NOT identifying other stones
//
//        determineSkystonePositionTF();
//
//        telemetry.addData("Skystone TF Center", middleOfStone);
//        telemetry.update();
//
//        Billy.IMUDriveRotate(0, "Return to 0", this);
//
//        telemetry.addData("SkystonePos", stonePos);
//        telemetry.addData("Skystone Select", stoneSelect);
//        telemetry.addData("Skystone TF Center", middleOfStone);
//        telemetry.addData("TF Zone Bounds","[L: %.02f, R: %.02f]",  leftBound, rightBound);
//
////        pressAToContinue();
//
//        setStoneSideways();
//
//        telemetry.addData("SkystonePos:", stonePos);
//        telemetry.addData("sideways to Skystone:", stoneSideways);
////        pressAToContinue();
//    }

    public void determineSkystonePosition2Look() {

        if(sideColor == 1) {
            if (stoneYLocation > 0) {
                telemetry.addData("Skystone status", "RIGHT");
                stonePos = "Right";
            }
            if (stoneYLocation < 0) {
                telemetry.addData("Skystone status", "CENTER");
                stonePos = "Center";
            }
        }
        if(sideColor == -1) {
            if (stoneYLocation < 0) {
                telemetry.addData("Skystone status", "LEFT");
                stonePos = "Left";
            }
            if (stoneYLocation > 0) {
                telemetry.addData("Skystone status", "CENTER");
                stonePos = "Center";
            }
        }

        if(!skystoneFound) /*NOT Visible*/ {

            if(sideColor == -1) {
                telemetry.addData("Skystone status", "Not Visible: Right");// was RIGHT for camera on robot left
                stonePos = "Right";
                telemetry.update();

            }
            else {
                telemetry.addData("Skystone status", "Not Visible: Left");// was RIGHT for camera on robot left
                stonePos = "Left";
                telemetry.update();
            }
        }
    }

    public void setStoneSideways2Look() {

        if(sideColor == 1) {
            if(stonePos.equals("Left")) {

                stoneSideways = 14.5 + cons.skystoneExtraSideways;// was 14.5
                secondStoneBackup = 14 + cons.skystoneExtraBack;// was 114.0
            }
            if(stonePos.equals("Center")) {

                stoneSideways = 7.5 + cons.skystoneExtraSideways;// was 7.5
                secondStoneBackup = 15.0 + cons.skystoneExtraBack;// ws 15.0
            }
            if(stonePos.equals("Right")) {

                stoneSideways = -2.5 + cons.skystoneExtraSideways;// was -2.5
                secondStoneBackup = 15.5 + cons.skystoneExtraBack;// was 15.5
            }
        }

        if(sideColor == -1) {
            if(stonePos.equals("Right")) {

                stoneSideways = 19.5 + cons.skystoneExtraSideways;// was 20.0
                secondStoneBackup = 12.0 + cons.skystoneExtraBack;// was 14.0
            }
            if(stonePos.equals("Center")) {

                stoneSideways = 10.0 + cons.skystoneExtraSideways;//was 11.0
                secondStoneBackup = 12.0 + cons.skystoneExtraBack;// was 15.0
            }
            if(stonePos.equals("Left")) {

                stoneSideways = 2.0 + cons.skystoneExtraSideways;// was -3.0 was also 0 after
                secondStoneBackup = 11.0 + cons.skystoneExtraBack;// was 15.0
            }
        }

    }

    public void knowSkystonePosition2Look() {
        double time = 0;

        targetsSkyStone.activate();

        start = runtime.time();
        while ((runtime.time() - start < 2) && opModeIsActive() && !skystoneFound) {

            identifySkystone();

            telemetry.addData("Elapsed Time", "%.1f",runtime.time() - start);
            time = runtime.time() - start;
            telemetry.update();
        }

        determineSkystonePosition2Look();

        telemetry.addData("time to find Skystone (%.2f)", time);
        telemetry.addData("SkystonePos", stonePos);
        telemetry.addData("Skystone Y Location", stoneYLocation);

        targetsSkyStone.deactivate();

        setStoneSideways2Look();

        telemetry.addData("SkystonePos:", stonePos);
        telemetry.addData("sideways to Skystone:", stoneSideways);
    }

    public void fwdToTwoStone() {
        if(!drivingMiniBot) {

            Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
            sleep(400);// 0.400 of a second
            Billy.slide.setPower(0);

            Billy.stoneServoLeft.setPosition(0.15);
            Billy.stoneServoRight.setPosition(0.15);
        }

        telemetry.addData("robotHeading:","(%.2f)", Billy.robotHeading);
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, cons.forwardFirstMove, 0,"Forward 16 inches",this);

//        pressAToContinue();
    }

    public void nextStone() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (7.5 * sideColor), 0, "Right 7.5 inches",this);
    }

//      Old Skystone Code from TT
//    public void fwdToStone() {
//
////        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
////        sleep(400);// 0.400 of a second
////        Billy.slide.setPower(0);
////
////        Billy.stoneServoLeft.setPosition(blockCamera);
////        Billy.stoneServoRight.setPosition(blockCamera);
//
//        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack,22, 0, "Forward 22 inches",this);
//
//    }
//
//
//    public void grabSkyStone() {
//
//        Billy.angleUnWrap();
//
////        Billy.stoneServoLeft.setPosition(0.15);
////        Billy.stoneServoRight.setPosition(0.15);
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (0 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate to 0 degrees CCW",this);
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (8 + extraFwdToBlock),  "Forward 8 inches",this);
//
//        //grab skystone with gripper
////        Billy.stoneServoLeft.setPosition(0.5);
////        Billy.stoneServoRight.setPosition(0.5);
//
//        sleep(250);
//
//    }
//
//    public void moveAcrossBridge() {
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CCW",this);
//
//        Billy.angleUnWrap();// think about commenting
//
//        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
//        //pressAToContinue();
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - Billy.robotHeading) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate to 90 degrees CCW",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,45.5 + extraFwd + cons.pHM.get("foundationExtraFwd").value,  "Forward 45.5+ inches",this);//was 48
//
//    }
//
//    public void placeStoneOnFoundation() {
//
//        //pressAToContinue();
//        //move jack to be above Foundation WAS 5
////        DeltaH = 5;
////        Billy.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Up 3 Inches",this);
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,4, "Forward 6 inches",this);//was 8 in grab skystone??
//
//        //pressAToContinue();
////        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,(8 - (foundationPosChange/13)),  "Forward 8 inches",this);
//        //move slide OUT more
////        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
////        sleep(400);// 0.400 of a second
////        Billy.slide.setPower(0);
////
////        //pressAToContinue();
////        //Place stone with gripper
////        Billy.stoneServoLeft.setPosition(0.15);
////        Billy.stoneServoRight.setPosition(0.15);
////        sleep(250);
////
////        Billy.stoneServoLeft.setPosition(0.9);
////        Billy.stoneServoRight.setPosition(0.9);
////        sleep(200);
////
////        //pressAToContinue();
////        //move slide IN
////        Billy.slide.setPower(cons.pHM.get("slidePowerLimit").value);
////        sleep(400);// 0.400 of a second
////        Billy.slide.setPower(0);
//
//        //pressAToContinue();
//
//    }
//
//    public void bridgeCrossSkyStone() {
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,(-4 - insideOutside),  "Back 4-24 inches",this);
//        // make shorter for OUTSIDE
//
//        moveAcrossBridge();
//
//        //pressAToContinue();
//        Billy.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Up 3 Inches",this);
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,((insideOutside + foundationPosChange) * sideColor),  "Sideways 0-50ish inches",this);
//
//        Billy.angleUnWrap();
//
//        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
//        //pressAToContinue();
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - (Billy.robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate to -90 degrees CCW",this);
//
//        placeStoneOnFoundation();
//    }
//
//    public void bridgeCrossSkyStoneF() {
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,(-4 - insideOutside),  "Back 4-24 inches",this);
//        // make shorter for OUTSIDE
//
//        moveAcrossBridge();
//
//        //pressAToContinue();
//        Billy.moveJack(5, cons.pHM.get("jackPowerLimit").value,"Jack Up 5 Inches",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,((insideOutside + foundationPosChange) * sideColor),  "Sideways 0-50ish inches",this);
//
//    }
//
    public void grabAndRotateFoundation() {

        //slide foudation away from wall
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,10 * sideColor,  0,"sideways with foundation",this);// was 4

        //release foundation from servos
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);
        if(sideColor == 1) {haveBlueFoundation = false;}
        if(sideColor == -1) {haveRedFoundation = false;}

        //drive along foundation to get to center to rotate it more directly
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,16 * sideColor,  0,"sideways without foundation",this);// was 4

        // grab foundation with servos
        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);
        if(sideColor == 1) {haveBlueFoundation = true;}
        if(sideColor == -1) {haveRedFoundation = true;}

        //Rotate foundation
        Billy.IMUDriveRotate(-90*sideColor,"Rotate 90 deg so long side with parallel to wall",this);// was 4

        //release foundation from servos
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);
        if(sideColor == 1) {haveBlueFoundation = false;}
        if(sideColor == -1) {haveRedFoundation = false;}
        //drive along foundation to get to center to push it more straight to wall
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-6 * sideColor,  -90*sideColor,"sideways without foundation to push",this);// was 4

        // grab foundation with servos
        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);
        if(sideColor == 1) {haveBlueFoundation = true;}
        if(sideColor == -1) {haveRedFoundation = true;}

        //push foundation up to wall
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack,13,  -90*sideColor,"Forward pushing foundation",this);// was 4

        //release foundation from servos
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);
        if(sideColor == 1) {haveBlueFoundation = false;}
        if(sideColor == -1) {haveRedFoundation = false;}
        //Back away from foundation
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -6, -90*sideColor, "backup to clear foundation", this);
        //move right/left to align with bridge
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (-2-insideOutside)*sideColor, -90*sideColor, "align to inside or outside", this);
        //finish backing up to bridge
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -45, -90*sideColor, "backup", this);


    }
//
//    public void straightToCorner() {
//
//        //pressAToContinue();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,20,  "Forward 20 inches",this);
//
//        //pressAToContinue();
//        Billy.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);
//
//        //move slide OUT more
////        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
////        sleep(600);// 0.600 of a second
////        Billy.slide.setPower(0);
//
//        //pressAToContinue();
//        //Place stone with gripper
////        Billy.stoneServoLeft.setPosition(0.15);
////        Billy.stoneServoRight.setPosition(0.15);
////
////        sleep(200);
////        // Release foundation with servos
////        Billy.servoFoundationL.setPosition(0.10);
////        Billy.servoFoundationR.setPosition(0.90);
//    }
//
//    public void backSkyStoneAndFoundation() {
//
//        //pressAToContinue();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-15,  "Backwards 15 inches",this);//was -20
//
//        //Stow gripper
////        Billy.stoneServoLeft.setPosition(1);
////        Billy.stoneServoRight.setPosition(1);
////        sleep(200);
////
////        //pressAToContinue();
////        //move slide IN
////        Billy.slide.setPower(cons.pHM.get("slidePowerLimit").value);
////        sleep(600);// 0.600 of a second
////        Billy.slide.setPower(0);
//
//        //pressAToContinue();
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate, 50 * sideColor, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 45 degrees CW", this);
//    }
//
//    public void parkSkyStoneF() {
//
//        //pressAToContinue();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,((-insideOutside - foundationPosChange) * sideColor),  "Sideways 0-50ish inches",this);
//
//        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
//        //pressAToContinue();
//
//
////        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - (Billy.robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate to -90 degrees CCW",this);
//
//        //pressAToContinue();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-13,  "Backward 13 inches",this);
//
//        //pressAToContinue();
//        // move jack down
//        Billy.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-11,  "Backward 11 inches",this);
//
//    }
//
//    public void parkSkyStone() {
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-(8 - (foundationPosChange/13)),  "Backward  inches",this);
//
//        //pressAToContinue();
//
//        Billy.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,((-(-4 + insideOutside + foundationPosChange)) * sideColor),  "Right 16 inches",this);
//
//        Billy.angleUnWrap();
//
//        //pressAToContinue();
//
////        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - (Billy.robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate to -90 degrees CCW",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-20, "Back 20 inches",this);
//
//    }
//
//    public void crossDropStonePark() {
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-4, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 4 inches",this);
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 90 degrees CCW",this);
//
//        Billy.angleUnWrap();// think about commenting
//
//        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
//
////        if(sideColor == 1) {
////
////            Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
////
////        }
//
////        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,cons.pHM.get("forwardFirstMove").value + extraFwd, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 35+ inches",this);//was 48
//
//        //Drop stone with gripper
////        Billy.stoneServoLeft.setPosition(0.15);
////        Billy.stoneServoRight.setPosition(0.15);
//        sleep(250);
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-6, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 6 inches",this);//was 4
//
//    }
//
//    public void crossDropStoneFor2Old() {
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-4,  "Back 4 inches",this);
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CCW",this);
//
//        Billy.angleUnWrap();// think about commenting
//
////        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
//
////        if(sideColor == 1) {
////
////            Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
////
////        }
//
////        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, cons.pHM.get("forwardFirstMove").value + 35 + extraFwd,  "Forward 35+ inches",this);//was 48
//
//        //Drop stone with gripper
////        Billy.stoneServoLeft.setPosition(0.15);
////        Billy.stoneServoRight.setPosition(0.15);
//        sleep(250);
//
//    }
//
//    public void getSecondStoneOld() {
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-cons.pHM.get("forwardFirstMove").value -35 - extraFwd - 24, "Backward to second stone",this);//was 48
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CW",this);
//
//        grabSkyStone();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-4,  "Back 4 inches",this);
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CCW",this);
//
////        Billy.angleUnWrap();// think about commenting
////
////        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
////
////        if(sideColor == 1) {
////
////            Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
////
////        }
////
////        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,cons.pHM.get("forwardFirstMove").value + 35 + extraFwd + 24,  "Forward with second stone",this);//was 48
//
//        //Drop stone with gripper
////        Billy.stoneServoLeft.setPosition(0.15);
////        Billy.stoneServoRight.setPosition(0.15);
//        sleep(250);
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-6,  "Back 6 inches",this);//was 4
//
//    }

    public void goToStone() {
        telemetry.addData("Stone sideways distance","%.2f",(stoneSideways));
        telemetry.addData("Stone sideways variable","%.2f", stoneSideways);
        telemetry.addData("CONSTANTS adj Right variable","%.2f",cons.adjRight);
        telemetry.addData("sideColor Variable","%.2f",sideColor);

//        pressAToContinue();

        if(!drivingMiniBot) {

            Billy.stoneServoLeft.setPosition(1);
            Billy.stoneServoRight.setPosition(1);
            sleep(400);// 0.400 of a second

            Billy.slide.setPower(cons.pHM.get("slidePowerLimit").value);
            sleep(350);// 0.350 of a second
            Billy.slide.setPower(0);
        }

//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, stoneSideways * sideColor, 0, "Sideways to face stone",this);
        Billy.IMUDriveRotate(-90 * sideColor, "Rotate 90 degrees CCW",this);

//        pressAToContinue();

//        Billy.IMUDriveRotate(-90 * sideColor, "Rotate 90 degrees CCW",this);
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, stoneSideways,-90 * sideColor, "Forward to align with stone",this);

//        pressAToContinue();

    }

    public void takeStone1() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, cons.sideGrabSkystone * sideColor, -90 * sideColor, "Right 8 inches",this);

//        pressAToContinue();
        //grab skystone with servo arm
        if(sideColor == 1) {Billy.armServoBlue.setPosition(stoneArmDownBlue);}
        if(sideColor == -1) {Billy.armServoRed.setPosition(stoneArmDownRed);}
        sleep(500);

        //pinch skystone with rack
        if(sideColor == 1) {Billy.rackServoBlue.setPosition(rackOutBlue);}
        if(sideColor == -1) {Billy.rackServoRed.setPosition(rackOutRed);}
        sleep(200);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueSkyStone1 = true;
            }
            if(sideColor == -1) {

                haveRedSkyStone1 = true;
            }
        }
//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (-(cons.sidePullSkystone)) * sideColor, -90 * sideColor, "Left 8 inches",this);

//        pressAToContinue();

        Billy.IMUDriveRotate(-180 * sideColor, "Rotate 90 degrees CCW",this);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, (-(cons.sidePullSkystone - 6)), -180 * sideColor, "Back 2 inches",this);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, ((47 + extraRedDistance) - stoneSideways) * sideColor, -180 * sideColor, "Right 52+ inches",this);
                                                                            // was 52
//        pressAToContinue();

        //release skystone with servo arm
        if(sideColor == 1){Billy.armServoBlue.setPosition(stoneArmUnderBridgeBlue); Billy.rackServoBlue.setPosition(rackInitBlue);}
        if(sideColor == -1){Billy.armServoRed.setPosition(stoneArmUnderBridgeRed); Billy.rackServoRed.setPosition(rackInitRed);}
        sleep(500);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueSkyStone1 = false;
            }
            if(sideColor == -1) {

                haveRedSkyStone1 = false;
            }
        }
//        pressAToContinue();

    }

    public void getStone2() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,( (-35 - (extraRedDistance / 2)) + stoneSideways - 24 ) * sideColor, -180 * sideColor, "Left 45+ inches",this);
                                                                                // was -40
//        pressAToContinue();

        Billy.IMUDriveRotate( -90 * sideColor, "Rotate to 90 degrees CCW",this);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -secondStoneBackup, -90 * sideColor, "Left 8 inches",this);

//        pressAToContinue();

    }

    public void takeStone2() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (cons.sideGrab2Skystone) * sideColor, -90 * sideColor, "Right 8 inches",this);

//        pressAToContinue();

        //grab skystone with servo arm
        if(sideColor == 1) {Billy.armServoBlue.setPosition(stoneArmDownBlue);}
        if(sideColor == -1) {Billy.armServoRed.setPosition(stoneArmDownRed);}
        sleep(500);

        //pinch skystone with rack
        if(sideColor == 1) {Billy.rackServoBlue.setPosition(rackOutBlue);}
        if(sideColor == -1) {Billy.rackServoRed.setPosition(rackOutRed);}
        sleep(200);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueSkyStone2 = true;
            }
            if(sideColor == -1) {

                haveRedSkyStone2 = true;
            }
        }
//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (-(cons.sidePullSkystone)) * sideColor, -90 * sideColor, "Left 8 inches",this);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, secondStoneBackup, -90 * sideColor, "Right 8 inches",this);

//        pressAToContinue();

        Billy.IMUDriveRotate( -180 * sideColor, "Rotate 90 degrees CCW",this);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, (-(cons.sidePullSkystone - 6)), -180 * sideColor, "Back 2 inches",this);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, ( (31 + extraRedDistance) - stoneSideways + 24) * sideColor, -180 * sideColor, "Right with stone 2",this);
                                                                            // was 35 reduced to not touch first Skystone
//        pressAToContinue();
        //release skystone with servo arm
        if(sideColor == 1){Billy.armServoBlue.setPosition(stoneArmUnderBridgeBlue); Billy.rackServoBlue.setPosition(rackInitBlue);}
        if(sideColor == -1){Billy.armServoRed.setPosition(stoneArmUnderBridgeRed); Billy.rackServoRed.setPosition(rackInitRed);}
        sleep(500);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueSkyStone2 = false;
            }
            if(sideColor == -1) {

                haveRedSkyStone2 = false;
            }
        }
//        pressAToContinue();

    }

    public void getStone2TimeCheck() {

        if(runtime.time() > 16) {// 16 sec is guesstimate

            getSecondStone = false;
        }

        if(getSecondStone) {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,( -35 - stoneSideways - 24 ) * sideColor, -180 * sideColor, "Left 45+ inches",this);
        // was -40
//        pressAToContinue();

        Billy.IMUDriveRotate( -90 * sideColor, "Rotate 90 degrees CW",this);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -secondStoneBackup, -90 * sideColor, "Left 8 inches",this);

//        pressAToContinue();
        }

    }

    public void takeStone2TimeCheck() {

        if(getSecondStone) {

            Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (cons.sideGrab2Skystone) * sideColor, -90 * sideColor, "Right 8 inches", this);

//        pressAToContinue();

            //grab skystone with servo arm
            if (sideColor == 1) {
                Billy.armServoBlue.setPosition(stoneArmDownBlue);
            }
            if (sideColor == -1) {
                Billy.armServoRed.setPosition(stoneArmDownRed);
            }
            sleep(500);

            if (testModeActive) {
                if (sideColor == 1) {

                    haveBlueSkyStone2 = true;
                }
                if (sideColor == -1) {

                    haveRedSkyStone2 = true;
                }
            }
//        pressAToContinue();

            Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (-(cons.sidePullSkystone)) * sideColor, -90 * sideColor, "Left 8 inches", this);

//        pressAToContinue();

            Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, secondStoneBackup, -90 * sideColor, "Right 8 inches", this);

//        pressAToContinue();

            Billy.IMUDriveRotate(-180 * sideColor, "Rotate 90 degrees CCW", this);

//        pressAToContinue();

            Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (35 + stoneSideways + 24) * sideColor, -180 * sideColor, "Right with stone 2", this);
            // was 40
//        pressAToContinue();
            //release skystone with servo arm
            if (sideColor == 1) {
                Billy.armServoBlue.setPosition(stoneArmUnderBridgeBlue);
            }
            if (sideColor == -1) {
                Billy.armServoRed.setPosition(stoneArmUnderBridgeRed);
            }
            sleep(500);

            if (testModeActive) {
                if (sideColor == 1) {

                    haveBlueSkyStone2 = false;
                }
                if (sideColor == -1) {

                    haveRedSkyStone2 = false;
                }
            }
//        pressAToContinue();

        }

    }

    public void twoStonePark() {

        if(sideColor == 1) {

            Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-13 * sideColor, -180 * sideColor, "Left 10 inches",this);

        }
        if(sideColor == -1) {

            Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-8 * sideColor, -180 * sideColor, "Left 10 inches",this);

        }

    }

    public void grabFoundation() {

        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, 36, 0,"Forward 36 inches to Foundation", this);
        //Added 6 inches for move - 2 for robot starting distance and 4 for distance to servos being 14: from back of robot vs. 18" from back

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, 2,0,"Forward 2 inches to Foundation", this);

        // grab foundation with servos
        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueFoundation = true;
            }
            if(sideColor == -1) {

                haveRedFoundation = true;
            }
        }

        sleep(250);

    }

    public void pullFoundation() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -29, 0, "Backward 29 inches with Foundation", this);

//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, -2, (cons.pHM.get("drivePowerLimit").value / 2), "Backward 2 inches with Foundation", this);

        Billy.IMUDriveRotate((-20 * sideColor), "Rotate 20 degrees CCW",this);//15 was too close

        // release foundation from gripper
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueFoundation = false;
            }
            if(sideColor == -1) {

                haveRedFoundation = false;
            }
        }
    }

    public void pullFoundationAngle() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -33, -10 * sideColor, "Backward 31 inches at 5 CCW with Foundation", this);

//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, 10 * sideColor, -15 * sideColor, "Right 10 inches with Foundation", this);

        Billy.IMUDriveRotate((-20 * sideColor), "Rotate to 20 degrees CCW",this);
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, 5, -90 * sideColor, "Forward 5 inches with Foundation", this);

        // release foundation from gripper
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueFoundation = false;
            }
            if(sideColor == -1) {

                haveRedFoundation = false;
            }
        }
    }

    public void grabFoundationSideways() {

        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        Billy.moveJack(3, (cons.JACK_POWER_LIMIT / 2), "Move Jack up 3 inches", this);

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, 36, 0,"Forward 36 inches to Foundation", this);
        //Added 6 inches for move - 2 for robot starting distance and 4 for distance to servos being 14: from back of robot vs. 18" from back

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, 2,0,"Forward 2 inches to Foundation", this);

        // grab foundation with servos
        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueFoundation = true;
            }
            if(sideColor == -1) {

                haveRedFoundation = true;
            }
        }

        sleep(250);

    }

    public void pullFoundationSideways() {

        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, 12 * sideColor, 0, "Right 12 inches", this);

        pressAToContinue();

        Billy.IMUDriveRotate((-90 * sideColor), "Rotate to 90 degrees CCW",this);

        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, 8, -90 * sideColor, "Forward 8 inches at 90 CCW with Foundation", this);

        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, -20 * sideColor, -90 * sideColor, "Right 20 inches at 90 CCW with Foundation", this);

        pressAToContinue();

        // release foundation from gripper
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueFoundation = false;
            }
            if(sideColor == -1) {

                haveRedFoundation = false;
            }
        }

//        sleep(500);

        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -5, -90 * sideColor, "Backward 20 inches at 90 CCW with Foundation", this);

        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, -10 * sideColor, -90 * sideColor, "Left 55 20 at 90 CCW with Foundation", this);

        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -10, -90 * sideColor, "Backward 25 inches at 90 CCW with Foundation", this);

        pressAToContinue();

        Billy.moveJack(0, (cons.JACK_POWER_LIMIT / 2), "Move Jack Down to 0", this);

        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -30, -90 * sideColor, "Backward 25 inches at 90 CCW with Foundation", this);

    }

    public void awayFromFoundationOneMove() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -2,  (-20 * sideColor),"Backward 2 inches away from Foundation", this);

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (50 * sideColor), 0,"Right 25 inches", this);//was 21

    }

    public void aroundFoundation() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -2,  (-20 * sideColor),"Backward 2 inches away from Foundation", this);

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (26 * sideColor),(-20 * sideColor),"Right 26 inches around Foundation", this);

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, 18,(-20 * sideColor),"Forward 18 inches around Foundation", this);

    }

    public void pushFoundation() {
        // touching foundation to push it

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (-foundationPush * sideColor), 0,"Right 8 inches pushing Foundation", this);

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (foundationPush * sideColor), 0,"Left 8 inches away from Foundation", this);

    }

//    public void awayFromFoundationOutside() {
//
//        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, 18,  "Forward 20 inches towards wall", this);
//
//        //pressAToContinue();
//
////        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate, (-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 90 Degrees CCW", this);
////
////        //pressAToContinue();
//
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, -16 * sideColor,  "Left 16 inches to park", this);
//
//    }

    public void awayFromFoundation() {

//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (45 * sideColor),  "Right 45 inches to park", this);
//        //was 50

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (25 * sideColor),(-20 * sideColor),"Right 25 inches", this);//was 21

        Billy.IMUDriveRotate( 0,"Rotate 20 degrees CW", this);//15 was too close

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -10, 0, "Backward 14 inches to wall", this);//was 16

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (20 * sideColor), 0, "Right 20 inches", this);//was 24

    }

    public void foundationInCorner() {
        int h=0;
        for(h=1;h<4;h++) {
            Billy.IMUDriveRotate((-10 * sideColor)*h, "rotate", this);
            Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -12, (-10 * sideColor)*h, "backup", this);
        }
        Billy.IMUDriveRotate(-90*sideColor, "rotate parallel to wall", this);

        // release foundation from gripper
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        if(testModeActive) {
            if(sideColor == 1) {

                haveBlueFoundation = false;
            }
            if(sideColor == -1) {

                haveRedFoundation = false;
            }
        }

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -6, -90*sideColor, "backup to clear foundation", this);
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (24-insideOutside)*sideColor, -90*sideColor, "align to inside or outside", this);
        /* Needed to change the Right/Left to be 24-insideOutside since robot alings on outside
        /* without this move and insideOutside value for outside = 24
        /*
         */

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack, -44, -90*sideColor, "backup", this);

    }

}