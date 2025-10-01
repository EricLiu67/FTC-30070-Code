package core.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.core.control.controllers.PIDFController;
import com.rowanmcalpin.nextftc.core.control.controllers.feedforward.StaticFeedforward;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.MotorEx;
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToPosition;
import com.qualcomm.robotcore.hardware.DcMotor; // <-- needed for ZeroPowerBehavior

public class Intake extends Subsystem {
    // BOILERPLATE
    public static final Intake INSTANCE = new Intake();
    private Intake() { }

    @Config
    public static class IntakeConstants {
        public static double kP = 0.05;
        public static double kI = 0.0;
        public static double kD = 0.001;

        public static int BOOTROTATE_SPEED = -20;
        public static int EXTENDED_TICKS = 680;
    }

    // USER CODE
    public MotorEx intakeMotor;

    public PIDFController controller = new PIDFController(
            IntakeConstants.kP,
            IntakeConstants.kI,
            IntakeConstants.kD,
            new StaticFeedforward(0.0)
    );

    public String name = "intakeMotor";

    public Command retracted() {
        return new RunToPosition(
                intakeMotor, // MOTOR TO MOVE
                IntakeConstants.BOOTROTATE_SPEED, // TARGET POSITION
                controller, // CONTROLLER
                this        // SUBSYSTEM
        );
    }

    public Command extended() {
        return new RunToPosition(
                intakeMotor, // MOTOR TO MOVE
                IntakeConstants.EXTENDED_TICKS, // TARGET POSITION
                controller, // CONTROLLER
                this        // SUBSYSTEM
        );
    }

    @Override
    public void initialize() {
        intakeMotor = new MotorEx(name);

        // 🔹 Ensure the motor resists motion when power = 0
        intakeMotor.getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
