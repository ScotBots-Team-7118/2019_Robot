package frc.robot;

// Imports for the Santstorm.java class.

public class Sandstorm {
    // Object Declaration.

    // Variable Declaration.
    enum AutoPosition {
        DEFAULT, LEFT, RIGHT, CENTER
    };

    enum AutoState {
        FIRST_STEP
    };

    private AutoState phase;
    private AutoPosition position;

    /**
     * Constructs a new Sandstorm object.
     */
    public Sandstorm() {

    }

    /**
     * Runs the program for the game's designated sandstorm phase.
     */
    public void run() {
        switch (phase) {
        case FIRST_STEP:
            break;
        }
    }

    /**
     * Tells the autonomous class the position of the robot.
     */
    public void setPosition(AutoPosition position) {
        this.position = position;
    }

    /**
     * Resets the autonomous progression and starting position.
     */
    public void reset() {
        phase = AutoState.FIRST_STEP;
        position = AutoPosition.DEFAULT;
    }

    /**
     * Advances the autonomous progression to the next step and resets the
     * appropriate sensors.
     * 
     * @param step
     */
    public void nextStep(AutoState step) {
        phase = step;
    }
}