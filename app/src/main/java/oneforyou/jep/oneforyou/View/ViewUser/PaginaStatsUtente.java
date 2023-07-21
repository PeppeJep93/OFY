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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Scommesse;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.CustomAdapterScommesse;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.Control.PrendiPartita;
import oneforyou.jep.oneforyou.Control.PrendiScommesseUtente;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaStatsUtente extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private RelativeLayout barramenu, voce1, voce2, voce3, voce4;
    private boolean segnalaerrori;
    private String errorpwd;
    private TextView nVinte, nPerse, nAttive, nTutte, nNulle;
    private int scvinte = 0, scperse = 0, scnulle = 0, scattive = 0, sctutte = 0;
    private ArrayList<Scommesse> scommesse = new ArrayList<>();
    private ArrayList<Scommesse> apgs = new ArrayList<>();
    static public ArrayList<Partite> apgp = new ArrayList<>();
    static public ArrayList<Partite> partite = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutstatsutente);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

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
        RelativeLayout scommesseutente = (RelativeLayout) findViewById(R.id.scommesseutente);

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
            voce2.setBackgroundResource(R.color.azzurroc);
            utente.setBackgroundResource(R.color.azzurro);
            scommesseutente.setBackgroundResource(R.color.azzurro);
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce2.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
            scommesseutente.setBackgroundResource(R.color.blu);
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            voce2.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
            scommesseutente.setBackgroundResource(R.color.giallo);
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            voce2.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
            scommesseutente.setBackgroundResource(R.color.rosso);
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            voce2.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
            scommesseutente.setBackgroundResource(R.color.verde);
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            voce2.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
            scommesseutente.setBackgroundResource(R.color.granata);
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce2.setBackgroundResource(R.color.arancionec);
            utente.setBackgroundResource(R.color.arancione);
            scommesseutente.setBackgroundResource(R.color.arancione);
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce2.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
            scommesseutente.setBackgroundResource(R.color.bianco);
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce2.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
            scommesseutente.setBackgroundResource(R.color.nero);
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce2.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
            scommesseutente.setBackgroundResource(R.color.viola);
        }

        scvinte = 0;
        scperse = 0;
        scnulle = 0;
        scattive = 0;
        sctutte = 0;

        //colore e foto
        if (controllointernet()) {
            creaListaScommesse();
        }
    }

    private void creaListaScommesse() {
        PrendiScommesseUtente ab = new PrendiScommesseUtente(this);
        ab.execute(loggato.getUsername());
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

        for (int j = 0; j<scommesse.size(); j++)
        {
            if (scommesse.get(j).getStato().equalsIgnoreCase("vinta"))
                scvinte++;
            if (scommesse.get(j).getStato().equalsIgnoreCase("persa"))
                scperse++;
            if (scommesse.get(j).getStato().equalsIgnoreCase("attesa"))
                scattive++;
            if (scommesse.get(j).getStato().equalsIgnoreCase("nulla"))
                scnulle++;
        }

        sctutte = scvinte + scperse + scattive + scnulle;

        nVinte = (TextView) findViewById(R.id.nVinte);
        nPerse = (TextView) findViewById(R.id.nperse);
        nAttive = (TextView) findViewById(R.id.nattive);
        nTutte = (TextView) findViewById(R.id.ntutte);
        nNulle = (TextView) findViewById(R.id.nNulle);

        nVinte.setText(""+ scvinte);
        nPerse.setText(""+ scperse);
        nAttive.setText(""+ scattive);
        nTutte.setText(""+ sctutte);
        nNulle.setText(""+ scnulle);

        scvinte = 0;
        sctutte = 0;
        scperse = 0;
        scattive = 0;
        scnulle = 0;

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
                        Log.d("OFY", "SONO DENTRO - DATI");
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

        apgp = new ArrayList<>(partite);
        creaListView(scommesse, partite);
    }

    private void creaListView(final ArrayList<Scommesse> listas, final ArrayList<Partite> listap) {
        final ListView listView = (ListView) findViewById(R.id.listviewScommesse);

        if (listas.size() > 0) {

            CustomAdapterScommesse customAdapter = new CustomAdapterScommesse(this, R.layout.elementoscommesse, R.id.squadra1, new ArrayList<Scommesse>());


            listView.setAdapter(customAdapter);

            listView.setAdapter(new CustomAdapterScommesse(this,R.layout.elementoscommesse, R.id.squadra1, listas));

            customAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Partite p = listap.get(position);
                    Scommesse s = listas.get(position);
                    Intent intent = new Intent(getApplicationContext(), PaginaDettaglioScommessa.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("UTENTE", loggato);
                    bundle.putSerializable("PARTITA", p);
                    bundle.putSerializable("SCOMMESSA", s);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnoncisonoscommesse, null);
            Toast toast = Toast.makeText(PaginaStatsUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            TextView zeroscommesse = (TextView) findViewById(R.id.zeroscommesse);
            zeroscommesse.setVisibility(View.VISIBLE);
            zeroscommesse.setText("PIAZZA UNA SCOMMESSA, DAI!");
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaStatsUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaStatsUtente.this, "message", Toast.LENGTH_LONG);
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
            case R.id.voce1:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
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
            case R.id.voce1a:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
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

    public void vediVinte(View view) {
        if (Integer.parseInt(nVinte.getText().toString()) > 0) {
            apgp.clear();
            apgs.clear();
            for (int i=0; i<scommesse.size(); i++)
            {
                if (scommesse.get(i).getStato().equalsIgnoreCase("Vinta")){
                    apgs.add(scommesse.get(i));
                    apgp.add(partite.get(i));
                }
            }
            creaListView(apgs, apgp);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnientescommessavinta, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            apgp = new ArrayList<>(partite);
            apgs = new ArrayList<>(scommesse);
            creaListView(scommesse, partite);
        }
    }

    public void vediNulle(View view) {
        if (Integer.parseInt(nNulle.getText().toString()) > 0) {
            apgp.clear();
            apgs.clear();
            for (int i=0; i<scommesse.size(); i++)
            {
                if (scommesse.get(i).getStato().equalsIgnoreCase("Nulla")){
                    apgs.add(scommesse.get(i));
                    apgp.add(partite.get(i));
                }
            }
            creaListView(apgs, apgp);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnientescommessanulla, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            apgp = new ArrayList<>(partite);
            apgs = new ArrayList<>(scommesse);
            creaListView(scommesse, partite);
        }
    }

    public void vediPerse(View view) {
        if (Integer.parseInt(nPerse.getText().toString()) > 0) {
            apgp.clear();
            apgs.clear();
            for (int i=0; i<scommesse.size(); i++)
            {
                if (scommesse.get(i).getStato().equalsIgnoreCase("Persa")){
                    apgs.add(scommesse.get(i));
                    apgp.add(partite.get(i));
                }
            }
            creaListView(apgs, apgp);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnientescommessapersa, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            apgp = new ArrayList<>(partite);
            apgs = new ArrayList<>(scommesse);
            creaListView(scommesse, partite);
        }
    }

    public void vediAttive(View view) {
        if (Integer.parseInt(nAttive.getText().toString()) > 0) {
            apgp.clear();
            apgs.clear();
            for (int i=0; i<scommesse.size(); i++)
            {
                if (scommesse.get(i).getStato().equalsIgnoreCase("Attesa")){
                    apgs.add(scommesse.get(i));
                    apgp.add(partite.get(i));
                }
            }
            creaListView(apgs, apgp);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnientescommessaattiva, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            apgp = new ArrayList<>(partite);
            apgs = new ArrayList<>(scommesse);
            creaListView(scommesse, partite);
        }
    }

    public void vediTutte(View view) {
        if (Integer.parseInt(nTutte.getText().toString()) > 0) {
            apgp = new ArrayList<>(partite);
            apgs = new ArrayList<>(scommesse);
            creaListView(scommesse, partite);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnientescommessa, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
    }
}

