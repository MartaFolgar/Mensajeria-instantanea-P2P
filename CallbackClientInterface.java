import java.rmi.*;
import java.util.Vector;


public interface CallbackClientInterface extends java.rmi.Remote{

	//actualizar chat cuando se envía un mensaje
    public String notifyMe(String message) throws java.rmi.RemoteException;

    //actualizar tabla de amigos conectados
    public void tablaClientes() throws java.rmi.RemoteException; 

    //notificacion de una nueva solicitud de amistad
    public void notSolicitud(Boolean bool) throws java.rmi.RemoteException; 

    public void setClientes(Vector clientes) throws java.rmi.RemoteException; //desde InicioGUI
    public void addCliente(CallbackClientInterface cliente) throws java.rmi.RemoteException;
    public void removeCliente(CallbackClientInterface cliente) throws java.rmi.RemoteException;

    public void setNombre(String nombre) throws java.rmi.RemoteException;
    public String getNombre() throws java.rmi.RemoteException;
    public Vector getClientes() throws java.rmi.RemoteException;
    public void setGUI(ClienteGUI GUI) throws java.rmi.RemoteException;
} 
