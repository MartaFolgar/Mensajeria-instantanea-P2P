import java.io.*;
import java.rmi.*;
import java.util.*;


public class CallbackClient {

  public static void main(String args[]) {

    try{
      int RMIPort;         
      String hostName;
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);

      System.out.println( "Introduzca el nombre del host:");
      hostName = br.readLine();      

      System.out.println( "Introduzca el numero de puerto:");
      String portNum = br.readLine();
      RMIPort = Integer.parseInt(portNum); 

      /*hostName="localhost";
      RMIPort=1099;*/

      //Creación de la interfaz 
      InicioGUI frameCl=new InicioGUI(RMIPort, hostName);
      frameCl.setVisible(true);

    } 
    catch (Exception e) {
      System.out.println("Exception in CallbackClient: " + e);
    } 
    
  }

}
