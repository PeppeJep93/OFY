package oneforyou.jep.oneforyou.View.ViewAdmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oneforyou.jep.oneforyou.Control.AggiornaEsito;
import oneforyou.jep.oneforyou.Control.AggiornaPunteggio;
import oneforyou.jep.oneforyou.Control.AggiornaScommesse;
import oneforyou.jep.oneforyou.Control.AggiornaStatistiche;
import oneforyou.jep.oneforyou.Control.AggiungiCrediti;
import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Scommesse;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Control.ComunicazioneEsito;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Control.ControllaGiocate;
import oneforyou.jep.oneforyou.Util.CustomAdapterPubblica;
import oneforyou.jep.oneforyou.Control.EstrapolaUtenti;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.Control.RecuperaPartiteFinite;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class
PaginaPubblicaRisultati extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private RelativeLayout barramenu, voce1, voce2;
    private Button voce1a, voce2a;
    private boolean segnalaerrori;
    private String errorpwd;
    private ArrayList<Partite> partite = new ArrayList<>();
    private CustomAdapterPubblica customAdapter;
    private ListView listView;
    private ArrayList<Scommesse> scommesse = new ArrayList<>();
    private Utenti utentetrovato;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutpubblicarisultati);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        barramenu = findViewById(R.id.barramenusotto);

        voce1 = findViewById(R.id.buttonutenti1);
        voce2 = findViewById(R.id.buttonpartite1);
        voce1a = findViewById(R.id.buttonutenti2);
        voce2a = findViewById(R.id.buttonpartite2);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        RelativeLayout utente = (RelativeLayout) findViewById(R.id.utente);

        final ImageView immagine = (ImageView) findViewById(R.id.immagine);

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

        String col = loggato.getColore();
        String fot = loggato.getFoto();
        String us = loggato.getUsername();
        TextView username = (TextView) findViewById(R.id.username);
        username.setText(us);

        if (col.toLowerCase().contains("azzurro")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.azzurros);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.azzurro);
            barramenu.setBackgroundResource(R.color.azzurros);
            voce2.setBackgroundResource(R.color.azzurroc);
        }

        if (col.toLowerCase().contains("blu")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.blus);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.blu);
            barramenu.setBackgroundResource(R.color.blus);
            voce2.setBackgroundResource(R.color.bluc);
        }

        if (col.toLowerCase().contains("giallo")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.giallos);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.giallo);
            barramenu.setBackgroundResource(R.color.giallos);
            voce2.setBackgroundResource(R.color.gialloc);
        }

        if (col.toLowerCase().contains("rosso")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.rossos);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.rosso);
            barramenu.setBackgroundResource(R.color.rossos);
            voce2.setBackgroundResource(R.color.rossoc);
        }

        if (col.toLowerCase().contains("verde")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.verdes);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.verde);
            barramenu.setBackgroundResource(R.color.verdes);
            voce2.setBackgroundResource(R.color.verdec);
        }

        if (col.toLowerCase().contains("granata")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.granatas);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.granata);
            barramenu.setBackgroundResource(R.color.granatas);
            voce2.setBackgroundResource(R.color.granatac);
        }

        if (col.toLowerCase().contains("arancione")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.aranciones);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.arancione);
            barramenu.setBackgroundResource(R.color.aranciones);
            voce2.setBackgroundResource(R.color.arancionec);
        }

        if (col.toLowerCase().contains("bianco")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.biancos);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.bianco);
            barramenu.setBackgroundResource(R.color.biancos);
            voce2.setBackgroundResource(R.color.biancoc);
        }

        if (col.toLowerCase().contains("nero")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.neros);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.nero);
            barramenu.setBackgroundResource(R.color.neros);
            voce2.setBackgroundResource(R.color.neroc);
        }



        PrendiFoto pf = new PrendiFoto(this);
        pf.execute(Connector.db + "img/" + fot);
        pf.setOnFinishListener(new PrendiFoto.onFinishListener() {
            @Override
            public void onFinish(String res) {
                // The task has finished and you can now use the result
                try{
                    byte [] encodeByte = Base64.decode(res,Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    immagine.setImageBitmap(bitmap);
                    //immagine.setBackground(getResources().getDrawable(R.drawable.editcentro));
                }
                catch(Exception e){
                    e.getMessage();
                }
            }
        });
        creaListaPartite();
    }

    private void creaListaPartite(){
        RecuperaPartiteFinite cd = new RecuperaPartiteFinite(this);
        cd.execute();
        cd.setOnFinishListener(new RecuperaPartiteFinite.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaPartiteFinite.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaPartiteFinite.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaPartiteFinite.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaPartiteFinite.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    RecuperaPartiteFinite.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    RecuperaPartiteFinite.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                try {
                    JSONObject jOResult = new JSONObject(result);
                    int success = jOResult.getInt("success");
                    if (success == 1) {
                        JSONArray datipartite;
                        datipartite = jOResult.getJSONArray("datipartite");
                        for (int i = 0; i < datipartite.length(); i++) {
                            JSONObject partita = datipartite.getJSONObject(i);
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

                            Partite partitatrovata = new Partite(id, casa, golCasa, golOspite, ospite, data, ora, minuti, coloreCasa, coloreOspite, esito, uno, ics, due, consiglio);
                            partite.add(partitatrovata);

                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RecuperaPartiteFinite.asydata = true;
            }
        });
        while (RecuperaPartiteFinite.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        RecuperaPartiteFinite.asydata = false;

        creaListView();
    }

    private void creaListView() {
        listView = (ListView) findViewById(R.id.listviewPubblicazione);

        if (partite.size() > 0) {

            /*customAdapter = new CustomAdapterAggiornaElimina(this, R.layout.elementoaggiornamento, R.id.datapartita, new ArrayList<Partite>());



            listView.setAdapter(customAdapter);*/

            customAdapter = new CustomAdapterPubblica(this,R.layout.elementopubblicazione, R.id.datapartita, partite);

            listView.setAdapter(customAdapter);

            customAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    View toastView = getLayoutInflater().inflate(R.layout.toastsceglioperazione, null);
                    Toast toast = Toast.makeText(PaginaPubblicaRisultati.this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                }
            });

        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastpartitenonfinite, null);
            Toast toast = Toast.makeText(PaginaPubblicaRisultati.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            TextView zeropartite = (TextView) findViewById(R.id.zeropartite);
            zeropartite.setVisibility(View.VISIBLE);
            zeropartite.setText("NESSUNA PARTITA AL MOMENTO E' STATA AGGIORNATA!");
        }
    }
    //colore e foto

    public void aggiornaPartita(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        Partite c = customAdapter.getItem(position);
        Intent intent;
        Bundle bundle;
        bundle = new Bundle();
        bundle.putSerializable("UTENTE", loggato);
        bundle.putSerializable("PARTITA", c);
        intent = new Intent(getApplicationContext(), PaginaModificaPartita.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public boolean pubblicaRisultato(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        Partite c = customAdapter.getItem(position);

        View view=listView.getChildAt(position);

        EditText golcasa = view.findViewById(R.id.GolCasa);
        EditText golospite = view.findViewById(R.id.Golospite);
        String esito = "-1";

        if (Integer.parseInt(golcasa.getText().toString()) > Integer.parseInt(golospite.getText().toString())){
            esito = "1";
        }
        if (Integer.parseInt(golcasa.getText().toString()) == Integer.parseInt(golospite.getText().toString())){
            esito = "0";
        }
        if (Integer.parseInt(golcasa.getText().toString()) < Integer.parseInt(golospite.getText().toString())){
            esito = "2";
        }

        if (controllointernet()) {

            AggiornaEsito as = new AggiornaEsito(this);
            as.execute(c.getId() + "", esito);
            as.setOnFinishListener(new AggiornaEsito.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaEsito.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaEsito.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaEsito.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaEsito.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaEsito.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaEsito.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    AggiornaEsito.asydata = true;
                }
            });
            while (AggiornaEsito.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            AggiornaEsito.asydata = false;

            ControllaGiocate ab = new ControllaGiocate(this);
            ab.execute(c.getId() + "");
            ab.setOnFinishListener(new ControllaGiocate.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ControllaGiocate.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ControllaGiocate.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ControllaGiocate.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ControllaGiocate.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ControllaGiocate.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ControllaGiocate.asydata = true;
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
                    ControllaGiocate.asydata = true;
                }
            });
            while (ControllaGiocate.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            ControllaGiocate.asydata = false;


            for (Scommesse s : scommesse) {
                if (s.getPronostico() == Integer.parseInt(esito)) {

                    AggiornaScommesse ap = new AggiornaScommesse(this);
                    ap.execute(loggato.getUsername());
                    ap.setOnFinishListener(new AggiornaScommesse.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            AggiornaScommesse.asydata = true;
                        }
                    });
                    while (AggiornaScommesse.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    AggiornaScommesse.asydata = false;



                    AggiungiCrediti ar = new AggiungiCrediti(this);
                    ar.execute(s.getGiocatore(), s.getVincita() + "");
                    ar.setOnFinishListener(new AggiungiCrediti.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiungiCrediti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiungiCrediti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiungiCrediti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiungiCrediti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiungiCrediti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiungiCrediti.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            AggiungiCrediti.asydata = true;
                        }
                    });
                    while (AggiungiCrediti.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    AggiungiCrediti.asydata = false;


                    ComunicazioneEsito ce = new ComunicazioneEsito(this);
                    ce.execute(s.getCodice() + "", "Vinta");
                    ce.setOnFinishListener(new ComunicazioneEsito.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            ComunicazioneEsito.asydata = true;
                        }
                    });
                    while (ComunicazioneEsito.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    ComunicazioneEsito.asydata = false;


                    EstrapolaUtenti cd = new EstrapolaUtenti(this);
                    cd.execute(s.getGiocatore());
                    cd.setOnFinishListener(new EstrapolaUtenti.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                EstrapolaUtenti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                EstrapolaUtenti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                EstrapolaUtenti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                EstrapolaUtenti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                EstrapolaUtenti.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                EstrapolaUtenti.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            try {
                                JSONObject jOResult = new JSONObject(result);
                                int success = jOResult.getInt("success");
                                if (success == 1) {
                                    JSONArray utentedati;
                                    utentedati = jOResult.getJSONArray("datiutente");
                                    for (int i = 0; i < utentedati.length(); i++) {
                                        JSONObject utente = utentedati.getJSONObject(i);
                                        String nome = utente.getString("nome");
                                        String cognome = utente.getString("cognome");
                                        int sesso = utente.getInt("sesso");
                                        String username = utente.getString("username");
                                        String ruolo = utente.getString("ruolo");
                                        String attivo = utente.getString("attivo");
                                        String colore = utente.getString("colore");
                                        String foto = utente.getString("foto");
                                        String luogo = utente.getString("luogonascita");
                                        int scommesse = utente.getInt("scommesse");
                                        int media = utente.getInt("media");
                                        int seguaci = utente.getInt("seguaci");
                                        int vinte = utente.getInt("vinte");
                                        int recensioni = utente.getInt("recensioni");
                                        int blocco = utente.getInt("blocco");
                                        int saldo = utente.getInt("saldo");
                                        String data = utente.getString("datanascita");
                                        String mail = utente.getString("email");
                                        String tel = utente.getString("telefono");
                                        if ((!username.equalsIgnoreCase(loggato.getUsername())) && (!username.equalsIgnoreCase("staff"))) {
                                            utentetrovato = new Utenti(nome, cognome, mail, tel, data, luogo, sesso, username, "", blocco, attivo, ruolo, saldo, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                                        }
                                    }
                                } else {
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            EstrapolaUtenti.asydata = true;
                        }
                    });
                    while (EstrapolaUtenti.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    EstrapolaUtenti.asydata = false;

                    int vinte = utentetrovato.getVinte() + 1;

                    double calcolomedia = vinte * 100 / utentetrovato.getScommesse();
                    int media = (int) Math.ceil(calcolomedia);

                    AggiornaStatistiche ad = new AggiornaStatistiche(this);
                    ad.execute(s.getGiocatore(), vinte + "", media + "");
                    ad.setOnFinishListener(new AggiornaStatistiche.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            AggiornaStatistiche.asydata = true;
                        }
                    });
                    while (AggiornaStatistiche.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    AggiornaStatistiche.asydata = false;


                } else {


                    AggiornaScommesse ap = new AggiornaScommesse(this);
                    ap.execute(loggato.getUsername());
                    ap.setOnFinishListener(new AggiornaScommesse.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaScommesse.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            AggiornaScommesse.asydata = true;
                        }
                    });
                    while (AggiornaScommesse.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    AggiornaScommesse.asydata = false;



                    ComunicazioneEsito ce = new ComunicazioneEsito(this);
                    ce.execute(s.getCodice() + "", "Persa");
                    ce.setOnFinishListener(new ComunicazioneEsito.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                ComunicazioneEsito.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            ComunicazioneEsito.asydata = true;
                        }
                    });
                    while (ComunicazioneEsito.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    ComunicazioneEsito.asydata = false;

                    int vinte = utentetrovato.getVinte();

                    double calcolomedia = vinte * 100 / utentetrovato.getScommesse();
                    int media = (int) Math.ceil(calcolomedia);

                    AggiornaStatistiche ad = new AggiornaStatistiche(this);
                    ad.execute(s.getGiocatore(), vinte + "", media + "");
                    ad.setOnFinishListener(new AggiornaStatistiche.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AggiornaStatistiche.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            AggiornaStatistiche.asydata = true;
                        }
                    });
                    while (AggiornaStatistiche.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    AggiornaStatistiche.asydata = false;

                }

            }

            View toastView = getLayoutInflater().inflate(R.layout.toastscommessepagate, null);
            Toast toast = Toast.makeText(PaginaPubblicaRisultati.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();

            AggiornaPunteggio ar = new AggiornaPunteggio(this);
            ar.execute(c.getId() + "", golcasa.getText().toString(), golospite.getText().toString());
            ar.setOnFinishListener(new AggiornaPunteggio.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaPunteggio.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaPunteggio.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaPunteggio.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        AggiornaPunteggio.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaPunteggio.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        AggiornaPunteggio.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    AggiornaPunteggio.asydata = true;
                }
            });
            while (AggiornaPunteggio.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            AggiornaPunteggio.asydata = false;

            esito = "";
            scommesse.clear();
            partite.remove(position);
            creaListView();
        }
        return false;
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

    //colore e foto




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

    }

    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaPubblicaRisultati.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaPubblicaRisultati.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
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
            case R.id.buttonutenti1:
                intent = new Intent(getApplicationContext(), PaginaGestioneUtenti.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonutenti2:
                intent = new Intent(getApplicationContext(), PaginaGestioneUtenti.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

}

