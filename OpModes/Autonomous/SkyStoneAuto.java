package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Skystone_14999.DriveMotion.DriveMethods;

@Autonomous(name="SkyStone Auto", group="Autonomous")

public class SkyStoneAuto extends BasicAuto {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        runtime.reset();

        fwdToStone();

        findSkyStone();

        telemetry.addLine("OpMode Complete");
        sleep(2000);
    }
}
