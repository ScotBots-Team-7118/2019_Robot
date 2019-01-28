package frc.robot;

// Imports for the "DriveBase.java" class.
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Talon;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;


/**
 * Framework for our slide drive base and associated methods.
 */
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

    //Stores encoder position
    private double initEncLeft, initEncRight, initEncCenter;

    /**
     * Constructs a new DriveBase object.
     * 
     * @param Gyroscope gyro
     */
    public DriveBase(Gyroscope gyro) {
        //Get instance of gyro
        this.gyro = gyro;
        //initLizes drive talons                   
        talLM = new TalonSRX(DRIVE_TALON_PORT[0]);
        talLF = new TalonSRX(DRIVE_TALON_PORT[1]);
        talRM = new TalonSRX(DRIVE_TALON_PORT[2]);
        talRF = new TalonSRX(DRIVE_TALON_PORT[3]);
        talC = new TalonSRX(DRIVE_TALON_PORT[4]);
       
        //Pairs encoders with talons
        talLM.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
        talRM.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
        talC.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0 );
        
        //Allows for set inverted 
        talLM.setSensorPhase(true);
        talRM.setSensorPhase(true);
        talC.setSensorPhase(true);

        //Gets encdoer value for future reference in getNormalizedPosition 
        initEncLeft = talLM.getSelectedSensorPosition(0);
        initEncRight = talRM.getSelectedSensorPosition(0);
        initEncCenter = talC.getSelectedSensorPosition(0);
    }

    /**
     * Gets the distance traveled by the left side of the robot.
     * @return
     */
    public double getNormalizedPositionL() {
        return talLM.getSelectedSensorPosition(0) - initEncLeft;
    }
        
    /**
     * Gets the distance traveled by the right side of the robot.
     * @return
     */
    public double getNormalizedPositionR() {
        return talRM.getSelectedSensorPosition(0) - initEncRight;
    }

    /**
     * Gets the distance traveled by the center wheels of the robot.
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
     * Accessor method that resets the gyroscope.
     */
    public void resetGyro() {
        gyro.reset();
    }

    /**
     * Sets the left side of the robot to a given velocity,
     * assuming it is within the accepted range of -1 < v < 1.
     * @param v
     */
    public void setLeft(double v) {
        if (Math.abs(v) > 2) {
            System.out.println("Left side velocity out of range!!");
            return;
        }

        if (Math.abs(v) > 0.9) {
            v = 0.9 * (v / Math.abs(v));
        }

        talLM.set(ControlMode.PercentOutput, v);
        talLF.set(ControlMode.Follower, DRIVE_TALON_PORT[0]);
    }

    /**
     * Sets the right side of the robot to a given velocity,
     * assuming it is within the accepted range of -1 < v < 1.
     * @param v
     */
    public void setRight(double v) {
        if (Math.abs(v) > 2) {
            System.out.println("Right side velocity out of range!!");
            return;
        }
        if (Math.abs(v) > 0.9) {
            v = 0.9 * (v / Math.abs(v));
        }
        talRM.set(ControlMode.PercentOutput, v);
        talRF.set(ControlMode.Follower, DRIVE_TALON_PORT[2]);

    }

    /**
     * Sets the center wheels of the robot to a given velocity,
     * assuming it is within the accepted range of -1 < v < 1.
     * @param v
     */
    public void setCenter(double v) {
        if (Math.abs(v) > 2) {
            System.out.println("Right side velocity out of range!!");
            return;
        }
        if (Math.abs(v) > 0.9) {
            v = 0.9 * (v / Math.abs(v));
        }
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
     * Sets the neutral mode on the talons according to a boolean
     * (true = brake mode, false = coast mode).
     * @param brake
     */
    public void brakeMode(boolean brake) {
        // set talon brakemodes
        if(brake){
            talLM.setNeutralMode(NeutralMode.Brake);
            talLF.setNeutralMode(NeutralMode.Brake);
            talRM.setNeutralMode(NeutralMode.Brake);
            talRF.setNeutralMode(NeutralMode.Brake);
            talC.setNeutralMode(NeutralMode.Brake);
        }else{
            talLM.setNeutralMode(NeutralMode.Coast);
            talLF.setNeutralMode(NeutralMode.Coast);
            talRM.setNeutralMode(NeutralMode.Coast);
            talRF.setNeutralMode(NeutralMode.Coast);
            talC.setNeutralMode(NeutralMode.Coast);
        }
    }


    /**
     * Drives the robot according to a single joystick
     * with 360 degrees of freedom.
     * @param axisX
     * @param axisY
     */
    public void teleopDrive(double axisX, double axisY) {
            setRight(0.25*(Math.pow(axisY, 3)));
            setLeft(0.25*(Math.pow(axisY, 3)));
            setCenter(0.25*(Math.pow(axisX, 3)));
    }

    // /**
    //  * Turns the robot a given number of degrees at a set speed.
    //  * @param angle
    //  * @return
    //  */
    // public boolean turn(double angle){
        
    // }

    /**
     * Enables PID control using set values of kF, kP, kI, and kD.
     */
    public void enablePIDControl() {
        
    }
}