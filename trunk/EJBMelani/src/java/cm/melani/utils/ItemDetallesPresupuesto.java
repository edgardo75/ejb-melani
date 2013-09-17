/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.melani.utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
@XStreamAlias("itemdetallespresupuesto")
public class ItemDetallesPresupuesto {
    private Double descuento;
    private Short cantidad;
    private Integer fk_id_producto;
    private Integer fk_id_presupuesto;
    private Double subtotal;
    private Double precio_desc;
    private Double precio;
    public Double getPrecio() {
        return precio;
    }
    public Short getCantidad() {
        return cantidad;
    }
    public Double getDescuento() {
        return descuento;
    }
    public Integer getFk_id_presupuesto() {
        return fk_id_presupuesto;
    }
    public Integer getFk_id_producto() {
        return fk_id_producto;
    }
    public Double getPrecio_desc() {
        return precio_desc;
    }
    public Double getSubtotal() {
        return subtotal;
    }
}
