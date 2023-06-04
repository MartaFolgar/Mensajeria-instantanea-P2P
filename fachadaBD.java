import java.sql.*;
import java.io.*;
import java.util.*;

 public class fachadaBD {
    private java.sql.Connection conexion;
	private DAOUsuarios daoUsuarios;

    public fachadaBD (){
		Properties configuracion = new Properties();
        FileInputStream arqConfiguracion;

        try {
			try {
				Class.forName("org.postgresql.Driver");
				System.out.println ( "Encontrado el driver de postgresql" );
			} 
			catch (ClassNotFoundException ex) {
				System.out.println(ex.getMessage());
			}
		

            arqConfiguracion = new FileInputStream("baseDatos.properties");
            configuracion.load(arqConfiguracion);
            arqConfiguracion.close();

            Properties usuario = new Properties(); 
            String gestor = configuracion.getProperty("gestor");
            usuario.setProperty("user", configuracion.getProperty("usuario"));
            usuario.setProperty("password", configuracion.getProperty("clave"));
            this.conexion=java.sql.DriverManager.getConnection("jdbc:"+gestor+"://"+
                    configuracion.getProperty("servidor")+":"+
                    configuracion.getProperty("puerto")+"/"+
                    configuracion.getProperty("baseDatos"),
                    usuario);


            conexion.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            daoUsuarios = new DAOUsuarios(conexion);

        } catch (FileNotFoundException f){
            System.out.println(f.getMessage());
        } catch (IOException i){
            System.out.println(i.getMessage());
        } 
        catch (java.sql.SQLException e){
            System.out.println(e.getMessage());
        }   
        
    }


    public DAOUsuarios getDAOUsuarios(){
        return daoUsuarios;
    }
 
    
 }	
  	  
  
