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

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, 24,cons.pHM.get("drivePowerLimit").value, "Forward 24 inches", this);

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, -24,cons.pHM.get("drivePowerLimit").value, "Backward 24 inches", this);

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, 24,cons.pHM.get("drivePowerLimit").value, "Right 24 inches", this);

        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, -24,cons.pHM.get("drivePowerLimit").value, "Left 24 inches", this);

        telemetry.addLine("Methods Done, Starting Power Only Motion");
        telemetry.update();
        sleep(1000);

        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Billy.frontLeft.setPower(-cons.pHM.get("drivePowerLimit").value);
        Billy.frontRight.setPower(cons.pHM.get("drivePowerLimit").value);
        Billy.backRight.setPower(cons.pHM.get("drivePowerLimit").value);
        Billy.backLeft.setPower(-cons.pHM.get("drivePowerLimit").value);

        sleep(1500);

        drv.setMotorPower(0, this);

        sleep(500);

        Billy.frontLeft.setPower(cons.pHM.get("drivePowerLimit").value);
        Billy.frontRight.setPower(-cons.pHM.get("drivePowerLimit").value);
        Billy.backRight.setPower(-cons.pHM.get("drivePowerLimit").value);
        Billy.backLeft.setPower(cons.pHM.get("drivePowerLimit").value);

        sleep(1500);

        drv.setMotorPower(0, this);

        sleep(500);

        Billy.frontLeft.setPower(-cons.pHM.get("drivePowerLimit").value);
        Billy.frontRight.setPower(-cons.pHM.get("drivePowerLimit").value);
        Billy.backRight.setPower(cons.pHM.get("drivePowerLimit").value);
        Billy.backLeft.setPower(cons.pHM.get("drivePowerLimit").value);

        sleep(1500);

        drv.setMotorPower(0, this);

        sleep(500);

        Billy.frontLeft.setPower(cons.pHM.get("drivePowerLimit").value);
        Billy.frontRight.setPower(cons.pHM.get("drivePowerLimit").value);
        Billy.backRight.setPower(-cons.pHM.get("drivePowerLimit").value);
        Billy.backLeft.setPower(-cons.pHM.get("drivePowerLimit").value);

        sleep(1500);

        drv.setMotorPower(0, this);

        sleep(500);

        telemetry.addLine("OpMode Complete");
        telemetry.update();
        sleep(2000);
    }
}
