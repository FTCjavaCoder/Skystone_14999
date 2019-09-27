package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import Skystone_14999.HarwareConfig.HardwareBilly;

/**
 *
 */

@TeleOp(name=" BasicDrive", group="TeleOp")

public class BasicDrive extends BasicTeleOp {

    double localPower = prm.ROTATE_POWER_LIMIT;

    @Override
    public void runOpMode() throws InterruptedException {

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
                clockwise = prm.TURN_POWER;
            else if (gamepad1.left_bumper)
                clockwise = -prm.TURN_POWER;
            else
                clockwise = 0;

//            if (gamepad2.dpad_up){
//
//                Billy.servoSweeper.setPower(1);
//            }
//            if (gamepad2.dpad_down){
//
//                Billy.servoSweeper.setPower(-1);
//            }
//
//            if (gamepad2.dpad_left) {
//
//                Billy.servoSweeper.setPower(0);
//            }

            //set motor power



            // Show the elapsed time, wheel power, arm motor speed, arm positions, and servo positions.
            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)",
                    forwardDirection, rightDirection, clockwise);

            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(),
                    Billy.backRight.getPower());

//            telemetry.addData("Slide Rotate Target and Current Pos","%.2f, %.2f",
//                    Billy.slideRotate.getTargetPosition() / (prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS),
//                    Billy.slideRotate.getCurrentPosition() / (prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));
//
//            telemetry.addData("Slide Extend Target and Current Pos","%.2f, %.2f",
//                    Billy.slideExtend.getTargetPosition() / (prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS),
//                    Billy.slideExtend.getCurrentPosition() / (prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));
//
//            telemetry.addData("Box Servo Target Pos","%.4f", mineralBoxTrgtPos);
//
//            telemetry.addData("Lead Screw", "screwepower (%.2f), screwposition (%.2f)",
//                    Billy.landingSlide.getPower(),
//                    Billy.landingSlide.getCurrentPosition()/prm.DEGREES_TO_COUNTS/prm.SLIDE_INCH_TO_MOTOR_DEG);
            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}