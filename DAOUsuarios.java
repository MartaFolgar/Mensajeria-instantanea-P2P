import java.sql.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author alumnogreibd
 */
public class DAOUsuarios extends AbstractDAO {

    public DAOUsuarios(Connection conexion) {
        super.setConexion(conexion);

    }


    /*comprobar si el usuario está registrado*/
    public Usuario validarUsuario(String nombre, String contra) {
        Usuario resultado=null;
        Connection con;
        PreparedStatement stmUsuario = null;
        ResultSet rsUsuario;

        con = this.getConexion();

        try {
            stmUsuario = con.prepareStatement("select * from usuario where nombre=? and contra=?");
            stmUsuario.setString(1, nombre);
            stmUsuario.setString(2, contra);

            rsUsuario = stmUsuario.executeQuery();

            if (rsUsuario.next()) {
                resultado = new Usuario(rsUsuario.getString("nombre"), rsUsuario.getString("contra"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }   
        return resultado;
    }


    public void registrarUsuario(String nombre, String contra) {
        Connection con;
        PreparedStatement stmUs = null;

        con = super.getConexion();

        try {
            stmUs = con.prepareStatement("insert into usuario(nombre, contra) "
                + "values (?,?)");
            stmUs.setString(1, nombre);
            stmUs.setString(2, contra);
            stmUs.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmUs.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
    }



    public int cambiarContra(String nombre, String contra, String nuevaContra) {
        Connection con;
        PreparedStatement stmUsuario = null;
        con = super.getConexion();
        int result=0;
        try {
            stmUsuario = con.prepareStatement("update usuario " +
                "set contra=? " +
                "where nombre=? and contra=?");
            stmUsuario.setString(1, nuevaContra);
            stmUsuario.setString(2, nombre);
            stmUsuario.setString(3, contra);
            if(stmUsuario.executeUpdate()>0){
                result=1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {            
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return result;
    }



    public int sonAmigos(String nombre1, String nombre2) {
        int result=0;
        Connection con;
        PreparedStatement stmUsuario = null;
        ResultSet rsUsuario;

        con = this.getConexion();

        try {
            stmUsuario = con.prepareStatement("select * from amistad where nombre1=? and nombre2=? and aceptada=?");
            stmUsuario.setString(1, nombre1);
            stmUsuario.setString(2, nombre2);
            stmUsuario.setString(3, "Si");

            rsUsuario = stmUsuario.executeQuery();

            if (rsUsuario.next()) {
                result=1;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }   
        return result;
    }



    public int existeUsuario(String nombre) {
        //Usuario resultado=null;
        int result=0;
        Connection con;
        PreparedStatement stmUsuario = null;
        ResultSet rsUsuario;

        con = this.getConexion();

        try {
            stmUsuario = con.prepareStatement("select * from usuario where nombre=?");
            stmUsuario.setString(1, nombre);

            rsUsuario = stmUsuario.executeQuery();

            if (rsUsuario.next()) {
                result=1;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }   
        return result;
    }



    public int existeSolicitud(String nombre1, String nombre2) {
        //Usuario resultado=null;
        int result=0;
        Connection con;
        PreparedStatement stmUsuario = null;
        ResultSet rsUsuario;

        con = this.getConexion();

        try {
            stmUsuario = con.prepareStatement("select * from amistad where nombre1=? and nombre2=? and aceptada=?");
            stmUsuario.setString(1, nombre1);
            stmUsuario.setString(2, nombre2);
            stmUsuario.setString(3, "No");

            rsUsuario = stmUsuario.executeQuery();

            if (rsUsuario.next()) {
                result=1;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }   
        return result;
    }


    public int crearSolicitud(String nombre1, String nombre2) {
        Connection con;
        PreparedStatement stmUs = null;
        int result=0;

        con = super.getConexion();

        try {
            stmUs = con.prepareStatement("insert into amistad(nombre1, nombre2, aceptada) "
                + "values (?,?,?)");
            stmUs.setString(1, nombre1);
            stmUs.setString(2, nombre2);
            stmUs.setString(3, "No");
            result= stmUs.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmUs.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return result;
    }



    public Vector obtenerSolicitudes(String nombre){
        Vector resultado = new Vector();
        Cliente cl;
        Connection con;
        PreparedStatement stmSolic = null;
        ResultSet rsSolic;

        con = this.getConexion();
        try {
            stmSolic = con.prepareStatement("select * from amistad where nombre2=? and aceptada=?");

            stmSolic.setString(1, nombre);
            stmSolic.setString(2, "No");
            rsSolic = stmSolic.executeQuery();

            while (rsSolic.next()) {
                cl = new Cliente(rsSolic.getString("nombre1"));
                resultado.add(cl);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmSolic.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return resultado;
    }



    public int AceptarSolicitud(String solicitante, String solicitado) throws java.rmi.RemoteException{
        Connection con;
        PreparedStatement stmUsuario = null, stmUs=null;
        con = super.getConexion();
        int result=0;
        try {
            //aceptar. Ejemplo: lola-pepe-si
            stmUsuario = con.prepareStatement("update amistad " +
                "set aceptada=? " +
                "where nombre1=? and nombre2=?");
            stmUsuario.setString(1, "Si");
            stmUsuario.setString(2, solicitante);
            stmUsuario.setString(3, solicitado);
            if(stmUsuario.executeUpdate()>0){

                //añadir amistad en el otro sentido: pepe-lola-si
                stmUs = con.prepareStatement("insert into amistad(nombre1, nombre2, aceptada) "
                    + "values (?,?,?)");
                stmUs.setString(1, solicitado);
                stmUs.setString(2, solicitante);
                stmUs.setString(3, "Si");
                if(stmUs.executeUpdate()>0){
                    result=1;
                }
            }            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {            
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return result;
    }



     public int RechazarSolicitud(String solicitante, String solicitado) throws java.rmi.RemoteException{
        Connection con;
        PreparedStatement stmUsuario = null;
        con = super.getConexion();
        int result=0;
        try {

            stmUsuario = con.prepareStatement("delete from amistad " +
                "where nombre1=? and nombre2=? and aceptada=?");
            stmUsuario.setString(1, solicitante);
            stmUsuario.setString(2, solicitado);
            stmUsuario.setString(3, "No");
            if(stmUsuario.executeUpdate()>0){
                result=1;
            }            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {            
                stmUsuario.close();
            } catch (SQLException e) {
                System.out.println("Imposible cerrar cursores");
            }
        }
        return result;
    }

}


