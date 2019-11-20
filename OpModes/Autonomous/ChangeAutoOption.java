package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Skystone_14999.OpModes.TeleOp.BasicTeleOp;

    @TeleOp(name="ChangeAutoOption", group="Autonomous")

public class ChangeAutoOption extends BasicTeleOp {

    @Override
    public void runOpMode() {

        telemetry.addLine("Status: Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        cons.readFromPhoneAO(hashMapFile, this);
        telemetry.addData("File Was Read?","%s", fileWasRead);
        pressAToContinue();

        if (!fileWasRead) {

            cons.defineAutoOptions();
            cons.writeToPhoneAO(hashMapFile, this);

            cons.readFromPhoneAO(hashMapFile, this);
            telemetry.addData("Created File, file Was Read?","%s", fileWasRead);
        }
        pressAToContinue();

        cons.editHashMapAO(this);

        cons.writeToPhoneAO(hashMapFile,this);

        telemetry.addLine("Auto OpMode selection complete");
        telemetry.update();
        sleep(1000);
    }
}