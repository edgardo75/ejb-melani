/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.melani.utils;





/**
 *
 * @author Edgardo™
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
    private String numdepto;
    private Localidadess localidad;
    private String homeDetelles;


    ////////-------------GETTERS AND SETTERS----------------
    ////----------------------------------
     public long getDomicilioId(){
        return domicilioId;
    }
    public String getPiso(){
        return piso;
    }

    

    

    public String getObservaciones() {
        return homeDetelles;
    }

    public void setObservaciones(String homeDetelles) {
        this.homeDetelles = homeDetelles;
    }
    
    public String getNumDepto(){
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



    public void setNumdepto(String numdepto) {
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

        public int getBarrioId(){
            return barrioId;
        }


    }
    public class Calless{
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
