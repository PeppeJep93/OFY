package oneforyou.jep.oneforyou.Model;

import java.io.Serializable;

public class Scommesse implements Serializable {

    //Codice	Giocatore	Partite	Pronostico	Quota	Crediti	Stato	Vincita

    public Scommesse(int codice, String giocatore, int partita, int pronostico, int quota, int crediti, String stato, int vincita) {
        this.codice= codice;
        this.giocatore= giocatore;
        this.partita= partita;
        this.pronostico  = pronostico;
        this.quota = quota;
        this.crediti= crediti;
        this.stato  = stato;
        this.vincita = vincita;
    }

    private int codice;
    public int getCodice() {
        return this.codice;
    }
    public void setCodice(int codice) {
        this.codice = codice;
    }

    private String giocatore;
    public String getGiocatore() {
        return this.giocatore;
    }
    public void setGiocatore(String giocatore) {
        this.giocatore = giocatore;
    }

    private int partita;
    public int getPartita() {
        return this.partita;
    }
    public void setPartita(int partita) {
        this.partita = partita;
    }

    private int pronostico;
    public int getPronostico() {
        return this.pronostico;
    }
    public void setPronostico(int pronostico) {
        this.pronostico = pronostico;
    }

    private int quota;
    public int getQuota() {
        return this.quota;
    }
    public void setQuota(int quota) {
        this.quota = quota;
    }

    private int crediti;
    public int getCrediti() {
        return this.crediti;
    }
    public void setCrediti(int crediti) {
        this.crediti = crediti;
    }

    private String stato;
    public String getStato() {
        return this.stato;
    }
    public void setStato(String stato) {
        this.stato = stato;
    }

    private int vincita;
    public int getVincita() {
        return this.vincita;
    }
    public void setVincita(int vincita) {
        this.vincita = vincita;
    }
}
