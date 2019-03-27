package frc.robot;

// Imports for the Plunger Class
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;

/** Methods:
 * public Plunger()
 * public double getPressure()
 * public double getVacuum()
 * public void runCompressor()
 * public boolean getCompressor()
 * public boolean[] getSolenoidStates()
 * public void runPiston(boolean pistonButton)
 * public void setSolenoids(boolean upstream, boolean downstream)
 * public void regulateState(boolean buttonPress)
 * public void runSolenoid()
 * public void run(boolean pistonButton, boolean suctionButton)
 * public void reset()
 */

/**
 * Framework for an object that controls the plunger mechanism.
 */
public class Plunger {
    // Object declaration
    Solenoid upstreamSolenoid;
    Solenoid downstreamSolenoid;
    DoubleSolenoid piston;
    AnalogInput pressureSensor;
    AnalogInput vacuumSensor;
    Timer timer;
    Compressor compressor;

    /**
     * Enum values for the different plunger states.
     */
    public enum plungerState
    {
        VACUUM_ON, DROP_STATE, CLOSED, VACUUM_TO_HOLD, HOLD, HOLD_TO_VACUUM
    }

    plungerState state;

    // Solenoid channels
    public static final int UPSTREAM_SOLENOID_CHANNEL = 2;
    public static final int DOWNSTREAM_SOLENOID_CHANNEL = 1;
    public static final int PRESSURE_SENSOR_CHANNEL = 2;
    public static final int VACUUM_SENSOR_CHANNEL = 3;
    public static final int PISTON_FWD_SOLENOID_CHANNEL = 0;
    public static final int PISTON_RVS_SOLENOID_CHANNEL = 3;

    // Variables for the solenoid sensor
    public static final double VACUUM_SENSOR_IDEAL_VAC = -6;
    public static final double VACUUM_SENSOR_MIN_VAC = -5;

    // Variables for timing while running the plunger
    public static final double WAIT_TIME = 0.01;
    public static final double DROP_TIME = 1;

    // Test variables
    public int iteration = 0;

    /**
     * Constructs a new plunger object.
     */
    public Plunger()
    {
        // Object initialization
        upstreamSolenoid = new Solenoid(UPSTREAM_SOLENOID_CHANNEL);
        downstreamSolenoid = new Solenoid(DOWNSTREAM_SOLENOID_CHANNEL);
        piston = new DoubleSolenoid(PISTON_FWD_SOLENOID_CHANNEL, PISTON_RVS_SOLENOID_CHANNEL);
        pressureSensor = new AnalogInput(PRESSURE_SENSOR_CHANNEL);
        vacuumSensor = new AnalogInput(VACUUM_SENSOR_CHANNEL);
        compressor = new Compressor();
        timer = new Timer();

        reset();
    }

    /**
     * Converts the pressure sensor volts to psi using a predetermined function.
     */
    public double getPressure()
    {
        return ((pressureSensor.getVoltage() * 50.0) - 25.0);
    }

    /**
     * Converts the vacuum sensor volts to psi using a predetermined function.
     */
    public double getVacuum()
    {
        return ((vacuumSensor.getVoltage()* 11.125) - 20.0625);
    }

    /**
     * Runs compressor if the system is below max pressure (120 psi).
     */
    public void runCompressor()
    {
        compressor.start();
    }

    /**
     * Accessor method for the compressor.
     * @return The state of the compressor.
     */
    public boolean getCompressorState()
    {
        return compressor.enabled();
    }

    /**
     * Accessor method for the plunger solenoids.
     * @return An array with the state of the solenoids in the order of (upstream, downstream).
     */
    public boolean[] getSolenoidStates()
    {
        boolean[] output = { upstreamSolenoid.get(), downstreamSolenoid.get() };
        return output;
    }

    /**
     * Changes the state of the plunger piston if a given button is pressed.
     * @param pistonButton
     */
    public void runPiston(boolean pistonButton)
    {
        if (pistonButton)
        {
            // piston.set(!piston.get());
            if (piston.get() == DoubleSolenoid.Value.kForward)
            {
                piston.set(DoubleSolenoid.Value.kReverse);
            }
            else piston.set(DoubleSolenoid.Value.kForward);
        }
    }

    /**
     * Sets the solenoids to the given values.
     * @param upstream
     * @param downstream
     */
    public void setSolenoids(boolean upstream, boolean downstream)
    {
        upstreamSolenoid.set(upstream);
        downstreamSolenoid.set(downstream);
    }

    /**
     * Change the state of the plunger based on the press of a button.
     * @param buttonPress
     */
    public void regulateState(boolean buttonPress)
    {
        switch (state) {
        case CLOSED:
            // On button press, switch to vacuum on
            if (buttonPress && getPressure() >= 20)
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
            else if (getVacuum() <= VACUUM_SENSOR_IDEAL_VAC)
            {
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
    public void runSolenoid()
    {
        // this swich governs which solenoids are open based on the state
        switch (state)
        {
        // all solenoids closed
        case CLOSED:
            setSolenoids(false, true);
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
    /**
     * printing values to smart dash
     */
    public void printValues() {
        SmartDashboard.putNumber("Pressure", getPressure());
        SmartDashboard.putNumber("Vacuum", getVacuum());
        SmartDashboard.putBoolean("upstream", upstreamSolenoid.get());
        SmartDashboard.putBoolean("downstream", downstreamSolenoid.get());
        SmartDashboard.putString("State", state.toString());

        // SmartDashboard.putBoolean("Piston", piston.get() == DoubleSolenoid.Value.kForward);
    }

    /**
     * Executes all the necessary methods to run the plunger mechanism.
     * @param pistonButton
     * @param suctionButton
     */
    public void run(boolean pistonButton, boolean suctionButton)
    {
        printValues();
        runPiston(pistonButton);
        regulateState(suctionButton);
        runSolenoid();
        runCompressor();
        System.out.println(state.toString());
    }

    public void reset() {
        state = plungerState.CLOSED;
        timer.reset();
        timer.start();
    }
}