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
    private short piso;
    private Short manzana;
    private String entrecalleycalle;
    private Short sector;
    private Short torre;
    private Short monoblock;
    private Barrioss barrios;
    private Calless calles;
    private Short area;
    private Orientacions orientacion;
    private int numero;
    private Short numdepto;
    private Localidadess localidad;
    private String observaciones;


    ////////-------------GETTERS AND SETTERS----------------
    ////----------------------------------
     public long getDomicilioId(){
        return domicilioId;
    }
    public short getPiso(){
        return piso;
    }

    

    

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Short getNumDepto(){
        return numdepto;
    }
    public Short getManzana(){
        return manzana;
    }
    public String getEntrecalleycalle(){
        return entrecalleycalle;
    }

    public Short getSector(){
        return sector;
    }
    public Short getTorre(){
        return torre;
    }

    public Short getArea() {
        return area;
    }

    public void setArea(Short area) {
        this.area = area;
    }



    public void setNumdepto(Short numdepto) {
        this.numdepto = numdepto;
    }

    public Short getMonoblock(){
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
    public void setPiso(short piso){
        this.piso = piso;
    }
    public void setManzana(Short manzana){
        this.manzana = manzana;
    }
   public void setEntrecalleycalle(String entrecalleycalle){
        this.entrecalleycalle = entrecalleycalle;
   }
   public void setSector(Short sector){
        this.sector = sector;
   }
   public void setTorre(Short torre){
        this.torre = torre;
   }
   public void setMonoblock(Short monoblock){
        this.monoblock = monoblock;
   }



}
