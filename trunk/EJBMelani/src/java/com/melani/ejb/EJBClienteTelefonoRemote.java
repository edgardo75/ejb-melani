/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBClienteTelefonoRemote {

    String addClienteTelefono(String numero, String prefijo, long idcliente);
    
}
