/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// Imports for the Robot.java class
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.cameraserver.*;

/** Methods:
 * public void robotInit()
 * public void robotPeriodic()
 * public void disabledInit()
 * public void autonomousInit()
 * public void autonomousPeriodic()
 * public void teleopInit()
 * public void teleopPeriodic()
 * public void testPeriodic()
 */


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
  Plunger plunger;
  Pillow pillow;

  // Variable Declaration
  private final int JOY_R_PORT = 1;
  private final int JOY_L_PORT = 0;
  private double[] joyR = { 0, 0, 0 }, joyL = { 0, 0, 0 };
  private final int SUCTION_BUTTON = 1;
  private final int PISTON_BUTTON = 1;
  private final int PILLOW_BUTTON_OPEN = 4;
  private final int PILLOW_BUTTON_CLOSED = 3;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit()
  {
    // Object initialization
    rawJoyR = new Joystick(JOY_R_PORT);
    rawJoyL = new Joystick(JOY_L_PORT);
    gyro = new Gyroscope();
    driveBase = new DriveBase();
    pillow = new Pillow();
    plunger = new Plunger();

    // Starts automatic capture for the Sandstorm cameras
    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);
  }

  /**
   * Runs once when the robot becomes disabled. This is to ensure that the sensors
   * are ready to go for the next mode and that the robot doesn't do anything
   * unexpected while disabled.
   */
  @Override
  public void disabledInit()
  {
    // TODO: Make sure with Nathaniel that this is necessary
    plunger.runPiston(false);
  }

  /**
   * Runs during autonomous mode initialization.
   */
  @Override
  public void autonomousInit()
  {
    gyro.reset();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic()
  {
    SmartDashboard.putNumber("Gyroscope Value", gyro.getOffsetHeading());
  }

  /**
   * Runs during teleop mode initialization.
   */
  @Override
  public void teleopInit()
  {
    gyro.reset();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic()
  {
    // Format the joystick axes to be used for teleopDrive
    joyR = driveBase.formatDriveJoystick(rawJoyR.getRawAxis(0), rawJoyR.getRawAxis(1));
    joyL = driveBase.formatDriveJoystick(rawJoyL.getRawAxis(0), -rawJoyL.getRawAxis(1));
    
    // Use the formatted joystick data to drive the robot
    driveBase.teleopDrive(joyR, joyL);
    
    // Run the pillow according to open and closed buttons
    pillow.run(rawJoyL.getRawButton(PILLOW_BUTTON_OPEN), rawJoyR.getRawButton(PILLOW_BUTTON_CLOSED));

    // Run the plunger according to piston and suction buttons
    plunger.run(rawJoyL.getRawButtonPressed(PISTON_BUTTON), rawJoyR.getRawButtonPressed(SUCTION_BUTTON));
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic()
  {
    
  }
}