import java.rmi.*;
import java.util.Vector;
import java.util.*;


public interface CallbackServerInterface extends Remote {

	//--------------------------incluyen validación de usuario-----------------------------
	//se conecta + se notifica a sus amigos que se acaba de conectar (doCallbacks) + se devuelven sus amigos que están conectados
	public Vector registerForCallback(CallbackClientInterface callbackClientObject, String contra) throws java.rmi.RemoteException, Exception; 
	
	//se desconecta + se notifica a sus amigos
	public void unregisterForCallback(CallbackClientInterface callbackClientObject, String contra) throws java.rmi.RemoteException, Exception;
	
	//se crea solicitud + se le notifica al cliente solicitado en caso de que esté conectado
	public int crearSolicitud(String nombre1, String nombre2, String contra) throws java.rmi.RemoteException, Exception;
	
	//obtener solicitudes de amistad de un usuario
	public Vector obtenerSolicitudes(String nombre, String contra) throws java.rmi.RemoteException, Exception;
	
	//se acepta una solicitud de amistad
	public int AceptarSolicitud(String solicitante, String solicitado, String contra) throws java.rmi.RemoteException, Exception; 
	
	//se rechaza una solicitud de amistad
	public int RechazarSolicitud(String solicitante, String solicitado, String contra) throws java.rmi.RemoteException, Exception;	
	
	//se crea un nuevo usuario en la BD
	public void registrarUsuario(String nombre, String contra) throws java.rmi.RemoteException, Exception;
	
	//se modifica la contraseña de un usuario
	public int cambiarContra(String nombre, String contra, String nuevaContra) throws java.rmi.RemoteException, Exception;



	//----------------------no incluyen validación de usuario------------------------
	//comprobar si dos usuarios son amigos
	public int sonAmigos(String nombre1, String nombre2) throws java.rmi.RemoteException;

	//comprobar si existe el usuario al que se le quiere solicitar amistad
	public int existeUsuario(String nombre) throws java.rmi.RemoteException;
	
	//comprobar si ya existe una solicitud entre dos usuarios
	public int existeSolicitud(String nombre1, String nombre2) throws java.rmi.RemoteException;	
	
	//actualizar vector de amigos+ tabla de chat del cliente que acepta una solicitud de amistad
	public void actClientesSolicitado(CallbackClientInterface callbackClientObject, String solicitante) throws java.rmi.RemoteException;
	
 	//actualizar vector de amigos+ tabla de chat del cliente al que se le aceptó una solicitud
	public void actClientesSolicitante(String solicitante, String solicitado) throws java.rmi.RemoteException;

}
