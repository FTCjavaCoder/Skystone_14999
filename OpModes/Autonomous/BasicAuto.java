package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import Skystone_14999.DriveMotion.DriveMethods;
import Skystone_14999.OpModes.BasicOpMode;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous(name="BasicAuto", group="Autonomous")
@Disabled
public class BasicAuto extends BasicOpMode {

    public DriveMethods drv = new DriveMethods();// call with drv.(drive method: driveFwdRev etc.)

    // motor position must be integer number of counts
    public int forwardPosition = 0;//Target position in forward direction for drive motors
    public int rightPosition = 0;//Target position in right direction for drive motors
    public int clockwisePosition = 0;//Target position in clockwise direction for drive motors
    public int slideDistance = 0;

    public int flStart;
    public int frStart;
    public int blStart;
    public int brStart;

    public double extraFwd = 0;
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
    public String colorFound = "No";

    public ElapsedTime runtime = new ElapsedTime(); //create a counter for elapsed time

    public double testDistFwd = 24;
    public double testAngleClock = 90;
    public double testDistRight = 24;

    public double sampleForward1 = 4;
    public double sampleRotate1;
    public double sampleForward2;
    public double markerRotate1;
    public double markerForward1;

    public double dtc1Rotate;
    public double dtc2Right;
    public double dtc3Forward;

    public double ctd1Back;
    public double ctd2Rotate;
    public double ctd3Forward;
    public double ctd4Rotate;
    public double ctd5Forward;
    public double btc1Back; // -60

    public double escapeFwd;
    public double escapeSideWays;
    public double parkRotate1;
    public double parkForward1;

    public String mineralGold = "Unknown";

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

        Billy.init(hardwareMap);

        //Motor configuration, recommend Not Changing - Set all motors to forward direction, positive = clockwise when viewed from shaft side
        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.jackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.jackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.jackRight.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.jackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.jackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.jackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.jackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Billy.frontLeft.setTargetPosition(0);
        Billy.frontRight.setTargetPosition(0);
        Billy.backLeft.setTargetPosition(0);
        Billy.backRight.setTargetPosition(0);
        Billy.jackLeft.setTargetPosition(0);
        Billy.jackRight.setTargetPosition(0);

        //Set all motors to position mode (assumes that all motors have encoders on them)
        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.jackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.jackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);
        Billy.jackLeft.setPower(0);
        Billy.jackRight.setPower(0);
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

        // Find Auto Option
        readOrWriteHashMap();

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)", forwardPosition, rightPosition, clockwisePosition);
        telemetry.update();//Update telemetry to update display

    }// sets: RUN_TO_POSITION, ZeroPowerBehaviour.FLOAT, and 0 power & targetPos

    public void vuforiaStoneIdentifyLoop() {

        targetsSkyStone.activate();
        while (!isStopRequested()) {

            // check all the trackable targets to see which one (if any) is visible.
            targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;
                    if (trackable.getName() == "Stone Target") {

                        telemetry.addLine("\n If statement successful for Stone Target \n");
                    }

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

    public void fwdToStone() {

        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
        sleep(400);// 0.400 of a second
        Billy.slide.setPower(0);

        Billy.stoneServoLeft.setPosition(blockCamera);
        Billy.stoneServoRight.setPosition(blockCamera);

        telemetry.addData("robotHeading: (%.2f)", robotHeading);
        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,22, cons.pHM.get("drivePowerLimit").value, "Forward 22 inches",this);

    }

    public void nextStone() {

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft,7.5, (cons.pHM.get("drivePowerLimit").value), "Right 7.5 inches",this);
    }

    public void grabSkyStone() {

        angleUnWrap();

        Billy.stoneServoLeft.setPosition(0.15);
        Billy.stoneServoRight.setPosition(0.15);

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,( (0 - robotHeading) * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate to 0 degrees CCW",this);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, (8 + extraFwdToBlock), cons.pHM.get("drivePowerLimit").value, "Forward 8 inches",this);

        //grab skystone with gripper
        Billy.stoneServoLeft.setPosition(0.5);
        Billy.stoneServoRight.setPosition(0.5);

        sleep(250);

    }

    public void moveAcrossBridge() {

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate 90 degrees CCW",this);

        angleUnWrap();// think about commenting

        telemetry.addData("robotHeading: (%.2f)", robotHeading);
        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,( (-90 - robotHeading) * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,45.5 + extraFwd + cons.pHM.get("foundationExtraFwd").value, cons.pHM.get("drivePowerLimit").value, "Forward 45.5+ inches",this);//was 48

    }

    public void placeStoneOnFoundation() {

        //pressAToContinue();
        //move jack to be above Foundation WAS 5
//        DeltaH = 5;
//        drv.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Up 3 Inches",this);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,4, cons.pHM.get("drivePowerLimit").value, "Forward 6 inches",this);//was 8 in grab skystone??

        //pressAToContinue();
//        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,(8 - (foundationPosChange/13)), cons.pHM.get("drivePowerLimit").value, "Forward 8 inches",this);
        //move slide OUT more
        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
        sleep(400);// 0.400 of a second
        Billy.slide.setPower(0);

        //pressAToContinue();
        //Place stone with gripper
        Billy.stoneServoLeft.setPosition(0.15);
        Billy.stoneServoRight.setPosition(0.15);
        sleep(250);

        Billy.stoneServoLeft.setPosition(0.9);
        Billy.stoneServoRight.setPosition(0.9);
        sleep(200);

        //pressAToContinue();
        //move slide IN
        Billy.slide.setPower(cons.pHM.get("slidePowerLimit").value);
        sleep(400);// 0.400 of a second
        Billy.slide.setPower(0);

        //pressAToContinue();

    }

    public void bridgeCrossSkyStone() {

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,(-4 - insideOutside), cons.pHM.get("drivePowerLimit").value, "Back 4-24 inches",this);
        // make shorter for OUTSIDE

        moveAcrossBridge();

        //pressAToContinue();
        drv.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Up 3 Inches",this);

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft,((insideOutside + foundationPosChange) * sideColor), cons.pHM.get("drivePowerLimit").value, "Sideways 0-50ish inches",this);

        angleUnWrap();

        telemetry.addData("robotHeading: (%.2f)", robotHeading);
        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,( (-90 - (robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate to -90 degrees CCW",this);

        placeStoneOnFoundation();
    }

    public void bridgeCrossSkyStoneF() {

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,(-4 - insideOutside), cons.pHM.get("drivePowerLimit").value, "Back 4-24 inches",this);
        // make shorter for OUTSIDE

        moveAcrossBridge();

        //pressAToContinue();
        drv.moveJack(5, cons.pHM.get("jackPowerLimit").value,"Jack Up 5 Inches",this);

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft,((insideOutside + foundationPosChange) * sideColor), cons.pHM.get("drivePowerLimit").value, "Sideways 0-50ish inches",this);

    }

    public void grabAndRotateFoundation() {

        telemetry.addData("robotHeading: (%.2f)", robotHeading);
        //pressAToContinue();

        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        //pressAToContinue();
//        drv.driveGeneral(DriveMethods.moveDirection.Rotate,( (-90 - robotHeading) * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);
        drv.driveGeneral(DriveMethods.moveDirection.Rotate, (-5 * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate -5 degrees CCW",this);

        //pressAToContinue();
        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,6, cons.pHM.get("drivePowerLimit").value, "Forward 6 inches",this);// was 4

        //pressAToContinue();
        // grab foundation with servos
        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);

        sleep(250);

        telemetry.addData("robotHeading: (%.2f)", robotHeading);
        //pressAToContinue();
//        drv.driveGeneral(DriveMethods.moveDirection.Rotate,(-80 * sideColor), cons.pHM.get("drivePowerLimit").value, "Rotate 80 degrees CCW",this);

        drv.driveRotateIMU(-134, cons.pHM.get("rotatePowerLimit").value, "Rotate to 134 degrees CCW using the IMU", this);// was -130 needed a lil bit more
    }

    public void straightToCorner() {

        //pressAToContinue();
        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,20, cons.pHM.get("drivePowerLimit").value, "Forward 20 inches",this);

        //pressAToContinue();
        drv.moveJack(3, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);

        //move slide OUT more
        Billy.slide.setPower(-cons.pHM.get("slidePowerLimit").value);
        sleep(600);// 0.600 of a second
        Billy.slide.setPower(0);

        //pressAToContinue();
        //Place stone with gripper
        Billy.stoneServoLeft.setPosition(0.15);
        Billy.stoneServoRight.setPosition(0.15);

        sleep(200);
        // Release foundation with servos
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);
    }

    public void backSkyStoneAndFoundation() {

        //pressAToContinue();
        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,-15, cons.pHM.get("drivePowerLimit").value, "Backwards 15 inches",this);//was -20

        //Stow gripper
        Billy.stoneServoLeft.setPosition(1);
        Billy.stoneServoRight.setPosition(1);
        sleep(200);

        //pressAToContinue();
        //move slide IN
        Billy.slide.setPower(cons.pHM.get("slidePowerLimit").value);
        sleep(600);// 0.600 of a second
        Billy.slide.setPower(0);

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.Rotate, 50, cons.pHM.get("rotatePowerLimit").value, "Rotate 45 degrees CW", this);
    }

    public void parkSkyStoneF() {

        //pressAToContinue();
        drv.driveGeneral(DriveMethods.moveDirection.RightLeft,((-insideOutside - foundationPosChange) * sideColor), cons.pHM.get("drivePowerLimit").value, "Sideways 0-50ish inches",this);

        telemetry.addData("robotHeading: (%.2f)", robotHeading);
        //pressAToContinue();


//        drv.driveGeneral(DriveMethods.moveDirection.Rotate,( (-90 - (robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate to -90 degrees CCW",this);

        //pressAToContinue();
        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,-13, cons.pHM.get("drivePowerLimit").value, "Backward 13 inches",this);

        //pressAToContinue();
        // move jack down
        drv.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,-11, cons.pHM.get("drivePowerLimit").value, "Backward 11 inches",this);

    }

    public void parkSkyStone() {

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,-(8 - (foundationPosChange/13)), cons.pHM.get("drivePowerLimit").value, "Backward  inches",this);

        //pressAToContinue();

        drv.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Down 2 Inches",this);

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft,((-(-4 + insideOutside + foundationPosChange)) * sideColor), cons.pHM.get("drivePowerLimit").value, "Right 16 inches",this);

        angleUnWrap();

        //pressAToContinue();

//        drv.driveGeneral(DriveMethods.moveDirection.Rotate,( (-90 - (robotHeading)) * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate to -90 degrees CCW",this);

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,-20, cons.pHM.get("drivePowerLimit").value, "Back 20 inches",this);

    }

    public void crossDropStonePark() {

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,-4, cons.pHM.get("drivePowerLimit").value, "Back 4 inches",this);

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,(-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate 90 degrees CCW",this);

        angleUnWrap();// think about commenting

        telemetry.addData("robotHeading: (%.2f)", robotHeading);
        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,( (-90 - robotHeading) * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate to 90 degrees CCW",this);

        //pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,cons.pHM.get("dropStoneForward").value + extraFwd, cons.pHM.get("drivePowerLimit").value, "Forward 35+ inches",this);//was 48

        //Drop stone with gripper
        Billy.stoneServoLeft.setPosition(0.15);
        Billy.stoneServoRight.setPosition(0.15);
        sleep(250);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack,-6, cons.pHM.get("drivePowerLimit").value, "Back 6 inches",this);//was 4

    }

    public void findSkyStone() {
        boolean skystoneFound = false;
        int looped = 0;

        while(looped < 2 && opModeIsActive()) {
            skystoneFound = vuforiaStoneIdentifyExit();

            if(skystoneFound) {

                telemetry.addLine("SkyStone Found");
                telemetry.update();

                grabSkyStone();

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

            grabSkyStone();

        }

    }


    public void grabFoundation() {

        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, 30, cons.pHM.get("drivePowerLimit").value, "Forward 32 inches to Foundation", this);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, 2, (cons.pHM.get("drivePowerLimit").value / 2), "Forward 2 inches to Foundation", this);

        // grab foundation with servos
        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);

        sleep(250);

    }

    public void pullFoundation() {

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, -29, cons.pHM.get("drivePowerLimit").value, "Backward 31 inches with Foundation", this);

//        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, -2, (cons.pHM.get("drivePowerLimit").value / 2), "Backward 2 inches with Foundation", this);

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,(-10 * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate 10 degrees CCW",this);//was 20

        // release foundation from gripper
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);
    }

    public void aroundFoundation() {

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, 2, cons.pHM.get("drivePowerLimit").value, "Forward 2 inches away from Foundation", this);

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, (-26 * sideColor), cons.pHM.get("drivePowerLimit").value, "Left 26 inches around Foundation", this);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, -18, cons.pHM.get("drivePowerLimit").value, "Backwards 18 inches around Foundation", this);

    }

    public void pushFoundation() {
        // touching foundation to push it

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, (foundationPush * sideColor), cons.pHM.get("drivePowerLimit").value, "Right 8 inches pushing Foundation", this);

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, (-foundationPush * sideColor), cons.pHM.get("drivePowerLimit").value, "Left 8 inches away from Foundation", this);

    }

//    public void awayFromFoundationOutside() {
//
//        //pressAToContinue();
//
//        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, 18, cons.pHM.get("drivePowerLimit").value, "Forward 20 inches towards wall", this);
//
//        //pressAToContinue();
//
////        drv.driveGeneral(DriveMethods.moveDirection.Rotate, (-90 * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate 90 Degrees CCW", this);
////
////        //pressAToContinue();
//
//        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, -16 * sideColor, cons.pHM.get("drivePowerLimit").value, "Left 16 inches to park", this);
//
//    }

    public void awayFromFoundation() {

//        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, (45 * sideColor), cons.pHM.get("drivePowerLimit").value, "Right 45 inches to park", this);
//        //was 50

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, (25 * sideColor), cons.pHM.get("drivePowerLimit").value, "Right 25 inches", this);//was 21

        drv.driveGeneral(DriveMethods.moveDirection.Rotate,(10 * sideColor), cons.pHM.get("rotatePowerLimit").value, "Rotate 10 degrees CW",this);//was 20

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, -18, cons.pHM.get("drivePowerLimit").value, "Backward 18 inches to wall", this);

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, (20 * sideColor), cons.pHM.get("drivePowerLimit").value, "Right 20 inches", this);//was 24

    }

}