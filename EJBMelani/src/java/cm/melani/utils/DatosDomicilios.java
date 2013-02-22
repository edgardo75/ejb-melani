/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.melani.utils;





/**
 *
 * @author Edgardo™
 * @version 1.0 Build 5600 Feb 20, 2013
 */

public class DatosDomicilios {

    private long domicilioId;
    private String piso;
    private String manzana;
    private String entrecalleycalle;
    private String sector;
    private String torre;
    private String monoblock;
    private Barrioss barrios;
    private Calless calles;
    private String area;
    private Orientacions orientacion;
    private int numero;
    private Integer numdepto;
    private Localidadess localidad;
    private String det1ails_homes;


    ////////-------------GETTERS AND SETTERS----------------
    ////----------------------------------
    public DatosDomicilios(){}
     public long getDomicilioId(){
        return domicilioId;
    }
    public String getPiso(){
        return piso;
    }

    

    

    public String getObservaciones() {
        return det1ails_homes;
    }

    public void setObservaciones(String det1ails_homes) {
        this.det1ails_homes = det1ails_homes;
    }
    
    public Integer getNumDepto(){
        return numdepto;
    }
    public String getManzana(){
        return manzana;
    }
    public String getEntrecalleycalle(){
        return entrecalleycalle;
    }

    public String getSector(){
        return sector;
    }
    public String getTorre(){
        return torre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }



    public void setNumdepto(Integer numdepto) {
        this.numdepto = numdepto;
    }

    public String getMonoblock(){
        return monoblock;
    }

    public int getNumero(){
        return numero;
    }


    ////----------------------------------

    public Barrioss getBarrio(){

        return barrios;
    }
    public Calless getCalle(){
        return calles;
    }

    public Orientacions getOrientacion(){
        return orientacion;
    }



    /////////////////////--------------------------------------------------
    public class Barrioss{
        private int barrioId;
        public Barrioss(){}
        public int getBarrioId(){
            return barrioId;
        }


    }
    public class Calless{

        public Calless(){}
        public Calless(int calleId) {
            this.calleId = calleId;
        }
        
        private int calleId;

        public int getCalleId(){
            return calleId;
        }
        public void setCalleId(int calleId){
            this.calleId = calleId;
        }

    }
    public class Orientacions{

        private long idOrientacion;

        public Orientacions(){}
        public Orientacions(long idOrientacion) {
            this.idOrientacion = idOrientacion;
        }
        

        public long getOrientacion(){
            return idOrientacion;
        }
        public void setOrientacion(long idOrientacion){
            this.idOrientacion = idOrientacion;
        }
    }
    /////////////////////--------------------------------------------------

    public Localidadess getLocalidad(){
        return localidad;
    }
//----- --  --  --  -   --- -   --      -
    public class Localidadess{
        private long idlocalidad;
        private short idProvincia;
        private int codigoPostal;

        public Localidadess(){}
        
        public Localidadess(long idlocalidad, short idProvincia, int codigoPostal) {
            this.idlocalidad = idlocalidad;
            this.idProvincia = idProvincia;
            this.codigoPostal = codigoPostal;
        }

        public int getCodigoPostal(){
                return codigoPostal;
        }
       public void setCodigoPostal(int codigopostal){
            this.codigoPostal = codigopostal;
        }

        public long getIdLocalidad() {
            return idlocalidad;
        }

        public void setIdLocalidad(long idlocalidad) {
            this.idlocalidad = idlocalidad;
        }

        public short getIdProvincia() {
            return idProvincia;
        }

        public void setIdProvincia(short idProvincia) {
            this.idProvincia = idProvincia;
        }

       


    }

    ///----------  --- --------- ----- ----------------------   ------

    public void setDomicilioId(int domicilioId){
        this.domicilioId = domicilioId;
    }
    public void setPiso(String piso){
        this.piso = piso;
    }
    public void setManzana(String manzana){
        this.manzana = manzana;
    }
   public void setEntrecalleycalle(String entrecalleycalle){
        this.entrecalleycalle = entrecalleycalle;
   }
   public void setSector(String sector){
        this.sector = sector;
   }
   public void setTorre(String torre){
        this.torre = torre;
   }
   public void setMonoblock(String monoblock){
        this.monoblock = monoblock;
   }



}
