import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("trying to connect to VisionDroid");
                Socket clientSocket = new Socket("192.168.14.34", 5802);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("data: " + inputLine);
                    Thread.sleep(200);
                }
                System.out.print("Desconnected from VisionDroid ");
            } catch (UnknownHostException e) {
                System.out.println("Error...");
            } catch (IOException e) {
                System.out.print("VisionDroid isn't reachable ");
            } catch (InterruptedException e) {
                System.out.println("Error when trying to sleep");
            }
        }
    }
}
