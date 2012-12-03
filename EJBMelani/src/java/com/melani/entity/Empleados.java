/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import javax.persistence.Temporal;

/**
 *A Entity Empleados
 * @version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@DiscriminatorValue("EMP")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="emptype",discriminatorType=DiscriminatorType.STRING)
public class Empleados extends Personas {
    @Column(precision=16,name="NUMEROEMPLEADO")
    private Integer numeroEmpleado;
    @Column(name="PASSWORD",nullable=false)
    private String password;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechacarga;
     @Column(name = "emptype",length = 10)
    private String emptype;

    public Empleados(){}
    
    public Date getFechacarga() {
        return fechacarga;
    }

    public Integer getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(Integer numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    

    public void setFechacarga(Date fechacarga) {
        this.fechacarga = fechacarga;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmptype() {
        return emptype;
    }

    public void setEmptype(String emptype) {
        this.emptype = emptype;
    }
    public String toXMLEmpleado(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String item="<numeroempleado>"+this.getNumeroEmpleado()+"</numeroempleado>\n" +
                "<clave>"+this.getPassword()+"</clave>\n" +
                "<fechacarga>"+sdf.format(this.getFechacarga())+"</fechacarga>\n";
        return item;


    }

}
