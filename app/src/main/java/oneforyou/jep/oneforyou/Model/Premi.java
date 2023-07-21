package oneforyou.jep.oneforyou.Model;

import java.io.Serializable;

public class Premi implements Serializable {

    public Premi(String utente, String servizio, int valore, int costo, String codice, String data) {
        this.utente= utente;
        this.servizio= servizio;
        this.valore= valore;
        this.costo = costo;
        this.codice  = codice;
        this.data = data;
    }

    private String utente;
    public String getUtente() {
        return this.utente;
    }
    public void setUtente(String utente) {
        this.utente = utente;
    }

    private String servizio;
    public String getServizio() {
        return this.servizio;
    }
    public void setServizio(String servizio) {
        this.servizio = servizio;
    }

    private int valore;
    public int getValore() {
        return this.valore;
    }
    public void setValore(int valore) {
        this.valore = valore;
    }

    private int costo;
    public int getCosto() {
        return this.costo;
    }
    public void setCosto(int costo) {
        this.costo = costo;
    }

    private String codice;
    public String getCodice() {
        return this.codice;
    }
    public void setCodice(String codice) {
        this.codice = codice;
    }

    private String data;
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }

}
