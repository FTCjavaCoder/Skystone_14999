package Skystone_14999.OpModes.Autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import Skystone_14999.DriveMotion.DriveMethods;
import Skystone_14999.HarwareConfig.HardwareBilly;
import Skystone_14999.I_Parameters.Parameters;

@Autonomous(name="BasicAuto", group="Autonomous")
@Disabled
public class BasicAuto extends LinearOpMode {

    public HardwareBilly Billy = new HardwareBilly();// call using Billy.(for hardware or angle unwrap method)
    public Parameters prm = new Parameters();// call using prm.(constant DRIVE_POWER_LIMIT etc.)
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

    //Define all double variables
    public double start = 0;//timer variable to use for setting waits in the code
    public float hsvValues[] = {0F, 0F, 0F};
    public double offset = 0;
    public double priorAngle = 0;
    public double robotHeading = 0;
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

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    public TFObjectDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {


    }

    public void initialize() {

        Billy.init(hardwareMap);

        //Set Arm motor configuration - Do Not Edit
        Billy.landingSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.landingSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.landingSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.landingSlide.setPower(0);
        Billy.landingSlide.setTargetPosition(0);

        //Motor configuration, recommend Not Changing - Set all motors to forward direction, positive = clockwise when viewed from shaft side
        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Set all motors to position mode (assumes that all motors have encoders on them)
        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Billy.frontLeft.setTargetPosition(0);
        Billy.frontRight.setTargetPosition(0);
        Billy.backLeft.setTargetPosition(0);
        Billy.backRight.setTargetPosition(0);

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);

        Billy.slideExtend.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.slideExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slideExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.slideExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.slideExtend.setPower(prm.SLIDE_EXTEND_POWER_LIMIT);
        Billy.slideExtend.setTargetPosition(0);

        Billy.slideRotate.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.slideRotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slideRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.slideRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.slideRotate.setPower(prm.SLIDE_ROTATE_POWER_LIMIT);
        Billy.slideRotate.setTargetPosition(0);

        Billy.servoMarker.setPosition(prm.stowServoMarker);

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)", forwardPosition, rightPosition, clockwisePosition);
        telemetry.update();//Update telemetry to update display

    }// sets: RUN_TO_POSITION, ZeroPowerBehaviour.FLOAT, and 0 power & targetPos

    public void initializeCrater() {

        Billy.init(hardwareMap);

        //Set Arm motor configuration - Do Not Edit
        Billy.landingSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.landingSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.landingSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.landingSlide.setPower(0);
        Billy.landingSlide.setTargetPosition(0);

        //Motor configuration, recommend Not Changing - Set all motors to forward direction, positive = clockwise when viewed from shaft side
        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Set all motors to position mode (assumes that all motors have encoders on them)
        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Billy.frontLeft.setTargetPosition(0);
        Billy.frontRight.setTargetPosition(0);
        Billy.backLeft.setTargetPosition(0);
        Billy.backRight.setTargetPosition(0);

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);

        Billy.slideExtend.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.slideExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slideExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.slideExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.slideExtend.setPower(prm.SLIDE_EXTEND_POWER_LIMIT);
        Billy.slideExtend.setTargetPosition(0);

        Billy.slideRotate.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.slideRotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slideRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.slideRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Billy.slideRotate.setPower(prm.SLIDE_ROTATE_POWER_LIMIT);
        Billy.slideRotate.setTargetPosition(0);

        Billy.servoMarker.setPosition(prm.stowServoMarker);

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfodCrater();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)", forwardPosition, rightPosition, clockwisePosition);
        telemetry.update();//Update telemetry to update display

    }//TFOD INIT WITHOUT SILVER\\ sets: RUN_TO_POSITION, ZeroPowerBehaviour.FLOAT, and 0 power & targetPos

    public void hangAngle() {

        runtime.reset();

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU
        double rollOffset = Billy.angles.secondAngle;

        start = runtime.time();
        while(!isStarted()){

            Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("Roll angle: ","( %.2f )", Billy.angles.secondAngle - rollOffset);
            //telemetry.addData("Pitch angle: ","( %.2f )", angles.thirdAngle);
            telemetry.addData(">", "Press Play to start"); //display this on screen
            telemetry.update();//Update telemetry to update display
        }

    }// for roll angle of robot when hanging

    public void landing() {

        // moving lead screw slide
        slideDistance = (int) Math.round(7 * prm.SLIDE_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.slideMove(slideDistance, prm.SLIDE_POWER_LIMIT, "Landing", this);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set motor power limits - for "Run_to_Position"  sets the maximum speed
        Billy.frontLeft.setPower(prm.DRIVE_POWER_LIMIT);
        Billy.frontRight.setPower(prm.DRIVE_POWER_LIMIT);
        Billy.backLeft.setPower(prm.DRIVE_POWER_LIMIT);
        Billy.backRight.setPower(prm.DRIVE_POWER_LIMIT);

        // getting off hook and then getting to sampling starting point
        rightPosition = (int) Math.round((-2.75 * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS);
        drv.driveRightLeft(rightPosition, 0.25, "Go 2.75 inches left",this);

        angleUnWrap();

        clockwisePosition = (int) Math.round((-robotHeading * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Rotating IMU value deg. counterclockwise",this);

    }// moves 7"

    public void landingIMU() {

        // moving lead screw slide
        slideDistance = (int) Math.round(7 * prm.SLIDE_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.slideMove(slideDistance, prm.SLIDE_POWER_LIMIT, "Landing", this);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set motor power limits - for "Run_to_Position"  sets the maximum speed
        Billy.frontLeft.setPower(prm.DRIVE_POWER_LIMIT);
        Billy.frontRight.setPower(prm.DRIVE_POWER_LIMIT);
        Billy.backLeft.setPower(prm.DRIVE_POWER_LIMIT);
        Billy.backRight.setPower(prm.DRIVE_POWER_LIMIT);

        // getting off hook and then getting to sampling starting point
        rightPosition = (int) Math.round((-2.75 * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS);
        drv.driveRightLeftIMU(rightPosition, 0.25, 0,"Go 2.75 inches left",this);

        angleUnWrap();

        drv.driveRotateIMU(-robotHeading, prm.ROTATE_POWER_LIMIT, "Rotating IMU value deg. counterclockwise",this);

    }// moves 7"

    public void angleUnWrap() {

        double deltaAngle;

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU
        deltaAngle = -(Billy.angles.firstAngle - priorAngle);// Determine how much the angle has changed since we last checked teh angle
        if (deltaAngle > 180) {//This is IF/THEN for the unwrap routine
            robotHeading += deltaAngle - 360;//Decrease angle for negative direction //rotation
        } else if (deltaAngle < -180) {
            robotHeading += deltaAngle + 360;//increase angle for positive direction //rotation 
        } else {
            robotHeading += deltaAngle;//No wrap happened, don't add any extra rotation
        }
        priorAngle = Billy.angles.firstAngle;//Update the latest measurement to be //priorAngle for the next time we call the method

    }

    public void pressAToContinue() {

        while (!gamepad1.a && opModeIsActive()) {
            telemetry.addLine("Press A to continue");
            telemetry.update();

            idle();
        }

    }

    public double adjustVariables(double V1, double increment, String Info) {

        while (!gamepad1.x && opModeIsActive()) {
            telemetry.addLine("Use left/right bumper to decrease/increase, X to continue");
            telemetry.addData("\r", "%s", Info);
            telemetry.addData("\rValue:", "%.2f", V1);
            telemetry.update();

            if (gamepad1.right_bumper) {

                V1 += increment;
                sleep(300);
            }

            if (gamepad1.left_bumper) {

                V1 -= increment;
                sleep(300);
            }

            idle();
        }
        sleep(500);

        return V1;

    }

    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = prm.VUFORIA_KEY;
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(prm.TFOD_MODEL_ASSET, prm.LABEL_GOLD_MINERAL, prm.LABEL_SILVER_MINERAL);
    }

    public void initTfodCrater() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(prm.TFOD_MODEL_ASSET, prm.LABEL_GOLD_MINERAL);
    }

    public String samplePosDepot() {

        String cubePos = "Unknown";
        boolean goldFound = false;

        /** Activate Tensor Flow Object Detection. */
        if (tfod != null) {
            tfod.activate();
        }

        while ( !goldFound && (opModeIsActive()) ) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());


                    if (updatedRecognitions.size() == 2) {

                        int goldMineralX = 0;
                        int silverMineral1X = 0;
                        int silverMineral2X = 0;
                        boolean seeGold = false;

                        for (Recognition recognition : updatedRecognitions) {

                            if (recognition.getLabel().equals(prm.LABEL_GOLD_MINERAL)) {
                                seeGold = true;
                                goldMineralX = (int) (recognition.getLeft() + recognition.getRight()) / 2;
                            } else if (silverMineral1X == 0) {
                                silverMineral1X = (int) (recognition.getLeft() + recognition.getRight()) / 2;
                            } else if (silverMineral2X == 0) {

                                silverMineral2X = (int) (recognition.getLeft() + recognition.getRight()) / 2;
                            }

                            if (!seeGold) {

                                cubePos = "Left";
                                telemetry.addData("# Object Detected", updatedRecognitions.size());
                                telemetry.addData("Gold Mineral Position:", "Left");
                                telemetry.addData("Gold Mineral X:", goldMineralX);
                                telemetry.addData("Silver Mineral 1 X:", silverMineral1X);
                                telemetry.update();
                                goldFound = true;

                                mineralGold = "Left";

                                sampleRotate1 = -25;
                                sampleForward2 = 27;
                                markerRotate1 = 50;
                                markerForward1 = 24;
                                dtc1Rotate = -160;
                                dtc2Right = 5;
                                dtc3Forward = 67;// was 62
                                escapeFwd = -14;
                                escapeSideWays = 0;
//                                parkRotate1 = 25;
//                                parkForward1 = 5;
//                                ctd1Back = -18;
//                                ctd2Rotate = -65;
//                                ctd3Forward = 43;
//                                ctd4Rotate = -135; // angle to face
//                                ctd5Forward = 30;
//                                btc1Back = -65;

                            } else if (goldMineralX < silverMineral1X) {

                                cubePos = "Center";
                                telemetry.addData("# Object Detected", updatedRecognitions.size());
                                telemetry.addData("Gold Mineral Position:", "Center");
                                telemetry.addData("Gold Mineral X:", goldMineralX);
                                telemetry.addData("Silver Mineral 1 X:", silverMineral1X);
                                telemetry.update();
                                goldFound = true;

                                mineralGold = "Center";

                                sampleRotate1 = 10;
                                sampleForward2 = 24;
                                markerRotate1 = -10;
                                markerForward1 = 20;
                                dtc1Rotate = -135;
                                dtc2Right = 12;
                                dtc3Forward = 65;
                                escapeFwd = -23;
                                escapeSideWays = 0;
//                                parkRotate1 = 0;
//                                parkForward1 = 0;
//                                ctd1Back = -15;
//                                ctd2Rotate = -100;
//                                ctd3Forward = 48;
//                                ctd4Rotate = -135; // angle to face
//                                ctd5Forward = 30;
//                                btc1Back = -60;

                            } else if (goldMineralX > silverMineral1X) {

                                cubePos = "Right";
                                telemetry.addData("# Object Detected", updatedRecognitions.size());
                                telemetry.addData("Gold Mineral Position:", "Right");
                                telemetry.addData("Gold Mineral X:", goldMineralX);
                                telemetry.addData("Silver Mineral 1 X:", silverMineral1X);
                                telemetry.update();
                                goldFound = true;

                                mineralGold = "Right";

                                sampleRotate1 = 35;
                                sampleForward2 = 30;
                                markerRotate1 = -55;
                                markerForward1 = 22;
                                dtc1Rotate = -115;// CCW 115 as first step || was -115
                                dtc2Right = -30;// left 30 as second step    || was 15
                                dtc3Forward = 65;// fwd 65 as third step    || was 50 and 68
                                escapeFwd = 0;
                                escapeSideWays = -26;
//                                parkRotate1 = -35;
//                                parkForward1 = 5;
//                                ctd1Back = -20;
//                                ctd2Rotate = -125;
//                                ctd3Forward = 50;
//                                ctd4Rotate = -135; // angle to face
//                                ctd5Forward = 35;
//                                btc1Back = -65;
                            }

                        }

                        if (tfod != null) {
                            tfod.shutdown();
                        }
                    }
                }
            }
        }

        return cubePos;
    }

    public String samplePosCrater() {

        String cubePos = "Unknown";
        double objectPosition = -1;

        /** Activate Tensor Flow Object Detection. */
        if (tfod != null) {
            tfod.activate();
        }

        boolean goldFound = false;

        start = runtime.time();

        while ( ( opModeIsActive() ) && (!goldFound) && (runtime.time() - start < 2) ) {// used 3 seconds previously

            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.

                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

                if (updatedRecognitions != null) {

                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    telemetry.update();

                    for (Recognition recognition : updatedRecognitions) {

                        if (recognition.getTop() > 450) {

                            objectPosition = (int) (recognition.getLeft() + recognition.getRight()) / 2;
                            goldFound = true;

                        }

                        telemetry.addData("object Center", " %d", (int) (recognition.getLeft() + recognition.getRight()) / 2);
                        telemetry.addData("object Top", " %d", (int) recognition.getTop());
                        telemetry.addData("Gold Found", goldFound);
                        telemetry.update();


                    }
                }
            }
        }

        if (objectPosition > 250 && objectPosition < 650) {

            cubePos = "Center";
            telemetry.addLine("CUBE IS CENTER");
            telemetry.update();

            mineralGold = "Center";

            sampleRotate1 = 10;
            sampleForward2 = 24;
            markerRotate1 = -10;
            markerForward1 = 20;
//            dtc1Rotate = -135;
//            dtc2Right = 12;
//            dtc3Forward = 65;
            ctd1Back = -13;// was -14
            ctd2Rotate = -100;
            ctd3Forward = 51;// was 46 || 51 worked!!
            ctd4Rotate = -133; // angle to face
            ctd5Forward = 27;
            btc1Back = -62;
            escapeFwd = -23;
            escapeSideWays = 0;
            parkRotate1 = 0;
            parkForward1 = 0;

        } else if (objectPosition > 800 && objectPosition < 1250) {

            cubePos = "Right";
            telemetry.addLine("CUBE IS RIGHT");
            telemetry.update();

            mineralGold = "Right";

            sampleRotate1 = 35;
            sampleForward2 = 30;
            markerRotate1 = -55;
            markerForward1 = 22;
//            dtc1Rotate = -115;// CCW 115 as second step || was -115
//            dtc2Right = -30;// left 30 as first step    || was 15
//            dtc3Forward = 50;// fwd 50 as third step    || was 68
            ctd1Back = -14;// was 16"
            ctd2Rotate = -125;
            ctd3Forward = 58.5;// was 55.5"
            ctd4Rotate = -133; // angle to face || was -135
            ctd5Forward = 32;
            btc1Back = -67;// was -62
            escapeFwd = 0;
            escapeSideWays = -26;
            parkRotate1 = -35;
            parkForward1 = 5;

        } else if (objectPosition == -1) {

            cubePos = "Left";
            telemetry.addLine("CUBE IS LEFT");
            telemetry.update();

            mineralGold = "Left";

            sampleRotate1 = -25;
            sampleForward2 = 27;
            markerRotate1 = 50;
            markerForward1 = 24;
//            dtc1Rotate = -160;
//            dtc2Right = 5;
//            dtc3Forward = 62;
            ctd1Back = -16;
            ctd2Rotate = -65;
            ctd3Forward = 40;
            ctd4Rotate = -133; // angle to face
            ctd5Forward = 27;
            btc1Back = -62;
            escapeFwd = -14;
            escapeSideWays = 0;
            parkRotate1 = 25;
            parkForward1 = 5;
        }

        return cubePos;
    }

    public void scoreSamplingDepot() {

        String cubePos = samplePosDepot();

        telemetry.addData("Cube is","%s",cubePos);
        telemetry.update();

        forwardPosition = (int) Math.round(sampleForward1 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);

        clockwisePosition = (int) Math.round((sampleRotate1 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Face Gold Mineral", this);

        forwardPosition = (int) Math.round(sampleForward2 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: Knock off Gold Mineral", this);

    }// DOES SAMPLING MOVES USING VARIABLES SET IN samplePosDepot

    public void scoreSamplingCrater() {

        String cubePos = samplePosCrater();

        telemetry.addData("Cube is","%s",cubePos);
        telemetry.update();

        forwardPosition = (int) Math.round(sampleForward1 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);

        clockwisePosition = (int) Math.round((sampleRotate1 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Face Gold Mineral", this);

        forwardPosition = (int) Math.round(sampleForward2 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: Knock off Gold Mineral", this);

    }// DOES SAMPLING MOVES USING VARIABLES SET IN samplePosCrater

    public void scoreMarker() {

        clockwisePosition = (int) Math.round((markerRotate1 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Facing Depot", this);

        forwardPosition = (int) Math.round(markerForward1 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Marker: Forward into Depot", this);

        Billy.servoMarker.setPosition(prm.deployServoMarker);
        start = runtime.time();

        while (runtime.time() - start < prm.markerTimeDelay) {
            telemetry.addLine("deploying marker");
            telemetry.update();
        }

        Billy.servoMarker.setPosition(prm.stowServoMarker);

    }// DOES MARKER MOVES USING VARIABLES SET IN samplePosCrater/Depot

    public void scoreMarkerQuick() {

        clockwisePosition = (int) Math.round((markerRotate1 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Facing Depot", this);

        Billy.servoMarker.setPosition(prm.deployServoMarker);
        start = runtime.time();

        while (runtime.time() - start < prm.markerTimeDelay) {
            telemetry.addLine("deploying marker");
            telemetry.update();
        }

        Billy.servoMarker.setPosition(prm.stowServoMarker);

    }// DOES MARKER WITHOUT GOING FORWARD USING VARIABLES SET IN samplePosDepot

    public void escapeDepot() {

        forwardPosition = (int) Math.round(escapeFwd * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, 1, "Step One: forward 4 inches", this);

        rightPosition = (int) Math.round((escapeSideWays * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRightLeft(rightPosition, 1, "Go 26 inches left", this);

    }// DOES ESCAPE MOVES USING VARIABLES SET IN samplePosDepot

    public void escapeNoMarker() {

        forwardPosition = (int) Math.round(-15 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, 1, "Step One: forward 4 inches", this);
    }

    public void scoreParking() {

        clockwisePosition = (int) Math.round((parkRotate1 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 25 deg. clockwise", this);

        forwardPosition = (int) Math.round(parkForward1 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 5 inches", this);

        Billy.servoMarker.setPosition(0.7);// reach marker arm into crater to park

    }// DOES PARKING MOVES USING VARIABLES SET IN samplePosCrater

    public void depotToCrater() {

        if (mineralGold != "Right"){

            clockwisePosition = (int) Math.round((dtc1Rotate * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
            drv.driveRotate(clockwisePosition,1, "DTC 1 rotate", this);

            rightPosition = (int) Math.round((dtc2Right * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
            drv.driveRightLeft(rightPosition,1, "DTC 2 right", this);

            angleUnWrap();

            telemetry.addData("robotHeading","%.2f", robotHeading);
            telemetry.addData("rotate angle","%.2f",(-135 - robotHeading) );
            telemetry.update();

//            sleep(2000);
//
//            pressAToContinue();

            clockwisePosition = (int) Math.round(( (-135 - robotHeading) * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
            drv.driveRotate(clockwisePosition,0.5, "DTC IMU angle correct", this);

            forwardPosition = (int) Math.round(dtc3Forward * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
            drv.driveFwdRev(forwardPosition,1, "DTC 3 forward", this);

        }
        else {

            rightPosition = (int) Math.round((dtc2Right * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
            drv.driveRightLeft(rightPosition,1, "DTC 2 Left 30", this);

            angleUnWrap();

            clockwisePosition = (int) Math.round(( (-135 - robotHeading) * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
            drv.driveRotate(clockwisePosition,1, "DTC 1 Rotate 115 CCW", this);

            forwardPosition = (int) Math.round(dtc3Forward * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
            drv.driveFwdRev(forwardPosition,1, "DTC 3 forward 50", this);

        }

    }// MOVES FROM DEPOT TO CRATER USING VARIABLES SET IN samplePosDepot(??)

    public void craterToDepot() {

        forwardPosition = (int) Math.round(ctd1Back * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition,1, "Crater to Depot 1", this);

        clockwisePosition = (int) Math.round((ctd2Rotate * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition,1, "Crater to Depot 2", this);

        forwardPosition = (int) Math.round(ctd3Forward * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition,1, "Crater to Depot 3", this);

        angleUnWrap();

        telemetry.addData("robotHeading","%.2f", robotHeading);
        telemetry.addData("rotate angle","%.2f", (ctd4Rotate - robotHeading) );
        telemetry.update();

        clockwisePosition = (int) Math.round(( (ctd4Rotate - robotHeading) * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveRotate(clockwisePosition,1, "Crater to Depot 4", this);

        forwardPosition = (int) Math.round(ctd5Forward * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition,1, "Crater to Depot 5", this);

        Billy.servoMarker.setPosition(prm.deployServoMarker);
        start = runtime.time();

        while (runtime.time() - start < prm.markerTimeDelay) {
            telemetry.addLine("deploying marker");
            telemetry.update();
        }

        Billy.servoMarker.setPosition(prm.stowServoMarker);

    }// MOVES FROM DEPOT TO CRATER USING VARIABLES SET IN samplePosCrater

    public void backToCrater() {

        forwardPosition = (int) Math.round(btc1Back * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition,1, "Move backwards to crater", this);

    }// MOVES FROM DEPOT TO CRATER USING VARIABLES SET IN samplePosCrater

//    public void samplingMethodColor() { // NOT TensorFlow
//
//        Billy.servoSampling.setPosition(0.85);
//
//        start = runtime.time();
//        while (runtime.time() - start < 1) {
//            //wait for initial angle data capture prior to running code
//        }
//
//        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        driveToColor(prm.targetColorRange, -0.4, prm.saturationTolerance, "Drive To Gold Mineral");
//
//        Billy.servoSampling.setPosition(0);
//
//        start = runtime.time();
//        while (runtime.time() - start < 1) {
//
//        }
//
//        if (colorFound == "Yes") {
//
//            Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            clockwisePosition = (int) Math.round((90 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//            drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Rotating 90 deg. clockwise", this);
//
//            forwardPosition = (int) Math.round(prm.cubeKnockOffDistance * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//            drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Push Off Cube", this);
//
//            //        forwardPosition = (int) Math.round((- cubeKnockOffDistance / 2) * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//            //        driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Back Off Cube");
//
//        }
//
//        telemetry.addData("Status: ", "Complete");
//        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
//                Billy.frontLeft.getPower(), Billy.frontRight.getPower(),
//                Billy.backLeft.getPower(), Billy.backRight.getPower());
//        telemetry.addData("Target Positions", "Forward (%d), Right (%d), Rotate (%d)",
//                forwardPosition, rightPosition, clockwisePosition);
//        telemetry.addLine("Check Robot's Final Position"); //display this on screen
//        telemetry.update(); //Update telemetry to update display
//
//    } // method that has sampling code starting motion at driveToColor
//    public void driveToColor(double[] targetColor, double powerLimit, double saturationTolerance, String step) {
//
//        //update power limit
//        Billy.frontLeft.setPower(-powerLimit);
//        Billy.frontRight.setPower(powerLimit);
//        Billy.backLeft.setPower(-powerLimit);
//        Billy.backRight.setPower(powerLimit);
//
//        //Check tolerance zone to exit method
//        start = runtime.time();
//        while ((colorFound != "Yes") && (opModeIsActive()) && (runtime.time() - start < 5.0)) {
//
//            Color.RGBToHSV((int) (Billy.colorSensorSampling.red() * prm.SCALE_FACTOR),
//                    (int) (Billy.colorSensorSampling.green() * prm.SCALE_FACTOR),
//                    (int) (Billy.colorSensorSampling.blue() * prm.SCALE_FACTOR),
//                    hsvValues);
//            //Simplified reflection of values 340 to 360 to be mirrored 0 to 20 for  color wheel for red range
//            // 340 to 0 is changed to 0 to 20
//            if (hsvValues[0] >= 340) {
//
//                hsvValues[0] -= 340;
//
//            }
//
//            if (hsvValues[0] > targetColor[0] && hsvValues[0] < targetColor[1] && hsvValues[1] > saturationTolerance) {
//
//                colorFound = "Yes";
//
//            }
//            telemetry.addData("Hue", hsvValues[0]);
//            telemetry.addData("Saturation", hsvValues[1]);
//            telemetry.addData("Value", hsvValues[2]);
////            telemetry.addData("Driving: %s", step);
////            telemetry.addData("HSV:", "Hue: %0.3f Saturation: %0.3f Value: %0.3f",(double) hsvValues[0], (double) hsvValues[1], (double) hsvValues[2]);
//            telemetry.update();
//
//            idle();
//        }
//        //Set power to zero
//        Billy.frontLeft.setPower(0);
//        Billy.frontRight.setPower(0);
//        Billy.backLeft.setPower(0);
//        Billy.backRight.setPower(0);
//    }

//    public void justSample() {
//
//        String cubePos = samplePosCrater();
//
//        if (cubePos != "Unknown") {
//
//            if (cubePos == "Left") {
//
//                telemetry.addLine("Cube is LEFT");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((-25 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 25 deg. counterclockwise", this);
//
//                forwardPosition = (int) Math.round(27 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 27 inches", this);
//
//                forwardPosition = (int) Math.round(-14 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition,1, "Backward 14 inches",this);
//
//            }
//            if (cubePos == "Center") {
//
//                telemetry.addLine("Cube is CENTER");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((10 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 10 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(24 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 24 inches", this);
//
//                forwardPosition = (int) Math.round(-15 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition,1, "Backward 15 inches",this);
//
//            }
//            if (cubePos == "Right") {
//
//                telemetry.addLine("Cube is RIGHT");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((35 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 35 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(30 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 30 inches", this);
//
//                forwardPosition = (int) Math.round(-10 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition,1, "Backward 10 inches",this);
//
//                rightPosition = (int) Math.round((-20 * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS);
//                drv.driveRightLeft(rightPosition, 1, "Go 20 inches left",this);
//
//            }
//
//        }
//
//
//    }// does sampling moves then back up (uses crater sampling) WILL BE DELETED


//    public void sampleMarkerTensorFlow() {
//
//        String cubePos = samplePosDepot();
//
//        if (cubePos != "Unknown") {
//
//            if (cubePos == "Left") {
//
//                telemetry.addLine("Cube is LEFT");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((-25 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 25 deg. counterclockwise", this);
//
//                forwardPosition = (int) Math.round(27 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 27 inches", this);
//
//
//                clockwisePosition = (int) Math.round((50 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Rotating 50 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(24 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Marker: forward 20 inches", this);
//
//                Billy.servoMarker.setPosition(0.75);
//                start = runtime.time();
//
//                while (runtime.time() - start < 1) {
//                    telemetry.addLine("deploying marker");
//                    telemetry.update();
//                }
//
//                Billy.servoMarker.setPosition(0.1);
//
//                forwardPosition = (int) Math.round(-14 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Move 14 inches backwards", this);
//
//            }
//            if (cubePos == "Center") {
//
//                telemetry.addLine("Cube is CENTER");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((10 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 10 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(24 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 24 inches", this);
//
//
//                clockwisePosition = (int) Math.round((-10 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Rotating 10 deg. counterclockwise", this);
//
//                forwardPosition = (int) Math.round(20 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Marker: forward 20 inches", this);
//
//                Billy.servoMarker.setPosition(0.75);
//                start = runtime.time();
//
//                while (runtime.time() - start < 1) {
//                    telemetry.addLine("deploying marker");
//                    telemetry.update();
//                }
//
//                Billy.servoMarker.setPosition(0.1);
//
//                forwardPosition = (int) Math.round(-23 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Move 23 inches backwards", this);
//
//            }
//            if (cubePos == "Right") {
//
//                telemetry.addLine("Cube is RIGHT");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((35 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 35 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(30 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 30 inches", this);
//
//
//                clockwisePosition = (int) Math.round((-55 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Rotating 55 deg. counterclockwise", this);
//
//                forwardPosition = (int) Math.round(22 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Marker: forward 22 inches", this);
//
//                Billy.servoMarker.setPosition(0.75);
//                start = runtime.time();
//
//                while (runtime.time() - start < 1) {
//                    telemetry.addLine("deploying marker");
//                    telemetry.update();
//                }
//
//                Billy.servoMarker.setPosition(0.1);
//
//                rightPosition = (int) Math.round((-26 * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRightLeft(rightPosition, prm.DRIVE_POWER_LIMIT, "Go 26 inches left", this);
//
//            }
//
//        } else {
//
//            telemetry.addLine("Cube position not found");
//            telemetry.update();
//        }
//
//    }
//
//    public void sampleParkTensorFlow() {
//
//        String cubePos = samplePosCrater();
//
//        if (cubePos != "Unknown") {
//
//            if (cubePos == "Left") {
//
//                telemetry.addLine("Cube is LEFT");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((-25 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 25 deg. counterclockwise", this);
//
//                forwardPosition = (int) Math.round(27 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 27 inches", this);
//
//
//                clockwisePosition = (int) Math.round((25 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 25 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(5 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 5 inches", this);
//
//                Billy.servoMarker.setPosition(0.7);// reach marker arm into crater to park
//
//            }
//            if (cubePos == "Center") {
//
//                telemetry.addLine("Cube is CENTER");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((10 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 10 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(24 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 24 inches", this);
//
//
//                Billy.servoMarker.setPosition(0.7);// reach marker arm into crater to park
//
//            }
//            if (cubePos == "Right") {
//
//                telemetry.addLine("Cube is RIGHT");
//                telemetry.update();
//
//                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                clockwisePosition = (int) Math.round((35 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 35 deg. clockwise", this);
//
//                forwardPosition = (int) Math.round(25 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step Three: forward 25 inches", this);
//
//
//                clockwisePosition = (int) Math.round((-35 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveRotate(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 35 deg. counterclockwise", this);
//
//                forwardPosition = (int) Math.round(5 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
//                drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Step One: forward 4 inches", this);
//
//                Billy.servoMarker.setPosition(0.7);// reach marker arm into crater to park
//
//            }
//
//        } else {
//
//            telemetry.addLine("Cube position not found");
//            telemetry.update();
//        }
//
//    }

    public void imuSampleMarkerTensorFlow() {

        String cubePos = samplePosDepot();

        if (cubePos != "Unknown") {

            if (cubePos == "Left") {

                telemetry.addLine("Cube is LEFT");
                telemetry.update();

                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Step One: forward 4 inches", this);

                clockwisePosition = (int) Math.round((-25 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveRotateIMU(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 25 deg. counterclockwise", this);

                forwardPosition = (int) Math.round(27 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Step Three: forward 27 inches", this);


                clockwisePosition = (int) Math.round((50 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveRotateIMU(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Rotating 50 deg. clockwise", this);

                forwardPosition = (int) Math.round(24 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Marker: forward 20 inches", this);

                Billy.servoMarker.setPosition(0.75);
                start = runtime.time();

                while (runtime.time() - start < 1) {
                    telemetry.addLine("deploying marker");
                    telemetry.update();
                }

                Billy.servoMarker.setPosition(0.1);

                forwardPosition = (int) Math.round(-14 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Move 14 inches backwards", this);

            }
            if (cubePos == "Center") {

                telemetry.addLine("Cube is CENTER");
                telemetry.update();

                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Step One: forward 4 inches", this);

                clockwisePosition = (int) Math.round((10 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveRotateIMU(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 10 deg. clockwise", this);

                forwardPosition = (int) Math.round(24 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Step Three: forward 24 inches", this);


                clockwisePosition = (int) Math.round((-10 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveRotateIMU(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Rotating 10 deg. counterclockwise", this);

                forwardPosition = (int) Math.round(20 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Marker: forward 20 inches", this);

                Billy.servoMarker.setPosition(0.75);
                start = runtime.time();

                while (runtime.time() - start < 1) {
                    telemetry.addLine("deploying marker");
                    telemetry.update();
                }

                Billy.servoMarker.setPosition(0.1);

                forwardPosition = (int) Math.round(-23 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Move 23 inches backwards", this);

            }
            if (cubePos == "Right") {

                telemetry.addLine("Cube is RIGHT");
                telemetry.update();

                forwardPosition = (int) Math.round(4 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Step One: forward 4 inches", this);

                clockwisePosition = (int) Math.round((35 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveRotateIMU(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Step Two: Rotating 35 deg. clockwise", this);

                forwardPosition = (int) Math.round(30 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Step Three: forward 30 inches", this);


                clockwisePosition = (int) Math.round((-55 * prm.adjustedRotate) * prm.ROBOT_DEG_TO_WHEEL_INCH * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveRotateIMU(clockwisePosition, prm.ROTATE_POWER_LIMIT, "Marker: Rotating 55 deg. counterclockwise", this);

                forwardPosition = (int) Math.round(22 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
                drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0, "Marker: forward 22 inches", this);

                Billy.servoMarker.setPosition(0.75);
                start = runtime.time();

                while (runtime.time() - start < 1) {
                    telemetry.addLine("deploying marker");
                    telemetry.update();
                }

                Billy.servoMarker.setPosition(0.1);

                rightPosition = (int) Math.round((-26 * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS);
                drv.driveRightLeftIMU(rightPosition, prm.DRIVE_POWER_LIMIT, 0, "Go 26 inches left", this);

            }

        } else {

            telemetry.addLine("Cube position not found");
            telemetry.update();
        }


    }// uses IMU drive methods

}