package oneforyou.jep.oneforyou.View.ViewAdmin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.CustomOnItemSelectedListener;
import oneforyou.jep.oneforyou.Util.DatePickerFragmentPost;
import oneforyou.jep.oneforyou.Control.InserimentoPartita;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaInserisciPartita extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private Partite partita;
    private RelativeLayout barramenu, voce1, voce2;
    private Button voce1a, voce2a;
    private boolean segnalaerrori;
    private String errorpwd;
    private EditText squadracasa, squadraospite, quota1, quotaX, quota2, orapartita, minutipartita, consiglio;
    private Spinner colorecasa, coloreospite;
    private Button datapartita, confermaaggiornamento;
    private String coloresceltocasa = "", coloresceltoospite = "";

    private int giorno, mese, anno;

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
        setContentView(R.layout.layoutinseriscipartita);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");
        partita = (Partite) bundle.getSerializable("PARTITA");

        barramenu = findViewById(R.id.barramenusotto);

        voce1 = findViewById(R.id.buttonutenti1);
        voce2 = findViewById(R.id.buttonpartite1);
        voce1a = findViewById(R.id.buttonutenti2);
        voce2a = findViewById(R.id.buttonpartite2);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        squadracasa = (EditText) findViewById(R.id.squadracasa);
        squadraospite = (EditText) findViewById(R.id.squadraospite);
        quota1  = (EditText) findViewById(R.id.quotacasa);
        quotaX  = (EditText) findViewById(R.id.quotapareggio);
        quota2  = (EditText) findViewById(R.id.quotaospite);
        orapartita = (EditText) findViewById(R.id.orapartita);
        minutipartita  = (EditText) findViewById(R.id.minutipartita);
        consiglio  = (EditText) findViewById(R.id.consiglio);

        colorecasa = (Spinner) findViewById(R.id.colorecasa);
        coloreospite = (Spinner) findViewById(R.id.coloreospite);


        addItemsOnSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        datapartita = (Button) findViewById(R.id.datapartita);

        GregorianCalendar oggi = new GregorianCalendar();
        int annoAttuale = oggi.get(Calendar.YEAR);
        int meseAttuale = oggi.get(Calendar.MONTH) + 1;
        int giornoAttuale = oggi.get(Calendar.DAY_OF_MONTH);

        datapartita.setText(giornoAttuale + "/" + meseAttuale + "/" + annoAttuale);
        datapartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragmentPost();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        confermaaggiornamento = (Button) findViewById(R.id.bottoneOK);

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

        quota1.addTextChangedListener(new TextWatcher() {

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
                    if ((string.length() > 1)) {
                        string = string.substring(0, 1);
                        quota1.setText(string);
                        errorpwd ="";
                    }

                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) < 1) {
                        ignoreChange = true;
                        orapartita.setText("");
                        orapartita.setSelection(orapartita.getText().length());
                        ignoreChange = false;
                    }

                    ignoreChange = true;
                    quota1.setText(string);
                    quota1.setSelection(quota1.getText().length());
                    ignoreChange = false;
                }
            }
        });



        quotaX.addTextChangedListener(new TextWatcher() {

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
                    if ((string.length() > 1)) {
                        string = string.substring(0, 1);
                        quotaX.setText(string);
                        errorpwd ="";
                    }

                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) < 2) {
                        ignoreChange = true;
                        quotaX.setText("");
                        quotaX.setSelection(orapartita.getText().length());
                        ignoreChange = false;
                    }

                    ignoreChange = true;
                    quotaX.setText(string);
                    quotaX.setSelection(quotaX.getText().length());
                    ignoreChange = false;
                }
            }
        });



        quota2.addTextChangedListener(new TextWatcher() {

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
                    if ((string.length() > 1)) {
                        string = string.substring(0, 1);
                        quota2.setText(string);
                        errorpwd ="";
                    }

                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) < 1) {
                        ignoreChange = true;
                        orapartita.setText("");
                        orapartita.setSelection(orapartita.getText().length());
                        ignoreChange = false;
                    }

                    ignoreChange = true;
                    quota2.setText(string);
                    quota2.setSelection(quota2.getText().length());
                    ignoreChange = false;
                }
            }
        });



        consiglio.addTextChangedListener(new TextWatcher() {

            boolean ignoreChange = false;
            boolean nonprono = false;

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
                    if ((string.length() > 1)) {
                        string = string.substring(0, 1);
                        consiglio.setText(string);
                    }
                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) > 2) {
                        consiglio.setText("");
                    }
                    ignoreChange = true;
                    consiglio.setSelection(consiglio.getText().length());
                    ignoreChange = false;
                }
            }
        });

        orapartita.addTextChangedListener(new TextWatcher() {

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
                    if ((string.length() > 2)) {
                        string = string.substring(0, 2);
                        orapartita.setText(string);
                        errorpwd ="";
                    }

                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) > 23) {
                        ignoreChange = true;
                        orapartita.setText("00");
                        orapartita.setSelection(orapartita.getText().length());
                        ignoreChange = false;
                    }

                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) < 0) {
                        ignoreChange = true;
                        orapartita.setText("00");
                        orapartita.setSelection(orapartita.getText().length());
                        ignoreChange = false;
                    }
                }
            }
        });

        minutipartita.addTextChangedListener(new TextWatcher() {

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
                    if ((string.length() > 2)) {
                        string = string.substring(0, 2);
                        minutipartita.setText(string);
                        errorpwd ="";
                    }

                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) > 59) {
                        ignoreChange = true;
                        minutipartita.setText("00");
                        minutipartita.setSelection(minutipartita.getText().length());
                        ignoreChange = false;
                    }

                    if (!(string.equalsIgnoreCase("")) && Integer.parseInt(string) < 0) {
                        ignoreChange = true;
                        minutipartita.setText("00");
                        minutipartita.setSelection(minutipartita.getText().length());
                        ignoreChange = false;
                    }
                }
            }
        });



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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.azzurros), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.blus), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.giallos), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.rossos), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.verdes), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.granatas), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.aranciones), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
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
            squadracasa.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            squadraospite.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            quota1.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            quotaX.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            quota2.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            orapartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            minutipartita.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
            consiglio.getBackground().mutate().setColorFilter(getResources().getColor(R.color.neros), PorterDuff.Mode.SRC_ATOP);
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

        colorecasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String colelto = colorecasa.getSelectedItem().toString();

                if (colelto.toLowerCase().contains("colore")) {
                    colorecasa.setBackgroundResource(R.drawable.edittextcentro);
                }

                if (colelto.toLowerCase().contains("azzurro")) {
                    colorecasa.setBackgroundResource(R.drawable.azzurrocolor);
                }

                if (colelto.toLowerCase().contains("blu")) {
                    colorecasa.setBackgroundResource(R.drawable.blucolor);
                }

                if (colelto.toLowerCase().contains("giallo")) {
                    colorecasa.setBackgroundResource(R.drawable.giallocolor);
                }

                if (colelto.toLowerCase().contains("rosso")) {
                    colorecasa.setBackgroundResource(R.drawable.rossocolor);
                }

                if (colelto.toLowerCase().contains("verde")) {
                    colorecasa.setBackgroundResource(R.drawable.verdecolor);
                }

                if (colelto.toLowerCase().contains("granata")) {
                    colorecasa.setBackgroundResource(R.drawable.granatacolor);
                }

                if (colelto.toLowerCase().contains("arancione")) {
                    colorecasa.setBackgroundResource(R.drawable.arancionecolor);
                }

                if (colelto.toLowerCase().contains("viola")) {
                    colorecasa.setBackgroundResource(R.drawable.violacolor);
                }

                if (colelto.toLowerCase().contains("bianco")) {
                    colorecasa.setBackgroundResource(R.drawable.biancocolor);
                }

                if (colelto.toLowerCase().contains("nero")) {
                    colorecasa.setBackgroundResource(R.drawable.nerocolor);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });




        coloreospite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String colelto = coloreospite.getSelectedItem().toString();

                if (colelto.toLowerCase().contains("colore")) {
                    coloreospite.setBackgroundResource(R.drawable.edittextcentro);
                }

                if (colelto.toLowerCase().contains("azzurro")) {
                    coloreospite.setBackgroundResource(R.drawable.azzurrocolor);
                }

                if (colelto.toLowerCase().contains("blu")) {
                    coloreospite.setBackgroundResource(R.drawable.blucolor);
                }

                if (colelto.toLowerCase().contains("giallo")) {
                    coloreospite.setBackgroundResource(R.drawable.giallocolor);
                }

                if (colelto.toLowerCase().contains("rosso")) {
                    coloreospite.setBackgroundResource(R.drawable.rossocolor);
                }

                if (colelto.toLowerCase().contains("verde")) {
                    coloreospite.setBackgroundResource(R.drawable.verdecolor);
                }

                if (colelto.toLowerCase().contains("granata")) {
                    coloreospite.setBackgroundResource(R.drawable.granatacolor);
                }

                if (colelto.toLowerCase().contains("arancione")) {
                    coloreospite.setBackgroundResource(R.drawable.arancionecolor);
                }

                if (colelto.toLowerCase().contains("viola")) {
                    coloreospite.setBackgroundResource(R.drawable.violacolor);
                }

                if (colelto.toLowerCase().contains("bianco")) {
                    coloreospite.setBackgroundResource(R.drawable.biancocolor);
                }

                if (colelto.toLowerCase().contains("nero")) {
                    coloreospite.setBackgroundResource(R.drawable.nerocolor);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int i, int i1, int i2) {
        giorno = i2;
        mese = i1 + 1;
        anno = i;
        datapartita.setText(giorno + "/" + mese + "/" + anno);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addListenerOnSpinnerItemSelection() {
        colorecasa.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        coloresceltocasa = colorecasa.getSelectedItem().toString();
        if (coloresceltocasa.toLowerCase().contains("azzurro")) {
            Drawable x = getDrawable(R.drawable.azzurrocolor);
            colorecasa.setBackgroundResource(R.drawable.azzurrocolor);
        }
        coloreospite.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        coloresceltoospite = coloreospite.getSelectedItem().toString();
        if (coloresceltoospite.toLowerCase().contains("azzurro")) {
            Drawable x = getDrawable(R.drawable.azzurrocolor);
            coloreospite.setBackgroundResource(R.drawable.azzurrocolor);
        }
    }


    private void addItemsOnSpinner() {
        colorecasa = (Spinner) findViewById(R.id.colorecasa);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.colori, R.layout.layoutspinner);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorecasa.setAdapter(adapter1);

        coloreospite = (Spinner) findViewById(R.id.coloreospite);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.colori, R.layout.layoutspinner);


        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coloreospite.setAdapter(adapter2);
    }


    // get the selected dropdown list value
    public void addListenerOnButton() {
        colorecasa = (Spinner) findViewById(R.id.colorecasa);
        coloreospite = (Spinner) findViewById(R.id.coloreospite);
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

    }

    private void toastview(String error) {
        if (error.equals("inserita"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpartitainserita, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            Intent intent;
            Bundle bundle;
            bundle = new Bundle();
            bundle.putSerializable("UTENTE", loggato);
            intent = new Intent(getApplicationContext(), PaginaGestionePartite.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (error.equals("problemi"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastproblemiinserimento, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
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

    public boolean inserisciPartita(View view) {
        if (squadracasa.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastsquadracasa, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            squadracasa.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (squadraospite.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastsquadraospite, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            squadraospite.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (colorecasa.getSelectedItem().toString().equalsIgnoreCase("colore")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastcolorecasamancante, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            colorecasa.performClick();
            return false;
        }
        if (coloreospite.getSelectedItem().toString().equalsIgnoreCase("colore")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastcoloreospitemancante, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            coloreospite.performClick();
            return false;
        }
        if (quota1.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastquotauno, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            quota1.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (quotaX.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastquotaics, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            quotaX.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (quota2.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastquotadue, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            quota2.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (orapartita.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastorariopartita, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            orapartita.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (minutipartita.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastcalciodinizio, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            minutipartita.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (consiglio.getText().toString().equalsIgnoreCase("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastconsiglio, null);
            Toast toast = Toast.makeText(PaginaInserisciPartita.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            consiglio.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }

        String sc = squadracasa.getText().toString();
        String so = squadraospite.getText().toString();
        String cc = colorecasa.getSelectedItem().toString();
        String co = coloreospite.getSelectedItem().toString();
        String q1 = quota1.getText().toString();
        String qx = quotaX.getText().toString();
        String q2 = quota2.getText().toString();
        String data = datapartita.getText().toString();
        String[] separati = data.split("/");
        String d = separati[2] + "-" + separati[1] + "-" + separati[0];
        String h = orapartita.getText().toString();
        String m = minutipartita.getText().toString();
        String p = consiglio.getText().toString();

        if (controllointernet()) {
        InserimentoPartita ip = new InserimentoPartita(this);
        ip.execute(sc, so, cc, co, q1, qx, q2, d, h, m, p);
        ip.setOnFinishListener(new InserimentoPartita.onFinishListener() {
            @Override
            public void onFinish(String result) {
                if (result.contains("java.net.ConnectException")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                }
                if (result.contains("java.net.UnsupportedEncoding")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                }
                if (result.contains("java.net.Protocol")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                }
                if (result.contains("java.net.MalformedUrl")) {
                    errorpwd = "probserver";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                }
                if (result.contains("java.net.IO")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                }
                if (result.contains("java.net.SocketTimeoutException")) {
                    errorpwd = "pceserverspento";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                    //stampare risultato asynctask
                }
                if (result.contains("OK")) {
                    errorpwd = "inserita";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                }
                if (result.contains("Problemi")) {
                    errorpwd = "problemi";
                    segnalaerrori = true;
                    InserimentoPartita.asydata = true;
                    return;
                }
                InserimentoPartita.asydata = true;
            }
        });
        while (InserimentoPartita.asydata == false) {
            if (segnalaerrori == true) {
                toastview(errorpwd);
            }
        }
        segnalaerrori = false;
        InserimentoPartita.asydata = false;
    }




        return false;
    }


    private boolean controllointernet() {
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

