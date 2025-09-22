package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@Autonomous(name = "OwenSummerTest", group = "Autonomous")
public class NewBlueTest extends LinearOpMode {
    int visionOutputPosition = 0;

    public class shooter {
        private DcMotorEx motor1;
        private DcMotorEx motor2;

        public shooter(HardwareMap hardwareMap) {
            motor1 = hardwareMap.get(DcMotorEx.class, "leftFly");
            motor2 = hardwareMap.get(DcMotorEx.class, "rightFly");

        }

        public class SpinUp implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    motor1.setPower(-0.5);
                    motor2.setPower(0.5);
                    initialized = true;
                }
                double vel1 = Math.abs(motor1.getVelocity());
                packet.put("shooterVelocity", vel1);
                return vel1 < 10_000.0;
            }

        }


        public Action spinUp() {
            return new SpinUp();
        }
    }
    public class kick {
        private Servo kicker;

        public kick(HardwareMap hardwareMap) {
            kicker = hardwareMap.get(Servo.class, "ethan");
        }

        public class kickDown implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                kicker.setPosition(-.5);
                return false;
            }
        }
        public Action kickDown() {
            return new kickDown();
        }

        public class kickUp implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                kicker.setPosition(1);
                return false;
            }
        }
        public Action kickUp() {
            return new kickUp();
        }
    }
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(7.00, -70.00, Math.toRadians(90.00));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        shooter shooter = new shooter(hardwareMap);
        kick kick = new kick(hardwareMap);

        while (!isStopRequested() && !opModeIsActive()) {
            int position = visionOutputPosition;
            telemetry.addData("Position during Init", position);
            telemetry.update();
            if (isStopRequested()) return;
        }

        int startPosition = visionOutputPosition;
        telemetry.addData("Starting Position", startPosition);
        telemetry.update();
        waitForStart();

        TrajectoryActionBuilder drive1 = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(7,-60))
                .strafeToLinearHeading(new Vector2d(-7,-35), Math.toRadians(145))
                .waitSeconds(1);

        // Trajectories

        // AutonomousActions.EmergencyArm emergencyArm = new AutonomousActions.EmergencyArm(hardwareMap, telemetry);


        Actions.runBlocking(new SequentialAction(
                drive1.build(),
                new ParallelAction(
                        shooter.spinUp(),
                        new SequentialAction(
                                new SleepAction(1),
                                kick.kickUp(),
                                new SleepAction(1),
                                kick.kickDown(),
                                new SleepAction(1),
                                kick.kickUp(),
                                new SleepAction(1),
                                kick.kickDown(),
                                new SleepAction(1),
                                kick.kickUp(),
                                new SleepAction(1),
                                kick.kickDown()
                            )
                        )


        ));
    }
}
