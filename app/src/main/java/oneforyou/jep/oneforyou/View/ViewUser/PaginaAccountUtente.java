package oneforyou.jep.oneforyou.View.ViewUser;

/* author: peppe
   version: 1.2 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oneforyou.jep.oneforyou.Control.AggiornaColore;
import oneforyou.jep.oneforyou.Control.AggiornaFoto;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Control.CaricaFoto;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Control.ControlloDati;
import oneforyou.jep.oneforyou.Util.CustomOnItemSelectedListener;
import oneforyou.jep.oneforyou.Util.DatePickerFragmentAnte18;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.Util.UploadImage;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaAccountUtente extends FragmentActivity
        implements DatePickerDialog.OnDateSetListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private RelativeLayout barramenu, voce1, voce2, voce3, voce4;
    private boolean segnalaerrori;
    private int risultatoquery;
    private String errorpwd;
    public static final String UPLOAD_KEY = "image";
    private ArrayList<Utenti> utenti = new ArrayList<>();
    private ArrayList<Bitmap> fotosutenti = new ArrayList<>();
    private Button selectdata, modificaDati, confermamodifica, eliminaaccount, replaytutorial;
    private RelativeLayout utente;
    private LinearLayout riga;
    private EditText nomemodifica, cognomemodifica, luogomodifica, mailmodifica, telefonomodifica, passmodifica1, passmodifica2;
    private TextView photo, nomeutente, cognomeutente, luogoutente, mailutente, telefonoutente, sessoutente, datautente, coloreutente;
    private int giorno, mese, anno;
    private Spinner spinner;
    private Bitmap bitmap;
    private final int IMG_REQUEST = 1;
    private ImageView fotomodifica;
    private Bitmap bitmap2 = null;

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
        setContentView(R.layout.layoutaccountutente);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);
        spinner = (Spinner) findViewById(R.id.coloremodifica);

        confermamodifica = (Button) findViewById(R.id.confermamodifica);


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
                        bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        immagine.setImageBitmap(bitmap);
                        //immagine.setBackground(getResources().getDrawable(R.drawable.editcentro));
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });
        }

        addItemsOnSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String colelto = spinner.getSelectedItem().toString();

                if (colelto.toLowerCase().contains("colore")) {
                    cambiaColore("azzurro");
                    spinner.setBackgroundResource(R.drawable.edittextcentro);
                }

                if (colelto.toLowerCase().contains("azzurro")) {
                    cambiaColore("azzurro");
                    spinner.setBackgroundResource(R.drawable.azzurrocolor);
                }

                if (colelto.toLowerCase().contains("blu")) {
                    cambiaColore("blu");
                    spinner.setBackgroundResource(R.drawable.blucolor);
                }

                if (colelto.toLowerCase().contains("giallo")) {
                    cambiaColore("giallo");
                    spinner.setBackgroundResource(R.drawable.giallocolor);
                }

                if (colelto.toLowerCase().contains("rosso")) {
                    cambiaColore("rosso");
                    spinner.setBackgroundResource(R.drawable.rossocolor);
                }

                if (colelto.toLowerCase().contains("verde")) {
                    cambiaColore("verde");
                    spinner.setBackgroundResource(R.drawable.verdecolor);
                }

                if (colelto.toLowerCase().contains("granata")) {
                    cambiaColore("granata");
                    spinner.setBackgroundResource(R.drawable.granatacolor);
                }

                if (colelto.toLowerCase().contains("arancione")) {
                    cambiaColore("arancione");
                    spinner.setBackgroundResource(R.drawable.arancionecolor);
                }

                if (colelto.toLowerCase().contains("viola")) {
                    cambiaColore("viola");
                    spinner.setBackgroundResource(R.drawable.violacolor);
                }

                if (colelto.toLowerCase().contains("bianco")) {
                    cambiaColore("bianco");
                    spinner.setBackgroundResource(R.drawable.biancocolor);
                }

                if (colelto.toLowerCase().contains("nero")) {
                    cambiaColore("nero");
                    spinner.setBackgroundResource(R.drawable.nerocolor);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        modificaDati = (Button) findViewById(R.id.modificadati);
        eliminaaccount = (Button) findViewById(R.id.eliminaaccount);
        replaytutorial = (Button) findViewById(R.id.replaytutorial);

        selectdata = (Button) findViewById(R.id.datamodifica);
        String datanas = loggato.getData();
        anno = Integer.parseInt(datanas.substring(0, 4));
        mese = Integer.parseInt(datanas.substring(5, 7));
        giorno = Integer.parseInt(datanas.substring(8, 10));
        selectdata.setText(giorno + "/" + mese + "/" + anno);
        selectdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragmentAnte18();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        utente = (RelativeLayout) findViewById(R.id.utente);

        barramenu = findViewById(R.id.barramenu);
        voce1 = findViewById(R.id.voce1);
        voce2 = findViewById(R.id.voce2);
        voce3 = findViewById(R.id.voce3);
        voce4 = findViewById(R.id.voce4);
        TextView usernametv = (TextView) findViewById(R.id.user);
        TextView infotv = (TextView) findViewById(R.id.info);
        TextView statstv = (TextView) findViewById(R.id.stats);
        riga = (LinearLayout) findViewById(R.id.riga);
        nomemodifica = (EditText) findViewById(R.id.nomemodifica);
        cognomemodifica = (EditText) findViewById(R.id.cognomemodifica);
        luogomodifica = (EditText) findViewById(R.id.luogomodifica);
        telefonomodifica = (EditText) findViewById(R.id.telmodifica);
        nomeutente = (TextView) findViewById(R.id.nomeutente);
        cognomeutente = (TextView) findViewById(R.id.cognomeutente);
        photo = (TextView) findViewById(R.id.photo);
        luogoutente = (TextView) findViewById(R.id.luogoutente);
        mailutente = (TextView) findViewById(R.id.mailutente);
        sessoutente = (TextView) findViewById(R.id.sessoutente);
        datautente = (TextView) findViewById(R.id.datautente);
        coloreutente = (TextView) findViewById(R.id.coloreutente);
        telefonoutente = (TextView) findViewById(R.id.telutente);
        mailmodifica = (EditText) findViewById(R.id.mailmodifica);
        telefonomodifica = (EditText) findViewById(R.id.telmodifica);
        passmodifica1 = (EditText) findViewById(R.id.passmodifica1);
        passmodifica2 = (EditText) findViewById(R.id.passmodifica2);
        fotomodifica = (ImageView) findViewById(R.id.fotomodifica);
        final ImageView fotoiv = (ImageView) findViewById(R.id.foto);




        nomeutente.setText(loggato.getNome());
        cognomeutente.setText(loggato.getCognome());
        luogoutente.setText(loggato.getLuogo());
        telefonoutente.setText(loggato.getTelefono());
        mailutente.setText(loggato.getEmail());
        if (loggato.getSesso() == 1) {
            sessoutente.setText("UOMO");
        }
        if (loggato.getSesso() == 0) {
            sessoutente.setText("DONNA");
        }
        datanas = loggato.getData();
        anno = Integer.parseInt(datanas.substring(0, 4));
        mese = Integer.parseInt(datanas.substring(5, 7));
        giorno = Integer.parseInt(datanas.substring(8, 10));
        datautente.setText(giorno + "/" + mese + "/" + anno);
        coloreutente.setText(loggato.getColore());

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
            riga.setBackgroundResource(R.color.azzurrocc);
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.azzurros));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.azzurros));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.azzurros));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.azzurros));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce3.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
            riga.setBackgroundResource(R.color.blucc);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.blus));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.blus));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.blus));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.blus));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.giallos));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.giallos));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.giallos));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.giallos));
            voce3.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
            riga.setBackgroundResource(R.color.giallocc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.rossos));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.rossos));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.rossos));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.rossos));
            voce3.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
            riga.setBackgroundResource(R.color.rossocc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.verdes));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.verdes));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.verdes));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.verdes));
            voce3.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
            riga.setBackgroundResource(R.color.verdecc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.granatas));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.granatas));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.granatas));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.granatas));
            voce3.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
            riga.setBackgroundResource(R.color.granatacc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce3.setBackgroundResource(R.color.arancionec);
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.aranciones));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.aranciones));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.aranciones));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.aranciones));
            utente.setBackgroundResource(R.color.arancione);
            riga.setBackgroundResource(R.color.arancionecc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce3.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
            riga.setBackgroundResource(R.color.biancocc);
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.neros));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.neros));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.neros));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.neros));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce3.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
            riga.setBackgroundResource(R.color.nerocc);
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.neros));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.neros));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.neros));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.neros));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce3.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
            riga.setBackgroundResource(R.color.violacc);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.violas));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.violas));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.violas));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.violas));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
        }



        PrendiFoto pf = new PrendiFoto(this);
        pf.execute(Connector.db + "img/" + loggato.getFoto());
        pf.setOnFinishListener(new PrendiFoto.onFinishListener() {
            @Override
            public void onFinish(String res) {
                // The task has finished and you can now use the result
                try {
                    byte[] encodeByte = Base64.decode(res, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    fotoiv.setImageBitmap(bitmap);
                    fotomodifica.setImageBitmap(bitmap);
                    //immagine.setBackground(getResources().getDrawable(R.drawable.editcentro));
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        if (loggato.getRuolo().equalsIgnoreCase("tipster")) {
            usernametv.setText("♚ " + loggato.getUsername() + " ♚");
        }

        else {
            usernametv.setText(loggato.getUsername() + "");
        }


        infotv.setText("Recensioni positive: " + loggato.getRecensioni() + " - Seguito da " +  loggato.getSeguaci() + " utenti");
        statstv.setText("Vinte " + loggato.getVinte() + " su " + loggato.getScommesse() + " (media del " + String.valueOf(loggato.getMedia() + "%)"));

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

    private void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.coloremodifica);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colori, R.layout.layoutspinner);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    // get the selected dropdown list value
    public void addListenerOnButton() {
        spinner = (Spinner) findViewById(R.id.coloremodifica);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addListenerOnSpinnerItemSelection() {
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        String colorescelto = spinner.getSelectedItem().toString();
        if (colorescelto.toLowerCase().contains("azzurro")) {
            Drawable x = getDrawable(R.drawable.azzurrocolor);
            spinner.setBackgroundResource(R.drawable.azzurrocolor);
        }
        if (spinner.getSelectedItem().toString().toLowerCase().equals("blu"))
            spinner.setBackgroundColor(getResources().getColor(R.color.blu));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("giallo"))
            spinner.setBackgroundColor(getResources().getColor(R.color.giallo));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("rosso"))
            spinner.setBackgroundColor(getResources().getColor(R.color.rosso));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("verde"))
            spinner.setBackgroundColor(getResources().getColor(R.color.verde));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("granata"))
            spinner.setBackgroundColor(getResources().getColor(R.color.granata));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("arancione"))
            spinner.setBackgroundColor(getResources().getColor(R.color.arancione));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("viola"))
            spinner.setBackgroundColor(getResources().getColor(R.color.viola));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("bianco"))
            spinner.setBackgroundColor(getResources().getColor(R.color.bianco));
        if (spinner.getSelectedItem().toString().toLowerCase().equals("nero"))
            spinner.setBackgroundColor(getResources().getColor(R.color.nero));

    }


    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaAccountUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaAccountUtente.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
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

    private void cambiaColore(String colore) {

        if (colore.equalsIgnoreCase("colore")) {
            int x = (int) (Math.random() * ((9 - 1) + 1)) + 1;

            if (x == 1) {
                cambiaColore("azzurro");
            }
            if (x == 2) {
                cambiaColore("blu");
            }
            if (x == 3) {
                cambiaColore("giallo");
            }
            if (x == 4) {
                cambiaColore("rosso");
            }
            if (x == 5) {
                cambiaColore("verde");
            }
            if (x == 6) {
                cambiaColore("granata");
            }
            if (x == 7) {
                cambiaColore("arancione");
            }
            if (x == 8) {
                cambiaColore("bianco");
            }
            if (x == 9) {
                cambiaColore("nero");
            }
            if (x == 10) {
                cambiaColore("viola");
            }
        }
        if (colore.equalsIgnoreCase("azzurro")) {
            barramenu.setBackgroundResource(R.color.azzurros);
            voce3.setBackgroundResource(R.color.azzurroc);
            utente.setBackgroundResource(R.color.azzurro);
            riga.setBackgroundResource(R.color.azzurrocc);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.azzurros));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.azzurros));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.azzurros));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.azzurros));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("blu")) {
            barramenu.setBackgroundResource(R.color.blus);
            voce3.setBackgroundResource(R.color.bluc);
            utente.setBackgroundResource(R.color.blu);
            riga.setBackgroundResource(R.color.blucc);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.blus));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.blus));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.blus));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.blus));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("giallo")) {
            barramenu.setBackgroundResource(R.color.giallos);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.giallos));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.giallos));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.giallos));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.giallos));
            voce3.setBackgroundResource(R.color.gialloc);
            utente.setBackgroundResource(R.color.giallo);
            riga.setBackgroundResource(R.color.giallocc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("rosso")) {
            barramenu.setBackgroundResource(R.color.rossos);
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.rossos));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.rossos));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.rossos));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.rossos));
            voce3.setBackgroundResource(R.color.rossoc);
            utente.setBackgroundResource(R.color.rosso);
            riga.setBackgroundResource(R.color.rossocc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("verde")) {
            barramenu.setBackgroundResource(R.color.verdes);
            modificaDati.setBackgroundColor(getResources().getColor(R.color.verdes));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.verdes));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.verdes));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.verdes));
            voce3.setBackgroundResource(R.color.verdec);
            utente.setBackgroundResource(R.color.verde);
            riga.setBackgroundResource(R.color.verdecc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("granata")) {
            barramenu.setBackgroundResource(R.color.granatas);
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.granatas));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.granatas));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.granatas));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.granatas));
            voce3.setBackgroundResource(R.color.granatac);
            utente.setBackgroundResource(R.color.granata);
            riga.setBackgroundResource(R.color.granatacc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("arancione")) {
            barramenu.setBackgroundResource(R.color.aranciones);
            voce3.setBackgroundResource(R.color.arancionec);
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.aranciones));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.aranciones));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.aranciones));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.aranciones));
            utente.setBackgroundResource(R.color.arancione);
            riga.setBackgroundResource(R.color.arancionecc);
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("bianco")) {
            barramenu.setBackgroundResource(R.color.biancos);
            voce3.setBackgroundResource(R.color.biancoc);
            utente.setBackgroundResource(R.color.bianco);
            riga.setBackgroundResource(R.color.biancocc);
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.neros));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.neros));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.neros));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.neros));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("nero")) {
            barramenu.setBackgroundResource(R.color.neros);
            voce3.setBackgroundResource(R.color.neroc);
            utente.setBackgroundResource(R.color.nero);
            riga.setBackgroundResource(R.color.nerocc);
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.neros));
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.neros));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.neros));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.neros));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
        }

        if (colore.equalsIgnoreCase("viola")) {
            barramenu.setBackgroundResource(R.color.violas);
            voce3.setBackgroundResource(R.color.violac);
            utente.setBackgroundResource(R.color.viola);
            riga.setBackgroundResource(R.color.violacc);
            eliminaaccount.setBackgroundColor(getResources().getColor(R.color.violas));
            replaytutorial.setBackgroundColor(getResources().getColor(R.color.violas));
            modificaDati.setBackgroundColor(getResources().getColor(R.color.violas));
            confermamodifica.setBackgroundColor(getResources().getColor(R.color.violas));
            cognomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            luogomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            mailmodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            telefonomodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            passmodifica1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            passmodifica2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
            nomemodifica.getBackground().mutate().setColorFilter(getResources().getColor(R.color.violas), PorterDuff.Mode.SRC_ATOP);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
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


    private boolean controllodata() {
        if (selectdata.getText().toString().contains("DATA")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastdatavuota, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            selectdata.callOnClick();
            return false;
        }
        if (anno < 1900) {
            View toastView = getLayoutInflater().inflate(R.layout.toastdatafalsa, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            selectdata.callOnClick();
            return false;
        }
        return true;
    }

    public void abilitamodifica(View view) {
        Button button = (Button) view;
        boolean scritta = button.getText().toString().equalsIgnoreCase("MODIFICA DATI");
        if (scritta)
        {
//            nomemodifica.setVisibility(View.VISIBLE);
//            cognomemodifica.setVisibility(View.VISIBLE);
//            luogomodifica.setVisibility(View.VISIBLE);
//            mailmodifica.setVisibility(View.VISIBLE);
//            telefonomodifica.setVisibility(View.VISIBLE);
//            passmodifica1.setVisibility(View.VISIBLE);
//            passmodifica2.setVisibility(View.VISIBLE);
//            selectdata.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            confermamodifica.setVisibility(View.VISIBLE);
            photo.setVisibility(View.VISIBLE);
            fotomodifica = (ImageView) findViewById(R.id.fotomodifica);
            fotomodifica.setVisibility(View.VISIBLE);
            fotomodifica.setImageBitmap(bitmap);

            if (loggato.getColore().equalsIgnoreCase("colore")){
                spinner.setSelection(0);
            }
            if (loggato.getColore().equalsIgnoreCase("azzurro")){
                spinner.setSelection(1);
            }
            if (loggato.getColore().equalsIgnoreCase("blu")){
                spinner.setSelection(2);
            }
            if (loggato.getColore().equalsIgnoreCase("viola")){
                spinner.setSelection(3);
            }
            if (loggato.getColore().equalsIgnoreCase("granata")){
                spinner.setSelection(4);
            }
            if (loggato.getColore().equalsIgnoreCase("rosso")){
                spinner.setSelection(5);
            }
            if (loggato.getColore().equalsIgnoreCase("arancione")){
                spinner.setSelection(6);
            }
            if (loggato.getColore().equalsIgnoreCase("giallo")){
                spinner.setSelection(7);
            }
            if (loggato.getColore().equalsIgnoreCase("verde")){
                spinner.setSelection(8);
            }
            if (loggato.getColore().equalsIgnoreCase("bianco")){
                spinner.setSelection(9);
            }
            if (loggato.getColore().equalsIgnoreCase("nero")){
                spinner.setSelection(10);
            }
            cambiaColore(spinner.getSelectedItem().toString());
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
            spinner.setVisibility(View.GONE);
            bitmap2 = null;
            confermamodifica.setVisibility(View.GONE);
            fotomodifica.setVisibility(View.GONE);
            cambiaColore(loggato.getColore());
            button.setText("MODIFICA DATI");
        }
    }

    public void prendifoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMG_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == IMG_REQUEST && resultcode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap2 = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                if (fotomodifica.getVisibility() == View.GONE) {
                    fotomodifica.setVisibility(View.VISIBLE);
                }
                fotomodifica.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void uploadImage(/*View view*/) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connector.db + "script/upload.php?", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
//                    picture.setImageResource(0);
//                    picture.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", loggato.getNome() + ".jpg");
                if (bitmap2 != null) {
                    params.put("image", imageToString(bitmap2));
                }
                else {
                    params.put("image", imageToString(bitmap));
                }
                return params;
            }
        };

        UploadImage.getInstance(PaginaAccountUtente.this).addtoRequestQue(stringRequest);
    }


    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onDateSet(DatePicker view, int i, int i1, int i2) {
            giorno = i2;
            mese = i1 + 1;
            anno = i;
            selectdata.setText(giorno + "/" + mese + "/" + anno);
    }

    public void confermaModificaDati(View view) {
        if (bitmap2 != null) {
            CaricaFoto cf = new CaricaFoto(getApplicationContext(), bitmap2, loggato.getUsername());
            cf.execute();
        }
        if (bitmap2 == null) {
            CaricaFoto cf = new CaricaFoto(getApplicationContext(), bitmap, loggato.getUsername());
            cf.execute();
        }

        AggiornaColore ac = new AggiornaColore(this);
        ac.execute(loggato.getUsername(), spinner.getSelectedItem().toString());
        ac.setOnFinishListener(new AggiornaColore.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaColore.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaColore.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaColore.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaColore.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AggiornaColore.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AggiornaColore.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                AggiornaColore.asydata = true;
            }
        });
        while (AggiornaColore.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        AggiornaColore.asydata = false;

        AggiornaFoto af = new AggiornaFoto(this);
        af.execute(loggato.getUsername());
        af.setOnFinishListener(new AggiornaFoto.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaFoto.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaFoto.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaFoto.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    AggiornaFoto.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AggiornaFoto.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    AggiornaFoto.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                AggiornaFoto.asydata = true;
            }
        });
        while (AggiornaFoto.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        AggiornaFoto.asydata = false;

        View toastView = getLayoutInflater().inflate(R.layout.toastcambiatidati, null);
        Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.show();

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

        Intent intent = new Intent(getApplicationContext(), PaginaHome.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("UTENTE", loggato);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}