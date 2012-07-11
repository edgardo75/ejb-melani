/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;

import cm.melani.utils.DatosDomicilios;
import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBDomiciliosRemote {

    long addDomicilios(DatosDomicilios xmlDomicilios);

    long addDomicilio(String xmlDomicilio);
    
}
