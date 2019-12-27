package Skystone_14999.OpModes.Autonomous.SkyStone;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import Skystone_14999.OpModes.Autonomous.BasicAuto;

@Autonomous(name="SkyStone and Foundation Outside", group="Autonomous")
@Disabled
public class SkyStoneFoundationOutside extends BasicAuto {

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

        foundationPosChange = 18;// 0 for moved, 18 for unmoved Foundation. (WAS 26 THEN 22)
        insideOutside = 20;// 0 for Inside, 20 for Outside (WAS 24)

        initialize();

        waitForStart();

        runtime.reset();

        Billy.initIMU(this);

//        fwdToStone();
//
//        findSkyStone();
//
//        bridgeCrossSkyStoneF();
//
//        grabAndRotateFoundation();
//
//        straightToCorner();
//
//        backSkyStoneAndFoundation();
//
//        parkSkyStoneF();

        telemetry.addLine("OpMode Complete");
        sleep(1000);
    }
}
