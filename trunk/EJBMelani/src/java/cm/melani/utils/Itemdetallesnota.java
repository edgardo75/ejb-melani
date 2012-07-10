/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.melani.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Edgardo
 */
@XStreamAlias("itemdetallesnota")
public class Itemdetallesnota {

        private int cantidad;
        private long precio;
        private long subtotal;
        private char entregado;
        private char cancelado;
        private char pendiente;
        private long descuento;
        private long iva;
        private long id_nota;
        private long id_producto;
        private char anulado;

        /////////////////---------------------------------------------------------------------



        public char getCancelado() {
            return cancelado;
        }

        public int getCantidad() {
            return cantidad;
        }

        public long getDescuento() {
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

        public long getPrecio() {
            return precio;
        }

        public long getSubtotal() {
            return subtotal;
        }
    //------------------------------------------------------------------------------------

}
