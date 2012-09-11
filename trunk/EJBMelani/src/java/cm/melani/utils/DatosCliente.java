/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.melani.utils;



/**
 *Clase DatosCliente Utilizada para Instanciar mediante libreria Xstream, con
 * el objetivo de Producir una salida de un Objeto Cliente
 * @version 1.0
 * @author Edgardo Alvarez
 */
public class DatosCliente {
    private String apellido;// variable interna atribute apellido
    private String nombre;// variable interna atribute nombre
    private String email;//variable interna atribute email
    private String det2allescli;//variable interna atribute observaciones
    private Integer nrodocu;//variable interna atribute nrodocu
    private Short idtipodocu;//variable interna atribute idtipodocu
    private float totalcompras;//variable interna atribute totalcompras
    private int totalpuntos;//variable interna atribute totalpuntos
    private long idcliente;//variable interna atribute idcliente
    private Generos genero;//variable interna atribute genero

    /*
     * Este metodo devuelve el apellido de un cliente
     */
    public String getApellido() {
        return apellido;
    }
/*
 * Este metodo "setea" los datos del cliente
 */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /*
     * Este metodo devuelve el email del cliente
     */
    public String getEmail() {
        return email;
    }
    /*
     * Este metodo "setea" el email del cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }
/*
 * Este metodo devuelve el id del Tipo de Documento
 */
    public Short getIdtipodocu() {
        return idtipodocu;
    }
/*
 * Este metodo "setea" el id Tipo de Documento
 */
    public void setIdtipodocu(Short idtipodocu) {
        this.idtipodocu = idtipodocu;
    }
/*
 * Este metodo devuelve el nombre del cliente
 */
    public String getNombre() {
        return nombre;
    }
/*
 * Este metodo "setea" el nombre del cliente
 */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
/*
 * Este metodo devuelve el numero de documento
 */
    public Integer getNrodocu() {
        return nrodocu;
    }
/*
 * Este metodo "setea" el numero de documento
 */
    public void setNrodocu(Integer nrodocu) {
        this.nrodocu = nrodocu;
    }
/*
 * Este metodo devuelve las observaciones de un cliente
 */
    public String getObservaciones() {
        return det2allescli;
    }
/*
 * Este metodo "setea" las observaciones del cliente
 */
    public void setObservaciones(String det2allescli) {
        this.det2allescli = det2allescli;
    }
/*
 * Este metodo devuelve el total de compras
 */
    public float getTotalcompras() {
        return totalcompras;
    }
/*
 * Este metodo "setea" el total de compras
 */
    public void setTotalcompras(float totalcompras) {
        this.totalcompras = totalcompras;
    }
/*
 * Este metodo devuelve el total de puntos del cliente
 */
    public int getTotalpuntos() {
        return totalpuntos;
    }
/*
 * Este metodo "setea" el total de puntos
 */
    public void setTotalpuntos(int totalpuntos) {
        this.totalpuntos = totalpuntos;
    }
/*
 * Este metodo devuelve el id de cliente
 */
    public long getIdcliente() {
        return idcliente;
    }
/*
 * Este metodo "setea" el id del cliente
 */
    public void setIdcliente(long idcliente) {
        this.idcliente = idcliente;
    }

    public Generos getGenero() {
        return genero;
    }

    public void setGenero(Generos genero) {
        this.genero = genero;
    }

    
public class Generos{
    private short idgenero;

        public short getIdgenero() {
            return idgenero;
        }

        public void setIdgenero(short idgenero) {
            this.idgenero = idgenero;
        }


}
    

}
