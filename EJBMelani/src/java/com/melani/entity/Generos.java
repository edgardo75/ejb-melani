/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * A Entity Generos
 *@version
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "GENEROS")
@NamedQueries({
    @NamedQuery(name = "Generos.findAll", query = "SELECT g FROM Generos g"),
    @NamedQuery(name = "Generos.findByIdGenero", query = "SELECT g FROM Generos g WHERE g.idGenero = :idGenero"),
    @NamedQuery(name = "Generos.findByDescripcion", query = "SELECT g FROM Generos g WHERE g.descripcion = :descripcion")})
public class Generos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_GENERO")
    private Short idGenero;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(mappedBy = "generos",fetch=FetchType.LAZY)
    private Collection<Personas> personasCollection;
    public Generos() {
    }
    public Generos(Short idGenero) {
        this.idGenero = idGenero;
    }
    public Short getIdGenero() {
        return idGenero;
    }
    public void setIdGenero(Short idGenero) {
        this.idGenero = idGenero;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Collection<Personas> getPersonasCollection() {
        return personasCollection;
    }
    public void setPersonasCollection(Collection<Personas> personasCollection) {
        this.personasCollection = personasCollection;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGenero != null ? idGenero.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Generos)) {
            return false;
        }
        Generos other = (Generos) object;
        if ((this.idGenero == null && other.idGenero != null) || (this.idGenero != null && !this.idGenero.equals(other.idGenero))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "entity.Generos[idGenero=" + idGenero + "]";
    }
}
