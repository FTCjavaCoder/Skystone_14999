package Skystone_14999.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import Skystone_14999.DriveMotion.DriveMethods;
import Skystone_14999.OpModes.Autonomous.BasicAuto;

@Autonomous(name="JackMoveTest", group="Autonomous")

public class JackMoveTest extends BasicAuto {

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

        drv.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Up 1 Inch",this);

    }
}
