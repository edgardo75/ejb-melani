/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.melani.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;

/**
 *
 * @author Edgardo
 */
@XStreamAlias("listaTelefonos")
public class ListaTelefonos {
    private List<DatosTelefonos>list;

    public List<DatosTelefonos> getList() {
        return list;
    }

    
}
