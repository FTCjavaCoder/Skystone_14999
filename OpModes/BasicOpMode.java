package Skystone_14999.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import Skystone_14999.HarwareConfig.HardwareBilly;
import Skystone_14999.Parameters.Constants;

public class BasicOpMode extends LinearOpMode {

    public HardwareBilly Billy = new HardwareBilly();// call using Billy.(for hardware or angle unwrap method)
    public Constants cons = new Constants();// call using cons.(constant DRIVE_POWER_LIMIT etc.)

    public double priorAngle = 0;
    public double robotHeading = 0;
    public boolean fileWasRead = true;
    public String hashMapFile = "HashMapFile.txt";

    public double DeltaH = 0;

    public BasicOpMode() {

    }

    @Override
    public void runOpMode() {

    }

    public void pressAToContinue() {

        telemetry.addLine("**********************");
        telemetry.addLine("Press A to continue");
        telemetry.update();
        while (!gamepad1.a && opModeIsActive()) {

            idle();
        }
        sleep(300);
    }

    public void angleUnWrap() {

        double deltaAngle;

        Billy.angles = Billy.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);//This line calls the angles from the IMU
        deltaAngle = -(Billy.angles.firstAngle - priorAngle);// Determine how much the angle has changed since we last checked teh angle
        if (deltaAngle > 180) {//This is IF/THEN for the unwrap routine
            robotHeading += deltaAngle - 360;//Decrease angle for negative direction //rotation
        } else if (deltaAngle < -180) {
            robotHeading += deltaAngle + 360;//increase angle for positive direction //rotation 
        } else {
            robotHeading += deltaAngle;//No wrap happened, don't add any extra rotation
        }
        priorAngle = Billy.angles.firstAngle;//Update the latest measurement to be //priorAngle for the next time we call the method

    }

    public void readOrWriteHashMap() {

        cons.readFromPhone(hashMapFile, this);
        telemetry.addData("Existing File Was Read?","%s", fileWasRead);

        if (!fileWasRead) {

            cons.defineParameters();
            cons.writeToPhone(hashMapFile, this);

            cons.readFromPhone(hashMapFile, this);
            telemetry.addData("Created File, File Was Read?","%s", fileWasRead);
        }
    }

}