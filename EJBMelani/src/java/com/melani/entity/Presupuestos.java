/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edgardo
 */
@Entity
@Table(name="PRESUPUESTOS")
@NamedQueries({
    @NamedQuery(name = "Presupuestos.findAll", query = "SELECT p FROM Presupuestos p"),
    @NamedQuery(name = "Presupuestos.findByIdPresupuesto", query = "SELECT p FROM Presupuestos p WHERE p.idPresupuesto = :idPresupuesto"),
    @NamedQuery(name = "Presupuestos.findByFechapresupuesto", query = "SELECT p FROM Presupuestos p WHERE p.fechapresupuesto = :fechapresupuesto"),
    @NamedQuery(name = "Presupuestos.findByValidez", query = "SELECT p FROM Presupuestos p WHERE p.validez = :validez"),
    @NamedQuery(name = "Presupuestos.findByTotal", query = "SELECT p FROM Presupuestos p WHERE p.total = :total"),
    @NamedQuery(name = "Presupuestos.findByObservaciones", query = "SELECT p FROM Presupuestos p WHERE p.observaciones = :observaciones"),
    @NamedQuery(name = "Presupuestos.findByIdUsuarioFk", query = "SELECT p FROM Presupuestos p WHERE p.idUsuarioFk = :idUsuarioFk")})
public class Presupuestos implements Serializable {
    private static final long serialVersionUID = 1L;
  @TableGenerator(name="PresupuestosIdGen", table="ID_GEN_PRE",
    pkColumnName="FNAME",pkColumnValue="Presupuestos" , valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @GeneratedValue(generator="PresupuestosIdGen",strategy=GenerationType.TABLE)
    @Basic(optional = false)
    @Column(name = "ID_PRESUPUESTO")
    private Integer idPresupuesto;
    @Column(name = "FECHAPRESUPUESTO")
    @Temporal(TemporalType.DATE)
    private Date fechapresupuesto;
    @Column(name = "VALIDEZ")
    @Temporal(TemporalType.DATE)
    private Date validez;
    @Column(name = "TOTAL",precision=15,scale=2)
    private BigDecimal total;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO_EXPIDIO_PRESUPUESTO")
    private int idUsuarioFk;    
    @Column(name="DESCUENTOTOTAL",precision=12,scale=2)
    private BigDecimal descuentototal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presupuestos",fetch=FetchType.LAZY)
    private List<Detallespresupuesto> detallepresupuestosList;
    @JoinColumn(name = "ID_CLIENTE_FK", referencedColumnName = "ID_PERSONA")
    @ManyToOne(fetch=FetchType.LAZY)
    private Personas idClienteFk;
    

    public Presupuestos() {
    }

    public Presupuestos(Integer idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public Presupuestos(Integer idPresupuesto, int idUsuarioFk) {
        this.idPresupuesto = idPresupuesto;
        this.idUsuarioFk = idUsuarioFk;
    }

    public Integer getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(Integer idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public Date getFechapresupuesto() {
        return fechapresupuesto;
    }

    public void setFechapresupuesto(Date fechapresupuesto) {
        this.fechapresupuesto = fechapresupuesto;
    }

    public Date getValidez() {
        return validez;
    }

    public void setValidez(Date validez) {
        this.validez = validez;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdUsuarioFk() {
        return idUsuarioFk;
    }

    public void setIdUsuarioFk(int idUsuarioFk) {
        this.idUsuarioFk = idUsuarioFk;
    }

    public List<Detallespresupuesto> getDetallepresupuestosList() {
        return detallepresupuestosList;
    }

    public void setDetallepresupuestosList(List<Detallespresupuesto> detallepresupuestosList) {
        this.detallepresupuestosList = detallepresupuestosList;
    }

    public Personas getIdClienteFk() {
        return idClienteFk;
    }

    public void setIdClienteFk(Personas idClienteFk) {
        this.idClienteFk = idClienteFk;
    }

    public BigDecimal getDescuentototal() {
        return descuentototal;
    }

    public void setDescuentototal(BigDecimal descuentototal) {
        this.descuentototal = descuentototal;
    }


    

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPresupuesto != null ? idPresupuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Presupuestos)) {
            return false;
        }
        Presupuestos other = (Presupuestos) object;
        if ((this.idPresupuesto == null && other.idPresupuesto != null) || (this.idPresupuesto != null && !this.idPresupuesto.equals(other.idPresupuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Presupuestos[idPresupuesto=" + idPresupuesto + "]";
    }

}
