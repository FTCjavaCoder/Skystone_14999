package OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/** Marker autonomous using TensorFlow
 *  
**/

@Autonomous(name="FullAutoDepot", group="Autonomous")

public class FullAutoDepot extends BasicAuto { // All Classes are StartWithCapital

    @Override
    //Begin running OpMode after INIT is selected on gamepad
    public void runOpMode() throws InterruptedException {

        //!! INITIALIZATION DO NOT REMOVE !!
        initialize();

        hangAngle();

        //Complete initialization and telemetry
        //**** PUSHING START BUTTON WILL RUN AUTONOMOUS CODE ******
        waitForStart();

        runtime.reset(); //reset counter to start with OpMode

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU

        offset = Billy.angles.firstAngle; //Determine initial angle offset 
        priorAngle = offset; //set prior angle for unwrap to be initial angle 
        robotHeading = Billy.angles.firstAngle - offset; //robotHeading to be 0 degrees to start 

        sleep(100);

        // landing and becoming free from bracket

        landing();

        scoreSamplingDepot();

        scoreMarker();

        depotToCrater();// must use full scoreMarker


    } //MAIN OpMode PROGRAM END

}