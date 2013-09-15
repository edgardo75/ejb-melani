/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.entity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
/**
 * A Entity EmpleadoParttime
 *@version 1.0
 * @author Edgardo Alvarez
 */
@Entity
@DiscriminatorValue("EMPPARTTIME")
public class EmpleadoParttime extends Empleados implements Serializable{
    @Column(name="SALARIO_POR_HORA",precision=10,scale=2)
    protected BigDecimal salarioporhora;
    public BigDecimal getSalarioporhora() {
        return salarioporhora;
    }
    public void setSalarioporhora(BigDecimal salarioporhora) {
        this.salarioporhora = salarioporhora;
    }
    @Override
    public String toXML(){
        String xml="<salarioporhora>"+this.getSalarioporhora()+"</salarioporhora>\n";
        return xml;
    }
}
