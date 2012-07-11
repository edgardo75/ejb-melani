/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.melani.entity;


import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * A Entity FullTimeEmpleado
 *@version
 * @author Edgardo Alvarez
 */

@Entity
@DiscriminatorValue("EMPFULLTIME")
public class FullTimeEmpleado extends Empleados{
    @Column(name="SALARIO",precision = 10,scale=2)
    protected BigDecimal salario;
    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
    
   


}
