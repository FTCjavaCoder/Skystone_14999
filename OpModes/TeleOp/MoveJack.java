package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import Skystone_14999.HarwareConfig.HardwareJack;


/**
 *
 */

@TeleOp(name="MoveJack", group="TeleOp")
@Disabled
public class MoveJack extends LinearOpMode {

    public HardwareJack Jack = new HardwareJack();

    public double verticalDirection = 0;

    public ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        Jack.init(hardwareMap);

        Jack.jackLeft.setPower(0);
        Jack.jackRight.setPower(0);

        Jack.jackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Jack.jackRight.setDirection(DcMotorSimple.Direction.FORWARD);

        Jack.jackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Jack.jackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Jack.jackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Jack.jackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Jack.jackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Jack.jackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Commands", "Vertical (%.2f)", verticalDirection);
        telemetry.addData("Drive Motors", "Jack L (%.2f), Jack R (%.2f)", Jack.jackLeft.getPower(), Jack.jackRight.getPower());
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Set Drive Motor Power
            verticalDirection = -gamepad2.left_stick_y * Math.pow(gamepad2.left_stick_y, 2);

            Jack.jackLeft.setPower(Range.clip(verticalDirection,-0.25,0.25));
            Jack.jackRight.setPower(Range.clip(verticalDirection,-0.25,0.25));


            telemetry.addData("Status", "Run Time: ",runtime.toString());
            telemetry.addData("Commands", "Vertical (%.2f)", verticalDirection);
            telemetry.addData("Jack Motors", "Jack L (%.2f), Jack R (%.2f)",
                    Jack.jackLeft.getPower(), Jack.jackRight.getPower());
            telemetry.update();

            idle();
        }

    } // main opMode end methods go below

}