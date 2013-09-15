/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang3.StringEscapeUtils;
/**
 * A Entity Calles
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name="CALLES")
public class Calles implements Serializable {
    private static final long serialVersionUID = 1L;   
    @TableGenerator(name="CalleIdGen", table="ID_GEN_CALLE",
    pkColumnName="FNAME",pkColumnValue="Calles" , valueColumnName="FKEY",
    allocationSize=1)
    @Id
    @GeneratedValue(generator="CalleIdGen",strategy=GenerationType.TABLE)
    @Column(name="ID_CALLE")
    private Long id;
    @Column(length = 100,name="DESCRIPCION",nullable = false,unique=true)
    private String descripcion;
    @OneToMany(mappedBy = "idcalle")
    private List<Domicilios> domicilioss;
    public Calles(){}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public List<Domicilios> getDomicilioss() {
        return domicilioss;
    }
    public void setDomicilioss(List<Domicilios> domicilioss) {
        this.domicilioss = domicilioss;
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
        if (!(object instanceof Calles)) {
            return false;
        }
        Calles other = (Calles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.melani.entity.Calles[id=" + id + "]";
    }
     public String toXML(){
        String item = "<item>\n" +
                "<id>"+this.getId()+"</id>\n" +
                "<nombre>"+StringEscapeUtils.escapeXml(this.getDescripcion())+"</nombre>\n" +
                "</item>\n";
        return item;
    }
}
