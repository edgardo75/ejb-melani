/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import javax.ejb.Remote;
/**
 *
 * @author Edgardo
 */
@Remote
public interface EJBPresupuestosRemote {
    long addBudgets(String xmlPresupuesto);
    String selectAllPresupuestosJPA();
    Integer getRecordCount();
    String selectPresupuestoOfTheDay();
    String verPresupuestosPaginados(Integer index, Integer record);
    String searchOneBudget(int idpresupuesto);
    String ShowReportPresupuesto(Integer idPresupuesto);
    String ShowReportVerPresupuesto(Long firt, Long Last);
}
