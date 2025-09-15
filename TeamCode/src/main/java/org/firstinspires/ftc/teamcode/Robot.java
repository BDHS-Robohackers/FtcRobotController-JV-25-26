package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    public DcMotor leftFrontDrive;
    public DcMotor leftBackDrive;
    public DcMotor rightFrontDrive;
    public DcMotor rightBackDrive;
    public DcMotor rightFly;
    public DcMotor leftFly;

    public Robot() {}

    public void initialize(HardwareMap hardwareMap) {
        try {
            leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
            leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
            rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
            rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
            leftFly = hardwareMap.get(DcMotor.class, "leftFly");
            rightFly = hardwareMap.get(DcMotor.class, "rightFly");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the drive motors with axial, lateral, yaw and an additional control (set to 0 by default for now)
     * @param axial forward is positive, backward is negative
     * @param lateral left is positive, right is negative
     * @param yaw clockwise is positive, counter-clockwise is negative
     * @param someOtherControl used for any additional control, if necessary
     */
    public void updateDriveMotors(double axial, double lateral, double yaw, double someOtherControl) {
        double max;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        double leftFrontPower = axial - lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial + lateral + yaw;
        double rightBackPower = axial + lateral - yaw;

        // Normalize the values so no wheel power exceeds 100%
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);
    }

    public void updateFlywheelMotors(double power) {
        leftFly.setPower(-0.80*power);
        rightFly.setPower(0.80*power);
        // ^^^ FLYWHEEL POWER SETTINGS
        // Change BOTH blue values to a percentage in decimal form.
        // The top one MUST be Negative.
    }
}
