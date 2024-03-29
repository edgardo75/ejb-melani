/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
 * A Entity EmpresaTelefonia
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@Table(name = "EMPRESATELEFONIA")
@NamedQueries({
    @NamedQuery(name = "EmpresaTelefonia.findAll", query = "SELECT e FROM EmpresaTelefonia e"),
    @NamedQuery(name = "EmpresaTelefonia.findByIdEmpTelefonia", query = "SELECT e FROM EmpresaTelefonia e WHERE e.idEmpTelefonia = :idEmpTelefonia"),
    @NamedQuery(name = "EmpresaTelefonia.findByNombre", query = "SELECT e FROM EmpresaTelefonia e WHERE e.nombre = :nombre")})
public class EmpresaTelefonia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_EMP_TELEFONIA", nullable = false,updatable=false)
    private Short idEmpTelefonia;
    @Column(name = "NOMBRE", length = 30,unique=true)
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresatelefonia",fetch=FetchType.LAZY)
    private List<Telefonos> telefonosList;
    public EmpresaTelefonia() {
    }
    public EmpresaTelefonia(Short idEmpTelefonia) {
        this.idEmpTelefonia = idEmpTelefonia;
    }
    public Short getidEmpTelefonia() {
        return idEmpTelefonia;
    }
    public void setidEmpTelefonia(Short idEmpTelefonia) {
        this.idEmpTelefonia = idEmpTelefonia;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<Telefonos> getTelefonosList() {
        return telefonosList;
    }
    public void setTelefonosList(List<Telefonos> telefonosList) {
        this.telefonosList = telefonosList;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpTelefonia != null ? idEmpTelefonia.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresaTelefonia)) {
            return false;
        }
        EmpresaTelefonia other = (EmpresaTelefonia) object;
        if ((this.idEmpTelefonia == null && other.idEmpTelefonia != null) || (this.idEmpTelefonia != null && !this.idEmpTelefonia.equals(other.idEmpTelefonia))) {
            return false;
        }
        return true;
    }
    public String toXML(){
        String item = "<item>\n" +
                            "<id>"+this.getidEmpTelefonia()+"</id>\n" +
                            "<nombre>"+this.getNombre()+"</nombre>\n" +
                       "</item>\n";
         return item;
    }
}
