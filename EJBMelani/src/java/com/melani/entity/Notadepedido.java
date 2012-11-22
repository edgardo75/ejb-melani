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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Edgardo
 */
@Entity
@Table(name = "NOTADEPEDIDO")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Notadepedido.findAll",
query = "SELECT n FROM Notadepedido n"), @NamedQuery(name = "Notadepedido.findById",
        query = "SELECT n FROM Notadepedido n WHERE n.id = :id"), @NamedQuery(name = "Notadepedido.findByObservaciones",
        query = "SELECT n FROM Notadepedido n WHERE n.observaciones = :observaciones"), @NamedQuery(name = "Notadepedido.findByPendiente",
        query = "SELECT n FROM Notadepedido n WHERE n.pendiente = :pendiente"), @NamedQuery(name = "Notadepedido.findByAnticipo",
        query = "SELECT n FROM Notadepedido n WHERE n.anticipo = :anticipo"), @NamedQuery(name = "Notadepedido.findByMontoiva",
        query = "SELECT n FROM Notadepedido n WHERE n.montoiva = :montoiva"), @NamedQuery(name = "Notadepedido.findByHoracompra",
        query = "SELECT n FROM Notadepedido n WHERE n.horacompra = :horacompra"), @NamedQuery(name = "Notadepedido.findByRecargo",
        query = "SELECT n FROM Notadepedido n WHERE n.recargo = :recargo"), @NamedQuery(name = "Notadepedido.findByIdUsuarioExpidioNota",
        query = "SELECT n FROM Notadepedido n WHERE n.idUsuarioExpidioNota = :idUsuarioExpidioNota"), @NamedQuery(name = "Notadepedido.findByIdusuarioAnulado",
        query = "SELECT n FROM Notadepedido n WHERE n.idusuarioAnulado = :idusuarioAnulado"), @NamedQuery(name = "Notadepedido.findByFechaAnulado",
        query = "SELECT n FROM Notadepedido n WHERE n.fechaAnulado = :fechaAnulado"), @NamedQuery(name = "Notadepedido.findByTotal",
        query = "SELECT n FROM Notadepedido n WHERE n.total = :total"), @NamedQuery(name = "Notadepedido.findByAnulado",
        query = "SELECT n FROM Notadepedido n WHERE n.anulado = :anulado"), @NamedQuery(name = "Notadepedido.findByNumerodecupon",
        query = "SELECT n FROM Notadepedido n WHERE n.numerodecupon = :numerodecupon"), @NamedQuery(name = "Notadepedido.findByEnefectivo",
        query = "SELECT n FROM Notadepedido n WHERE n.enefectivo = :enefectivo"), @NamedQuery(name = "Notadepedido.findByEntregado",
        query = "SELECT n FROM Notadepedido n WHERE n.entregado = :entregado"), @NamedQuery(name = "Notadepedido.findByFechaentrega",
        query = "SELECT n FROM Notadepedido n WHERE n.fechaentrega = :fechaentrega"), @NamedQuery(name = "Notadepedido.findByIdusuarioEntregado",
        query = "SELECT n FROM Notadepedido n WHERE n.idusuarioEntregado = :idusuarioEntregado"), @NamedQuery(name = "Notadepedido.findByStockfuturo",
        query = "SELECT n FROM Notadepedido n WHERE n.stockfuturo = :stockfuturo"), @NamedQuery(name = "Notadepedido.findBySaldo",
        query = "SELECT n FROM Notadepedido n WHERE n.saldo = :saldo"), @NamedQuery(name = "Notadepedido.findByFechadecompra",
        query = "SELECT n FROM Notadepedido n WHERE n.fechadecompra = :fechadecompra"), @NamedQuery(name = "Notadepedido.findByCancelado",
        query = "SELECT n FROM Notadepedido n WHERE n.cancelado = :cancelado"), @NamedQuery(name = "Notadepedido.findByFecancelado",
        query = "SELECT n FROM Notadepedido n WHERE n.fecancelado = :fecancelado"), @NamedQuery(name = "Notadepedido.findByIdUsuarioCancelo",
        query = "SELECT n FROM Notadepedido n WHERE n.idUsuarioCancelo = :idUsuarioCancelo"), @NamedQuery(name = "Notadepedido.findByDescuentoNota",
        query = "SELECT n FROM Notadepedido n WHERE n.descuentoNota = :descuentoNota"), @NamedQuery(name = "Notadepedido.findByMontototalapagar",
        query = "SELECT n FROM Notadepedido n WHERE n.montototalapagar = :montototalapagar"), @NamedQuery(name = "Notadepedido.findByPorcdesctotal",
        query = "SELECT n FROM Notadepedido n WHERE n.porcdesctotal = :porcdesctotal"), @NamedQuery(name = "Notadepedido.findByPorcrecargo",
        query = "SELECT n FROM Notadepedido n WHERE n.porcrecargo = :porcrecargo")})
public class Notadepedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id        
    @TableGenerator(name="NoPeIdGen", table="ID_GEN_NOTAP",
    pkColumnName="FNAME",pkColumnValue="Notadepedido" , valueColumnName="FKEY",
    allocationSize=1) 
    @GeneratedValue(strategy=GenerationType.TABLE,generator="NoPeIdGen")
    private Long id;
    @Column(name = "ANTICIPO",precision=15,scale=3)
    private BigDecimal anticipo;
    @Column(name = "SALDO",precision=15,scale=3)
    private BigDecimal saldo;
    @Column(name = "HORACOMPRA")
    @Temporal(TemporalType.TIME)
    private Date horacompra;
    @Column(name = "ENTREGADO")
    private Character entregado;
    @Column(name = "ID_USUARIO_EXPIDIO_NOTA")
    private Integer idUsuarioExpidioNota;
    @Column(name = "STOCKFUTURO")
    private Integer stockfuturo;
    @Column(name = "FECHA_ANULADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnulado;
    @Column(name = "IDUSUARIO_ENTREGADO")
    private Integer idusuarioEntregado;
    @Column(name = "ANULADO")
    private Character anulado;
    @Column(name = "FECHADECOMPRA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadecompra;
    @Column(name = "OBSERVACIONES",length=32000)
    private String observaciones;
    @Column(name = "PENDIENTE")
    private Character pendiente;
    @Column(name = "MONTOIVA",precision=12,scale=2)
    private BigDecimal montoiva;
    @Column(name = "RECARGO",precision=15,scale=3)
    private BigDecimal recargo;
    @Column(name = "IDUSUARIO_ANULADO")
    private Integer idusuarioAnulado;
    @Column(name = "TOTAL",precision=15,scale=3)
    private BigDecimal total;    
    @Column(name = "NUMERODECUPON",length=20)
    private String numerodecupon;
    @Column(name = "ENEFECTIVO")
    private Character enefectivo;
    @Column(name="FECHAENTREGA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaentrega;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "notadepedido")
    private List<Detallesnotadepedido> detallesnotadepedidoList;
    @OneToMany(cascade= CascadeType.ALL,mappedBy = "fkidnotapedido")
    private List<Historiconotapedido> historiconotapedidoList;
    
    @ManyToOne
    private TarjetasCreditoDebito idTarjetaFk;
    @JoinColumn(name = "FK_IDCLIENTE", referencedColumnName = "ID_PERSONA")
    @ManyToOne
    private Personas fkIdcliente;
    @JoinColumn(name = "FKIDPORCENTAJENOTA_ID", referencedColumnName = "ID_PORCENTAJES")
    @ManyToOne
    private Porcentajes fkidporcentajenotaId;

    @Column(name="CANCELADO",length=1)
    private Character cancelado;
    @Column(name="FECANCELADO")
    @Temporal(TemporalType.DATE)
    private Date fecancelado;
    @Column(name="ID_USUARIO_CANCELO")
    private Integer idUsuarioCancelo;
    @Column(name="DESCUENTO_NOTA",precision=15,scale=3)
    private BigDecimal descuentoNota;
    @Column(name = "MONTOTOTALAPAGAR",precision=15,scale=3)
    private BigDecimal montototalapagar;
    @Column(name = "PORCDESCTOTAL",precision=12,scale=2)
    private BigDecimal porcdesctotal;
    
    @Column(name = "PORCRECARGO",precision=12,scale=2)
    private BigDecimal porcrecargo;


    public Notadepedido() {
    }

    public Notadepedido(Long id) {
        this.id = id;
    }

   

    
   

    public BigDecimal getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(BigDecimal anticipo) {
        this.anticipo = anticipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Date getHoracompra() {
        return horacompra;
    }

    public void setHoracompra(Date horacompra) {
        this.horacompra = horacompra;
    }

    public Character getEntregado() {
        return entregado;
    }

    public void setEntregado(Character entregado) {
        this.entregado = entregado;
    }

    public Integer getIdUsuarioExpidioNota() {
        return idUsuarioExpidioNota;
    }

    public void setIdUsuarioExpidioNota(Integer idUsuarioExpidioNota) {
        this.idUsuarioExpidioNota = idUsuarioExpidioNota;
    }

    public Integer getStockfuturo() {
        return stockfuturo;
    }

    public void setStockfuturo(Integer stockfuturo) {
        this.stockfuturo = stockfuturo;
    }

    public Date getFechaAnulado() {
        return fechaAnulado;
    }

    public BigDecimal getDescuentonota() {
        return descuentoNota;
    }

    public void setDescuentonota(BigDecimal descuentonota) {
        this.descuentoNota = descuentonota;
    }

   

    
    public void setFechaAnulado(Date fechaAnulado) {
        this.fechaAnulado = fechaAnulado;
    }

    public Integer getIdusuarioEntregado() {
        return idusuarioEntregado;
    }

    public void setIdusuarioEntregado(Integer idusuarioEntregado) {
        this.idusuarioEntregado = idusuarioEntregado;
    }

    public Character getAnulado() {
        return anulado;
    }

    public void setAnulado(Character anulado) {
        this.anulado = anulado;
    }

  

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Character getPendiente() {
        return pendiente;
    }

    public void setPendiente(Character pendiente) {
        this.pendiente = pendiente;
    }

    public BigDecimal getMontoiva() {
        return montoiva;
    }

    public void setMontoiva(BigDecimal montoiva) {
        this.montoiva = montoiva;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public Integer getIdusuarioAnulado() {
        return idusuarioAnulado;
    }

    public void setIdusuarioAnulado(Integer idusuarioAnulado) {
        this.idusuarioAnulado = idusuarioAnulado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Porcentajes getFkidporcentajenotaId() {
        return fkidporcentajenotaId;
    }

    public void setFkidporcentajenotaId(Porcentajes fkidporcentajenotaId) {
        this.fkidporcentajenotaId = fkidporcentajenotaId;
    }

    public String getNumerodecupon() {
        return numerodecupon;
    }

    public void setNumerodecupon(String numerodecupon) {
        this.numerodecupon = numerodecupon;
    }

    public Character getEnefectivo() {
        return enefectivo;
    }

    public void setEnefectivo(Character enefectivo) {
        this.enefectivo = enefectivo;
    }

    @XmlTransient
    public List<Detallesnotadepedido> getDetallesnotadepedidoList() {
        return detallesnotadepedidoList;
    }

    public void setDetallesnotadepedidoList(List<Detallesnotadepedido> detallesnotadepedidoList) {
        this.detallesnotadepedidoList = detallesnotadepedidoList;
    }

    

    public Date getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Date fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    

    @XmlTransient
    public List<Historiconotapedido> getHistoriconotapedidoList() {
        return historiconotapedidoList;
    }

    public void setHistoriconotapedidoList(List<Historiconotapedido> historiconotapedidoList) {
        this.historiconotapedidoList = historiconotapedidoList;
    }
    

    public TarjetasCreditoDebito getIdTarjetaFk() {
        return idTarjetaFk;
    }

    public void setIdTarjetaFk(TarjetasCreditoDebito idTarjetaFk) {
        this.idTarjetaFk = idTarjetaFk;
    }

    public Personas getFkIdcliente() {
        return fkIdcliente;
    }

    public void setFkIdcliente(Personas fkIdcliente) {
        this.fkIdcliente = fkIdcliente;
    }

    public Date getFechadecompra() {
        return fechadecompra;
    }

    public void setFechadecompra(Date fechadecompra) {
        this.fechadecompra = fechadecompra;
    }

    public Character getCancelado() {
        return cancelado;
    }

    public void setCancelado(Character cancelado) {
        this.cancelado = cancelado;
    }

    public Date getFecancelado() {
        return fecancelado;
    }

    public void setFecancelado(Date fecancelado) {
        this.fecancelado = fecancelado;
    }

    public Integer getIdusuariocancelo() {
        return idUsuarioCancelo;
    }

    public void setIdusuariocancelo(Integer idusuariocancelo) {
        this.idUsuarioCancelo = idusuariocancelo;
    }

    public BigDecimal getMontototalapagar() {
        return montototalapagar;
    }

    public void setMontototalapagar(BigDecimal montototalapagar) {
        this.montototalapagar = montototalapagar;
    }

    public BigDecimal getPorcdesctotal() {
        return porcdesctotal;
    }

    public void setPorcdesctotal(BigDecimal porcdesctotal) {
        this.porcdesctotal = porcdesctotal;
    }

    public BigDecimal getPorcrecargo() {
        return porcrecargo;
    }

    public void setPorcrecargo(BigDecimal porcrecargo) {
        this.porcrecargo = porcrecargo;
    }

  


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notadepedido)) {
            return false;
        }
        Notadepedido other = (Notadepedido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.melani.entity.Notadepedido[ id=" + id + " ]";
    }
    public String toXML(){
        
        //---------------------------------------------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfh = new SimpleDateFormat("HH:mm:ss");
        //---------------------------------------------------------------------

        String fechanulado="";
        if (this.getFechaAnulado()!=null)
            fechanulado=sdf.format(this.getFechaAnulado());
        String feentrega="";
       if (this.getFechaentrega()!=null)
           feentrega =sdf.format(this.getFechaentrega());
        String fecompra = "";
       if(this.getFechadecompra()!=null)
           fecompra =  sdf.format(this.getFechadecompra());
        String hocompra = "";
        if (this.getHoracompra()!=null)
            hocompra=sdfh.format(this.getHoracompra());
        String fecancel="";
        if(this.getFecancelado()!=null)
            fecancel=sdf.format(this.getFecancelado());
        //--------------------------------------------------------------------

        String item ="";
       
            
            
            item+="<item>\n"

                + "<id>"+this.getId()+"</id>\n"

                + "<numerocupon>"+this.getNumerodecupon().toString()+"</numerocupon>\n"               
                + "<observaciones>"+StringEscapeUtils.escapeXml(this.getObservaciones())+"</observaciones>\n"
                + "<anticipo>"+this.getAnticipo()+"</anticipo>\n"
                + "<anulado>"+this.getAnulado()+"</anulado>\n"
                + "<cancelado>"+this.getCancelado()+"</cancelado>\n"
                + "<efectivo>"+this.getEnefectivo()+"</efectivo>\n"
                + "<entregado>"+this.getEntregado()+"</entregado>\n"
                + "<fechaanulado>"+fechanulado+"</fechaanulado>\n" +
                "<descuentonota>" +this.getDescuentonota()+"</descuentonota>\n"+
                "<fecancel>"+fecancel+"</fecancel>\n"
                + "<fechacompra>"+fecompra+"</fechacompra>\n"
                    + "<fechaentrega>"+feentrega+"</fechaentrega>\n"
                + "<cliente>\n"
                    + "<id>"+this.getFkIdcliente().getIdPersona()+"</id>\n"
                    + "<apellidoynombre>"+this.getFkIdcliente().getApellido()+" "+this.getFkIdcliente().getNombre()+ "</apellidoynombre>\n"
                + "</cliente>\n"
                + "<idporcentajes>"+this.getFkidporcentajenotaId().getIdPorcentajes()+"</idporcentajes>\n"
                + "<horacompra>"+hocompra+"</horacompra>\n"
                + "<tarjetadecredito>"+this.getIdTarjetaFk().getDescripcion()+"</tarjetadecredito>\n"
                + "<usuarioexpidionota>"+this.getIdUsuarioExpidioNota()+"</usuarioexpidionota>\n"
                + "<usuarioanulonota>"+this.getIdusuarioAnulado()+"</usuarioanulonota>\n"
                + "<usuarioentregonota>"+this.getIdusuarioEntregado()+"</usuarioentregonota>\n" +
                "<usuariocancelonota>"+this.getIdusuariocancelo()+"</usuariocancelonota>\n"
                + "<montoiva>"+this.getMontoiva()+"</montoiva>\n"
                + "<stockfuturo>"+this.getStockfuturo()+"</stockfuturo>\n"
                + "<pendiente>"+this.getPendiente()+"</pendiente>\n"
                + "<recargo>"+this.getRecargo()+"</recargo>\n"
                + "<total>"+this.getTotal()+"</total>\n"
                + "<saldo>"+this.getSaldo()+"</saldo>\n" +
                "<montototalapagar>"+this.getMontototalapagar().toString()+"</montototalapagar>\n" +
                "<porcentajedesctotal>"+this.getPorcdesctotal().toString()+"</porcentajedesctotal>\n" +
                "<recargototal>"+this.getRecargo().toString()+"</recargototal>\n" +
                "<porcrecargo>"+this.getPorcrecargo().toString()+"</porcrecargo>\n"
                + "<detallenota>";
                    if(this.getDetallesnotadepedidoList().isEmpty())
                        item+="</detallenota>\n";
                    else{

                        List<Detallesnotadepedido>lista = this.getDetallesnotadepedidoList();
                        for (Iterator<Detallesnotadepedido> it = lista.iterator(); it.hasNext();) {
                            Detallesnotadepedido detallesnotadepedido = it.next();
                            item+="<itemdetalle>\n"
                                    + "<producto>"
                                    + "<descripcion>"+StringEscapeUtils.escapeXml(detallesnotadepedido.getProductos().getDescripcion())+"</descripcion>\n"
                                    + "</producto>\n"
                                    + "<cantidad>"+detallesnotadepedido.getCantidad()+"</cantidad>\n"
                                    + "<precio>"+detallesnotadepedido.getPrecio()+"</precio>\n" +
                                    "<preciocondescuento>"+detallesnotadepedido.getPreciocondescuento()+"</preciocondescuento>\n" +
                                    "<descuento>"+detallesnotadepedido.getDescuento()+"</descuento>\n"
                                    + "</itemdetalle>\n";

                        }
                    
                    
                        
                        item+="</detallenota>\n";
                    
                    
                    }
                    item+="<detallehistorico>\n";
                    if(this.getHistoriconotapedidoList().isEmpty())
                        item+="</detallehistorico>\n";
                    else{
                     Iterator iter = this.getHistoriconotapedidoList().iterator();
                     
                        while(iter.hasNext()){
                            Historiconotapedido historico = (Historiconotapedido) iter.next();
                            item+=historico.toXML();
                        
                        }
                        item+="</detallehistorico>\n";
                    
                    }
                 item+="</item>\n";   
            
      
        
        return item;
    }
}
