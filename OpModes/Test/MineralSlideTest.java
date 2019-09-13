package OpModes.Test;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import OpModes.TeleOp.BasicTeleOp;

/**
 *
 */

@TeleOp(name="MineralSlide", group="TeleOp")
@Disabled
public class MineralSlideTest extends BasicTeleOp {
    CRServo servoSweeper = null;
    Servo mineralBox = null;
    DcMotor slideExtend = null;
    DcMotor slideRotate = null;

    @Override
    public void runOpMode() throws InterruptedException {

        servoSweeper = hardwareMap.get(CRServo.class, "sweeper_servo");
        mineralBox = hardwareMap.get(Servo.class, "mineral_box_servo");
        slideExtend = hardwareMap.get(DcMotor.class, "motor_slide_extend");
        slideRotate = hardwareMap.get(DcMotor.class, "motor_slide_rotate");
//
//        Billy.servoSweeper.setDirection(DcMotorSimple.Direction.FORWARD);
        slideExtend.setDirection(DcMotorSimple.Direction.FORWARD);
        slideRotate.setDirection(DcMotorSimple.Direction.FORWARD);

        slideExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideRotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slideExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slideExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideExtend.setPower(prm.LINEAR_SLIDE_POWER_LIMIT);
        slideRotate.setPower(prm.LINEAR_SLIDE_POWER_LIMIT);

        slideExtend.setTargetPosition(slideExtend.getCurrentPosition());
        slideRotate.setTargetPosition(slideRotate.getCurrentPosition());

        telemetry.addLine("initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad2.dpad_up){

                servoSweeper.setPower(1);
            }
            if (gamepad2.dpad_down){

                servoSweeper.setPower(-1);
            }

            if (gamepad2.dpad_left) {

                servoSweeper.setPower(0);
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

            mineralBox.setPosition(mineralBoxTrgtPos);

            slideRotate.setTargetPosition((int) (desiredRotate * prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));
            slideExtend.setTargetPosition((int) (desiredExtend * prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));


            // Show the elapsed time, wheel power, arm motor speed, arm positions, and servo positions.
            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Slide Rotate Target and Current Pos","%.2f, %.2f", slideRotate.getTargetPosition() / (prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS), slideRotate.getCurrentPosition() / (prm.SLIDE_ROTATE_DEG_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));
            telemetry.addData("Slide Extend Target and Current Pos","%.2f, %.2f", slideExtend.getTargetPosition() / (prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS), slideExtend.getCurrentPosition() / (prm.SLIDE_EXTEND_INCH_TO_MOTOR_DEG * prm.DEGREES_TO_COUNTS));
            telemetry.addData("Box Servo Target Pos","%.4f", mineralBoxTrgtPos);
            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}