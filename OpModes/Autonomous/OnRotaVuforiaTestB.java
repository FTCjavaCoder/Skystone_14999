package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@Autonomous(name="Rotating Early On Vuforia Test Blue", group="Vuforia")
//@Disabled
public class OnRotaVuforiaTestB extends BasicAuto {

    @Override
    public void runOpMode() {

        telemetry.addLine("NOT READY DON'T PRESS PLAY");
        telemetry.update();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = cons.VUFORIA_KEY;
        parameters.cameraDirection   = cons.CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");
        //all above lines need to be all autonomous OpMode's runOpMode before initialization

        foundationPosChange = 0;// 0 for moved, 26 for unmoved Foundation.
        insideOutside = 0;// 0 for Inside, 24 for Outside
        sideColor = 1;// + for Blue, - for Red, KEEP RED

        initializeMiniBot();

        targetsSkyStone.activate();

        waitForStart();

        runtime.reset();

        Billy.initIMU(this);

        fwdToTwoStone();

        vuforiaStoneLIRotate();

//        goToStone();
//
//        takeStone1();
//
//        getStone2();
//
//        takeStone2();
//
//        twoStonePark();

        telemetry.addData("stoneYLocation","(%.2f)", stoneYLocation);
        telemetry.addLine("OpMode Complete");
        telemetry.update();
        sleep(4000);
    }
}
