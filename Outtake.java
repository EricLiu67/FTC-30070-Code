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
import com.rowanmcalpin.nextftc.ftc.hardware.controllables.RunToVelocity;

public class Outtake extends Subsystem {
    // BOILERPLATE
    public static final Outtake INSTANCE = new Outtake();
    private Outtake() { }

    @Config
    public static class OuttakeConstants {
        public static double kP = 0.05;
        public static double kI = 0.0;
        public static double kD = 0.001;

        public static int OUTTAKEROTATESPEED = 200;
    }

    // USER CODE
    public MotorEx outtakeMotor;

    public PIDFController controller = new PIDFController(
            OuttakeConstants.kP,
            OuttakeConstants.kI,
            OuttakeConstants.kD,
            new StaticFeedforward(0.0)
    );

    public String name = "outtakeMotor";

    public Command OuttakeRotating() {
        return new RunToVelocity(
                outtakeMotor,
                OuttakeConstants.OUTTAKEROTATESPEED,
                controller,
                this
        );
    }

    @Override
    public void initialize() {
        outtakeMotor = new MotorEx(name);

        // 🔹 Ensure the motor resists motion when power = 0
        outtakeMotor.getMotor().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        outtakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
