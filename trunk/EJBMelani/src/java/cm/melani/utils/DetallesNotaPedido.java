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
