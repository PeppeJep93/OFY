package oneforyou.jep.oneforyou.View.ViewUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

import oneforyou.jep.oneforyou.Model.Social;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.CustomAdapterUtenti1;
import oneforyou.jep.oneforyou.Control.EstrapolaAmici;
import oneforyou.jep.oneforyou.Control.EstrapolaFollowers;
import oneforyou.jep.oneforyou.Control.EstrapolaPreferiti;
import oneforyou.jep.oneforyou.Control.EstrapolaRecensioni;
import oneforyou.jep.oneforyou.Control.EstrapolaUtenti;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.Control.VerificaRelazione;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaUtentiUtente extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private RelativeLayout barramenu, voce1, voce2, voce3, voce4;
    private boolean segnalaerrori;
    private int risultatoquery;
    private String errorpwd;
    static public Drawable utentino;
    private ArrayList<Utenti> tutti = new ArrayList<>();
    private ArrayList<Utenti> utenti = new ArrayList<>();
    private ArrayList<Utenti> amici = new ArrayList<>();
    private ArrayList<Utenti> preferiti = new ArrayList<>();
    private ArrayList<Utenti> followers = new ArrayList<>();
    private ArrayList<Utenti> recensioni = new ArrayList<>();
    private EditText cerchiamo;
    private Button casellacerca;
    private TextView nAmici, nFollowers, nRecensioni, nPreferiti;
    private Social legame = null;

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
        setContentView(R.layout.layoututentiutente);

        utentino = getResources().getDrawable(R.drawable.utentino);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        TextView user = (TextView) findViewById(R.id.username);
        user.setTypeface(myfont);
        TextView soldi = (TextView) findViewById(R.id.saldo);
        soldi.setTypeface(myfont);
        cerchiamo = (EditText) findViewById(R.id.scrivicerca);
        nAmici = (TextView) findViewById(R.id.nAmici);
        nFollowers = (TextView) findViewById(R.id.nFollowers);
        nRecensioni = (TextView) findViewById(R.id.nRecensioni);
        nPreferiti = (TextView) findViewById(R.id.nPreferiti);
        casellacerca = (Button) findViewById(R.id.casellacerca);

        cerchiamo.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    casellacerca.performClick();
                    return true;
                }
                return false;
            }
        });




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
        }

        cerchiamo.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 0)
                {
                    casellacerca.setBackgroundResource(R.drawable.cercalente);
                }
                else {
                    casellacerca.setBackgroundResource(R.drawable.noncercalente);
                }

            }
        });

        RelativeLayout utente = (RelativeLayout) findViewById(R.id.utente);
        RelativeLayout socialutente = (RelativeLayout) findViewById(R.id.socialutente);

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
            voce3.setBackgroundResource(R.color.azzurroc);
            utente.setBackgroundResource(R.color.azzurro);
            socialutente.setBackgroundResource(R.color.azzurro);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce3.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
            socialutente.setBackgroundResource(R.color.blu);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            voce3.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
            socialutente.setBackgroundResource(R.color.giallo);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            voce3.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
            socialutente.setBackgroundResource(R.color.rosso);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            voce3.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
            socialutente.setBackgroundResource(R.color.verde);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            voce3.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
            socialutente.setBackgroundResource(R.color.granata);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce3.setBackgroundResource(R.color.arancionec);
            utente.setBackgroundResource(R.color.arancione);
            socialutente.setBackgroundResource(R.color.arancione);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce3.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
            socialutente.setBackgroundResource(R.color.bianco);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.biancos), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce3.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
            socialutente.setBackgroundResource(R.color.nero);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce3.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
            socialutente.setBackgroundResource(R.color.viola);
            cerchiamo.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
        }

        if (controllointernet()) {
            EstrapolaAmici ef = new EstrapolaAmici(this);
            ef.execute(loggato.getUsername());
            ef.setOnFinishListener(new EstrapolaAmici.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaAmici.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaAmici.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaAmici.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaAmici.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaAmici.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaAmici.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    try {
                        utenti.clear();
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
                                int scommesse = utente.getInt("scommesse");
                                int media = utente.getInt("media");
                                int seguaci = utente.getInt("seguaci");
                                int vinte = utente.getInt("vinte");
                                int recensioni = utente.getInt("recensioni");
                                if ((!username.equalsIgnoreCase(loggato.getUsername())) && (!username.equalsIgnoreCase("staff"))) {
                                    Utenti utentetrovato = new Utenti(nome, cognome, "", "", "", "", sesso, username, "", 0, attivo, ruolo, 0, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                                    amici.add(utentetrovato);
                                }
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    EstrapolaAmici.asydata = true;
                }
            });
            while (EstrapolaAmici.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            EstrapolaAmici.asydata = false;

            nAmici.setTypeface(myfontdoppio);
            nAmici.setText("" + amici.size());
        }

        nFollowers.setTypeface(myfontdoppio);
        nFollowers.setText("" + loggato.getSeguaci());

        nRecensioni.setTypeface(myfontdoppio);
        nRecensioni.setText("" + loggato.getRecensioni());

        if (controllointernet()) {
            EstrapolaPreferiti ef = new EstrapolaPreferiti(this);
            ef.execute(loggato.getUsername());
            ef.setOnFinishListener(new EstrapolaPreferiti.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaPreferiti.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaPreferiti.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaPreferiti.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaPreferiti.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaPreferiti.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaPreferiti.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    try {
                        JSONObject jOResult = new JSONObject(result);
                        int success = jOResult.getInt("success");
                        if (success == 1) {
                            utenti.clear();
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
                                int scommesse = utente.getInt("scommesse");
                                int media = utente.getInt("media");
                                int seguaci = utente.getInt("seguaci");
                                int vinte = utente.getInt("vinte");
                                int recensioni = utente.getInt("recensioni");
                                if ((!username.equalsIgnoreCase(loggato.getUsername())) && (!username.equalsIgnoreCase("staff"))) {
                                    Utenti utentetrovato = new Utenti(nome, cognome, "", "", "", "", sesso, username, "", 0, attivo, ruolo, 0, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                                    preferiti.add(utentetrovato);
                                }
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    EstrapolaPreferiti.asydata = true;
                }
            });
            while (EstrapolaPreferiti.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            EstrapolaPreferiti.asydata = false;

            nPreferiti.setTypeface(myfontdoppio);
            nPreferiti.setText("" + preferiti.size());
        }

        /*String ricerca = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");*/
        if (controllointernet()) {
            creaListaUtenti("");
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void creaListaUtenti(String substring) {
        EstrapolaUtenti cd = new EstrapolaUtenti(this);
        cd.execute(substring);
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
                                Utenti utentetrovato = new Utenti(nome, cognome, "", "", "", luogo, sesso, username, "", 0, attivo, ruolo, 0, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                                utenti.add(utentetrovato);
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


        if (substring.equals(""))
        {
            tutti = new ArrayList<>(utenti);
        }
        creaListViewUtenti(utenti);
    }

    private void creaListViewUtenti(final ArrayList<Utenti> lista){
        ListView listView = (ListView) findViewById(R.id.listviewListaUtenti);
        if (lista.size() > 0) {

            CustomAdapterUtenti1 customAdapter = new CustomAdapterUtenti1(this, R.layout.elementoutente, R.id.username, new ArrayList<Utenti>());

            listView.setAdapter(customAdapter);

            listView.setAdapter(new CustomAdapterUtenti1(this,R.layout.elementoutente, R.id.username, lista));

            customAdapter.notifyDataSetChanged();


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Utenti u = lista.get(position);

                    VerificaRelazione ef = new VerificaRelazione(getApplicationContext());
                    ef.execute(loggato.getUsername(), u.getUsername());
                    ef.setOnFinishListener(new VerificaRelazione.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                VerificaRelazione.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                VerificaRelazione.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                VerificaRelazione.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errorpwd = "probserver";
                                segnalaerrori = true;
                                VerificaRelazione.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                VerificaRelazione.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errorpwd = "pceserverspento";
                                segnalaerrori = true;
                                VerificaRelazione.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            try {
                                JSONObject jOResult = new JSONObject(result);
                                int success = jOResult.getInt("success");
                                if (success == 1) {
                                    utenti.clear();
                                    JSONArray utentedati;
                                    utentedati = jOResult.getJSONArray("datiutente");
                                    //for (int i = 0; i < utentedati.length(); i++) {
                                    JSONObject relazione = utentedati.getJSONObject(0);
                                    String amico = relazione.getString("Amico");
                                    String follow = relazione.getString("Follow");
                                    int preferito = relazione.getInt("Preferito");
                                    legame = new Social(amico, follow, preferito);
                                    //}
                                } else {
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            VerificaRelazione.asydata = true;
                        }
                    });
                    while (VerificaRelazione.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errorpwd);
                        }
                    }
                    segnalaerrori = false;
                    VerificaRelazione.asydata = false;

                    Intent intent = new Intent(getApplicationContext(), PaginaAltroUtente.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("UTENTE", loggato);
                    bundle.putSerializable("AMICO", u);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
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
            Toast toast = Toast.makeText(PaginaUtentiUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaUtentiUtente.this, "message", Toast.LENGTH_LONG);
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

    public void vediListaFollowers(View view) {
        if (Integer.parseInt(nFollowers.getText().toString()) > 0) {
            EstrapolaFollowers ef = new EstrapolaFollowers(this);
            ef.execute(loggato.getUsername());
            ef.setOnFinishListener(new EstrapolaFollowers.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaFollowers.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaFollowers.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaFollowers.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaFollowers.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaFollowers.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaFollowers.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    try {
                        JSONObject jOResult = new JSONObject(result);
                        int success = jOResult.getInt("success");
                        if (success == 1) {
                            utenti.clear();
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
                                int scommesse = utente.getInt("scommesse");
                                int media = utente.getInt("media");
                                int seguaci = utente.getInt("seguaci");
                                int vinte = utente.getInt("vinte");
                                int recensioni = utente.getInt("recensioni");
                                if ((!username.equalsIgnoreCase(loggato.getUsername())) && (!username.equalsIgnoreCase("staff"))) {
                                    Utenti utentetrovato = new Utenti(nome, cognome, "", "", "", "", sesso, username, "", 0, attivo, ruolo, 0, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                                    utenti.add(utentetrovato);
                                }
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    EstrapolaFollowers.asydata = true;
                }
            });
            while (EstrapolaFollowers.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            EstrapolaFollowers.asydata = false;

            creaListViewUtenti(utenti);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnessunaccount, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            creaListViewUtenti(tutti);
        }
    }

    public void vediListaRecensioni(View view) {
        if (Integer.parseInt(nRecensioni.getText().toString()) > 0) {
            EstrapolaRecensioni ef = new EstrapolaRecensioni(this);
            ef.execute(loggato.getUsername());
            ef.setOnFinishListener(new EstrapolaRecensioni.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        EstrapolaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaRecensioni.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        EstrapolaRecensioni.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    try {
                        JSONObject jOResult = new JSONObject(result);
                        int success = jOResult.getInt("success");
                        if (success == 1) {
                            utenti.clear();
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
                                int scommesse = utente.getInt("scommesse");
                                int media = utente.getInt("media");
                                int seguaci = utente.getInt("seguaci");
                                int vinte = utente.getInt("vinte");
                                int recensioni = utente.getInt("recensioni");
                                if ((!username.equalsIgnoreCase(loggato.getUsername())) && (!username.equalsIgnoreCase("staff"))) {
                                    Utenti utentetrovato = new Utenti(nome, cognome, "", "", "", "", sesso, username, "", 0, attivo, ruolo, 0, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                                    utenti.add(utentetrovato);
                                }
                            }
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    EstrapolaRecensioni.asydata = true;
                }
            });
            while (EstrapolaRecensioni.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            EstrapolaRecensioni.asydata = false;

            creaListViewUtenti(utenti);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnessunaccount, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            creaListViewUtenti(tutti);
        }
    }

    public void vediListaAmici(View view) {
        if (Integer.parseInt(nAmici.getText().toString()) > 0) {
            creaListViewUtenti(amici);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnessunaccount, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            creaListViewUtenti(tutti);
        }
    }

    public void vediListaPreferiti(View view) {
        if (Integer.parseInt(nPreferiti.getText().toString()) > 0) {
            creaListViewUtenti(preferiti);
        }
        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnessunaccount, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            creaListViewUtenti(tutti);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void effettuaRicerca(View view) {
        if (cerchiamo.getText().toString().length() > 0)
        {
            utenti.clear();
            creaListaUtenti(cerchiamo.getText().toString());
            if (utenti.size() < 1)
            {
                View toastView = getLayoutInflater().inflate(R.layout.toastnessunaccount, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                cerchiamo.requestFocus(1);
                creaListViewUtenti(tutti);
            }
            if (utenti.size() > 0)
            {
                creaListViewUtenti(utenti);
            }
        }
    }
}