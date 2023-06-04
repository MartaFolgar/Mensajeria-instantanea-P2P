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

/**
 *
 * @author alumnogreibd
 */
public class ModeloTablaSolicitudes extends AbstractTableModel{
    private Vector solicitudes;

    public ModeloTablaSolicitudes(){
        this.solicitudes=new Vector();
    }
            
    public int getColumnCount (){
        return 1;
    }

    public int getRowCount(){
        return solicitudes.size();
    }

    @Override
    public String getColumnName(int col){
        String nombre="";

        switch (col){
            case 0: nombre= "Usuario"; break;
        }
        return nombre;
    }

    @Override
    public Class getColumnClass(int col){
        Class clase=null;

        switch (col){
            case 0: clase= java.lang.String.class; break;
        }
        return clase;
    }

    @Override
    public boolean isCellEditable(int row, int col){
        return false;
    }

    public Object getValueAt(int row, int col){
        Object resultado=null;

        Cliente cl=(Cliente)solicitudes.elementAt(row);

        switch (col){
            case 0: resultado= cl.getNombre(); break;
        }
        return resultado;
    }

    public void setFilas(Vector solicitudes){
        this.solicitudes=solicitudes;
        fireTableDataChanged();
    }

}
