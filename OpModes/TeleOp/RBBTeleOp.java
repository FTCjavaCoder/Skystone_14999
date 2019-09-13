package OpModes.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 *
 */

@TeleOp(name="RBBTeleOp", group="TeleOp")
@Disabled
public class RBBTeleOp extends BasicTeleOp {
    
    @Override
    public void runOpMode() throws InterruptedException {

        initializeRBB();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Set Drive Motor Power
            forwardDirection = -gamepad1.left_stick_y * Math.pow(gamepad1.left_stick_y, 2);
            rightDirection = gamepad1.right_stick_x * Math.pow(gamepad1.right_stick_x, 2);

            if (gamepad1.right_bumper && gamepad1.left_bumper)
                clockwise = 0;
            else if (gamepad1.right_bumper)  //boolean gamepad1.right_bumper is evaluated for "true" or "false" to determine if pressed
                clockwise = prm.TURN_POWER;
            else if (gamepad1.left_bumper)
                clockwise = -prm.TURN_POWER;
            else
                clockwise = 0;

            if (gamepad2.x) {

                BillyTele.servoMarker.setPosition(prm.stowServoMarker);
            }

            if (gamepad2.y) {

                mineralBoxTrgtPos += prm.BOX_SERVO_INCREMENT;
                mineralBoxTrgtPos = Range.clip(mineralBoxTrgtPos, 0, 1);

            }

            if (gamepad2.a) {

                mineralBoxTrgtPos -= prm.BOX_SERVO_INCREMENT;
                mineralBoxTrgtPos = Range.clip(mineralBoxTrgtPos, 0, 1);
            }

            if (gamepad2.left_bumper) {

                desiredExtend += prm.LINEAR_SLIDE_EXTEND_INCREMENT;
                desiredExtend = Range.clip(desiredExtend, prm.LINEAR_SLIDE_EXTEND_MIN, prm.LINEAR_SLIDE_EXTEND_MAX);
            }

            if (gamepad2.right_bumper) {

                desiredExtend -= prm.LINEAR_SLIDE_EXTEND_INCREMENT;
                desiredExtend = Range.clip(desiredExtend, prm.LINEAR_SLIDE_EXTEND_MIN, prm.LINEAR_SLIDE_EXTEND_MAX);
            }

            // move stick forward to move the arm down
            if (gamepad2.left_stick_y < -prm.STICK_DEADZONE) {

                desiredRotate += prm.LINEAR_SLIDE_ROTATE_INCREMENT;
                desiredRotate = Range.clip(desiredRotate, prm.LINEAR_SLIDE_ROTATE_MIN, prm.LINEAR_SLIDE_ROTATE_MAX);
            }

            // move stick backward to move the arm up
            if (gamepad2.left_stick_y > prm.STICK_DEADZONE) {

                desiredRotate -= prm.LINEAR_SLIDE_ROTATE_INCREMENT;
                desiredRotate = Range.clip(desiredRotate, prm.LINEAR_SLIDE_ROTATE_MIN, prm.LINEAR_SLIDE_ROTATE_MAX);
            }

//            // sets the slide to...
//            if (gamepad2.dpad_left) {
//
//                desiredExtend = prm.DESEXT_TEMP1;
//                desiredRotate = prm.DESROT_TEMP1;
//                mineralBoxTrgtPos = prm.MINBOXTRGTPOS_TEMP1;
//
//            }
//
//            // sets the slide to...
//            if (gamepad2.dpad_right) {
//
//                desiredExtend = prm.DESEXT_TEMP2;
//                desiredRotate = prm.DESROT_TEMP2;
//                mineralBoxTrgtPos = prm.MINBOXTRGTPOS_TEMP2;
//
//            }

            BillyTele.servoMineralBox.setPosition(mineralBoxTrgtPos);

            BillyTele.slideRotate.setTargetPosition((int) (desiredRotate * prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));
            BillyTele.slideExtend.setTargetPosition((int) (desiredExtend * prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));

            BillyTele.landingSlide.setPower(Range.clip(- (gamepad2.right_stick_y), -prm.SLIDE_POWER_LIMIT, prm.SLIDE_POWER_LIMIT)); // moving slide
//          BillyTele.mineralDumperL.setPosition(servosMineralPos);
//          BillyTele.mineralDumperR.setPosition(1-servosMineralPos);
            BillyTele.frontLeft.setPower(Range.clip(-forwardDirection - rightDirection - clockwise, -prm.TELEOP_DRIVE_POWER_LIMIT, prm.TELEOP_DRIVE_POWER_LIMIT));
            BillyTele.frontRight.setPower(Range.clip(forwardDirection - rightDirection - clockwise, -prm.TELEOP_DRIVE_POWER_LIMIT, prm.TELEOP_DRIVE_POWER_LIMIT));
            BillyTele.backLeft.setPower(Range.clip(-forwardDirection + rightDirection - clockwise, -prm.TELEOP_DRIVE_POWER_LIMIT, prm.TELEOP_DRIVE_POWER_LIMIT));
            BillyTele.backRight.setPower(Range.clip(forwardDirection + rightDirection - clockwise, -prm.TELEOP_DRIVE_POWER_LIMIT, prm.TELEOP_DRIVE_POWER_LIMIT));


            // Show the elapsed time, wheel power, arm motor speed, arm positions, and servo positions.
            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)",
                    forwardDirection, rightDirection, clockwise);

            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    BillyTele.frontLeft.getPower(), BillyTele.frontRight.getPower(), BillyTele.backLeft.getPower(),
                    BillyTele.backRight.getPower());

            telemetry.addData("Slide Rotate Target and Current Pos","%.2f, %.2f",
                    BillyTele.slideRotate.getTargetPosition() / (prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS),
                    BillyTele.slideRotate.getCurrentPosition() / (prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));

            telemetry.addData("Slide Extend Target and Current Pos","%.2f, %.2f",
                    BillyTele.slideExtend.getTargetPosition() / (prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS),
                    BillyTele.slideExtend.getCurrentPosition() / (prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));

            telemetry.addData("Box Servo Target Pos","%.4f", mineralBoxTrgtPos);

            telemetry.addData("Lead Screw", "screwepower (%.2f), screwposition (%.2f)",
                    BillyTele.landingSlide.getPower(),
                    BillyTele.landingSlide.getCurrentPosition()/prm.DEGREES_TO_COUNTS/prm.SLIDE_INCH_TO_MOTOR_DEG);
            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}