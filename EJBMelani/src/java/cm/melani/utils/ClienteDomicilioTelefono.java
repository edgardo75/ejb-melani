/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.melani.utils;



/** Descripcion de la Clase
 *
 * @author Edgardo Alvarez™
 * @version 1.0 Build 5600 Feb 20, 2013
 *
 * Esta clase se usa para parsear los datos en xml a clases pojos, en este caso la información es de cliente con sus datos de telefonos y domicilios
 */
public class ClienteDomicilioTelefono {
    
    private DatosCliente item;
    private Integer idusuario;
    private DatosDomicilios Domicilio;
    private ListaTelefonos listaTelefonos;

    //Constructor
    public ClienteDomicilioTelefono(){}

    public DatosCliente getCliente() {
        return item;
    }

    public Integer getIdusuario() {
        return idusuario;
    }   

    public DatosCliente getItem() {
        return item;
    }

    public ListaTelefonos getListaTelefonos() {
        return listaTelefonos;
    }

    public DatosDomicilios getDomicilio() {
        return Domicilio;
    }




}
