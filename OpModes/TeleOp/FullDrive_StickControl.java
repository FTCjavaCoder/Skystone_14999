package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 *
 */

@TeleOp(name="FullDrive Stick Control", group="TeleOp")

public class FullDrive_StickControl extends BasicTeleOp {

    @Override
    public void runOpMode() {

        initializeTeleOp();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU

        offset = Billy.angles.firstAngle; //Determine initial angle offset 
        priorAngle = offset; //set prior angle for unwrap to be initial angle 
        robotHeading = Billy.angles.firstAngle - offset; //robotHeading to be 0 degrees to start 

        sleep(100);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Set Drive Motor Power
            drivePowerAllLeftStick();

            // use the left/right triggers on gamepad1 to rotate the robot counter/clockwise
            rotatePowerRightStick();

            // use the left stick on gamepad2 to raise/lower the jack
            jackPower();

            // use the right stick on gamepad2 to extend/retract the slide
            slidePower();

            //
            if (gamepad2.right_bumper) {

                servoStonePos = Billy.stoneServoRight.getPosition() + 0.05;
                Range.clip(servoStonePos, 0.15, 1);
                setServoPos(servoStonePos);
                sleep(300);
            }

            if (gamepad2.left_bumper) {

                servoStonePos = Billy.stoneServoRight.getPosition() - 0.05;
                Range.clip(servoStonePos, 0.15, 1);
                setServoPos(servoStonePos);
                sleep(300);
            }

            // dpad up/down to grab the foundation


//            // sets the position of the servos to 4"
//            if (gamepad2.x) {
//                servoStonePos = 0.94;
//                setServoPos(servoStonePos);
//                sleep(300);
//            }

            // sets the position of the servos to 8"
            if (gamepad2.a) {
                servoStonePos = 0.5;
                setServoPos(servoStonePos);
                sleep(300);
            }

            // sets the position of the servos to open
            if (gamepad2.b) {
                servoStonePos = 0.15;
                setServoPos(servoStonePos);
                sleep(300);
            }

            if (gamepad2.dpad_up) {

                jackLeft();
            }

            // SERVOS FOUNDATION
            if(gamepad1.dpad_up) {

                Billy.servoFoundationL.setPosition(0.10);

                Billy.servoFoundationR.setPosition(0.90);
            }
            if(gamepad1.dpad_down) {

                Billy.servoFoundationL.setPosition(0.80);

                Billy.servoFoundationR.setPosition(0.20);
            }

            if (gamepad1.right_bumper && gamepad1.b) {

                Billy.servoCapstoneRelease.setPosition(0);
            }

            angleUnWrap();

            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Robot Heading", "( %.2f )", robotHeading);
            telemetry.addData("Slide Pos", "Slide (%d)", Billy.slide.getCurrentPosition());
            telemetry.addData("Slide TargetPos", "Slide (%d)", Billy.slide.getTargetPosition());
            telemetry.addData("Slide Power", "Slide (%.2f)", Billy.slide.getPower());
            telemetry.addData("Servos", "F Servo Left (%.2f), F Servo Right (%.2f)",
                    Billy.servoFoundationL.getPosition(), Billy.servoFoundationR.getPosition());
            telemetry.addData("Commands Drive", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)",
                    forwardDirection, rightDirection, clockwise);
            telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                    Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(),
                    Billy.backRight.getPower());
            telemetry.addData("Jack Pos", "L (%d), R (%d)", Billy.jackLeft.getCurrentPosition(), Billy.jackRight.getCurrentPosition());
            telemetry.addData("Jack TargetPos", "L (%d), R (%d)", Billy.jackLeft.getTargetPosition(), Billy.jackRight.getTargetPosition());
            telemetry.addData("Jack Power Cmd", "Vertical (%.2f)", verticalDirection);
//            telemetry.addData("Jack Target Height", "Height (%.2f)", DeltaH);
//            telemetry.addData("Jack Current Height", "Height (%.2f)", currentHeight);
            telemetry.addData("Jack Motors", "Jack Left (%.2f), Jack Right (%.2f)",
                    Billy.jackLeft.getPower(), Billy.jackRight.getPower());

            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}