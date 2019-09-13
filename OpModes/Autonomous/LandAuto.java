package OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


/**
 *
**/

//This is autonomous program under "Test" group, Groups can be used to sort programs that are mature and those in work
//Rename the OpMode to be unique for this program / challenge
@Autonomous(name="LandAuto", group="Autonomous")
//This is a Linear OpMode meaning the OpMode itself will not loop upon completion
public class LandAuto extends BasicAuto { // All Classes are StartWithCapital

    @Override
    //Begin running OpMode after INIT is selected on gamepad
    public void runOpMode() throws InterruptedException {
        // Map all of hardware to program from robot using HardwareVincent script
        //Will refer all defined configuration items - motors, servos, sensors- to this defined Billy class

        initialize();

        hangAngle();

        //Complete initialization and telemetry
        //**** PUSHING START BUTTON WILL RUN AUTONOMOUS CODE ******
        waitForStart();

        runtime.reset(); //reset counter to start with OpMode

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU

        start = runtime.time();

        offset = Billy.angles.firstAngle; //Determine initial angle offset 
        priorAngle = offset; //set prior angle for unwrap to be initial angle 
        robotHeading = Billy.angles.firstAngle - offset; //robotHeading to be 0 degrees to start 

        while (runtime.time() - start < 0.1) {
            //wait for initial angle data capture prior to running code
        }

        // landing and becoming free from bracket

        landing();

        forwardPosition = (int) Math.round(8 * prm.ROBOT_INCH_TO_MOTOR_DEG) * prm.DEGREES_TO_COUNTS;
        drv.driveFwdRev(forwardPosition, prm.DRIVE_POWER_LIMIT, "Go 8 inches forward",this);

    } //MAIN OpMode PROGRAM END

    
}