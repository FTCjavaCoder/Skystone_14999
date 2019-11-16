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

        pickCase(autoChoice.SkyStoneInside);

        telemetry.addLine("OpMode Complete");
        sleep(2000);
    }

    public void pickCase(autoChoice autoOptions) {

        switch(autoOptions) {

            case SkyStoneInside :


                break;
            case SkyStoneOutside :


                break;

            case SkyStoneInsideUnmoved :


                break;

            case SkyStoneOutsideUnmoved :


                break;

        }
    }
}
