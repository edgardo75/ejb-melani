/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.melani.utils;

/**
 *
 * @author Edgardo
 * @version 1.0 Build 5600 Feb 20, 2013
 */
public class DatosNotaPedido {
    private long idnota;
    private Personas personas;
    private int vendedor;
    private double montototal;
    private double anticipoacum;
    private double anticipo;
    private double saldo;
    private char entregado;
    private int usuario_expidio_nota;
    private int stockfuturo;
    private int usuario_entregado;
    private char anulado;
    private int usuario_cancelo_nota;
    private char cancelado;
    private String observaciones;
    private char pendiente;
    private double montoiva;
    private double recargo;
    private int id_usuario_anulado;
    private String numerodecupon;
    private char enefectivo;
    private Double montototalapagar;
    private Double porc_descuento_total;    
    private Double porcentajerecargo;
    private TarjetaCredito tarjetacredito;
    private Porcentajes porcentaje;
    private DetallesNotaPedido detallesnotapedido;
    private Double descuentonota;
    private String fechaentrega;
    private String fechaanulada;
    private String fechacancelada;
    private String fechacompra;


    public long getIdnota() {
        return idnota;
    }

    public void setIdnota(long idnota) {
        this.idnota = idnota;
    }

    public int getVendedor() {
        return vendedor;
    }

    public String getFechacompra() {
        return fechacompra;
    }
    

    public void setVendedor(int vendedor) {
        this.vendedor = vendedor;
    }

    public double getAnticipoacum() {
        return anticipoacum;
    }

    public void setAnticipoacum(double anticipoacum) {
        this.anticipoacum = anticipoacum;
    }
    

    public int getUsuario_cancelo_nota() {
        return usuario_cancelo_nota;
    }

    public void setUsuario_cancelo_nota(int usuario_cancelo_nota) {
        this.usuario_cancelo_nota = usuario_cancelo_nota;
    }

    public Double getDescuentonota() {
        return descuentonota;
    }

    public String getFechaanulada() {
        return fechaanulada;
    }

    public String getFechacancelada() {
        return fechacancelada;
    }

    public String getFechaentrega() {
        return fechaentrega;
    }

    public Porcentajes getPorcentaje() {
        return porcentaje;
    }

    

    public DetallesNotaPedido getDetallesnotapedido() {
        return detallesnotapedido;
    }

    public Double getMontototalapagar() {
        return montototalapagar;
    }

    public Double getPorc_descuento_total() {
        return porc_descuento_total;
    }

    public Double getPorcentajerecargo() {
        return porcentajerecargo;
    }

 

    
    

    //------------------------------------------------------------------------------------------------
    

    public double getAnticipo() {
        return anticipo;
    }

    public char getAnulado() {
        return anulado;
    }

    public char getEnefectivo() {
        return enefectivo;
    }

    public char getEntregado() {
        return entregado;
    }

    public char getCancelado() {
        return cancelado;
    }

    public void setCancelado(char cancelado) {
        this.cancelado = cancelado;
    }

   
    

    public int getId_usuario_anulado() {
        return id_usuario_anulado;
    }

    public double getMontoiva() {
        return montoiva;
    }

    public double getMontototal() {
        return montototal;
    }

    public String getNumerodecupon() {
        return numerodecupon;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public int getControlastock() {
        return stockfuturo;
    }

    public char getPendiente() {
        return pendiente;
    }

    public double getRecargo() {
        return recargo;
    }

    public double getSaldo() {
        return saldo;
    }

    public int getUsuario_entregado() {
        return usuario_entregado;
    }

    public int getUsuario_expidio_nota() {
        return usuario_expidio_nota;
    }
    
    //-----------------------------------------------------------------------------------------------
    
    

    public Personas getPersonas() {
        return personas;
    }
    
    
   public class Personas{
       private long id;
       private int numerodocu;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getNumerodocu() {
            return numerodocu;
        }

        public void setNumerodocu(int numerodocu) {
            this.numerodocu = numerodocu;
        }
       
       
   
   }
   //------------------------------------------------------------------------------------------

    public Porcentajes getPorcentajes() {
        return porcentaje;
    }

    public TarjetaCredito getTarjetacredito() {
        return tarjetacredito;
    }
   
   //------------------------------------------------------------------------------------------
    public class TarjetaCredito{
        private int id_tarjeta;

        public int getId_tarjeta() {
            return id_tarjeta;
        }
        
        
    
    }
    //----------------------------------------------------------------------------------------
    public class Porcentajes{
        private short id_porcentaje;

        public short getId_porcentaje() {
            return id_porcentaje;
        }
     }
    //-----------------------------------------------------------------------------------------
    
   

    public int getStockfuturo() {
        return stockfuturo;
    }

    public void setStockfuturo(int stockfuturo) {
        this.stockfuturo = stockfuturo;
    }
    //-----------------------------------------------------------------------------------------
  
    
}
