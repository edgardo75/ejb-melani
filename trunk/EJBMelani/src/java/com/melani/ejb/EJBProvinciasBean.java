/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melani.ejb;
import com.melani.entity.Provincias;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author Edgardo
 */
@Stateless(name="ejb/EJBProvinciasBean")
@WebService(serviceName="ServiceProvincias",name="ProvinciasWs")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class EJBProvinciasBean implements EJBProvinciasRemote {
org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EJBProvinciasBean.class);
@PersistenceContext
private EntityManager em;
    public String searchAllProvincias() {
        String xml = "<Lista>\n";
        try {
            Query consulta = em.createNamedQuery("Provincias.findAll");
            List<Provincias> lista = consulta.getResultList();
            if(lista.size()==0)
                xml="NO HAY PROVINCIAS CARGADAS";
            else{
                for (Iterator<Provincias> it = lista.iterator(); it.hasNext();) {
                    Provincias provincias = it.next();
                    xml+=provincias.toXML();
                }
                xml+="</Lista>\n";
            }
        } catch (Exception e) {
            logger.error("error en metodo searchallPRovincias", e);
        }finally{
        return xml;
        }
    }
}
