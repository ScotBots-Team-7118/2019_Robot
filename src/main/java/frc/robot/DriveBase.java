package frc.robot;

// Imports for the "DriveBase.java" class.
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Talon;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.usfirst.frc.team7118.robot.RobotObject;

public class DriveBase {
    // Object declaraction
    private Gyroscope gyro;
    private TalonSRX talLM, talLF, talRM, talRF, talC;
    // Constant inititalization
    // Talon Port values  
    // Order as follows: Right-Master, Right-Follower, Left-Master, Left-Follower, Center
    private final int[] DRIVE_TALON_PORT = {0, 0, 0, 0, 0};
    // Minimum joystick movement required for robot control
    private final int JOYSTICK_DEADZONE = 0;
    // Drive speed for autonomous movement
    private final int AUTO_DRIVE_SPEED = 0;
    // Constants for PID drive
    private final double kF = 0;
    private final double kP = 0;
    private final double kI = 0;
    private final double kD = 0;
    // Methods for turn method
    private final double TURN_OFFSET = 0;
    private final double MIN_TURN_SPEED = 0;
    private final double MAX_TURN_SPEED = 0;
    private final double MIN_DEGREES_FULL_SPEED = 0;
    // The number of encoder rotations in a single food
    private final int ROTATIONS_TO_FEET = 0;
    // Maximum speed at which the drive train is allowed to move
    private final int MAXIMUM_DRIVE_TALON_INPUT = 0;
    



    public Drive (Gyroscope gyro){
        //initlizes drive talons
        talLM = new TalonSRX(DRIVE_TALON_PORT[0]);
        talLF = new TalonSRX(DRIVE_TALON_PORT[1]);
        talRM = new TalonSRX(DRIVE_TALON_PORT[2]);
        talRF = new TalonSRX(DRIVE_TALON_PORT[3]);
        talC = new TalonSRX(DRIVE_TALON_PORT[4]);

        talLM.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talRM.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        talC.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

        

    }
}