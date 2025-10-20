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
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;


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

        Servo servo1 = hardwareMap.get(Servo.class, "leftServo");
        Servo servo2 = hardwareMap.get(Servo.class, "rightServo");
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

            if (gamepad1.right_trigger > 0.3) {
                intakeMotor.setPower(1);
            } else {
                intakeMotor.setPower(0);
            }

            if (gamepad1.cross) {
                outtakeMotor.setPower(-0.5);
                while (outtakeMotor.getVelocity() < 0.5) {
                    sleep(10);
                }

                servo1.setPosition(0.0);
                servo2.setPosition(1.0);

                while (outtakeMotor.getVelocity() < 0.5) {
                    sleep(10);
                }

                intakeMotor.setPower(-0.4);
                sleep(500);
                servo1.setPosition(0.0);
                servo2.setPosition(1.0);
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

            if (gamepad1.right_bumper) {
                servo1.setPosition(0.0);
                servo2.setPosition(1.0);
            } else if (gamepad1.left_bumper) {
                servo1.setPosition(1.0);
                servo2.setPosition(0.0);
            } else {
                servo1.setPosition(0.5);
                servo2.setPosition(0.5);
            }

        }
    }
}
