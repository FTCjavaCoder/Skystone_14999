package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="EditHashMapParameters", group="TeleOp")

public class EditHashMapParameters extends BasicTeleOp {

    @Override
    public void runOpMode() {

        telemetry.addLine("Status: Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        cons.readFromPhone(hashMapFile, this);
        telemetry.addData("File Was Read?","%s", fileWasRead);
        pressAToContinue();

        if (!fileWasRead) {

            cons.defineParameters();
            cons.writeToPhone(hashMapFile, this);

            cons.readFromPhone(hashMapFile, this);
            telemetry.addData("Created File, file Was Read?","%s", fileWasRead);
        }
        pressAToContinue();

        cons.editHashMap(this);

        telemetry.addLine("HashMap editing complete");
        telemetry.update();
        sleep(1000);
    }
}