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
public interface EJBLocalidadesRemote {

    String searchAllLocalidades();

    String searchLocXProvincia(short provincia);

    String searchAllLocalidadesSQL();

    long addLocalidadCompleto(String descripcion, short idprovincia, int codigopostal);

    String searchAllLocalidadesbyidprov(Short idprovincia);
    
}
