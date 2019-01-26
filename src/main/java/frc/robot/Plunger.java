package frc.robot;

//imports
import edu.wpi.first.wpilibj.Solenoid;

/**
 *  Controls plunger mechanism.
 */
public class Plunger{

    // object declaration
    Solenoid solenoid;
    
    /**
     * Constructs a new plunger object.
     */
    public Plunger(){

        // object initialization
        solenoid = new Solenoid(0);
    }

    
    /**
     * Flips solenoid to other setting (T/F).
     */
    public void flipSolenoid(){
        solenoid.set(!solenoid.get());
    }
}