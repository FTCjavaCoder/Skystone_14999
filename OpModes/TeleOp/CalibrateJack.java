package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 *
 */

@TeleOp(name="CalibrateJack", group="TeleOp")

public class CalibrateJack extends BasicTeleOp {

    @Override
    public void runOpMode() {

        initializeTeleOp();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            Billy.jack.setPower(Range.clip(-gamepad2.left_stick_y * Math.pow(gamepad2.left_stick_y, 2), -0.15, 0.15));
//            Billy.jackRight.setPower(Range.clip(-gamepad2.right_stick_y * Math.pow(gamepad2.left_stick_y, 2), -0.15, 0.15));

            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Commands Jack", "Vertical (%.2f)", verticalDirection);
            telemetry.addData("Jack Motors", "Jack Left (%.2f), Jack Right (%.2f)",
                    Billy.jack.getPower()/*, Billy.jackRight.getPower()*/);
            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}