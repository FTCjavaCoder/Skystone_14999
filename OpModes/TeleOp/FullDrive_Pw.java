/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package Skystone_14999.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="FullDrive_Pw", group="Iterative Opmode")
//@Disabled
public class FullDrive_Pw extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private Skystone_14999.HarwareConfig.Hardware_Pw Billy;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        Billy = new Skystone_14999.HarwareConfig.Hardware_Pw(this);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        // DRIVING
        Billy.setWheelsPower(gamepad1, gamepad2);

        // SERVOS FOUNDATION. Still original way
        if(gamepad1.dpad_up) {
            Billy.servoFoundationL.setPosition(0.10);
            Billy.servoFoundationR.setPosition(0.90);
        }
        if(gamepad1.dpad_down) {
            Billy.servoFoundationL.setPosition(0.80);
            Billy.servoFoundationR.setPosition(0.20);
        }

        // JACK
        Billy.setJackPower(gamepad1, gamepad2);

        // SLIDE
        Billy.setSlidePower(gamepad1, gamepad2);

        // SERVO HANDS
        if (Billy.GoGrab8in) // If GRABBING 8in is in process
            Billy.HandGoClosingTo8in();
         else if (Billy.GoOpenUp) // If GRABBING 8in is in process
           Billy.HandGoOpeningUp();
        else {
            // STARTS GRABBING 8in process
            if ((gamepad2.a))
                Billy.GoGrab8in = true; // GRABBING 8in process  to START

            if ((gamepad2.y) && !(Billy.GoGrab8in))
                Billy.GoOpenUp = true; // OPENING_UP process to START

            // CLOSES Hands if GP2_Right_Bumper is pressed
            if ((gamepad2.right_bumper) && !(Billy.GoGrab8in) && !(Billy.GoOpenUp))
                Billy.HandClosing();

            // OPENS Hands if GP2_Left_Bumper is pressed
            if ((gamepad2.left_bumper) && !(Billy.GoGrab8in && !(Billy.GoOpenUp)))
                Billy.HandOpening();
        }

        if (gamepad2.dpad_up) Billy.setDisplayStatus(+1);
        if (gamepad2.dpad_down) Billy.setDisplayStatus (-1);

        // Display information
        Billy.DisplayStatus(this);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        // Stop Robot from any movement
        Billy.stopMotors();
    }

}
