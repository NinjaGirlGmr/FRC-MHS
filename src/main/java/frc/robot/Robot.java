// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.TimedRobot; 
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.cameraserver.CameraServer;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
   //DRIVETRAIN TALON
   private final Talon m_bottomLeftDrive = new Talon(0);
   private final Talon m_topLeftDrive = new Talon(1);
   private final MotorControllerGroup m_left = new MotorControllerGroup(m_topLeftDrive, m_bottomLeftDrive);
   private final Talon m_bottomRightDrive = new Talon(2);
   private final Talon m_topRightDrive = new Talon(3);
   private final MotorControllerGroup m_right = new MotorControllerGroup(m_topRightDrive, m_bottomRightDrive);
   private final DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
   private final XboxController m_controller = new XboxController(0);
   private final Timer m_timer = new Timer();
    //ARM 1
   private final Talon m_sideArmLeft = new Talon(4);
   private final Talon m_sideArmRight = new Talon(5);
   private final MotorControllerGroup m_bottomArm = new MotorControllerGroup(m_sideArmRight,m_sideArmLeft);
   //ARM 2
   private final Spark m_topArm = new Spark(6);
   //CLAW
   private final Spark m_openCloseClaw = new Spark(7);
   
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_left.setInverted(true);
    CameraServer.startAutomaticCapture();
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
     // Drive for .5 second straight forward regular speed, .5 second straight back fair speed
    if (m_timer.get() < 0.5) {
      m_drive.arcadeDrive(-0.9, 0.0, false);
    }
    else if (m_timer.get() < 1.2 && m_timer.get() > 0.7) { // this movement looked slow on field
      m_drive.arcadeDrive(0.65, 0.0, false);
    }
     else {    
      m_drive.stopMotor(); // stop robot
    }

    // no auto
    // m_drive.stopMotor(); // stop robot
   } 

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    
    // Arcade drive with a given forward and turn rate
    //m_drive.arcadeDrive(m_controller.getLeftY()*.8,m_controller.getLeftX()*.7, true);
    // dual-stick controls
    m_drive.arcadeDrive(m_controller.getLeftY()*0.95,m_controller.getRightX()*.75, true);
    
    // first arm control (raise)
    if (m_controller.getLeftBumper() == true) {
      m_bottomArm.set(0.7); // # between -1.0 and 1.0
    }
    else if (m_controller.getLeftTriggerAxis() > 0.0){
      m_bottomArm.set(-0.3); 
    }
    else {
      m_bottomArm.set(0);
    }
    // second arm 
    if (m_controller.getRightBumper() == true){
      m_topArm.set(0.4); // raises 2nd arm, trig down bump up
    }
    else if (m_controller.getRightTriggerAxis() > 0.0){ 
      m_topArm.set(-0.8); 
    }
    else {
      m_topArm.set(0);
    }

    // open close claw controls 
    if (m_controller.getAButton() == true){
      m_openCloseClaw.set(1.0); //close
    }
    else if (m_controller.getBButton() == true) {
      m_openCloseClaw.set(-0.8);
    }
    else {
      m_openCloseClaw.set(0);
    }
  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}

// getXButton, getXButtonPressed, getXButtonReleased) for each of the buttons, 
// and the indices can be accessed with XboxController.Button.kX.value