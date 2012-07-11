/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.entity;

import java.io.Serializable;

import java.util.Date;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

/**
 *
 * @author Edgardo
 */
@Entity
@Table(name="HISTPERSONASDOMICILIOS")
public class HistPersonasDomicilios implements Serializable {
    @Id
    @TableGenerator(name="HistPerDomIdGen", table="ID_GEN_HPD",
    pkColumnName="FNAME",pkColumnValue="HistPersonasDomicilios", valueColumnName="FKEY",
    allocationSize=1)
    @GeneratedValue(generator="HistPerDomIdGen",strategy=GenerationType.TABLE)
    private Long idhistperdom; 
    @Column(name="IDDOMICILIO",nullable=false,precision=16)
    private Long idDomicilio;
    @Column(name="IDPERSONA",precision=16)
    private Long idPersona;
    @Column(name="FECHADECAMBIO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechadecambio;
    @Column(name="IDUSUARIO")
    private Integer idusuario;
    


    public HistPersonasDomicilios(){}
    public HistPersonasDomicilios(Long idhistperdom){
        this.idhistperdom=idhistperdom;
    }

    public Long getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(Long idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }



    

    public Date getFechadecambio() {
        return fechadecambio;
    }

    public void setFechadecambio(Date fechadecambio) {
        this.fechadecambio = fechadecambio;
    }

    public Long getIdhistperdom() {
        return idhistperdom;
    }

    public void setIdhistperdom(Long idhistperdom) {
        this.idhistperdom = idhistperdom;
    }

    public String toXML(){
        String item ="<item>\n";

        item+="<id>"+this.getIdhistperdom()+"</id>\n" +
                "<idper>"+this.getIdPersona()+"</idper>\n" +
                "<iddom>"+this.getIdDomicilio()+"<iddom>\n" +
                "<fechacambio>"+this.getFechadecambio()+"</fechacambio>\n" +
                "<idusuario>"+this.getIdusuario()+"</idusuario>\n" +
                "</item>\n";

        return item;
    }

}
