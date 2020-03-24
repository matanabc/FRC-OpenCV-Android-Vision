/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.VisionDroid;

public class VisionDroidRecevDataCommnad extends CommandBase {
  private boolean isConnected2VisionDroid = false;
  private BufferedReader visionDroidReader;
  private String visionDroidRead;

  private VisionDroid visionDroid;

  public VisionDroidRecevDataCommnad(VisionDroid visionDroid) {
    addRequirements(visionDroid);
    this.visionDroid = visionDroid;
  }

  @Override
  public void execute() {
    try {
      if (!isConnected2VisionDroid) {
        connect2VisionDroid();
      }

      if ((visionDroidRead = visionDroidReader.readLine()) != null) {
        visionDroid.setVisionDroidData(visionDroidRead.split(";"));
      } else {
        visionDroid.setVisionDroidData("false;0;0".split(";"));
        isConnected2VisionDroid = false;
      }
      visionDroid.uploadVisionDroidData2DashBoard();
    } catch (UnknownHostException e) {
    } catch (IOException e) {
      isConnected2VisionDroid = false;
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  private void connect2VisionDroid() throws UnknownHostException, IOException {
    SmartDashboard.putBoolean("Connected", false);
    Socket visionDroidClient = new Socket();
    visionDroidClient.connect(new InetSocketAddress("192.168.14.34", 5802), 10);
    visionDroidReader = new BufferedReader(new InputStreamReader(visionDroidClient.getInputStream()));
    SmartDashboard.putBoolean("Connected", true);
    isConnected2VisionDroid = true;
  }
}
