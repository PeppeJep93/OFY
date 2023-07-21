package oneforyou.jep.oneforyou.Model;

import java.io.Serializable;

public class Partite implements Serializable {

    //ID	SquadraCasa	GolCasa	GolOspite	SquadraOspite	Giorno	Mese	Anno	Ora	Minuti	ColoreCasa	ColoreOspite	Esito	Uno	Ics

    public Partite(int id, String casa, int golcasa, int golospite, String ospite, String data, int ore, int minuti, String colorec, String coloreo, int esito, int uno, int ics, int due, int consiglio) {
        this.id= id;
        this.casa= casa;
        this.golCasa= golcasa;
        this.golOspite  = golospite;
        this.ospite = ospite;
        this.data = data;
        this.ore = ore;
        this.minuti = minuti;
        this.coloreC = colorec;
        this.coloreO = coloreo;
        this.esito = esito;
        this.uno = uno;
        this.ics = ics;
        this.due = due;
        this.consiglio = consiglio;
    }

    private int id;
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private String casa;
    public String getCasa() {
        return this.casa;
    }
    public void setCasa(String casa) {
        this.casa = casa;
    }

    private int golCasa;
    public int getGolCasa() {
        return this.golCasa;
    }
    public void setGolCasa(int golCasa) {
        this.golCasa = golCasa;
    }

    private int golOspite;
    public int getGolOspite() {
        return this.golOspite;
    }
    public void setGolOspite(int golOspite) {
        this.golOspite = golOspite;
    }

    private String ospite;
    public String getOspite() {
        return this.ospite;
    }
    public void setOspite(String ospite) {
        this.ospite = ospite;
    }

    private String data;
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }

    private int ore;
    public int getOre() {
        return this.ore;
    }
    public void setOre(int ore) {
        this.ore = ore;
    }

    private int minuti;
    public int getMinuti() {
        return this.minuti;
    }
    public void setMinuti(int minuti) {
        this.minuti = minuti;
    }

    private String coloreC;
    public String getColoreC() {
        return this.coloreC;
    }
    public void setColoreC(String coloreC) {
        this.coloreC = coloreC;
    }

    private String coloreO;
    public String getColoreO() {
        return this.coloreO;
    }
    public void setColoreO(String coloreO) {
        this.coloreO = coloreO;
    }

    private int esito;
    public int getEsito() { return this.esito; }
    public void setEsito(int esito) {
        this.esito = esito;
    }

    private int uno;
    public int getUno() {
        return this.uno;
    }
    public void setUno(int uno) {
        this.uno = uno;
    }

    private int ics;
    public int getIcs() {
        return this.ics;
    }
    public void setIcs(int ics) {
        this.ics = ics;
    }

    private int due;
    public int getDue() {
        return this.due;
    }
    public void setDue(int due) {
        this.due = due;
    }

    private int consiglio;
    public int getConsiglio() { return this.consiglio; }
    public void setConsiglio(int consiglio) {
        this.consiglio = consiglio;
    }


}
