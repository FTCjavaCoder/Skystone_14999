package Skystone_14999.DriveMotion;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import Skystone_14999.OpModes.Autonomous.BasicAuto;

public class DriveMethods{

    
    public DriveMethods(){
        // no OpMode NOTE: still contains non driving methods
    }

    public void driveFwdRev(int distance, double powerLimit, String step, BasicAuto om) {
        // "Distance" = the added straight distance from the current position
        int flZone;
        int frZone;
        int blZone;
        int brZone;
        int flStart;
        int frStart;
        int blStart;
        int brStart;

        //Define om.starting position before setting command
        flStart = om.Billy.frontLeft.getCurrentPosition();
        frStart = om.Billy.frontRight.getCurrentPosition();
        blStart = om.Billy.backLeft.getCurrentPosition();
        brStart = om.Billy.backRight.getCurrentPosition();
        //update power limit
        om.Billy.frontLeft.setPower(powerLimit);
        om.Billy.frontRight.setPower(powerLimit);
        om.Billy.backLeft.setPower(powerLimit);
        om.Billy.backRight.setPower(powerLimit);

        //Drive to forward position
        //Right side motors are "+" for positive forward
        //Left side motors are "-" for positive forward
        //Set target position by effectivel "adding" distance to current position
        om.Billy.frontLeft.setTargetPosition(flStart - distance);
        om.Billy.frontRight.setTargetPosition(frStart + distance);
        om.Billy.backLeft.setTargetPosition(blStart - distance);
        om.Billy.backRight.setTargetPosition(brStart + distance);

        //Set the tolerance zone for completing motion
        flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + distance);
        frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart - distance);
        blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + distance);
        brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - distance);

        //Check tolerance zone to exit method
        while ( ( (flZone > om.prm.MOVE_TOL) || (frZone > om.prm.MOVE_TOL) || (blZone >  om.prm.MOVE_TOL) || (brZone > om.prm.MOVE_TOL) ) && om.opModeIsActive()) {
            flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + distance);
            frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart - distance);
            blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + distance);
            brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - distance);
            om.telemetry.addData("Driving: ", step);
            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    om.Billy.frontLeft.getTargetPosition(), om.Billy.frontRight.getTargetPosition(),
                    om.Billy.backLeft.getTargetPosition(), om.Billy.backRight.getTargetPosition());
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
            om.telemetry.addData("Move Tolerance: ", om.prm.MOVE_TOL);
            om.telemetry.update();

            om.idle();
        }
        om.Billy.frontLeft.setPower(0);
        om.Billy.frontRight.setPower(0);
        om.Billy.backLeft.setPower(0);
        om.Billy.backRight.setPower(0);

    }

    public void driveRightLeft(int right, double powerLimit, String step, BasicAuto om){
        int flZone;
        int frZone;
        int blZone;
        int brZone;
        int flStart;
        int frStart;
        int blStart;
        int brStart;

        //Define Starting position before setting command
        flStart = om.Billy.frontLeft.getCurrentPosition();
        frStart = om.Billy.frontRight.getCurrentPosition();
        blStart = om.Billy.backLeft.getCurrentPosition();
        brStart = om.Billy.backRight.getCurrentPosition();

        //Update power limits
        om.Billy.frontLeft.setPower(powerLimit);
        om.Billy.frontRight.setPower(powerLimit);
        om.Billy.backLeft.setPower(powerLimit);
        om.Billy.backRight.setPower(powerLimit);

        //Back side motors are "+" for positive right
        //Front side motors are "-" for positive right
        //Effectively "add" right position to current position
        om.Billy.frontLeft.setTargetPosition(flStart - right);
        om.Billy.frontRight.setTargetPosition(frStart - right);
        om.Billy.backLeft.setTargetPosition(blStart + right);
        om.Billy.backRight.setTargetPosition(brStart + right);

        //Set the tolerance zone for completing motion
        flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + right);
        frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + right);
        blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart - right);
        brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - right);

        //Check tolerance zone to exit methodn
        while ( ( (flZone > om.prm.MOVE_TOL) || (frZone > om.prm.MOVE_TOL) || (blZone > om.prm.MOVE_TOL) || (brZone > om.prm.MOVE_TOL) ) && om.opModeIsActive()) {
            flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + right);
            frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + right);
            blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart - right);
            brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - right);
            om.telemetry.addData("Driving: ", step);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
            om.telemetry.update();

            om.idle();
        }
        //Set power to zero
        om.Billy.frontLeft.setPower(0);
        om.Billy.frontRight.setPower(0);
        om.Billy.backLeft.setPower(0);
        om.Billy.backRight.setPower(0);
    }

    public void driveRotate(int clockwise, double powerLimit, String step, BasicAuto om){
        int flZone;
        int frZone;
        int blZone;
        int brZone;
        int flStart;
        int frStart;
        int blStart;
        int brStart;

        //Define Starting position before setting command
        flStart = om.Billy.frontLeft.getCurrentPosition();
        frStart = om.Billy.frontRight.getCurrentPosition();
        blStart = om.Billy.backLeft.getCurrentPosition();
        brStart = om.Billy.backRight.getCurrentPosition();

        //Update power limits
        om.Billy.frontLeft.setPower(powerLimit);
        om.Billy.frontRight.setPower(powerLimit);
        om.Billy.backLeft.setPower(powerLimit);
        om.Billy.backRight.setPower(powerLimit);

        //Effectively "add" rotate position to current position
        om.Billy.frontLeft.setTargetPosition(flStart - clockwise);
        om.Billy.frontRight.setTargetPosition(frStart - clockwise);
        om.Billy.backLeft.setTargetPosition(blStart - clockwise);
        om.Billy.backRight.setTargetPosition(brStart - clockwise);

        //Set the tolerance zone for completing motion
        flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + clockwise);
        frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + clockwise);
        blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + clockwise);
        brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart + clockwise);

        //Check tolerance zone to exit method
        while ( ( (flZone > om.prm.MOVE_TOL) || (frZone > om.prm.MOVE_TOL) || (blZone >  om.prm.MOVE_TOL) || (brZone > om.prm.MOVE_TOL) ) && om.opModeIsActive()) {
            flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + clockwise);
            frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + clockwise);
            blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + clockwise);
            brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart + clockwise);

            om.telemetry.addData("Rotating: ", step);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
            om.telemetry.update();

            om.idle();
        }
        om.Billy.frontLeft.setPower(0);
        om.Billy.frontRight.setPower(0);
        om.Billy.backLeft.setPower(0);
        om.Billy.backRight.setPower(0);
    }

//    public void slideMove(int distance, double powerLimit, String step, BasicAuto om){
//
//        int lsZone;
//        int lsStart;
//
//        lsStart = om.Billy.landingSlide.getCurrentPosition();
//        om.Billy.landingSlide.setPower(powerLimit);
//        om.Billy.landingSlide.setTargetPosition(lsStart + distance);
//
//        lsZone = (int) Math.abs (om.Billy.landingSlide.getCurrentPosition() - (lsStart + distance)); // need to confirm sign before distance
//
//        while (lsZone > om.prm.SLIDE_TOL && om.opModeIsActive()) {
//            lsZone = (int) Math.abs (om.Billy.landingSlide.getCurrentPosition() - (lsStart + distance)); // need to confirm sign before distance
//
//            om.telemetry.addData("Slide: ", step);
//            om.telemetry.addData("Motor Counts: ", "SlideArm CurrentPos (%d), Command (%d)",
//                    om.Billy.landingSlide.getCurrentPosition(), lsStart + distance);
//
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
//                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
//                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
//            om.telemetry.update();
//
//            om.idle();
//        }
//        om.Billy.landingSlide.setPower(0);
//
//    }

    public void driveFwdRevIMU(int distance, double powerLimit, double cmdDriveAngle, String step, BasicAuto om) { //not working

        double error;
        double steering;
        double distanceMoved;

        // "Distance" = the added straight distance from the current position
        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        calStart(om);

        distanceMoved = IMUCalDistanceFwdRev(om);
        om.angleUnWrap();

        //Check tolerance zone to exit method
        while (Math.abs(distanceMoved - distance) > om.prm.IMU_TOL && (om.opModeIsActive())) {

            error = cmdDriveAngle - om.robotHeading;
            steering = Range.clip((error * om.prm.GAIN), -om.prm.ROTATE_POWER_LIMIT, om.prm.ROTATE_POWER_LIMIT);

            //update power limit
            om.Billy.frontLeft.setPower(-powerLimit - steering);
            om.Billy.frontRight.setPower(powerLimit - steering);
            om.Billy.backLeft.setPower(-powerLimit - steering);
            om.Billy.backRight.setPower(powerLimit - steering);

            om.telemetry.addData("Driving: %s", step);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
            om.telemetry.addData("\r","wanted distance (%d), calc forward travel (%.2f)", distance, IMUCalDistanceFwdRev(om));
            om.telemetry.update();

            distanceMoved = IMUCalDistanceFwdRev(om);
            om.angleUnWrap();

            om.idle();
        }

        om.Billy.frontLeft.setPower(0);
        om.Billy.frontRight.setPower(0);
        om.Billy.backLeft.setPower(0);
        om.Billy.backRight.setPower(0);

        om.telemetry.addLine("Power set to zero");
        om.telemetry.update();

    }

    public void driveRightLeftIMU(int distance, double powerLimit, double cmdDriveAngle, String step, BasicAuto om) {

        double error;
        double steering;
        double distanceMoved;

        // "Distance" = the added sideways distance from the current position
        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        calStart(om);

        distanceMoved = IMUCalDistanceRightLeft(om);
        om.angleUnWrap();

        //Check tolerance zone to exit method
        while (Math.abs(distanceMoved - distance) > om.prm.IMU_TOL && (om.opModeIsActive())) {

            error = cmdDriveAngle - om.robotHeading;
            steering = Range.clip((error * om.prm.GAIN), -om.prm.ROTATE_POWER_LIMIT, om.prm.ROTATE_POWER_LIMIT);

            //update power limit
            om.Billy.frontLeft.setPower(-powerLimit - steering);
            om.Billy.frontRight.setPower(-powerLimit - steering);
            om.Billy.backLeft.setPower(powerLimit - steering);
            om.Billy.backRight.setPower(powerLimit - steering);

            om.telemetry.addData("Driving: %s", step);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
            om.telemetry.addData("\r","wanted distance (%d), calc right/left travel (%.2f)", distance, IMUCalDistanceRightLeft(om));
            om.telemetry.update();

            distanceMoved = IMUCalDistanceRightLeft(om);
            om.angleUnWrap();

            om.idle();
        }

        om.Billy.frontLeft.setPower(0);
        om.Billy.frontRight.setPower(0);
        om.Billy.backLeft.setPower(0);
        om.Billy.backRight.setPower(0);


        om.telemetry.addLine("Power set to zero");
        om.telemetry.update();

    }

    public void driveRotateIMU(double angle, double powerLimit, String step, BasicAuto om) {

        double error;
        double steering;

        // "angle" = the added rotation angle from the current position
        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        om.angleUnWrap();
        //Check tolerance zone to exit method
        while (Math.abs(angle - om.robotHeading) > om.prm.IMU_ROTATE_TOL && (om.opModeIsActive())) {

            error = angle - om.robotHeading;
            steering = Range.clip((error * om.prm.GAIN), -om.prm.ROTATE_POWER_LIMIT, om.prm.ROTATE_POWER_LIMIT);

            //update power limit
            om.Billy.frontLeft.setPower(-powerLimit - steering);
            om.Billy.frontRight.setPower(-powerLimit - steering);
            om.Billy.backLeft.setPower(-powerLimit - steering);
            om.Billy.backRight.setPower(-powerLimit - steering);

            om.angleUnWrap();

            om.telemetry.addData("Driving: %s", step);
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
            om.telemetry.addData("wanted angle","%.2f, heading %.2f ", angle, om.robotHeading);
            om.telemetry.update();

            om.idle();
        }

        om.Billy.frontLeft.setPower(0);
        om.Billy.frontRight.setPower(0);
        om.Billy.backLeft.setPower(0);
        om.Billy.backRight.setPower(0);

        om.telemetry.addLine("Power set to zero");
        om.telemetry.update();

    }

    public void calStart(BasicAuto om) {

        om.flStart = om.Billy.frontLeft.getCurrentPosition();
        om.frStart = om.Billy.frontRight.getCurrentPosition();
        om.blStart = om.Billy.backLeft.getCurrentPosition();
        om.brStart = om.Billy.backRight.getCurrentPosition();

    }

    public double IMUCalDistanceFwdRev(BasicAuto om) {

        double deltaFL;
        double deltaFR;
        double deltaBL;
        double deltaBR;
        double rotationOffset;
        double flAdjust;
        double frAdjust;
        double blAdjust;
        double brAdjust;
        double distanceTraveled;

        deltaFL = om.Billy.frontLeft.getCurrentPosition() - om.flStart;
        deltaFR = om.Billy.frontRight.getCurrentPosition() - om.frStart;
        deltaBL = om.Billy.backLeft.getCurrentPosition() - om.blStart;
        deltaBR = om.Billy.backRight.getCurrentPosition() - om.brStart;

        rotationOffset = (deltaFL + deltaFR + deltaBL + deltaBR) / 4;

        flAdjust = deltaFL - rotationOffset;
        frAdjust = deltaFR - rotationOffset;
        blAdjust = deltaBL - rotationOffset;
        brAdjust = deltaBR - rotationOffset;

        distanceTraveled = (- flAdjust - blAdjust + frAdjust  + brAdjust) / 4;

        return distanceTraveled;
    }

    public double IMUCalDistanceRightLeft(BasicAuto om) {

        double deltaFL;
        double deltaFR;
        double deltaBL;
        double deltaBR;
        double rotationOffset;
        double flAdjust;
        double frAdjust;
        double blAdjust;
        double brAdjust;
        double distanceTraveled;

        deltaFL = om.Billy.frontLeft.getCurrentPosition() - om.flStart;
        deltaFR = om.Billy.frontRight.getCurrentPosition() - om.frStart;
        deltaBL = om.Billy.backLeft.getCurrentPosition() - om.blStart;
        deltaBR = om.Billy.backRight.getCurrentPosition() - om.brStart;

        rotationOffset = (deltaFL + deltaFR + deltaBL + deltaBR) / 4;

        flAdjust = deltaFL - rotationOffset;
        frAdjust = deltaFR - rotationOffset;
        blAdjust = deltaBL - rotationOffset;
        brAdjust = deltaBR - rotationOffset;

        distanceTraveled = (- flAdjust - frAdjust + blAdjust + brAdjust) / 4;

        return distanceTraveled;
    }

}