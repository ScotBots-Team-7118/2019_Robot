/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// Imports for the Robot.java class
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Object Declaration
  DriveBase driveBase;
  Gyroscope gyro;
  Joystick rawJoyR, rawJoyL;
  // Pillow pillow;
  Plunger plunger;

  // Variable Declaration
  private final int JOY_R_PORT = 0;
  private final int JOY_L_PORT = 1;
  private double[] joyR = { 0, 0, 0 }, joyL = { 0, 0, 0 };
  private final int SUCTION_BUTTON = 1;
  private final int PISTON_BUTTON = 2;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Object initialization
    rawJoyR = new Joystick(JOY_R_PORT);
    rawJoyL = new Joystick(JOY_L_PORT);
    gyro = new Gyroscope();
    driveBase = new DriveBase();
    // pillow = new Pillow();
    plunger = new Plunger();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

  }

  /**
   * Runs once when the robot becomes disabled. This is to ensure that the sensors
   * are ready to go for the next mode and that the robot doesn't do anything
   * unexpected while disabled.
   */
  @Override
  public void disabledInit() {

  }

  /**
   * Runs during autonomous mode initialization.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * Runs during teleop mode initialization.
   */
  @Override
  public void teleopInit() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // Format the joystick axes to be used for teleopDrive
    joyR = driveBase.formatDriveJoystick(rawJoyR.getRawAxis(0), rawJoyR.getRawAxis(1), rawJoyR.getRawAxis(2));
    joyL = driveBase.formatDriveJoystick(rawJoyL.getRawAxis(0), rawJoyL.getRawAxis(1), rawJoyL.getRawAxis(2));
    // Use the formatted joystick data to drive the robot
    driveBase.teleopDrive(joyR, joyL);
    // Run the plunger according to the state machine within the class and the given
    // suction button
    // if (pillow.closedState()) {
    // plunger.runPlunger(rawJoyR.getRawButton(SUCTION_BUTTON));
    // }
    // else plunger.runPlunger(false);

    // Run the plunger according to the state machine within the class and the given
    // suction button
    plunger.plungerPiston(rawJoyR.getRawButtonPressed(PISTON_BUTTON));
    plunger.runPlunger(rawJoyR.getRawButtonPressed(SUCTION_BUTTON));
    plunger.runSolenoid();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    // driveBase.setRight(rawJoyR.getRawAxis(1)*0.3);
    // driveBase.setLeft(rawJoyL.getRawAxis(1)*0.3);
    // driveBase.setCenter(rawJoyR.getRawAxis(0)*0.3);
    joyR = driveBase.formatDriveJoystick(rawJoyR.getRawAxis(0), rawJoyR.getRawAxis(1), rawJoyR.getRawAxis(2));
    joyL = driveBase.formatDriveJoystick(rawJoyL.getRawAxis(0), rawJoyL.getRawAxis(1), rawJoyL.getRawAxis(2));
    driveBase.teleopDrive(joyR, joyL);
  }
}