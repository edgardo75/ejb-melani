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
public interface EJBMelaniLoggsRemote {
    String searchAllLogs();
    String getRecordCountLog();
    String getRecordCountPagingLog(int index, int recordCount);
}
