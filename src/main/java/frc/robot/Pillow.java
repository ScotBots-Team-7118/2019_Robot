package frc.robot;

// Imports needed for Pillow class
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;

/** Methods:
 * public Pillow()
 * public void pillowTest()
 * public boolean isOpen()
 * public boolean isClosed()
 * public void runDoor(int direction)
 * public void runPillow(boolean openingButton, boolean closingButton)
 * public boolean closedState()
 * public void reset()
 */

/**
 * Framework for an object that controls the relevant devices to the Pillow
 * mechanism (Door Talon and Limit Swtiches).
 */
public class Pillow {
    // Object declaration
    private TalonSRX talOpen;
    private DigitalInput limOpen, limClosed;

    // Variable initialization
    private final int PILLOW_TALON_PORT = 3;
    private final double PILLOW_TALON_SPEED = 0.3;
    private final int STOP = 0;
    private final int FORWARDS = 1;
    private final int BACKWARDS = -1;
    
    // Pillow test variables
    private static int iteration = 0;
    private String stateTest ="DefaultValue"; 

    // States for the runPillow() function
    public enum PillowStates {
        OPEN, OPENING, CLOSED, CLOSING
    }

    // Variable representing the current state of the Pillow
    public PillowStates state;

    /**
     * Constructs and intitalizes a new Pillow object.
     */
    public Pillow() {
        // Object initialization
        talOpen = new TalonSRX(PILLOW_TALON_PORT);
        limOpen = new DigitalInput(0);
        limClosed = new DigitalInput(1);

        reset();
    }

    // NOTE: High priority to remove this and start using the state machine with limit switches
    /**
     * Sends pillow values to dashboard
     */
    public void pillowTest() {
        iteration++;
        if((iteration%20) == 0)
        {
            SmartDashboard.putBoolean("Open", isOpen());
            SmartDashboard.putBoolean("Closed", isClosed());
            SmartDashboard.putString("State", stateTest);
        }
    }

    /**
     * Accessor method for the top limit switch (showing that the door is open).
     * 
     * @return the current value of the limit switch (open = true, closed = false)
     */
    public boolean isOpen() {
        return limOpen.get();

    }

    /**
     * Accessor method for the bottom limit switch (showing that the door is
     * closed).
     * 
     * @return the inverse of the current value of the limit switch (closed = true,
     *         open = false)
     */
    public boolean isClosed() {
        return limClosed.get();
    }

    /**
     * Runs the talon in a given direction (input of 0, 1, or -1).
     * 
     * @param direction
     */
    public void runDoor(int direction) {
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
    public void run(boolean openingButton, boolean closingButton) {
        switch (state) {
        // State representing a closed and immobile Pillow door
        case CLOSED:
            stateTest = "Closed";
            // If the appropriate button is pressed, begin opening the pillow door
            if (openingButton) {
                state = PillowStates.OPENING;
            } else {
                // Otherwise, stop the Pillow axle from moving
                runDoor(STOP);
            }
            break;

        // State representing a Pillow door in the process of opening up
        case OPENING:
            stateTest = "Opening";
            // If the appropriate button is pressed and the door isn't open, set the motor
            // to open the door
            if (openingButton && !isOpen()) {
                runDoor(FORWARDS);
            }
            // Otherwise, if the closing button is pressed, stop opening the door
            // and instead start closing it
            else if (closingButton) {
                state = PillowStates.CLOSING;
            }
            // Otherwise, if the limit door is open, stop moving
            else if (isOpen()) {
                runDoor(STOP);
                state = PillowStates.OPEN;
            } else {
                runDoor(STOP);
            }
            break;

        // State representing an open and immobile Pillow door
        case OPEN:
            stateTest = "Open";
            // If the closing button is pressed, begin to close the Pillow door
            if (closingButton) {
                state = PillowStates.CLOSING;
            }
            // Otherwise, keep the pillow door still
            else {
                runDoor(STOP);
            }
            break;

        // State representing a Pillow door in the process of closing
        case CLOSING:
            stateTest = "Closing";
            // If the appropriate button is pressed and the door isn't closed,
            // set the motors to close the Pillow door
            if (closingButton && !isClosed()) {
                runDoor(BACKWARDS);
            }
            // Otherwise, if the opening button is pressed, stop closing the door
            // and instead start opening it
            else if (openingButton) {
                state = PillowStates.OPENING;
            }
            // Otherwise, if the Pillow door is closed, stop the door from moving
            else if (isClosed()) {
                runDoor(STOP);
                state = PillowStates.CLOSED;
            } else {
                runDoor(STOP);
            }
        }
    }

    /**
     * Pseudo-accesor method for the state of the Pillow door. This is used to
     * determine if it is safe to extend the plunger arm or not.
     * 
     * @return If it is safe to extend the plunger arm or not.
     */
    public boolean closedState() {
        if (state == PillowStates.CLOSED || state == PillowStates.CLOSING)
            return true;
        else
            return false;
    }

    /**
     * Resets the Pillow mechanism state for a new match.
     */
    public void reset() {
        state = PillowStates.CLOSED;
    }
}