/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * A Entity Productos
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "PRODUCTOS")
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p"),
    @NamedQuery(name = "Productos.findBySid", query = "SELECT p FROM Productos p WHERE p.sid = :sid"),
    @NamedQuery(name = "Productos.findByCantidadInicial", query = "SELECT p FROM Productos p WHERE p.cantidadInicial = :cantidadInicial"),
    @NamedQuery(name = "Productos.findByCantidadDisponible", query = "SELECT p FROM Productos p WHERE p.cantidadDisponible = :cantidadDisponible"),
    @NamedQuery(name = "Productos.findByPrecioUnitario", query = "SELECT p FROM Productos p WHERE p.precioUnitario = :precioUnitario"),
    @NamedQuery(name = "Productos.findByFecha", query = "SELECT p FROM Productos p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Productos.findByDescripcion", query = "SELECT p FROM Productos p WHERE p.descripcion = :descripcion")})
public class Productos implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableGenerator(name="ProductoIdGen", table="ID_GEN_PROD",
    pkColumnName="FNAME",pkColumnValue="Productos" , valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="ProductoIdGen",strategy=GenerationType.TABLE)
    @Column(name = "SID")
    private Long sid;
    @Column(name = "DESCRIPCION",length=100,nullable=false,unique=true)
    private String descripcion;
    @Column(name="CODPRODUCTO",length=100,unique=true,nullable=true)
    private String codproducto;
    @Column(name = "PRECIOUNITARIO",scale=2,precision=12)
    private BigDecimal precioUnitario;
    @Column(name = "CANTIDADINICIAL",precision=10)
    private BigInteger cantidadInicial;
    @Column(name = "CANTIDADDISPONIBLE",precision=10)
    private BigInteger cantidadDisponible;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Lob @Basic(fetch=FetchType.EAGER)
    @Column(name = "IMG")
    private byte[] img;
    @OneToMany(mappedBy = "productos",cascade={CascadeType.ALL})
    private List<ExistenciasProductos> existenciasProductoss;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productos")
    private List<Detallesnotadepedido> detallesnotadepedidoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productos")
    private List<Detallespresupuesto> detallepresupuestosList;

    public Productos(){}
    
    public Productos(Long sid) {
        this.sid = sid;
    }
    public Long getSid() {
        return sid;
    }
    public void setSid(Long sid) {
        this.sid = sid;
    }

    public BigInteger getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(BigInteger cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public BigInteger getCantidadInicial() {
        return cantidadInicial;
    }

    public void setCantidadInicial(BigInteger cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ExistenciasProductos> getExistenciasProductoss() {
        return existenciasProductoss;
    }

    public String getCodproducto() {
        return codproducto;
    }

    public void setCodproducto(String codproducto) {
        this.codproducto = codproducto;
    }
    
    public void setExistenciasProductoss(List<ExistenciasProductos> existenciasProductoss) {
        this.existenciasProductoss = existenciasProductoss;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public List<Detallespresupuesto> getDetallepresupuestosList() {
        return detallepresupuestosList;
    }

    public void setDetallepresupuestosList(List<Detallespresupuesto> detallepresupuestosList) {
        this.detallepresupuestosList = detallepresupuestosList;
    }

    public List<Detallesnotadepedido> getDetallesnotadepedidoList() {
        return detallesnotadepedidoList;
    }

    public void setDetallesnotadepedidoList(List<Detallesnotadepedido> detallesnotadepedidoList) {
        this.detallesnotadepedidoList = detallesnotadepedidoList;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sid != null ? sid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.sid == null && other.sid != null) || (this.sid != null && !this.sid.equals(other.sid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.melani.entity.Productos[id=" + sid + "]";
    }
    public String toXML(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String item="<producto>\n" +
                "<id>" +this.getSid()+"</id>\n" +
                "<descripcion>"+StringEscapeUtils.escapeXml(this.getDescripcion())+"</descripcion>\n" +
                "<cantidadinicial>"+this.getCantidadInicial()+"</cantidadinicial>\n" +
                "<cantidaddisponible>"+this.getCantidadDisponible()+"</cantidaddisponible>\n" +
                "<fechacarga>"+sdf.format(this.getFecha())+"</fechacarga>\n" +
                "<preciovigente>"+this.getPrecioUnitario().toString()+"</preciovigente>";
                item+="<existencias>\n";
                    if(this.getExistenciasProductoss().isEmpty())
                        item+="</existencias>\n";
                    else{
                        List<ExistenciasProductos>lista = this.getExistenciasProductoss();
                        for (Iterator<ExistenciasProductos> it = lista.iterator(); it.hasNext();) {
                            ExistenciasProductos existenciasProductos = it.next();
                            item+=existenciasProductos.toXML();

                        }
                        item+="</existencias>\n";

                    }
        item+=        "</producto>";
    return item;
    }

}
