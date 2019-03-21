package frc.robot;

// Imports needed for Pillow class
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;

/** Methods:
 * public Pillow()
 * public boolean isOpen()
 * public boolean isClosed()
 * public void runDoor(int direction)
 * public void run(boolean openingButton, boolean closingButton)
 * public boolean closedState()
 * public void reset()
 * public void changeState(PillowStates newState)
 */

/**
 * Framework for an object that controls the relevant devices to the Pillow
 * mechanism (Door Talon and Limit Swtiches).
 */
public class Pillow {
    // Object declaration
    private TalonSRX talOpen;
    private DigitalInput limClosed;

    // Variable initialization
    private final int PILLOW_TALON_PORT = 3;
    private final double PILLOW_TALON_SPEED = 0.3;
    private final int STOP = 0;
    private final int FORWARDS = 1;
    private final int BACKWARDS = -1;

    // States for the run() function
    public enum PillowStates {
        OPENING, CLOSED, CLOSING, IDLE
    }

    private PillowStates state;
    private String stateValue;

    /**
     * Constructs and intitalizes a new Pillow object.
     */
    public Pillow()
    {
        // Object initialization
        talOpen = new TalonSRX(PILLOW_TALON_PORT);
        talOpen.setInverted(true);
        limClosed = new DigitalInput(1);
        
        reset();
    }

    /**
     * Accessor method for the top limit switch (showing that the door is open).
     * 
     * @return the current value of the limit switch (open = true, closed = false)
     */
    public boolean isOpen()
    {
        return !isClosed();
    }

    /**
     * Accessor method for the bottom limit switch (showing that the door is
     * closed).
     * 
     * @return the inverse of the current value of the limit switch (closed = true,
     *         open = false)
     */
    public boolean isClosed()
    {
        return !limClosed.get();
    }

    // NOTE: Does this need to take an input of a double or can we just use +-1?
    /**
     * Runs the door talon in a given direction (input of 0, 1, or -1).
     * 
     * @param direction
     */
    public void runDoor(int direction)
    {
        if (direction == 0)
            talOpen.set(ControlMode.PercentOutput, 0);
        else if (direction == 1 || direction == -1)
            talOpen.set(ControlMode.PercentOutput, direction * PILLOW_TALON_SPEED);
        else {
            System.out.println("Invalid input for runDoor(direction) in Pillow.java.");
            talOpen.set(ControlMode.PercentOutput, 0);
        }
    }

    /**
     * Runs the Pillow based various states of the mechanical device.
     * 
     * @param openingButton
     * @param closingButton
     */
    public void run(boolean openingButton, boolean closingButton)
    {
        // Send the current limit switch values to the SmartDashboard
        SmartDashboard.putBoolean("Open", isOpen());
        SmartDashboard.putBoolean("Closed", isClosed());

        switch (state)
        {
        case IDLE:
            stateValue = "Idle";
            // If the door is closed, change to represent a closed state
            if (isClosed())
            {
                changeState(PillowStates.CLOSED);
            }
            // Otherwise, if the closing button is pressed, change state to close the door
            else if (closingButton)
            {
                changeState(PillowStates.CLOSING);
            }
            // Otherwise, if the opening button is pressed, change state to open the door
            else if (openingButton)
            {
                changeState(PillowStates.OPENING);
            }
            // Otherwise, keep the door idle (as the state dictates)
            else runDoor(STOP);
            break;

        case CLOSED:
            stateValue = "Closed";
            // If the door is open, change to represent an open and idle state
            if (isOpen())
            {
                changeState(PillowStates.IDLE);
            }
            // Otherwise, if the opening button is pressed, change state to open the door
            else if (openingButton)
            {
                changeState(PillowStates.OPENING);
            }
            // Otherwise, keep the door closed and idel as the state dictates
            else runDoor(STOP);
            break;

        case OPENING:
            stateValue = "Opening";
            // If the opening button isn't pressed,
            // change state to represent an idle, open door
            if (!openingButton)
            {
                changeState(PillowStates.IDLE);
            }
            // Otherwise, if the closing button is pressed, change state to close the door
            else if (closingButton)
            {
                changeState(PillowStates.CLOSED);
            }
            // Otherwise, run the pillow door to open as the state dictates
            else runDoor(FORWARDS);
            break;

        case CLOSING:
            stateValue = "Closing";
            // If the pillow door is closed completely,
            // change state to represent a closed, idle door
            if (isClosed())
            {
                changeState(PillowStates.CLOSED);
            }
            // Otherwise, if the closing button isn't pressed,
            // change state to represent an open, idle door
            else if (!closingButton)
            {
                changeState(PillowStates.IDLE);
            }
            // Otherwise, if the opening button is pressed, change state to open the door
            else if (openingButton)
            {
                changeState(PillowStates.OPENING);
            }
            // Otherwise, run the pillow door to close as the state dictates
            else runDoor(BACKWARDS);
            break;
        }

        // Send the state that ran to the SmartDashboard
        SmartDashboard.putString("Pillow State", stateValue);
    }

    /**
     * Pseudo-accesor method for the state of the Pillow door. This is used to
     * determine if it is safe to extend the plunger arm or not.
     * 
     * @return If it is safe to extend the plunger arm or not.
     */
    public boolean closedState()
    {
        if (state == PillowStates.CLOSED || state == PillowStates.CLOSING)
            return true;
        else
            return false;
    }

    /**
     * Resets the state of the pillow for robot initialization.
     */
    public void reset()
    {
        if (isClosed()) changeState(PillowStates.CLOSED);
        else changeState(PillowStates.IDLE);
    }

    /**
     * Changes the state of the Pillow and resets the necessary sensors/devices.
     * @param newState
     */
    public void changeState(PillowStates newState)
    {
        state = newState;
        runDoor(STOP);
    }
}