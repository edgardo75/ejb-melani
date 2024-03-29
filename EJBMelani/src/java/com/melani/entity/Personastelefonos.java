/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
 * A Entity Persoastelefonos
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "PERSONASTELEFONOS")
@NamedQueries({
    @NamedQuery(name = "Personastelefonos.findAll", query = "SELECT p FROM Personastelefonos p"),
    @NamedQuery(name = "Personastelefonos.findByDetalles", query = "SELECT p FROM Personastelefonos p WHERE p.detalles = :detalles"),
    @NamedQuery(name = "Personastelefonos.findByEstado", query = "SELECT p FROM Personastelefonos p WHERE p.estado = :estado"),
    @NamedQuery(name = "Personastelefonos.findByPrefijo", query = "SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.prefijo = :prefijo"),
    @NamedQuery(name = "Personastelefonos.findByNumerotel", query = "SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.numerotel = :numerotel"),
    @NamedQuery(name = "Personastelefonos.findByIdPersona", query = "SELECT p FROM Personastelefonos p WHERE p.personastelefonosPK.idPersona = :idPersona")})
public class Personastelefonos implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PersonastelefonosPK personastelefonosPK;
    @Column(name = "ESTADO", length = 50)
    private String estado;
    @Column(name = "DETALLES", length = 60)
    private String detalles;
    @JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID_PERSONA", nullable = false, insertable = false, updatable = false)//@ManyToOne(optional = false,cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    @ManyToOne(optional = false,cascade={CascadeType.PERSIST},fetch=FetchType.EAGER)
    private Personas idPersona;
    @JoinColumns({
        @JoinColumn(name = "NUMEROTEL", referencedColumnName = "NUMERO", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "PREFIJO", referencedColumnName = "ID_PREFIJO", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false,cascade={CascadeType.PERSIST},fetch=FetchType.EAGER)
    private Telefonos telefonos;
    public Personastelefonos() {
    }
    public Personastelefonos(PersonastelefonosPK personastelefonosPK) {
        this.personastelefonosPK = personastelefonosPK;
    }
    public Personastelefonos(int numerotel, long prefijo, long idPersona) {
        this.personastelefonosPK = new PersonastelefonosPK(numerotel, prefijo, idPersona);
    }
    public PersonastelefonosPK getPersonastelefonosPK() {
        return personastelefonosPK;
    }
    public void setPersonastelefonosPK(PersonastelefonosPK personastelefonosPK) {
        this.personastelefonosPK = personastelefonosPK;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getDetalles() {
        return detalles;
    }
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
    public Personas getIdPersona() {
        return idPersona;
    }
    public void setIdPersona(Personas idPersona) {
        this.idPersona = idPersona;
    }
    public Telefonos getTelefonos() {
        return telefonos;
    }
    public void setTelefonos(Telefonos telefonos) {
        this.telefonos = telefonos;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personastelefonosPK != null ? personastelefonosPK.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personastelefonos)) {
            return false;
        }
        Personastelefonos other = (Personastelefonos) object;
        if ((this.personastelefonosPK == null && other.personastelefonosPK != null) || (this.personastelefonosPK != null && !this.personastelefonosPK.equals(other.personastelefonosPK))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.tarjetadata.dao.ejb.Personas.Personastelefonos[PersonastelefonosPK=" + personastelefonosPK + "]";
    }
    public String toXML(){
            String item=telefonos.toXML();
                return item;
    }
}
