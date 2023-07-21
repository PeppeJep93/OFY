package oneforyou.jep.oneforyou.View.ViewAdmin;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import oneforyou.jep.oneforyou.Control.AcquistaBuono;
import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Scommesse;
import oneforyou.jep.oneforyou.Model.Social;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Control.CambiaMail;
import oneforyou.jep.oneforyou.Control.CambiaTelefono;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Control.ControlloMail;
import oneforyou.jep.oneforyou.Control.ControlloTelefono;
import oneforyou.jep.oneforyou.Util.CustomOnItemSelectedListener;
import oneforyou.jep.oneforyou.Control.ModificaRuolo;
import oneforyou.jep.oneforyou.Control.ModificaSaldo;
import oneforyou.jep.oneforyou.Control.ModificaStato;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.ViewUser.PaginaAccountUtente;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaProfiloUtente extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato, amico;
    private Social legame;
    private RelativeLayout barramenu, voce1, voce2;
    private boolean segnalaerrori, telefonovalido = false;
    private int risultatoquery;
    private int modt = 0;
    private int modm = 0;
    private String errorpwd = "", errormail = "";
    private Spinner servizio;
    private ArrayList<Utenti> utenti = new ArrayList<>();
    private ArrayList<Bitmap> fotosutenti = new ArrayList<>();
    private LinearLayout riga;
    private ArrayList<Scommesse> scommesse = new ArrayList<>();
    public static ArrayList<Partite> partite = new ArrayList<>();
    private TextView saldoutente, codicebuono, valorebuono, nomeutente, cognomeutente, luogoutente, mailutente, telefonoutente, sessoutente, datautente, coloreutente, infotv, usernametv, statstv;
    private EditText mailmodifica, telefonomodifica;
    private Button modificaDati, confermamodifica, aggiornasaldo, regalacodice, promuoviboccia, sbloccaaccount;
    private EditText codice;
    private boolean codicevalido, valorevalido;
    private EditText valore;

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public static final Pattern NUMERI_PATTERN = Pattern.compile("^[0-9]*$");

    public static final Pattern USER_PATTERN = Pattern.compile(
            "^(?=.{5,15}$)[a-zA-Z0-9]+");

    public static final Pattern PAROLA_PATTERN = Pattern.compile(
            "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutprofiloutente);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");
        amico = (Utenti) bundle.getSerializable("AMICO");


        nomeutente = (TextView) findViewById(R.id.nomeutente);
        cognomeutente = (TextView) findViewById(R.id.cognomeutente);
        luogoutente = (TextView) findViewById(R.id.luogoutente);
        sessoutente = (TextView) findViewById(R.id.sessoutente);
        datautente = (TextView) findViewById(R.id.datautente);
        coloreutente = (TextView) findViewById(R.id.coloreutente);
        mailutente = (TextView) findViewById(R.id.mailutente);
        telefonoutente = (TextView) findViewById(R.id.telutente);
        saldoutente = (TextView) findViewById(R.id.saldoutente);
        saldoutente.setText("" + amico.getSaldo());

        servizio = (Spinner) findViewById(R.id.serviziobuono);

        addItemsOnSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        TextView user = (TextView) findViewById(R.id.username);

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

            String datanas = amico.getData();
            int anno = Integer.parseInt(datanas.substring(0, 4));
            int mese = Integer.parseInt(datanas.substring(5, 7));
            int giorno = Integer.parseInt(datanas.substring(8, 10));
            datautente.setText(giorno + "/" + mese + "/" + anno);

            coloreutente.setText(amico.getColore());

            telefonoutente.setText(amico.getTelefono());
            mailutente.setText(amico.getEmail());



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

        barramenu = findViewById(R.id.barramenusotto);
        voce1 = findViewById(R.id.buttonutenti1);

        modificaDati = (Button) findViewById(R.id.modificadati);
        confermamodifica = (Button) findViewById(R.id.confermamodifica);
        aggiornasaldo = (Button) findViewById(R.id.aggiornasaldo);
        regalacodice = (Button) findViewById(R.id.regalacodice);
        promuoviboccia = (Button) findViewById(R.id.promuoviboccia);
        sbloccaaccount = (Button) findViewById(R.id.sbloccaaccount);

        if (amico.getRuolo().toLowerCase().equals("normale")){
            promuoviboccia.setText("PROMUOVI A TIPSTER!");
        }
        if (amico.getRuolo().toLowerCase().equals("tipster")){
            promuoviboccia.setText("BOCCIA TIPSTER!");
        }

        if (amico.getAttivo().toLowerCase().equals("bloccato") || amico.getAttivo().toLowerCase().equals("bannato") || amico.getAttivo().toLowerCase().equals("fermo") || amico.getBlocco() == -1){
            sbloccaaccount.setText("SBLOCCA ACCOUNT!");
        }
        if ((amico.getAttivo().toLowerCase().equals("attesa") || amico.getAttivo().toLowerCase().equals("attivo")) && (amico.getBlocco() != -1)){
            sbloccaaccount.setText("BANNA UTENTE!");
        }

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
            voce1.setBackgroundResource(R.color.azzurroc);
            utente.setBackgroundResource(R.color.azzurro);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.azzurros));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.azzurros));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.azzurros));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.azzurros));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.azzurros));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.azzurros));
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce1.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.blus));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.blus));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.blus));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.blus));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.blus));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.blus));
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            voce1.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.giallos));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.giallos));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.giallos));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.giallos));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.giallos));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.giallos));
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            voce1.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.rossos));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.rossos));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.rossos));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.rossos));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.rossos));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.rossos));
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            voce1.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.verdes));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.verdes));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.verdes));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.verdes));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.verdes));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.verdes));
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            voce1.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.granatas));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.granatas));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.granatas));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.granatas));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.granatas));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.granatas));
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce1.setBackgroundResource(R.color.arancionec);
            utente.setBackgroundResource(R.color.arancione);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.aranciones));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.aranciones));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.aranciones));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.aranciones));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.aranciones));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.aranciones));
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce1.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.neros));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.neros));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.neros));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.neros));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.neros));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.neros));
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce1.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.neros));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.neros));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.neros));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.neros));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.neros));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.neros));
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce1.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.violas));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.violas));
            aggiornasaldo.setBackgroundColor(getResources().getColor(R.color.violas));
            regalacodice.setBackgroundColor(getResources().getColor(R.color.violas));
            sbloccaaccount.setBackgroundColor(getResources().getColor(R.color.violas));
            promuoviboccia.setBackgroundColor(getResources().getColor(R.color.violas));
        }



        mailmodifica = (EditText) findViewById(R.id.mailmodifica);
        telefonomodifica = (EditText) findViewById(R.id.telmodifica);
        codicebuono = (EditText) findViewById(R.id.codicebuono);
        valorebuono = (EditText) findViewById(R.id.valorebuono);

        telefonomodifica.addTextChangedListener(new TextWatcher() {

            boolean ignoreChange = false;

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!ignoreChange) {
                    String string = s.toString();
                    if (string.length() == 10) {
                        if (NUMERI_PATTERN.matcher(string).matches()) {
                            telefonovalido = true;
                            errorpwd = "";
                        } else {
                            telefonovalido = false;
                            errorpwd ="telefonononvalido";
                        }
                    } else {
                        telefonovalido = false;
                    }
                    if ((string.length() > 10)) {
                        string = string.substring(0, 10);
                        telefonomodifica.setText(string);
                        telefonovalido = true;
                        errorpwd ="";
                    }
                    ignoreChange = true;
                    telefonomodifica.setText(string);
                    telefonomodifica.setSelection(telefonomodifica.getText().length());
                    ignoreChange = false;
                }
            }
        });

        if (amico.getColore().toLowerCase().contains("azzurro")) {
            riga.setBackgroundResource(R.color.azzurrocc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("blu")) {
            riga.setBackgroundResource(R.color.blucc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("giallo")) {
            riga.setBackgroundResource(R.color.giallocc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("rosso")) {
            riga.setBackgroundResource(R.color.rossocc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("verde")) {
            riga.setBackgroundResource(R.color.verdecc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("granata")) {
            riga.setBackgroundResource(R.color.granatacc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("arancione")) {
            riga.setBackgroundResource(R.color.arancionecc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("bianco")) {
            riga.setBackgroundResource(R.color.biancocc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("nero")) {
            riga.setBackgroundResource(R.color.nerocc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
        }

        if (amico.getColore().toLowerCase().contains("viola")) {
            riga.setBackgroundResource(R.color.violacc);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            saldoutente.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            valorebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            codicebuono.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
        }

        telefonoutente.setText(amico.getTelefono());
        mailutente.setText(amico.getEmail());

        servizio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String marchio = servizio.getSelectedItem().toString();

                if (marchio.toLowerCase().contains("netflix")) {
                    servizio.setBackgroundResource(R.drawable.rossocolor);
                }

                if (marchio.toLowerCase().contains("spotify")) {
                    servizio.setBackgroundResource(R.drawable.verdecolor);
                }

                if (marchio.toLowerCase().contains("nowtv")) {
                    servizio.setBackgroundResource(R.drawable.violacolor);
                }

                if (marchio.toLowerCase().contains("amazon")) {
                    servizio.setBackgroundResource(R.drawable.arancionecolor);
                }

                if (marchio.toLowerCase().contains("dazn")) {
                    servizio.setBackgroundResource(R.drawable.biancocolor);
                }

                if (marchio.toLowerCase().contains("playstore")) {
                    servizio.setBackgroundResource(R.drawable.azzurrocolor);
                }

                if (marchio.toLowerCase().contains("granata")) {
                    servizio.setBackgroundResource(R.drawable.granatacolor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        codice = (EditText) findViewById(R.id.codicebuono);

        codice.addTextChangedListener(new TextWatcher() {

            boolean ignoreChange = false;

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!ignoreChange) {
                    String string = s.toString();
                    if (string.length() == 5) {
                        codicevalido = true;
                    } else {
                        codicevalido = false;
                    }
                    if ((string.length() > 5)) {
                        string = string.substring(0, 5);
                        codice.setText(string);
                        codicevalido = true;
                    }
                    ignoreChange = true;
                    codice.setText(string);
                    codice.setSelection(codice.getText().length());
                    ignoreChange = false;
                }
            }
        });

        valore = (EditText) findViewById(R.id.valorebuono);

        valore.addTextChangedListener(new TextWatcher() {

            boolean ignoreChange = false;

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!ignoreChange) {
                    String string = s.toString();
                    if ((string.length() > 2)) {
                        string = string.substring(0, 2);
                        valore.setText(string);
                        valorevalido = true;
                    }
                    if (!string.equals("") && Integer.parseInt(string) == 0) {
                        valorevalido = false;
                    } else {
                        valorevalido = true;
                    }
                    ignoreChange = true;
                    valore.setText(string);
                    valore.setSelection(valore.getText().length());
                    ignoreChange = false;
                }
            }
        });


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

    public void addListenerOnButton() {
        servizio = (Spinner) findViewById(R.id.serviziobuono);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addListenerOnSpinnerItemSelection() {
        servizio.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        String marchioscelto = servizio.getSelectedItem().toString();
        if (marchioscelto.toLowerCase().contains("azzurro")) {
            Drawable x = getDrawable(R.drawable.azzurrocolor);
            servizio.setBackgroundResource(R.drawable.azzurrocolor);
        }
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("blu"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("giallo"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("rosso"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("verde"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("granata"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("arancione"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("viola"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("bianco"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));
//        if (spinner.getSelectedItem().toString().toLowerCase().equals("nero"))
//            spinner.setBackgroundColor(getResources().getColor(R.color.azzurro));

    }

    private void addItemsOnSpinner() {
        servizio = (Spinner) findViewById(R.id.serviziobuono);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.marchi, R.layout.layoutspinner);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servizio.setAdapter(adapter);
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
        if (error.equals("cambiomail")) {
            cambiamail(amico.getUsername(), mailmodifica.getText().toString());
            mailutente.setText(mailmodifica.getText().toString());
            mailmodifica.setText("");
            View toastView = getLayoutInflater().inflate(R.layout.toastcambiomail, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("cambiotel")) {
            cambiatel(amico.getUsername(), telefonomodifica.getText().toString());
            telefonoutente.setText(telefonomodifica.getText().toString());
            telefonomodifica.setText("");
            View toastView = getLayoutInflater().inflate(R.layout.toastcambiotel, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("mailnoncambiata")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastmailnoncambiata, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("telnoncambiato")) {
            View toastView = getLayoutInflater().inflate(R.layout.toasttelnoncambiato, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("datinoncambiati")){
            //problemi per entrambi
            View toastView = getLayoutInflater().inflate(R.layout.toastdatinoncambiati, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("telsimailno")){
            //problemi per la mail
            String tel = telefonomodifica.getText().toString();
            cambiatel(amico.getUsername(), telefonomodifica.getText().toString());
            View toastView = getLayoutInflater().inflate(R.layout.toasttelsimailno, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            telefonoutente.setText(tel);
            telefonomodifica.setText("");
        }

        if (error.equals("mailsitelno")){
            String mail = mailmodifica.getText().toString();
            cambiamail(amico.getUsername(), mailmodifica.getText().toString());
            //problemi per il tel
            View toastView = getLayoutInflater().inflate(R.layout.toastmailsitelno, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            mailutente.setText(mail);
            mailmodifica.setText("");
        }

        if (error.equals("mailetelsi")){
            //entrambi buoni
            String tel = telefonomodifica.getText().toString();
            String mail = mailmodifica.getText().toString();
            cambiamail(amico.getUsername(), mailmodifica.getText().toString());
            cambiatel(amico.getUsername(), telefonomodifica.getText().toString());
            View toastView = getLayoutInflater().inflate(R.layout.toastmailetelsi, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            telefonoutente.setText(tel);
            telefonomodifica.setText("");
            mailutente.setText(mail);
            mailmodifica.setText("");
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
            case R.id.buttonpartite1:
                intent = new Intent(getApplicationContext(), PaginaGestionePartite.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.buttonpartite2:
                intent = new Intent(getApplicationContext(), PaginaGestionePartite.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }


    public void abilitamodifica(View view) {
        Button button = (Button) view;
        boolean scritta = button.getText().toString().equalsIgnoreCase("MODIFICA DATI");
        if (scritta)
        {
//            nomemodifica.setVisibility(View.VISIBLE);
//            cognomemodifica.setVisibility(View.VISIBLE);
//            luogomodifica.setVisibility(View.VISIBLE);
//
//            passmodifica1.setVisibility(View.VISIBLE);
//            passmodifica2.setVisibility(View.VISIBLE);
//            selectdata.setVisibility(View.VISIBLE);
//            spinner.setVisibility(View.VISIBLE);
//            photo.setVisibility(View.VISIBLE);
//            fotomodifica = (ImageView) findViewById(R.id.fotomodifica);
//            fotomodifica.setVisibility(View.VISIBLE);
//            fotomodifica.setImageBitmap(bitmap);

//            if (loggato.getColore().equalsIgnoreCase("colore")){
//                spinner.setSelection(0);
//            }
//            if (loggato.getColore().equalsIgnoreCase("azzurro")){
//                spinner.setSelection(1);
//            }
//            if (loggato.getColore().equalsIgnoreCase("blu")){
//                spinner.setSelection(2);
//            }
//            if (loggato.getColore().equalsIgnoreCase("viola")){
//                spinner.setSelection(3);
//            }
//            if (loggato.getColore().equalsIgnoreCase("granata")){
//                spinner.setSelection(4);
//            }
//            if (loggato.getColore().equalsIgnoreCase("rosso")){
//                spinner.setSelection(5);
//            }
//            if (loggato.getColore().equalsIgnoreCase("arancione")){
//                spinner.setSelection(6);
//            }
//            if (loggato.getColore().equalsIgnoreCase("giallo")){
//                spinner.setSelection(7);
//            }
//            if (loggato.getColore().equalsIgnoreCase("verde")){
//                spinner.setSelection(8);
//            }
//            if (loggato.getColore().equalsIgnoreCase("bianco")){
//                spinner.setSelection(9);
//            }
//            if (loggato.getColore().equalsIgnoreCase("nero")){
//                spinner.setSelection(10);
//            }
//            cambiaColore(spinner.getSelectedItem().toString());
            confermamodifica.setVisibility(View.VISIBLE);
            mailmodifica.setVisibility(View.VISIBLE);
            telefonomodifica.setVisibility(View.VISIBLE);
            button.setText("ANNULLA MODIFICA");
        }
        if (!scritta)
        {
//            nomemodifica.setVisibility(View.GONE);
//            cognomemodifica.setVisibility(View.GONE);
//            luogomodifica.setVisibility(View.GONE);
//            mailmodifica.setVisibility(View.GONE);
//            telefonomodifica.setVisibility(View.GONE);
//            passmodifica1.setVisibility(View.GONE);
//            passmodifica2.setVisibility(View.GONE);
//            selectdata.setVisibility(View.GONE);
//            spinner.setVisibility(View.GONE);
//            bitmap2 = null;
            confermamodifica.setVisibility(View.GONE);
            mailmodifica.setVisibility(View.GONE);
            telefonomodifica.setVisibility(View.GONE);
//            fotomodifica.setVisibility(View.GONE);
//            cambiaColore(loggato.getColore());
            button.setText("MODIFICA DATI");
        }
    }

    public boolean confermaModificaDatiUtente(View view) {

        errormail = "";
        segnalaerrori = false;
        ControlloMail.asydata = false;
        ControlloTelefono.asydata = false;

        if (!(mailmodifica.getText().toString().equalsIgnoreCase("")) && !(telefonomodifica.getText().toString().equalsIgnoreCase(""))){

            if (checkEmail(mailmodifica.getText().toString()))
            {
                if (telefonovalido)
                {
                    ControlloMail cd = new ControlloMail(this);
                    cd.execute(mailmodifica.getText().toString());
                    cd.setOnFinishListener(new ControlloMail.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            if (result.contains("utilizzato")) {
                                errormail = "";
                                modm = -1;
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("valida")) {
                                errormail = "";
                                modm = 1;
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            ControlloMail.asydata = true;
                        }
                    });
                    while (ControlloMail.asydata == false) {
                        if (segnalaerrori == true) {
                        }
                    }
                    segnalaerrori = false;
                    ControlloMail.asydata = false;





                    ControlloTelefono ca = new ControlloTelefono(this);
                    ca.execute(telefonomodifica.getText().toString());
                    ca.setOnFinishListener(new ControlloTelefono.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            if (result.contains("Abbinato")) {
                                errormail = "";
                                modt = -1;
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("valid")) {
                                errormail = "";
                                modt = 1;
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            ControlloTelefono.asydata = true;
                        }
                    });
                    while (ControlloTelefono.asydata == false) {
                        if (segnalaerrori == true) {
                        }
                    }
                    segnalaerrori = false;
                    ControlloTelefono.asydata = false;
                    errormail = "";


                    if (modm == -1 && modt == -1){
                        //problemi per entrambi
                        toastview("datinoncambiati");
                        return false;
                    }

                    if (modm == -1 && modt == 1){
                        //problemi per la mail
                        toastview("telsimailno");
                        return false;
                    }

                    if (modm == 1 && modt == -1){
                        //problemi per il tel
                        toastview("mailsitelno");
                        return false;
                    }

                    if (modm == 1 && modt == 1){
                        //entrambi buoni
                        toastview("mailetelsi");
                        return false;
                    }

                }
                else {
                    View toastView = getLayoutInflater().inflate(R.layout.toasttelnonvalido, null);
                    Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    return false;
                }
            }
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnonvalidomail, null);
                Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return false;
            }
        }

        if (mailmodifica.getText().toString().equalsIgnoreCase("") && !(telefonomodifica.getText().toString().equalsIgnoreCase(""))){
            /*mail vuota, ma telefono pieno*/

            if (telefonovalido && NUMERI_PATTERN.matcher(telefonomodifica.getText().toString()).matches())
            {
                //verificare se uguale a quello già esistente
                if (telefonomodifica.getText().toString().equalsIgnoreCase(telefonoutente.getText().toString())){
                    View toastView = getLayoutInflater().inflate(R.layout.toaststessotel, null);
                    Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    return false;
                }
                else {
                    ControlloTelefono cd = new ControlloTelefono(this);
                    cd.execute(telefonomodifica.getText().toString());
                    cd.setOnFinishListener(new ControlloTelefono.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            if (result.contains("Abbinato")) {
                                errormail = "telnoncambiato";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("valid")) {
                                errormail = "cambiotel";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            ControlloTelefono.asydata = true;
                        }
                    });
                    while (ControlloTelefono.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errormail);
                        }
                    }
                    segnalaerrori = false;
                    ControlloTelefono.asydata = false;
                    errormail = "";
                }
            }
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toasttelnonvalido, null);
                Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return false;
            }

            return false;
        }

        if (!(mailmodifica.getText().toString().equalsIgnoreCase("")) && telefonomodifica.getText().toString().equalsIgnoreCase("")){
            /*mail piena, ma telefono vuoto*/

            if (checkEmail(mailmodifica.getText().toString()))
            {
                //verificare se uguale a quello già esistente
                if (mailmodifica.getText().toString().equalsIgnoreCase(mailutente.getText().toString())){
                    View toastView = getLayoutInflater().inflate(R.layout.toaststessamail, null);
                    Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    return false;
                }
                else {
                    ControlloMail cd = new ControlloMail(this);
                    cd.execute(mailmodifica.getText().toString());
                    cd.setOnFinishListener(new ControlloMail.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errormail = "probserver";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errormail = "pceserverspento";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            if (result.contains("utilizzato")) {
                                errormail = "mailnoncambiata";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("valida")) {
                                errormail = "cambiomail";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            ControlloMail.asydata = true;
                        }
                    });
                    while (ControlloMail.asydata == false) {
                        if (segnalaerrori == true) {
                            toastview(errormail);
                        }
                    }
                    segnalaerrori = false;
                    ControlloMail.asydata = false;

                }
            }
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnonvalidomail, null);
                Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                return false;
            }

            return false;
        }

        if (mailmodifica.getText().toString().equalsIgnoreCase("") && telefonomodifica.getText().toString().equalsIgnoreCase("")){
            /*entrambi pieni*/

            View toastView = getLayoutInflater().inflate(R.layout.toastvalorivuoti, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            return false;
        }

        return false;
    }

    private void cambiamail(String username, String email) {
        CambiaMail as = new CambiaMail(this);
        as.execute(username, email);
        as.setOnFinishListener(new CambiaMail.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaMail.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaMail.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaMail.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaMail.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    CambiaMail.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    CambiaMail.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                CambiaMail.asydata = true;
            }
        });
        while (CambiaMail.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        CambiaMail.asydata = false;

        View toastView = getLayoutInflater().inflate(R.layout.toastcambiomail, null);
        Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.show();
    }

    private void cambiatel(String username, String email) {
        CambiaTelefono as = new CambiaTelefono(this);
        as.execute(amico.getUsername(), telefonomodifica.getText().toString());
        as.setOnFinishListener(new CambiaTelefono.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaTelefono.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaTelefono.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaTelefono.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    CambiaTelefono.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    CambiaTelefono.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    CambiaTelefono.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                CambiaTelefono.asydata = true;
            }
        });
        while (CambiaTelefono.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        CambiaTelefono.asydata = false;


    }

    private boolean checkEmail(String mail) {
        return EMAIL_ADDRESS_PATTERN.matcher(mail).matches();
    }

    public void aggiungiQuantità(View view) {
        TextView saldo = (TextView) findViewById(R.id.saldoutente);

        if (view.getId() == R.id.menocinquanta) {
            if ((Integer.parseInt(saldo.getText().toString())) -50 <= 0)
            {
                saldo.setText("0");
                amico.setSaldo(0);
            }
            else {
                amico.setSaldo(amico.getSaldo() - 50);
                int nuovo = (Integer.parseInt(saldo.getText().toString()))-50;
                saldo.setText(nuovo + "");
            }
        }

        if (view.getId() == R.id.piucinquanta) {
            amico.setSaldo(amico.getSaldo() + 50);
            int nuovo = (Integer.parseInt(saldo.getText().toString()))+50;
            saldo.setText(nuovo + "");
        }
    }

    public void aggiornaSaldo(View view) {
        TextView saldo = (TextView) findViewById(R.id.saldoutente);

        boolean negativo = Integer.parseInt(saldo.getText().toString()) < 0;

        if (!saldo.getText().toString().equalsIgnoreCase("") && !PAROLA_PATTERN.matcher(saldo.getText().toString()).matches() && !negativo) {
            ModificaSaldo as = new ModificaSaldo(this);
            as.execute(amico.getUsername(), saldo.getText().toString());
            as.setOnFinishListener(new ModificaSaldo.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaSaldo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaSaldo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaSaldo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaSaldo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaSaldo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaSaldo.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    ModificaSaldo.asydata = true;
                }
            });
            while (ModificaSaldo.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            ModificaSaldo.asydata = false;

            View toastView = getLayoutInflater().inflate(R.layout.toastaggiornatosaldo, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        } else {
            View toastView = getLayoutInflater().inflate(R.layout.toastnumerononvalido, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
    }

    public void regalacodice(View view) {
        Spinner servizio = (Spinner) findViewById(R.id.serviziobuono);
        TextView codice = (TextView) findViewById(R.id.codicebuono);
        TextView valore = (TextView) findViewById(R.id.valorebuono);

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

        if (codicevalido) {
            if (USER_PATTERN.matcher(codice.getText().toString()).matches()) {
                if (valorevalido) {

                    AcquistaBuono ab = new AcquistaBuono(this);
                    ab.execute(codice.getText().toString().toUpperCase(), amico.getUsername(), servizio.getSelectedItem().toString(), data, valore.getText().toString(), "0");
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

                    View toastView = getLayoutInflater().inflate(R.layout.toastregalatocodice, null);
                    Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                }
                else {
                    View toastView = getLayoutInflater().inflate(R.layout.toastmancavalore, null);
                    Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                }
            }

            else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnonsimbolicodice, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }

        }

        else {
            View toastView = getLayoutInflater().inflate(R.layout.toastmancacodice, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }

    }

    public boolean promuoviboccia(View view) {
        Button button = (Button) findViewById(view.getId());

        if (button.getText().toString().toLowerCase().contains("promuovi")){
            button.setText("BOCCIA TIPSTER!");

            amico.setRuolo("Tipster");

            ModificaRuolo as = new ModificaRuolo(this);
            as.execute(amico.getUsername(), "Tipster");
            as.setOnFinishListener(new ModificaRuolo.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    ModificaRuolo.asydata = true;
                }
            });
            while (ModificaRuolo.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            ModificaRuolo.asydata = false;

            View toastView = getLayoutInflater().inflate(R.layout.toastpromosso, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();

            return false;
        }

        if (button.getText().toString().toLowerCase().contains("boccia")){
            button.setText("PROMUOVI A TIPSTER!");

            amico.setRuolo("Normale");

            ModificaRuolo as = new ModificaRuolo(this);
            as.execute(amico.getUsername(), "Normale");
            as.setOnFinishListener(new ModificaRuolo.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaRuolo.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    ModificaRuolo.asydata = true;
                }
            });
            while (ModificaRuolo.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            ModificaRuolo.asydata = false;

            View toastView = getLayoutInflater().inflate(R.layout.toastbocciato, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();

            return false;
        }

        return false;

    }

    public boolean sbloccaaccount(View view) {
        Button button = (Button) findViewById(view.getId());

        if (button.getText().toString().toLowerCase().contains("sblocca")){
            button.setText("BANNA UTENTE!");

            amico.setBlocco(0);
            amico.setAttivo("Attesa");

            ModificaStato as = new ModificaStato(this);
            as.execute(amico.getUsername(), "Attesa");
            as.setOnFinishListener(new ModificaStato.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    ModificaStato.asydata = true;
                }
            });
            while (ModificaStato.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            ModificaStato.asydata = false;

            View toastView = getLayoutInflater().inflate(R.layout.toastsbloccato, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();

            return false;
        }

        if (button.getText().toString().toLowerCase().contains("banna")){
            button.setText("SBLOCCA ACCOUNT!");

            amico.setBlocco(-1);
            amico.setAttivo("Bannato");

            ModificaStato as = new ModificaStato(this);
            as.execute(amico.getUsername(), "Bannato");
            as.setOnFinishListener(new ModificaStato.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errorpwd = "probserver";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errorpwd = "pceserverspento";
                        segnalaerrori = true;
                        ModificaStato.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    ModificaStato.asydata = true;
                }
            });
            while (ModificaStato.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errorpwd);
                }
            }
            segnalaerrori = false;
            ModificaStato.asydata = false;

            View toastView = getLayoutInflater().inflate(R.layout.toastbannato, null);
            Toast toast = Toast.makeText(PaginaProfiloUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();

            return false;
        }

        return false;

    }
}