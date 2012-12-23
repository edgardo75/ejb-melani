/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import com.melani.entity.Clientes;
import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBClientesRemote {

    long addDatosCliente(String apellido, String nombre, short idtipo, int numerodocu, String email, String observaciones,short idGenero);

    long addCliente(String xmlClienteDomicilioTelefono);

    String obtenerCliente(long idCliente);

    String obtenerClienteXTipoAndNumeroDocu(short idTipo, int nrodDocu);

    long addClientes(Clientes clientes);

    long updateCliente(Clientes cliente);

    String getCustomerDocNumber(Integer docNumber);

    String searchClientForNameAndLastName(String name,String lastname);

    String addClienteDatosPersonales(String datospersonalescliente);

    String ShowReportClient();
    
}
