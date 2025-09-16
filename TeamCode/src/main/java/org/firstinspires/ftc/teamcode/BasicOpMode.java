package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Driver Op Mode (SAFE)", group = "Driver Op Mode")
public class BasicOpMode extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private Robot robot;

    private Gamepad driverController;
    private Gamepad armController;

    private boolean flywheelControl = false;

    @Override
    public void runOpMode() {
        driverController = gamepad1;
        armController = gamepad2;
        robot = new Robot();
        robot.initialize(hardwareMap);

        // Wait for the robot to start (driver presses PLAY).
        telemetry.addData("Status", "Initialized.");
        telemetry.addData("Controls", "Use the following controls:");
        telemetry.addData("Strafe Left", "Left Trigger");
        telemetry.addData("Strafe Right", "Right Trigger");
        telemetry.addData("Forward/Back", "LEFT Stick up/down");
        telemetry.addData("Rotate", "RIGHT Stick left/right");
        telemetry.addData("Flywheel On", "MUST Hold ALL Left Bumper + Right Bumper + X");
        telemetry.addData("Flywheel Off", "Release any Left Bumper / Right Bumper / X");
        telemetry.addData("Ethan Servo Control", "D-pad Up: Forward, D-pad Down: Reverse");
        telemetry.addData("Good luck!", "DONT CRASH THE ROBOT PLS :)");
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

        // Pass a fourth parameter (0 for now) to match the method signature
        robot.updateDriveMotors(axial, lateral, yaw, 0);
    }

    // Update flywheel motors based on button presses
    private void updateFlywheel() {
        // If both bumpers and X are pressed, start flywheel
        if (driverController.left_bumper && driverController.right_bumper && driverController.x) {
            flywheelControl = true;  // Enable flywheel control
            robot.updateFlywheelMotors(1.0);  // Run the flywheel at full speed
        } else {
            flywheelControl = false;  // Disable flywheel control
            robot.updateFlywheelMotors(0.0);  // Stop the flywheel
        }
    }

    // Update Ethan servo based on D-pad input
    private void updateEthanServo() {
        if (driverController.dpad_up) {
            robot.updateEthanServo(1.0);  // Move Ethan forward (servo position 1.0)
        } else if (driverController.dpad_down) {
            robot.updateEthanServo(0.0);  // Move Ethan reverse (servo position 0.0)
        }
    }
}
