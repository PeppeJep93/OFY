package oneforyou.jep.oneforyou.View.ViewUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import oneforyou.jep.oneforyou.Control.AcquistaBuono;
import oneforyou.jep.oneforyou.Model.Premi;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.CustomAdapterCoupon;
import oneforyou.jep.oneforyou.Control.PagaCoupon;
import oneforyou.jep.oneforyou.Control.PrelevaCoupon;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.Control.PrendiSaldo;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaPremiUtente extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private RelativeLayout barramenu, voce1, voce2, voce3, voce4;
    private boolean segnalaerrori;
    private String errorpwd;
    private ArrayList<Premi> premi = new ArrayList<>();
    private int denari, comprato = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutpremiutente);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        barramenu = findViewById(R.id.barramenu);
        voce1 = findViewById(R.id.voce1);
        voce2 = findViewById(R.id.voce2);
        voce3 = findViewById(R.id.voce3);
        voce4 = findViewById(R.id.voce4);

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
            voce4.setBackgroundResource(R.color.azzurroc);
            utente.setBackgroundResource(R.color.azzurro);
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce4.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            voce4.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            voce4.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            voce4.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            voce4.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce4.setBackgroundResource(R.color.arancionec);
            utente.setBackgroundResource(R.color.arancione);
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce4.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce4.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce4.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
        }
        //colore e foto
        creaListaCoupon();
    }

    private void creaListaCoupon() {
        PrelevaCoupon ab = new PrelevaCoupon(this);
        ab.execute(loggato.getUsername());
        ab.setOnFinishListener(new PrelevaCoupon.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrelevaCoupon.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrelevaCoupon.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrelevaCoupon.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    PrelevaCoupon.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    PrelevaCoupon.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    PrelevaCoupon.asydata = true;
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
                            String utente = partita.getString("Utente");
                            String servizio = partita.getString("Servizio");
                            int valore = partita.getInt("Valore");
                            int costo = partita.getInt("Costo");
                            String codice = partita.getString("Codice");
                            String data = partita.getString("DataAcquisto");
                            Premi gift = new Premi(utente, servizio, valore, costo, codice, data);
                            premi.add(gift);
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PrelevaCoupon.asydata = true;
            }
        });
        while (PrelevaCoupon.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        PrelevaCoupon.asydata = false;

        creaListView(premi);
    }

    private void creaListView(final ArrayList<Premi> lista) {
        final ListView listView = (ListView) findViewById(R.id.listviewCoupon);

        if (lista.size() > 0) {

            CustomAdapterCoupon customAdapter = new CustomAdapterCoupon(this, R.layout.elementocoupon, R.id.servizio, new ArrayList<Premi>());

            listView.setAdapter(customAdapter);

            listView.setAdapter(new CustomAdapterCoupon(this, R.layout.elementocoupon, R.id.servizio, lista));

            customAdapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Premi p = lista.get(position);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", p.getCodice());
                    clipboard.setPrimaryClip(clip);
                    View toastView = getLayoutInflater().inflate(R.layout.toastcodicecopiato, null);
                    Toast toast = Toast.makeText(PaginaPremiUtente.this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                }
            });
        } else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnoncisonocoupon, null);
            Toast toast = Toast.makeText(PaginaPremiUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            TextView zerocoupon = (TextView) findViewById(R.id.zerocoupon);
            zerocoupon.setVisibility(View.VISIBLE);
            zerocoupon.setText("NON HAI EFFETTUATO ALCUN ACQUISTO SINORA!");
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
        if (error.equals("probserver")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaPremiUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaPremiUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("boh")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastnoncomprabuono, null);
            Toast toast = Toast.makeText(PaginaPremiUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("andata")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastacquistatobuono, null);
            Toast toast = Toast.makeText(PaginaPremiUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            /* crea Premi */
            /* aggiunta in cima alla lista*/
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
            case R.id.voce2:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
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
            case R.id.voce2a:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
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

    public void compraGift(View view) {
        TextView prezzo;
        Button funnies;
        String apg, codice;
        int valore = 0, costo = 0;
        GregorianCalendar oggi = new GregorianCalendar();
        int annoAttuale = oggi.get(Calendar.YEAR);
        int meseAttuale = oggi.get(Calendar.MONTH) + 1;
        int giornoAttuale = oggi.get(Calendar.DAY_OF_MONTH);
        String giorno, mese;

        if (giornoAttuale < 10)
            giorno = "0" + giornoAttuale;
        else
            giorno = giornoAttuale + "";

        if (meseAttuale < 10)
            mese = "0" + meseAttuale;
        else
            mese = meseAttuale + "";

        String data = annoAttuale + "-" + mese + "-" + giorno;

        Premi premio = null;

        codice = generaCodice();


        if (view.getId() == R.id.netflixfunnies) {
            prezzo = (TextView) findViewById(R.id.netflixprezzo);
            funnies = (Button) findViewById(R.id.netflixfunnies);
            apg = prezzo.getText().toString().substring(0, prezzo.getText().toString().length()-4);
            valore = Integer.parseInt(apg);
            costo = Integer.parseInt(funnies.getText().toString());

            if (loggato.getSaldo() >= costo) {
                comprato = costo;
                premio = new Premi(loggato.getUsername(), "Netflix", valore, costo, codice, data);
                AcquistaBuono ab = new AcquistaBuono(this);
                ab.execute(codice, loggato.getUsername(), "Netflix", data, "" + valore, "" + costo);
                ab.setOnFinishListener(new AcquistaBuono.onFinishListener() {
                    @Override
                    public void onFinish(String result) {
                        if (result.contains("java.net.ConnectException")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AcquistaBuono.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.UnsupportedEncoding")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AcquistaBuono.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.Protocol")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AcquistaBuono.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.MalformedUrl")) {
                            errorpwd = "probserver";
                            segnalaerrori = true;
                            AcquistaBuono.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.IO")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            AcquistaBuono.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.SocketTimeoutException")) {
                            errorpwd = "pceserverspento";
                            segnalaerrori = true;
                            AcquistaBuono.asydata = true;
                            return;
                            //stampare risultato asynctask
                        }
                        if (result.contains("Registrato")) {
                            errorpwd = "andata";
                            segnalaerrori = true;
                            AcquistaBuono.asydata = true;
                            return;
                        }
                        if (result.contains("Problemi")) {
                            errorpwd = "boh";
                            segnalaerrori = true;
                            comprato = -1;
                            AcquistaBuono.asydata = true;
                            return;
                        }
                        AcquistaBuono.asydata = true;
                    }
                });
                while (AcquistaBuono.asydata == false) {
                    if (segnalaerrori == true) {
                        toastview(errorpwd);
                    }
                }
                segnalaerrori = false;
                AcquistaBuono.asydata = false;
            } else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnonhaicrediti, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
        }


            if (view.getId() == R.id.spotifyfunnies) {
                prezzo = (TextView) findViewById(R.id.spotifyprezzo);
                funnies = (Button) findViewById(R.id.spotifyfunnies);
                apg = prezzo.getText().toString().substring(0, prezzo.getText().toString().length()-4);
                valore = Integer.parseInt(apg);
                costo = Integer.parseInt(funnies.getText().toString());

                if (loggato.getSaldo() >= costo) {
                    comprato = costo;
                    premio = new Premi(loggato.getUsername(), "Spotify", valore, costo, codice, data);
                    AcquistaBuono ab = new AcquistaBuono(this);
                    ab.execute(codice, loggato.getUsername(), "Spotify", data, "" + valore, "" + costo);
                    ab.setOnFinishListener(new AcquistaBuono.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AcquistaBuono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AcquistaBuono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AcquistaBuono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                AcquistaBuono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AcquistaBuono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                AcquistaBuono.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            if (result.contains("Registrato")) {
                                errorpwd = "andata";
                                segnalaerrori = true;
                                AcquistaBuono.asydata = true;
                                return;
                            }
                            if (result.contains("Problemi")) {
                                errorpwd = "boh";
                                segnalaerrori = true;
                                comprato = -1;
                                AcquistaBuono.asydata = true;
                                return;
                            }
                            AcquistaBuono.asydata = true;
                        }
                    });
                    while (AcquistaBuono.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    AcquistaBuono.asydata = false;
                } else {
                    View toastView = getLayoutInflater().inflate(R.layout.toastnonhaicrediti, null);
                    Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                }
            }


                if (view.getId() == R.id.nowtvfunnies) {
                    prezzo = (TextView) findViewById(R.id.nowtvprezzo);
                    funnies = (Button) findViewById(R.id.nowtvfunnies);
                    apg = prezzo.getText().toString().substring(0, prezzo.getText().toString().length()-4);
                    valore = Integer.parseInt(apg);
                    costo = Integer.parseInt(funnies.getText().toString());

                    if (loggato.getSaldo() >= costo) {
                        comprato = costo;
                        premio = new Premi(loggato.getUsername(), "NowTv", valore, costo, codice, data);
                        AcquistaBuono ab = new AcquistaBuono(this);
                        ab.execute(codice, loggato.getUsername(), "NowTv", data, "" + valore, "" + costo);
                        ab.setOnFinishListener(new AcquistaBuono.onFinishListener() {
                            @Override
                            public void onFinish(String result) {
                                if (result.contains("java.net.ConnectException")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.UnsupportedEncoding")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.Protocol")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.MalformedUrl")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.IO")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.SocketTimeoutException")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                    //stampare risultato asynctask
                                }
                                if (result.contains("Registrato")) {
                                    errorpwd = "andata";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("Problemi")) {
                                    errorpwd = "boh";
                                    segnalaerrori = true;
                                    comprato = -1;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                AcquistaBuono.asydata = true;
                            }
                        });
                        while (AcquistaBuono.asydata == false) {
                            if (segnalaerrori == true) {
                                toastview(errorpwd);
                            }
                        }
                        segnalaerrori = false;
                        AcquistaBuono.asydata = false;
                    } else {
                        View toastView = getLayoutInflater().inflate(R.layout.toastnonhaicrediti, null);
                        Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                        toast.setView(toastView);
                        toast.show();
                    }
                }



                if (view.getId() == R.id.amazonfunnies) {
                    prezzo = (TextView) findViewById(R.id.amazonprezzo);
                    funnies = (Button) findViewById(R.id.amazonfunnies);
                    apg = prezzo.getText().toString().substring(0, prezzo.getText().toString().length()-4);
                    valore = Integer.parseInt(apg);
                    costo = Integer.parseInt(funnies.getText().toString());

                    if (loggato.getSaldo() >= costo) {
                        comprato = costo;
                        premio = new Premi(loggato.getUsername(), "Amazon", valore, costo, codice, data);
                        AcquistaBuono ab = new AcquistaBuono(this);
                        ab.execute(codice, loggato.getUsername(), "Amazon", data, "" + valore, "" + costo);
                        ab.setOnFinishListener(new AcquistaBuono.onFinishListener() {
                            @Override
                            public void onFinish(String result) {
                                if (result.contains("java.net.ConnectException")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.UnsupportedEncoding")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.Protocol")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.MalformedUrl")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.IO")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.SocketTimeoutException")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                    //stampare risultato asynctask
                                }
                                if (result.contains("Registrato")) {
                                    errorpwd = "andata";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("Problemi")) {
                                    errorpwd = "boh";
                                    segnalaerrori = true;
                                    comprato = -1;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                AcquistaBuono.asydata = true;
                            }
                        });
                        while (AcquistaBuono.asydata == false) {
                            if (segnalaerrori == true) {
                                toastview(errorpwd);
                            }
                        }
                        segnalaerrori = false;
                        AcquistaBuono.asydata = false;
                    } else {
                        View toastView = getLayoutInflater().inflate(R.layout.toastnonhaicrediti, null);
                        Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                        toast.setView(toastView);
                        toast.show();
                    }
                }




                if (view.getId() == R.id.daznfunnies) {
                    prezzo = (TextView) findViewById(R.id.daznprezzo);
                    funnies = (Button) findViewById(R.id.daznfunnies);
                    apg = prezzo.getText().toString().substring(0, prezzo.getText().toString().length()-4);
                    valore = Integer.parseInt(apg);
                    costo = Integer.parseInt(funnies.getText().toString());

                    if (loggato.getSaldo() >= costo) {
                        comprato = costo;
                        premio = new Premi(loggato.getUsername(), "Dazn", valore, costo, codice, data);
                        AcquistaBuono ab = new AcquistaBuono(this);
                        ab.execute(codice, loggato.getUsername(), "Dazn", data, "" + valore, "" + costo);
                        ab.setOnFinishListener(new AcquistaBuono.onFinishListener() {
                            @Override
                            public void onFinish(String result) {
                                if (result.contains("java.net.ConnectException")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.UnsupportedEncoding")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.Protocol")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.MalformedUrl")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.IO")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.SocketTimeoutException")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                    //stampare risultato asynctask
                                }
                                if (result.contains("Registrato")) {
                                    errorpwd = "andata";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("Problemi")) {
                                    errorpwd = "boh";
                                    segnalaerrori = true;
                                    comprato = -1;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                AcquistaBuono.asydata = true;
                            }
                        });
                        while (AcquistaBuono.asydata == false) {
                            if (segnalaerrori == true) {
                                toastview(errorpwd);
                            }
                        }
                        segnalaerrori = false;
                        AcquistaBuono.asydata = false;
                    } else {
                        View toastView = getLayoutInflater().inflate(R.layout.toastnonhaicrediti, null);
                        Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                        toast.setView(toastView);
                        toast.show();
                    }
                }



                if (view.getId() == R.id.playstorefunnies) {
                    prezzo = (TextView) findViewById(R.id.playstoreprezzo);
                    funnies = (Button) findViewById(R.id.playstorefunnies);
                    apg = prezzo.getText().toString().substring(0, prezzo.getText().toString().length()-4);
                    valore = Integer.parseInt(apg);
                    costo = Integer.parseInt(funnies.getText().toString());

                    if (loggato.getSaldo() >= costo) {
                        comprato = costo;
                        premio = new Premi(loggato.getUsername(), "PlayStore", valore, costo, codice, data);
                        AcquistaBuono ab = new AcquistaBuono(this);
                        ab.execute(codice, loggato.getUsername(), "PlayStore", data, "" + valore, "" + costo);
                        ab.setOnFinishListener(new AcquistaBuono.onFinishListener() {
                            @Override
                            public void onFinish(String result) {
                                if (result.contains("java.net.ConnectException")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.UnsupportedEncoding")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.Protocol")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.MalformedUrl")) {
                                    errorpwd = "probserver";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.IO")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("java.net.SocketTimeoutException")) {
                                    errorpwd = "pceserverspento";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                    //stampare risultato asynctask
                                }
                                if (result.contains("Registrato")) {
                                    errorpwd = "andata";
                                    segnalaerrori = true;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                if (result.contains("Problemi")) {
                                    errorpwd = "boh";
                                    segnalaerrori = true;
                                    comprato = -1;
                                    AcquistaBuono.asydata = true;
                                    return;
                                }
                                AcquistaBuono.asydata = true;
                            }
                        });
                        while (AcquistaBuono.asydata == false) {
                            if (segnalaerrori == true) {
                                toastview(errorpwd);
                            }
                        }
                        segnalaerrori = false;
                        AcquistaBuono.asydata = false;
                    } else {
                        View toastView = getLayoutInflater().inflate(R.layout.toastnonhaicrediti, null);
                        Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                        toast.setView(toastView);
                        toast.show();
                    }
                }

                if (comprato > -1){
                    PagaCoupon as = new PagaCoupon(this);
                    as.execute(loggato.getUsername(), "" + comprato);
                    as.setOnFinishListener(new PagaCoupon.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PagaCoupon.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PagaCoupon.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PagaCoupon.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PagaCoupon.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                PagaCoupon.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                PagaCoupon.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            PagaCoupon.asydata = true;
                        }
                    });
                    while (PagaCoupon.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    PagaCoupon.asydata = false;

                    if(premi != null)
                    {
                        premi.add(0 , premio);
                        creaListView(premi);
                    }

                    PrendiSaldo ps = new PrendiSaldo(this);
                    ps.execute(loggato.getUsername());
                    ps.setOnFinishListener(new PrendiSaldo.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PrendiSaldo.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PrendiSaldo.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PrendiSaldo.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                PrendiSaldo.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                PrendiSaldo.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                PrendiSaldo.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            if (Integer.parseInt(result) > 0) {
                                errorpwd = "";
                                denari = Integer.parseInt(result);
                                segnalaerrori = true;
                                PrendiSaldo.asydata = true;
                                return;
                            }
                            PrendiSaldo.asydata = true;
                        }
                    });
                    while (PrendiSaldo.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    PrendiSaldo.asydata = false;

                    TextView zerocoupon = (TextView) findViewById(R.id.zerocoupon);
                    zerocoupon.setVisibility(View.GONE);

                    TextView soldi = (TextView) findViewById(R.id.saldo);
                    soldi.setText("" + denari + "◍ funnies");
                    loggato.setSaldo(denari);

                }
            }

    private String generaCodice() {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
