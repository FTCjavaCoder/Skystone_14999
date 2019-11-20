
package Skystone_14999.HarwareConfig;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/*
* REVISION HISTORY
* made this the hardware for "Billy" with everything for lead screw slide and mineral collecting arm
*/
public class HardwareBilly
{
    /* Public OpMode members. */
    public DcMotor  frontLeft        = null;
    public DcMotor  frontRight       = null;
    public DcMotor  backLeft         = null;
    public DcMotor  backRight        = null;
    public DcMotor  jackLeft     = null;
    public DcMotor  jackRight = null;
    public DcMotor slide = null;

//    public DcMotor  slideRotate = null;
//    public Servo    servoStoneGrab  = null;
//    public Servo servoPrototype = null;
//    public CRServo    servoSweeper   = null;
//    public ColorSensor    colorSensorSampling   = null;
    public BNO055IMU    imu = null;
    public HardwareMap hwMap           =  null;
    public ElapsedTime period  = new ElapsedTime();


//    public DcMotor  mineralCollector = null;
      public Servo  stoneServoLeft   = null;
      public Servo  stoneServoRight   = null;
//    public Servo    mineralDumperR   = null;
//    public ColorSensor    colorSensorTeam   = null;
//    public TouchSensor    touchSensor1   = null;
//    public TouchSensor    touchSensor2   = null;

    /* local OpMode members. */
    public Orientation angles;

    /* Constructor */
    public HardwareBilly(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        frontLeft  = hwMap.get(DcMotor.class, "motor_fl");
        frontRight = hwMap.get(DcMotor.class, "motor_fr");
        backLeft  = hwMap.get(DcMotor.class, "motor_bl");
        backRight = hwMap.get(DcMotor.class, "motor_br");
        jackLeft  = hwMap.get(DcMotor.class, "motor_jack_left");
        jackRight = hwMap.get(DcMotor.class, "motor_jack_right");
        slide = hwMap.get(DcMotor.class, "motor_slide");

        stoneServoLeft = hwMap.get(Servo.class, "stone_servo_left");
        stoneServoRight = hwMap.get(Servo.class, "stone_servo_right");
        //foundation servo left
        //foundation servo right

        //        slideRotate = hwMap.get(DcMotor.class, "motor_slide_rotate");
//        mineralCollector = hwMap.get(DcMotor.class, "motor_collect");

        // Define and initialize ALL installed servos.
//        servoStoneGrab = hwMap.get(Servo.class, "StoneGrab_servo");
//        servoPrototype = hwMap.get(Servo.class, "marker_servo");

//        mineralDumperR = hwMap.get(Servo.class, "mineral_servo_right");

        //Define all installed sensors
//        colorSensorSampling  = hwMap.get(ColorSensor.class, "color_sensor1");
//        colorSensorTeam  = hwMap.get(ColorSensor.class, "color_sensor2");
//        touchSensor1  = hwMap.get(TouchSensor.class, "touch_1");
//        touchSensor2  = hwMap.get(TouchSensor.class, "touch_2");

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.

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
    }


}