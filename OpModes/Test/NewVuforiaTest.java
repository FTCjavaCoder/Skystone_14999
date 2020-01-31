package Skystone_14999.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

import Skystone_14999.OpModes.Autonomous.BasicAuto;

@Autonomous(name="New Vuforia Test", group="Test")
//@Disabled
public class NewVuforiaTest extends BasicAuto {

    @Override
    public void runOpMode() {

        while(!gamepad1.x) {

            if(gamepad1.a) {

                cons.CAMERA_CHOICE = VuforiaLocalizer.CameraDirection.BACK;
            }

            if(gamepad1.b) {

                cons.CAMERA_CHOICE = VuforiaLocalizer.CameraDirection.FRONT;
            }

            telemetry.addData("Current camera", cons.CAMERA_CHOICE);
            telemetry.addLine("Press A to select BACK and B to select FRONT");
            telemetry.addLine("Press X to exit");
            telemetry.update();
        }

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

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
//        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
//        blueRearBridge.setName("Blue Rear Bridge");
//        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
//        redRearBridge.setName("Red Rear Bridge");
//        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
//        redFrontBridge.setName("Red Front Bridge");
//        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
//        blueFrontBridge.setName("Blue Front Bridge");
//        VuforiaTrackable red1 = targetsSkyStone.get(5);
//        red1.setName("Red Perimeter 1");
//        VuforiaTrackable red2 = targetsSkyStone.get(6);
//        red2.setName("Red Perimeter 2");
//        VuforiaTrackable front1 = targetsSkyStone.get(7);
//        front1.setName("Front Perimeter 1");
//        VuforiaTrackable front2 = targetsSkyStone.get(8);
//        front2.setName("Front Perimeter 2");
//        VuforiaTrackable blue1 = targetsSkyStone.get(9);
//        blue1.setName("Blue Perimeter 1");
//        VuforiaTrackable blue2 = targetsSkyStone.get(10);
//        blue2.setName("Blue Perimeter 2");
//        VuforiaTrackable rear1 = targetsSkyStone.get(11);
//        rear1.setName("Rear Perimeter 1");
//        VuforiaTrackable rear2 = targetsSkyStone.get(12);
//        rear2.setName("Rear Perimeter 2");

//        allTrackables.addAll(targetsSkyStone);
        allTrackables.add(targetsSkyStone.get(0));

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

        waitForStart();

        runtime.reset();

        while(opModeIsActive()) {

            vuforiaStoneIdentifyLoop();// press dpad up to exit

            while(!gamepad1.dpad_right && opModeIsActive()) {

                telemetry.addLine("Press Y for autonomous method and dpad right to go back to looped vuforia");
                telemetry.update();

                if(gamepad1.y) {

                    vuforiaStoneLocateInches();

                    telemetry.update();

                    sleep(3000);
                }
            }

        }

        telemetry.addLine("OpMode Complete");

        telemetry.update();
    }
}
