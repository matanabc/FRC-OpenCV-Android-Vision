/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.VisionDroidRecevDataCommnad;

public class VisionDroid extends SubsystemBase {
  private boolean isValid = false;
  private double X = 0, Y = 0;

  public VisionDroid() {
    setDefaultCommand(new VisionDroidRecevDataCommnad(this));
  }

  public void setVisionDroidData(String[] data){
    isValid = Boolean.valueOf(data[0]);
    X = Double.valueOf(data[1]);
    Y = Double.valueOf(data[2]);
  }

  public void uploadVisionDroidData2DashBoard(){
    SmartDashboard.putBoolean("Valid", isValid());
    SmartDashboard.putNumber("X", getX());
    SmartDashboard.putNumber("Y", getY());
  }

  public boolean isValid(){
    return isValid;
  }

  public double getX(){
    return X;
  }

  public double getY(){
    return Y;
  }
}
