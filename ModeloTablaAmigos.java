/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.table.AbstractTableModel;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.*;


public class ModeloTablaAmigos extends AbstractTableModel{
    private Vector amigos;
    private HashMap<String, String> numMensajes;

    public ModeloTablaAmigos(){
        this.amigos=new Vector();
        this.numMensajes=new HashMap<String, String>();
    }

    public int getColumnCount (){
        return 2;
    }

    public int getRowCount(){
        return amigos.size();
    }

    @Override
    public String getColumnName(int col){
        String nombre="";

        switch (col){
            case 0: nombre= "Amigos"; break;
            case 1: nombre= "Sin leer"; break;
        }
        return nombre;
    }

    @Override
    public Class getColumnClass(int col){
        Class clase=null;

        switch (col){
            case 0: clase= java.lang.String.class; break;
            case 1: clase= java.lang.Integer.class; break;
       }
       return clase;
   }

   @Override
   public boolean isCellEditable(int row, int col){
    return false;
}

public Object getValueAt(int row, int col){   

    try{
        Object resultado=null;

        CallbackClientInterface cl=(CallbackClientInterface)amigos.elementAt(row);
        switch (col){

            case 0: resultado= cl.getNombre(); break;
            case 1: resultado= numMensajes.get(cl.getNombre()); break;

       }
       return resultado;
    }catch (Exception e) {
        System.out.println("Exception in CallbackClient: " + e);
    } 
    return null;
}


 //@Override
public void setValor(int tipo, Object nombre, int fila, int columna) {
    if(numMensajes.containsKey(nombre)){

        if((Integer)tipo==1){ //aumentar num mensajes no leidos
            Integer mensajes;
            if(numMensajes.get(nombre).equals("")){
                mensajes=1;
            }else{
                mensajes=Integer.parseInt(numMensajes.get(nombre))+1;
            }    
            numMensajes.put((String)nombre, String.valueOf(mensajes));

        }else if((Integer)tipo==0){ //reiniciar 
            numMensajes.put((String)nombre, "");
        }

    }else{
    }   
    
}


public void setFilas(Vector amigos, HashMap<String, String> numMensajes){
    this.amigos=amigos;
    this.numMensajes=numMensajes;
    fireTableDataChanged();
}

}
