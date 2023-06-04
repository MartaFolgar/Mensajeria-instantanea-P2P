import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClienteGUI extends javax.swing.JFrame implements WindowListener{

	private JLabel labelUs;
	private JTextField nombreUs;	
	
	private JTable tabla;
	private JScrollPane jScrollPane3;

	private JLabel labelChat;
	private JTextArea chat;
	private JScrollPane jScrollPane1;

	private JTextArea msjEnviar; 
	private JScrollPane jScrollPane2;

	private JButton btnEnviar;
	private JButton btnSolicitarAmistad;
	private JButton btnSolicPend; 
	private JLabel labelNotificSolic;
	
	private int RMIPort;
	private String hostname;
	private String nombre;
	private String contra;
	private CallbackServerInterface server;
	private CallbackClientInterface callbackObj;
	private ModeloTablaAmigos mba;
	private Vector clientes=new Vector();
	private HashMap<String, String> chats = new HashMap<String, String>();
	private HashMap<String, String> numMensajes = new HashMap<String, String>();


	//Inicializacion interfaz
	public ClienteGUI(String nombre, String contra, int RMIPort, String hostname, CallbackClientInterface callbackObj, CallbackServerInterface server) {    	
		super("Chat"); 
		this.RMIPort=RMIPort;
		this.hostname=hostname;
		this.nombre=nombre;		
		this.callbackObj=callbackObj;
		this.server=server;
		this.contra=contra;

        //Inicializacion de componentes
		setSize(800, 600);        	
		initComponents();
		setVisible(true);
		btnEnviar.setEnabled(false);
	}




	//Evento al pulsar la cruz de cierre del jframe
	public void windowClosing(WindowEvent arg0) {  		
      	//unregister for callback
		if(server!=null && callbackObj!=null){
			try{
				server.unregisterForCallback(callbackObj, contra);
				System.out.println("Unregistered for callback.");
			} 
			catch (Exception e) {
				System.out.println("Exception in CallbackClient: " + e);
			} 
		}
		
		System.exit(0);
	}  



	//Enviar mensajes  
	private void btnEnviarMouseClicked(java.awt.event.MouseEvent evt) { 
		try{ 
			clientes=callbackObj.getClientes();
		}catch (Exception e) {
			System.out.println("Exception in CallbackClient: " + e);
		} 


		//escribir el mensaje que envio yo
		String nombre = (String)tabla.getValueAt(tabla.getSelectedRow(), 0);
		chats.put(nombre, chats.get(nombre)+"\nyo: "+msjEnviar.getText());
		chat.setText(chats.get(nombre));


		//enviarle el mensaje solo a la persona que tenemos seleccionada en la lista		
		for(int i=0; i<clientes.size(); i++){
			CallbackClientInterface nextClient =(CallbackClientInterface)clientes.elementAt(i);

			try{	
				if(nextClient.getNombre().equals(nombre)){	
					//enviar mensaje 				
					nextClient.notifyMe(this.nombre + ": " + msjEnviar.getText());						
				}	

			}catch (Exception e) {
				System.out.println("Exception in CallbackClient: " + e);
			}	


		}
		msjEnviar.setText("");
	}



	//Enviar mensajes  
	private void btnSolicitarAmistadMouseClicked(java.awt.event.MouseEvent evt) { 
			//se crea la ventana de solicitar amistad
		SolicitarAmistadGUI frame=new SolicitarAmistadGUI(nombreUs.getText(), contra, server, this);
		this.dispose();
		frame.setVisible(true);	
	}


	//Ver solicitudes pendientes de aceptar
	private void btnSolicPendMouseClicked(java.awt.event.MouseEvent evt) {  
		SolicPendientesGUI frame=new SolicPendientesGUI(nombreUs.getText(), contra, server, this, callbackObj);
		this.dispose();
		frame.setVisible(true);	
	}



	public void tablaKeyListener(){
		int fila=tabla.getSelectedRow();
		String nombre = (String)tabla.getValueAt(fila, 0);
		chat.setText(chats.get(nombre));

		//reiniciar el num de mensajes no leídos pq ya accedió a su chat
		mba.setValor(0, nombre, 0, 0);
	}


	//Inicializacion de los elementos de la interfaz	
	private void initComponents() {
		setLayout(null);

		labelUs = new javax.swing.JLabel();
		labelUs.setBounds(20, 30, 80, 25);
		add(labelUs);

		nombreUs = new javax.swing.JTextField();
		nombreUs.setBounds(90, 30, 80, 25);
		add(nombreUs);

		//tabla usuarios
		jScrollPane3 = new javax.swing.JScrollPane();
		jScrollPane3.setBounds(20, 80, 250, 370);
		add(jScrollPane3);

		tabla = new javax.swing.JTable();
		tabla.setBounds(20, 80, 250, 370);
		add(tabla);
		tabla.setModel(new ModeloTablaAmigos());
		jScrollPane3.setViewportView(tabla);


		//Recepcion
		labelChat = new javax.swing.JLabel();
		labelChat.setBounds(450, 50, 150, 25);
		add(labelChat);

		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane1.setBounds(320, 80, 350, 370);
		add(jScrollPane1);

		chat = new javax.swing.JTextArea();
		chat.setBounds(320, 80, 350, 370);
		add(chat);


		//Envio
		jScrollPane2 = new javax.swing.JScrollPane();
		jScrollPane2.setBounds(320, 470, 350, 70);
		add(jScrollPane2);

		msjEnviar = new javax.swing.JTextArea();
		msjEnviar.setBounds(320, 470, 350, 70);
		add(msjEnviar);


		btnEnviar = new javax.swing.JButton();
		btnEnviar.setBounds(700, 510, 80, 25);
		add(btnEnviar);        

		btnSolicitarAmistad = new javax.swing.JButton();
		btnSolicitarAmistad.setBounds(60, 460, 200, 30);
		add(btnSolicitarAmistad); 

		btnSolicPend = new javax.swing.JButton();
		btnSolicPend.setBounds(60, 510, 200, 30);
		add(btnSolicPend); 

		labelNotificSolic = new javax.swing.JLabel();
		labelNotificSolic.setBounds(270, 510, 30, 30);
		add(labelNotificSolic);


		nombreUs.setText(nombre);
		labelUs.setText("Usuario:");
		labelChat.setText("Chat:");
		btnEnviar.setText("Enviar");
		btnSolicitarAmistad.setText("Solicitar amistad");
		btnSolicPend.setText("Solicitudes pendientes");
		labelNotificSolic.setText("!!!");
		labelNotificSolic.setForeground(Color.red);

		//comprobar si tiene solicitudes pendientes o no:
		try{
			if(server.obtenerSolicitudes(this.nombre, this.contra).size()>0){
				labelNotificSolic.setVisible(true);				
			}else{
				labelNotificSolic.setVisible(false);	
			}
		}catch (Exception e) {
			System.out.println("Exception in CallbackClient: " + e);
		} 
		

		chat.setColumns(20);
		chat.setRows(5);
		jScrollPane1.setViewportView(chat);

		msjEnviar.setColumns(20);
		msjEnviar.setRows(5);
		jScrollPane2.setViewportView(msjEnviar);              

		nombreUs.setEditable(false);
		chat.setEditable(false);


		//evento cierre jframe
		addWindowListener(this);

		//evento boton Enviar
		btnEnviar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnEnviarMouseClicked(evt);
			}
		});


		//evento boton solicitar amistad
		btnSolicitarAmistad.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnSolicitarAmistadMouseClicked(evt);
			}
		});

		//evento boton ver solicitudes pendientes
		btnSolicPend.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnSolicPendMouseClicked(evt);
			}
		});

		msjEnviar.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updated();
			}
			public void removeUpdate(DocumentEvent e) {
				updated();
			}
			public void insertUpdate(DocumentEvent e) {
				updated();
			}

			public void updated() {
				btnEnviar.setEnabled(msjEnviar.getText().length()>0);
			}
		});


		tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) { 
				if(tabla.getSelectedRowCount()>0){
					tablaKeyListener();
				}
			} 

		});

	}



	public void windowActivated(WindowEvent arg0) {  
	}  
	public void windowClosed(WindowEvent arg0) {  
	}  
	public void windowDeactivated(WindowEvent arg0) {  
	}  
	public void windowDeiconified(WindowEvent arg0) {  
	}  
	public void windowIconified(WindowEvent arg0) {  
	}  
	public void windowOpened(WindowEvent arg0) {  
	}  

	public void setMsjEnviar(String msj){
		msjEnviar.setText(msj);
	}

	public void setMsjInfo(String msj){
		chat.setText(msj);
	}

	public String getMsjEnviar(){
		return msjEnviar.getText();
	}

	public String getMsjInfo(){
		return chat.getText();
	}


	public void setClientes(Vector clientes){ //callbackobjs: se hace en InicioGUI (server.RegisterForCallbacks.restoClientes)
		this.clientes=clientes;
	}


	public void setTabla(){ 
		//un amigo se acaba de desconectar y no queda ningún amigo conectado
		if(clientes.size()==0){ 
			//se vacía su chat
			chat.setText("");			
		}else{
			for(int i=0; i<clientes.size(); i++){
				try{
					CallbackClientInterface nextClient =(CallbackClientInterface)clientes.elementAt(i);	
					//si hay clientes y alguno es nuevo:	
					if(!chats.containsKey(nextClient.getNombre())){
					//si se conecta por primera vez, los mensajes van a estar vacíos					
						chats.put(nextClient.getNombre(), "");
						numMensajes.put(nextClient.getNombre(), "");
					}
				}catch (Exception e) {
					System.out.println("Exception in CallbackClient: " + e);
				} 
			}
		}

		//se actualiza la tabla de amigos conectados
		mba = (ModeloTablaAmigos) tabla.getModel();
		mba.setFilas(clientes, numMensajes);

		if (mba.getRowCount() > 0) {
			tabla.setRowSelectionInterval(0, 0);
		}
		
	}


	public void actualizarChat(String mensaje){
		int fila=tabla.getSelectedRow();		
		String nombreTabla = (String)tabla.getValueAt(fila, 0);
		String partes[]=mensaje.split(":");

		//se añade el mensaje que envió x persona (forma del mensaje = x: mensaje) al array de mensajes de esa persona
		chats.put(partes[0], chats.get(partes[0])+"\n"+mensaje);

		//si la persona que envió el mensaje es la misma del chat que está en pantalla, se muestra, si no, no
		if(partes[0].equals(nombreTabla)){
			chat.setText(chats.get(partes[0]));
		}else{
			//actualizar tabla
			//aumentar en 1 el número de mensajes no leidos
			mba.setValor(1, partes[0], 0, 0);	
			mba.fireTableDataChanged();					
			tabla.setRowSelectionInterval(fila, 0);		
			
		}
		
	}

	public void labelNotificacion(Boolean bool){		
		labelNotificSolic.setText("!!!");
		labelNotificSolic.setVisible(bool);
	}


}