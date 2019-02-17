// Imports needed for Pillow class
package frc.robot;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * Framework for an object that controls the relevant devices to the Pillow mechanism
 * (Door Talon and Limit Swtiches).
 */
public class Pillow {
    // Object declaration
    private TalonSRX talOpen;
    private DigitalInput limOpen, limClosed;

    // Variable initialization
    private final int PILLOW_TALON_PORT = 0;
    private final int PILLOW_TALON_SPEED = 0;
    private final int STOP = 0;
    private final int FORWARDS = 1;
    private final int BACKWARDS = -1;

    // States for the runPillow() function
    public enum PillowStates{
        OPEN,
        OPENING,
        CLOSED,
        CLOSING
    }
    public PillowStates state;
    
    /**
     * Constructs and intitalizes a new Pillow object.
     */
    public Pillow() {
        // Object initialization
        talOpen = new TalonSRX(PILLOW_TALON_PORT);
        limOpen = new DigitalInput(0);
        limClosed = new DigitalInput(0);
    }

    /**
     * Accessor method for the top limit switch (showing that the door is open).
     * @return the current value of the limit switch (open = true, closed = false)
     */
    public boolean isOpen(){
        return limOpen.get();
    }

    /**
     * Accessor method for the bottom limit switch (showing that the door is closed).
     * @return the inverse of the current value of the limit switch (closed = true, open = false)
     */
    public boolean isClosed(){
        return limClosed.get();
    }
    

    // NOTE: Does this need to take an input of a double or can we just use +-1?
    /**
     * Runs the talon in a given direction (input of 0, 1, or -1).
     * @param direction
     */
    public void run(int direction){
        if (direction == 0) talOpen.set(ControlMode.PercentOutput, 0);
        else if (direction == 1 || direction == -1) talOpen.set(ControlMode.PercentOutput, direction*PILLOW_TALON_SPEED);
        else {
            System.out.println("Invalid input for run(direction) in Pillow.java.");
            talOpen.set(ControlMode.PercentOutput, 0);
        }
    }

    /**
     * Runs the Pillow based various states of the mechanical device.
     * @param openingButton
     * @param closingButton
     */
    public void runPillow(boolean openingButton, boolean closingButton){
        switch(state){
            // State representing a closed and immobile Pillow door
            case CLOSED:
            // If the appropriate button is pressed, begin opening the pillow door
            if (openingButton) {
                state = PillowStates.OPENING;
            }
            else {
                // Otherwise, stop the Pillow axle from moving
                run(STOP);
            }
            break;

            // State representing a Pillow door in the process of opening up
            case OPENING:
            // If the appropriate button is pressed and the door isn't open, set the motor to open the door
            if(openingButton && !isOpen()){
                run(FORWARDS);
            }
            // Otherwise, if the closing button is pressed, stop opening the door
            // and instead start closing it
            else if(closingButton){
                state = PillowStates.CLOSING;
            }
            // Otherwise, if the limit door is open, stop moving
            else if(isOpen()){
                run(STOP);
                state = PillowStates.OPEN;
            }
            break;

            // State representing an open and immobile Pillow door
            case OPEN:
            // If the closing button is pressed, begin to close the Pillow door
            if (closingButton) {
                state = PillowStates.CLOSING;
            }
            // Otherwise, keep the pillow door still
            else {
                run(STOP);
            }
            break;

            // State representing a Pillow door in the process of closing
            case CLOSING:
            // If the appropriate button is pressed and the door isn't closed,
            // set the motors to close the Pillow door
            if (closingButton && !isClosed()) {
                run(BACKWARDS);
            }
            // Otherwise, if the opening button is pressed, stop closing the door
            // and instead start opening it
            else if (openingButton) {
                state = PillowStates.OPENING;
            }
            // Otherwise, if the Pillow door is closed, stop the door from moving
            else if (isClosed()) {
                run(STOP);
                state = PillowStates.CLOSED;
            }
        }
    }

    /**
     * Pseudo-accesor method for the state of the Pillow door.
     * This is used to determine if it is safe to extend the plunger arm or not.
     * @return If it is safe to extend the plunger arm or not.
     */
    public boolean closedState() {
        if (state == PillowStates.CLOSED || state == PillowStates.CLOSING) return true;
        else return false;
    }
}