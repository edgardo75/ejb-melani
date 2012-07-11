/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import cm.melani.utils.DatosTelefonos;
import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBTelefonosRemote {

    long addTelefonos(DatosTelefonos datosTel);
    
}
