package core.opModes;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.lynx.LynxModule;

import java.util.List;


@TeleOp
public class Teleop extends LinearOpMode {

    @Config
    public static class MotorTuning {
        public static double outtakeMotorPower = -0.6;
        public static double intakeMotorPower = 2;
    }

    @Override
    public void runOpMode() throws InterruptedException {

// Declare our motors
// Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("leftFront");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("leftRear");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("rightFront");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("rightRear");

        DcMotor intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        DcMotorEx outtakeMotor = hardwareMap.get(DcMotorEx.class, "outtakeMotor");

        outtakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Servo blockServo = hardwareMap.get(Servo.class, "rightServo");

        boolean test = false;

        blockServo.setPosition(0.5);

// Reverse the right side motors. This may be wrong for your setup.
// If your robot moves backwards when commanded to go forwards,
// reverse the left side instead.
// See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x * 0.7;
// Denominator is the largest motor power (absolute value) or 1
// This ensures all the powers maintain the same ratio,
// but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            double velocity = outtakeMotor.getVelocity();

            if (gamepad1.right_trigger > 0.3) {
                blockServo.setPosition(0.75);
                intakeMotor.setPower(0.3);
            } else {
                intakeMotor.setPower(0);
                blockServo.setPosition(0.5);
            }

            if (gamepad1.cross) {
                outtakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset
                outtakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Set mode
                blockServo.setPosition(0.5);
                outtakeMotor.setPower(-0.6);// Set power
                intakeMotor.setPower(-0.3);
                sleep(100);
                intakeMotor.setPower(0);
            }

            if (gamepad1.triangle) {
                outtakeMotor.setPower(0);
            }

            if (gamepad1.dpad_down) {
                outtakeMotor.setPower(0.6);
            }
            if (gamepad1.dpad_right) {
                intakeMotor.setPower(-0.4);
            }

            if (gamepad1.dpad_left) {
                while (outtakeMotor.getVelocity() != 0) {
                    if (outtakeMotor.getVelocity() > 50) {
                        outtakeMotor.setPower(-0.2);
                    } else if (outtakeMotor.getVelocity() < -50) {
                        outtakeMotor.setPower(0.2);
                    } else {
                        outtakeMotor.setPower(0);
                    }
                }
            }


            if (gamepad1.left_trigger > 0.3) {
                intakeMotor.setPower(gamepad1.left_trigger);
            }

                telemetry.addData("outtakeVelocity", velocity);
                telemetry.addData("servoDir", blockServo.getPosition());
                telemetry.update();


        }
    }
}
