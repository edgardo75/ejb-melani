/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.ejb;



import com.melani.entity.Calles;

import java.util.ArrayList;
import javax.ejb.Remote;

/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBCallesRemote {

    long addCalles(String descripcion,int idUsuario);

    String searchAllCalles();

    Object recorCountCalles();  

    
    
}
