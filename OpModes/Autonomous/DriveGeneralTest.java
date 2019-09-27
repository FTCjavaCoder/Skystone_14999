package Skystone_14999.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Skystone_14999.DriveMotion.DriveMethods;

@Autonomous(name="DriveGeneral", group="Autonomous")

public class DriveGeneralTest extends BasicAuto {

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        runtime.reset();

        drv.driveGeneral(DriveMethods.moveDirection.FwdBack, 24, prm.DRIVE_POWER_LIMIT, "Back 24 inches",this);
        sleep(1000);
        drv.driveGeneral(DriveMethods.moveDirection.RightLeft, -24, prm.DRIVE_POWER_LIMIT, "Left 24 inches",this);
        sleep(1000);
        drv.driveGeneral(DriveMethods.moveDirection.Rotate, 90, prm.DRIVE_POWER_LIMIT, "Clockwise 90 degrees",this);
        sleep(1000);
    }
}
