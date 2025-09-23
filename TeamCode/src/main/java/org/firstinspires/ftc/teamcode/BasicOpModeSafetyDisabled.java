package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Driver Op Mode (Safety Disabled)", group = "Driver Op Mode")
public class BasicOpModeSafetyDisabled extends LinearOpMode {

    /** @noinspection FieldMayBeFinal*/
    private ElapsedTime runtime = new ElapsedTime();
    private Robot robot;

    private Gamepad driverController;
    //private Gamepad armController;

    private boolean flywheelControl = false;

    @Override
    public void runOpMode() {
        driverController = gamepad1;
        //armController = gamepad2;
        robot = new Robot();
        robot.initialize(hardwareMap);

        // Wait for the robot to start (driver presses PLAY).
        telemetry.addData("Status", "Initialized.");
        telemetry.addData("Controls", "Use the following controls:");
        telemetry.addData("Strafe Left", "Left Trigger");
        telemetry.addData("Strafe Right", "Right Trigger");
        telemetry.addData("Forward/Back", "LEFT Stick up/down");
        telemetry.addData("Rotate", "RIGHT Stick left/right");
        telemetry.addData("Flywheel On", "Push X to Switch on");
        telemetry.addData("Flywheel Off", "Push B to Switch off");
        telemetry.addData("Ethan Servo Control", "D-pad Up: Forward, D-pad Down: Reverse");
        telemetry.addData("Good luck!", "DON'T CRASH THE ROBOT PLS :)");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Run until the end of the match (driver presses STOP or time runs out).
        while (opModeIsActive()) {
            updateDrive();
            updateFlywheel();
            updateEthanServo();  // Update Ethan servo control based on D-pad input
            telemetry.update();
        }
    }

    // Update driving controls (tank drive or similar).
    private void updateDrive() {
        double axial = (1.0 * driverController.right_stick_x); // Forward Back
        double lateral = (0.6 * (driverController.left_trigger - driverController.right_trigger)); // Strafing
        double yaw = (-0.6 * driverController.left_stick_y); // Rotate

        robot.updateDriveMotors(axial, lateral, yaw);
    }

    // Update flywheel motors based on X and B button presses
    private void updateFlywheel() {
        // Standard X (on) and B (off) control
        if (driverController.x) {
            flywheelControl = true;
        } else if (driverController.b) {
            flywheelControl = false;
        }

        if (flywheelControl) {
            robot.updateFlywheelMotors(1.0);  // Full speed on flywheel
        } else {
            robot.updateFlywheelMotors(0.0);  // Stop flywheel
        }
    }

    // Update Ethan servo based on D-pad input
    private void updateEthanServo() {
        if (driverController.dpad_down) {
            robot.updateEthanServo(1.0);  // Move Ethan forward (servo position 1.0)
        } else if (driverController.dpad_up) {
            robot.updateEthanServo(0.0);  // Move Ethan reverse (servo position 0.0)
        }
    }
}
