// Imports needed for Pillow class
package frc.robot;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import java.nio.channels.ClosedByInterruptException;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Pillow class: used to open and close the basket door on the Cargo container
 */
public class Pillow{
    //construsting the pillow class 
    private TalonSRX talOpen;
    private  final int PILLOW_TALON_PORT = 0;
    private final int PILLOW_TALON_SPEED = 0;
    private final int STOP_VELOCITY = 0;
    private DigitalInput limOpen, limClosed;
    public PillowStates state;
    //states for pillow function 
    public enum PillowStates{
        OPEN,
        OPENING,
        CLOSED,
        CLOSING


    }

    
    //intitalizes Pillow class: talons and limit switches
    public Pillow(){
        talOpen = new TalonSRX(PILLOW_TALON_PORT);
        limOpen = new DigitalInput(0);
        limClosed = new DigitalInput(0);

    }

    /**
     * Returns true when the limit switch in open position is pressed
     * @return
     */

    public boolean checkOpen(){
        return limOpen.get();
    }

    public boolean checkClosed(){
        return limClosed.get();
    }
    


    /**
     * Runs the talon at the velocity of v
     * @param v
     */
    public void run(double v){
        talOpen.set(ControlMode.PercentOutput,v);
        
    }
    /**
     * State machine for the running of the pillow
     * May need to change input type
     */
    public void runPillow(boolean buttonPressO, boolean buttonPressC ){
        switch(state){
            //When the door is closed and the appropriate button is pressed move to OPENING state otherwise don't move
            case CLOSED:
            if(buttonPressO){
                state = PillowStates.OPENING;
            }
            else{
                run(STOP_VELOCITY);
            }
            break;

            //When the button is being pressed and the limit switch for open has not been triggered keep opening at correct speed
            //If the closing button is pressed while in opening state switch to CLOSING 
            //If the limit switch is triggered stop moving
            case OPENING:
            if(buttonPressO && limOpen.get() ){
                run(PILLOW_TALON_SPEED);
            }
            else if(buttonPressC){
                state = PillowStates.CLOSING;
            }
            else if(checkOpen()){
                run(STOP_VELOCITY);
                state = PillowStates.OPEN;
            }
            break;

            //If the closing button is pressed move to CLOSING state otherwise don't move
            case OPEN:
            if(buttonPressC){
                state = PillowStates.CLOSING;

            }
            else{
                run(STOP_VELOCITY);
            }
            break;

            //If the closing buton is pressed and the limit switch is not triggered keep opening 
            //If the limit swithc is triggered stop and move to CLOSED
            //If the opening button is pressed move to OPENING
            case CLOSING:
            if(buttonPressC && limClosed.get()){
                run(-PILLOW_TALON_SPEED);
            }
            else if (buttonPressO){
                state = PillowStates.OPENING;

            }
            else if(checkClosed()){
                run(STOP_VELOCITY);
                state = PillowStates.CLOSED;
            }



        }
        }

    }



