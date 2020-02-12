package Skystone_14999.OpModes.Autonomous.Foundation;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import Skystone_14999.HarwareConfig.HardwareBilly;
import Skystone_14999.OpModes.Autonomous.BasicAuto;

@Autonomous(name="Delay Park Red", group="Park")

public class DelayParkRed extends BasicAuto {

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

        sideColor = 1;
        foundationInOut = 22;// 0 for Inside, 22 for Outside

        initialize();

        waitForStart();

        runtime.reset();

        Billy.initIMU(this);

        sleep((long)(cons.delayForPark * 1000));// 20 seconds base

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.FwdBack,2, 0,"Forward 2 inches",this);

        Billy.IMUDriveFwdRight(HardwareBilly.moveDirection.RightLeft,-16, 0,"Left 16 inches",this);

        telemetry.addLine("OpMode Complete");
        telemetry.update();
        sleep(2000);
    }
}
