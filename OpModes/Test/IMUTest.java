package OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import OpModes.Autonomous.BasicAuto;

/** IMU auto test program
 *  
**/

@Autonomous(name="IMUTest", group="Test")
@Disabled
public class IMUTest extends BasicAuto { // All Classes are StartWithCapital

    @Override
    //Begin running OpMode after INIT is selected on gamepad
    public void runOpMode() throws InterruptedException {

        //!! INITIALIZATION DO NOT REMOVE !!
        initialize();

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Complete initialization and telemetry
        //**** PUSHING START BUTTON WILL RUN AUTONOMOUS CODE ******
        waitForStart();

        runtime.reset(); //reset counter to start with OpMode

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU

        offset = Billy.angles.firstAngle; //Determine initial angle offset 
        priorAngle = offset; //set prior angle for unwrap to be initial angle 
        robotHeading = Billy.angles.firstAngle - offset; //robotHeading to be 0 degrees to start 

        sleep(100);

        adjustVariables(testDistFwd, 1, "fwd/rev move distance");

        forwardPosition = (int) Math.round(testDistFwd * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRevIMU(forwardPosition, prm.DRIVE_POWER_LIMIT, 0,"Go right/left",this);

        adjustVariables(testAngleClock, 5, "clockwise angle");

        drv.driveRotateIMU(testAngleClock, prm.ROTATE_POWER_LIMIT, "Rotating",this);

        adjustVariables(testDistRight, 1, "right/left move distance");

        rightPosition = (int) Math.round((testDistRight * prm.adjustedRight) * prm.ROBOT_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS);
        drv.driveRightLeftIMU(rightPosition, prm.DRIVE_POWER_LIMIT, 90,"Go right/left",this);


        } //MAIN OpMode PROGRAM END

}