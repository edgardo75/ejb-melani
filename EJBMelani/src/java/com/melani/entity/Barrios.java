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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * A Entity Barrios
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name="BARRIOS")
@NamedQueries({
    @NamedQuery(name = "Barrios.findAll", query = "SELECT b FROM Barrios b"),
    @NamedQuery(name = "Barrios.findByIdBarrio", query = "SELECT b FROM Barrios b WHERE b.id = :id"),
    @NamedQuery(name = "Barrios.findByDescripcion", query = "SELECT b FROM Barrios b WHERE b.descripcion = :descripcion")})
public class Barrios implements Serializable {
        
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ID_BARRIO")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="BarrioIdGen")
    @TableGenerator(name="BarrioIdGen", table="ID_GEN_BARRIO",
    pkColumnName="FNAME",pkColumnValue="Barrios", valueColumnName="FKEY",
    allocationSize=1)
    private Long id;
    @Column(length = 100,name="DESCRIPCION",nullable = false,unique=true)
    private String descripcion;
    @OneToMany(mappedBy = "idbarrio")
    private List<Domicilios> domicilioss;
    
    public Barrios(){}

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
        if (!(object instanceof Barrios)) {
            return false;
        }
        Barrios other = (Barrios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.melani.entity.Barrios[id=" + id + "]";
    }



     public String toXML(){
       String item = "<item>\n" +
                "<id>"+this.getId()+"</id>\n" +
                "<nombre>"+StringEscapeUtils.escapeXml(this.getDescripcion())+"</nombre>\n" +
                "</item>\n";
        return item;

    }

}
