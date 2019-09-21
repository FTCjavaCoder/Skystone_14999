package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="DriveGeneral", group="Autonomous")

public class DriveGeneralTest extends BasicAuto {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        runtime.reset();

        drv.driveGeneral("Forward", 24, prm.DRIVE_POWER_LIMIT, "Back 10 inches",this);
        sleep(1000);
        drv.driveGeneral("Right", -24, prm.DRIVE_POWER_LIMIT, "Left 10 inches",this);
        sleep(1000);
        drv.driveGeneral("Rotate", 90, prm.DRIVE_POWER_LIMIT, "Clockwise 90 degrees",this);
        sleep(1000);
    }
}
