package Skystone_14999.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import Skystone_14999.DriveMotion.DriveMethods;
import Skystone_14999.OpModes.Autonomous.BasicAuto;

@Autonomous(name="Auto Driving Test", group="Test")

public class AutoDrivingTest extends BasicAuto {

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

//        foundationPosChange = 0;// 0 for moved, 26 for unmoved Foundation.
//        insideOutside = 0;// 0 for Inside, 24 for Outside

        initialize();

        waitForStart();

        runtime.reset();

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU

        offset = Billy.angles.firstAngle; //Determine initial angle offset 
        priorAngle = offset; //set prior angle for unwrap to be initial angle 
        robotHeading = Billy.angles.firstAngle - offset; //robotHeading to be 0 degrees to start 

        sleep(100);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, 60, cons.pHM.get("drivePowerLimit").value,"Forward 60 Inches", this);

        pressAToContinue();

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, 36, cons.pHM.get("drivePowerLimit").value,"Right 36 Inches", this);

        pressAToContinue();

        drv.driveGeneralPower(DriveMethods.moveDirection.FwdBack, 60, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Forward 60 Inches", this);

        pressAToContinue();

        drv.driveGeneralPower(DriveMethods.moveDirection.RightLeft, 36, cons.pHM.get("drivePowerLimit").value, cons.pHM.get("drivePowerMinimum").value, "Right 36 Inches", this);

        pressAToContinue();

        telemetry.addLine("OpMode Complete");
        telemetry.update();
        sleep(2000);
    }
}
