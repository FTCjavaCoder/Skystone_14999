package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import Skystone_14999.HarwareConfig.HardwareBilly;

/**
 *
 */

@TeleOp(name=" BasicDrive", group="TeleOp")

public class BasicDrive extends BasicTeleOp {

    @Override
    public void runOpMode() {

        initializeTeleOp();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Set Drive Motor Power
               drivePower();

            if (gamepad1.right_bumper && gamepad1.left_bumper)
                clockwise = 0;
            else if (gamepad1.right_bumper)  //boolean gamepad1.right_bumper is evaluated for "true" or "false" to determine if pressed
                clockwise = cons.TURN_POWER;
            else if (gamepad1.left_bumper)
                clockwise = -cons.TURN_POWER;
            else
                clockwise = 0;

            // Show the elapsed time, wheel power, arm motor speed, arm positions, and servo positions.
            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)",
                    forwardDirection, rightDirection, clockwise);

            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(),
                    Billy.backRight.getPower());
            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}