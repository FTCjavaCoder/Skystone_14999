package Skystone_14999.OpModes.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Skystone_14999.DriveMotion.DriveMethods;
import Skystone_14999.OpModes.Autonomous.BasicAuto;

@Autonomous(name="JackMoveTest", group="Autonomous")

public class JackMoveTest extends BasicAuto {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        runtime.reset();

        drv.moveJack(1, cons.pHM.get("jackPowerLimit").value,"Jack Up 1 Inch",this);

    }
}
