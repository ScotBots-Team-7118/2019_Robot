package frc.robot;

// Imports for the "DriveBase.java" class.
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Framework for our slide drive base and associated methods.
 */
public class DriveBase {
    // Object declaraction
    private TalonSRX talLM, talLF, talRM, talRF, talC;
    // Constant inititalization
    // Talon Port values
    private final int RIGHT_MASTER_PORT = 7;
    private final int RIGHT_FOLLOWER_PORT = 4;
    private final int LEFT_MASTER_PORT = 5;
    private final int LEFT_FOLLWER_PORT = 1;
    private final int CENTER_PORT = 6;

    // Minimum joystick movement required for robot control
    private final double JOYSTICK_DEADZONE = 0.1;

    // Drive speed for autonomous movement
    private final Double AUTO_DRIVE_SPEED = 0.1;

    // // Constants for PID drive
    // private final double kF = 0;
    // private final double kP = 0;
    // private final double kI = 0;
    // private final double kD = 0;

    // // Variables for the turn method
    // private final double TURN_OFFSET = 0;
    // private final double MIN_TURN_SPEED = 0;
    // private final double MAX_TURN_SPEED = 0;
    // private final double MIN_DEGREES_FULL_SPEED = 0;

    // // The number of encoder rotations in a single foot
    // private final int ROTATIONS_TO_FEET = 0;

    // Maximum speed at which the drive train is allowed to move
    private final double MAXIMUM_DRIVE_TALON_INPUT = 0.9;

    // The encoder positions from the last time they were reset
    private double initEncLeft, initEncRight, initEncCenter;

    /**
     * Constructs a new DriveBase object.
     */
    public DriveBase() {
        // Drive Talon Initialization
        talLM = new TalonSRX(LEFT_MASTER_PORT);
        talLF = new TalonSRX(LEFT_FOLLWER_PORT);
        talRM = new TalonSRX(RIGHT_MASTER_PORT);
        talRF = new TalonSRX(RIGHT_FOLLOWER_PORT);
        talC = new TalonSRX(CENTER_PORT);

        // Pairs encoders with their respective Talons
        talLM.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        talRM.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        talC.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        // Gets encdoer value for future reference in getNormalizedPosition
        initEncLeft = talLM.getSelectedSensorPosition(0);
        initEncRight = talRM.getSelectedSensorPosition(0);
        initEncCenter = talC.getSelectedSensorPosition(0);
    }

    /**
     * Gets the distance traveled by the left side of the robot.
     * 
     * @return
     */
    public double getNormalizedPositionL() {
        return talLM.getSelectedSensorPosition(0) - initEncLeft;
    }

    /**
     * Gets the distance traveled by the right side of the robot.
     * 
     * @return
     */
    public double getNormalizedPositionR() {
        return talRM.getSelectedSensorPosition(0) - initEncRight;
    }

    /**
     * Gets the distance traveled by the center wheels of the robot.
     * 
     * @return
     */
    public double getNormalizedPositionC() {
        return talC.getSelectedSensorPosition(0) - initEncCenter;
    }

    /**
     * Resets the distance traveled by the encoders for the normalized positions.
     */
    public void resetEncoders() {
        initEncLeft = talLM.getSelectedSensorPosition(0);
        initEncRight = talRM.getSelectedSensorPosition(0);
        initEncCenter = talC.getSelectedSensorPosition(0);
    }

    /**
     * Sets the left side of the robot to a given velocity, assuming it is within
     * the accepted range of -1 < v < 1.
     * 
     * @param v
     */
    public void setLeft(double v) {
        // Checks if the input is out of bounds, and returns out of the method if it is
        if (Math.abs(v) > 2) {
            System.out.println("Left side velocity out of range!!");
            return;
        }

        // Makes sure the input velocity isn't above the maximum allowed velocity
        if (Math.abs(v) > MAXIMUM_DRIVE_TALON_INPUT) {
            v = MAXIMUM_DRIVE_TALON_INPUT * (v / Math.abs(v));
        }
        // Makes sure the input velocity isn't less than the joystick deadzone
        else if (Math.abs(v) < JOYSTICK_DEADZONE) {
            // If it is, set it to 0
            v = 0;
        }

        // Set the left-side talons to the new adjusted velocity
        talLM.set(ControlMode.PercentOutput, v);
        talLF.set(ControlMode.Follower, LEFT_MASTER_PORT);
    }

    /**
     * Sets the right side of the robot to a given velocity, assuming it is within
     * the accepted range of -1 < v < 1.
     * 
     * @param v
     */
    public void setRight(double v) {
        // Checks if the input is out of bounds, and returns out of the method if it is
        if (Math.abs(v) > 2) {
            System.out.println("Right side velocity out of range!!");
            return;
        }

        // Makes sure the input velocity isn't above the maximum allowed velocity
        if (Math.abs(v) > MAXIMUM_DRIVE_TALON_INPUT) {
            v = MAXIMUM_DRIVE_TALON_INPUT * (v / Math.abs(v));
        }
        // Makes sure the input velocity isn't less than the joystick deadzone
        else if (Math.abs(v) < JOYSTICK_DEADZONE) {
            // If it is, set it to 0
            v = 0;
        }

        // Set the right-side talons to the new adjusted velocity
        talRM.set(ControlMode.PercentOutput, v);
        talRF.set(ControlMode.Follower, RIGHT_MASTER_PORT);
    }

    /**
     * Sets the center wheels of the robot to a given velocity, assuming it is
     * within the accepted range of -1 < v < 1.
     * 
     * @param v
     */
    public void setCenter(double v) {
        // Checks if the input is out of bounds, and returns out of the method if it is
        if (Math.abs(v) > 2) {
            System.out.println("Right side velocity out of range!!");
            return;
        }

        // Makes sure the input velocity isn't above the maximum allowed velocity
        if (Math.abs(v) > MAXIMUM_DRIVE_TALON_INPUT) {
            v = MAXIMUM_DRIVE_TALON_INPUT * (v / Math.abs(v));
        }
        // Makes sure the input velocity isn't less than the joystick deadzone
        else if (Math.abs(v) < JOYSTICK_DEADZONE) {
            // If it is, set it to 0
            v = 0;
        }

        // Set the center talon to the new adjusted velocity
        talC.set(ControlMode.PercentOutput, v);
    }

    /**
     * Moves the robot forward at a predetermined speed.
     */
    public void moveForward() {
        setRight(AUTO_DRIVE_SPEED);
        setLeft(AUTO_DRIVE_SPEED);
    }

    /**
     * Sets the neutral mode on the talons according to a boolean (true = brake
     * mode, false = coast mode).
     * 
     * @param brake
     */
    public void brakeMode(boolean brake) {
        if (brake) {
            talLM.setNeutralMode(NeutralMode.Brake);
            talLF.setNeutralMode(NeutralMode.Brake);
            talRM.setNeutralMode(NeutralMode.Brake);
            talRF.setNeutralMode(NeutralMode.Brake);
            talC.setNeutralMode(NeutralMode.Brake);
        } else {
            talLM.setNeutralMode(NeutralMode.Coast);
            talLF.setNeutralMode(NeutralMode.Coast);
            talRM.setNeutralMode(NeutralMode.Coast);
            talRF.setNeutralMode(NeutralMode.Coast);
            talC.setNeutralMode(NeutralMode.Coast);
        }
    }

    /**
     * Formats the joystick axis values into an array for teleop drive.
     * 
     * @param x
     * @param y
     * @return A formatted double array (x-axis, y-axis).
     */
    public double[] formatDriveJoystick(double x, double y) {
        // NOTE: Z-axis likely not needed, maybe should remove later
        double[] joyArray = { x, y };
        return joyArray;
    }

    /**
     * Drives the robot according to a single joystick with two axes of linear
     * freedom (x and y).
     * 
     * @param joyR
     * @param joyL
     */
    public void teleopDrive(double[] joyR, double[] joyL) {
        double[] spinReturn = { joyL[1], joyR[1], ((joyR[0] + joyL[0]) / 2) };

        // input joy commands into drive methods w/ cubic function
        setLeft(Math.pow(spinReturn[0], 3));
        setRight(Math.pow(spinReturn[1], 3));
        setCenter(Math.pow(spinReturn[2], 3));
    }

    /**
     * 
     * Enables or disables PID control using set values of kF, kP, kI, and kD.
     * 
     * @param mode
     */
    public void pidControl(boolean mode) {
        if (mode) {

        } else {

        }
    }
}