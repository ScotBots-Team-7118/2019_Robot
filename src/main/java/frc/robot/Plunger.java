package frc.robot;

//imports
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 *  Controls plunger mechanism.
 */
public class Plunger{

    // object declaration
    Solenoid upstreamSolenoid;
    Solenoid downstreamSolenoid;
    AnalogInput pressureSensor;
    AnalogInput vacuumSensor;
    plungerState state;

    //Solenoid channels
    public static final int UPSTREAM_SOLENOID_CHANNEL = 0;
    public static final int DOWNSTREAM_SOLENOID_CHANNEL = 0;
    public static final int PRESSURE_SENSOR_CHANNEL = 0;
    public static final int VACUUM_SENSOR_CHANNEL = 0;

    /**
     * Constructs a new plunger object.
     */
    public Plunger(){

        // object initialization
        upstreamSolenoid = new Solenoid(UPSTREAM_SOLENOID_CHANNEL);
        downstreamSolenoid = new Solenoid(DOWNSTREAM_SOLENOID_CHANNEL);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_CHANNEL);
        vacuumSensor = new AnalogInput(VACUUM_SENSOR_CHANNEL);
    }

    /**
     * convert pressure sensor volts to psi
     */
    public double getPressure(){
        return ((pressureSensor.getVoltage()*50.0)-25.0);
    }

    /**
     * convert vacuum sensor volts to psi
     */
    public double getVacuum(){
        return ((vacuumSensor.getVoltage()*11.125)-20.0625);
    }

    /**
     * declares plunger states
     */
    public enum plungerState {

        VACUUM_ON,
        DROP_STATE,
        CLOSED,
        VACUUM_TO_HOLD,
        HOLD,
        HOLD_TO_VACUUM
    }
    
    /**
     * control pulse, pickup, and dropping game piece 
     * @param buttonPress
     */
    public void runPlunger(boolean buttonPress){
        
        switch(state){
            
            case CLOSED:
            if (buttonPress){
                state = plungerState.DROP_STATE;
            }
            else
            {

            }

            break;

            case VACUUM_ON:
            if (buttonPress){
                state = plungerState.DROP_STATE;
            }
            else
            {

            }

            break;

            case VACUUM_TO_HOLD:
            if (buttonPress){
                state = plungerState.DROP_STATE;
            }
            else
            {

            }
            
            break;

            case HOLD:
            if (buttonPress){
                state = plungerState.DROP_STATE;
            }
            else
            {

            }
            
            break;

            case HOLD_TO_VACUUM:
            if (buttonPress){
                state = plungerState.DROP_STATE;
            }
            else
            {

            }
            
            break;

            case DROP_STATE:
            break;
        }
    }
    
    /**
     * cont
     * @param buttonPress
     */
    public void plungerPiston(boolean buttonPress){
        
    }

    // /**
    //  * Flips solenoid to other setting (T/F).
    //  */
    // public void flipSolenoid(){
    //     upstreamSolenoid.set(!upstreamSolenoid.get());
    // }

}