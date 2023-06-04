import java.io.Serializable;


public class Cliente implements Serializable  {
	private String nombre;
	

    public Cliente(String nombre) {
        this.nombre = nombre;
    }


	public String getNombre() {
        return nombre;
    }
   
	public void setNombre(String nombre) {
		this.nombre=nombre;
    }

}
