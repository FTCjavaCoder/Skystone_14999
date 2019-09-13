package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import Skystone_14999.DriveMotion.DriveMethods;
import Skystone_14999.HarwareConfig.HardwareBillyTeleOp;
import Skystone_14999.I_Parameters.Parameters;

@Autonomous(name="BasicTeleOp", group="TeleOp")
@Disabled
public class BasicTeleOp extends LinearOpMode {

    public HardwareBillyTeleOp BillyTele = new HardwareBillyTeleOp();
    public Parameters prm = new Parameters();
    public DriveMethods drv = new DriveMethods();

    // Define the static power levels and limits for motors
    public double servosMineralPos;
    public double clockwise =0;
    public double forwardDirection =0;
    public double rightDirection =0;
    public int slideDistance = 0;
    public double desiredExtend = 0;
    public double desiredRotate = 0;
    public double mineralBoxTrgtPos = 0;

    public ElapsedTime runtime = new ElapsedTime(); //create a counter for elapsed time

    @Override
    public void runOpMode() throws InterruptedException {

    }

    public void initializeTeleOp() {

        // initialize configuration to BillyTele
        BillyTele.init(hardwareMap);
        // define variables for OpMode powers and positions
        // Initialize all powers and variables to zero

        BillyTele.frontLeft.setPower(0);
        BillyTele.frontRight.setPower(0);
        BillyTele.backLeft.setPower(0);
        BillyTele.backRight.setPower(0);
        BillyTele.landingSlide.setPower(0);

        BillyTele.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.landingSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.slideExtend.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.slideRotate.setDirection(DcMotorSimple.Direction.FORWARD);

        BillyTele.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.landingSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.slideExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.slideRotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        BillyTele.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.landingSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.slideExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.slideRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        BillyTele.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.landingSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.slideExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BillyTele.slideRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        BillyTele.slideExtend.setPower(prm.SLIDE_EXTEND_POWER_LIMIT);
        BillyTele.slideRotate.setPower(prm.SLIDE_ROTATE_POWER_LIMIT);

        BillyTele.slideExtend.setTargetPosition(BillyTele.slideExtend.getCurrentPosition());
        BillyTele.slideRotate.setTargetPosition(BillyTele.slideRotate.getCurrentPosition());

        BillyTele.servoMineralBox.setPosition(0);

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)", forwardDirection, rightDirection, clockwise);
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", BillyTele.frontLeft.getPower(), BillyTele.frontRight.getPower(), BillyTele.backLeft.getPower(), BillyTele.backRight.getPower());
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

    }

    public void initializeRBB() {

        // initialize configuration to BillyTele
        BillyTele.init(hardwareMap);
        // define variables for OpMode powers and positions
        // Initialize all powers and variables to zero

        BillyTele.frontLeft.setPower(0);
        BillyTele.frontRight.setPower(0);
        BillyTele.backLeft.setPower(0);
        BillyTele.backRight.setPower(0);
        BillyTele.landingSlide.setPower(0);

        BillyTele.frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.landingSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.slideExtend.setDirection(DcMotorSimple.Direction.FORWARD);
        BillyTele.slideRotate.setDirection(DcMotorSimple.Direction.FORWARD);

        BillyTele.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.landingSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.slideExtend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BillyTele.slideRotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reset all motor encoders
        BillyTele.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.landingSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.slideExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BillyTele.slideRotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        BillyTele.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.landingSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BillyTele.slideExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BillyTele.slideRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        BillyTele.slideExtend.setPower(prm.SLIDE_EXTEND_POWER_LIMIT);
        BillyTele.slideRotate.setPower(prm.SLIDE_ROTATE_POWER_LIMIT);

        BillyTele.slideExtend.setTargetPosition(BillyTele.slideExtend.getCurrentPosition());
        BillyTele.slideRotate.setTargetPosition(BillyTele.slideRotate.getCurrentPosition());

        BillyTele.servoMineralBox.setPosition(0);

        //Indicate initialization complete and provide telemetry
        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("Commands", "Forward (%.2f), Right (%.2f), Clockwise (%.2f)", forwardDirection, rightDirection, clockwise);
        telemetry.addData("Drive Motors", "FL (%.2f), FR (%.2f), BL (%.2f), BR (%.2f)", BillyTele.frontLeft.getPower(), BillyTele.frontRight.getPower(), BillyTele.backLeft.getPower(), BillyTele.backRight.getPower());
        telemetry.addData(">", "Press Play to start");
        telemetry.update();

    }

    public void slideMove(int distance, double powerLimit, String step) {

        int lsZone;
        int lsStart;

        lsStart = BillyTele.landingSlide.getCurrentPosition();
        BillyTele.landingSlide.setPower(powerLimit);
        BillyTele.landingSlide.setTargetPosition(lsStart + distance);

        lsZone = (int) Math.abs (BillyTele.landingSlide.getCurrentPosition() - (lsStart + distance)); // need to confirm sign before distance

        while (lsZone > prm.SLIDE_TOL && opModeIsActive()) {
            lsZone = (int) Math.abs (BillyTele.landingSlide.getCurrentPosition() - (lsStart + distance)); // need to confirm sign before distance

            telemetry.addData("Slide: ", step);
            telemetry.addData("Motor Counts: ", "SlideArm CurrentPos (%d), Command (%d)",
                    BillyTele.landingSlide.getCurrentPosition(), lsStart + distance);

            telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    BillyTele.frontLeft.getCurrentPosition(), BillyTele.frontRight.getCurrentPosition(),
                    BillyTele.backLeft.getCurrentPosition(), BillyTele.backRight.getCurrentPosition());
            telemetry.update();

            idle();
        }
        BillyTele.landingSlide.setPower(0);

    }

}