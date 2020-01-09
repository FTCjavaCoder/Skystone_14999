package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 *
 */

@TeleOp(name="MiniDrive Scaled Stick Control", group="TeleOp")

public class MiniDrive_Scaled_StickControl extends BasicTeleOp {

    @Override
    public void runOpMode() {

        telemetry.addLine("NOT READY DON'T PRESS PLAY");
        telemetry.update();

        initializeTeleOpMiniBot();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Billy.initIMU(this);

        Billy.armServoBlue.setPosition(0);
        Billy.armServoRed.setPosition(1);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Set Drive Motor Power
            Billy.drivePowerAllLeftStickScaled(gamepad1, gamepad2);

            Billy.angleUnWrap();

            if (gamepad1.dpad_up) {

                telemetryOption += 1;

                if (telemetryOption > 4) {

                    telemetryOption = 0;
                }

            }

            if(Billy.jackStopSensor.isPressed()){
                telemetry.addLine("TouchSensor Pressed");
            }
            if(Billy.jackStopSensor.getValue() == 0){
                telemetry.addLine("TouchSensor Equals 0");
            }
            if(Billy.jackStopSensor.getValue() == 1){
                telemetry.addLine("TouchSensor Equals 1");
            }
            if(Billy.jackStopSensor.getValue() > 0){
                telemetry.addLine("TouchSensor > 0");
            }
            if(Billy.jackStopSensor.getValue() < 1){
                telemetry.addLine("TouchSensor < 1");
            }

//            multiTelemetry(telemetryOption);

            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Touch Sensor", "%s", Billy.jackStopSensor.isPressed());
            telemetry.addData("Touch Sensor Value", "%.2f", Billy.jackStopSensor.getValue());
            telemetry.addData("Robot Heading", "( %.2f )", Billy.robotHeading);
            telemetry.addData("Commands Drive", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)",
                    forwardDirection, rightDirection, clockwise);
            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(),
                    Billy.backRight.getPower());
            telemetry.update();

            idle();
        }
        Billy.setMotorPower(0);

    } // main opMode end methods go below

}