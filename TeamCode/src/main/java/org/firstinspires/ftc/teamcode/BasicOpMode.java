package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utils.LoggingUtils.FTCDashboardPackets;
import org.firstinspires.ftc.teamcode.utils.RobotHardwareInitializer;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.HashMap;

@TeleOp(name = "Driver Op Mode (SAFE)", group = "Driver Op Mode")
public class BasicOpMode extends LinearOpMode {

    /** @noinspection FieldMayBeFinal*/
    private ElapsedTime runtime = new ElapsedTime();
    private Robot robot;

    private Gamepad driverController;

    private DcMotor fl_drv;
    private DcMotor fr_drv;
    private DcMotor bl_drv;
    private DcMotor br_drv;


    private final FTCDashboardPackets dbp = new FTCDashboardPackets("BasicOpMode");

    //private Gamepad armController; This is not needed because we are not using the 2nd controller at this time

    //private boolean flywheelControl = false;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        OpenCvWebcam camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        FtcDashboard.getInstance().startCameraStream(camera, 0);
        driverController = gamepad1;
        //armController = gamepad2;
        robot = new Robot();
        robot.initialize(hardwareMap);

        // Todo: Get rid of ROBOT class and implement robot hardware initializer

        fl_drv = RobotHardwareInitializer.MotorComponent.LEFT_FRONT.get(hardwareMap);
        fr_drv = RobotHardwareInitializer.MotorComponent.RIGHT_FRONT.get(hardwareMap);
        bl_drv = RobotHardwareInitializer.MotorComponent.LEFT_BACK.get(hardwareMap);
        br_drv = RobotHardwareInitializer.MotorComponent.RIGHT_BACK.get(hardwareMap);

        // Wait for the robot to start (driver presses PLAY).
        dbp.createNewTelePacket();
        dbp.info("Status: Initialized");
        dbp.info("Controls Use the following controls:");
        dbp.info("Strafe Left Left Trigger");
        dbp.info("Strafe Right Right Trigger");
        dbp.info("Forward/Back LEFT Stick up/down");
        dbp.info("Rotate RIGHT Stick left/right");
        dbp.info("Flywheel On MUST Hold ALL Left Bumper + Right Bumper + X");
        dbp.info("Flywheel Off Release any Left Bumper / Right Bumper / X");
        dbp.info("Ethan Servo Control D-pad Up: Forward, D-pad Down: Reverse");
        dbp.info("Good luck! DON'T CRASH THE ROBOT PLS :)");
        dbp.send(false);

        waitForStart();
        runtime.reset();

        // Run until the end of the match (driver presses STOP or time runs out).
        while (opModeIsActive()) {
            updateDrive();
            updateFlywheel();
            updateEthanServo();  // Update Ethan servo control based on D-pad input
            dbp.send(false);
        }
    }

    // Update driving controls (tank drive or similar).
    private void updateDrive() {
        double axial = (1.0 * driverController.right_stick_x); // Forward Back
        double lateral = (0.6 * (driverController.left_trigger - driverController.right_trigger)); // Strafing
        double yaw = (-0.6 * driverController.left_stick_y); // Rotate

        // Pass a fourth parameter (0 for now) to match the method signature
        robot.updateDriveMotors(axial, lateral, yaw);
    }

    // Update flywheel motors based on button presses
    private void updateFlywheel() {
        // If both bumpers and X are pressed, start flywheel
        if (driverController.left_bumper && driverController.right_bumper && driverController.x) {
            //flywheelControl = true;  // Enable flywheel control
            robot.updateFlywheelMotors(1.0);  // Run the flywheel at full speed
        } else {
            //flywheelControl = false;  // Disable flywheel control
            robot.updateFlywheelMotors(0.0);  // Stop the flywheel
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
