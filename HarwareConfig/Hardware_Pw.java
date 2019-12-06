package Skystone_14999.HarwareConfig;

import com.qualcomm.ftccommon.configuration.EditLegacyServoControllerActivity;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Hardware_Pw
{
    /* Public OpMode members. */
    public DcMotor  frontLeft        = null;
    public DcMotor  frontRight       = null;
    public DcMotor  backLeft         = null;
    public DcMotor  backRight        = null;
    public DcMotor  jackLeft         = null;
    public DcMotor  jackRight        = null;
    public DcMotor  slide            = null;

    public Servo  servoFoundationL  = null;
    public Servo  servoFoundationR = null;

    public Servo  stoneServoLeft   = null;
    public Servo  stoneServoRight   = null;

    // DRIVING Variables
    private double teleOpDrivePowerLimit = 0;

    private double forwardDirection = 0;
    private double rightDirection = 0;

    private double clockwise = 0;
    private double clockwiseDirection = 0;
    private double counterclockwiseDirection = 0;

    // DRIVING Variables
    private double x, y, turn = 0;
    private double dmFrontLeft, dmBackLeft, dmFrontRight, dmBackRight = 0;

    // JACK Variables
    private double jackPower = 0;
    private double jackPowerLimit = 0;

    // SLIDE Variables
    private double slidePowerDirection = 0;
    private double slidePowerLimit = 0;

    // HAND Variables
    // Range of SERVOs movement. Valid values between 0.0 and 1.0
    private double servoGear_Movement = 0.85;   // From 0.0 to 0.81 = 3.25 inches
    // From 0.0 to 1.0 = 4 inches
    // Hand 'Wide Open' position
    private double servoHand_MinPosition = 0.15;

    // Hand 'Totally Closed' position
    private double servoHand_MaxPosition = (servoHand_MinPosition + servoGear_Movement > 1.0) ? 1.0 : servoHand_MinPosition + servoGear_Movement;

    private double servoHand_Position = servoHand_MaxPosition; // It will close hand during INIT process

    private double servoHandMovement = 0.005; // to double the speed change value to 0.01

    // Variables used for 'Hand process' that GRABs automatically an 8in wide block
    private double servoHandHold8inPosition = 0.45; // Position to grab block 8in wide
    public boolean GoGrab8in = false;  // if true => Servos will go to 'servoHandHold8in' position
    public boolean GoOpenUp = false;


    public BNO055IMU    imu = null;

    // public HardwareMap hwMap           =  null;
    // public ElapsedTime period  = new ElapsedTime();

    // To keep information about initial parameters
    public Skystone_14999.Parameters.Constants_Pw cons = new Skystone_14999.Parameters.Constants_Pw();

    // Calibration Mode. Variables
    private boolean jackCalibrationMode = false;

    // Variables to control DISPLAY info
    private int displayStatusNumber = 1;
    private int displayStatusNumberBackup = 0;

    // public Orientation angles;

    /* Constructor */
    public Hardware_Pw(OpMode mc){ // HardwareMap hwMap
        mc.telemetry.addData("Status: ", "Initializing...");

        HardwareMap hwMap = mc.hardwareMap;
        mc.telemetry.addData("Status: ", "Initializing [Robot]");

        /* MOTORS. Initialize and set initial values */
        frontLeft  = hwMap.get(DcMotor.class, "motor_fl");
        frontRight = hwMap.get(DcMotor.class, "motor_fr");
        backLeft  = hwMap.get(DcMotor.class, "motor_bl");
        backRight = hwMap.get(DcMotor.class, "motor_br");
        jackLeft  = hwMap.get(DcMotor.class, "motor_jack_left");
        jackRight = hwMap.get(DcMotor.class, "motor_jack_right");
        slide = hwMap.get(DcMotor.class, "motor_slide");

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        jackLeft.setPower(0);
        jackRight.setPower(0);
        slide.setPower(0);

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        jackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        jackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        slide.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        jackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        jackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        jackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        jackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /* SERVOS. Initialize */
        stoneServoLeft = hwMap.get(Servo.class, "stone_servo_left");
        stoneServoRight = hwMap.get(Servo.class, "stone_servo_right");

        servoFoundationL = hwMap.get(Servo.class, "foundation_l_servo");
        servoFoundationR = hwMap.get(Servo.class, "foundation_r_servo");

        stoneServoLeft.setPosition(servoHand_Position);
        stoneServoRight.setPosition(servoHand_Position);

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.

        mc.telemetry.addData("Status: ", "Initializing [Robot IMU]");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        //stoneServoLeft.setPosition(stoneServoLeft.getPosition());
        //stoneServoRight.setPosition(stoneServoRight.getPosition());
        //servoFoundationL.setPosition(0.10);
        //servoFoundationR.setPosition(0.90);

        // Load parameters from file
        cons.readOrWriteHashMap(mc);

        jackPowerLimit = cons.pHM.get("jackPowerLimit").value;
        slidePowerLimit = cons.pHM.get("slidePowerLimit").value;
        teleOpDrivePowerLimit = cons.pHM.get("teleOpDrivePowerLimit").value;

        //Indicate initialization complete and provide telemetry
        mc.telemetry.addData("Status: ", "Initialized");
        //mc.telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)", forwardDirection, rightDirection, clockwise);
        //mc.telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        //mc.telemetry.addData(">", "Press Play to start");
        mc.telemetry.update();
    }

    private boolean isCalibrationMode(Gamepad gap1, Gamepad gap2) {
        if (((gap2.right_trigger > 0.5) && (gap2.left_trigger > 0.5)) && (! jackCalibrationMode)) {
                jackCalibrationMode = true;
                displayStatusNumberBackup = displayStatusNumber;
                displayStatusNumber = 90; // Will display CALIBRATION MODE
            }
        else {
            if (jackCalibrationMode) {
                jackCalibrationMode = false;
                displayStatusNumber = displayStatusNumberBackup;
            }
        }
        return jackCalibrationMode;
    }

    public void setWheelsPower(Gamepad gp1, Gamepad gp2) {
        x = gp1.left_stick_x * teleOpDrivePowerLimit;
        y = gp1.left_stick_y * teleOpDrivePowerLimit;
        turn = gp1.right_stick_x * teleOpDrivePowerLimit;

        if (gp1.left_trigger > 0) x = -gp1.left_trigger;
        if (gp1.right_trigger > 0) x = gp1.right_trigger;

        dmFrontLeft = y -x -turn;
        dmBackLeft = y +x -turn;
        dmFrontRight = -y -x -turn;
        dmBackRight = -y +x -turn;

        frontLeft.setPower(Range.clip(dmFrontLeft, -teleOpDrivePowerLimit, teleOpDrivePowerLimit));
        backLeft.setPower(Range.clip(dmBackLeft, -teleOpDrivePowerLimit, teleOpDrivePowerLimit));
        frontRight.setPower(Range.clip(dmFrontRight, -teleOpDrivePowerLimit, teleOpDrivePowerLimit));
        backRight.setPower(Range.clip(dmBackRight, -teleOpDrivePowerLimit, teleOpDrivePowerLimit));
    }

    public void setJackPower(Gamepad gp1, Gamepad gp2) {
        if (!(isCalibrationMode(gp1, gp2))) {
            jackPower = gp2.left_stick_y * jackPowerLimit * -1;

            jackLeft.setPower(Range.clip(jackPower, -0.9, 0.9)); // Min/Max for fixed max. limits
            jackRight.setPower(Range.clip(jackPower, -0.9, 0.9)); // Min/Max for fixed max. limits
        }
        else // CALIBRATION MODE
        {
            jackPower = 0;

            jackLeft.setPower(gp2.left_stick_y * 0.20 * -1);
            jackRight.setPower(gp2.right_stick_y * 0.20 * -1);
        }
    }

    public void setSlidePower(Gamepad gp1, Gamepad gp2) {
        if (!(isCalibrationMode(gp1, gp2)))
        {
            slidePowerDirection = gp2.right_stick_y * slidePowerLimit;
            slide.setPower(Range.clip(slidePowerDirection, -0.8, 0.8));
        }
    }

    public void HandOpening() {
        servoHand_Position = servoHand_Position - servoHandMovement;
        if (servoHand_Position < servoHand_MinPosition)
            servoHand_Position = servoHand_MinPosition;

        stoneServoLeft.setPosition(servoHand_Position);
        stoneServoRight.setPosition(servoHand_Position);
    }

    public void HandClosing() {
        servoHand_Position = servoHand_Position + servoHandMovement;
        if (servoHand_Position > servoHand_MaxPosition)
            servoHand_Position = servoHand_MaxPosition;

        stoneServoLeft.setPosition(servoHand_Position);
        stoneServoRight.setPosition(servoHand_Position);
    }

    public void HandGoClosingTo8in() {
        if ((GoGrab8in) && !(servoHand_Position >= servoHandHold8inPosition)){
            servoHand_Position = servoHand_Position + (servoHandMovement * 2);

            if (servoHand_Position >= servoHandHold8inPosition) {
                servoHand_Position = servoHandHold8inPosition;
                GoGrab8in = false; // GRABBING 8in process COMPLETED
            }

            // Code for unforeseen situations
            if ((GoGrab8in) && (servoHand_Position >= 1))
            {
                servoHand_Position = 1;
                GoGrab8in = false; // GRABBING 8in process STOPPED
            }

            stoneServoLeft.setPosition(servoHand_Position);
            stoneServoRight.setPosition(servoHand_Position);
        }
        else
            GoGrab8in = false;
    }

    public void HandGoOpeningUp() {
        if (GoOpenUp && !(GoGrab8in) && !(servoHand_Position <= servoHand_MinPosition)){
            servoHand_Position = servoHand_Position - (servoHandMovement * 2);

            if (servoHand_Position <= servoHand_MinPosition) {
                servoHand_Position = servoHand_MinPosition;
                GoOpenUp = false; // OpenUp process COMPLETED
            }

            // Code for unforeseen situations
            if ((GoOpenUp) && (servoHand_Position <= 0))
            {
                servoHand_Position = 0;
                GoOpenUp = false; // GRABBING 8in process STOPPED
            }

            stoneServoLeft.setPosition(servoHand_Position);
            stoneServoRight.setPosition(servoHand_Position);
        }
        else
            GoOpenUp = false;
    }

    /* Method to make sure all engines are stopped */
    public void stopMotors() {

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        jackLeft.setPower(0);
        jackRight.setPower(0);
        slide.setPower(0);
    }

    public void setDisplayStatus(int value) {
        displayStatusNumber += value;
        if (displayStatusNumber >= 4) displayStatusNumber = 1;
    }

    public void DisplayStatus(OpMode mc) {

                switch (displayStatusNumber) {
                    case 1:
                        mc.telemetry.addData("DISPLAY: ", "Wheels");
                        mc.telemetry.addData("Front Left", frontLeft.getPower());
                        mc.telemetry.addData("Front Right", frontRight.getPower());
                        mc.telemetry.addData("Back Left", backLeft.getPower());
                        mc.telemetry.addData("Back Right", backRight.getPower());
                        mc.telemetry.addData("Wheels_PowerLimit", teleOpDrivePowerLimit);
                        break;
                    case 2:
                        mc.telemetry.addData("DISPLAY: ", "Hand Info");
                        mc.telemetry.addData("Servo_Position", servoHand_Position);
                        mc.telemetry.addData("GoGrab8in", GoGrab8in);
                        mc.telemetry.addData("GoOpenOp", GoOpenUp);
                        break;
                    case 3:
                        mc.telemetry.addData("DISPLAY: ", "Jack & Slide");
                        mc.telemetry.addData("Jack_Power", jackPower);
                        mc.telemetry.addData("Jack_PowerLimit", jackPowerLimit);

                        mc.telemetry.addData("Slide_Power", slidePowerDirection);
                        mc.telemetry.addData("Slide_PowerLimit", slidePowerLimit);
                        break;
                    case 90:
                        mc.telemetry.addData("DISPLAY: ", "** CALIBRATION MODE **");
                        mc.telemetry.addData("Power - Jack_Left = ", jackLeft.getPower());
                        mc.telemetry.addData("Power - Jack_Right = ", jackRight.getPower());
                }

        mc.telemetry.update();
    }
}