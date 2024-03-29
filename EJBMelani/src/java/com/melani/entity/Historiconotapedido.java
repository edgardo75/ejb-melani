/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "HISTORICONOTAPEDIDO", catalog = "", schema = "")
@NamedQueries({@NamedQuery(name = "Historiconotapedido.findAll", query = "SELECT h FROM Historiconotapedido h"),
@NamedQuery(name = "Historiconotapedido.findByIdhistorico", query = "SELECT h FROM Historiconotapedido h WHERE h.idhistorico = :idhistorico"),
@NamedQuery(name = "Historiconotapedido.findByAnticipo", query = "SELECT h FROM Historiconotapedido h WHERE h.anticipo = :anticipo"),
@NamedQuery(name = "Historiconotapedido.findByIdusuariocancelo", query = "SELECT h FROM Historiconotapedido h WHERE h.idusuariocancelo = :idusuariocancelo"),
@NamedQuery(name = "Historiconotapedido.findByPendiente", query = "SELECT h FROM Historiconotapedido h WHERE h.pendiente = :pendiente"),
@NamedQuery(name = "Historiconotapedido.findByTotal", query = "SELECT h FROM Historiconotapedido h WHERE h.total = :total"),
@NamedQuery(name = "Historiconotapedido.findByIdusuarioanulo", query = "SELECT h FROM Historiconotapedido h WHERE h.idusuarioanulo = :idusuarioanulo"),
@NamedQuery(name = "Historiconotapedido.findBySaldo", query = "SELECT h FROM Historiconotapedido h WHERE h.saldo = :saldo"),
@NamedQuery(name = "Historiconotapedido.findByHoraregistro", query = "SELECT h FROM Historiconotapedido h WHERE h.horaregistro = :horaregistro"),
@NamedQuery(name = "Historiconotapedido.findByFecharegistro", query = "SELECT h FROM Historiconotapedido h WHERE h.fecharegistro = :fecharegistro"),
@NamedQuery(name = "Historiconotapedido.findByAccion", query = "SELECT h FROM Historiconotapedido h WHERE h.accion = :accion"),
@NamedQuery(name = "Historiconotapedido.findByPorcentajeaplicado", query = "SELECT h FROM Historiconotapedido h WHERE h.porcentajeaplicado = :porcentajeaplicado"),
@NamedQuery(name = "Historiconotapedido.findByDescuento", query = "SELECT h FROM Historiconotapedido h WHERE h.descuento = :descuento"),
@NamedQuery(name = "Historiconotapedido.findByIdusuarioexpidio", query = "SELECT h FROM Historiconotapedido h WHERE h.idusuarioexpidio = :idusuarioexpidio"),
@NamedQuery(name = "Historiconotapedido.findByTotalapagar", query = "SELECT h FROM Historiconotapedido h WHERE h.totalapagar = :totalapagar"),
@NamedQuery(name = "Historiconotapedido.findByObservaciones", query = "SELECT h FROM Historiconotapedido h WHERE h.observaciones = :observaciones"),
@NamedQuery(name = "Historiconotapedido.findByRecargo", query = "SELECT h FROM Historiconotapedido h WHERE h.recargo = :recargo"),
@NamedQuery(name = "Historiconotapedido.findByIdusuarioentrega", query = "SELECT h FROM Historiconotapedido h WHERE h.idusuarioentrega = :idusuarioentrega"),
@NamedQuery(name = "Historiconotapedido.findByPorcrecargo", query = "SELECT h FROM Historiconotapedido h WHERE h.porcrecargo = :porcrecargo"),
@NamedQuery(name = "Historiconotapedido.findByEntregado", query = "SELECT h FROM Historiconotapedido h WHERE h.entregado = :entregado"),
@NamedQuery(name = "Historiconotapedido.findByCancelado", query = "SELECT h FROM Historiconotapedido h WHERE h.cancelado = :cancelado"),
@NamedQuery(name = "Historiconotapedido.findByAnulado", query = "SELECT h FROM Historiconotapedido h WHERE h.anulado = :anulado"),
@NamedQuery(name = "Historiconotapedido.findByPorcdesc", query = "SELECT h FROM Historiconotapedido h WHERE h.porcdesc = :porcdesc")})
public class Historiconotapedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.TABLE,generator="HistoriconotapedidoIdGen")
    @TableGenerator(name="HistoriconotapedidoIdGen", table="ID_GEN_HIST_NOTAP",
    pkColumnName="FNAME",pkColumnValue="Historiconotapedido" , valueColumnName="FKEY",
    allocationSize=1)
    @Column(name = "IDHISTORICO")
    private Integer idhistorico;
    @Column(name = "ANTICIPO",precision=15,scale=3)
    private BigDecimal anticipo;
    @Column(name = "IDUSUARIOCANCELO")
    private Integer idusuariocancelo;
    @Column(name = "PENDIENTE",length=1)
    private Character pendiente;
    @Column(name = "TOTAL",precision=15,scale=3)
    private BigDecimal total;
    @Column(name = "IDUSUARIOANULO")
    private Integer idusuarioanulo;
    @Column(name = "SALDO",precision=15,scale=3)
    private BigDecimal saldo;
    @Column(name = "HORAREGISTRO")
    @Temporal(TemporalType.TIME)
    private Date horaregistro;
    @Column(name = "FECHAREGISTRO")
    @Temporal(TemporalType.DATE)
    private Date fecharegistro;
    @Column(name = "ACCION",length=100)
    private String accion;
    @Column(name = "PORCENTAJEAPLICADO")
    private Short porcentajeaplicado;
    @Column(name = "DESCUENTO",precision=15,scale=3)
    private BigDecimal descuento;
    @Column(name = "IDUSUARIOEXPIDIO")
    private Integer idusuarioexpidio;
    @Column(name = "TOTALAPAGAR",precision=15,scale=3)
    private BigDecimal totalapagar;
    @Column(name = "OBSERVACIONES",length=32000)
    private String observaciones;
    @Column(name = "RECARGO",precision=15,scale=3)
    private BigDecimal recargo;
    @Column(name = "IDUSUARIOENTREGA")
    private Integer idusuarioentrega;
    @Column(name = "PORCRECARGO",precision=15,scale=3)
    private BigDecimal porcrecargo;
    @Column(name = "ENTREGADO",length=1)
    private Character entregado;
    @Column(name = "CANCELADO",length=1)
    private Character cancelado;
    @Column(name = "ANULADO",length=1)
    private Character anulado;
    @Column(name = "PORCDESC",precision=15,scale=3)
    private BigDecimal porcdesc;
    @JoinColumn(name="FKIDNOTAPEDIDO_ID",referencedColumnName="ID")
    @ManyToOne
    private Notadepedido fkidnotapedido;
    public Notadepedido getFkidnotapedido() {
        return fkidnotapedido;
    }
    public void setFkidnotapedido(Notadepedido fkidnotapedido) {
        this.fkidnotapedido = fkidnotapedido;
    }
    public Historiconotapedido() {
    }
    public Historiconotapedido(Integer idhistorico) {
        this.idhistorico = idhistorico;
    }
    public BigDecimal getPorcentajedesc() {
        return porcdesc;
    }
    public void setPorcentajedesc(BigDecimal porcentajedesc) {
        this.porcdesc = porcentajedesc;
    }
    public Short getPorcentajeaplicado() {
        return porcentajeaplicado;
    }
    public void setPorcentajeaplicado(Short porcentajeaplicado) {
        this.porcentajeaplicado = porcentajeaplicado;
    }
    public Character getPendiente() {
        return pendiente;
    }
    public String getAccion() {
        return accion;
    }
    public void setAccion(String accion) {
        this.accion = accion;
    }   
    public void setPendiente(Character pendiente) {
        this.pendiente = pendiente;
    }
    public Character getEntregado() {
        return entregado;
    }
    public void setEntregado(Character entregado) {
        this.entregado = entregado;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public Integer getIdusuarioexpidio() {
        return idusuarioexpidio;
    }
    public void setIdusuarioexpidio(Integer idusuarioexpidio) {
        this.idusuarioexpidio = idusuarioexpidio;
    }
    public BigDecimal getSaldo() {
        return saldo;
    }
    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    public Integer getIdusuarioentrega() {
        return idusuarioentrega;
    }
    public void setIdusuarioentrega(Integer idusuarioentrega) {
        this.idusuarioentrega = idusuarioentrega;
    }
    public Date getFecharegistro() {
        return fecharegistro;
    }
    public Integer getIdusuariocancelo() {
        return idusuariocancelo;
    }
    public void setIdusuariocancelo(Integer idusuariocancelo) {
        this.idusuariocancelo = idusuariocancelo;
    }    
    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public BigDecimal getAnticipo() {
        return anticipo;
    }
    public Date getHoraregistro() {
        return horaregistro;
    }
    public void setHoraregistro(Date horaregistro) {
        this.horaregistro = horaregistro;
    }
    public void setAnticipo(BigDecimal anticipo) {
        this.anticipo = anticipo;
    }
    public Integer getIdhistorico() {
        return idhistorico;
    }
    public void setIdhistorico(Integer idhistorico) {
        this.idhistorico = idhistorico;
    }
    public Integer getIdusuarioanulo() {
        return idusuarioanulo;
    }
    public void setIdusuarioanulo(Integer idusuarioanulo) {
        this.idusuarioanulo = idusuarioanulo;
    }
    public BigDecimal getDescuento() {
        return descuento;
    }
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }
    public BigDecimal getPorcrecargo() {
        return porcrecargo;
    }
    public void setPorcrecargo(BigDecimal porcrecargo) {
        this.porcrecargo = porcrecargo;
    }
    public BigDecimal getRecargo() {
        return recargo;
    }
    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }
    public BigDecimal getTotalapagar() {
        return totalapagar;
    }
    public void setTotalapagar(BigDecimal totalapagar) {
        this.totalapagar = totalapagar;
    }
    public Character getCancelado() {
        return cancelado;
    }
    public void setCancelado(Character cancelado) {
        this.cancelado = cancelado;
    }
    public Character getAnulado() {
        return anulado;
    }
    public void setAnulado(Character anulado) {
        this.anulado = anulado;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idhistorico != null ? idhistorico.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historiconotapedido)) {
            return false;
        }
        Historiconotapedido other = (Historiconotapedido) object;
        if ((this.idhistorico == null && other.idhistorico != null) || (this.idhistorico != null && !this.idhistorico.equals(other.idhistorico))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.melani.entidades.Historiconotapedido[idhistorico=" + idhistorico + "]";
    }
    public String toXML(){
        //-------------------------------------------------------------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fereg = "";
        if(this.getFecharegistro()!=null)
            fereg =sdf.format(this.getFecharegistro());
        String hourreg = "";
        if(this.getHoraregistro()!=null)
            hourreg=sdf.format(this.getHoraregistro());
        //-------------------------------------------------------------------------------------
            String item = "";
        try {
              item+="<item>\n"
                           + "<idhistorico>"+this.getIdhistorico()+"</idhistorico>\n"                          
                           + "<anticipo>"+this.getAnticipo().toString()+"</anticipo>\n"
                           + "<entregado>"+this.getEntregado().toString()+"</entregado>\n"
                           + "<fecharegistro>"+fereg+"</fecharegistro>\n"
                           + "<horaregistro>"+hourreg+"</horaregistro>\n" 
                          + "<cancelado>"+this.getCancelado().toString()+"</cancelado>\n"
                          + "<anulado>"+this.getAnulado().toString()+"</anulado>\n"
                           + "<idnota>"+this.getFkidnotapedido().getId()+"</idnota>\n"
                           + "<iduseranulo>"+this.getIdusuarioanulo().toString()+"</iduseranulo>\n"
                           + "<iduserentrega>"+this.getIdusuarioentrega().toString()+"</iduserentrega>\n"
                           + "<iduserexpidio>"+this.getIdusuarioexpidio().toString()+"</iduserexpidio>\n" +
                           "<idusuariocancelo>"+this.getIdusuariocancelo().toString()+"</idusuariocancelo>\n" +
                           "<recargo>"+this.getRecargo().toString()+"</recargo>\n" +
                           "<totalapagar>"+this.getTotalapagar().toString()+"</totalapagar>\n" +
                           "<porcrecargo>"+this.getPorcrecargo().toString()+"</porcrecargo>\n" +
                           "<porcentajedescuento>"+this.getPorcentajedesc().toString()+"</porcentajedescuento>\n" +
                           "<descuento>"+this.getDescuento().toString()+"</descuento>\n" +
                           "<accion>"+StringEscapeUtils.escapeXml(this.getAccion())+"</accion>\n"
                           + "<saldo>"+this.getSaldo().toString()+"</saldo>\n"
                           + "<total>"+this.getTotal().toString()+"</total>\n" +
                   "</item>\n";
        } catch (Exception e) {
            e.getMessage();
        }finally{
                    return item;
        }
    }
}
