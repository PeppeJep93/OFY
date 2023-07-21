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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Scommesse;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.CustomAdapterPartite;
import oneforyou.jep.oneforyou.Control.NuovePartite;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.Control.RecuperaScommesse;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaGiocaUtente extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private RelativeLayout barramenu, voce1, voce2, voce3, voce4;
    private boolean segnalaerrori;
    private String errorpwd;
    private ArrayList<Partite> partite = new ArrayList<>();
    private ArrayList<Scommesse> scommesse = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutgiocautente);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

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

        if (controllointernet())
        {
            creaListaPartite();
        }

        //colore e foto
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void creaListaPartite() {
        RecuperaScommesse ab = new RecuperaScommesse(this);
        ab.execute(loggato.getUsername());
        ab.setOnFinishListener(new RecuperaScommesse.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaScommesse.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaScommesse.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaScommesse.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    RecuperaScommesse.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    RecuperaScommesse.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    RecuperaScommesse.asydata = true;
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
                RecuperaScommesse.asydata = true;
            }
        });
        while (RecuperaScommesse.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        RecuperaScommesse.asydata = false;



        NuovePartite cd = new NuovePartite(this);
        cd.execute();
        cd.setOnFinishListener(new NuovePartite.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    NuovePartite.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    NuovePartite.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    NuovePartite.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    NuovePartite.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    NuovePartite.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    NuovePartite.asydata = true;
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

                            GregorianCalendar oggi = new GregorianCalendar();
                            int annoAttuale = oggi.get(Calendar.YEAR);
                            int meseAttuale = oggi.get(Calendar.MONTH)+1;
                            int giornoAttuale = oggi.get(Calendar.DAY_OF_MONTH);
                            int oraAttuale = oggi.get(Calendar.HOUR_OF_DAY);
                            int minutiAttuale = oggi.get(Calendar.MINUTE);
                            String dataoggi = annoAttuale + "" + meseAttuale + giornoAttuale + oraAttuale + minutiAttuale;

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm"); //Dichiarazione dell'oggetto che rappresenta il formato della data che andremo a usare nel confronto. Quando andremo a inserire i valori dovremo rispettare tale formato
                            Date now = sdf.parse(annoAttuale + "-" + meseAttuale + "-" + giornoAttuale + " " + oraAttuale + "." + minutiAttuale);


                            int anno = Integer.parseInt(data.substring(0, 4));
                            int mese = Integer.parseInt(data.substring(5, 7));
                            int giorno = Integer.parseInt(data.substring(8, 10));

                            Date match = sdf.parse(anno + "-" + mese + "-" + giorno + " " + ora + "." + minuti);

                            if (match.compareTo(now) > 0) {
                                int trovata = 0;
                                for (int o = 0; o < scommesse.size(); o++) {
                                    if (scommesse.get(o).getPartita() == id)
                                        trovata = 1;
                                }
                                if (trovata == 0) {
                                    Partite partitatrovata = new Partite(id, casa, golCasa, golOspite, ospite, data, ora, minuti, coloreCasa, coloreOspite, esito, uno, ics, due, consiglio);
                                    partite.add(partitatrovata);
                                }
                                trovata = 0;
                            }
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                NuovePartite.asydata = true;
            }
        });
        while (NuovePartite.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        NuovePartite.asydata = false;

        creaListView();
    }

    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaGiocaUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaGiocaUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
    }

    private void creaListView() {
        final ListView listView = (ListView) findViewById(R.id.listviewPartite);

        if (partite.size() > 0) {

            CustomAdapterPartite customAdapter = new CustomAdapterPartite(this, R.layout.elementopartite, R.id.datapartita, new ArrayList<Partite>());


            listView.setAdapter(customAdapter);

            listView.setAdapter(new CustomAdapterPartite(this,R.layout.elementopartite, R.id.datapartita, partite));

            customAdapter.notifyDataSetChanged();


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Partite u = partite.get(position);
                    GregorianCalendar oggi = new GregorianCalendar();
                    int annoAttuale = oggi.get(Calendar.YEAR);
                    int meseAttuale = oggi.get(Calendar.MONTH)+1;
                    int giornoAttuale = oggi.get(Calendar.DAY_OF_MONTH);
                    int oraAttuale = oggi.get(Calendar.HOUR_OF_DAY);
                    int minutiAttuale = oggi.get(Calendar.MINUTE);
                    String dataoggi = annoAttuale + "" + meseAttuale + giornoAttuale + oraAttuale + minutiAttuale;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm"); //Dichiarazione dell'oggetto che rappresenta il formato della data che andremo a usare nel confronto. Quando andremo a inserire i valori dovremo rispettare tale formato
                    Date now = null;
                    try {
                        now = sdf.parse(annoAttuale + "-" + meseAttuale + "-" + giornoAttuale + " " + oraAttuale + "." + minutiAttuale);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    int anno = Integer.parseInt(u.getData().substring(0, 4));
                    int mese = Integer.parseInt(u.getData().substring(5, 7));
                    int giorno = Integer.parseInt(u.getData().substring(8, 10));

                    Date match = null;
                    try {
                        match = sdf.parse(anno + "-" + mese + "-" + giorno + " " + u.getOre() + "." + u.getMinuti());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (match.compareTo(now) > 0) {
                        Intent intent = new Intent(getApplicationContext(), PaginaScommetteUtente.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("UTENTE", loggato);
                        bundle.putSerializable("PARTITA", u);
                        bundle.putString("COLORE", loggato.getColore());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    if (match.compareTo(now) <= 0) {
                        View toastView = getLayoutInflater().inflate(R.layout.toastpartitanondisponibile, null);
                        Toast toast = Toast.makeText(PaginaGiocaUtente.this, "message", Toast.LENGTH_LONG);
                        toast.setView(toastView);
                        toast.show();
                        partite.clear();
                        creaListaPartite();
                        creaListView();
                    }
                }
            });
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnoncisonopartite, null);
            Toast toast = Toast.makeText(PaginaGiocaUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            TextView zeropartite = (TextView) findViewById(R.id.zeropartite);
            zeropartite.setVisibility(View.VISIBLE);
            zeropartite.setText("NON CI SONO PARTITE DISPONIBILI AL MOMENTO!");
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
}

