package core.opModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.NextFTCOpMode;

import core.subsystems.Intake;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;



@TeleOp(name = "Teleop")
public class Teleop extends NextFTCOpMode {

    public Teleop() {
        //super(Intake.INSTANCE);
        super();
    }

    private Follower follower;
    private final Pose startPose = new Pose(0, 0, 0);

    @Override
    public void onStartButtonPressed() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        follower.startTeleopDrive();

        //gamepadManager.getGamepad1().getCircle().setPressedCommand(Intake.INSTANCE::extended);
        //gamepadManager.getGamepad1().getSquare().setPressedCommand(Intake.INSTANCE::retracted);

        follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();

        /* Telemetry Outputs of our Follower */
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading in Degrees", Math.toDegrees(follower.getPose().getHeading()));

        /* Update Telemetry to the Driver Hub */
        telemetry.update();
    }
}