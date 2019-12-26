package Skystone_14999.OpModes.Autonomous.SkyStone;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import Skystone_14999.OpModes.Autonomous.BasicAuto;

@Autonomous(name="SkyStone Outside Unmoved", group="Skystone")
@Disabled
public class SkyStoneOutsideUnmoved extends BasicAuto {

    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = cons.VUFORIA_KEY;
        parameters.cameraDirection   = cons.CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");
        //all above lines need to be all autonomous OpMode's runOpMode before initialization

        foundationPosChange = 26;// 0 for moved, 26 for unmoved Foundation.
        insideOutside = 24;// 0 for Inside, 24 for Outside

        initialize();

        waitForStart();

        runtime.reset();

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU

        Billy.offset = Billy.angles.firstAngle; //Determine initial angle offset 
        Billy.priorAngle = Billy.offset; //set prior angle for unwrap to be initial angle 
        Billy.robotHeading = Billy.angles.firstAngle - Billy.offset; //robotHeading to be 0 degrees to start 

        sleep(100);

        fwdToStone();

        findSkyStone();

        bridgeCrossSkyStone();

        parkSkyStone();

        telemetry.addLine("OpMode Complete");
        sleep(2000);
    }
}
