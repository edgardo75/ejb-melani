/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.List;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 *
 * @author Edgardo
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarjetasCreditoDebito.findAll", query = "SELECT t FROM TarjetasCreditoDebito t"),
    @NamedQuery(name = "TarjetasCreditoDebito.findByIdtarjeta", query = "SELECT t FROM TarjetasCreditoDebito t WHERE t.idtarjeta = :idtarjeta"),
    @NamedQuery(name = "TarjetasCreditoDebito.findByDescripcion", query = "SELECT t FROM TarjetasCreditoDebito t WHERE t.descripcion = :descripcion")})
public class TarjetasCreditoDebito implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="IDTARJETA")
    private Integer idtarjeta;
    @Column(name="DESCRIPCION",length=30)
    private String descripcion;
    @OneToMany(mappedBy = "idTarjetaFk")
    private List<Notadepedido> notadepedidoList;
    @OneToMany(cascade= CascadeType.ALL,mappedBy="idTarjetaFk",fetch= FetchType.LAZY)
    private List<EntradasySalidasCaja>entradasysalidascajaList;
public TarjetasCreditoDebito() {
    }
    public TarjetasCreditoDebito(Integer idtarjeta) {
        this.idtarjeta = idtarjeta;
    }
    public Integer getIdtarjeta() {
        return idtarjeta;
    }
    public void setIdtarjeta(Integer idtarjeta) {
        this.idtarjeta = idtarjeta;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @XmlTransient
    public List<Notadepedido> getNotadepedidoList() {
        return notadepedidoList;
    }
    public void setNotadepedidoList(List<Notadepedido> notadepedidoList) {
        this.notadepedidoList = notadepedidoList;
    }
    @XmlTransient
    public List<EntradasySalidasCaja> getEntradasysalidascajaList() {
        return entradasysalidascajaList;
    }
    public void setEntradasysalidascajaList(List<EntradasySalidasCaja> entradasysalidascajaList) {
        this.entradasysalidascajaList = entradasysalidascajaList;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtarjeta != null ? idtarjeta.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TarjetasCreditoDebito)) {
            return false;
        }
        TarjetasCreditoDebito other = (TarjetasCreditoDebito) object;
        if ((this.idtarjeta == null && other.idtarjeta != null) || (this.idtarjeta != null && !this.idtarjeta.equals(other.idtarjeta))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.melani.entity.TarjetasCreditoDebito[ idtarjeta=" + idtarjeta + " ]";
    }
    public String toXML(){
        String item ="<item>\n" +
                "<id>"+this.getIdtarjeta()+"</id>\n" +
                "<descripcion>"+StringEscapeUtils.escapeXml(this.getDescripcion())+"</descripcion>\n" +
                "</item>\n";
    return item;
    }
}