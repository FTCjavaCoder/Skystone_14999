package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 *
 */

@TeleOp(name="MiniDrive Stick Control", group="TeleOp")

public class MiniDrive_StickControl extends BasicTeleOp {

    @Override
    public void runOpMode() {

        initializeTeleOpMiniBot();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Billy.initIMU(this);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Set Drive Motor Power
            Billy.drivePowerAllLeftStick(gamepad1, gamepad2);

            // use the left/right triggers on gamepad1 to rotate the robot counter/clockwise
            Billy.rotatePowerRightStick(gamepad1, gamepad2);

            Billy.angleUnWrap();

            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Robot Heading", "( %.2f )", Billy.robotHeading);
            telemetry.addData("Commands Drive", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)",
                    forwardDirection, rightDirection, clockwise);
            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(),
                    Billy.backRight.getPower());
            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}