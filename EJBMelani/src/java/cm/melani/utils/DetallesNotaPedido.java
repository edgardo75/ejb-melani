/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.melani.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
  //----------------------------------------------------------------------------------------
@XStreamAlias("detallesnotapedido")
    public class DetallesNotaPedido{

    @XStreamImplicit
    private List<Itemdetallesnota> list;

    
    public List<Itemdetallesnota> getDetallesnota() {
        return list;
    }

    public void setDetallesnota(List<Itemdetallesnota> list) {
        this.list = list;
    }
    
        
    }
