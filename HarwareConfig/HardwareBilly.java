
package Skystone_14999.HarwareConfig;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
//import TestOpModesOffline.DcMotor;//ADDED AND COMMENTED OUT ABOVE FOR OFFLINE
//import TestOpModesOffline.BNO055IMU; //ADDED AND COMMENTED OUT ABOVE FOR OFFLINE
//import TestOpModesOffline.JustLoggingAccelerationIntegrator; //ADDED AND COMMENTED OUT ABOVE FOR OFFLINE
//import TestOpModesOffline.Servo;//ADDED AND COMMENTED OUT ABOVE FOR OFFLINE

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import Skystone_14999.OpModes.Autonomous.BasicAuto;
import Skystone_14999.OpModes.BasicOpMode;
import Skystone_14999.Parameters.Constants;

/*
* REVISION HISTORY
* made this the hardware for "Billy" with everything for lead screw slide and mineral collecting arm
*/
public class HardwareBilly
{
    /* Public OpMode members. */
    public DcMotor  frontLeft   = null;
    public DcMotor  frontRight  = null;
    public DcMotor  backLeft    = null;
    public DcMotor  backRight   = null;
    public DcMotor jack = null;
    public DcMotor  slide       = null;

    public Servo servoFoundationL  = null;
    public Servo  servoFoundationR  = null;
    public Servo    stoneServoArm   = null;
    public BNO055IMU    imu = null;
    public HardwareMap hwMap           =  null;
    public ElapsedTime period  = new ElapsedTime();

    public Servo  stoneServoLeft    = null;
    public Servo  stoneServoRight   = null;
//      public Servo  servoCapstoneRelease   = null;

    int targetPos[] = new int[4];

    public enum moveDirection {FwdBack, RightLeft, Rotate}

    public double DRIVE_POWER_LIMIT = 1;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit
    public double ROTATE_POWER_LIMIT = 1;//clockwise rotation power/speed to be converted to individual motor powers/speeds
    public double DRIVE_POWER_MINIMUM = 0.1;
    public double STEERING_POWER_LIMIT = 0.5;
    public double STEERING_POWER_GAIN = 0.1;
    public double POWER_GAIN = 0.2;
    public double ROTATE_POWER_GAIN = 0.02;
    public double IMU_ROTATE_TOL = 1.0;
    public double IMU_DISTANCE_TOL = 1.0;
    int MOVE_TOL = 30;// tolerance for motor reaching final positions in drive methods
    double TELEOP_DRIVE_POWER_LIMIT = 1;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
    double TELEOP_ROTATE_POWER_LIMIT = 1;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
    double JACK_POWER_LIMIT = 0.75;
    double SLIDE_POWER_LIMIT = 0.6;
    double dropStoneForward = 35;
    double foundationExtraFwd = 0;
    double foundationExtraSideways = 0;

    public double clockwise =0;
    public double forwardDirection =0;
    public double rightDirection =0;
    public double verticalDirection = 0;
    public double clockwiseDirection =0;
    public double counterclockwiseDirection = 0;
    public double slideDirection =0;

    public double priorAngle = 0;
    public double offset = 0;
    public double robotHeading = 0;

    public int currentPos[] = new int[4];
    public int priorPos[] = new int[4];
    public double distanceTraveled = 0;

    /* local OpMode members. */
    public Orientation angles;

    /* Constructor */
    public HardwareBilly(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, boolean tm, Constants cons) {
        // Save reference to Hardware map
        if(tm) {

            // Define and Initialize Motors
//            frontLeft = new DcMotor();
//            frontRight = new DcMotor();
//            backLeft = new DcMotor();
//            backRight = new DcMotor();
//            jack = new DcMotor();
//            slide = new DcMotor();
//
//            // Define and initialize ALL installed servos.
//            servoFoundationL = new Servo();
//            servoFoundationR = new Servo();
//
//            stoneServoLeft = new Servo();
//            stoneServoRight = new Servo();
////        servoCapstoneRelease = new Servo();
//            stoneServoArm = new Servo();
//
//            imu = new TestOpModesOffline.BNO055IMU();
//            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();//Need help on enclosing class
//            imu.initialize(parameters);
//
//            DRIVE_POWER_LIMIT = cons.DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit
//            ROTATE_POWER_LIMIT = cons.ROTATE_POWER_LIMIT;//clockwise rotation power/speed to be converted to individual motor powers/speeds
//            DRIVE_POWER_MINIMUM = cons.DRIVE_POWER_MINIMUM;
//            STEERING_POWER_LIMIT = cons.STEERING_POWER_LIMIT;
//            STEERING_POWER_GAIN = cons.STEERING_POWER_GAIN;
//            POWER_GAIN = cons.POWER_GAIN;
//            ROTATE_POWER_GAIN = cons.ROTATE_POWER_GAIN;
//            IMU_ROTATE_TOL = cons.IMU_ROTATE_TOL;
//            IMU_DISTANCE_TOL = cons.IMU_DISTANCE_TOL;
//            MOVE_TOL = cons.MOVE_TOL;// tolerance for motor reaching final positions in drive methods
//            TELEOP_DRIVE_POWER_LIMIT = cons.TELEOP_DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
//            TELEOP_ROTATE_POWER_LIMIT = cons.TELEOP_ROTATE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
//            JACK_POWER_LIMIT = cons.JACK_POWER_LIMIT;
//            SLIDE_POWER_LIMIT = cons.SLIDE_POWER_LIMIT;
//            dropStoneForward = cons.dropStoneForward;
//            foundationExtraFwd = cons.foundationExtraFwd;
//            foundationExtraSideways = cons.foundationExtraSideways;

        }
        else {

            hwMap = ahwMap;

            // Define and Initialize Motors
            frontLeft = hwMap.get(DcMotor.class, "motor_fl");
            frontRight = hwMap.get(DcMotor.class, "motor_fr");
            backLeft = hwMap.get(DcMotor.class, "motor_bl");
            backRight = hwMap.get(DcMotor.class, "motor_br");
            jack = hwMap.get(DcMotor.class, "motor_jack");
            slide = hwMap.get(DcMotor.class, "motor_slide");

            // Define and initialize ALL installed servos.
            servoFoundationL = hwMap.get(Servo.class, "foundation_l_servo");
            servoFoundationR = hwMap.get(Servo.class, "foundation_r_servo");

            stoneServoLeft = hwMap.get(Servo.class, "stone_servo_left");
            stoneServoRight = hwMap.get(Servo.class, "stone_servo_right");
//        servoCapstoneRelease = hwMap.get(Servo.class, "capstone_servo");
            stoneServoArm = hwMap.get(Servo.class, "stone_arm_servo");

            //Define all installed sensors

            // Set up the parameters with which we will use our IMU. Note that integration
            // algorithm here just reports accelerations to the logcat log; it doesn't actually
            // provide positional information.

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
            // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
            // and named "imu".
            imu = hwMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);

            DRIVE_POWER_LIMIT = cons.DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit
            ROTATE_POWER_LIMIT = cons.ROTATE_POWER_LIMIT;//clockwise rotation power/speed to be converted to individual motor powers/speeds
            DRIVE_POWER_MINIMUM = cons.DRIVE_POWER_MINIMUM;
            STEERING_POWER_LIMIT = cons.STEERING_POWER_LIMIT;
            STEERING_POWER_GAIN = cons.STEERING_POWER_GAIN;
            POWER_GAIN = cons.POWER_GAIN;
            ROTATE_POWER_GAIN = cons.ROTATE_POWER_GAIN;
            IMU_ROTATE_TOL = cons.IMU_ROTATE_TOL;
            IMU_DISTANCE_TOL = cons.IMU_DISTANCE_TOL;
            MOVE_TOL = cons.MOVE_TOL;// tolerance for motor reaching final positions in drive methods
            TELEOP_DRIVE_POWER_LIMIT = cons.TELEOP_DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
            TELEOP_ROTATE_POWER_LIMIT = cons.TELEOP_ROTATE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
            JACK_POWER_LIMIT = cons.JACK_POWER_LIMIT;
            SLIDE_POWER_LIMIT = cons.SLIDE_POWER_LIMIT;
            dropStoneForward = cons.dropStoneForward;
            foundationExtraFwd = cons.foundationExtraFwd;
            foundationExtraSideways = cons.foundationExtraSideways;
        }

    }

    public void initMiniBot(HardwareMap ahwMap, boolean tm, Constants cons) {

        if(tm) {

            // Define and Initialize Motors
//            frontLeft = new DcMotor();
//            frontRight = new DcMotor();
//            backLeft = new DcMotor();
//            backRight = new DcMotor();
//            jack = new DcMotor();
//            slide = new DcMotor();
//
//            // Define and initialize ALL installed servos.
//            servoFoundationL = new Servo();
//            servoFoundationR = new Servo();
//
//            stoneServoLeft = new Servo();
//            stoneServoRight = new Servo();
////        servoCapstoneRelease = new Servo();
//            stoneServoArm = new Servo();
//            imu = new TestOpModesOffline.BNO055IMU();
//            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();//Need help on enclosing class
//            imu.initialize(parameters);
//
//            DRIVE_POWER_LIMIT = cons.DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit
//            ROTATE_POWER_LIMIT = cons.ROTATE_POWER_LIMIT;//clockwise rotation power/speed to be converted to individual motor powers/speeds
//            DRIVE_POWER_MINIMUM = cons.DRIVE_POWER_MINIMUM;
//            STEERING_POWER_LIMIT = cons.STEERING_POWER_LIMIT;
//            STEERING_POWER_GAIN = cons.STEERING_POWER_GAIN;
//            POWER_GAIN = cons.POWER_GAIN;
//            ROTATE_POWER_GAIN = cons.ROTATE_POWER_GAIN;
//            IMU_ROTATE_TOL = cons.IMU_ROTATE_TOL;
//            IMU_DISTANCE_TOL = cons.IMU_DISTANCE_TOL;
//            MOVE_TOL = cons.MOVE_TOL;// tolerance for motor reaching final positions in drive methods
//            TELEOP_DRIVE_POWER_LIMIT = cons.TELEOP_DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
//            TELEOP_ROTATE_POWER_LIMIT = cons.TELEOP_ROTATE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
//            JACK_POWER_LIMIT = cons.JACK_POWER_LIMIT;
//            SLIDE_POWER_LIMIT = cons.SLIDE_POWER_LIMIT;
//            dropStoneForward = cons.dropStoneForward;
//            foundationExtraFwd = cons.foundationExtraFwd;
//            foundationExtraSideways = cons.foundationExtraSideways;

        }
        else {
            // Save reference to Hardware map
            hwMap = ahwMap;

            // Define and Initialize Motors
            frontLeft = hwMap.get(DcMotor.class, "motor_fl");
            frontRight = hwMap.get(DcMotor.class, "motor_fr");
            backLeft = hwMap.get(DcMotor.class, "motor_bl");
            backRight = hwMap.get(DcMotor.class, "motor_br");

            stoneServoArm = hwMap.get(Servo.class, "stone_arm_servo");

            // Set up the parameters with which we will use our IMU. Note that integration
            // algorithm here just reports accelerations to the logcat log; it doesn't actually
            // provide positional information.

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
            // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
            // and named "imu".
            imu = hwMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);

            DRIVE_POWER_LIMIT = cons.DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit
            ROTATE_POWER_LIMIT = cons.ROTATE_POWER_LIMIT;//clockwise rotation power/speed to be converted to individual motor powers/speeds
            DRIVE_POWER_MINIMUM = cons.DRIVE_POWER_MINIMUM;
            STEERING_POWER_LIMIT = cons.STEERING_POWER_LIMIT;
            STEERING_POWER_GAIN = cons.STEERING_POWER_GAIN;
            POWER_GAIN = cons.POWER_GAIN;
            ROTATE_POWER_GAIN = cons.ROTATE_POWER_GAIN;
            IMU_ROTATE_TOL = cons.IMU_ROTATE_TOL;
            IMU_DISTANCE_TOL = cons.IMU_DISTANCE_TOL;
            MOVE_TOL = cons.MOVE_TOL;// tolerance for motor reaching final positions in drive methods
            TELEOP_DRIVE_POWER_LIMIT = cons.TELEOP_DRIVE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
            TELEOP_ROTATE_POWER_LIMIT = cons.TELEOP_ROTATE_POWER_LIMIT;//chassis drive wheel (FR, FL, BR, BL) Motor power/speed limit for teleop
            JACK_POWER_LIMIT = cons.JACK_POWER_LIMIT;
            SLIDE_POWER_LIMIT = cons.SLIDE_POWER_LIMIT;
            dropStoneForward = cons.dropStoneForward;
            foundationExtraFwd = cons.foundationExtraFwd;
            foundationExtraSideways = cons.foundationExtraSideways;
        }

    }

    public void initIMU(BasicOpMode om) {

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU

        offset = angles.firstAngle; //Determine initial angle offset 
        priorAngle = offset; //set prior angle for unwrap to be initial angle 
        robotHeading = angles.firstAngle - offset; //robotHeading to be 0 degrees to start 

        om.sleep(100);
    }

    public void angleUnWrap() {

        double deltaAngle;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU
        deltaAngle = -(angles.firstAngle - priorAngle);// Determine how much the angle has changed since we last checked teh angle
        if (deltaAngle > 180) {//This is IF/THEN for the unwrap routine
            robotHeading += deltaAngle - 360;//Decrease angle for negative direction //rotation
        } else if (deltaAngle < -180) {
            robotHeading += deltaAngle + 360;//increase angle for positive direction //rotation 
        } else {
            robotHeading += deltaAngle;//No wrap happened, don't add any extra rotation
        }
        priorAngle = angles.firstAngle;//Update the latest measurement to be //priorAngle for the next time we call the method

    }

    //Autonomous
    public void IMUDriveFwdRight(HardwareBilly.moveDirection moveType, double distanceInch, double targetAngle, String step, BasicAuto om) {
        int countDistance = 0;
        int[] driveDirection = new int[4];
        int[] startPos = new int[4];
        boolean motorsDone = false;
        double[] error = new double[4];
        double[] setPower = new double[4];
        double scaledDistance = 0 ;

        setMotorPower(0);

        distanceTraveled = 0;

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        switch(moveType) {

            case FwdBack :
                scaledDistance = (distanceInch * om.cons.adjForward);

                driveDirection[0] = -1;// FL
                driveDirection[1] = +1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = -1;// BL
                break;

            case RightLeft :
                scaledDistance = (distanceInch * om.cons.adjRight);
                driveDirection[0] = -1;// FL
                driveDirection[1] = -1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = +1;// BL
                break;

            default:

                driveDirection[0] = 0;// FL
                driveDirection[1] = 0;// FR
                driveDirection[2] = 0;// BR
                driveDirection[3] = 0;// BL
        }

        currentPos = motorStartPos();

        priorPos = currentPos;

        calcDistanceIMU(driveDirection, om);

        double steeringPower = calcSteeringPowerIMU(targetAngle);

        om.updateIMU();

        double prePower[] = new double[4];

        for(int i = 0; i < 4; i++){

            prePower[i] = Range.clip( ( (scaledDistance - distanceTraveled) * driveDirection[i] ) * POWER_GAIN, -DRIVE_POWER_LIMIT, DRIVE_POWER_LIMIT) + steeringPower;

        }

        double maxLeft = Math.max(Math.abs(prePower[0]), Math.abs(prePower[3]));
        double maxRight = Math.max(Math.abs(prePower[1]), Math.abs(prePower[2]));
        double max = Math.max(maxLeft, maxRight);

        if(DRIVE_POWER_LIMIT >= max) {

            max = 1;
        }

        for(int i = 0; i < 4; i++){

            prePower[i] = Range.clip(Math.abs(prePower[i] / max), DRIVE_POWER_MINIMUM, DRIVE_POWER_LIMIT) * Math.signum(prePower[i]);

        }

        setMotorPowerArray(prePower);

        while( (Math.abs(scaledDistance - distanceTraveled) > IMU_DISTANCE_TOL) && (om.opModeIsActive() || om.testModeActive)) {

            currentPos = motorStartPos();

            calcDistanceIMU(driveDirection, om);

            steeringPower = calcSteeringPowerIMU(targetAngle);

            om.updateIMU();

            for(int i = 0; i < 4; i++){

                prePower[i] = Range.clip( ( (scaledDistance - distanceTraveled) * driveDirection[i] ) * POWER_GAIN, -DRIVE_POWER_LIMIT, DRIVE_POWER_LIMIT) + steeringPower;

            }

            maxLeft = Math.max(Math.abs(prePower[0]), Math.abs(prePower[3]));
            maxRight = Math.max(Math.abs(prePower[1]), Math.abs(prePower[2]));
            max = Math.max(maxLeft, maxRight);

            if(DRIVE_POWER_LIMIT >= max) {

                max = 1;
            }

            for(int i = 0; i < 4; i++){

                //*************** MODIFIED POWER CLIP FOR setPower TO HAVE DRIVE_POWER_MINIMUM ************************************

//                prePower[i] = prePower[i] / max;//NOT LIMITED TO DRIVE_POWER_MINIMUM
                setPower[i] = Math.signum(prePower[i]) * Range.clip(Math.abs(prePower[i] / max),DRIVE_POWER_MINIMUM,DRIVE_POWER_LIMIT);
                //*************** MODIFIED POWER CLIP FOR setPower TO HAVE DRIVE_POWER_MINIMUM ************************************
            }
            //*************** MODIFIED TO setPower  ************************************
            setMotorPowerArray(setPower);
            //*************** MODIFIED TO setPower  ************************************

            //*************** MODIFIED TELEMETRY FOR DEBUGGING ************************************
            om.telemetry.addData("Driving: ", step);
//            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                    targetPos[0], targetPos[1],targetPos[2],targetPos[3]);
            om.telemetry.addData("Robot Heading: ", " Desired: %.2f, Actual: %.2f",targetAngle,robotHeading);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    currentPos[0],currentPos[1],currentPos[2],currentPos[3]);
            om.telemetry.addData("Motor Power: ", "FL (%.2f) FR (%.2f) BR (%.2f) BL (%.2f)",
                    setPower[0],setPower[1],setPower[2],setPower[3]);
            om.telemetry.addData("Steering ", "Power: %.2f, Gain: %.3f", steeringPower,STEERING_POWER_GAIN);
            om.telemetry.addData("Distance Moved: ", "%.2f",distanceTraveled);
            om.telemetry.addData("Time: ", om.runtime);
            om.telemetry.update();
        }

        setMotorPower(0);

        angleUnWrap();
        om.updateIMU();

        om.telemetry.addData("COMPLETED Driving: ", step);
//        om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                targetPos[0], targetPos[1],targetPos[2],targetPos[3]);
        om.telemetry.addData("Robot Heading: ", " Desired: %.2f, Actual: %.2f",targetAngle,robotHeading);
        om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
                backRight.getCurrentPosition(), backLeft.getCurrentPosition());
        om.telemetry.addData("Distance Moved: ", "%.2f",distanceTraveled);
        om.telemetry.addData("Time: ", om.runtime);
        om.telemetry.addLine("=========================================");
        om.telemetry.update();
        //*************** MODIFIED TELEMETRY FOR DEBUGGING ************************************
    }

    public void IMUDriveRotate(double targetAngle, String step, BasicAuto om) {
        int countDistance = 0;
        int[] driveDirection = new int[4];
        int[] startPos = new int[4];
        boolean motorsDone = false;
        double[] error = new double[4];
        double[] setPower = new double[4];
        double prePower[] = new double[4];
        double scaledDistance = 0 ;

        setMotorPower(0);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        driveDirection[0] = -1;// FL
        driveDirection[1] = -1;// FR
        driveDirection[2] = -1;// BR
        driveDirection[3] = -1;// BL
        //*************** ADDED CURRENT POSITION CALCULATION  ************************************

        currentPos = motorStartPos();
        //*************** ADDED CURRENT POSITION CALCULATION  ************************************

        angleUnWrap();
        om.updateIMU();

        for(int i = 0; i < 4; i++){

            prePower[i] = driveDirection[i] * (targetAngle - robotHeading) * ROTATE_POWER_GAIN;

            setPower[i] = Math.signum(prePower[i]) * Range.clip( Math.abs(prePower[i]), DRIVE_POWER_MINIMUM, ROTATE_POWER_LIMIT);

        }

        setMotorPowerArray(prePower);

        while( (Math.abs(targetAngle - robotHeading) > IMU_ROTATE_TOL) && (om.opModeIsActive() || om.testModeActive)) {
            //*************** ADDED CURRENT POSITION CALCULATION  FOR TELEMETRY ************************************

            currentPos = motorStartPos();
            //*************** ADDED CURRENT POSITION CALCULATION  FOR TELEMETRY ************************************


            angleUnWrap();
            om.updateIMU();

            for(int i = 0; i < 4; i++){

                prePower[i] = driveDirection[i] * (targetAngle - robotHeading) * ROTATE_POWER_GAIN;

                setPower[i] = Math.signum(prePower[i]) * Range.clip( Math.abs(prePower[i]) , DRIVE_POWER_MINIMUM, ROTATE_POWER_LIMIT);

            }

            setMotorPowerArray(prePower);
            //*************** MODIFIED TELEMETRY FOR DEBUGGING ************************************

            om.telemetry.addData("Driving: ", step);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    currentPos[0], currentPos[1], currentPos[2], currentPos[3]);
            om.telemetry.addData("Motor Power: ", "FL (%.2f) FR (%.2f) BR (%.2f) BL (%.2f)",
                    setPower[0],setPower[1],setPower[2],setPower[3]);
            om.telemetry.addData("Robot Heading: ", " Desired: %.2f, Actual: %.2f",targetAngle,robotHeading);
            om.telemetry.addData("Time: ", om.runtime);
            om.telemetry.addLine("=========================================");
            om.telemetry.update();
        }

        setMotorPower(0);

        currentPos = motorStartPos();
        angleUnWrap();
        om.updateIMU();

        om.telemetry.addData("COMPLETED Driving: ", step);
        om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                currentPos[0], currentPos[1], currentPos[2], currentPos[3]);
        om.telemetry.addData("Robot Heading: ", " Desired: %.2f, Actual: %.2f",targetAngle,robotHeading);
        om.telemetry.addData("Time: ", om.runtime);
        om.telemetry.update();
        //*************** MODIFIED TELEMETRY FOR DEBUGGING ************************************

    }

    public double calcSteeringPowerIMU(double angleWanted) {

        angleUnWrap();
        double steerPower = (angleWanted - robotHeading) * STEERING_POWER_GAIN;

        double clippedSteering = -1.0 * (Range.clip(steerPower, -STEERING_POWER_LIMIT, STEERING_POWER_LIMIT) );

        return clippedSteering;
    }

    public void calcDistanceIMU(int[] driveDirection, BasicAuto om) {
        int deltaPos[] = new int[4];
        int adjustedPos[] = new int[4];

        for(int i=0; i < 4; i++){

            deltaPos[i] = currentPos[i] - priorPos[i];
        }

        double rotationOffset = (deltaPos[0] + deltaPos[1] + deltaPos[2] + deltaPos[3] ) / 4;

        for(int i = 0; i < 4; i++){

            adjustedPos[i] = (int) Math.round(deltaPos[i] - rotationOffset);

        }

        double incrementalDistance = ((deltaPos[0] * driveDirection[0]) + (deltaPos[1] * driveDirection[1]) + (deltaPos[2] * driveDirection[2]) + (deltaPos[3] * driveDirection[3]) ) / 4;

        distanceTraveled += incrementalDistance / om.cons.DEGREES_TO_COUNTS / om.cons.ROBOT_INCH_TO_MOTOR_DEG;

        priorPos = currentPos;

        om.telemetry.addData("Motor Movement", "FL (%d) FR (%d) BR (%d) BL (%d)", deltaPos[0], deltaPos[1], deltaPos[2], deltaPos[3]);
        om.telemetry.addData("Robot Movement", "Incremental: (%.2f) Total: (%.2f)", incrementalDistance / om.cons.DEGREES_TO_COUNTS / om.cons.ROBOT_INCH_TO_MOTOR_DEG, distanceTraveled);
        om.telemetry.update();

    }

//    public void driveGeneral(HardwareBilly.moveDirection moveType, double distanceInch, double powerLimit, String step, BasicAuto om) {
//        int countDistance = 0;
//        int[] driveDirection = new int[4];
//        int[] startPos = new int[4];
//        boolean motorsDone = false;
//
//        switch(moveType) {
//
//            case FwdBack :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjForward) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = +1;// FR
//                driveDirection[2] = +1;// BR
//                driveDirection[3] = -1;// BL
//                break;
//
//            case RightLeft :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjRight) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = -1;// FR
//                driveDirection[2] = +1;// BR
//                driveDirection[3] = +1;// BL
//                break;
//
//            case Rotate :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjRotate) * om.cons.ROBOT_DEG_TO_WHEEL_INCH * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = -1;// FR
//                driveDirection[2] = -1;// BR
//                driveDirection[3] = -1;// BL
//                break;
//            default:
//                countDistance = (int) 0;
//
//                driveDirection[0] = 0;// FL
//                driveDirection[1] = 0;// FR
//                driveDirection[2] = 0;// BR
//                driveDirection[3] = 0;// BL
//        }
//
//        om.runtime.reset();
//        startPos = motorStartPos();
//        setMotorPower(powerLimit);
//
//        for (int i=0; i<4; i++) {
//
//            targetPos[i] = startPos[i] + (driveDirection[i] * countDistance);
//        }
//
//        frontLeft.setTargetPosition(targetPos[0]);
//        frontRight.setTargetPosition(targetPos[1]);
//        backRight.setTargetPosition(targetPos[2]);
//        backLeft.setTargetPosition(targetPos[3]);
//
//
//        while(!motorsDone && (om.opModeIsActive() || om.testModeActive)) {
//
//            motorsDone = targetPosTolerence();
//
//            om.telemetry.addData("Driving: ", step);
//            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                    frontLeft.getTargetPosition(), frontRight.getTargetPosition(),
//                    backRight.getTargetPosition(),backLeft.getTargetPosition());
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                    frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
//                    backRight.getCurrentPosition(), backLeft.getCurrentPosition());
//            om.telemetry.addData("Move Tolerance: ", MOVE_TOL);
//            om.telemetry.update();
//
//            om.idle();
//        }
//
//        setMotorPower(0);
//        frontLeft.setTargetPosition(frontLeft.getCurrentPosition());
//        frontRight.setTargetPosition(frontRight.getCurrentPosition());
//        backRight.setTargetPosition(backRight.getCurrentPosition());
//        backLeft.setTargetPosition(backLeft.getCurrentPosition());
//
//        om.telemetry.addData("Driving: ", step);
//        om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                frontLeft.getTargetPosition(), frontRight.getTargetPosition(),
//                backRight.getTargetPosition(),backLeft.getTargetPosition());
//        om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
//                backRight.getCurrentPosition(), backLeft.getCurrentPosition());
//        om.telemetry.addData("Move Tolerance: ", MOVE_TOL);
//        om.telemetry.addData("Time: ", om.runtime);
//
//    }
//
//    public void driveGeneralPower(HardwareBilly.moveDirection moveType, double distanceInch, double powerLimit, double powerMin, String step, BasicAuto om) {
//        int countDistance = 0;
//        int[] driveDirection = new int[4];
//        int[] startPos = new int[4];
//        boolean motorsDone = false;
//        double[] error = new double[4];
//        double[] setPower = new double[4];
//        double powerGain = POWER_GAIN;// 4 is inches from target when robot will start slowing down
//
//        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        switch(moveType) {
//
//            case FwdBack :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjForward) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = +1;// FR
//                driveDirection[2] = +1;// BR
//                driveDirection[3] = -1;// BL
//                break;
//
//            case RightLeft :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjRight) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = -1;// FR
//                driveDirection[2] = +1;// BR
//                driveDirection[3] = +1;// BL
//                break;
//
//            case Rotate :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjRotate) * om.cons.ROBOT_DEG_TO_WHEEL_INCH * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = -1;// FR
//                driveDirection[2] = -1;// BR
//                driveDirection[3] = -1;// BL
//                break;
//            default:
//                countDistance = (int) 0;
//
//                driveDirection[0] = 0;// FL
//                driveDirection[1] = 0;// FR
//                driveDirection[2] = 0;// BR
//                driveDirection[3] = 0;// BL
//        }
//
//        startPos = motorStartPos();
//
//        for (int i=0; i<4; i++) {
//
//            targetPos[i] = startPos[i] + (driveDirection[i] * countDistance);
//        }
//
//        error[0] = targetPos[0] - frontLeft.getCurrentPosition();
//        error[1] = targetPos[1] - frontRight.getCurrentPosition();
//        error[2] = targetPos[2] - backRight.getCurrentPosition();
//        error[3] = targetPos[3] - backLeft.getCurrentPosition();
//
////            prePower[0] = Math.abs(error[0]) * powerGain;
////            prePower[1] = Math.abs(error[1]) * powerGain;
////            prePower[2] = Math.abs(error[2]) * powerGain;
////            prePower[3] = Math.abs(error[3]) * powerGain;
////        prePower[0] = Range.clip(Math.abs(error[0]) * powerGain, powerMin, powerLimit);
////        prePower[1] = Range.clip(Math.abs(error[1]) * powerGain, powerMin, powerLimit);
////        prePower[2] = Range.clip(Math.abs(error[2]) * powerGain, powerMin, powerLimit);
////        prePower[3] = Range.clip(Math.abs(error[3]) * powerGain, powerMin, powerLimit);
//
//        for (int i=0; i<4; i++) {
//
//            setPower[i] = Range.clip(Math.abs(error[i]) * powerGain, powerMin, powerLimit) * Math.signum(error[i]);
//        }
//
//        om.runtime.reset();
//        setMotorPowerArray(setPower);
//
//        while(!motorsDone && (om.opModeIsActive() || om.testModeActive)) {
//
//            error[0] = targetPos[0] - frontLeft.getCurrentPosition();
//            error[1] = targetPos[1] - frontRight.getCurrentPosition();
//            error[2] = targetPos[2] - backRight.getCurrentPosition();
//            error[3] = targetPos[3] - backLeft.getCurrentPosition();
//
////            prePower[0] = Math.abs(error[0]) * powerGain;
////            prePower[1] = Math.abs(error[1]) * powerGain;
////            prePower[2] = Math.abs(error[2]) * powerGain;
////            prePower[3] = Math.abs(error[3]) * powerGain;
////            prePower[0] = Range.clip(Math.abs(error[0]) * powerGain, powerMin, powerLimit);
////            prePower[1] = Range.clip(Math.abs(error[1]) * powerGain, powerMin, powerLimit);
////            prePower[2] = Range.clip(Math.abs(error[2]) * powerGain, powerMin, powerLimit);
////            prePower[3] = Range.clip(Math.abs(error[3]) * powerGain, powerMin, powerLimit);
//
//            for (int i=0; i<4; i++) {
//
//                setPower[i] = Range.clip(Math.abs(error[i]) * powerGain, powerMin, powerLimit) * Math.signum(error[i]);
//            }
//
//            setMotorPowerArray(setPower);
//
//            om.telemetry.addData("Driving: ", step);
//            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                    targetPos[0], targetPos[1],targetPos[2],targetPos[3]);
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                    frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
//                    backRight.getCurrentPosition(), backLeft.getCurrentPosition());
//            om.telemetry.addData("Move Tolerance: ", MOVE_TOL);
//            om.telemetry.update();
//
//            motorsDone = targetPosTolerence();
//
//            om.idle();
//        }
//        setMotorPower(0);
//
//        om.telemetry.addData("Driving: ", step);
//        om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                targetPos[0], targetPos[1],targetPos[2],targetPos[3]);
//        om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
//                backRight.getCurrentPosition(), backLeft.getCurrentPosition());
//        om.telemetry.addData("Move Tolerance: ", MOVE_TOL);
//        om.telemetry.addData("Time: ", om.runtime);
//
////        frontLeft.setTargetPosition(frontLeft.getCurrentPosition());
////        frontRight.setTargetPosition(frontRight.getCurrentPosition());
////        backRight.setTargetPosition(backRight.getCurrentPosition());
////        backLeft.setTargetPosition(backLeft.getCurrentPosition());
////
////        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
////        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
////        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
////        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//
//
//    }
//
//    public void driveGeneralQuickStop(HardwareBilly.moveDirection moveType, double distanceInch, double powerLimit, String step, BasicAuto om) {
//        int countDistance = 0;
//        int[] driveDirection = new int[4];
//        int[] startPos = new int[4];
//        boolean motorsDone = false;
//
//        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        switch(moveType) {
//
//            case FwdBack :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjForward) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = +1;// FR
//                driveDirection[2] = +1;// BR
//                driveDirection[3] = -1;// BL
//                break;
//
//            case RightLeft :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjRight) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = -1;// FR
//                driveDirection[2] = +1;// BR
//                driveDirection[3] = +1;// BL
//                break;
//
//            case Rotate :
//                countDistance = (int) Math.round((distanceInch * om.cons.adjRotate) * om.cons.ROBOT_DEG_TO_WHEEL_INCH * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);
//
//                driveDirection[0] = -1;// FL
//                driveDirection[1] = -1;// FR
//                driveDirection[2] = -1;// BR
//                driveDirection[3] = -1;// BL
//                break;
//            default:
//                countDistance = (int) 0;
//
//                driveDirection[0] = 0;// FL
//                driveDirection[1] = 0;// FR
//                driveDirection[2] = 0;// BR
//                driveDirection[3] = 0;// BL
//        }
//
//        startPos = motorStartPos();
//
//        for (int i=0; i<4; i++) {
//
//            targetPos[i] = startPos[i] + (driveDirection[i] * countDistance);
//        }
//
//        setMotorPower(powerLimit);
//
//        while(!motorsDone && (om.opModeIsActive() || om.testModeActive)) {
//
//            motorsDone = targetPosTolerence();
//
//            om.telemetry.addData("Driving: ", step);
//            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                    frontLeft.getTargetPosition(), frontRight.getTargetPosition(),
//                    backRight.getTargetPosition(),backLeft.getTargetPosition());
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
//                    frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
//                    backRight.getCurrentPosition(), backLeft.getCurrentPosition());
//            om.telemetry.addData("Move Tolerance: ", MOVE_TOL);
//            om.telemetry.update();
//
//            om.idle();
//        }
//        setMotorPower(0);
//
//        frontLeft.setTargetPosition(frontLeft.getCurrentPosition());
//        frontRight.setTargetPosition(frontRight.getCurrentPosition());
//        backRight.setTargetPosition(backRight.getCurrentPosition());
//        backLeft.setTargetPosition(backLeft.getCurrentPosition());
//
//        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//    }

    public int[] motorStartPos() {

        int[] currentPos = new int[4];

        currentPos[0]= frontLeft.getCurrentPosition(); //FL
        currentPos[1]= frontRight.getCurrentPosition(); //FR
        currentPos[2]= backRight.getCurrentPosition(); //BR
        currentPos[3]= backLeft.getCurrentPosition(); //BL

        return currentPos;
    }

    public void setMotorPower(double power) {

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);

    }

    public void setMotorPowerArray(double[] power) {

        frontLeft.setPower(power[0]);
        frontRight.setPower(power[1]);
        backRight.setPower(power[2]);
        backLeft.setPower(power[3]);

    }

    public boolean targetPosTolerence() {

        int countTol = 0;
        Boolean motorFinish = false;
        int[] motorPos = new int[4];

        motorPos = motorStartPos();
        for (int i = 0; i < 4; i++) {

            if (MOVE_TOL >= Math.abs(motorPos[i] - targetPos[i])) {

                countTol += 1;

                if (countTol == 1) {// was 4 also 1

                    motorFinish = true;

                }
            }

        }

        return motorFinish;

    }

    public void moveJack(double height, double jackPowerLimit, String step, BasicOpMode om) {
//        int countDistance = (int) (om.cons.NUMBER_OF_JACK_STAGES * (om.cons.W0 - ( (Math.sqrt(Math.pow(om.cons.W0, 2) - Math.pow(om.DeltaH + om.cons.H0, 2)) / om.cons.MOTOR_DEG_TO_LEAD) * om.cons.DEGREES_TO_COUNTS) ) );
        int countDistance = (int) ( ((height / om.cons.MOTOR_DEG_TO_LEAD) * om.cons.DEGREES_TO_COUNTS) / om.cons.NUMBER_OF_JACK_STAGES);//!!!!!!!!!!!!!!!!!!!
//        int startPosL;
//        int startPosR;
        int jackZoneC = countDistance;// remove: = countDistance

//        startPosL = jack.getCurrentPosition();
//        startPosR = jackRight.getCurrentPosition();
//        jack.setPower(jackPowerLimit);
//        jack.setTargetPosition(countDistance);

//        jackZoneC = Math.abs(countDistance - jack.getCurrentPosition() );

        while((jackZoneC > MOVE_TOL) && (om.opModeIsActive() || om.testModeActive)) {

//            jackZoneC = Math.abs(countDistance - jack.getCurrentPosition() );

            om.telemetry.addData("Jack: ", step);
//            om.telemetry.addData("Motor Commands: ", "Jack Center (%d)", jack.getTargetPosition());
//            om.telemetry.addData("Motor Counts: ", "Jack Center (%d)", jack.getCurrentPosition());
            om.telemetry.addData("Move Tolerance: ", MOVE_TOL);
            om.telemetry.update();

            om.idle();
        }
//        jack.setPower(0);

        om.telemetry.addLine("Jack Motion Done");
    }

    public void driveRotateIMU(double angle, double powerLimit, String step, BasicAuto om) {

        double error;
        double steering;

        // "angle" = the added rotation angle from the current position
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        angleUnWrap();
        //Check tolerance zone to exit method
        while (Math.abs(angle - robotHeading) > om.cons.IMU_ROTATE_TOL && ((om.opModeIsActive() || om.testModeActive))) {

            error = angle - robotHeading;
            steering = Range.clip((error * om.cons.GAIN), -ROTATE_POWER_LIMIT, ROTATE_POWER_LIMIT);

            //update power limit
            frontLeft.setPower(powerLimit - steering);
            frontRight.setPower(powerLimit - steering);
            backLeft.setPower(powerLimit - steering);
            backRight.setPower(powerLimit - steering);

            angleUnWrap();

            om.telemetry.addData("Driving: %s", step);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
                    backLeft.getCurrentPosition(), backRight.getCurrentPosition());
            om.telemetry.addData("wanted angle","%.2f, heading %.2f ", angle, robotHeading);
            om.telemetry.update();

            om.idle();
        }

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        om.telemetry.addLine("Power set to zero");
        om.telemetry.update();

    }

    //TeleOp
    public void drivePower(Gamepad g1, Gamepad g2) {

        forwardDirection = (-g1.left_stick_y * Math.pow(g1.left_stick_y, 2) ) * TELEOP_DRIVE_POWER_LIMIT;
        rightDirection = (g1.right_stick_x * Math.pow(g1.right_stick_x, 2) ) * TELEOP_DRIVE_POWER_LIMIT;

        frontLeft.setPower(Range.clip(-forwardDirection - rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
        frontRight.setPower(Range.clip(forwardDirection - rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
        backRight.setPower(Range.clip(forwardDirection + rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
        backLeft.setPower(Range.clip(-forwardDirection + rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
    }

    public void rotatePower(Gamepad g1, Gamepad g2) {

        counterclockwiseDirection = (-g1.left_trigger * Math.pow(g1.left_trigger, 2) ) * TELEOP_ROTATE_POWER_LIMIT;
        clockwiseDirection = (g1.right_trigger * Math.pow(g1.right_trigger, 2) ) * TELEOP_ROTATE_POWER_LIMIT;

        clockwise = Range.clip(clockwiseDirection + counterclockwiseDirection, -TELEOP_ROTATE_POWER_LIMIT, TELEOP_ROTATE_POWER_LIMIT);

    }

    public void drivePowerAllLeftStick(Gamepad g1, Gamepad g2) {

        forwardDirection = (-g1.left_stick_y) * TELEOP_DRIVE_POWER_LIMIT;
        rightDirection = (g1.left_stick_x) * TELEOP_DRIVE_POWER_LIMIT;

        frontLeft.setPower(Range.clip(-forwardDirection - rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
        frontRight.setPower(Range.clip(forwardDirection - rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
        backRight.setPower(Range.clip(forwardDirection + rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
        backLeft.setPower(Range.clip(-forwardDirection + rightDirection - clockwise, -TELEOP_DRIVE_POWER_LIMIT, TELEOP_DRIVE_POWER_LIMIT));
    }

    public void rotatePowerRightStick(Gamepad g1, Gamepad g2) {

        clockwise = (g1.right_stick_x) * TELEOP_ROTATE_POWER_LIMIT;

        clockwise = Range.clip(clockwise, -TELEOP_ROTATE_POWER_LIMIT, TELEOP_ROTATE_POWER_LIMIT);

    }

    public void drivePowerAllLeftStickScaled(Gamepad g1, Gamepad g2) {

        forwardDirection = (-g1.left_stick_y) * TELEOP_DRIVE_POWER_LIMIT;
        rightDirection = (g1.left_stick_x) * TELEOP_DRIVE_POWER_LIMIT;

        clockwise = (g1.right_stick_x) * TELEOP_ROTATE_POWER_LIMIT;

        double maxPower = Math.abs(forwardDirection) + Math.abs(rightDirection) + Math.abs(clockwise);

        if (maxPower < TELEOP_DRIVE_POWER_LIMIT) {
            maxPower = TELEOP_DRIVE_POWER_LIMIT;
        }

        frontLeft.setPower((-(forwardDirection/maxPower) - (rightDirection/maxPower) - (clockwise/maxPower)) * TELEOP_DRIVE_POWER_LIMIT);
        frontRight.setPower(((forwardDirection/maxPower) - (rightDirection/maxPower) - (clockwise/maxPower)) * TELEOP_DRIVE_POWER_LIMIT);
        backRight.setPower(((forwardDirection/maxPower) + (rightDirection/maxPower) - (clockwise/maxPower)) * TELEOP_DRIVE_POWER_LIMIT);
        backLeft.setPower((-(forwardDirection/maxPower) + (rightDirection/maxPower) - (clockwise/maxPower)) * TELEOP_DRIVE_POWER_LIMIT);

    }

    public void jackPower(Gamepad g1, Gamepad g2) {

        verticalDirection = (-g2.left_stick_y * Math.pow(g2.left_stick_y, 2) ) * JACK_POWER_LIMIT;

        jack.setPower(Range.clip(verticalDirection, -JACK_POWER_LIMIT, JACK_POWER_LIMIT));

    }

    public void slidePower(Gamepad g1, Gamepad g2) {

        slideDirection = (g2.right_stick_y * Math.pow(g2.right_stick_y, 2) ) * SLIDE_POWER_LIMIT;

        slide.setPower(Range.clip(slideDirection, -SLIDE_POWER_LIMIT, SLIDE_POWER_LIMIT));

    }

    public void setServoPos(double servoPos) {

        stoneServoLeft.setPosition(servoPos);
        stoneServoRight.setPosition(servoPos);

    }

}