package frc.robot;

//imports
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

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
    Timer timer;
    

    //Solenoid channels
    public static final int UPSTREAM_SOLENOID_CHANNEL = 0;
    public static final int DOWNSTREAM_SOLENOID_CHANNEL = 0;
    public static final int PRESSURE_SENSOR_CHANNEL = 0;
    public static final int VACUUM_SENSOR_CHANNEL = 0;

    //Solenoid sensor variables
    public static final double VACUUM_SENSOR_GOOD_VAC = 20;
    public static final double VACUUM_SENSOR_MIN_VAC = 15; 

    /**
     * Constructs a new plunger object.
     */
    public Plunger(){
    
        // object initialization
        upstreamSolenoid = new Solenoid(UPSTREAM_SOLENOID_CHANNEL);
        downstreamSolenoid = new Solenoid(DOWNSTREAM_SOLENOID_CHANNEL);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_CHANNEL);
        vacuumSensor = new AnalogInput(VACUUM_SENSOR_CHANNEL);
        timer = new Timer();
        timer.start();
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


       //state change requirements
        switch(state){
            case CLOSED:
            if(buttonPress)
            {
                state = plungerState.VACUUM_ON;
                timer.reset();
            }
           
            break;

            case VACUUM_ON:
            if(getVacuum() >= VACUUM_SENSOR_GOOD_VAC)
            {
                state = plungerState.VACUUM_TO_HOLD;
                timer.reset();    
            }
            else if(buttonPress)
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            
            break;

            case VACUUM_TO_HOLD:
            if(timer.hasPeriodPassed(0.01))
            {
                state = plungerState.HOLD;
                timer.reset();
            }
            else if(buttonPress)
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }

            break;

            case HOLD:
            if(getVacuum() <= VACUUM_SENSOR_MIN_VAC)
            {
                state = plungerState.HOLD_TO_VACUUM;
                timer.reset();
            }
            else if(buttonPress)
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }

            break;

            case HOLD_TO_VACUUM:
            if(timer.hasPeriodPassed(0.01))
            {
                state = plungerState.VACUUM_ON;
                timer.reset();
            }
            else if(buttonPress)
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }

            break;

            case DROP_STATE:
            if(timer.hasPeriodPassed(1))
            {
                state = plungerState.CLOSED;
                timer.reset();
            }

            break;

        }
        //state governs which solenoids are open
        switch(state){
            
            case CLOSED:

            upstreamSolenoid.set(false);
            downstreamSolenoid.set(false);

            break;

            case VACUUM_ON:

            upstreamSolenoid.set(true);
            downstreamSolenoid.set(true);

            break;

            case VACUUM_TO_HOLD:

            upstreamSolenoid.set(true);
            downstreamSolenoid.set(false);
            
            break;

            case HOLD:
 
            upstreamSolenoid.set(false);
            downstreamSolenoid.set(false);
            
            break;

            case HOLD_TO_VACUUM:

            upstreamSolenoid.set(true);
            downstreamSolenoid.set(false);
            
            break;

            case DROP_STATE:

            downstreamSolenoid.set(true);
            upstreamSolenoid.set(false);
            
            break;
        }
    }
    
    /**
     * use button to operate piston
     * @param pistonButton
     */
    public void plungerPiston(boolean pistonButton){
        //run double solenoid!
    }
}