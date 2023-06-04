import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SolicPendientesGUI extends javax.swing.JFrame implements WindowListener{
	
	private JButton btnCerrar; //cerrar
	private JButton btnAceptar; //aceptar solicitud	
	private JButton btnRechazar; //rechazar solicitud	

	private JLabel labelUs;
	private JTable tabla;
	private JScrollPane jScrollPane;
	
	private String nombreUs1;
	private String contra;
	private CallbackServerInterface server;
	private ClienteGUI clienteGUI;
	private CallbackClientInterface callbackObj;



	//Inicializacion interfaz
	public SolicPendientesGUI(String nombreUs1, String contra, CallbackServerInterface server, ClienteGUI clienteGUI, CallbackClientInterface callbackObj) {    	
		super("Solicitud amistad"); 
		this.nombreUs1=nombreUs1;
		this.server=server;
		this.clienteGUI=clienteGUI;
		this.callbackObj=callbackObj;
		this.contra=contra;


        //Inicializacion de componentes
		setSize(800, 600);        	
		initComponents();
		setVisible(true);

		setFilasTabla();		
	}


	//Evento al pulsar la cruz de cierre del jframe
	public void windowClosing(WindowEvent arg0) {  

	}  


	//Cerrar ventana de solicitud 
	private void btnCerrarMouseClicked(java.awt.event.MouseEvent evt) {  
		this.dispose();
		clienteGUI.setVisible(true);		
	}


	//Aceptar solicitud de otro usuario
	private void btnAceptarMouseClicked(java.awt.event.MouseEvent evt) {  
		try{
			String nombre = (String)tabla.getValueAt(tabla.getSelectedRow(), 0);
			int result=server.AceptarSolicitud(nombre, nombreUs1, contra); //solicitante y solicitado

			//actualizar tabla
			setFilasTabla();

			//actualizar mis clientes
			server.actClientesSolicitado(callbackObj, nombre); //nombre del solicitante

			//notificar al cliente al que se le aceptó la solicitud
			server.actClientesSolicitante(nombre, nombreUs1); //solicitante, solicitado

			//si le queda alguna solicitud pendiente, seguirá mostrándose el label
			if(server.obtenerSolicitudes(this.nombreUs1, this.contra).size()>0){
				this.clienteGUI.labelNotificacion(true);				
			}else{
				this.clienteGUI.labelNotificacion(false);	
			}

		}catch (Exception e) {
			System.out.println("Exception in CallbackClient: " + e);
		} 
	}
	

	//Rechazar solicitud de otro usuario
	private void btnRechazarMouseClicked(java.awt.event.MouseEvent evt) {  
		try{
			String nombre = (String)tabla.getValueAt(tabla.getSelectedRow(), 0);
			int result=server.RechazarSolicitud(nombre, nombreUs1, contra); //solicitante y solicitado

			//actualizar tabla
			setFilasTabla();

			//si le queda alguna solicitud pendiente, seguirá mostrándose el label
			if(server.obtenerSolicitudes(this.nombreUs1, this.contra).size()>0){
				this.clienteGUI.labelNotificacion(true);			
			}else{
				this.clienteGUI.labelNotificacion(false);	
			}

		}catch (Exception e) {
			System.out.println("Exception in CallbackClient: " + e);
		} 
	}
	

    //Inicializacion de los elementos de la interfaz	
	private void initComponents() {
		setLayout(null);

		labelUs = new javax.swing.JLabel();
		labelUs.setBounds(260, 70, 500, 30);
		add(labelUs);

		//tabla usuarios
		jScrollPane = new javax.swing.JScrollPane();
		jScrollPane.setBounds(280, 100, 210, 320);
		add(jScrollPane);

		tabla = new javax.swing.JTable();
		tabla.setBounds(280, 100, 210, 320);
		add(tabla);
		tabla.setModel(new ModeloTablaSolicitudes());
		jScrollPane.setViewportView(tabla);

		btnAceptar = new javax.swing.JButton();
		btnAceptar.setBounds(300, 440, 170, 30);
		add(btnAceptar); 

		btnRechazar = new javax.swing.JButton();
		btnRechazar.setBounds(300, 480, 170, 30);
		add(btnRechazar); 

		btnCerrar = new javax.swing.JButton();
		btnCerrar.setBounds(640, 480, 120, 30);
		add(btnCerrar);   		


		labelUs.setText("Usuarios que le han solicitado amistad:");
		btnAceptar.setText("Aceptar amistad");
		btnRechazar.setText("Rechazar amistad");
		btnCerrar.setText("Cerrar");



		//evento boton aceptar amistad
		btnAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnAceptarMouseClicked(evt);
			}
		});



		//evento boton rechazar amistad
		btnRechazar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnRechazarMouseClicked(evt);
			}
		});

		

		//evento boton Acceder
		btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnCerrarMouseClicked(evt);
			}
		});


addWindowListener(this);

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


//actualizar tabla de nombre de solicitudes
public void setFilasTabla(){ //clase Cliente
	try{
		Vector solicitudes = server.obtenerSolicitudes(this.nombreUs1, contra); 
		ModeloTablaSolicitudes mba;
		mba = (ModeloTablaSolicitudes) tabla.getModel();
		mba.setFilas(solicitudes);

		if (mba.getRowCount() > 0) {
			tabla.setRowSelectionInterval(0, 0);
		}


	}catch (Exception e) {
		System.out.println("Exception in CallbackClient: " + e);
	} 
}	

}