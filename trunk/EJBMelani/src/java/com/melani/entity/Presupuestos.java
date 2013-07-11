/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Edgardo
 */
@Entity
@Table(name="PRESUPUESTOS")

@NamedQueries({@NamedQuery(name = "Presupuestos.findAll",
query = "SELECT p FROM Presupuestos p"), @NamedQuery(name = "Presupuestos.findByIdPresupuesto",query = "SELECT p FROM Presupuestos p WHERE p.idPresupuesto = :idPresupuesto"), @NamedQuery(name = "Presupuestos.findByObservaciones",
        query = "SELECT p FROM Presupuestos p WHERE p.observaciones = :observaciones"), @NamedQuery(name = "Presupuestos.findByIdUsuarioExpidioPresupuesto",
        query = "SELECT p FROM Presupuestos p WHERE p.idUsuarioFk = :idUsuarioFk"), @NamedQuery(name = "Presupuestos.findByValidez",
        query = "SELECT p FROM Presupuestos p WHERE p.validez = :validez"), @NamedQuery(name = "Presupuestos.findByFechapresupuesto",
        query = "SELECT p FROM Presupuestos p WHERE p.fechapresupuesto = :fechapresupuesto"), @NamedQuery(name = "Presupuestos.findByTotalapagar",
        query = "SELECT p FROM Presupuestos p WHERE p.totalapagar = :totalapagar"), @NamedQuery(name = "Presupuestos.findByIva",
        query = "SELECT p FROM Presupuestos p WHERE p.iva = :iva"), @NamedQuery(name = "Presupuestos.findByNombre",
        query = "SELECT p FROM Presupuestos p WHERE p.nombre = :nombre"), @NamedQuery(name = "Presupuestos.findByApellido",
        query = "SELECT p FROM Presupuestos p WHERE p.apellido = :apellido"), @NamedQuery(name = "Presupuestos.findByTotal",
        query = "SELECT p FROM Presupuestos p WHERE p.total = :total"), @NamedQuery(name = "Presupuestos.findByPorcDescTotal", query = "SELECT p FROM Presupuestos p WHERE p.porcetajedescuentoTOTAL = :porcetajedescuentoTOTAL"), @NamedQuery(name = "Presupuestos.findByRecargototal",
        query = "SELECT p FROM Presupuestos p WHERE p.recargototal = :recargototal"), @NamedQuery(name = "Presupuestos.findByPorcentajerecargo",
        query = "SELECT p FROM Presupuestos p WHERE p.porcentajerecargo = :porcentajerecargo"), @NamedQuery(name = "Presupuestos.findByDescuentoresto",
        query = "SELECT p FROM Presupuestos p WHERE p.descuentoresto = :descuentoresto")})
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
    @Column(name = "TOTAL",precision=15,scale=3)
    private BigDecimal total;
    @Column(name = "OBSERVACIONES",length=5000)
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO_EXPIDIO_PRESUPUESTO")
    private int idUsuarioFk;    
    @Column(name="TOTALAPAGAR",precision=15,scale=3)
    private BigDecimal totalapagar;
    @Column(name = "IVA",precision=15,scale=3)
    private BigDecimal iva;
    @Column(name = "NOMBRE",length=40)
    private String nombre;
    @Column(name = "APELLIDO",length=20)
    private String apellido;
    @Column(name="PORC_DESC_TOTAL",precision=15,scale=2)
    private BigDecimal porcetajedescuentoTOTAL;
    @Column(name = "RECARGOTOTAL",precision=15,scale=2)
    private BigDecimal recargototal;
    @Column(name = "PORCENTAJERECARGO",precision=15,scale=2)
    private BigDecimal porcentajerecargo;
     @Column(name = "DESCUENTORESTO",precision=15,scale=3)
    private BigDecimal descuentoresto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presupuestos",fetch=FetchType.LAZY)
    private List<Detallespresupuesto> detallepresupuestosList;
    
    

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

    
    public BigDecimal getTotalapagar() {
        return totalapagar;
    }

    public void setTotalapagar(BigDecimal totalapagar) {
        this.totalapagar = totalapagar;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPorcetajedescuentoTOTAL() {
        return porcetajedescuentoTOTAL;
    }

    public void setPorcetajedescuentoTOTAL(BigDecimal porcetajedescuento) {
        this.porcetajedescuentoTOTAL = porcetajedescuento;
    }

    public BigDecimal getDescuentoresto() {
        return descuentoresto;
    }

    public void setDescuentoresto(BigDecimal descuentoresto) {
        this.descuentoresto = descuentoresto;
    }

    public BigDecimal getPorcentajerecargo() {
        return porcentajerecargo;
    }

    public void setPorcentajerecargo(BigDecimal porcentajerecargo) {
        this.porcentajerecargo = porcentajerecargo;
    }

    public BigDecimal getRecargototal() {
        return recargototal;
    }

    public void setRecargototal(BigDecimal recargototal) {
        this.recargototal = recargototal;
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

    public String toXML(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String xml ="<presupuesto>\n" +
                "<id>" +this.getIdPresupuesto()+ "</id>\n" +
                "<nombre>" +this.getNombre()+ "</nombre>\n"+
                "<apellido>" +this.getApellido()+ "</apellido>\n"+
                //"<observaciones><![CDATA[" +this.getObservaciones()+ "]]></observaciones>\n"+
                "<observaciones>"+StringEscapeUtils.escapeXml(this.getObservaciones())+"</observaciones>\n"+
                "<totalapagar>" +this.getTotalapagar().toString()+ "</totalapagar>\n"+
                "<usuarioexpidio>" +this.getIdUsuarioFk()+ "</usuarioexpidio>\n"+
                "<iva>" +this.getIva().toString()+ "</iva>\n"+
                "<total>" +this.getTotal().toString()+ "</total>\n"+
                "<fechapresupuesto>" +sdf.format(this.getFechapresupuesto())+ "</fechapresupuesto>\n"+
                "<fechavalidez>" +sdf.format(this.getValidez())+ "</fechavalidez>\n" +
                "<porcentajedescuentototal>" +this.getPorcetajedescuentoTOTAL().toString()+ "</porcentajedescuentototal>\n" +
                "<descuentoresto>"+this.getDescuentoresto().toString()+"</descuentoresto>\n" +
                "<recargototal>"+this.getRecargototal().toString()+"</recargototal>\n" +
                "<porcentajerecargo>"+this.getPorcentajerecargo().toString()+"</porcentajerecargo>\n"+
                "<detallepresupuesto>\n" ;

                if(this.getDetallepresupuestosList().size()==0)
                        xml+=   "</detallepresupuesto>\n";
                else{
                    List<Detallespresupuesto>lista = this.getDetallepresupuestosList();
                    for (Iterator<Detallespresupuesto> it = lista.iterator(); it.hasNext();) {
                        Detallespresupuesto detallespresupuesto = it.next();
                        xml+=detallespresupuesto.toXML();

                    }


                }
            xml+=   "</detallepresupuesto>\n"+

                "</presupuesto>\n";



    return xml;
    }

}
