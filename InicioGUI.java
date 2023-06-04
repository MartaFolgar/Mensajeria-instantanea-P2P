import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class InicioGUI extends javax.swing.JFrame implements WindowListener{

	private JButton btnCambiarContra;
	private JButton btnGuardar;
	private JButton btnAcceder;
	private JButton btnRegistrar;
	private JButton btnVer;
	private JButton btnVer2;

	private JLabel labelUs;
	private JTextField nombreUs;
	private JLabel labelContra;
	private JPasswordField contraUs;
	private JLabel labelCambiarContra;
	private JPasswordField contraNuevaUs;
	private JLabel labelIncorrecto;
	private JLabel labelContraCambiada;

	private int RMIPort;
	private String hostname;
	private Vector clientes=new Vector();
	private CallbackServerInterface server;


	//Inicializacion interfaz
	public InicioGUI(int RMIPort, String hostname) {    	
		super("Autenticación chat");        
		this.RMIPort=RMIPort;
		this.hostname=hostname;

        //Inicializacion de componentes
		setSize(800, 600);        	
		initComponents();
		setVisible(true);
		btnAcceder.setEnabled(false);		


		try{
			String registryURL =  "rmi://localhost:" + String.valueOf(RMIPort) + "/callback";  
			System.out.println("REGISTRO: " + registryURL + "\n");

			server = (CallbackServerInterface)Naming.lookup(registryURL);
		} 
		catch (Exception e) {
			System.out.println("Exception in CallbackClient: " + e);
		} 
	}


	//Evento al pulsar la cruz de cierre del jframe
	public void windowClosing(WindowEvent arg0) {  

	}  


	//Acceder al chat  
	private void btnAccederMouseClicked(java.awt.event.MouseEvent evt) {  
		//se autentica+registra para Callback al usuario y si es correcto: se crea su interfaz gráfica
		crearGUI();
	}


	//Registrar nuevo usuario en el sistema
	private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {  
		try{
			if(!nombreUs.getText().equals("") && !contraUs.getText().equals("")){
				int exec=0;
				try{		
					//se crea el nuevo us en la BD			
					server.registrarUsuario(nombreUs.getText(), contraUs.getText());
				}catch (Exception e) {
					exec=1;
				}finally{
					if(exec==0){
						//se accede al chat: autenticacion+registro para Callback
						crearGUI();
					}else{
						labelIncorrecto.setText("Usuario ya registrado.");
						labelIncorrecto.setVisible(true);
					}
				} 
				
			}else{
				labelIncorrecto.setText("Usuario incorrecto.");
				labelIncorrecto.setVisible(true);
			}			
		}catch (Exception e) {
			System.out.println("Exception in CallbackClient: " + e);
		} 
	}


	//Cambiar contraseña
	private void btnCambiarContraMouseClicked(java.awt.event.MouseEvent evt) { 
		contraNuevaUs.setText("");
		labelIncorrecto.setVisible(false);
		labelContraCambiada.setVisible(true);
		btnGuardar.setVisible(true);
		labelCambiarContra.setVisible(true);
		btnVer2.setVisible(true);	
		contraNuevaUs.setVisible(true);		
	}


	//Guardar nueva contraseña
	private void btnGuardarMouseClicked(java.awt.event.MouseEvent evt) {  
		try{
			if(server.cambiarContra(nombreUs.getText(), contraUs.getText(), contraNuevaUs.getText())>0){
				labelContraCambiada.setText("Contraseña cambiada correctamente.");
				contraUs.setText("");
				btnGuardar.setVisible(false);			
				labelCambiarContra.setVisible(false);
				contraNuevaUs.setVisible(false);
				btnVer2.setVisible(false);
				labelIncorrecto.setVisible(false);
			}else{
				labelIncorrecto.setText("Usuario incorrecto.");
				contraUs.setText("");
				contraNuevaUs.setText("");
				labelIncorrecto.setVisible(true);
			}			

		}catch (Exception e) {
			labelIncorrecto.setText("Usuario incorrecto.");
			labelIncorrecto.setVisible(true);
		} 
	}


	//Ver contraseña
	private void btnVerMouseClicked(java.awt.event.MouseEvent evt) {  
		if(contraUs.getEchoChar()=='\u2022'){ //si la clave no está visible: hacerla visible
			contraUs.setEchoChar((char)0); 
        }else if(contraUs.getEchoChar()==(char)0){ //si la clave está visible: ocultarla
        	contraUs.setEchoChar((char)'\u2022');
        }
    }


	//Ver nueva contraseña
    private void btnVer2MouseClicked(java.awt.event.MouseEvent evt) {  
		if(contraNuevaUs.getEchoChar()=='\u2022'){ //si la clave no está visible: hacerla visible
			contraNuevaUs.setEchoChar((char)0); 
        }else if(contraNuevaUs.getEchoChar()==(char)0){ //si la clave está visible: ocultarla
        	contraNuevaUs.setEchoChar((char)'\u2022');
        }
    }


	//Crear interfaz grafica del chat
    private void crearGUI(){
    	try{
    		CallbackClientInterface callbackObj =  new CallbackClientImpl();  	
    		callbackObj.setNombre(nombreUs.getText()); 

    		int exc=0;
    		try{
				//se intenta registrar el usuario
    			clientes=server.registerForCallback(callbackObj, contraUs.getText());				
    		}catch (Exception e) {
				//si la autenticación es incorrecta o si no se pudo registrar: lanza mensaje de error
    			exc=1;
    			if(e.getMessage().equals("Usuario ya conectado.")){
    				labelIncorrecto.setText("Usuario ya conectado.");
    				labelIncorrecto.setVisible(true);
    			}else if(e.getMessage().equals("Usuario incorrecto.")){
    				labelIncorrecto.setText("Usuario incorrecto.");
    				labelIncorrecto.setVisible(true);
    			}		

    			nombreUs.setText("");
    			contraUs.setText("");
    		}finally{
				//si logró autenticarse y registrarse en el sistema:
    			if(exc!=1){
					//se crea la ventana del chat
    				ClienteGUI frame=new ClienteGUI(nombreUs.getText(), contraUs.getText(), this.RMIPort, this.hostname, callbackObj, server);
    				callbackObj.setGUI(frame);
    				callbackObj.setClientes(clientes);
    				frame.setClientes(clientes);

    				if(clientes.size()>0){					
    					frame.setTabla();	
    				}

					//se abre el chat
    				this.dispose();
    				frame.setVisible(true);	
    			}
    		} 

    	}catch (Exception e) {
    		System.out.println("Exception in CallbackClient: " + e);
    	} 
    }




    //Inicializacion de los elementos de la interfaz	
    private void initComponents() {
    	setLayout(null);


    	labelUs = new javax.swing.JLabel();
    	labelUs.setBounds(310, 100, 300, 30);
    	add(labelUs);

    	nombreUs = new javax.swing.JTextField();
    	nombreUs.setBounds(300, 135, 150, 30);
    	add(nombreUs);

    	labelContra = new javax.swing.JLabel();
    	labelContra.setBounds(330, 190, 300, 30);
    	add(labelContra);

    	contraUs = new javax.swing.JPasswordField();
    	contraUs.setBounds(300, 225, 155, 30);
    	add(contraUs);

    	btnVer = new javax.swing.JButton();
    	btnVer.setBounds(470, 225, 35, 30);
    	add(btnVer); 

    	labelCambiarContra = new javax.swing.JLabel();
    	labelCambiarContra.setBounds(305, 280, 300, 30);
    	add(labelCambiarContra);

    	contraNuevaUs = new javax.swing.JPasswordField();
    	contraNuevaUs.setBounds(300, 320, 155, 30);
    	add(contraNuevaUs);

    	btnVer2 = new javax.swing.JButton();
    	btnVer2.setBounds(470, 320, 35, 30);
    	add(btnVer2); 

    	labelIncorrecto = new javax.swing.JLabel();
    	labelIncorrecto.setBounds(305, 370, 450, 30);
    	add(labelIncorrecto);

    	labelContraCambiada= new javax.swing.JLabel();
    	labelContraCambiada.setBounds(265, 295, 450, 30);
    	add(labelContraCambiada);


    	btnCambiarContra = new javax.swing.JButton();
    	btnCambiarContra.setBounds(60, 460, 180, 30);
    	add(btnCambiarContra); 

    	btnGuardar = new javax.swing.JButton();
    	btnGuardar.setBounds(320, 410, 120, 30);
    	add(btnGuardar); 

    	btnRegistrar = new javax.swing.JButton();
    	btnRegistrar.setBounds(640, 410, 120, 30);
    	add(btnRegistrar); 

    	btnAcceder = new javax.swing.JButton();
    	btnAcceder.setBounds(640, 460, 120, 30);
    	add(btnAcceder);   		


    	labelUs.setText("Nombre de usuario:");
    	btnAcceder.setText("Acceder");  
    	labelContra.setText("Contraseña:");
    	btnRegistrar.setText("Registrarse");   
    	labelIncorrecto.setText("Usuario incorrecto."); 
    	labelIncorrecto.setForeground(Color.red);
    	labelIncorrecto.setVisible(false);
    	btnGuardar.setText("Guardar");
    	btnCambiarContra.setText("Cambiar contraseña");
    	labelCambiarContra.setText("Nueva contraseña:");

    	labelContraCambiada.setVisible(false);
    	btnGuardar.setVisible(false);
    	labelCambiarContra.setVisible(false);
    	contraNuevaUs.setVisible(false);
    	btnVer2.setVisible(false);


		//evento boton cambiar contra
    	btnCambiarContra.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {				
    			btnCambiarContraMouseClicked(evt);
    		}
    	});

		//evento boton guardar contra cambiada
    	btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {				
    			btnGuardarMouseClicked(evt);
    		}
    	});

		//evento boton Acceder
    	btnAcceder.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {				
    			btnAccederMouseClicked(evt);
    		}
    	});


		//evento boton Registrar
    	btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {				
    			btnRegistrarMouseClicked(evt);
    		}
    	});


    	btnVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("ojo.jpg"))); 
    	btnVer.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {				
    			btnVerMouseClicked(evt);
    		}
    	});

    	btnVer2.setIcon(new javax.swing.ImageIcon(getClass().getResource("ojo.jpg"))); 
    	btnVer2.addMouseListener(new java.awt.event.MouseAdapter() {
    		public void mouseClicked(java.awt.event.MouseEvent evt) {				
    			btnVer2MouseClicked(evt);
    		}
    	});

    	contraUs.getDocument().addDocumentListener(new DocumentListener() {
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
    			btnAcceder.setEnabled(contraUs.getText().length()>0);
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