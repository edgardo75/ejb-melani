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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * A Entity TiposDocumentos
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name="TIPOSDOCUMENTO")
@NamedQueries({
    @NamedQuery(name = "Tiposdocumento.findAll", query = "SELECT t FROM Tiposdocumento t"),
    @NamedQuery(name = "Tiposdocumento.findById", query = "SELECT t FROM Tiposdocumento t WHERE t.id = :id"),
    @NamedQuery(name = "Tiposdocumento.findByDescripcion", query = "SELECT t FROM Tiposdocumento t WHERE t.descripcion = :descripcion")})
public class Tiposdocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ID",nullable=false,updatable=false)
    private Short id;
    @Column(length=20,name="DESCRIPCION")
    protected String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipodocumento",fetch=FetchType.LAZY)
    private List<Personas> personasList;
    public Tiposdocumento(){}
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Short getId() {
        return id;
    }
    public void setId(Short id) {
        this.id = id;
    }
    public List<Personas> getPersonasList() {
        return personasList;
    }
    public void setPersonasList(List<Personas> personasList) {
        this.personasList = personasList;
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
        if (!(object instanceof Tiposdocumento)) {
            return false;
        }
        Tiposdocumento other = (Tiposdocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.melani.entity.TiposDocumento[id=" + id + "]";
    }
}
