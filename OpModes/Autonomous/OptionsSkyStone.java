package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@Autonomous(name="OptionsSkyStone", group="Autonomous")
@Disabled
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

            case 0 :

                foundationPosChange = 0;// 0 for moved, 26 for unmoved Foundation.
                insideOutside = 0;// 0 for Inside, 24 for Outside

                fwdToStone();

                findSkyStone();

                bridgeCrossSkyStone();

                parkSkyStone();

                break;

            case 1 :

                foundationPosChange = 0;
                insideOutside = 24;// 0 for Inside, 24 for Outside

                fwdToStone();

                findSkyStone();

                bridgeCrossSkyStone();

                parkSkyStone();

                break;

            case 2 :

                foundationPosChange = 26;
                insideOutside = 0;// 0 for Inside, 24 for Outside

                fwdToStone();

                findSkyStone();

                bridgeCrossSkyStone();

                parkSkyStone();

                break;

            case 3 :

                foundationPosChange = 26;
                insideOutside = 24;// 0 for Inside, 24 for Outside

                fwdToStone();

                findSkyStone();

                bridgeCrossSkyStone();

                parkSkyStone();

                break;

            case 4 :
                foundationInOut = 0;

                grabFoundation();

                pullFoundation();

                aroundFoundation();

                pushFoundation();

                awayFromFoundation();

                break;

            case 5 :

                foundationInOut = 26;

                grabFoundation();

                pullFoundation();

                aroundFoundation();

                pushFoundation();

                awayFromFoundation();

                break;

        }

        telemetry.addLine("OpMode Complete");
        sleep(2000);
    }

}
