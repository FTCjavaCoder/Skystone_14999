package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@Autonomous(name="OptionsSkyStone", group="Autonomous")

public class OptionsSkyStone extends BasicAuto {

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

        initialize();

        waitForStart();

        runtime.reset();

        switch(selected) {

            case "skyStoneOutside" :
                foundationPosChange = 0;

                fwdToStone();

                findSkyStone();

                bridgeCrossInside();

                parkInside();

                break;

            case "skyStoneInside" :
                foundationPosChange = 0;

                fwdToStone();

                findSkyStone();

                bridgeCrossOutside();

                parkOutside();

                break;

            case "skyStoneOutsideUnmoved" :
                foundationPosChange = 26;

                fwdToStone();

                findSkyStone();

                bridgeCrossInside();

                parkInside();

                break;

            case "skyStoneInsideUnmoved" :
                foundationPosChange = 26;

                fwdToStone();

                findSkyStone();

                bridgeCrossOutside();

                parkOutside();

                break;

            case "foundationOutside" :

                grabFoundation();

                pullFoundation();

                aroundFoundation();

                pushFoundation();

                awayFromFoundationInside();

                break;

            case "foundationInside" :

                grabFoundation();

                pullFoundation();

                aroundFoundation();

                pushFoundation();

                awayFromFoundationOutside();

                break;

        }

        telemetry.addLine("OpMode Complete");
        sleep(2000);
    }

}
