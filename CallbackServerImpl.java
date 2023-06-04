import java.rmi.*;
import java.rmi.server.*;
import java.util.Vector;
import java.util.*;


public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

 private Vector clientList;
 private DAOUsuarios daoUsuarios;


 public CallbackServerImpl(DAOUsuarios daoUsuarios) throws RemoteException {
  super( );
  clientList = new Vector();
  this.daoUsuarios=daoUsuarios;
}



public synchronized Vector registerForCallback(CallbackClientInterface callbackClientObject, String contra)throws java.rmi.RemoteException, Exception{
  int encontrado=0;

  //se comprueba si el usuario ya está conectado 
  for (int i = 0; i < clientList.size(); i++){  
    CallbackClientInterface nextClient =(CallbackClientInterface)clientList.elementAt(i); 
    if(nextClient.getNombre().equals(callbackClientObject.getNombre())){
      encontrado=1;
    }
  }
  
  //si no está conectado:
  if(encontrado==0){
      //se comprueba si el usuario está registrado en el sistema (autenticación)
    if(daoUsuarios.validarUsuario(callbackClientObject.getNombre(), contra)==null){
      throw new Exception("Usuario incorrecto.");     
    }else{
        //si la autenticación fue correcta:
      if (!(clientList.contains(callbackClientObject))) {
          //se añade al vector de clientes conectados
        clientList.addElement(callbackClientObject);
        System.out.println("Registered new client: " + callbackClientObject.getNombre());
          //se notifica a sus amigos que se acaba de conectar
        doCallbacks(callbackClientObject); 
          //se le devuelve un vector con sus amigos que están conectados en ese momento
        return restoClientes(callbackClientObject);
      }      
    }
  }else{
   throw new Exception("Usuario ya conectado."); 
 }
 return null;
}



//array de clientes conectados para el que se acaba de conectar (al autenticarse)
private synchronized Vector restoClientes(CallbackClientInterface callbackClientObject) throws java.rmi.RemoteException{
  Vector clientes = new Vector();

  //se van recorriendo todos los clientes conectados
  for (int i = 0; i < clientList.size(); i++){ 
    CallbackClientInterface nextClient =(CallbackClientInterface)clientList.elementAt(i);
    //si son amigos se añade al vector
    if(sonAmigos(callbackClientObject.getNombre(), nextClient.getNombre())==1){
      clientes.add(nextClient);
    }
  }
  return clientes;
} 



//notificar al resto de clientes de que se conectó uno de sus amigos
private synchronized void doCallbacks(CallbackClientInterface callbackClientObject) throws java.rmi.RemoteException{
  //se recorren todos los clientes conectados
  for (int i = 0; i < clientList.size(); i++){
    CallbackClientInterface nextClient =(CallbackClientInterface)clientList.elementAt(i);
    //comprobar si son amigos
    if(sonAmigos(callbackClientObject.getNombre(), nextClient.getNombre())==1){        
      //añadir el nuevo cliente al vector de cada cliente:
      //se actualiza el vector de clientes
      nextClient.addCliente(callbackClientObject);
      //se actualiza su tabla
      nextClient.tablaClientes();            
    }
  }
}



//cuando un cliente se desconecta: eliminarlo del vector de clientes conectados + informar al resto de clientes
public synchronized void unregisterForCallback (CallbackClientInterface callbackClientObject, String contra) throws java.rmi.RemoteException, Exception {
  //se comprueba si el usuario está registrado en el sistema
  if(daoUsuarios.validarUsuario(callbackClientObject.getNombre(), contra)==null){
    throw new Exception("Usuario incorrecto.");     
  }else{
      //se elimina del vector de clientes conectados 
    if (clientList.removeElement(callbackClientObject)) {
      System.out.println(callbackClientObject.getNombre() + " has unregistered.");

        //se recorre el resto de clientes conectados
      for (int i = 0; i < clientList.size(); i++){  
        CallbackClientInterface nextClient =(CallbackClientInterface)clientList.elementAt(i); 

          //eliminar al cliente de la lista de clientes
        if(sonAmigos(callbackClientObject.getNombre(), nextClient.getNombre())==1){
          //se actualiza el vector de clientes:
         nextClient.removeCliente(callbackClientObject);
           //se actualiza su tabla
         nextClient.tablaClientes(); 
       }
     }
   }else {
     System.out.println("unregister: client wasn't registered.");
   }
 }
}


//comprobar que el nombre y contraseña son correctos
private Usuario validarUsuario(String nombre, String contra) throws java.rmi.RemoteException {
  return daoUsuarios.validarUsuario(nombre, contra);
} 


public int crearSolicitud(String nombre1, String nombre2, String contra) throws java.rmi.RemoteException, Exception{
  //comprobar si el usuario que intenta crear la solicitud es el mismo que está llamando al método
  //(que nadie está intentando suplantar su identidad)
  if(daoUsuarios.validarUsuario(nombre1, contra)==null){
    throw new Exception("Usuario incorrecto.");     
  }else{
    int result=daoUsuarios.crearSolicitud(nombre1, nombre2);

    //si el usuario al que se le envía la solicitud está conectado en ese momento: hay que notificárselo
    for (int i = 0; i < clientList.size(); i++){  
      CallbackClientInterface nextClient =(CallbackClientInterface)clientList.elementAt(i); 
      if(nextClient.getNombre().equals(nombre2)){
       nextClient.notSolicitud(true);
     }
   }
   return result;

 } 
}



public Vector obtenerSolicitudes(String nombre, String contra) throws java.rmi.RemoteException, Exception{
  //comprobar si el usuario que intenta obtener las solicitudes es el mismo que está llamando al método
  //(que nadie está intentando suplantar su identidad)
  if(daoUsuarios.validarUsuario(nombre, contra)==null){
    throw new Exception("Usuario incorrecto.");     
  }else{
    return daoUsuarios.obtenerSolicitudes(nombre);
  }
}



public int AceptarSolicitud(String solicitante, String solicitado, String contra) throws java.rmi.RemoteException, Exception{
  //comprobar si el usuario que intenta aceptar la solicitud es el mismo que está llamando al método
  //(que nadie está intentando suplantar su identidad)
  if(daoUsuarios.validarUsuario(solicitado, contra)==null){
    throw new Exception("Usuario incorrecto.");     
  }else{
    return daoUsuarios.AceptarSolicitud(solicitante, solicitado);
  }
}


public int RechazarSolicitud(String solicitante, String solicitado, String contra) throws java.rmi.RemoteException, Exception{
  //comprobar si el usuario que intenta rechazar la solicitud es el mismo que está llamando al método
  //(que nadie está intentando suplantar su identidad)
  if(daoUsuarios.validarUsuario(solicitado, contra)==null){
    throw new Exception("Usuario incorrecto.");     
  }else{
    return daoUsuarios.RechazarSolicitud(solicitante, solicitado);
  }
}



public void registrarUsuario(String nombre, String contra) throws java.rmi.RemoteException, Exception{
  if(daoUsuarios.validarUsuario(nombre, contra)==null){
    daoUsuarios.registrarUsuario(nombre, contra);
  }else{
   throw new Exception("Usuario ya registrado."); 
 }  
} 


public int cambiarContra(String nombre, String contra, String nuevaContra) throws java.rmi.RemoteException, Exception{
  if(daoUsuarios.validarUsuario(nombre, contra)==null){
    throw new Exception("Usuario incorrecto.");     
  }else{
    return daoUsuarios.cambiarContra(nombre, contra, nuevaContra);
  }
} 


public int sonAmigos(String nombre1, String nombre2) throws java.rmi.RemoteException {
  return daoUsuarios.sonAmigos(nombre1, nombre2);
}

public int existeUsuario(String nombre) throws java.rmi.RemoteException{
  return daoUsuarios.existeUsuario(nombre);
}


public int existeSolicitud(String nombre1, String nombre2) throws java.rmi.RemoteException{
 return daoUsuarios.existeSolicitud(nombre1, nombre2);
}


 //cuando un cliente acepta una solicitud de amistad: hay que añadir al nuevo amigo a su vector de clientes
public synchronized void actClientesSolicitado(CallbackClientInterface callbackClientObject, String solicitante) throws java.rmi.RemoteException{
  //se van recorriendo todos los clientes conectados
  for (int i = 0; i < clientList.size(); i++){ 
    CallbackClientInterface nextClient =(CallbackClientInterface)clientList.elementAt(i);
      //si coincide con el cliente solicitante, se añade a su vector de clientes pq ahora son amigos
    if(nextClient.getNombre().equals(solicitante)){ 
      //actualizar su vector de amigos
      callbackClientObject.addCliente(nextClient);
    }
  }
  //actualizar su tabla de chat
  callbackClientObject.tablaClientes();
}


//cuando un cliente acepta una solicitud de amistad: hay que añadir a ese cliente al vector de clientes del que le mandó la solicitud
public synchronized void actClientesSolicitante(String solicitante, String solicitado) throws java.rmi.RemoteException{
  //se van recorriendo todos los clientes conectados
  for (int i = 0; i < clientList.size(); i++){ 
    CallbackClientInterface nextClient =(CallbackClientInterface)clientList.elementAt(i);

    //se encuentra al cliente solicitante
    if(nextClient.getNombre().equals(solicitante)){ 
      //se van recorriendo todos los clientes conectados
      for (int j = 0; j < clientList.size(); j++){ 
        CallbackClientInterface nextClient2 =(CallbackClientInterface)clientList.elementAt(j);
        //se encuentra al cliente solicitado y se añade al vector del cliente solicitante
        if(nextClient2.getNombre().equals(solicitado)){ 
          //actualizar su vector de amigos
          nextClient.addCliente(nextClient2);
        }
      }
      //actualizar su tabla de chat
      nextClient.tablaClientes();      
    }
  }
}


}





