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
public interface EJBHistoricoPersonaDomicilioRemote {

    long addOneHomePerson(Integer idDomicilio, Integer idPersona,Integer idUsuario);

    String searchAllHistPD();
    
}
