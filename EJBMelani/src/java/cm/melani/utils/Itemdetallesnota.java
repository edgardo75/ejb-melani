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
@XStreamAlias("itemdetallesnota")
public class Itemdetallesnota {

        private int cantidad;
        private Double precio;
        private Double subtotal;
        private char entregado;
        private char cancelado;
        private char pendiente;
        private Double descuento;
        private long iva;
        private long id_nota;
        private long id_producto;
        private char anulado;
        private Double preciocondescuento;

    public Double getPreciocondescuento() {
        return preciocondescuento;
    }




        /////////////////---------------------------------------------------------------------






        public char getCancelado() {
            return cancelado;
        }

        public int getCantidad() {
            return cantidad;
        }

        public Double getDescuento() {
            return descuento;
        }

        public char getEntregado() {
            return entregado;
        }

    public char getAnulado() {
        return anulado;
    }
        

        public long getId_nota() {
            return id_nota;
        }

        public long getId_producto() {
            return id_producto;
        }

        public long getIva() {
            return iva;
        }

        public char getPendiente() {
            return pendiente;
        }

        public Double getPrecio() {
            return precio;
        }

        public Double getSubtotal() {
            return subtotal;
        }
    //------------------------------------------------------------------------------------

}
