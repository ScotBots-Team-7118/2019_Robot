// Imports needed for Pillow class
package frc.robot;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Pillow class: used to open and close the basket door on the Cargo container
 */
public class Pillow{
    
    private TalonSRX talOpen;
    private  final int PILLOW_TALON_PORT = 0;
    private DigitalInput limO, limC;
    private enum PillowStates{

    }

    
    //intitalizes Pillow class: talons and limit switches
    public Pillow(){
        talOpen = new TalonSRX(PILLOW_TALON_PORT);
        limO = new DigitalInput(0);
        limC = new DigitalInput(0);

    }


    /**
     * Runs the talon at the velocity of v
     * @param v
     */
    public void run(double v){
        talOpen.set(ControlMode.PercentOutput,v);
        

    }
}


