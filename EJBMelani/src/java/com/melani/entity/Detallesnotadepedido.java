/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
 *
 * @author Edgardo
 */
@Entity
@Table(name = "DETALLESNOTADEPEDIDO")
@NamedQueries({
    @NamedQuery(name = "Detallesnotadepedido.findAll", query = "SELECT d FROM Detallesnotadepedido d"),
    @NamedQuery(name = "Detallesnotadepedido.findByCantidad", query = "SELECT d FROM Detallesnotadepedido d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detallesnotadepedido.findByPrecio", query = "SELECT d FROM Detallesnotadepedido d WHERE d.precio = :precio"),
    @NamedQuery(name = "Detallesnotadepedido.findBySubtotal", query = "SELECT d FROM Detallesnotadepedido d WHERE d.subtotal = :subtotal"),
    @NamedQuery(name = "Detallesnotadepedido.findByEntregado", query = "SELECT d FROM Detallesnotadepedido d WHERE d.entregado = :entregado"),
    @NamedQuery(name = "Detallesnotadepedido.findByCancelado", query = "SELECT d FROM Detallesnotadepedido d WHERE d.cancelado = :cancelado"),
    @NamedQuery(name = "Detallesnotadepedido.findByPendiente", query = "SELECT d FROM Detallesnotadepedido d WHERE d.pendiente = :pendiente"),
    @NamedQuery(name = "Detallesnotadepedido.findByDescuento", query = "SELECT d FROM Detallesnotadepedido d WHERE d.descuento = :descuento"),
    @NamedQuery(name = "Detallesnotadepedido.findByIva", query = "SELECT d FROM Detallesnotadepedido d WHERE d.iva = :iva"),
    @NamedQuery(name = "Detallesnotadepedido.findByFkIdnota", query = "SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdnota = :fkIdnota"),
    @NamedQuery(name = "Detallesnotadepedido.findByFkIdproducto", query = "SELECT d FROM Detallesnotadepedido d WHERE d.detallesnotadepedidoPK.fkIdproducto = :fkIdproducto")})
public class Detallesnotadepedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallesnotadepedidoPK detallesnotadepedidoPK;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    @Column(name = "PRECIO",precision=15,scale=2)
    private BigDecimal precio;
    @Column(name = "SUBTOTAL",precision=15,scale=3)
    private BigDecimal subtotal;
    @Column(name = "ENTREGADO")
    private Character entregado;
    @Column(name = "CANCELADO")
    private Character cancelado;
    @Column(name = "PENDIENTE")
    private Character pendiente;
    @Column(name = "DESCUENTO",precision=15,scale=2)
    private BigDecimal descuento;
    @Column(name = "IVA")
    private BigDecimal iva;
    @JoinColumn(name = "FK_IDNOTA", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private Notadepedido notadepedido;
    @JoinColumn(name = "FK_IDPRODUCTO", referencedColumnName = "SID", insertable = false, updatable = false)
    @ManyToOne
    private Productos productos;
    @Column(name = "ANULADO")
    private Character anulado;
    @Column(name="PRECIO_DESC",precision=15,scale=2)
    private BigDecimal preciocondescuento;
    public Detallesnotadepedido() {
    }
    public Detallesnotadepedido(DetallesnotadepedidoPK detallesnotadepedidoPK) {
        this.detallesnotadepedidoPK = detallesnotadepedidoPK;
    }
    public Detallesnotadepedido(int fkIdnota, int fkIdproducto) {
        this.detallesnotadepedidoPK = new DetallesnotadepedidoPK(fkIdnota, fkIdproducto);
    }
    public DetallesnotadepedidoPK getDetallesnotadepedidoPK() {
        return detallesnotadepedidoPK;
    }
    public void setDetallesnotadepedidoPK(DetallesnotadepedidoPK detallesnotadepedidoPK) {
        this.detallesnotadepedidoPK = detallesnotadepedidoPK;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    public BigDecimal getPreciocondescuento() {
        return preciocondescuento;
    }
    public void setPreciocondescuento(BigDecimal preciocondescuento) {
        this.preciocondescuento = preciocondescuento;
    }
    public Character getAnulado() {
        return anulado;
    }
    public void setAnulado(Character anulado) {
        this.anulado = anulado;
    }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    public Character getEntregado() {
        return entregado;
    }
    public void setEntregado(Character entregado) {
        this.entregado = entregado;
    }
    public Character getCancelado() {
        return cancelado;
    }
    public void setCancelado(Character cancelado) {
        this.cancelado = cancelado;
    }
    public Character getPendiente() {
        return pendiente;
    }
    public void setPendiente(Character pendiente) {
        this.pendiente = pendiente;
    }
    public BigDecimal getDescuento() {
        return descuento;
    }
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    public BigDecimal getIva() {
        return iva;
    }
    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }
    public Notadepedido getNotadepedido() {
        return notadepedido;
    }
    public void setNotadepedido(Notadepedido notadepedido) {
        this.notadepedido = notadepedido;
    }
    public Productos getProductos() {
        return productos;
    }
    public void setProductos(Productos productos) {
        this.productos = productos;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallesnotadepedidoPK != null ? detallesnotadepedidoPK.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallesnotadepedido)) {
            return false;
        }
        Detallesnotadepedido other = (Detallesnotadepedido) object;
        if ((this.detallesnotadepedidoPK == null && other.detallesnotadepedidoPK != null) || (this.detallesnotadepedidoPK != null && !this.detallesnotadepedidoPK.equals(other.detallesnotadepedidoPK))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "entity.Detallesnotadepedido[detallesnotadepedidoPK=" + detallesnotadepedidoPK + "]";
    }
    public String toXML(){
    String item="<itemdetalle>\n" +
                  "<idnota>"+this.getNotadepedido().getId()+"</idnota>\n"
                                    + "<producto>\n" +
                                        "<id>"+this.getProductos().getSid()+"</id>\n" +
                                        "<code>"+this.getProductos().getCodproducto()+"</code>\n"
                                    + "<descripcion>"+this.getProductos().getDescripcion()+"</descripcion>\n"
                                    + "</producto>\n"
                                    + "<cantidad>"+this.getCantidad()+"</cantidad>\n"
                                    + "<precio>"+this.getPrecio()+"</precio>\n" +
                                    "<preciocondescuento>"+this.getPreciocondescuento()+"</preciocondescuento>\n" +
                                    "<descuento>"+this.getDescuento()+"</descuento>\n" +
                                    "<subtotal>"+this.getSubtotal()+"</subtotal>\n" +
                                    "<entregado>"+this.getEntregado()+"</entregado>\n" +
                                    "<iva>"+this.getIva()+"</iva>\n" +
                                    "<cancelado>"+this.getCancelado()+"</cancelado>\n" +
                                    "<anulado>"+this.getAnulado()+"</anulado>\n"
                                    + "</itemdetalle>\n";
    return item;
    }
}
