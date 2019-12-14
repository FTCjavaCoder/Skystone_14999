package Skystone_14999.DriveMotion;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import Skystone_14999.OpModes.BasicOpMode;
import Skystone_14999.OpModes.Autonomous.BasicAuto;

public class DriveMethods{

    int targetPos[] = new int[4];

    public enum moveDirection {FwdBack, RightLeft, Rotate}

    public DriveMethods(){
        // no OpMode NOTE: still contains non driving methods
    }

    public void driveGeneral(moveDirection moveType, double distanceInch, double powerLimit, String step, BasicAuto om) {
        int countDistance = 0;
        int[] driveDirection = new int[4];
        int[] startPos = new int[4];
        boolean motorsDone = false;

        switch(moveType) {

            case FwdBack :
                countDistance = (int) Math.round(distanceInch * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = +1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = -1;// BL
                break;

            case RightLeft :
                countDistance = (int) Math.round((distanceInch * om.cons.adjustedRight) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = -1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = +1;// BL
                break;

            case Rotate :
                countDistance = (int) Math.round((distanceInch * om.cons.adjustedRotate) * om.cons.ROBOT_DEG_TO_WHEEL_INCH * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = -1;// FR
                driveDirection[2] = -1;// BR
                driveDirection[3] = -1;// BL
                break;
            default:
                countDistance = (int) 0;

                driveDirection[0] = 0;// FL
                driveDirection[1] = 0;// FR
                driveDirection[2] = 0;// BR
                driveDirection[3] = 0;// BL
        }

        om.runtime.reset();
        startPos = motorStartPos(om);
        setMotorPower(powerLimit, om);

        for (int i=0; i<4; i++) {

            targetPos[i] = startPos[i] + (driveDirection[i] * countDistance);
        }

        om.Billy.frontLeft.setTargetPosition(targetPos[0]);
        om.Billy.frontRight.setTargetPosition(targetPos[1]);
        om.Billy.backRight.setTargetPosition(targetPos[2]);
        om.Billy.backLeft.setTargetPosition(targetPos[3]);


        while(!motorsDone && om.opModeIsActive()) {

            motorsDone = targetPosTolerence(om);

            om.telemetry.addData("Driving: ", step);
            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    om.Billy.frontLeft.getTargetPosition(), om.Billy.frontRight.getTargetPosition(),
                    om.Billy.backRight.getTargetPosition(),om.Billy.backLeft.getTargetPosition());
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backRight.getCurrentPosition(), om.Billy.backLeft.getCurrentPosition());
            om.telemetry.addData("Move Tolerance: ", om.cons.pHM.get("moveTol").value);
            om.telemetry.update();

            om.idle();
        }

        setMotorPower(0, om);
        om.Billy.frontLeft.setTargetPosition(om.Billy.frontLeft.getCurrentPosition());
        om.Billy.frontRight.setTargetPosition(om.Billy.frontRight.getCurrentPosition());
        om.Billy.backRight.setTargetPosition(om.Billy.backRight.getCurrentPosition());
        om.Billy.backLeft.setTargetPosition(om.Billy.backLeft.getCurrentPosition());

        om.telemetry.addData("Driving: ", step);
        om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                om.Billy.frontLeft.getTargetPosition(), om.Billy.frontRight.getTargetPosition(),
                om.Billy.backRight.getTargetPosition(),om.Billy.backLeft.getTargetPosition());
        om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                om.Billy.backRight.getCurrentPosition(), om.Billy.backLeft.getCurrentPosition());
        om.telemetry.addData("Move Tolerance: ", om.cons.pHM.get("moveTol").value);
        om.telemetry.addData("Time: ", om.runtime);

    }

    public void driveGeneralPower(moveDirection moveType, double distanceInch, double powerLimit, double powerMin, String step, BasicAuto om) {
        int countDistance = 0;
        int[] driveDirection = new int[4];
        int[] startPos = new int[4];
        boolean motorsDone = false;
        double[] error = new double[4];
        double[] prePower = new double[4];
        double[] setPower = new double[4];
        double powerGain = powerLimit / (4 * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);// 4 is inches from target when robot will start slowing down

        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        switch(moveType) {

            case FwdBack :
                countDistance = (int) Math.round(distanceInch * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = +1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = -1;// BL
                break;

            case RightLeft :
                countDistance = (int) Math.round((distanceInch * om.cons.adjustedRight) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = -1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = +1;// BL
                break;

            case Rotate :
                countDistance = (int) Math.round((distanceInch * om.cons.adjustedRotate) * om.cons.ROBOT_DEG_TO_WHEEL_INCH * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = -1;// FR
                driveDirection[2] = -1;// BR
                driveDirection[3] = -1;// BL
                break;
            default:
                countDistance = (int) 0;

                driveDirection[0] = 0;// FL
                driveDirection[1] = 0;// FR
                driveDirection[2] = 0;// BR
                driveDirection[3] = 0;// BL
        }

        startPos = motorStartPos(om);

        for (int i=0; i<4; i++) {

            targetPos[i] = startPos[i] + (driveDirection[i] * countDistance);
        }

        error[0] = targetPos[0] - om.Billy.frontLeft.getCurrentPosition();
        error[1] = targetPos[1] - om.Billy.frontRight.getCurrentPosition();
        error[2] = targetPos[2] - om.Billy.backRight.getCurrentPosition();
        error[3] = targetPos[3] - om.Billy.backLeft.getCurrentPosition();

//            prePower[0] = Math.abs(error[0]) * powerGain;
//            prePower[1] = Math.abs(error[1]) * powerGain;
//            prePower[2] = Math.abs(error[2]) * powerGain;
//            prePower[3] = Math.abs(error[3]) * powerGain;
//        prePower[0] = Range.clip(Math.abs(error[0]) * powerGain, powerMin, powerLimit);
//        prePower[1] = Range.clip(Math.abs(error[1]) * powerGain, powerMin, powerLimit);
//        prePower[2] = Range.clip(Math.abs(error[2]) * powerGain, powerMin, powerLimit);
//        prePower[3] = Range.clip(Math.abs(error[3]) * powerGain, powerMin, powerLimit);

        for (int i=0; i<4; i++) {

            setPower[i] = Range.clip(Math.abs(error[i]) * powerGain, powerMin, powerLimit) * Math.signum(error[i]);
        }

        om.runtime.reset();
        setMotorPowerArray(setPower, om);

        while(!motorsDone && om.opModeIsActive()) {

            error[0] = targetPos[0] - om.Billy.frontLeft.getCurrentPosition();
            error[1] = targetPos[1] - om.Billy.frontRight.getCurrentPosition();
            error[2] = targetPos[2] - om.Billy.backRight.getCurrentPosition();
            error[3] = targetPos[3] - om.Billy.backLeft.getCurrentPosition();

//            prePower[0] = Math.abs(error[0]) * powerGain;
//            prePower[1] = Math.abs(error[1]) * powerGain;
//            prePower[2] = Math.abs(error[2]) * powerGain;
//            prePower[3] = Math.abs(error[3]) * powerGain;
//            prePower[0] = Range.clip(Math.abs(error[0]) * powerGain, powerMin, powerLimit);
//            prePower[1] = Range.clip(Math.abs(error[1]) * powerGain, powerMin, powerLimit);
//            prePower[2] = Range.clip(Math.abs(error[2]) * powerGain, powerMin, powerLimit);
//            prePower[3] = Range.clip(Math.abs(error[3]) * powerGain, powerMin, powerLimit);

            for (int i=0; i<4; i++) {

                setPower[i] = Range.clip(Math.abs(error[i]) * powerGain, powerMin, powerLimit) * Math.signum(error[i]);
            }

            setMotorPowerArray(setPower, om);

            om.telemetry.addData("Driving: ", step);
            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    om.Billy.frontLeft.getTargetPosition(), om.Billy.frontRight.getTargetPosition(),
                    om.Billy.backRight.getTargetPosition(),om.Billy.backLeft.getTargetPosition());
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backRight.getCurrentPosition(), om.Billy.backLeft.getCurrentPosition());
            om.telemetry.addData("Move Tolerance: ", om.cons.pHM.get("moveTol").value);
            om.telemetry.update();

            motorsDone = targetPosTolerence(om);

            om.idle();
        }
        setMotorPower(0, om);

        om.Billy.frontLeft.setTargetPosition(om.Billy.frontLeft.getCurrentPosition());
        om.Billy.frontRight.setTargetPosition(om.Billy.frontRight.getCurrentPosition());
        om.Billy.backRight.setTargetPosition(om.Billy.backRight.getCurrentPosition());
        om.Billy.backLeft.setTargetPosition(om.Billy.backLeft.getCurrentPosition());

        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        om.telemetry.addData("Driving: ", step);
        om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                om.Billy.frontLeft.getTargetPosition(), om.Billy.frontRight.getTargetPosition(),
                om.Billy.backRight.getTargetPosition(),om.Billy.backLeft.getTargetPosition());
        om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                om.Billy.backRight.getCurrentPosition(), om.Billy.backLeft.getCurrentPosition());
        om.telemetry.addData("Move Tolerance: ", om.cons.pHM.get("moveTol").value);
        om.telemetry.addData("Time: ", om.runtime);

    }

    public void driveGeneralQuickStop(moveDirection moveType, double distanceInch, double powerLimit, String step, BasicAuto om) {
        int countDistance = 0;
        int[] driveDirection = new int[4];
        int[] startPos = new int[4];
        boolean motorsDone = false;

        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        switch(moveType) {

            case FwdBack :
                countDistance = (int) Math.round(distanceInch * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = +1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = -1;// BL
                break;

            case RightLeft :
                countDistance = (int) Math.round((distanceInch * om.cons.adjustedRight) * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = -1;// FR
                driveDirection[2] = +1;// BR
                driveDirection[3] = +1;// BL
                break;

            case Rotate :
                countDistance = (int) Math.round((distanceInch * om.cons.adjustedRotate) * om.cons.ROBOT_DEG_TO_WHEEL_INCH * om.cons.ROBOT_INCH_TO_MOTOR_DEG * om.cons.DEGREES_TO_COUNTS);

                driveDirection[0] = -1;// FL
                driveDirection[1] = -1;// FR
                driveDirection[2] = -1;// BR
                driveDirection[3] = -1;// BL
                break;
            default:
                countDistance = (int) 0;

                driveDirection[0] = 0;// FL
                driveDirection[1] = 0;// FR
                driveDirection[2] = 0;// BR
                driveDirection[3] = 0;// BL
        }

        startPos = motorStartPos(om);

        for (int i=0; i<4; i++) {

            targetPos[i] = startPos[i] + (driveDirection[i] * countDistance);
        }

        setMotorPower(powerLimit, om);

        while(!motorsDone && om.opModeIsActive()) {

            motorsDone = targetPosTolerence(om);

            om.telemetry.addData("Driving: ", step);
            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    om.Billy.frontLeft.getTargetPosition(), om.Billy.frontRight.getTargetPosition(),
                    om.Billy.backRight.getTargetPosition(),om.Billy.backLeft.getTargetPosition());
            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BR (%d) BL (%d)",
                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
                    om.Billy.backRight.getCurrentPosition(), om.Billy.backLeft.getCurrentPosition());
            om.telemetry.addData("Move Tolerance: ", om.cons.pHM.get("moveTol").value);
            om.telemetry.update();

            om.idle();
        }
        setMotorPower(0, om);

        om.Billy.frontLeft.setTargetPosition(om.Billy.frontLeft.getCurrentPosition());
        om.Billy.frontRight.setTargetPosition(om.Billy.frontRight.getCurrentPosition());
        om.Billy.backRight.setTargetPosition(om.Billy.backRight.getCurrentPosition());
        om.Billy.backLeft.setTargetPosition(om.Billy.backLeft.getCurrentPosition());

        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public int[] motorStartPos(BasicOpMode om) {

        int[] currentPos = new int[4];

        currentPos[0]= om.Billy.frontLeft.getCurrentPosition(); //FL
        currentPos[1]= om.Billy.frontRight.getCurrentPosition(); //FR
        currentPos[2]= om.Billy.backRight.getCurrentPosition(); //BR
        currentPos[3]= om.Billy.backLeft.getCurrentPosition(); //BL

        return currentPos;
    }

    public void setMotorPower(double power, BasicOpMode om) {

        om.Billy.frontLeft.setPower(power);
        om.Billy.frontRight.setPower(power);
        om.Billy.backRight.setPower(power);
        om.Billy.backLeft.setPower(power);

    }

    public void setMotorPowerArray(double[] power, BasicOpMode om) {

        om.Billy.frontLeft.setPower(power[0]);
        om.Billy.frontRight.setPower(power[1]);
        om.Billy.backRight.setPower(power[2]);
        om.Billy.backLeft.setPower(power[3]);

    }

    public boolean targetPosTolerence(BasicOpMode om) {

        int countTol = 0;
        Boolean motorFinish = false;
        int[] motorPos = new int[4];

        motorPos = motorStartPos(om);
        for (int i = 0; i < 4; i++) {

            if (om.cons.pHM.get("moveTol").value >= Math.abs(motorPos[i] - targetPos[i])) {

                countTol += 1;

                if (countTol == 1) {// was 4

                    motorFinish = true;

                }
            }

        }

        return motorFinish;

    }

    public void moveJack(double height, double jackPowerLimit, String step, BasicOpMode om) {
//        int countDistance = (int) (om.cons.NUMBER_OF_JACK_STAGES * (om.cons.W0 - ( (Math.sqrt(Math.pow(om.cons.W0, 2) - Math.pow(om.DeltaH + om.cons.H0, 2)) / om.cons.MOTOR_DEG_TO_LEAD) * om.cons.DEGREES_TO_COUNTS) ) );
        int countDistance = (int) ( ((height / om.cons.MOTOR_DEG_TO_LEAD) * om.cons.DEGREES_TO_COUNTS) / om.cons.NUMBER_OF_JACK_STAGES);//!!!!!!!!!!!!!!!!!!!
//        int startPosL;
//        int startPosR;
        int jackZoneC;

//        startPosL = om.Billy.jack.getCurrentPosition();
//        startPosR = om.Billy.jackRight.getCurrentPosition();
        om.Billy.jack.setPower(jackPowerLimit);
        om.Billy.jack.setTargetPosition(countDistance);

        jackZoneC = Math.abs(countDistance - om.Billy.jack.getCurrentPosition() );

        while((jackZoneC > om.cons.pHM.get("moveTol").value) && om.opModeIsActive()) {

            jackZoneC = Math.abs(countDistance - om.Billy.jack.getCurrentPosition() );

            om.telemetry.addData("Jack: ", step);
            om.telemetry.addData("Motor Commands: ", "Jack Center (%d)", om.Billy.jack.getTargetPosition());
            om.telemetry.addData("Motor Counts: ", "Jack Center (%d)", om.Billy.jack.getCurrentPosition());
            om.telemetry.addData("Move Tolerance: ", om.cons.pHM.get("moveTol").value);
            om.telemetry.update();

            om.idle();
        }
        om.Billy.jack.setPower(0);

        om.telemetry.addLine("Jack Motion Done");
    }

//    public void driveFwdRev(int distance, double powerLimit, String step, BasicAuto om) {
//        // "Distance" = the added straight distance from the current position
//        int flZone;
//        int frZone;
//        int blZone;
//        int brZone;
//        int flStart;
//        int frStart;
//        int blStart;
//        int brStart;
//
//        //Define om.starting position before setting command
//        flStart = om.Billy.frontLeft.getCurrentPosition();
//        frStart = om.Billy.frontRight.getCurrentPosition();
//        blStart = om.Billy.backLeft.getCurrentPosition();
//        brStart = om.Billy.backRight.getCurrentPosition();
//        //update power limit
//        om.Billy.frontLeft.setPower(powerLimit);
//        om.Billy.frontRight.setPower(powerLimit);
//        om.Billy.backLeft.setPower(powerLimit);
//        om.Billy.backRight.setPower(powerLimit);
//
//        //Drive to forward position
//        //Right side motors are "+" for positive forward
//        //Left side motors are "-" for positive forward
//        //Set target position by effectivel "adding" distance to current position
//        om.Billy.frontLeft.setTargetPosition(flStart - distance);
//        om.Billy.frontRight.setTargetPosition(frStart + distance);
//        om.Billy.backLeft.setTargetPosition(blStart - distance);
//        om.Billy.backRight.setTargetPosition(brStart + distance);
//
//        //Set the tolerance zone for completing motion
//        flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + distance);
//        frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart - distance);
//        blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + distance);
//        brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - distance);
//
//        //Check tolerance zone to exit method
//        while ( ( (flZone > om.cons.pHM.get("moveTol").value) || (frZone > om.cons.pHM.get("moveTol").value) || (blZone >  om.cons.pHM.get("moveTol").value) || (brZone > om.cons.pHM.get("moveTol").value) ) && om.opModeIsActive()) {
//            flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + distance);
//            frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart - distance);
//            blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + distance);
//            brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - distance);
//            om.telemetry.addData("Driving: ", step);
//            om.telemetry.addData("Motor Commands: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
//                    om.Billy.frontLeft.getTargetPosition(), om.Billy.frontRight.getTargetPosition(),
//                    om.Billy.backLeft.getTargetPosition(), om.Billy.backRight.getTargetPosition());
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
//                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
//                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
//            om.telemetry.addData("Move Tolerance: ", om.cons.pHM.get("moveTol").value);
//            om.telemetry.update();
//
//            om.idle();
//        }
//        om.Billy.frontLeft.setPower(0);
//        om.Billy.frontRight.setPower(0);
//        om.Billy.backLeft.setPower(0);
//        om.Billy.backRight.setPower(0);
//
//    }
//
//    public void driveRightLeft(int right, double powerLimit, String step, BasicAuto om){
//        int flZone;
//        int frZone;
//        int blZone;
//        int brZone;
//        int flStart;
//        int frStart;
//        int blStart;
//        int brStart;
//
//        //Define Starting position before setting command
//        flStart = om.Billy.frontLeft.getCurrentPosition();
//        frStart = om.Billy.frontRight.getCurrentPosition();
//        blStart = om.Billy.backLeft.getCurrentPosition();
//        brStart = om.Billy.backRight.getCurrentPosition();
//
//        //Update power limits
//        om.Billy.frontLeft.setPower(powerLimit);
//        om.Billy.frontRight.setPower(powerLimit);
//        om.Billy.backLeft.setPower(powerLimit);
//        om.Billy.backRight.setPower(powerLimit);
//
//        //Back side motors are "+" for positive right
//        //Front side motors are "-" for positive right
//        //Effectively "add" right position to current position
//        om.Billy.frontLeft.setTargetPosition(flStart - right);
//        om.Billy.frontRight.setTargetPosition(frStart - right);
//        om.Billy.backLeft.setTargetPosition(blStart + right);
//        om.Billy.backRight.setTargetPosition(brStart + right);
//
//        //Set the tolerance zone for completing motion
//        flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + right);
//        frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + right);
//        blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart - right);
//        brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - right);
//
//        //Check tolerance zone to exit methodn
//        while ( ( (flZone > om.cons.pHM.get("moveTol").value) || (frZone > om.cons.pHM.get("moveTol").value) || (blZone > om.cons.pHM.get("moveTol").value) || (brZone > om.cons.pHM.get("moveTol").value) ) && om.opModeIsActive()) {
//            flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + right);
//            frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + right);
//            blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart - right);
//            brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart - right);
//            om.telemetry.addData("Driving: ", step);
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
//                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
//                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
//            om.telemetry.update();
//
//            om.idle();
//        }
//        //Set power to zero
//        om.Billy.frontLeft.setPower(0);
//        om.Billy.frontRight.setPower(0);
//        om.Billy.backLeft.setPower(0);
//        om.Billy.backRight.setPower(0);
//    }
//
//    public void driveRotate(int clockwise, double powerLimit, String step, BasicAuto om){
//        int flZone;
//        int frZone;
//        int blZone;
//        int brZone;
//        int flStart;
//        int frStart;
//        int blStart;
//        int brStart;
//
//        //Define Starting position before setting command
//        flStart = om.Billy.frontLeft.getCurrentPosition();
//        frStart = om.Billy.frontRight.getCurrentPosition();
//        blStart = om.Billy.backLeft.getCurrentPosition();
//        brStart = om.Billy.backRight.getCurrentPosition();
//
//        //Update power limits
//        om.Billy.frontLeft.setPower(powerLimit);
//        om.Billy.frontRight.setPower(powerLimit);
//        om.Billy.backLeft.setPower(powerLimit);
//        om.Billy.backRight.setPower(powerLimit);
//
//        //Effectively "add" rotate position to current position
//        om.Billy.frontLeft.setTargetPosition(flStart - clockwise);
//        om.Billy.frontRight.setTargetPosition(frStart - clockwise);
//        om.Billy.backLeft.setTargetPosition(blStart - clockwise);
//        om.Billy.backRight.setTargetPosition(brStart - clockwise);
//
//        //Set the tolerance zone for completing motion
//        flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + clockwise);
//        frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + clockwise);
//        blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + clockwise);
//        brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart + clockwise);
//
//        //Check tolerance zone to exit method
//        while ( ( (flZone > om.cons.pHM.get("moveTol").value) || (frZone > om.cons.pHM.get("moveTol").value) || (blZone >  om.cons.pHM.get("moveTol").value) || (brZone > om.cons.pHM.get("moveTol").value) ) && om.opModeIsActive()) {
//            flZone = (int) Math.abs (om.Billy.frontLeft.getCurrentPosition() - flStart + clockwise);
//            frZone = (int) Math.abs (om.Billy.frontRight.getCurrentPosition() - frStart + clockwise);
//            blZone = (int) Math.abs (om.Billy.backLeft.getCurrentPosition() - blStart + clockwise);
//            brZone = (int) Math.abs (om.Billy.backRight.getCurrentPosition() - brStart + clockwise);
//
//            om.telemetry.addData("Rotating: ", step);
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
//                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
//                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
//            om.telemetry.update();
//
//            om.idle();
//        }
//        om.Billy.frontLeft.setPower(0);
//        om.Billy.frontRight.setPower(0);
//        om.Billy.backLeft.setPower(0);
//        om.Billy.backRight.setPower(0);
//    }
//
////    public void slideMove(int distance, double powerLimit, String step, BasicAuto om){
////
////        int lsZone;
////        int lsStart;
////
////        lsStart = om.Billy.landingSlide.getCurrentPosition();
////        om.Billy.landingSlide.setPower(powerLimit);
////        om.Billy.landingSlide.setTargetPosition(lsStart + distance);
////
////        lsZone = (int) Math.abs (om.Billy.landingSlide.getCurrentPosition() - (lsStart + distance)); // need to confirm sign before distance
////
////        while (lsZone > om.prm.SLIDE_TOL && om.opModeIsActive()) {
////            lsZone = (int) Math.abs (om.Billy.landingSlide.getCurrentPosition() - (lsStart + distance)); // need to confirm sign before distance
////
////            om.telemetry.addData("Slide: ", step);
////            om.telemetry.addData("Motor Counts: ", "SlideArm CurrentPos (%d), Command (%d)",
////                    om.Billy.landingSlide.getCurrentPosition(), lsStart + distance);
////
////            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
////                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
////                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
////            om.telemetry.update();
////
////            om.idle();
////        }
////        om.Billy.landingSlide.setPower(0);
////
////    }
//
//    public void driveFwdRevIMU(int distance, double powerLimit, double cmdDriveAngle, String step, BasicAuto om) { //not working
//
//        double error;
//        double steering;
//        double distanceMoved;
//
//        // "Distance" = the added straight distance from the current position
//        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        calStart(om);
//
//        distanceMoved = IMUCalDistanceFwdRev(om);
//        om.angleUnWrap();
//
//        //Check tolerance zone to exit method
//        while (Math.abs(distanceMoved - distance) > om.cons.IMU_TOL && (om.opModeIsActive())) {
//
//            error = cmdDriveAngle - om.robotHeading;
//            steering = Range.clip((error * om.cons.GAIN), -om.cons.pHM.get("rotatePowerLimit").value, om.cons.pHM.get("rotatePowerLimit").value);
//
//            //update power limit
//            om.Billy.frontLeft.setPower(-powerLimit - steering);
//            om.Billy.frontRight.setPower(powerLimit - steering);
//            om.Billy.backLeft.setPower(-powerLimit - steering);
//            om.Billy.backRight.setPower(powerLimit - steering);
//
//            om.telemetry.addData("Driving: %s", step);
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
//                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
//                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
//            om.telemetry.addData("\r","wanted distance (%d), calc forward travel (%.2f)", distance, IMUCalDistanceFwdRev(om));
//            om.telemetry.update();
//
//            distanceMoved = IMUCalDistanceFwdRev(om);
//            om.angleUnWrap();
//
//            om.idle();
//        }
//
//        om.Billy.frontLeft.setPower(0);
//        om.Billy.frontRight.setPower(0);
//        om.Billy.backLeft.setPower(0);
//        om.Billy.backRight.setPower(0);
//
//        om.telemetry.addLine("Power set to zero");
//        om.telemetry.update();
//
//    }
//
//    public void driveRightLeftIMU(int distance, double powerLimit, double cmdDriveAngle, String step, BasicAuto om) {
//
//        double error;
//        double steering;
//        double distanceMoved;
//
//        // "Distance" = the added sideways distance from the current position
//        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        calStart(om);
//
//        distanceMoved = IMUCalDistanceRightLeft(om);
//        om.angleUnWrap();
//
//        //Check tolerance zone to exit method
//        while (Math.abs(distanceMoved - distance) > om.cons.IMU_TOL && (om.opModeIsActive())) {
//
//            error = cmdDriveAngle - om.robotHeading;
//            steering = Range.clip((error * om.cons.GAIN), -om.cons.pHM.get("rotatePowerLimit").value, om.cons.pHM.get("rotatePowerLimit").value);
//
//            //update power limit
//            om.Billy.frontLeft.setPower(-powerLimit - steering);
//            om.Billy.frontRight.setPower(-powerLimit - steering);
//            om.Billy.backLeft.setPower(powerLimit - steering);
//            om.Billy.backRight.setPower(powerLimit - steering);
//
//            om.telemetry.addData("Driving: %s", step);
//            om.telemetry.addData("Motor Counts: ", "FL (%d) FR (%d) BL (%d) BR (%d)",
//                    om.Billy.frontLeft.getCurrentPosition(), om.Billy.frontRight.getCurrentPosition(),
//                    om.Billy.backLeft.getCurrentPosition(), om.Billy.backRight.getCurrentPosition());
//            om.telemetry.addData("\r","wanted distance (%d), calc right/left travel (%.2f)", distance, IMUCalDistanceRightLeft(om));
//            om.telemetry.update();
//
//            distanceMoved = IMUCalDistanceRightLeft(om);
//            om.angleUnWrap();
//
//            om.idle();
//        }
//
//        om.Billy.frontLeft.setPower(0);
//        om.Billy.frontRight.setPower(0);
//        om.Billy.backLeft.setPower(0);
//        om.Billy.backRight.setPower(0);
//
//
//        om.telemetry.addLine("Power set to zero");
//        om.telemetry.update();
//
//    }
//
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
        while (Math.abs(angle - om.robotHeading) > om.cons.IMU_ROTATE_TOL && (om.opModeIsActive())) {

            error = angle - om.robotHeading;
            steering = Range.clip((error * om.cons.GAIN), -om.cons.pHM.get("rotatePowerLimit").value, om.cons.pHM.get("rotatePowerLimit").value);

            //update power limit
            om.Billy.frontLeft.setPower(powerLimit - steering);
            om.Billy.frontRight.setPower(powerLimit - steering);
            om.Billy.backLeft.setPower(powerLimit - steering);
            om.Billy.backRight.setPower(powerLimit - steering);

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
        om.Billy.backRight.setPower(0);
        om.Billy.backLeft.setPower(0);

        om.Billy.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        om.Billy.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        om.telemetry.addLine("Power set to zero");
        om.telemetry.update();

    }
//
//    public void calStart(BasicAuto om) {
//
//        om.flStart = om.Billy.frontLeft.getCurrentPosition();
//        om.frStart = om.Billy.frontRight.getCurrentPosition();
//        om.blStart = om.Billy.backLeft.getCurrentPosition();
//        om.brStart = om.Billy.backRight.getCurrentPosition();
//
//    }
//
//    public double IMUCalDistanceFwdRev(BasicAuto om) {
//
//        double deltaFL;
//        double deltaFR;
//        double deltaBL;
//        double deltaBR;
//        double rotationOffset;
//        double flAdjust;
//        double frAdjust;
//        double blAdjust;
//        double brAdjust;
//        double distanceTraveled;
//
//        deltaFL = om.Billy.frontLeft.getCurrentPosition() - om.flStart;
//        deltaFR = om.Billy.frontRight.getCurrentPosition() - om.frStart;
//        deltaBL = om.Billy.backLeft.getCurrentPosition() - om.blStart;
//        deltaBR = om.Billy.backRight.getCurrentPosition() - om.brStart;
//
//        rotationOffset = (deltaFL + deltaFR + deltaBL + deltaBR) / 4;
//
//        flAdjust = deltaFL - rotationOffset;
//        frAdjust = deltaFR - rotationOffset;
//        blAdjust = deltaBL - rotationOffset;
//        brAdjust = deltaBR - rotationOffset;
//
//        distanceTraveled = (- flAdjust - blAdjust + frAdjust  + brAdjust) / 4;
//
//        return distanceTraveled;
//    }
//
//    public double IMUCalDistanceRightLeft(BasicAuto om) {
//
//        double deltaFL;
//        double deltaFR;
//        double deltaBL;
//        double deltaBR;
//        double rotationOffset;
//        double flAdjust;
//        double frAdjust;
//        double blAdjust;
//        double brAdjust;
//        double distanceTraveled;
//
//        deltaFL = om.Billy.frontLeft.getCurrentPosition() - om.flStart;
//        deltaFR = om.Billy.frontRight.getCurrentPosition() - om.frStart;
//        deltaBL = om.Billy.backLeft.getCurrentPosition() - om.blStart;
//        deltaBR = om.Billy.backRight.getCurrentPosition() - om.brStart;
//
//        rotationOffset = (deltaFL + deltaFR + deltaBL + deltaBR) / 4;
//
//        flAdjust = deltaFL - rotationOffset;
//        frAdjust = deltaFR - rotationOffset;
//        blAdjust = deltaBL - rotationOffset;
//        brAdjust = deltaBR - rotationOffset;
//
//        distanceTraveled = (- flAdjust - frAdjust + blAdjust + brAdjust) / 4;
//
//        return distanceTraveled;
//    }

}