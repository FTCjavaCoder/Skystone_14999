package Skystone_14999.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import Skystone_14999.OpModes.TeleOp.BasicTeleOp;


@TeleOp(name="Servo Test", group="TeleOp")
@Disabled
public class TestTeleOpMethods extends BasicTeleOp {

    public double StoneArmPosBlue = 0.5;
    public double StoneArmPosRed = 0.5;
    public double CapstoneArmPos = 0.5;
    public double StoneArmPosIncrement = 0.05;

    @Override
    public void runOpMode() {

            initializeTeleOp();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();
            runtime.reset();

        while (opModeIsActive()) {

            if (gamepad1.b) {

                StoneArmPosRed += StoneArmPosIncrement;
                TestServoRed(StoneArmPosRed);
                sleep(250);
            }
            if (gamepad1.x) {

                StoneArmPosRed -= StoneArmPosIncrement;
                TestServoRed(StoneArmPosRed);
                sleep(250);
            }
//            if (gamepad1.y) {
//
//                StoneArmPosBlue += StoneArmPosIncrement;
//                TestServoBlue(StoneArmPosBlue);
//                sleep(250);
//            }
//            if (gamepad1.a) {
//
//                StoneArmPosBlue -= StoneArmPosIncrement;
//                TestServoBlue(StoneArmPosBlue);
//                sleep(250);
//            }
//            if (gamepad1.y) {
//
//                CapstoneArmPos += StoneArmPosIncrement;
//                TestServoCapstone(CapstoneArmPos);
//                sleep(250);
//            }
//            if (gamepad1.a) {
//
//                CapstoneArmPos -= StoneArmPosIncrement;
//                TestServoCapstone(CapstoneArmPos);
//                sleep(250);
//            }

//            telemetry.addLine("For Capstone servo Y to increase position and A to decrease position");
//            telemetry.addData("Servo Variable", "Capstone Command (%.2f)", CapstoneArmPos);
//            telemetry.addData("Servo Position", "Servo Capstone (%.2f)", Billy.servoCapstoneRelease.getPosition());
            telemetry.addLine("For Blue servo Y to increase position and A to decrease position");
            telemetry.addLine("For Red servo B to increase position and X to decrease position");
            telemetry.addData("Servo Variables", "Blue Command (%.2f), Red Command (%.2f)",
                    StoneArmPosBlue, StoneArmPosRed);
            telemetry.addData("Servo Positions", "Servo Blue (%.2f), Servo Red (%.2f)",
                    Billy.armServoBlue.getPosition(), Billy.armServoRed.getPosition());
            telemetry.update();
        }
    }

    public void TestServoRed(double armPos) {

        Billy.armServoRed.setPosition(armPos);
    }

    public void TestServoBlue(double armPos) {

        Billy.armServoBlue.setPosition(armPos);
    }

    public void TestServoCapstone(double armPos) {

        Billy.servoCapstoneRelease.setPosition(armPos);

    }

}