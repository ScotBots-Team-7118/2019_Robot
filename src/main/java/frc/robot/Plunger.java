package frc.robot;

// Imports for the Plunger Class
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Methods:
 * public Plunger()
 * public double getPressure()
 * public double getVacuum()
 * public void runCompressor()
 * public boolean getCompressor()
 * public boolean[] getSolenoidStates()
 * public void runPiston(boolean pistonButton)
 * public void plungertest()
 * public void setSolenoids(boolean upstream, boolean downstream)
 * public void regulateState(boolean buttonPress)
 * public void runSolenoid()
 * // TODO: Make method that combines regulateState() and runSolenoid()
 */

/**
 * Framework for an object that controls the plunger mechanism.
 */
public class Plunger {
    // Object declaration
    Solenoid upstreamSolenoid;
    Solenoid downstreamSolenoid;
    Solenoid piston;
    AnalogInput pressureSensor;
    AnalogInput vacuumSensor;
    Timer timer;
    Compressor compressor;

    /**
     * Enum values for the different plunger states.
     */
    public enum plungerState {
        VACUUM_ON, DROP_STATE, CLOSED, VACUUM_TO_HOLD, HOLD, HOLD_TO_VACUUM
    }

    plungerState state;

    // Solenoid channels
    public static final int UPSTREAM_SOLENOID_CHANNEL = 2;
    public static final int DOWNSTREAM_SOLENOID_CHANNEL = 1;
    public static final int PRESSURE_SENSOR_CHANNEL = 2;
    public static final int VACUUM_SENSOR_CHANNEL = 3;
    public static final int PISTON_SOLENOID_CHANNEL = 0;

    // Variables for the solenoid sensor
    public static final double VACUUM_SENSOR_IDEAL_VAC = -4;
    public static final double VACUUM_SENSOR_MIN_VAC = -3;

    // Variables for timing while running the plunger
    public static final double WAIT_TIME = 0.01;
    public static final double DROP_TIME = 1;

    // Test variables
    public int iteration = 0;

    /**
     * Constructs a new plunger object.
     */
    public Plunger() {
        // Object initialization
        upstreamSolenoid = new Solenoid(UPSTREAM_SOLENOID_CHANNEL);
        downstreamSolenoid = new Solenoid(DOWNSTREAM_SOLENOID_CHANNEL);
        piston = new Solenoid(PISTON_SOLENOID_CHANNEL);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_CHANNEL);
        vacuumSensor = new AnalogInput(VACUUM_SENSOR_CHANNEL);
        compressor = new Compressor();
        timer = new Timer();

        timer.start();
        state = plungerState.CLOSED;
    }

    /**
     * Converts the pressure sensor volts to psi using a predetirmined function.
     */
    public double getPressure() {
        return ((pressureSensor.getVoltage() * 50.0) - 25.0);
    }

    /**
     * Converts the vacuum sensor volts to psi using a predetirmined function.
     */
    public double getVacuum() {
        return ((vacuumSensor.getVoltage()* 11.125) - 20.0625);
    }

    /**
     * Runs compressor if the system is below max pressure (120 psi).
     */
    public void runCompressor() {
        compressor.start();
    }

    /**
     * Accessor method for the compressor.
     * @return The state of the compressor.
     */
    public boolean getCompressorState() {
        return compressor.enabled();
    }

    /**
     * Accessor method for the plunger solenoids.
     * @return An array with the state of the solenoids in the order of (upstream, downstream).
     */
    public boolean[] getSolenoidStates() {
        boolean[] output = { upstreamSolenoid.get(), downstreamSolenoid.get() };
        return output;
    }

    /**
     * Changes the state of the plunger piston if a given button is pressed.
     * @param pistonButton
     */
    public void runPiston(boolean pistonButton) {
        if (pistonButton)
        {
        piston.set(!piston.get());
        }
    }

    // TODO: Remove this method as soon as it is deemed unnecessary for function
    public void plungertest() {
        iteration++;
        if (iteration%20 == 0)
        {
            SmartDashboard.putNumber("VacuumVoltage: ", vacuumSensor.getVoltage());
            SmartDashboard.putNumber("VacuumPSI", getVacuum());
            SmartDashboard.putNumber("PressureVoltage", pressureSensor.getVoltage());
            SmartDashboard.putNumber("PressurePSI", getPressure());
            SmartDashboard.putNumber("Pressure Until Next Cycle", getVacuum()-VACUUM_SENSOR_MIN_VAC);
        }
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

    /**
     * Change the state of the plunger based on the press of a button.
     * @param buttonPress
     */
    public void regulateState(boolean buttonPress) {
        switch (state) {
        case CLOSED:
            // On button press, switch to vacuum on
            if (buttonPress)
            {
                state = plungerState.VACUUM_ON;
            }
            // Otherwise, maintain the current state
            break;

        case VACUUM_ON:
            // If the plunger button is pressed, go to drop state
            if (buttonPress)
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            // At the ideal pressure, switch state to hold the hatch
            else if (getVacuum() <= VACUUM_SENSOR_IDEAL_VAC) {
                state = plungerState.VACUUM_TO_HOLD;
                timer.reset();
            }
            // Otherwise, maintain the current state
            break;
            
        case VACUUM_TO_HOLD:
            // If the plunger button is pressed, switch to drop state
            if (buttonPress) 
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            // After a short waiting period, switch to hold the hatch
            else if (timer.hasPeriodPassed(WAIT_TIME)) 
            {
                state = plungerState.HOLD;
            }
            // Otherwise, maintain the current state
            break;

        
        case HOLD:
            // If the plunger button is pressed, switch to drop state
            if (buttonPress) 
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            // If the vacuum isn't strong enough, switch to holding the hatch to the vacuum
            else if (getVacuum() >= VACUUM_SENSOR_MIN_VAC)
            {
                state = plungerState.HOLD_TO_VACUUM;
                timer.reset();
            }
            // Otherwise, maintain the current state
            break;
       
        case HOLD_TO_VACUUM:
            // If the plunger button is pressed, switch to drop state
            if (buttonPress)
            {
                state = plungerState.DROP_STATE;
                timer.reset();
            }
            // After a short waiting period, switch to turning the vacuum on
            else if (timer.hasPeriodPassed(WAIT_TIME)) 
            {
                state = plungerState.VACUUM_ON;
                timer.reset();
            }
            // Otherwise, maintain the current state
            break;

        case DROP_STATE:
            // After a short waiting period, switch to the closed state to reset the cycle
            if (timer.hasPeriodPassed(DROP_TIME)) 
            {
                state = plungerState.CLOSED;
                timer.reset();
            }
            // Otherwise, maintain the current state
            break;

        }
    }

    /**
     * Run the solenoid based on the current state.
     */
    public void runSolenoid() {
        // this swich governs which solenoids are open based on the state
        switch (state)
        {
        // all solenoids closed
        case CLOSED:
            setSolenoids(false, false);
            break;

        // all solenoids open
        case VACUUM_ON:
            setSolenoids(true, true);
            break;

        // close downstream solenoid to retain vacuum in suction cup
        case VACUUM_TO_HOLD:
            setSolenoids(true, false);
            break;

        // close upstream (downstream already closed), conserve air to refresh required
        case HOLD:
            setSolenoids(false, false);
            break;

        // prepare to refresh vacuum, open upstream solenoid
        case HOLD_TO_VACUUM:
            setSolenoids(true, false);
            break;

        // close upstream, open downstream, release pressure through vac gen
        case DROP_STATE:
            setSolenoids(false, true);
            break;
        }
    }
}