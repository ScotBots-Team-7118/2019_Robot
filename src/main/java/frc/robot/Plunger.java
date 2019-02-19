package frc.robot;

// Imports for the Plunger Class
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    plungerState state;

    // Solenoid channels
    public static final int UPSTREAM_SOLENOID_CHANNEL = 2;
    public static final int DOWNSTREAM_SOLENOID_CHANNEL = 1;
    public static final int PRESSURE_SENSOR_CHANNEL = 2;
    public static final int VACUUM_SENSOR_CHANNEL = 3;
    public static final int PISTON_SOLENOID_CHANNEL = 0;
    // Solenoid sensor variables
    public static final double VACUUM_SENSOR_IDEAL_VAC = -4;
    public static final double VACUUM_SENSOR_MIN_VAC = -3;
    //plunger test variables
    public int iteration = 0;

    /**
     * Constructs a new plunger object.
     */
    public Plunger() {

        // object initialization
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
     * Converts the pressure sensor volts to psi.
     */
    public double getPressure() {
        return ((pressureSensor.getVoltage() * 50.0) - 25.0);
    }

    /**
     * Converts the vacuum sensor volts to psi.
     */
    public double getVacuum() {
        return ((vacuumSensor.getVoltage()* 11.125) - 20.0625);
    }

    /**
     * runs compressor if below max pressure (120 psi)
     */
    public void compressor() {
        compressor.start();
    }

    /**
     * returns true if compressor is on
     * 
     * @return
     */
    public boolean getCompressor() {
        return compressor.enabled();
    }

    /**
     * return an array of solenoids that are on (T/F)
     * 
     * @return
     */
    public boolean[] getSolenoid() {
        boolean[] output = { upstreamSolenoid.get(), downstreamSolenoid.get() };
        return output;
    }

    /**
     * use button to operate piston
     * 
     * @param pistonButton
     */
    public void plungerPiston(boolean pistonButton) {
        // on button press, change solenoid (if bool input is based on last press)
        if( pistonButton)
        {
        piston.set(!piston.get());
        }
    }
    public void plungertest(){
        iteration++;
        if (iteration%20 == 0)
        {
        
            SmartDashboard.putNumber("VacuumVoltage: ", vacuumSensor.getVoltage());
            SmartDashboard.putNumber("VacuumPSI", getVacuum());
            SmartDashboard.putNumber("PressureVoltage", pressureSensor.getVoltage());
            SmartDashboard.putNumber("PressurePSI", getPressure());
        }
    }
    /**
     * Sets the solenoids to the given values.
     * 
     * @param upstream
     * @param downstream
     */
    public void setSolenoids(boolean upstream, boolean downstream) {
        upstreamSolenoid.set(upstream);
        downstreamSolenoid.set(downstream);
    }

    /**
     * declares plunger states
     */
    public enum plungerState {

        VACUUM_ON, DROP_STATE, CLOSED, VACUUM_TO_HOLD, HOLD, HOLD_TO_VACUUM
    }

    /**
     * using first state machine to control state change, second to enact state Runs
     * the plunger device by controlling pulse, pick-up, and drop-off of the hatch.
     * 
     * @param buttonPress
     */
    public void runPlunger(boolean buttonPress) {
        // this switch controls the requirments to change states
        switch (state) {
        // on button press, switch to vacuum on
        case CLOSED:
            if (buttonPress) {
                state = plungerState.VACUUM_ON;
                timer.reset();
            }
            break;

        // on 30 psi, switch to hold
        case VACUUM_ON:
            if (buttonPress) {
                // if button press, go to drop state
                state = plungerState.DROP_STATE;
                timer.reset();
            } else if (getVacuum() <= VACUUM_SENSOR_IDEAL_VAC) {
                state = plungerState.VACUUM_TO_HOLD;
                timer.reset();
            }
            break;

        // after 0.01 sec, switch to hold
        case VACUUM_TO_HOLD:
            if (buttonPress) {
                // if buttonpress, switch to drop state
                state = plungerState.DROP_STATE;
                timer.reset();
            } else if (timer.hasPeriodPassed(0.01)) {
                state = plungerState.HOLD;
                timer.reset();
            }

            break;

        // if low pressure( below 20 psi), switch to vacuum
        case HOLD:
            if (buttonPress) {
                // if buttonpress, go to drop state
                state = plungerState.DROP_STATE;
                timer.reset();
            } else if (getVacuum() >= VACUUM_SENSOR_MIN_VAC) {
                state = plungerState.HOLD_TO_VACUUM;
                timer.reset();
            }

            break;

        // after 0.01 sec, switch to vacuum on
        case HOLD_TO_VACUUM:
            if (buttonPress) {
                // if button press, go to drop state
                state = plungerState.DROP_STATE;
                timer.reset();
            } else if (timer.hasPeriodPassed(0.01)) {
                state = plungerState.VACUUM_ON;
                timer.reset();
            }

            break;

        // release pressure on plunger, switch to closed state after 1 sec to reset
        // cycle
        case DROP_STATE:
            if (timer.hasPeriodPassed(1)) {
                state = plungerState.CLOSED;
                timer.reset();
            }
            break;

        }
    }

    /**
     * Changes solenoid based on state
     */
    public void runSolenoid() {
        // this swich governs which solenoids are open based on the state
        switch (state) {

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