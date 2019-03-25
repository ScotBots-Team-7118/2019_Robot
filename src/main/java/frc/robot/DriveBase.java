package frc.robot;

// Imports for the "DriveBase.java" class.
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;

/** Methods:
 * public DriveBase()
 * public void setLeft(double v)
 * public void setRight(double v)
 * public void setCenter(double v)
 * public void moveForward()
 * public double[] formatDriveJoystick(double x, double y)
 * public void teleopDrive(double[] joyR, double[] joyL)
 */
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

    // Parabolic Drive Constants
    private final double A = 0;
    private final double B = 0;
    private final double C = 0;

    // Minimum joystick movement required for robot control
    private final double JOYSTICK_DEADZONE = 0.1;

    //forward drive speeds
    private final double SPEED_LEFT = 0.3;
    private final double SPEED_RIGHT = 0.3;

    // Drive speed for autonomous movement
    private final double AUTO_DRIVE_SPEED = 0.1;

    // Maximum speed at which the drive train is allowed to move
    private final double MAXIMUM_DRIVE_TALON_INPUT = 0.9;

    /**
     * Constructs a new DriveBase object.
     */
    public DriveBase()
    {
        // Drive Talon Initialization
        talLM = new TalonSRX(LEFT_MASTER_PORT);
        talLF = new TalonSRX(LEFT_FOLLWER_PORT);
        talRM = new TalonSRX(RIGHT_MASTER_PORT);
        talRF = new TalonSRX(RIGHT_FOLLOWER_PORT);
        talC = new TalonSRX(CENTER_PORT);
    }

    /**
     * Sets the left side of the robot to a given velocity, assuming it is within
     * the accepted range of -1 < v < 1.
     * 
     * @param v
     */
    public void setLeft(double v)
    {
        // Checks if the input is out of bounds, and returns out of the method if it is
        if (Math.abs(v) > 2) {
            System.out.println("Left-side velocity out of range for setLeft() in Drive.java");
            return;
        }

        // Makes sure the input velocity isn't above the maximum allowed velocity
        if (Math.abs(v) > MAXIMUM_DRIVE_TALON_INPUT) {
            v = MAXIMUM_DRIVE_TALON_INPUT * (v / Math.abs(v));
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
    public void setRight(double v)
    {
        // Checks if the input is out of bounds, and returns out of the method if it is
        if (Math.abs(v) > 2) {
            System.out.println("Right-side velocity out of range for setRight() in Drive.java");
            return;
        }

        // Makes sure the input velocity isn't above the maximum allowed velocity
        if (Math.abs(v) > MAXIMUM_DRIVE_TALON_INPUT) {
            v = MAXIMUM_DRIVE_TALON_INPUT * (v / Math.abs(v));
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
    public void setCenter(double v)
    {
        // Checks if the input is out of bounds, and returns out of the method if it is
        if (Math.abs(v) > 2) {
            System.out.println("Center velocity out of range for setCenter in Drive.java");
            return;
        }

        // Makes sure the input velocity isn't above the maximum allowed velocity
        if (Math.abs(v) > MAXIMUM_DRIVE_TALON_INPUT) {
            v = MAXIMUM_DRIVE_TALON_INPUT * (v / Math.abs(v));
        }

        // Set the center talon to the new adjusted velocity
        talC.set(ControlMode.PercentOutput, v);
    }

    /**
     * Moves the robot forward at a predetermined speed.
     */
    public void moveForward(boolean button)
    {
        if(button){
        setRight(-SPEED_RIGHT);
        setLeft(SPEED_LEFT);
        }
    }

    /**
     * Formats the joystick axis values into an array for teleop drive.
     * 
     * @param x
     * @param y
     * @return A formatted double array (x-axis, y-axis).
     */
    public double[] formatDriveJoystick(double x, double y)
    {
        double[] joyArray = { x, y };
        return joyArray;
    }

    /**
     * Drives the robot according to two joysticks with
     * two axes of linear freedom (x and y).
     * 
     * @param joyR
     * @param joyL
     */
    public void teleopDrive(double[] joyR, double[] joyL)
    {
        // Insert appropriate values into a new array for driving
        double[] spinReturn = { joyL[1], joyR[1], ((joyR[0] + joyL[0]) / 2) };

        // Parabolic functions for given values (use if needed)
        // spinReturn[0] = (Math.pow(spinReturn[0], 2) * A
        // + spinReturn[0] * B + C) * (Math.abs(spinReturn[0])/spinReturn[0]);
        // spinReturn[1] = (Math.pow(spinReturn[1], 2) * A
        // + spinReturn[1] * B + C) * (Math.abs(spinReturn[1])/spinReturn[1]);
        // spinReturn[2] = (Math.pow(spinReturn[2], 2) * A
        // + spinReturn[2] * B + C) * (Math.abs(spinReturn[2])/spinReturn[2]);

        // If the joystick input is within the deadzone, set the input
        // velocity to the modified joystick input
        if (Math.abs(spinReturn[0]) > JOYSTICK_DEADZONE) {
            spinReturn[0] = Math.pow(spinReturn[0], 3);
        }
        // Otherwise, set the input velocity to 0
        else spinReturn[0] = 0;

        // If the joystick input is within the deadzone, set the input
        // velocity to the modified joystick input
        if (Math.abs(spinReturn[1]) > JOYSTICK_DEADZONE) {
            spinReturn[1] = Math.pow(spinReturn[1], 3);
        }
        // Otherwise, set the input velocity to 0
        else spinReturn[1] = 0;

        // If the joystick input is within the deadzone, set the input
        // velocity to the modified joystick input
        if (Math.abs(spinReturn[2]) > JOYSTICK_DEADZONE) {
            spinReturn[2] = Math.pow(spinReturn[2], 3);
        }
        // Otherwise, set the input velocity to 0
        else spinReturn[2] = 0;

        System.out.println("Left Drive = " + spinReturn[0]);
        System.out.println("Right Drive = " + spinReturn[1]);
        System.out.println("Center Drive = " + spinReturn[2]);

        // Run motors according to new parabolic outputs
        setLeft(spinReturn[0]);
        setRight(spinReturn[1]);
        setCenter(spinReturn[2]);

        SmartDashboard.putNumber("Center", talC.getOutputCurrent());
        SmartDashboard.putNumber("LM", talLM.getOutputCurrent());
        SmartDashboard.putNumber("RM", talRM.getOutputCurrent());
        SmartDashboard.putNumber("LF", talLF.getOutputCurrent());
        SmartDashboard.putNumber("RF", talRF.getOutputCurrent());
    }
}