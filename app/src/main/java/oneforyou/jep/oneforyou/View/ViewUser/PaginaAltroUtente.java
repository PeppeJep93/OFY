package oneforyou.jep.oneforyou.View.ViewUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oneforyou.jep.oneforyou.Control.AggiornaLegame;
import oneforyou.jep.oneforyou.Control.AggiungiRelazione;
import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Scommesse;
import oneforyou.jep.oneforyou.Model.Social;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.CustomAdapterScommesseAmico;
import oneforyou.jep.oneforyou.Control.EliminaRelazione;
import oneforyou.jep.oneforyou.Control.IncrementaRecensioni;
import oneforyou.jep.oneforyou.Control.IncrementaSeguaci;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.Control.PrendiPartita;
import oneforyou.jep.oneforyou.Control.PrendiScommesseUtente;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.Control.TogliRecensione;
import oneforyou.jep.oneforyou.Control.TogliSeguace;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaAltroUtente extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato, amico;
    private Social legame;
    private RelativeLayout barramenu, voce1, voce2, voce3, voce4;
    private boolean segnalaerrori;
    private int risultatoquery;
    private String errorpwd;
    private ArrayList<Utenti> utenti = new ArrayList<>();
    private ArrayList<Bitmap> fotosutenti = new ArrayList<>();
    private LinearLayout riga;
    private Button seguireUtente, votareUtente;
    private ArrayList<Scommesse> scommesse = new ArrayList<>();
    public static ArrayList<Partite> partite = new ArrayList<>();
    private TextView nomeutente, cognomeutente, luogoutente, sessoutente, infotv, usernametv, statstv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutpaginautente);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");
        amico = (Utenti) bundle.getSerializable("AMICO");
        legame = (Social) bundle.getSerializable("SOCIAL");


        seguireUtente = (Button) findViewById(R.id.seguireutente);
        votareUtente = (Button) findViewById(R.id.votareutente);

        if (legame == null)
        {
            votareUtente.setVisibility(View.GONE);
            seguireUtente.setText("SEGUI I SUOI PRONOSTICI!");
        }

        if (legame != null) {
            if (legame.getPreferito() == 0) {
                votareUtente.setVisibility(View.VISIBLE);
                votareUtente.setText("LASCIA UN LIKE!");
                seguireUtente.setText("NON SEGUIRE PIU'!");
            }

            if (legame.getPreferito() == 1) {
                votareUtente.setVisibility(View.VISIBLE);
                votareUtente.setText("NON TI PIACE PIU'!");
                seguireUtente.setText("NON SEGUIRE PIU'!");
            }
        }

        nomeutente = (TextView) findViewById(R.id.nomeutente);
        cognomeutente = (TextView) findViewById(R.id.cognomeutente);
        luogoutente = (TextView) findViewById(R.id.luogoutente);
        sessoutente = (TextView) findViewById(R.id.sessoutente);
        
        TextView user = (TextView) findViewById(R.id.username);
        TextView soldi = (TextView) findViewById(R.id.saldo);

        soldi.setText("" + loggato.getSaldo() + "◍ funnies");
        if (loggato.getRuolo().equalsIgnoreCase("tipster"))
            user.setText("♚ " + loggato.getUsername() + " ♚");
        else
            user.setText(loggato.getUsername() + "");

        if (controllointernet()) {
            final ImageView immagine = (ImageView) findViewById(R.id.immagine);
            PrendiFoto pf = new PrendiFoto(this);
            pf.execute(Connector.db + "img/" + loggato.getFoto());
            pf.setOnFinishListener(new PrendiFoto.onFinishListener() {
                @Override
                public void onFinish(String res) {
                    // The task has finished and you can now use the result
                    try {
                        byte[] encodeByte = Base64.decode(res, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        immagine.setImageBitmap(bitmap);
                        //immagine.setBackground(getResources().getDrawable(R.drawable.editcentro));
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            nomeutente.setText(amico.getNome());
            cognomeutente.setText(amico.getCognome());
            luogoutente.setText(amico.getLuogo());
            if (amico.getSesso() == 1) {
                sessoutente.setText("UOMO");
            }
            if (amico.getSesso() == 0) {
                sessoutente.setText("DONNA");
            }

            usernametv = (TextView) findViewById(R.id.user);

            if (amico.getRuolo().equalsIgnoreCase("tipster")) {
                usernametv.setText("♚ " + amico.getUsername() + " ♚");
            }

            else {
                usernametv.setText(amico.getUsername() + "");
            }

            infotv = (TextView) findViewById(R.id.info);
            statstv = (TextView) findViewById(R.id.stats);
            infotv.setText("Recensioni positive: " + amico.getRecensioni() + " - Seguito da " +  amico.getSeguaci() + " utenti");
            statstv.setText("Vinte " + amico.getVinte() + " su " + amico.getScommesse() + " (media del " + String.valueOf(amico.getMedia() + "%)"));

        }

        RelativeLayout utente = (RelativeLayout) findViewById(R.id.utente);

        barramenu = findViewById(R.id.barramenu);
        voce1 = findViewById(R.id.voce1);
        voce2 = findViewById(R.id.voce2);
        voce3 = findViewById(R.id.voce3);
        voce4 = findViewById(R.id.voce4);
        riga = (LinearLayout) findViewById(R.id.riga);

        if (loggato.getColore().toLowerCase().contains("colore")) {
            int x = (int) (Math.random() * ((9 - 1) + 1)) + 1;

            if (x == 1) {
                loggato.setColore("azzurro");
            }
            if (x == 2) {
                loggato.setColore("blu");
            }
            if (x == 3) {
                loggato.setColore("giallo");
            }
            if (x == 4) {
                loggato.setColore("rosso");
            }
            if (x == 5) {
                loggato.setColore("verde");
            }
            if (x == 6) {
                loggato.setColore("granata");
            }
            if (x == 7) {
                loggato.setColore("arancione");
            }
            if (x == 8) {
                loggato.setColore("bianco");
            }
            if (x == 9) {
                loggato.setColore("nero");
            }
            if (x == 10) {
                loggato.setColore("viola");
            }

        }

        if (loggato.getColore().toLowerCase().contains("azzurro")) {
            barramenu.setBackgroundResource(R.color.azzurros);
            voce3.setBackgroundResource(R.color.azzurroc);
            utente.setBackgroundResource(R.color.azzurro);
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce3.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            voce3.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            voce3.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            voce3.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            voce3.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce3.setBackgroundResource(R.color.arancionec);
            utente.setBackgroundResource(R.color.arancione);
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce3.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce3.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce3.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
        }



        if (amico.getColore().toLowerCase().contains("azzurro")) {
            riga.setBackgroundResource(R.color.azzurrocc);
            votareUtente.setBackgroundResource(R.color.azzurros);
            seguireUtente.setBackgroundResource(R.color.azzurros);
        }

        if (amico.getColore().toLowerCase().contains("blu")) {
            riga.setBackgroundResource(R.color.blucc);
            votareUtente.setBackgroundResource(R.color.blus);
            seguireUtente.setBackgroundResource(R.color.blus);
        }

        if (amico.getColore().toLowerCase().contains("giallo")) {
            riga.setBackgroundResource(R.color.giallocc);
            votareUtente.setBackgroundResource(R.color.giallos);
            seguireUtente.setBackgroundResource(R.color.giallos);
        }

        if (amico.getColore().toLowerCase().contains("rosso")) {
            riga.setBackgroundResource(R.color.rossocc);
            votareUtente.setBackgroundResource(R.color.rossos);
            seguireUtente.setBackgroundResource(R.color.rossos);
        }

        if (amico.getColore().toLowerCase().contains("verde")) {
            riga.setBackgroundResource(R.color.verdecc);
            votareUtente.setBackgroundResource(R.color.verdes);
            seguireUtente.setBackgroundResource(R.color.verdes);
        }

        if (amico.getColore().toLowerCase().contains("granata")) {
            riga.setBackgroundResource(R.color.granatacc);
            votareUtente.setBackgroundResource(R.color.granatas);
            seguireUtente.setBackgroundResource(R.color.granatas);
        }

        if (amico.getColore().toLowerCase().contains("arancione")) {
            riga.setBackgroundResource(R.color.arancione);
            votareUtente.setBackgroundResource(R.color.aranciones);
            seguireUtente.setBackgroundResource(R.color.aranciones);
        }

        if (amico.getColore().toLowerCase().contains("bianco")) {
            riga.setBackgroundResource(R.color.biancocc);
            votareUtente.setBackgroundResource(R.color.neros);
            seguireUtente.setBackgroundResource(R.color.neros);
        }

        if (amico.getColore().toLowerCase().contains("nero")) {
            riga.setBackgroundResource(R.color.nerocc);
            votareUtente.setBackgroundResource(R.color.neros);
            seguireUtente.setBackgroundResource(R.color.neros);
        }

        if (amico.getColore().toLowerCase().contains("viola")) {
            riga.setBackgroundResource(R.color.violacc);
            votareUtente.setBackgroundResource(R.color.violas);
            seguireUtente.setBackgroundResource(R.color.violas);
        }

        if (controllointernet()) {
            creaListaScommesse();
        }

        if (controllointernet()) {
            final ImageView immagine = (ImageView) findViewById(R.id.immagine);
            PrendiFoto pf = new PrendiFoto(this);
            pf.execute(Connector.db + "img/" + loggato.getFoto());
            pf.setOnFinishListener(new PrendiFoto.onFinishListener() {
                @Override
                public void onFinish(String res) {
                    // The task has finished and you can now use the result
                    try {
                        byte[] encodeByte = Base64.decode(res, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        immagine.setImageBitmap(bitmap);
                        //immagine.setBackground(getResources().getDrawable(R.drawable.editcentro));
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
        }

        if (controllointernet()) {
            final ImageView foto = (ImageView) findViewById(R.id.foto);
            PrendiFoto pf = new PrendiFoto(this);
            pf.execute(Connector.db + "img/" + amico.getFoto());
            pf.setOnFinishListener(new PrendiFoto.onFinishListener() {
                @Override
                public void onFinish(String res) {
                    // The task has finished and you can now use the result
                    try {
                        byte[] encodeByte = Base64.decode(res, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        foto.setImageBitmap(bitmap);
                        //immagine.setBackground(getResources().getDrawable(R.drawable.editcentro));
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
        }
    }

    private void creaListaScommesse() {
        PrendiScommesseUtente ab = new PrendiScommesseUtente(this);
        ab.execute(amico.getUsername());
        ab.setOnFinishListener(new PrendiScommesseUtente.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrendiScommesseUtente.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrendiScommesseUtente.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrendiScommesseUtente.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrendiScommesseUtente.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    PrendiScommesseUtente.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    PrendiScommesseUtente.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                try {
                    JSONObject jOResult = new JSONObject(result);
                    int success = jOResult.getInt("success");
                    if (success == 1) {
                        JSONArray datipartite;
                        datipartite = jOResult.getJSONArray("datiutente");
                        for (int i = 0; i < datipartite.length(); i++) {
                            JSONObject partita = datipartite.getJSONObject(i);
                            int codice = partita.getInt("Codice");
                            String giocatore = partita.getString("Giocatore");
                            int gara = partita.getInt("Partita");
                            int pronostico = partita.getInt("Pronostico");
                            int quota = partita.getInt("Quota");
                            int crediti = partita.getInt("Crediti");
                            String stato = partita.getString("Stato");
                            int vincita = partita.getInt("Vincita");
                            Scommesse sc = new Scommesse(codice, giocatore, gara, pronostico, quota, crediti, stato, vincita);
                            scommesse.add(sc);
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PrendiScommesseUtente.asydata = true;
            }
        });
        while (PrendiScommesseUtente.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        PrendiScommesseUtente.asydata = false;

        partite.clear();

        for (int i=0; i < scommesse.size(); i++){
            PrendiPartita pp = new PrendiPartita(this);
            pp.execute(""+scommesse.get(i).getPartita());
            pp.setOnFinishListener(new PrendiPartita.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        PrendiPartita.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        PrendiPartita.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        PrendiPartita.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        PrendiPartita.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        PrendiPartita.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        PrendiPartita.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    try {
                        JSONObject jOResult = new JSONObject(result);
                        int success = jOResult.getInt("success");
                        if (success == 1) {
                            JSONArray utentedati;
                            utentedati = jOResult.getJSONArray("datiutente");
                            //for (int i = 0; i < utentedati.length(); i++) {
                            JSONObject partita = utentedati.getJSONObject(0);

                            int id = partita.getInt("ID");
                            String casa = partita.getString("SquadraCasa");
                            int golCasa = partita.getInt("GolCasa");
                            int golOspite = partita.getInt("GolOspite");
                            String ospite = partita.getString("SquadraOspite");
                            String data = partita.getString("DataInizio");
                            int ora = partita.getInt("Ora");
                            int minuti = partita.getInt("Minuti");
                            String coloreCasa = partita.getString("ColoreCasa");
                            String coloreOspite = partita.getString("ColoreOspite");
                            int esito = partita.getInt("Esito");
                            int uno = partita.getInt("Uno");
                            int ics = partita.getInt("Ics");
                            int due = partita.getInt("Due");
                            int consiglio = partita.getInt("Consiglio");

                            Partite match = new Partite(id, casa, golCasa, golOspite, ospite, data, ora, minuti, coloreCasa, coloreOspite, esito, uno, ics, due, consiglio);
                            partite.add(match);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PrendiPartita.asydata = true;
                }
            });
            while (PrendiPartita.asydata == false) {
                if (segnalaerrori == true) {
                    ;
                }
            }
            segnalaerrori = false;
            PrendiPartita.asydata = false;
        }

        creaListView(scommesse, partite);
    }

    private void creaListView(final ArrayList<Scommesse> listas, final ArrayList<Partite> listap) {
        final ListView listView = (ListView) findViewById(R.id.listviewScommesse);

        if (listas.size() > 0) {

            CustomAdapterScommesseAmico customAdapter = new CustomAdapterScommesseAmico(this, R.layout.elementoscommesse, R.id.squadra1, new ArrayList<Scommesse>());

            listView.setAdapter(customAdapter);

            listView.setAdapter(new CustomAdapterScommesseAmico(this,R.layout.elementoscommesse, R.id.squadra1, listas));

            customAdapter.notifyDataSetChanged();

        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastutentesenzascommesse, null);
            Toast toast = Toast.makeText(PaginaAltroUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            TextView zeroscommesse = (TextView) findViewById(R.id.zeroscommesse);
            zeroscommesse.setVisibility(View.VISIBLE);
            zeroscommesse.setText("L'AMICO NON HA ANCORA PIAZZATO UNA SCOMMESSA!");
        }
    }


    private synchronized boolean controllointernet() {
        boolean situation = false;

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isAvailable() == true) {
            situation = true;
        } else if (mMobile.isAvailable() == true) {
            View toastView = getLayoutInflater().inflate(R.layout.toastattivawifi, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            situation = false;
        }

        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
            View toastView = getLayoutInflater().inflate(R.layout.toastnondatimobili, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            situation = false;
        }
        return situation;
    }





    /*private synchronized void controllodati() throws ExecutionException, InterruptedException {
        ControlloDati cd = new ControlloDati(this);
        cd.setOnFinishListener(new ControlloDati.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    ControlloDati.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    ControlloDati.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    ControlloDati.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    ControlloDati.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    ControlloDati.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    ControlloDati.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                try {
                    JSONObject jOResult = new JSONObject(result);
                    int success = jOResult.getInt("success");
                    if (success == 1) {
                        JSONArray utentedati;
                        utentedati = jOResult.getJSONArray("datiutente");
                        //for (int i = 0; i < utentedati.length(); i++) {
                        JSONObject utente = utentedati.getJSONObject(0);
                        loggato.setSaldo(utente.getInt("Saldo"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cd.execute(loggato.getUsername(), loggato.getPassword());
        while (ControlloDati.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        ControlloDati.asydata = false;
    }*/

    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaAltroUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaAltroUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("nonpiacepiu"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastnonpiacepiu, null);
            Toast toast = Toast.makeText(PaginaAltroUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("tipiace"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toasttipiace, null);
            Toast toast = Toast.makeText(PaginaAltroUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("segui"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastsegui, null);
            Toast toast = Toast.makeText(PaginaAltroUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("nonsegui"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastnonsegui, null);
            Toast toast = Toast.makeText(PaginaAltroUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
    }

    public void profilo(View view) {
        Intent intent = new Intent(getApplicationContext(), PaginaAccountUtente.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("UTENTE", loggato);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void esci(View view) {
        AlertDialog alertbox = new AlertDialog.Builder(this, R.style.MyTheme)
                .setMessage("Vuoi uscire dall'app?")
                .setCancelable(false)
                .setPositiveButton("Sì!", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent;
                        intent = new Intent(getApplicationContext(), PaginaLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        //close();


                    }
                })
                .setNegativeButton("No.", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    public void passasezione(View view) {
        Intent intent;
        Bundle bundle;
        bundle = new Bundle();
        bundle.putSerializable("UTENTE", loggato);
        switch (view.getId()) {
            case R.id.voce1:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.voce2:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.voce4:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaPremiUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.voce1a:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.voce2a:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.voce4a:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaPremiUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    public boolean votaUtente(View view) {
        infotv = (TextView) findViewById(R.id.info);

        Button button = (Button) findViewById(view.getId());
        if (button.getText().toString().toLowerCase().contains("like")){
            button.setText("NON MI PIACE PIU'!");

            infotv.setText("Recensioni positive: " + (amico.getRecensioni()+1) + " - Seguito da " +  (amico.getSeguaci()+1) + " utenti");

            AggiornaLegame rs = new AggiornaLegame(this);
            rs.execute(loggato.getUsername(), amico.getUsername(), "1");
            rs.setOnFinishListener(new AggiornaLegame.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    if (result.contains("Social")) {
                        errorpwd = "tipiace";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("Problemi")) {
                        errorpwd = "boh";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    AggiornaLegame.asydata = true;
                }
            });
            while (AggiornaLegame.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            AggiornaLegame.asydata = false;

            IncrementaRecensioni ts = new IncrementaRecensioni(this);
            ts.execute(amico.getUsername());
            ts.setOnFinishListener(new IncrementaRecensioni.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        IncrementaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        IncrementaRecensioni.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    IncrementaRecensioni.asydata = true;
                }
            });
            while (IncrementaRecensioni.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            IncrementaRecensioni.asydata = false;

            return false;
        }

        if (button.getText().toString().toLowerCase().contains("piace")){
            button.setText("LASCIA UN LIKE!");

            infotv.setText("Recensioni positive: " + (amico.getRecensioni()) + " - Seguito da " +  (amico.getSeguaci()+1) + " utenti");

            AggiornaLegame rs = new AggiornaLegame(this);
            rs.execute(loggato.getUsername(), amico.getUsername(), "0");
            rs.setOnFinishListener(new AggiornaLegame.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    if (result.contains("Social")) {
                        errorpwd = "nonpiacepiu";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    if (result.contains("Problemi")) {
                        errorpwd = "boh";
                        segnalaerrori = true;
                        AggiornaLegame.asydata = true;
                        return;
                    }
                    AggiornaLegame.asydata = true;
                }
            });
            while (AggiornaLegame.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            AggiornaLegame.asydata = false;

            TogliRecensione ts = new TogliRecensione(this);
            ts.execute(amico.getUsername());
            ts.setOnFinishListener(new TogliRecensione.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliRecensione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliRecensione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliRecensione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliRecensione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        TogliRecensione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        TogliRecensione.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    TogliRecensione.asydata = true;
                }
            });
            while (TogliRecensione.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            TogliRecensione.asydata = false;
            return false;
        }
        return false;
    }

    public boolean seguiUtente(View view) {
        infotv = (TextView) findViewById(R.id.info);

        Button button = (Button) findViewById(view.getId());
        Button votare = (Button) findViewById(R.id.votareutente);

        if (button.getText().toString().toLowerCase().contains("pronostici")){
            button.setText("NON SEGUIRE PIU'!");

            infotv.setText("Recensioni positive: " + amico.getRecensioni() + " - Seguito da " +  (amico.getSeguaci()+1) + " utenti");

            AggiungiRelazione rs = new AggiungiRelazione(this);
            rs.execute(loggato.getUsername(), amico.getUsername());
            rs.setOnFinishListener(new AggiungiRelazione.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    if (result.contains("Social")) {
                        errorpwd = "segui";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("Problemi")) {
                        errorpwd = "boh";
                        segnalaerrori = true;
                        AggiungiRelazione.asydata = true;
                        return;
                    }
                    AggiungiRelazione.asydata = true;
                }
            });
            while (AggiungiRelazione.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            AggiungiRelazione.asydata = false;

            IncrementaSeguaci ts = new IncrementaSeguaci(this);
            ts.execute(amico.getUsername());
            ts.setOnFinishListener(new IncrementaSeguaci.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaSeguaci.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaSeguaci.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaSeguaci.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        IncrementaSeguaci.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        IncrementaSeguaci.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        IncrementaSeguaci.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    IncrementaSeguaci.asydata = true;
                }
            });
            while (IncrementaSeguaci.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            IncrementaSeguaci.asydata = false;

            votareUtente.setVisibility(View.VISIBLE);
            votareUtente.setText("LASCIA UN LIKE!");
            return false;
        }


        if (button.getText().toString().toLowerCase().contains("seguire")){
            button.setText("SEGUI I SUOI PRONOSTICI!");

            infotv.setText("Recensioni positive: " + amico.getRecensioni() + " - Seguito da " +  (amico.getSeguaci()) + " utenti");

            EliminaRelazione rs = new EliminaRelazione(this);
            rs.execute(loggato.getUsername(), amico.getUsername());
            rs.setOnFinishListener(new EliminaRelazione.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    if (result.contains("Social")) {
                        errorpwd = "nonsegui";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                    }
                    if (result.contains("Problemi")) {
                        errorpwd = "boh";
                        segnalaerrori = true;
                        EliminaRelazione.asydata = true;
                        return;
                    }
                    EliminaRelazione.asydata = true;
                }
            });
            while (EliminaRelazione.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            EliminaRelazione.asydata = false;

            TogliSeguace ts = new TogliSeguace(this);
            ts.execute(amico.getUsername());
            ts.setOnFinishListener(new TogliSeguace.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliSeguace.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliSeguace.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliSeguace.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TogliSeguace.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        TogliSeguace.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        TogliSeguace.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    TogliSeguace.asydata = true;
                }
            });
            while (TogliSeguace.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            TogliSeguace.asydata = false;

            if (votareUtente.getText().toString().toLowerCase().contains("piace")){

                infotv.setText("Recensioni positive: " + (amico.getRecensioni()) + " - Seguito da " +  amico.getSeguaci() + " utenti");

                TogliRecensione tr = new TogliRecensione(this);
                tr.execute(amico.getUsername());
                tr.setOnFinishListener(new TogliRecensione.onFinishListener() {
                    @Override
                    public void onFinish(String result) {
                        if (result.contains("java.net.ConnectException")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            TogliRecensione.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.UnsupportedEncoding")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            TogliRecensione.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.Protocol")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            TogliRecensione.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.MalformedUrl")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            TogliRecensione.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.IO")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            TogliRecensione.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.SocketTimeoutException")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            TogliRecensione.asydata = true;
                            return;
                            //stampare risultato asynctask
                        }
                        TogliRecensione.asydata = true;
                    }
                });
                while (TogliRecensione.asydata == false) {
                    if (segnalaerrori == true) {
                        toastview(errorpwd);
                    }
                }
                segnalaerrori = false;
                TogliRecensione.asydata = false;
            }

            votareUtente.setText("LASCIA UN LIKE!");
            votareUtente.setVisibility(View.GONE);

            return false;
        }
        return false;
    }
}