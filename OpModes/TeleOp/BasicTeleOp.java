package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
//import TestOpModesOffline.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import Skystone_14999.OpModes.BasicOpMode;

@Autonomous(name="BasicTeleOp", group="TeleOp")
@Disabled
public class BasicTeleOp extends BasicOpMode {

    // Define the static power levels and limits for motors
    public double servoStonePos;
    public double clockwise =0;
    public double forwardDirection =0;
    public double rightDirection =0;
    public double verticalDirection = 0;
    public double clockwiseDirection =0;
    public double counterclockwiseDirection = 0;
    public double slideDirection =0;
    public double rightTrigger = 0;
    public double leftTrigger = 0;
    public int slideDistance = 0;
    public double desiredExtend = 0;
    public double desiredRotate = 0;
    public double mineralBoxTrgtPos = 0;
    public double slidePwr = 0;
    public double setJackHeightPos = 3;

    public double capstoneServoPosition = 0;

    public int telemetryOption = 1;

    public ElapsedTime runtime = new ElapsedTime(); //create a counter for elapsed time

    @Override
    public void runOpMode() {

    }

    public void initializeTeleOp() {

        telemetry.addLine("NOT READY DON'T PRESS PLAY");
        telemetry.update();

        telemetry.addData(">", "Press Play to start");

        readOrWriteHashMap();

        // initialize configuration to Billy
        Billy.init(hardwareMap, testModeActive);
        // define variables for OpMode powers and positions
        // Initialize all powers and variables to zero

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);
        Billy.jack.setPower(0);
        Billy.slide.setPower(0);

        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.jack.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.slide.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.jack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.jack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.jack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Billy.stoneServoLeft.setPosition(Billy.stoneServoLeft.getPosition());
        Billy.stoneServoRight.setPosition(Billy.stoneServoRight.getPosition());
        Billy.servoFoundationL.setPosition(0.80);
        Billy.servoFoundationR.setPosition(0.20);

        Billy.armServoBlue.setPosition(Billy.stoneArmInitBlue);
        Billy.armServoRed.setPosition(Billy.stoneArmInitRed);
        Billy.rackServoBlue.setPosition(Billy.rackInitBlue);
        Billy.rackServoRed.setPosition(Billy.rackInitRed);

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)", forwardDirection, rightDirection, clockwise);
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

    }

    public void initializeTeleOpMiniBot() {

        telemetry.addLine("NOT READY DON'T PRESS PLAY");
        telemetry.update();

        telemetry.addData(">", "Press Play to start");

        readOrWriteHashMap();

        // initialize configuration to Billy
        Billy.initMiniBot(hardwareMap, testModeActive);
        // define variables for OpMode powers and positions
        // Initialize all powers and variables to zero

        Billy.frontLeft.setPower(0);
        Billy.frontRight.setPower(0);
        Billy.backLeft.setPower(0);
        Billy.backRight.setPower(0);

        Billy.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        Billy.backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        Billy.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Billy.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        Billy.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)", forwardDirection, rightDirection, clockwise);
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(), Billy.backRight.getPower());
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

    }

    public void stopMotors() {

        Billy.setMotorPower(0);
        Billy.jack.setPower(0);
        Billy.slide.setPower(0);
    }

    public void multiTelemetry(int option) {

        switch (option) {

            case 1 :
            // Motors
                telemetry.addData("Commands Drive", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)",
                        forwardDirection, rightDirection, clockwise);
                telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)",
                        Billy.frontLeft.getPower(), Billy.frontRight.getPower(), Billy.backLeft.getPower(),
                        Billy.backRight.getPower());

            case 2 :
            // Jack and Slide
                telemetry.addData("Jack Pos", "Center (%d)", Billy.jack.getCurrentPosition());
                telemetry.addData("Jack TargetPos", "Center (%d)", Billy.jack.getTargetPosition());
                telemetry.addData("Jack Power Cmd", "Vertical (%.2f)", verticalDirection);
                telemetry.addData("Jack Motors", "Jack Center (%.2f)", Billy.jack.getPower());
                telemetry.addData("Slide Pos", "Slide (%d)", Billy.slide.getCurrentPosition());
                telemetry.addData("Slide TargetPos", "Slide (%d)", Billy.slide.getTargetPosition());
                telemetry.addData("Slide Power", "Slide (%.2f)", Billy.slide.getPower());

            case 3 :
            // Servos
                telemetry.addData("Foundation", "Servo Left (%.2f), Servo Right (%.2f)",
                        Billy.servoFoundationL.getPosition(), Billy.servoFoundationR.getPosition());
                telemetry.addData("Gripper", "Servo Left (%.2f), Servo Right (%.2f)",
                        Billy.stoneServoLeft.getPosition(), Billy.stoneServoRight.getPosition());
                telemetry.addData("Autonomous", "Servo Blue (%.2f), Servo Red (%.2f)",
                        Billy.armServoBlue.getPosition(), Billy.armServoRed.getPosition());

            case 4 :
            // Status and Heading
                telemetry.addData("Status", "Run Time: ",runtime.toString());
                telemetry.addData("Robot Heading", "( %.2f )", Billy.robotHeading);

        }
        telemetry.update();
    }
}