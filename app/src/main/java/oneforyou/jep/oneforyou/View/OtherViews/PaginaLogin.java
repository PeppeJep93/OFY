package oneforyou.jep.oneforyou.View.OtherViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import oneforyou.jep.oneforyou.Control.AggiornamentoErrori;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Control.ControlloDati;
import oneforyou.jep.oneforyou.Control.ControlloUsername;
import oneforyou.jep.oneforyou.Util.Encryptor;
import oneforyou.jep.oneforyou.Util.Metodi;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.ViewAdmin.PaginaDashboard;
import oneforyou.jep.oneforyou.View.ViewUser.PaginaHome;
import oneforyou.jep.oneforyou.View.ViewUser.PaginaTutorial;

public class PaginaLogin extends Activity {

    public static final Pattern USER_PATTERN = Pattern.compile(
            "^(?=.{5,15}$)[a-zA-Z0-9]+");

    public static final Pattern PWD_PATTERN = Pattern.compile(
            "^(?=.{5,15}$)(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+");

    private Typeface myfont, myfontdoppio, myfontsottile;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView nomeapp, iscriviti, recp;
    private EditText campouser, campopass;
    private Button pulsanteaccedi, pulsanteregistrati;
    private boolean risultatonick, sistu = false, sistp = false;
    private String password = "", ruolo = "", attivo = "", colore = "", foto = "";
    private int blocco = 0, saldo = 0, scommesse = 0, media = 0, seguaci = 0, vinte = 0, recensioni = 0;
    private static String errornick = "", errorpwd = "";
    private static boolean segnalaerrori = false;
    private String pwdcrypt = "";


    /*private VideoView vv;*/


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutlogin);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        nomeapp = (TextView) findViewById(R.id.accedi);
        iscriviti = (TextView) findViewById(R.id.nonseiiscritto);
        recp = (TextView) findViewById(R.id.recuperaPassword);

        recp.setTypeface(myfont);
        nomeapp.setTypeface(myfontdoppio);
        iscriviti.setTypeface(myfontdoppio);

        pulsanteaccedi = (Button) findViewById(R.id.premiaccesso);
        pulsanteregistrati = (Button) findViewById(R.id.registrati);

        pulsanteaccedi.setTypeface(myfontdoppio);
        pulsanteregistrati.setTypeface(myfontdoppio);

        campouser = (EditText) findViewById(R.id.inserisciuser);
        campopass = (EditText) findViewById(R.id.inseriscipass);
        campouser.setTextColor(Color.argb(124, 0, 0, 0));
        campopass.setTextColor(Color.argb(124, 0, 0, 0));
        campouser.setHintTextColor(Color.argb(255, 0, 0, 0));
        campopass.setHintTextColor(Color.argb(255, 0, 0, 0));

        campopass.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    pulsanteaccedi.performClick();
                    return true;
                }
                return false;
            }
        });

        Intent i = getIntent();
        String u = i.getStringExtra("USER");
        String p = i.getStringExtra("PASS");
        campouser.setText(u);
        campopass.setText(p);

        /*codice per ESEGUITO da tastiera*/


        campouser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    campouser.setHintTextColor(Color.argb(75, 255, 255, 255));
                    campouser.setTextColor(Color.argb(255, 255, 255, 255));
                    campouser.setBackgroundResource(R.drawable.editsopra);
                    campouser.setNextFocusForwardId(R.id.dgtpass1);

                } else {
                    campouser.setHintTextColor(Color.argb(255, 0, 0, 0));
                    campouser.setTextColor(Color.argb(255, 0, 0, 0));
                    campouser.setBackgroundResource(R.drawable.edittextsopra);
                }
            }
        });


        campopass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    campopass.setHintTextColor(Color.argb(75, 255, 255, 255));
                    campopass.setTextColor(Color.argb(255, 255, 255, 255));
                    campopass.setBackgroundResource(R.drawable.editsotto);
                } else {
                    campopass.setHintTextColor(Color.argb(255, 0, 0, 0));
                    campopass.setTextColor(Color.argb(255, 0, 0, 0));
                    campopass.setBackgroundResource(R.drawable.edittextsotto);
                }
            }
        });
    }


    protected void onResume(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutlogin);

        /*Intent datix = null;
        if (getIntent() == null) {
            datix = getIntent();
            index = datix.getIntExtra("indice", -1);
            if (index>-1) {
                utenti.get(index).setAttivo(true);
            }
        }*/
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
    }

    /*@Override codice per avviare un video in loop*/
    /*protected void onResume() {
        super.onResume();
        vv = (VideoView) findViewById(R.id.wow);
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                vv.start(); //need to make transition seamless.
            }
        });

        vv.setVideoURI(Uri.parse("android.resource://oneforyou.jep.oneforyou/raw/nomevideo"));

        vv.requestFocus();
        vv.start();
    }*/

    public synchronized boolean controllonickname() throws InterruptedException, ExecutionException {
        if (!USER_PATTERN.matcher(campouser.getText().toString()).matches()) {
            if (campouser.getText().toString().length() < 5 || campouser.getText().toString().length() > 15) {
                View toastView = getLayoutInflater().inflate(R.layout.toastcaratteriusername, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                campouser.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                ControlloUsername.asynick = true;
            } else /*caratteristrani */ {
                View toastView = getLayoutInflater().inflate(R.layout.toastspecialiusername, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                campouser.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                ControlloUsername.asynick = true;
            }
            return false;
        } else {
            String u = campouser.getText().toString();
            ControlloUsername task = new ControlloUsername(this);
            task.setOnFinishListener(new ControlloUsername.onFinishListener() {
                @Override
                public void onFinish(String res) {
                    // The task has finished and you can now use the result
                    if (res.contains("Scegline")) {
                        risultatonick = true;
                        ControlloUsername.asynick = true;
                    }
                    if (res.contains("disponibile")) {
                        errornick = "accountnonesistente";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                    }
                    if (res.contains("java.net.ConnectException")) {
                        errornick = "probserver";
                        res = "";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                    }
                    if (res.contains("java.net.UnsupportedEncoding")) {
                        errornick = "probserver";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                        res = "";
                    }
                    if (res.contains("java.net.Protocol")) {
                        errornick = "probserver";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                        res = "";
                    }
                    if (res.contains("java.net.MalformedUrl")) {
                        errornick = "probserver";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                        res = "";
                    }
                    if (res.contains("java.net.IO")) {
                        errornick = "pceserverspento";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                        res = "";
                    }
                    if (res.contains("java.net.SocketTimeoutException")) {
                        errornick = "pceserverspento";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                        res = "";
                        //stampare risultato asynctask
                    }
                }
            });
            task.execute(u);
            while (ControlloUsername.asynick == false) {
            }
            ControlloUsername.asynick = false;
            return risultatonick;
        }
    }


    private synchronized boolean controllopassword() {
        String passs = campopass.getText().toString();
        if (!PWD_PATTERN.matcher(passs).matches()) {
            if (passs.length() < 5 || passs.length() > 15) {
                View toastView = getLayoutInflater().inflate(R.layout.toastpwdlung, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                campopass.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                return false;
            }
            if (passs.length() > 4 || passs.length() < 16) {
                boolean num, let;
                let = (passs.contains("Q") || passs.contains("W") || passs.contains("E") || passs.contains("R") || passs.contains("T") || passs.contains("Y") || passs.contains("U") || passs.contains("I") || passs.contains("O") || passs.contains("P") || passs.contains("A") || passs.contains("S") || passs.contains("D") || passs.contains("F") || passs.contains("G") || passs.contains("H") || passs.contains("J") || passs.contains("K") || passs.contains("L") || passs.contains("Z") || passs.contains("X") || passs.contains("C") || passs.contains("V") || passs.contains("B") || passs.contains("N") || passs.contains("M"));
                num = (passs.contains("0") || passs.contains("1") || passs.contains("2") || passs.contains("3") || passs.contains("4") || passs.contains("5") || passs.contains("6") || passs.contains("7") || passs.contains("8") || passs.contains("9"));

                if (let == false && num == false) {
                    View toastView = getLayoutInflater().inflate(R.layout.toastpwdregex, null);
                    Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    let = num = false;
                    campopass.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    return false;
                }
                if (num == true && let == false) {
                    View toastView = getLayoutInflater().inflate(R.layout.toastpwdregexn, null);
                    Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    let = num = false;
                    campopass.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    return false;
                }
                if (num == false && let == true) {
                    View toastView = getLayoutInflater().inflate(R.layout.toastpwdregexl, null);
                    Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    let = num = false;
                    campopass.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    return false;
                }

                if (num == true && let == true) {
                    if (!USER_PATTERN.matcher(passs).matches()) {
                        View toastView = getLayoutInflater().inflate(R.layout.toastpwdchar, null);
                        Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                        toast.setView(toastView);
                        toast.show();
                        campopass.requestFocus(1);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        return false;
                    }
                }
            }
        }
        if (PWD_PATTERN.matcher(passs).matches()) {
            if (!USER_PATTERN.matcher(passs).matches()) {
                View toastView = getLayoutInflater().inflate(R.layout.toastpwdchar, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                campopass.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                return false;
            } else return true;
        }
        return true;
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


    public synchronized void eseguiLogin(View view) throws ExecutionException, InterruptedException {

        errornick = "";

        if (controllointernet()) {

            if (Metodi.prendiTestoEditato(campopass).equals("") && Metodi.prendiTestoEditato(campouser).equals("")) {
                View toastView = getLayoutInflater().inflate(R.layout.toasteffettuareaccesso, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return;
            }


            if (Metodi.prendiTestoEditato(campouser).equals("")) {
                View toastView = getLayoutInflater().inflate(R.layout.toastusernamenoninserita, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return;
            } else {
                sistu = controllonickname();
            }

            if (sistu == false)
            {
                toastview(errornick);
                return;
            }

            if (Metodi.prendiTestoEditato(campopass).equals("")) {
                View toastView = getLayoutInflater().inflate(R.layout.toastpasswordnoninserita, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return;
            } else {
                //sistp = controllopassword();
                controllodati();
            }

            /*if (sistp == false) return;

            else controllodati();*/

            /*
            vedere cosa restituisce, se restituisce 1, 2 o 3
            */
        /*
            -scaricarne i dati
            -confrontare password e vedere errori
                -- se ok, accede
                     --- se admin o se normale
                -- se no, errore database
        }*/
        }
    }

    private void toastview(String error) {
        if (error.equals("administrator"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastadministrator, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();

        }
        if (error.equals("accountnonesistente"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastaccountnonesistente, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            campouser.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        if (error.equalsIgnoreCase("tretentativi"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastancoratre, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            AggiornamentoErrori ae = new AggiornamentoErrori(PaginaLogin.this);
            ae.execute("3", campouser.getText().toString());
            ControlloDati.asydata = true;
        }
        if (error.equalsIgnoreCase("duetentativi"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastduetentativi, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            AggiornamentoErrori ae = new AggiornamentoErrori(PaginaLogin.this);
            ae.execute("2", campouser.getText().toString());
            ControlloDati.asydata = true;
        }
        if (error.equalsIgnoreCase("untentativo"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastunaltrapos, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            AggiornamentoErrori ae = new AggiornamentoErrori(PaginaLogin.this);
            ae.execute("1", campouser.getText().toString());
            ControlloDati.asydata = true;
        }
        if (error.equalsIgnoreCase("bloccato"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastaccountbloccato, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            AggiornamentoErrori ae = new AggiornamentoErrori(PaginaLogin.this);
            ae.execute("-1", campouser.getText().toString());
            ControlloDati.asydata = true;
        }
        if (error.equalsIgnoreCase("bannato"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastaccountbannato, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            AggiornamentoErrori ae = new AggiornamentoErrori(PaginaLogin.this);
            ae.execute("-1", campouser.getText().toString());
            ControlloDati.asydata = true;
        }
        if (error.equalsIgnoreCase("fermo"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastattendiverifica, null);
            Toast toast = Toast.makeText(PaginaLogin.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            AggiornamentoErrori ae = new AggiornamentoErrori(PaginaLogin.this);
            ae.execute("-1", campouser.getText().toString());
            ControlloDati.asydata = true;
        }
    }


    private synchronized void controllodati() throws ExecutionException, InterruptedException {
        pwdcrypt = Encryptor.encryptPassword(campopass.getText().toString());
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
                        Utenti loggato = new Utenti(nome, cognome, email, telefono, data, luogo, sesso, campouser.getText().toString(), password, blocco, attivo, ruolo, saldo, foto, colore, scommesse, media, vinte, seguaci, recensioni);
                        if (!pwdcrypt.equals(password) && !(loggato.getRuolo().toLowerCase().contains("admin"))) {
                            if (blocco == 0) {
                                errorpwd = "tretentativi";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }
                            if (blocco == 3) {
                                errorpwd = "duetentativi";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }
                            if (blocco == 2) {
                                errorpwd = "untentativo";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }
                            if (blocco == 1) {
                                errorpwd = "bloccato";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }

                            if (blocco == -1) {
                                errorpwd = "bloccato";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }

                            if (attivo.equalsIgnoreCase("bannato")) {
                                errorpwd = "bannato";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }
                        }

                        if (!pwdcrypt.equals(password) && (loggato.getRuolo().toLowerCase().contains("admin"))) {
                            errorpwd = "administrator";
                            segnalaerrori = true;
                            ControlloDati.asydata = true;
                            return;
                        }

                        if (pwdcrypt.equals(password)) {
                            if (attivo.equalsIgnoreCase("bannato")) {
                                errorpwd = "bannato";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }
                            if (attivo.equalsIgnoreCase("fermo")) {
                                errorpwd = "fermo";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }
                            if (blocco == -1) {
                                errorpwd = "bloccato";
                                segnalaerrori = true;
                                ControlloDati.asydata = true;
                                return;
                            }
                        }
                        if (ruolo.toLowerCase().contains("admin")) {
                            Intent intent = new Intent(getApplicationContext(), PaginaDashboard.class);
                                /*intent.putExtra("COLORE", colore);
                                intent.putExtra("FOTO", foto);
                                intent.putExtra("USER", username);*/
                            try {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("UTENTE", loggato);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                ControlloDati.asydata = true;
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        if (attivo.toLowerCase().contains("attesa")) {
                            if (ruolo.toLowerCase().contains("normale") || ruolo.toLowerCase().contains("tipster")) {
                                Intent intent = new Intent(getApplicationContext(), PaginaTutorial.class);
                                    /*intent.putExtra("RUOLO", ruolo);
                                    intent.putExtra("SALDO", saldo);
                                    intent.putExtra("COLORE", colore);
                                    intent.putExtra("FOTO", foto);
                                    intent.putExtra("SCOMMESSE", scommesse);
                                    intent.putExtra("MEDIA", media);
                                    intent.putExtra("SEGUACI", seguaci);
                                    intent.putExtra("VINTE", vinte);
                                    intent.putExtra("RECENSIONI", recensioni);
                                    intent.putExtra("USER", username);*/
                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("UTENTE", loggato);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    ControlloDati.asydata = true;
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        }
                        if (attivo.toLowerCase().contains("attivo")) {
                            if (ruolo.toLowerCase().contains("normale") || ruolo.toLowerCase().contains("tipster")) {
                                Intent intent = new Intent(getApplicationContext(), PaginaHome.class);
                                    /*intent.putExtra("RUOLO", ruolo);
                                    intent.putExtra("SALDO", saldo);
                                    intent.putExtra("COLORE", colore);
                                    intent.putExtra("FOTO", foto);
                                    intent.putExtra("SCOMMESSE", scommesse);
                                    intent.putExtra("MEDIA", media);
                                    intent.putExtra("SEGUACI", seguaci);
                                    intent.putExtra("VINTE", vinte);
                                    intent.putExtra("RECENSIONI", recensioni);
                                    intent.putExtra("USER", username);*/
                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("UTENTE", loggato);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    ControlloDati.asydata = true;
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cd.execute(campouser.getText().toString(), pwdcrypt);
        while (ControlloDati.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        ControlloDati.asydata = false;
    }

        /*else
        blocco == -1

    //accessodatabase
        //controllautenteseesiste e seattivo
        //controllapassword e segnalaerrore e recuperopassword
        //accedi


    /*private void accessonegato(final String name, String pass, String email) {
        nomeapp.setText("ACCOUNT BLOCCATO\nInviata mail recupero");
        String sb = "";
        int lung = 6;
        int index;
        String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random(System.currentTimeMillis());
        for (int i = 0; i < lung; i++)
            sb = sb + (ALPHABET.charAt(rnd.nextInt(ALPHABET.length())));
        String asdfg = "";

        for (int i = 0; i < utenti.size(); i++) {
            /*se non hai trovato l'utente, cerca ancora
            if (utenti.get(i).getNome().equalsIgnoreCase(name)) {
                utenti.get(i).setChiave(sb);
                break;
            }
        }*/

    public void passaiscrizione (View view){
        Intent intent = new Intent(getApplicationContext(), PaginaRegistrazione.class);
        startActivity(intent);
    }

    public void recuperoPass(View view) {
        Intent intent = new Intent(getApplicationContext(), PaginaRecuperoPassword.class);
        startActivity(intent);
    }
}





/*@Override codice per avviare un video in loop*/
    /*protected void onResume() {
        super.onResume();
        vv = (VideoView) findViewById(R.id.wow);
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                vv.start(); //need to make transition seamless.
            }
        });

        vv.setVideoURI(Uri.parse("android.resource://oneforyou.jep.oneforyou/raw/nomevideo"));

        vv.requestFocus();
        vv.start();
    }*/


//FIREBASE COMMANDS



    /*public void provascritta(View view)
    {
        mAuth.createUserWithEmailAndPassword("ggg@gm.com", "abc123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }

                    private void updateUI(FirebaseUser user) {
                    }
                });
    }*/


    /*public void provascritta(View view) {
        String dove = "ospite";
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://oneforyou-pd.firebaseio.com/");
        // DatabaseReference myRef = database.getReference("partite");
        DatabaseReference myRef = database.getReference("partite/match/" + dove);

        // qui scrivo da cell
        myRef.setValue(3.5f);
    }




    /*public void provaletta(View view) {
        // Read from the database
        String dove = "ospite";
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://oneforyou-pd.firebaseio.com/");
        DatabaseReference myRef = database.getReference("partite/match/" + dove);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                /*if (!dataSnapshot.exists()) {
                    textView3.setText("cancellato");
                    return;
                }


                Float value = dataSnapshot.getValue(Float.class);
                Log.d(TAG, "Value is: " + value);
                textView3.setText(value.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                textView3.setText("eliminato");
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }*/

