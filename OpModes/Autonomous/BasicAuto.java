package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import Skystone_14999.HarwareConfig.HardwareBilly;
import Skystone_14999.OpModes.BasicOpMode;

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
    public double sideGrabStone = 18;
    public double foundationPosChange = 0;// 26 for unmoved Foundation, 0 for moved Foundation
    public double insideOutside = 0;// 0 for Inside, 24 for Outside
    public double foundationInOut = 0;// 0 for Inside, 26 for Outside
    public double foundationPush = 8;
    public double sideColor = 1;// + for Blue, - for Red, KEEP BLUE

    public double blockCamera = 0.525;
    public double extraFwdToBlock = 0;

    public enum autoChoice {SkyStoneOutside, SkyStoneInside, SkyStoneOutsideUnmoved, SkyStoneInsideUnmoved}

    //Define all double variables
    public double start = 0;//timer variable to use for setting waits in the code
    public float hsvValues[] = {0F, 0F, 0F};
    public String stonePos = "Unknown";

    public int stoneSelect = -1;// FOR BLUE: 0 nearest to bridge, 1 center, 2 farthest from bridge FOR RED: 0 farthest from bridge, 1 center, 2 nearest to bridge

    public double stoneArmUp = 0.25;
    public double stoneArmDown = 0.5;

    public ElapsedTime runtime = new ElapsedTime(); //create a counter for elapsed time

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    public VuforiaLocalizer vuforia;
    public boolean targetVisible = false;

    public VuforiaTrackables targetsSkyStone = null;

    public List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    @Override
    //    public void runOpMode() throws InterruptedException {
    public void runOpMode() {


    }

    public void initialize() {

        readOrWriteHashMap();

        Billy.init(hardwareMap, cons);

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

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");

        allTrackables.addAll(targetsSkyStone);

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)", forwardPosition, rightPosition, clockwisePosition);
        telemetry.addData(">", "Press Play to start");
        telemetry.update();//Update telemetry to update display

    }// sets: RUN_TO_POSITION, ZeroPowerBehaviour.FLOAT, and 0 power & targetPos

    public void initializeMiniBot() {

        readOrWriteHashMap();

        Billy.initMiniBot(hardwareMap, cons);

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

        Billy.stoneServoArm.setPosition(0);

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");

        allTrackables.addAll(targetsSkyStone);

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)", forwardPosition, rightPosition, clockwisePosition);
        telemetry.addData(">", "Press Play to start");
        telemetry.update();//Update telemetry to update display

    }

    public void vuforiaStoneIdentifyLoop() {

        targetsSkyStone.activate();
        while (!isStopRequested()) {

            // check all the trackable targets to see which one (if any) is visible.
            targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;

                    break;
                }

            }
            if(!targetVisible) {
                telemetry.addData("Visible Target", "none");
            }
            telemetry.update();
        }
        targetsSkyStone.deactivate();
    }

    public boolean vuforiaStoneIdentifyExit() {
        boolean skystone = false;

        targetsSkyStone.activate();
        double start = runtime.time();
        while (!isStopRequested() && !skystone && ( (runtime.time() - start) < 1) ) {// was 2 seconds

            // check all the trackable targets to see which one (if any) is visible.
            targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;

                    if (trackable.getName() == "Stone Target") {

                        skystone = true;
                        telemetry.addLine("\n If statement successful for Stone Target \n");
                    }

                    break;
                }
            }

            if(!targetVisible) {
                telemetry.addData("Visible Target", "none");
            }
            //telemetry.update();
        }
        targetsSkyStone.deactivate();

        return skystone;
    }

    public void vuforiaStoneLocate() {

        OpenGLMatrix pose = null;
        VectorF translation = null;
        double degreesToTurn = 0;

        telemetry.update();

        targetsSkyStone.activate();

        start = runtime.time();
        while ((runtime.time() - start < 2) && opModeIsActive()) {
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());

                    pose = ((VuforiaTrackableDefaultListener)trackable.getListener()).getPose();
                    if (pose != null) {
                        translation = pose.getTranslation();
                        telemetry.addData(trackable.getName() + "-Translation", translation);
                        degreesToTurn = Math.toDegrees(Math.atan2(translation.get(0), translation.get(1)));
                        telemetry.addData(trackable.getName() + "-Degrees", degreesToTurn);

                        if (translation.get(1) >= 100) {
                            telemetry.addData("Skystone status", "Left");// was RIGHT for camera on robot left
                            stonePos = "Left";
                            stoneSelect = 0;
                        }
                        if (translation.get(1) < 100 && translation.get(1) >  -100) {
                            telemetry.addData("Skystone status", "CENTER");
                            stonePos = "Center";
                            stoneSelect = 1;
                        }
                        if (translation.get(1) <= -100) {
                            telemetry.addData("Skystone status", "Right");// was LEFT for camera on robot left
                            stonePos = "Right";
                            stoneSelect = 2;
                        }
                        telemetry.addData("translation of stone", translation.get(1));
                        telemetry.update();
                    }
                }

            }

        }
        if(stonePos.equals("Unknown") || stoneSelect == -1) /*NOT Visible*/ {
            telemetry.addData("Skystone status", "Not Visible: Left");// was RIGHT for camera on robot left
            stonePos = "Left";
            stoneSelect = 0;
            telemetry.update();
        }

        telemetry.addData("SkystonePos", stonePos);
        telemetry.addData("Skystone Select", stoneSelect);
//        pressAToContinue();

        targetsSkyStone.deactivate();

        if(sideColor == 1) {
            if(stonePos.equals("Left")) {

                stoneSideways = -14;// was -12
            }
            if(stonePos.equals("Center")) {

                stoneSideways = -6;// was -4
            }
            if(stonePos.equals("Right")) {

                stoneSideways = 5;// was 8
            }
        }

        if(sideColor == -1) {
            if(stonePos.equals("Right")) {

                stoneSideways = -14;// was -12
            }
            if(stonePos.equals("Center")) {

                stoneSideways = -6;// was -4
            }
            if(stonePos.equals("Left")) {

                stoneSideways = 5;// was 8
            }
        }
        telemetry.addData("SkystonePos:", stonePos);
        telemetry.addData("sideways to Skystone:", stoneSideways);
//        pressAToContinue();

    }

    public void fwdToStone() {

//        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
//        sleep(400);// 0.400 of a second
//        Billy.slide.setPower(0);
//
//        Billy.stoneServoLeft.setPosition(blockCamera);
//        Billy.stoneServoRight.setPosition(blockCamera);

        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,22, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 22 inches",this);

    }

    public void fwdToTwoStone() {

//        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
//        sleep(400);// 0.400 of a second
//        Billy.slide.setPower(0);

//        Billy.stoneServoLeft.setPosition(blockCamera);
//        Billy.stoneServoRight.setPosition(blockCamera);

        telemetry.addData("robotHeading:","(%.2f)", Billy.robotHeading);
        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack,16, 0,"Forward 16 inches",this);

//        pressAToContinue();
    }

    public void nextStone() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft,(7.5 * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Right 7.5 inches",this);
    }

    public void grabSkyStone() {

        Billy.angleUnWrap();

//        Billy.stoneServoLeft.setPosition(0.15);
//        Billy.stoneServoRight.setPosition(0.15);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (0 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate to 0 degrees CCW",this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, (8 + extraFwdToBlock), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 8 inches",this);

        //grab skystone with gripper
//        Billy.stoneServoLeft.setPosition(0.5);
//        Billy.stoneServoRight.setPosition(0.5);

        sleep(250);

    }

    public void moveAcrossBridge() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CCW",this);

        Billy.angleUnWrap();// think about commenting

        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - Billy.robotHeading) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate to 90 degrees CCW",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,45.5 + extraFwd + cons.pHM.get("foundationExtraFwd").value, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 45.5+ inches",this);//was 48

    }

    public void placeStoneOnFoundation() {

        //pressAToContinue();
        //move jack to be above Foundation WAS 5
//        DeltaH = 5;
//        Billy.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Up 3 Inches",this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,4, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Forward 6 inches",this);//was 8 in grab skystone??

        //pressAToContinue();
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,(8 - (foundationPosChange/13)), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 8 inches",this);
        //move slide OUT more
//        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
//        sleep(400);// 0.400 of a second
//        Billy.slide.setPower(0);
//
//        //pressAToContinue();
//        //Place stone with gripper
//        Billy.stoneServoLeft.setPosition(0.15);
//        Billy.stoneServoRight.setPosition(0.15);
//        sleep(250);
//
//        Billy.stoneServoLeft.setPosition(0.9);
//        Billy.stoneServoRight.setPosition(0.9);
//        sleep(200);
//
//        //pressAToContinue();
//        //move slide IN
//        Billy.slide.setPower(cons.pHM.get("slidePowerLimit").value);
//        sleep(400);// 0.400 of a second
//        Billy.slide.setPower(0);

        //pressAToContinue();

    }

    public void bridgeCrossSkyStone() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,(-4 - insideOutside), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 4-24 inches",this);
        // make shorter for OUTSIDE

        moveAcrossBridge();

        //pressAToContinue();
        Billy.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Up 3 Inches",this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft,((insideOutside + foundationPosChange) * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Sideways 0-50ish inches",this);

        Billy.angleUnWrap();

        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - (Billy.robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate to -90 degrees CCW",this);

        placeStoneOnFoundation();
    }

    public void bridgeCrossSkyStoneF() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,(-4 - insideOutside), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 4-24 inches",this);
        // make shorter for OUTSIDE

        moveAcrossBridge();

        //pressAToContinue();
        Billy.moveJack(5, cons.pHM.get("jackPowerLimit").value,"Jack Up 5 Inches",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft,((insideOutside + foundationPosChange) * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Sideways 0-50ish inches",this);

    }

    public void grabAndRotateFoundation() {

        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
        //pressAToContinue();

//        Billy.servoFoundationL.setPosition(0.10);
//        Billy.servoFoundationR.setPosition(0.90);

        //pressAToContinue();
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - Billy.robotHeading) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate to 90 degrees CCW",this);
        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate, (-5 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate -5 degrees CCW",this);

        //pressAToContinue();
        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,6, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 6 inches",this);// was 4

        //pressAToContinue();
        // grab foundation with servos
//        Billy.servoFoundationL.setPosition(0.80);
//        Billy.servoFoundationR.setPosition(0.20);

        sleep(250);

        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
        //pressAToContinue();
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-80 * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 80 degrees CCW",this);

        Billy.driveRotateIMU(-134 * sideColor, cons.pHM.get("rotatePowerLimit").value, "Rotate to 134 degrees CCW using the IMU", this);// was -130 needed a lil bit more
    }

    public void straightToCorner() {

        //pressAToContinue();
        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,20, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 20 inches",this);

        //pressAToContinue();
        Billy.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);

        //move slide OUT more
//        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
//        sleep(600);// 0.600 of a second
//        Billy.slide.setPower(0);

        //pressAToContinue();
        //Place stone with gripper
//        Billy.stoneServoLeft.setPosition(0.15);
//        Billy.stoneServoRight.setPosition(0.15);
//
//        sleep(200);
//        // Release foundation with servos
//        Billy.servoFoundationL.setPosition(0.10);
//        Billy.servoFoundationR.setPosition(0.90);
    }

    public void backSkyStoneAndFoundation() {

        //pressAToContinue();
        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-15, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Backwards 15 inches",this);//was -20

        //Stow gripper
//        Billy.stoneServoLeft.setPosition(1);
//        Billy.stoneServoRight.setPosition(1);
//        sleep(200);
//
//        //pressAToContinue();
//        //move slide IN
//        Billy.slide.setPower(cons.pHM.get("slidePowerLimit").value);
//        sleep(600);// 0.600 of a second
//        Billy.slide.setPower(0);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate, 50 * sideColor, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 45 degrees CW", this);
    }

    public void parkSkyStoneF() {

        //pressAToContinue();
        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft,((-insideOutside - foundationPosChange) * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Sideways 0-50ish inches",this);

        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
        //pressAToContinue();


//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - (Billy.robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate to -90 degrees CCW",this);

        //pressAToContinue();
        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-13, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Backward 13 inches",this);

        //pressAToContinue();
        // move jack down
        Billy.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-11, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Backward 11 inches",this);

    }

    public void parkSkyStone() {

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-(8 - (foundationPosChange/13)), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Backward  inches",this);

        //pressAToContinue();

        Billy.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft,((-(-4 + insideOutside + foundationPosChange)) * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Right 16 inches",this);

        Billy.angleUnWrap();

        //pressAToContinue();

//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 - (Billy.robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate to -90 degrees CCW",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-20, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Back 20 inches",this);

    }

    public void crossDropStonePark() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-4, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 4 inches",this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 90 degrees CCW",this);

        Billy.angleUnWrap();// think about commenting

        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);

//        if(sideColor == 1) {
//
//            Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
//
//        }

//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,cons.pHM.get("dropStoneForward").value + extraFwd, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 35+ inches",this);//was 48

        //Drop stone with gripper
//        Billy.stoneServoLeft.setPosition(0.15);
//        Billy.stoneServoRight.setPosition(0.15);
        sleep(250);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-6, cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 6 inches",this);//was 4

    }

    public void crossDropStoneFor2Old() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-4, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 4 inches",this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CCW",this);

        Billy.angleUnWrap();// think about commenting

//        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);

//        if(sideColor == 1) {
//
//            Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
//
//        }

//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, cons.pHM.get("dropStoneForward").value + 35 + extraFwd, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 35+ inches",this);//was 48

        //Drop stone with gripper
//        Billy.stoneServoLeft.setPosition(0.15);
//        Billy.stoneServoRight.setPosition(0.15);
        sleep(250);

    }

    public void getSecondStoneOld() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-cons.pHM.get("dropStoneForward").value -35 - extraFwd - 24, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Backward to second stone",this);//was 48

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CW",this);

        grabSkyStone();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-4, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 4 inches",this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value,"Rotate 90 degrees CCW",this);

//        Billy.angleUnWrap();// think about commenting
//
//        telemetry.addData("robotHeading: (%.2f)", Billy.robotHeading);
//
//        if(sideColor == 1) {
//
//            Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-90 - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
//
//        }
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,( (-90 * sideColor) - Billy.robotHeading), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);

        //pressAToContinue();

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,cons.pHM.get("dropStoneForward").value + 35 + extraFwd + 24, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward with second stone",this);//was 48

        //Drop stone with gripper
//        Billy.stoneServoLeft.setPosition(0.15);
//        Billy.stoneServoRight.setPosition(0.15);
        sleep(250);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack,-6, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Back 6 inches",this);//was 4

    }

    public void goToStone() {
        telemetry.addData("Stone sideways distance","%.2f",(stoneSideways * sideColor));
        telemetry.addData("Stone sideways variable","%.2f",stoneSideways);
        telemetry.addData("CONSTANTS adj Right variable","%.2f",cons.adjRight);
        telemetry.addData("sideColor Variable","%.2f",sideColor);

//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, stoneSideways * sideColor, 0, "Sideways to face stone",this);

//        pressAToContinue();

        Billy.IMUDriveRotate(-90 * sideColor, "Rotate 90 degrees CCW",this);

//        pressAToContinue();

    }

    public void takeStone1() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, sideGrabStone * sideColor, -90 * sideColor, "Right 8 inches",this);

//        pressAToContinue();

        //Move commented below is for angle adjustment
//        Billy.angleUnWrap();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.Rotate, ((-90 - Billy.robotHeading) * sideColor), 0, "Rotate 90 degrees CW",this);

//        pressAToContinue();
        //grab skystone with servo arm
        Billy.stoneServoArm.setPosition(stoneArmDown);
        sleep(500);
//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (-sideGrabStone / 2) * sideColor, -90 * sideColor, "Left 8 inches",this);

//        pressAToContinue();

        Billy.IMUDriveRotate(-180 * sideColor, "Rotate 90 degrees CCW",this);

//        pressAToContinue();

//        Billy.angleUnWrap();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.Rotate, ((-180 - Billy.robotHeading) * sideColor), 0, "Rotate 90 degrees CW",this);
//
//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (52 + stoneSideways) * sideColor, -180 * sideColor, "Right 52+ inches",this);
                                                                            // was 48
//        pressAToContinue();

        //release skystone with servo arm
        Billy.stoneServoArm.setPosition(stoneArmUp);
        sleep(500);
//        pressAToContinue();

    }

    public void getStone2() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,  ( -52 - stoneSideways - 24 ) * sideColor, -180 * sideColor, "Left 45+ inches",this);
                                                                                // was 48
//        pressAToContinue();

        Billy.IMUDriveRotate( -90 * sideColor, "Rotate 90 degrees CW",this);

//        pressAToContinue();

    }

    public void takeStone2() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,(sideGrabStone/2) * sideColor, -90 * sideColor, "Right 8 inches",this);

//        pressAToContinue();

//        Billy.angleUnWrap();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.Rotate, ((-90 - Billy.robotHeading) * sideColor), 0, "Rotate 90 degrees CW",this);

//        pressAToContinue();

        //grab skystone with servo arm
        Billy.stoneServoArm.setPosition(stoneArmDown);
        sleep(500);
//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (-sideGrabStone/2) * sideColor, -90 * sideColor, "Left 8 inches",this);

//        pressAToContinue();

        Billy.IMUDriveRotate( -180 * sideColor, "Rotate 90 degrees CCW",this);

//        pressAToContinue();

//        Billy.angleUnWrap();
//        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.Rotate, ((-180 - Billy.robotHeading) * sideColor), 0, "Rotate 90 degrees CW",this);
//
//        pressAToContinue();

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft, (52 + stoneSideways + 24) * sideColor, -180 * sideColor, "Right with stone 2",this);
                                                                            // was 48
//        pressAToContinue();
        //release skystone with servo arm
        Billy.stoneServoArm.setPosition(stoneArmUp);
        sleep(500);

//        pressAToContinue();

    }

    public void twoStonePark() {

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-18 * sideColor, -180 * sideColor, "Left 10 inches",this);

    }

    public void findSkyStone() {
        boolean skystoneFound = false;
        int looped = 0;

        while(looped < 2 && opModeIsActive()) {
            skystoneFound = vuforiaStoneIdentifyExit();

            if(skystoneFound) {

                telemetry.addLine("SkyStone Found");
                telemetry.update();

//                grabSkyStone();

                looped = 100;
            }
            else {

                telemetry.addLine("Next Stone");
                telemetry.update();

                nextStone();
                looped +=1;
                extraFwd += 8;
                extraFwdToBlock += 1;
            }
        }
        if(!skystoneFound) {

            telemetry.addLine("Third Stone");
            telemetry.update();

//            grabSkyStone();

        }

    }


    public void grabFoundation() {

//        Billy.servoFoundationL.setPosition(0.10);
//        Billy.servoFoundationR.setPosition(0.90);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, 30, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 32 inches to Foundation", this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, 2, (cons.pHM.get("drivePowerLimit").value / 2), cons.pHM.get("drivePowerMinimum").value,"Forward 2 inches to Foundation", this);

        // grab foundation with servos
//        Billy.servoFoundationL.setPosition(0.80);
//        Billy.servoFoundationR.setPosition(0.20);

        sleep(250);

    }

    public void pullFoundation() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, -29, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Backward 31 inches with Foundation", this);

//        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, -2, (cons.pHM.get("drivePowerLimit").value / 2), "Backward 2 inches with Foundation", this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(-20 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 20 degrees CCW",this);//15 was too close

        // release foundation from gripper
//        Billy.servoFoundationL.setPosition(0.10);
//        Billy.servoFoundationR.setPosition(0.90);
    }

    public void aroundFoundation() {

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, -2, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 2 inches away from Foundation", this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft, (26 * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Left 26 inches around Foundation", this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, 18, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Backwards 18 inches around Foundation", this);

    }

    public void pushFoundation() {
        // touching foundation to push it

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft, (-foundationPush * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Right 8 inches pushing Foundation", this);

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft, (foundationPush * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Left 8 inches away from Foundation", this);

    }

//    public void awayFromFoundationOutside() {
//
//        //pressAToContinue();
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, 18, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 20 inches towards wall", this);
//
//        //pressAToContinue();
//
////        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate, (-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 90 Degrees CCW", this);
////
////        //pressAToContinue();
//
//        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft, -16 * sideColor, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Left 16 inches to park", this);
//
//    }

    public void awayFromFoundation() {

//        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft, (45 * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Right 45 inches to park", this);
//        //was 50

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft, (25 * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Right 25 inches", this);//was 21

        Billy.driveGeneralPower(HardwareBilly.moveDirection.Rotate,(20 * sideColor), cons.pHM.get("rotatePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Rotate 20 degrees CW",this);//15 was too close

        Billy.driveGeneralPower(HardwareBilly.moveDirection.FwdBack, -10, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Backward 14 inches to wall", this);//was 16

        Billy.driveGeneralPower(HardwareBilly.moveDirection.RightLeft, (20 * sideColor), cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Right 20 inches", this);//was 24

    }

}