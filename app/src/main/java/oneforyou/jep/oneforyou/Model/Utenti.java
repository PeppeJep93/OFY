package oneforyou.jep.oneforyou.Model;

import java.io.Serializable;

public class Utenti implements Serializable {

    public Utenti(String nome, String cognome, String email, String telefono, String data, String luogo, int sesso, String username, String password, int blocco, String attivo, String ruolo, int saldo, String foto, String colore, int scommesse, float media, int vinte, int seguaci, int recensioni) {
        this.nome= nome;
        this.cognome= cognome;
        this.email= email;
        this.telefono  = telefono;
        this.data = data;
        this.luogo = luogo;
        this.sesso = sesso;
        this.username = username;
        this.password = password;
        this.blocco = blocco;
        this.attivo = attivo;
        this.ruolo = ruolo;
        this.saldo = saldo;
        this.foto = foto;
        this.colore = colore;
        this.scommesse = scommesse;
        this.vinte = vinte;
        this.media = media;
        this.seguaci = seguaci;
        this.recensioni = recensioni;
    }

    private String nome;
    public String getNome() {
        return this.nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    private String cognome;
    public String getCognome() {
        return this.cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    private String email;
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    private String telefono;
    public String getTelefono() {
        return this.telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private String data;
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }

    private String luogo;
    public String getLuogo() {
        return this.luogo;
    }
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    private int sesso;
    public int getSesso() {
        return this.sesso;
    }
    public void setSesso(int sesso) {
        this.sesso = sesso;
    }

    private String username;
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

	private String password;
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

	private int blocco;
    public int getBlocco() {
        return this.blocco;
    }
    public void setBlocco(int blocco) {
        this.blocco = blocco;
    }

    private String attivo;
    public String getAttivo() {
        return this.attivo;
    }
    public void setAttivo(String attivo) {
        this.attivo = attivo;
    }

	private String ruolo;
    public String getRuolo() {
        return this.ruolo;
    }
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

	private int saldo;
    public int getSaldo() {
        return this.saldo;
    }
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

	private String foto;
    public String getFoto() {
        return this.foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    private String colore;
    public String getColore() {
        return this.colore;
    }
    public void setColore(String colore) {
        this.colore = colore;
    }

    private int scommesse;
    public int getScommesse() {
        return this.scommesse;
    }
    public void setScommesse(int scommesse) {
        this.scommesse = scommesse;
    }

    private int vinte;
    public int getVinte() {
        return this.vinte;
    }
    public void setVinte(int vinte) {
        this.vinte = vinte;
    }

    private float media;
    public float getMedia() {
        return this.media;
    }
    public void setMedia(float media) {
        this.media = media;
    }

    private int seguaci;
    public int getSeguaci() {
        return this.seguaci;
    }
    public void setSeguaci(int seguaci) {
        this.seguaci = seguaci;
    }

    private int recensioni;
    public int getRecensioni() {
        return this.recensioni;
    }
    public void setRecensioni(int recensioni) {
        this.recensioni = recensioni;
    }

}

 /*public boolean load(String email)
    {
        boolean res=false;
        Connection conn=null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection("jdbc:odbc:nome");
            ps = conn.prepareStatement("SELECT * from Utenti Where email=?");
            ps.setString(1,email);
            rs = ps.executeQuery();
            if (rs.next()){
                setNome(rs.getString("nome"));
                setCognome(rs.getString("cognome"));
                setEmail(rs.getString("email"));
                res=true;
            }
        }
        catch (Exception e){e.printStackTrace(); return res;}
        finally{ try{
            if (rs!=null)
                rs.close();
            if (ps!=null)
                ps.close();
            if (conn!=null)
                conn.close();
        }
        catch (Exception ee){ee.printStackTrace();return res;}
        }
        return res;
    }*/
