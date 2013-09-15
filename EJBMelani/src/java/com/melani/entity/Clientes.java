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
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
/**
 * A Entity Clientes
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@DiscriminatorValue("CLI")
public class Clientes extends Personas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(precision = 15,scale=3,name="TOTALCOMPRAS")
    private BigDecimal totalCompras;
    @Column(precision = 15,name = "TOTALPUNTOS")
    private BigInteger totalEnPuntos;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCarga;
    @OneToMany(mappedBy = "fkIdcliente")
    private List<Notadepedido> notadepedidoList;
    public Clientes(){}
    public Date getFechaCarga() {
        return fechaCarga;
    }
    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
    public BigDecimal getTotalCompras() {
        return totalCompras;
    }
    public void setTotalCompras(BigDecimal totalCompras) {
        this.totalCompras = totalCompras;
    }
    public BigInteger getTotalEnPuntos() {
        return totalEnPuntos;
    }
    public void setTotalEnPuntos(BigInteger totalEnPuntos) {
        this.totalEnPuntos = totalEnPuntos;
    }
     public List<Notadepedido> getNotadepedidoList() {
        return notadepedidoList;
    }
    public void setNotadepedidoList(List<Notadepedido> notadepedidoList) {
        this.notadepedidoList = notadepedidoList;
    }
    public String toXMLCLI(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String xml=
                "<totalcompras>"+this.getTotalCompras()+"</totalcompras>\n" +
                "<totalpuntos>"+this.getTotalEnPuntos()+"</totalpuntos>\n" +
                "<fechacarga>"+sdf.format(this.getFechaCarga())+"</fechacarga>\n";
                xml+="<notapedidolist>\n";
                if(this.getNotadepedidoList().isEmpty())
                    xml+="</notapedidolist>\n";
                else{
                    List<Notadepedido>lista = this.getNotadepedidoList();
                    for (Iterator<Notadepedido> it = lista.iterator(); it.hasNext();) {
                        Notadepedido notadepedido = it.next();
                        xml+=notadepedido.toXML();
                    }
                    xml+="</notapedidolist>\n";
                }
        return xml;
    }
}





















