package Skystone_14999.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Skystone_14999.HarwareConfig.HardwareBilly;
import Skystone_14999.Parameters.Constants;

public class BasicOpMode extends LinearOpMode {

    public HardwareBilly Billy = new HardwareBilly();// call using Billy.(for hardware or angle unwrap method)
    public Constants cons = new Constants();// call using cons.(constant DRIVE_POWER_LIMIT etc.)

    public boolean fileWasRead = true;
    public String hashMapFile = "HashMapFile.txt";
    public String autoOptionFile = "AutoOptionFile.txt";
    public int selected = 0;

    public double DeltaH = 0;
    public double currentH = 0;

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

    public void readOrWriteHashMap() {

        cons.readFromPhone(hashMapFile, this);
        telemetry.addData("Existing File Was Read?","%s", fileWasRead);

        if (!fileWasRead) {

            cons.defineParameters();
            cons.writeToPhone(hashMapFile, this);

            cons.readFromPhone(hashMapFile, this);
            telemetry.addData("Created File, File Was Read?","%s", fileWasRead);

        }

        cons.initParameters();
    }

//    public void readOrWriteHashMapAO() {
//
//        cons.readFromPhoneAO(hashMapFile, this);
//        telemetry.addData("Existing File Was Read?","%s", fileWasRead);
//
//        if (!fileWasRead) {
//
//            cons.defineAutoOptions();
//            cons.writeToPhoneAO(hashMapFile, this);
//
//            cons.readFromPhoneAO(hashMapFile, this);
//            telemetry.addData("Created File, File Was Read?","%s", fileWasRead);
//        }
//    }

}