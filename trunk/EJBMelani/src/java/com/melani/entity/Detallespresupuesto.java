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
@Table(name = "DETALLESPRESUPUESTO")
@NamedQueries({
    @NamedQuery(name = "Detallespresupuesto.findAll", query = "SELECT d FROM Detallespresupuesto d"),
    @NamedQuery(name = "Detallespresupuesto.findByIdDpFk", query = "SELECT d FROM Detallespresupuesto d WHERE d.detallespresupuestoPK.idDpFk = :idDpFk"),
    @NamedQuery(name = "Detallespresupuesto.findByFkProducto", query = "SELECT d FROM Detallespresupuesto d WHERE d.detallespresupuestoPK.fkProducto = :fkProducto"),
    @NamedQuery(name = "Detallespresupuesto.findBySubtotal", query = "SELECT d FROM Detallespresupuesto d WHERE d.subtotal = :subtotal"),
    @NamedQuery(name = "Detallespresupuesto.findByDescuento", query = "SELECT d FROM Detallespresupuesto d WHERE d.descuento = :descuento"),
    @NamedQuery(name = "Detallespresupuesto.findByIva", query = "SELECT d FROM Detallespresupuesto d WHERE d.iva = :iva"),
    @NamedQuery(name = "Detallespresupuesto.findByCantidad", query = "SELECT d FROM Detallespresupuesto d WHERE d.cantidad = :cantidad")})
public class Detallespresupuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallespresupuestoPK detallespresupuestoPK;
    @Column(name = "SUBTOTAL")
    private BigDecimal subtotal;
    @Column(name = "DESCUENTO")
    private BigDecimal descuento;
    @Column(name = "IVA")
    private BigDecimal iva;
    @Column(name = "CANTIDAD")
    private Short cantidad;
    @Column(name="DESC_PROD")
    private BigDecimal descprod;
    @JoinColumn(name = "ID_DP_FK", referencedColumnName = "ID_PRESUPUESTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Presupuestos presupuestos;
    @JoinColumn(name = "FK_PRODUCTO", referencedColumnName = "SID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Productos productos;


    public Detallespresupuesto() {
    }

    public Detallespresupuesto(DetallespresupuestoPK detallespresupuestoPK) {
        this.detallespresupuestoPK = detallespresupuestoPK;
    }

    public Detallespresupuesto(int idDpFk, int fkProducto) {
        this.detallespresupuestoPK = new DetallespresupuestoPK(idDpFk, fkProducto);
    }

    public DetallespresupuestoPK getDetallespresupuestoPK() {
        return detallespresupuestoPK;
    }

    public void setDetallespresupuestoPK(DetallespresupuestoPK detallespresupuestoPK) {
        this.detallespresupuestoPK = detallespresupuestoPK;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
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

    public Short getCantidad() {
        return cantidad;
    }

    public void setCantidad(Short cantidad) {
        this.cantidad = cantidad;
    }

    public Presupuestos getPresupuestos() {
        return presupuestos;
    }

    public void setPresupuestos(Presupuestos presupuestos) {
        this.presupuestos = presupuestos;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    public BigDecimal getDescprod() {
        return descprod;
    }

    public void setDescprod(BigDecimal descprod) {
        this.descprod = descprod;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallespresupuestoPK != null ? detallespresupuestoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallespresupuesto)) {
            return false;
        }
        Detallespresupuesto other = (Detallespresupuesto) object;
        if ((this.detallespresupuestoPK == null && other.detallespresupuestoPK != null) || (this.detallespresupuestoPK != null && !this.detallespresupuestoPK.equals(other.detallespresupuestoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Detallespresupuesto[detallespresupuestoPK=" + detallespresupuestoPK + "]";
    }

}