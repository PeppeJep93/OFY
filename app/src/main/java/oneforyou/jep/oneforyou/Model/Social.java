package oneforyou.jep.oneforyou.Model;

import java.io.Serializable;

public class Social implements Serializable {

    //Codice	Giocatore	Partite	Pronostico	Quota	Crediti	Stato	Vincita

    public Social(String amico, String follow, int preferito) {
        this.amico= amico;
        this.follow= follow;
        this.preferito= preferito;
    }

    private String amico;
    public String getAmico() {
        return this.amico;
    }
    public void setAmico(String amico) {
        this.amico = amico;
    }

    private String follow;
    public String getFollow() {
        return this.follow;
    }
    public void setFollow(String follow) {
        this.follow = follow;
    }

    private int preferito;
    public int getPreferito() {
        return this.preferito;
    }
    public void setPreferito(int preferito) {
        this.preferito = preferito;
    }

}
