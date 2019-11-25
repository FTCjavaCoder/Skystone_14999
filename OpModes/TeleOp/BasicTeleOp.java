package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import Skystone_14999.DriveMotion.DriveMethods;
import Skystone_14999.HarwareConfig.HardwareBilly;
import Skystone_14999.OpModes.BasicOpMode;
import Skystone_14999.Parameters.Constants;

@Autonomous(name="BasicTeleOp", group="TeleOp")
@Disabled
public class BasicTeleOp extends BasicOpMode {

    public DriveMethods drv = new DriveMethods();

    // Define the static power levels and limits for motors
    public double servoStonePos;
    public double clockwise =0;
    public double forwardDirection =0;
    public double rightDirection =0;
    public double verticalDirection = 0;
    public double clockwiseDirection =0;
    public double counterclockwiseDirection = 0;
    public double slideDirection =0;
    public int slideDistance = 0;
    public double desiredExtend = 0;
    public double desiredRotate = 0;
    public double mineralBoxTrgtPos = 0;
    public double slidePwr = 0;

    public ElapsedTime runtime = new ElapsedTime(); //create a counter for elapsed time

    @Override
    public void runOpMode() {

    }


    public void initializeTeleOp() {

        // initialize configuration to Billy
        Billy.init(hardwareMap);
        // define variables for OpMode powers and positions
        // Initialize all powers and variables to zero

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);
        Billy.jackLeft.setPower(0);
        Billy.jackRight.setPower(0);
        Billy.slide.setPower(0);

        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.jackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.jackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.slide.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.jackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.jackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.jackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.jackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
 //       Billy.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.jackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.jackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Billy.stoneServoLeft.setPosition(Billy.stoneServoLeft.getPosition());
        Billy.stoneServoRight.setPosition(Billy.stoneServoRight.getPosition());
        Billy.servoFoundationL.setPosition(0.10);
        Billy.servoFoundationR.setPosition(0.90);

        readOrWriteHashMap();

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)", forwardDirection, rightDirection, clockwise);
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

    }

    public void drivePower() {

        forwardDirection = -gamepad1.left_stick_y * Math.pow(gamepad1.left_stick_y, 2);
        rightDirection = gamepad1.right_stick_x * Math.pow(gamepad1.right_stick_x, 2);

        Billy.frontLeft.setPower(Range.clip(-forwardDirection - rightDirection - clockwise, -cons.pHM.get("teleOpDrivePowerLimit").value, cons.pHM.get("teleOpDrivePowerLimit").value));
        Billy.frontRight.setPower(Range.clip(forwardDirection - rightDirection - clockwise, -cons.pHM.get("teleOpDrivePowerLimit").value, cons.pHM.get("teleOpDrivePowerLimit").value));
        Billy.backRight.setPower(Range.clip(forwardDirection + rightDirection - clockwise, -cons.pHM.get("teleOpDrivePowerLimit").value, cons.pHM.get("teleOpDrivePowerLimit").value));
        Billy.backLeft.setPower(Range.clip(-forwardDirection + rightDirection - clockwise, -cons.pHM.get("teleOpDrivePowerLimit").value, cons.pHM.get("teleOpDrivePowerLimit").value));
    }

    public void rotatePower() {

        counterclockwiseDirection = -gamepad1.left_trigger * Math.pow(gamepad1.left_trigger, 2);
        clockwiseDirection = gamepad1.right_trigger * Math.pow(gamepad1.right_trigger, 2);

        clockwise = Range.clip(clockwiseDirection + counterclockwiseDirection, -cons.TURN_POWER, cons.TURN_POWER);

    }

    public void jackPower() {

        verticalDirection = -gamepad2.left_stick_y * Math.pow(gamepad2.left_stick_y, 2);

        Billy.jackLeft.setPower(Range.clip(verticalDirection, -cons.pHM.get("jackPowerLimit").value, cons.pHM.get("jackPowerLimit").value));
        Billy.jackRight.setPower(Range.clip(verticalDirection, -cons.pHM.get("jackPowerLimit").value, cons.pHM.get("jackPowerLimit").value));

    }

    public void slidePower() {

        slideDirection = gamepad2.right_stick_y * Math.pow(gamepad2.right_stick_y, 2);

        Billy.slide.setPower(Range.clip(slideDirection, -cons.pHM.get("slidePowerLimit").value, cons.pHM.get("slidePowerLimit").value));

    }

    public void setServoPos(double servoPos) {

        Billy.stoneServoLeft.setPosition(servoPos);
        Billy.stoneServoRight.setPosition(servoPos);

    }

}