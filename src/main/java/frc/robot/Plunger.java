package frc.robot;

// Imports for the Plunger Class
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

/**
 *  Framework for an object that controls the plunger mechanism.
 */
public class Plunger {
    // Object declaration
    Solenoid upstreamSolenoid;
    Solenoid downstreamSolenoid;
    AnalogInput pressureSensor;
    AnalogInput vacuumSensor;
    Timer timer;
    
    // The various states of the plunger device
    public enum plungerState {
        VACUUM_ON,
        DROP_STATE,
        CLOSED,
        VACUUM_TO_HOLD,
        HOLD,
        HOLD_TO_VACUUM
    }
    plungerState state;

    // Solenoid channels
    public static final int UPSTREAM_SOLENOID_CHANNEL = 1;
    public static final int DOWNSTREAM_SOLENOID_CHANNEL = 2;
    public static final int PRESSURE_SENSOR_CHANNEL = 3;
    public static final int VACUUM_SENSOR_CHANNEL = 4;

    // Solenoid sensor variables
    public static final double VACUUM_SENSOR_GOOD_VAC = 20;
    public static final double VACUUM_SENSOR_MIN_VAC = 15; 

    /**
     * Constructs a new plunger object.
     */
    public Plunger() {
    
        // object initialization
        upstreamSolenoid = new Solenoid(UPSTREAM_SOLENOID_CHANNEL);
        downstreamSolenoid = new Solenoid(DOWNSTREAM_SOLENOID_CHANNEL);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_CHANNEL);
        vacuumSensor = new AnalogInput(VACUUM_SENSOR_CHANNEL);
        timer = new Timer();
        timer.start();
    }

    /**
     * Converts the pressure sensor volts to psi.
     */
    public double getPressure() {
        return ((pressureSensor.getVoltage()*50.0)-25.0);
    }

    /**
     * Converts the vacuum sensor volts to psi.
     */
    public double getVacuum() {
        return ((vacuumSensor.getVoltage()*11.125)-20.0625);
    }
    
    /**
     * Runs the plunger device by controlling pulse, pick-up, and drop-off of the hatch. 
     * @param buttonPress
     */
    public void runPlunger(boolean buttonPress) {
       // State change requirements
        switch(state){
            // NOTE: NATHANIEL COMMENT YOUR STATE MACHINE - Manoli
            case CLOSED:
            if (buttonPress) {
                state = plungerState.VACUUM_ON;
                timer.reset();
            }
            break;

            case VACUUM_ON:
            if (getVacuum() >= VACUUM_SENSOR_GOOD_VAC) {
                state = plungerState.VACUUM_TO_HOLD;
                timer.reset();    
            }
            else if (buttonPress) {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            break;

            case VACUUM_TO_HOLD:
            if (timer.hasPeriodPassed(0.01)) {
                state = plungerState.HOLD;
                timer.reset();
            }
            else if (buttonPress) {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            break;

            case HOLD:
            if (getVacuum() <= VACUUM_SENSOR_MIN_VAC) {
                state = plungerState.HOLD_TO_VACUUM;
                timer.reset();
            }
            else if (buttonPress) {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            break;

            case HOLD_TO_VACUUM:
            if (timer.hasPeriodPassed(0.01)) {
                state = plungerState.VACUUM_ON;
                timer.reset();
            }
            else if (buttonPress) {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            break;

            case DROP_STATE:
            if (timer.hasPeriodPassed(1)) {
                state = plungerState.CLOSED;
                timer.reset();
            }
            break;

        }
        //state governs which solenoids are open
        switch(state){
            
            case CLOSED:
            setSolenoids(false, false);
            break;

            case VACUUM_ON:
            setSolenoids(true, true);
            break;

            case VACUUM_TO_HOLD:
            setSolenoids(true, false);
            break;

            case HOLD:
            setSolenoids(false, false);
            break;

            case HOLD_TO_VACUUM:
            setSolenoids(true, false);
            break;

            case DROP_STATE:
            setSolenoids(false, true);
            break;
        }
    }
    
    // NOTE: Nathaniel, what is this for? Why do we need this?
    /**
     * use button to operate piston
     * @param pistonButton
     */
    public void plungerPiston(boolean pistonButton){
        //run double solenoid!
    }

    /**
     * Sets the solenoids to the given values.
     * @param upstream
     * @param downstream
     */
    public void setSolenoids(boolean upstream, boolean downstream) {
        upstreamSolenoid.set(upstream);
        downstreamSolenoid.set(downstream);
    }
}