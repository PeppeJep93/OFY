package oneforyou.jep.oneforyou.View.ViewUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import oneforyou.jep.oneforyou.Control.AggiornaSaldo;
import oneforyou.jep.oneforyou.Control.AggiornaScommesse;
import oneforyou.jep.oneforyou.Control.AmiciDue;
import oneforyou.jep.oneforyou.Control.AmiciIcs;
import oneforyou.jep.oneforyou.Control.AmiciUno;
import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Control.ControlloDati;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.Control.RegistraScommessa;
import oneforyou.jep.oneforyou.Control.TuttiDue;
import oneforyou.jep.oneforyou.Control.TuttiIcs;
import oneforyou.jep.oneforyou.Control.TuttiUno;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaScommetteUtente extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private Partite match;
    private int prono = -1, crediti = 1, funnies = 0, scelta = -1;
    private RelativeLayout barramenu, voce1, voce2, voce3, voce4;
    private String errorpwd;
    private boolean segnalaerrori;
    private TextView quota1, quotaX, quota2;
    private int risultatoquery, tuttiuno, tuttiics, tuttidue;
    private ArrayList<String> amiciuno = new ArrayList<>(), amiciics = new ArrayList<>(), amicidue = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutscommetteutente);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        ControlloDati cd = new ControlloDati(this);
        cd.execute(loggato.getUsername());
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
                    Log.d("OFY", "SONO DENTRO - DATI");
                    JSONObject jOResult = new JSONObject(result);
                    int success = jOResult.getInt("success");
                    if (success == 1) {
                        JSONArray utentedati;
                        utentedati = jOResult.getJSONArray("datiutente");
                        //for (int i = 0; i < utentedati.length(); i++) {
                        JSONObject utente = utentedati.getJSONObject(0);
                        String nome = utente.getString("Nome");
                        String cognome = utente.getString("Cognome");
                        String email = utente.getString("Email");
                        String telefono = utente.getString("Telefono");
                        String data = utente.getString("DataNascita");
                        String luogo = utente.getString("LuogoNascita");
                        int sesso = utente.getInt("Sesso");
                        //username
                        String password = utente.getString("Password");
                        int blocco = utente.getInt("Blocco");
                        String ruolo = utente.getString("Ruolo");
                        String attivo = utente.getString("Attivo");
                        int saldo = utente.getInt("Saldo");
                        String colore = utente.getString("Colore");
                        String foto = utente.getString("Foto");
                        int scommesse = utente.getInt("Scommesse");
                        int media = utente.getInt("Media");
                        int seguaci = utente.getInt("Seguaci");
                        int vinte = utente.getInt("Vinte");
                        int recensioni = utente.getInt("Recensioni");
                        Utenti loggatox = new Utenti(nome, cognome, email, telefono, data, luogo, sesso, loggato.getUsername(), password, blocco, attivo, ruolo, saldo, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                        loggato = loggatox;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ControlloDati.asydata = true;
            }
        });
        while (ControlloDati.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        ControlloDati.asydata = false;

        String colore = bundle.getString("COLORE");
        loggato.setColore(colore);

        match = (Partite) bundle.getSerializable("PARTITA");

        TextView user = (TextView) findViewById(R.id.username);
        TextView soldi = (TextView) findViewById(R.id.saldo);

        soldi.setText("" + loggato.getSaldo() + "◍ funnies");
        if (loggato.getRuolo().equalsIgnoreCase("tipster"))
            user.setText("♚ " + loggato.getUsername() + " ♚");
        else
            user.setText(loggato.getUsername() + "");

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

        RelativeLayout utente = (RelativeLayout) findViewById(R.id.utente);

        barramenu = findViewById(R.id.barramenu);
        voce1 = findViewById(R.id.voce1);
        voce2 = findViewById(R.id.voce2);
        voce3 = findViewById(R.id.voce3);
        voce4 = findViewById(R.id.voce4);

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
            voce1.setBackgroundResource(R.color.azzurroc);
            utente.setBackgroundResource(R.color.azzurro);
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce1.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            voce1.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            voce1.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            voce1.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            voce1.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce1.setBackgroundResource(R.color.arancionec);
            utente.setBackgroundResource(R.color.arancione);
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce1.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce1.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce1.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
        }

        //colore e foto

        if (controllointernet()) {
            TuttiUno cp = new TuttiUno(this);
            cp.setOnFinishListener(new TuttiUno.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TuttiUno.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TuttiUno.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TuttiUno.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        TuttiUno.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        TuttiUno.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        TuttiUno.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    if (Integer.parseInt(result) >= 0) {
                        segnalaerrori = false;
                        TuttiUno.asydata = true;
                        tuttiuno = Integer.parseInt(result);
                        return;
                        //stampare risultato asynctask
                    }
                }
            });
            cp.execute(""+match.getId());
            while (TuttiUno.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            TuttiUno.asydata = false;
        }

    if (controllointernet())
    {
        TuttiIcs cp = new TuttiIcs(this);
        cp.setOnFinishListener(new TuttiIcs.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    TuttiIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    TuttiIcs.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                if (Integer.parseInt(result) >= 0) {
                    segnalaerrori = false;
                    TuttiIcs.asydata = true;
                    tuttiics = Integer.parseInt(result);
                    return;
                    //stampare risultato asynctask
                }
            }
        });
        cp.execute(""+match.getId());
        while (TuttiIcs.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        TuttiIcs.asydata = false;
    }



    if (controllointernet())
    {
        TuttiDue cp = new TuttiDue(this);
        cp.setOnFinishListener(new TuttiDue.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    TuttiDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    TuttiDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    TuttiDue.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                if (Integer.parseInt(result) >= 0) {
                    segnalaerrori = false;
                    TuttiDue.asydata = true;
                    tuttidue = Integer.parseInt(result);
                    return;
                    //stampare risultato asynctask
                }
            }
        });
        cp.execute(""+match.getId());
        while (TuttiDue.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        TuttiDue.asydata = false;
    }

        AmiciUno df = new AmiciUno(this);
        df.execute(loggato.getUsername(), ""+match.getId());
        df.setOnFinishListener(new AmiciUno.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciUno.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciUno.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciUno.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciUno.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AmiciUno.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AmiciUno.asydata = true;
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
                            int prono = partita.getInt("Pronostico");
                            String giocatore = partita.getString("Giocatore");

                            if (prono == 1) {
                                amiciuno.add(giocatore);
                            }
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AmiciUno.asydata = true;
            }
        });
        while (AmiciUno.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        AmiciUno.asydata = false;



        AmiciIcs cda = new AmiciIcs(this);
        cda.execute(loggato.getUsername(), ""+match.getId());
        cda.setOnFinishListener(new AmiciIcs.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AmiciIcs.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AmiciIcs.asydata = true;
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
                            int prono = partita.getInt("Pronostico");
                            String giocatore = partita.getString("Giocatore");

                            if (prono == 0) {
                                amiciics.add(giocatore);
                            }
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AmiciIcs.asydata = true;
            }
        });
        while (AmiciIcs.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        AmiciIcs.asydata = false;



        AmiciDue cod = new AmiciDue(this);
        cod.execute(loggato.getUsername(), ""+match.getId());
        cod.setOnFinishListener(new AmiciDue.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AmiciDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AmiciDue.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AmiciDue.asydata = true;
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
                            int prono = partita.getInt("Pronostico");
                            String giocatore = partita.getString("Giocatore");

                            if (prono == 2) {
                                amicidue.add(giocatore);
                            }
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AmiciDue.asydata = true;
            }
        });
        while (AmiciDue.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        AmiciDue.asydata = false;

        TextView totaliuno = (TextView) findViewById(R.id.altriuno);
        totaliuno.setText("" + tuttiuno + "/" + (tuttiuno+tuttiics+tuttidue));
        TextView totaliics = (TextView) findViewById(R.id.altriics);
        totaliics.setText("" + tuttiics + "/" + (tuttiuno+tuttiics+tuttidue));
        TextView totalidue = (TextView) findViewById(R.id.altridue);
        totalidue.setText("" + tuttidue + "/" + (tuttiuno+tuttiics+tuttidue));

        TextView amici1 = (TextView) findViewById(R.id.amiciuno);
        amici1.setText("" + amiciuno.size() + " amici" );
        TextView amiciX = (TextView) findViewById(R.id.amiciics);
        amiciX.setText("" + amiciics.size() + " amici" );
        TextView amici2 = (TextView) findViewById(R.id.amicidue);
        amici2.setText("" + amicidue.size() + " amici" );


        TextView sqcasa = (TextView) findViewById(R.id.squadra1);
        TextView sqosp = (TextView) findViewById(R.id.squadra2);
        sqcasa.setText(match.getCasa() + "    ");
        sqosp.setText( "    " + match.getOspite());

        quota1 = (TextView) findViewById(R.id.quotauno);
        quotaX = (TextView) findViewById(R.id.quotaics);
        quota2 = (TextView) findViewById(R.id.quotadue);
        quota1.setText(""+match.getUno());
        quotaX.setText(""+match.getIcs());
        quota2.setText(""+match.getDue());

        if (match.getConsiglio() == 1)
            quota1.setPaintFlags(quota1.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        if (match.getConsiglio() == 0)
            quotaX.setPaintFlags(quotaX.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        if (match.getConsiglio() == 2)
            quota2.setPaintFlags(quota2.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        TextView dataPartita = (TextView) findViewById(R.id.datapartita);
        int anno = Integer.parseInt(match.getData().substring(0, 4));
        int mese = Integer.parseInt(match.getData().substring(5, 7));
        int giorno = Integer.parseInt(match.getData().substring(8, 10));
        if (match.getMinuti() < 10)
            dataPartita.setText("" + giorno + "/" +  mese + "/" + anno + " " + match.getOre() + ".0" + match.getMinuti());
        else
            dataPartita.setText("" + giorno + "/" +  mese + "/" + anno + " " + match.getOre() + "." + match.getMinuti());

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("azzurro")) {
            sqcasa.setBackgroundResource(R.color.azzurrocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("blu")) {
            sqcasa.setBackgroundResource(R.color.blucc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("giallo")) {
            sqcasa.setBackgroundResource(R.color.giallocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("rosso")) {
            sqcasa.setBackgroundResource(R.color.rossocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("verde")) {
            sqcasa.setBackgroundResource(R.color.verdecc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("granata")) {
            sqcasa.setBackgroundResource(R.color.granatacc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("arancione")) {
            sqcasa.setBackgroundResource(R.color.arancionecc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("bianco")) {
            sqcasa.setBackgroundResource(R.color.biancocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("nero")) {
            sqcasa.setBackgroundResource(R.color.nerocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("viola")) {
            sqcasa.setBackgroundResource(R.color.violacc);
        }





        if (match.getColoreO().toLowerCase().equalsIgnoreCase("azzurro")) {
            sqosp.setBackgroundResource(R.color.azzurrocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("blu")) {
            sqosp.setBackgroundResource(R.color.blucc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("giallo")) {
            sqosp.setBackgroundResource(R.color.giallocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("rosso")) {
            sqosp.setBackgroundResource(R.color.rossocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("verde")) {
            sqosp.setBackgroundResource(R.color.verdecc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("granata")) {
            sqosp.setBackgroundResource(R.color.granatacc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("arancione")) {
            sqosp.setBackgroundResource(R.color.arancionecc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("bianco")) {
            sqosp.setBackgroundResource(R.color.biancocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("nero")) {
            sqosp.setBackgroundResource(R.color.nerocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("viola")) {
            sqosp.setBackgroundResource(R.color.violacc);
        }


}


    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("boh"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastproblemiscommessa, null);
            Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("andata"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastscommessaok, null);
            Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            Intent intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("UTENTE", loggato);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this, R.style.MyTheme)
                .setMessage("Vuoi uscire dall'app?")
                .setCancelable(false)
                .setPositiveButton("Sì!", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    public void onClick(DialogInterface arg0, int arg1) {

                        finishAndRemoveTask();
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


    protected void onResume(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layouthome);
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
            case R.id.voce2:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.voce3:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaUtentiUtente.class);
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
            case R.id.voce2a:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.voce3a:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaUtentiUtente.class);
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


    public void vediListaAmici(View view) {

        if (view.getId() == R.id.amiciuno)
        {
            TextView listauno = (TextView) findViewById(R.id.listauno);
            if (listauno.getVisibility() == View.VISIBLE) {
                listauno.setVisibility(View.GONE);
                return;
            }
            if (listauno.getVisibility() == View.GONE) {
                listauno.setVisibility(View.VISIBLE);
                String list1 = "";
                for (int i = 0; i < amiciuno.size(); i++) {
                    list1 += amiciuno.get(i) + "\n";
                }
                listauno.setText(list1);
            }
        }

        if (view.getId() == R.id.amiciics)
        {
            TextView listaics = (TextView) findViewById(R.id.listaics);
            if (listaics.getVisibility() == View.VISIBLE) {
                listaics.setVisibility(View.GONE);
                return;
            }
            if (listaics.getVisibility() == View.GONE) {
                listaics.setVisibility(View.VISIBLE);
                String listX = "";
                for (int i = 0; i < amiciics.size(); i++) {
                    listX += amiciics.get(i) + "\n";
                }
                listaics.setText(listX);
            }
        }

        if (view.getId() == R.id.amicidue)
        {
            TextView listadue = (TextView) findViewById(R.id.listadue);
            if (listadue.getVisibility() == View.VISIBLE) {
                listadue.setVisibility(View.GONE);
                return;
            }
            if (listadue.getVisibility() == View.GONE) {
                listadue.setVisibility(View.VISIBLE);
                String list2 = "";
                for (int i = 0; i < amicidue.size(); i++) {
                    list2 += amicidue.get(i) + "\n";
                }
                listadue.setText(list2);
            }
        }
    }

    public void effettuaScommessa(View view) {
        RelativeLayout tastierino = (RelativeLayout) findViewById(R.id.tastierino);
        TextView q1 = (TextView) findViewById(R.id.quotauno);
        TextView qX = (TextView) findViewById(R.id.quotaics);
        TextView q2 = (TextView) findViewById(R.id.quotadue);
        TextView importo = (TextView) findViewById(R.id.ncrediti);
        TextView rimanenti = (TextView) findViewById(R.id.rimasti);
        Button vincita = (Button) findViewById(R.id.scommetti);

        if (view.getId() == R.id.quotauno){
            if (loggato.getSaldo() == 0)
            {
                View toastView = getLayoutInflater().inflate(R.layout.toastcreditiinsufficienti, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return;
            }
            if (prono == 1) {
                prono = -1;
                tastierino.setVisibility(View.GONE);
                q1.setBackgroundResource(R.color.trasp);
                q1.setTextColor(getResources().getColor(R.color.neros));
                crediti = 1;
            }
            if (prono == 2 || prono == 0 || prono == -1)  {
                prono = 1;
                if (loggato.getRuolo().equalsIgnoreCase("tipster")) {
                    funnies = (int) Math.ceil(Integer.parseInt(quota1.getText().toString()) * crediti * 1.10f + 1);
                }
                else {
                    funnies = (int) Math.ceil(Integer.parseInt(quota1.getText().toString()) * crediti);
                }
                vincita.setText("VINCI " + funnies + " funnies!");
                scelta = Integer.parseInt(quota1.getText().toString());
                tastierino.setVisibility(View.VISIBLE);
                q1.setBackgroundResource(R.color.neros);
                q1.setTextColor(getResources().getColor(R.color.biancos));
                qX.setBackgroundResource(R.color.trasp);
                qX.setTextColor(getResources().getColor(R.color.neros));
                q2.setBackgroundResource(R.color.trasp);
                q2.setTextColor(getResources().getColor(R.color.neros));
                importo.setText(""+crediti);
                rimanenti.setText("" + (loggato.getSaldo()-crediti));
            }
        }

        if (view.getId() == R.id.quotaics){
            if (loggato.getSaldo() == 0)
            {
                View toastView = getLayoutInflater().inflate(R.layout.toastcreditiinsufficienti, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return;
            }
            if (prono == 0) {
                prono = -1;
                tastierino.setVisibility(View.GONE);
                qX.setBackgroundResource(R.color.trasp);
                qX.setTextColor(getResources().getColor(R.color.neros));
                crediti = 1;
            }
            if (prono == 2 || prono == 1 || prono == -1)  {
                prono = 0;
                if (loggato.getRuolo().equalsIgnoreCase("tipster")) {
                    funnies = (int) Math.ceil(Integer.parseInt(quotaX.getText().toString()) * crediti * 1.10f + 1);
                }
                else {
                    funnies = (int) Math.ceil(Integer.parseInt(quotaX.getText().toString()) * crediti);
                }
                vincita.setText("VINCI " + funnies + " funnies!");
                scelta = Integer.parseInt(quotaX.getText().toString());
                tastierino.setVisibility(View.VISIBLE);
                qX.setBackgroundResource(R.color.neros);
                qX.setTextColor(getResources().getColor(R.color.biancos));
                q1.setBackgroundResource(R.color.trasp);
                q1.setTextColor(getResources().getColor(R.color.neros));
                q2.setBackgroundResource(R.color.trasp);
                q2.setTextColor(getResources().getColor(R.color.neros));
                importo.setText(""+crediti);
                rimanenti.setText("" + (loggato.getSaldo()-crediti));
            }
        }

        if (view.getId() == R.id.quotadue){
            if (loggato.getSaldo() == 0)
            {
                View toastView = getLayoutInflater().inflate(R.layout.toastcreditiinsufficienti, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return;
            }
            if (prono == 2) {
                prono = -1;
                tastierino.setVisibility(View.GONE);
                q2.setBackgroundResource(R.color.trasp);
                q2.setTextColor(getResources().getColor(R.color.neros));
                crediti = 1;
            }
            if (prono == 1 || prono == 0 || prono == -1)  {
                prono = 2;
                if (loggato.getRuolo().equalsIgnoreCase("tipster")) {
                    funnies = (int) Math.ceil(Integer.parseInt(quota2.getText().toString()) * crediti * 1.10f + 1);
                }
                else {
                    funnies = (int) Math.ceil(Integer.parseInt(quota2.getText().toString()) * crediti);
                }
                vincita.setText("VINCI " + funnies + " funnies!");
                scelta = Integer.parseInt(quota2.getText().toString());
                tastierino.setVisibility(View.VISIBLE);
                q2.setBackgroundResource(R.color.neros);
                q2.setTextColor(getResources().getColor(R.color.biancos));
                qX.setBackgroundResource(R.color.trasp);
                qX.setTextColor(getResources().getColor(R.color.neros));
                q1.setBackgroundResource(R.color.trasp);
                q1.setTextColor(getResources().getColor(R.color.neros));
                importo.setText(""+crediti);
                rimanenti.setText("" + (loggato.getSaldo()-crediti));
            }
        }
    }

    public void aggiungiQuantità(View view) {
        TextView importo = (TextView) findViewById(R.id.ncrediti);
        TextView rimasti = (TextView) findViewById(R.id.rimasti);
        Button vincita = (Button) findViewById(R.id.scommetti);

        if (view.getId() == R.id.solouno){
            crediti = 1;
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (view.getId() == R.id.allin){
            crediti = loggato.getSaldo();
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (view.getId() == R.id.piuuno){
            if (crediti + 1 <= loggato.getSaldo())
                crediti++;
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnondipiu, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (view.getId() == R.id.piudieci){
            if (crediti + 10 <= loggato.getSaldo())
                crediti += 10;
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnondipiu, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (view.getId() == R.id.piucinquanta){
            if (crediti + 50 <= loggato.getSaldo())
                crediti += 50;
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnondipiu, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (view.getId() == R.id.menouno){
            if (crediti - 1 >= 1)
                crediti--;
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnocreditinegativi, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (view.getId() == R.id.menodieci){
            if (crediti - 10 >= 1)
                crediti -= 10;
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnocreditinegativi, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (view.getId() == R.id.menocinquanta){
            if (crediti - 50 >= 1)
                crediti -= 50;
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnocreditinegativi, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            importo.setText(""+crediti);
            rimasti.setText("" + (loggato.getSaldo()-crediti));
        }

        if (prono == 1) {
            funnies = (int) Math.ceil(Integer.parseInt(quota1.getText().toString()) * crediti);
        }
        if (prono == 0) {
            funnies = (int) Math.ceil(Integer.parseInt(quota1.getText().toString()) * crediti);
        }
        if (prono == 2) {
            funnies = (int) Math.ceil(Integer.parseInt(quota1.getText().toString()) * crediti);
        }

        if (prono == 1 && loggato.getRuolo().equalsIgnoreCase("tipster")) {
            funnies = (int) Math.ceil(Integer.parseInt(quota1.getText().toString()) * crediti * 1.10f + 1);
        }
        if (prono == 0 && loggato.getRuolo().equalsIgnoreCase("tipster")) {
            funnies = (int) Math.ceil(Integer.parseInt(quotaX.getText().toString()) * crediti * 1.10f + 1);
        }
        if (prono == 2 && loggato.getRuolo().equalsIgnoreCase("tipster")) {
            funnies = (int) Math.ceil(Integer.parseInt(quota2.getText().toString()) * crediti * 1.10f + 1);
        }

        vincita.setText("VINCI " + funnies + " funnies!");
    }

    public void scommetti(View view) throws ParseException {
        if (controllointernet()) {
            GregorianCalendar oggi = new GregorianCalendar();
            int annoAttuale = oggi.get(Calendar.YEAR);
            int meseAttuale = oggi.get(Calendar.MONTH) + 1;
            int giornoAttuale = oggi.get(Calendar.DAY_OF_MONTH);
            int oraAttuale = oggi.get(Calendar.HOUR_OF_DAY);
            int minutiAttuale = oggi.get(Calendar.MINUTE);
            String dataoggi = annoAttuale + "" + meseAttuale + giornoAttuale + oraAttuale + minutiAttuale;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm"); //Dichiarazione dell'oggetto che rappresenta il formato della data che andremo a usare nel confronto. Quando andremo a inserire i valori dovremo rispettare tale formato
            Date now = sdf.parse(annoAttuale + "-" + meseAttuale + "-" + giornoAttuale + " " + oraAttuale + "." + minutiAttuale);


            int anno = Integer.parseInt(match.getData().substring(0, 4));
            int mese = Integer.parseInt(match.getData().substring(5, 7));
            int giorno = Integer.parseInt(match.getData().substring(8, 10));
            int ora = match.getOre();
            int minuti = match.getMinuti();
            int id = match.getId();

            Date match = sdf.parse(anno + "-" + mese + "-" + giorno + " " + ora + "." + minuti);

            if (match.compareTo(now) > 0) {

                RegistraScommessa rs = new RegistraScommessa(this);
                rs.execute(loggato.getUsername(), "" + id, "" + prono, "" + scelta, "" + crediti, "0" + funnies);
                rs.setOnFinishListener(new RegistraScommessa.onFinishListener() {
                    @Override
                    public void onFinish(String result) {
                        if (result.contains("java.net.ConnectException")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.UnsupportedEncoding")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.Protocol")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.MalformedUrl")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.IO")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.SocketTimeoutException")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                            //stampare risultato asynctask
                        }
                        if (result.contains("Scommessa")) {
                            errorpwd = "andata";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                        }
                        if (result.contains("Problemi")) {
                            errorpwd = "boh";
                            segnalaerrori = true;
                            RegistraScommessa.asydata = true;
                            return;
                        }
                        RegistraScommessa.asydata = true;
                    }
                });
                while (RegistraScommessa.asydata == false) {
                    if (segnalaerrori == true) {
                        toastview(errorpwd);
                    }
                }
                segnalaerrori = false;
                RegistraScommessa.asydata = false;


                AggiornaSaldo as = new AggiornaSaldo(this);
                as.execute(loggato.getUsername(), "" + crediti);
                as.setOnFinishListener(new AggiornaSaldo.onFinishListener() {
                    @Override
                    public void onFinish(String result) {
                        if (result.contains("java.net.ConnectException")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AggiornaSaldo.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.UnsupportedEncoding")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AggiornaSaldo.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.Protocol")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AggiornaSaldo.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.MalformedUrl")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AggiornaSaldo.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.IO")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            AggiornaSaldo.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.SocketTimeoutException")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            AggiornaSaldo.asydata = true;
                            return;
                            //stampare risultato asynctask
                        }
                        AggiornaSaldo.asydata = true;
                    }
                });
                while (AggiornaSaldo.asydata == false) {
                    if (segnalaerrori == true) {
                        toastview(errorpwd);
                    }
                }
                segnalaerrori = false;
                AggiornaSaldo.asydata = false;


            }
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastpartitanondisponibile, null);
                Toast toast = Toast.makeText(PaginaScommetteUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("UTENTE", loggato);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}

