import java.rmi.*;
import java.rmi.server.*;
import javax.swing.*; 
import java.util.Vector;


public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {
  private String nombre;
  private Vector clientes;
  private ClienteGUI GUI;
  
  public CallbackClientImpl() throws RemoteException {
    super( );
  }

   //actualizar chat cuando se envía un mensaje
  public String notifyMe(String message){
    this.getGUI().actualizarChat(message);
    return message;
  } 

  //actualizar tabla de amigos conectados
  public void tablaClientes(){
    this.getGUI().setTabla();
  } 

   //notificacion de una nueva solicitud de amistad
  public void notSolicitud(Boolean bool){
    this.getGUI().labelNotificacion(bool);
  }


  public void setNombre(String nombre){
    this.nombre=nombre;
  }

  public String getNombre(){
    return nombre;
  }

  public void setClientes(Vector clientes){
    this.clientes=clientes;
  }

  public void addCliente(CallbackClientInterface cliente){
    this.clientes.addElement(cliente);
  }

  public void removeCliente(CallbackClientInterface cliente){
    this.clientes.removeElement(cliente);
  }

  public Vector getClientes(){
    return clientes;
  }

  public void setGUI(ClienteGUI GUI){
    this.GUI=GUI;
  }

  private ClienteGUI getGUI(){
    return GUI;
  }


}
