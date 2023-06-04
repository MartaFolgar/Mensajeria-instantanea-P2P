import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SolicitarAmistadGUI extends javax.swing.JFrame implements WindowListener{
	
	private JButton btnCerrar; //cerrar
	private JButton btnSolicitar; //solicitar	

	private JLabel labelUs;
	private JTextField nombreUs2;
	private JLabel labelIncorrecto;
	private JLabel labelCorrecto;

	
	private String nombreUs1;
	private String contra;
	private CallbackServerInterface server;
	private ClienteGUI clienteGUI;



	//Inicializacion interfaz
	public SolicitarAmistadGUI(String nombreUs1, String contra, CallbackServerInterface server, ClienteGUI clienteGUI) {    	
		super("Solicitud amistad"); 
		this.nombreUs1=nombreUs1;
		this.server=server;
		this.clienteGUI=clienteGUI;
		this.contra=contra;


        //Inicializacion de componentes
		setSize(800, 600);        	
		initComponents();
		setVisible(true);
		btnSolicitar.setEnabled(false);
	}


	//Evento al pulsar la cruz de cierre del jframe
	public void windowClosing(WindowEvent arg0) {  

	}  


	//Cerrar ventana de solicitud 
	private void btnCerrarMouseClicked(java.awt.event.MouseEvent evt) {  
		this.dispose();
		clienteGUI.setVisible(true);
	}
	

	//Solicitar amistad a otro usuario
	private void btnSolicitarMouseClicked(java.awt.event.MouseEvent evt) {  
		try{
			if(server.existeUsuario(nombreUs2.getText())==1){

				if(server.sonAmigos(nombreUs1, nombreUs2.getText())==1){
					labelIncorrecto.setText("Ya son amigos");
					labelIncorrecto.setVisible(true);
				}else if(server.existeSolicitud(nombreUs1, nombreUs2.getText())==1){
					labelIncorrecto.setText("Ya existe esa solicitud");
					labelIncorrecto.setVisible(true);
				}else{
					server.crearSolicitud(nombreUs1, nombreUs2.getText(), contra);
					labelIncorrecto.setVisible(false);
					labelCorrecto.setVisible(true);
					nombreUs2.setText("");
				}
			}else{
				labelIncorrecto.setText("No existe ese usuario");
				labelIncorrecto.setVisible(true);
			}
			
		}catch (Exception e) {
			System.out.println("Exception in CallbackClient: " + e);
		} 
	}
	


	

    //Inicializacion de los elementos de la interfaz	
	private void initComponents() {
		setLayout(null);

		labelUs = new javax.swing.JLabel();
		labelUs.setBounds(240, 190, 500, 30);
		add(labelUs);
		
		nombreUs2 = new javax.swing.JTextField();
		nombreUs2.setBounds(305, 225, 150, 30);
		add(nombreUs2);

		labelIncorrecto = new javax.swing.JLabel();
		labelIncorrecto.setBounds(280, 270, 450, 30);
		add(labelIncorrecto);

		labelCorrecto= new javax.swing.JLabel();
		labelCorrecto.setBounds(265, 270, 450, 30);
		add(labelCorrecto);


		btnSolicitar = new javax.swing.JButton();
		btnSolicitar.setBounds(300, 300, 170, 30);
		add(btnSolicitar); 

		btnCerrar = new javax.swing.JButton();
		btnCerrar.setBounds(640, 460, 120, 30);
		add(btnCerrar);   		


		labelUs.setText("Nombre del usuario al que quiere enviar solicitud:");
		btnSolicitar.setText("Solicitar amistad");
		btnCerrar.setText("Cerrar");
		labelCorrecto.setText("Solicitud enviada correctamente");
		labelIncorrecto.setText("El usuario indicado no exite");
		labelIncorrecto.setForeground(Color.red);

		labelCorrecto.setVisible(false);
		labelIncorrecto.setVisible(false);


		//evento boton solicitar amistad
		btnSolicitar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnSolicitarMouseClicked(evt);
			}
		});

		

		//evento boton Acceder
		btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {				
				btnCerrarMouseClicked(evt);
			}
		});


		nombreUs2.getDocument().addDocumentListener(new DocumentListener() {
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
				btnSolicitar.setEnabled(nombreUs2.getText().length()>0);
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

}