package Skystone_14999.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Skystone_14999.Parameters.Constants;
import Skystone_14999.OpModes.TeleOp.BasicTeleOp;


@TeleOp(name="ServoTest", group="TeleOp")
@Disabled
public class TestTeleOpMethods extends BasicTeleOp {

    public Constants prm = new Constants();

    public double StoneGrabPos;
    boolean positive;
    @Override
    public void runOpMode() {

            initializeTeleOp();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();
            runtime.reset();

        while (opModeIsActive()) {

            if (gamepad1.b) {
                positive = true;
                TestServo(positive);
                sleep(250);
            }
            if (gamepad1.x) {
                positive = false;
                TestServo(positive);
                sleep(250);
            }
        }
    }

    public void TestServo(boolean positive) {

            if (positive){
                StoneGrabPos += .05;
            }
            if (!positive){
                StoneGrabPos -= .05;
            }

//        Billy.servoStoneGrab.setPosition(StoneGrabPos);
    }
}