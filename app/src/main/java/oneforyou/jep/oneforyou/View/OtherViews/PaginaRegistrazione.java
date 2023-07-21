package oneforyou.jep.oneforyou.View.OtherViews;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import oneforyou.jep.oneforyou.Control.CaricaFoto;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Control.ControlloMail;
import oneforyou.jep.oneforyou.Control.ControlloTelefono;
import oneforyou.jep.oneforyou.Control.ControlloUsername;
import oneforyou.jep.oneforyou.Util.CustomOnItemSelectedListener;
import oneforyou.jep.oneforyou.Util.DatePickerFragmentAnte18;
import oneforyou.jep.oneforyou.Util.Encryptor;
import oneforyou.jep.oneforyou.Util.InserimentoCampoPassword;
import oneforyou.jep.oneforyou.Control.InvioDatiGET;
import oneforyou.jep.oneforyou.Control.InvioDatiPOST;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.Util.UploadImage;

public class PaginaRegistrazione extends FragmentActivity
        implements DatePickerDialog.OnDateSetListener {

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


    public static final Pattern PAROLA_PATTERN = Pattern.compile(
            "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");

    public static final Pattern NUMERI_PATTERN = Pattern.compile("^[0-9]*$");

    public static final Pattern USER_PATTERN = Pattern.compile(
            "^(?=.{5,15}$)[a-zA-Z0-9]+");

    public static final Pattern PWD_PATTERN = Pattern.compile(
            "^(?=.{5,15}$)(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+");

    public static final Pattern CHAR_PATTERN = Pattern.compile(
            "/[^a-zA-Z0-9]/");

   /*^(?=.{5,15}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    └─────┬────┘└───┬──┘└─────┬─────┘└─────┬─────┘ └───┬───┘
            │         │         │            │           no _ or . at the end
            │         │         │            allowed characters
            │         │         no __ or _. or ._ or .. inside
            │         no _ or . at the beginning
    username is 5-15 characters long*/

    public volatile boolean risultatonick = false, risultatotel = false, risultatomail = false;


    private final String UPLOAD_URL = Connector.db + "img/";
    private DatabaseReference mDatabase;
    private Typeface myfont, myfontdoppio, myfontsottile;

    private ArrayList<String> usernamearray = new ArrayList<String>(), passwordarray = new ArrayList<String>();
    /*private VideoView vv;*/
    private FirebaseAuth mAuth;
    private TextView invito, controllouser, controllouser2, controllopass, controllopassa, controllopass2, controllopassb;
    private ImageView iv;
    private EditText user, email, telefono, nome, cognome, luogo, pass, pass2;
    private RadioButton mm, ff;
    private Spinner spinner;
    private Button chooseimage, posttest, selectdata, occhio;
    private int sex = 2, giorno, mese, anno;
    private String pwd, pwd2;
    private boolean isPasswordVisible = false, isImageSelected = false;

    JSONObject jsonObject;
    RequestQueue rQueue;

    private ImageView picture;
    private Uri indirizzo;
    private String uri, colorescelto, ba1;
    private Bitmap bitmap;

    public InvioDatiGET invio;
    public static final String UPLOAD_KEY = "image";
    private final int IMG_REQUEST = 1;
    private boolean emailvalida = false, telefonovalido = false;
    private static String errornick = "", errormail = "", errortel = "";
    private boolean segnalaerrori = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutregistrazione);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        uri = "";

        mm = (RadioButton) findViewById(R.id.maschio);
        ff = (RadioButton) findViewById(R.id.femmina);

        user = (EditText) findViewById(R.id.dgtuser);
        controllouser = (TextView) findViewById(R.id.controllouser);
        controllouser2 = (TextView) findViewById(R.id.controllouser2);

        pass = (EditText) findViewById(R.id.dgtpass1);
        pass2 = (EditText) findViewById(R.id.dgtpass2);
        occhio = (Button) findViewById(R.id.occhio);
        controllopass = (TextView) findViewById(R.id.controllopass1);
        controllopassa = (TextView) findViewById(R.id.controllopassa);
        controllopass2 = (TextView) findViewById(R.id.controllopass2);
        controllopassb = (TextView) findViewById(R.id.controllopassb);

        email = (EditText) findViewById(R.id.dgtemail);
        telefono = (EditText) findViewById(R.id.dgttelefono);
        nome = (EditText) findViewById(R.id.dgtnome);
        cognome = (EditText) findViewById(R.id.dgtcognome);
        luogo = (EditText) findViewById(R.id.dgtluogo);

        spinner = (Spinner) findViewById(R.id.colorchoose);

        posttest = (Button) findViewById(R.id.post);

        selectdata = (Button) findViewById(R.id.datanascita);
        selectdata.setText("DATA NASCITA");
        selectdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragmentAnte18();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        chooseimage = (Button) findViewById(R.id.immagine);

        picture = (ImageView) findViewById(R.id.profilo);

        invito = (TextView) findViewById(R.id.sopraform);

        user.setTypeface(myfontdoppio);
        pass.setTypeface(myfontdoppio);
        pass2.setTypeface(myfontdoppio);
        controllopass.setTypeface(myfontsottile);
        controllopass2.setTypeface(myfontsottile);
        controllopassa.setTypeface(myfontsottile);
        controllopassb.setTypeface(myfontsottile);
        email.setTypeface(myfontdoppio);
        telefono.setTypeface(myfontdoppio);
        nome.setTypeface(myfontdoppio);
        cognome.setTypeface(myfontdoppio);
        luogo.setTypeface(myfontdoppio);
        posttest.setTypeface(myfontdoppio);
        selectdata.setTypeface(myfontdoppio);
        chooseimage.setTypeface(myfontsottile);
        invito.setTypeface(myfontdoppio);
        mm.setTypeface(myfontdoppio);
        ff.setTypeface(myfontdoppio);

        user.setTextColor(Color.argb(255, 0, 0, 0));
        pass.setTextColor(Color.argb(255, 0, 0, 0));
        pass2.setTextColor(Color.argb(255, 0, 0, 0));
        controllopass.setTextColor(Color.argb(255, 0, 0, 0));
        controllopass2.setTextColor(Color.argb(255, 0, 0, 0));
        controllopassa.setTextColor(Color.argb(255, 0, 0, 0));
        controllopassb.setTextColor(Color.argb(255, 0, 0, 0));
        email.setTextColor(Color.argb(255, 0, 0, 00));
        telefono.setTextColor(Color.argb(255, 0, 0, 0));
        nome.setTextColor(Color.argb(255, 0, 0, 0));
        cognome.setTextColor(Color.argb(255, 0, 0, 0));
        luogo.setTextColor(Color.argb(255, 0, 0, 0));
        posttest.setTextColor(Color.argb(255, 0, 0, 0));
        selectdata.setTextColor(Color.argb(255, 0, 0, 0));
        chooseimage.setTextColor(Color.argb(255, 0, 0, 0));
        invito.setTextColor(Color.argb(255, 255, 255, 255));

        user.setHintTextColor(Color.argb(255, 0, 0, 0));
        pass.setHintTextColor(Color.argb(255, 0, 0, 0));
        pass2.setHintTextColor(Color.argb(255, 0, 0, 0));
        controllopass.setHintTextColor(Color.argb(255, 0, 0, 0));
        controllopass2.setHintTextColor(Color.argb(255, 0, 0, 0));
        controllopassa.setHintTextColor(Color.argb(255, 0, 0, 0));
        controllopassb.setHintTextColor(Color.argb(255, 0, 0, 0));
        email.setHintTextColor(Color.argb(255, 0, 0, 0));
        telefono.setHintTextColor(Color.argb(255, 0, 0, 0));
        nome.setHintTextColor(Color.argb(255, 0, 0, 0));
        cognome.setHintTextColor(Color.argb(255, 0, 0, 0));
        luogo.setHintTextColor(Color.argb(255, 0, 0, 0));
        posttest.setHintTextColor(Color.argb(255, 0, 0, 0));
        selectdata.setHintTextColor(Color.argb(255, 0, 0, 0));
        chooseimage.setHintTextColor(Color.argb(255, 0, 0, 0));
        invito.setHintTextColor(Color.argb(255, 0, 0, 0));

        /*mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();*/

        addItemsOnSpinner();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();


        pass.setTransformationMethod(new InserimentoCampoPassword());
        pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        pass.setTextSize(20);

        pass2.setTransformationMethod(new InserimentoCampoPassword());
        pass2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        pass2.setTextSize(20);


        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    user.setHintTextColor(Color.argb(75, 255, 255, 255));
                    user.setTextColor(Color.argb(255, 255, 255, 255));
                    user.setBackgroundResource(R.drawable.editsopra);
                    // Button b = (Button) findViewById(R.id.cancellauser);
                    // b.setVisibility(View.VISIBLE);
                    controllouser.setVisibility(View.VISIBLE);
                    controllouser2.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) controllouser.getLayoutParams();
                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) controllouser2.getLayoutParams();
                    //params.addRule(RelativeLayout.BELOW, R.id.dgtuser);
                    //params.setMargins(0, 0, 0, 10);
                    controllouser.setTypeface(myfontsottile);
                    controllouser.setLayoutParams(params);
                    controllouser2.setTypeface(myfontsottile);
                    controllouser2.setLayoutParams(params2);
                    RelativeLayout.LayoutParams pwd = (RelativeLayout.LayoutParams) pass.getLayoutParams();
                    pwd.addRule(RelativeLayout.BELOW, R.id.controllouser2);
                    pwd.setMargins(0, -2, 0, 0);
                    user.setNextFocusForwardId(R.id.dgtpass1);

                } else {
                    user.setHintTextColor(Color.argb(255, 0, 0, 0));
                    user.setTextColor(Color.argb(255, 0, 0, 0));
                    user.setBackgroundResource(R.drawable.edittextsopra);
                    // Button b = (Button) findViewById(R.id.cancellauser);
                    // b.setVisibility(View.GONE);
                    controllouser.setVisibility(View.GONE);
                    controllouser2.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams pwd = (RelativeLayout.LayoutParams) pass.getLayoutParams();
                    pwd.addRule(RelativeLayout.BELOW, R.id.dgtuser);
                    pwd.setMargins(0, 30, 0, 0);
                }
            }
        });


        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pass.setHintTextColor(Color.argb(75, 255, 255, 255));
                    pass.setTextColor(Color.argb(255, 255, 255, 255));
                    pass.setBackgroundResource(R.drawable.editcentro);
                    pass2.setHintTextColor(Color.argb(75, 255, 255, 255));
                    pass2.setTextColor(Color.argb(255, 255, 255, 255));
                    pass2.setBackgroundResource(R.drawable.editcentro);
                    controllopass.setVisibility(View.VISIBLE);
                    controllopass2.setVisibility(View.VISIBLE);
                    controllopassa.setVisibility(View.VISIBLE);
                    controllopassb.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams eye = (RelativeLayout.LayoutParams) occhio.getLayoutParams();
                    eye.addRule(RelativeLayout.BELOW, R.id.controllopassa);
                    eye.setMargins(0, 10, 0, 65);

                    RelativeLayout.LayoutParams passcon = (RelativeLayout.LayoutParams) pass2.getLayoutParams();
                    passcon.setMargins(0, 32, 0, 0);


                } else {
                    pass.setHintTextColor(Color.argb(255, 0, 0, 0));
                    pass.setTextColor(Color.argb(255, 0, 0, 0));
                    pass.setBackgroundResource(R.drawable.edittextcentro);
                    pass2.setHintTextColor(Color.argb(255, 0, 0, 0));
                    pass2.setTextColor(Color.argb(255, 0, 0, 0));
                    pass2.setBackgroundResource(R.drawable.edittextcentro);
                    controllopass.setVisibility(View.GONE);
                    controllopass2.setVisibility(View.GONE);
                    controllopassa.setVisibility(View.GONE);
                    controllopassb.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams eye = (RelativeLayout.LayoutParams) occhio.getLayoutParams();
                    eye.setMargins(0, 15, 0, 15);

                    RelativeLayout.LayoutParams passcon = (RelativeLayout.LayoutParams) pass2.getLayoutParams();
                    passcon.setMargins(0, 0, 0, 0);
                }
            }
        });

        pass2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pass.setHintTextColor(Color.argb(75, 255, 255, 255));
                    pass.setTextColor(Color.argb(255, 255, 255, 255));
                    pass.setBackgroundResource(R.drawable.editcentro);
                    pass2.setHintTextColor(Color.argb(75, 255, 255, 255));
                    pass2.setTextColor(Color.argb(255, 255, 255, 255));
                    pass2.setBackgroundResource(R.drawable.editcentro);
                    controllopass.setVisibility(View.VISIBLE);
                    controllopass2.setVisibility(View.VISIBLE);
                    controllopassa.setVisibility(View.VISIBLE);
                    controllopassb.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams eye = (RelativeLayout.LayoutParams) occhio.getLayoutParams();
                    eye.addRule(RelativeLayout.BELOW, R.id.controllopassa);
                    eye.setMargins(0, 10, 0, 65);

                    RelativeLayout.LayoutParams passcon = (RelativeLayout.LayoutParams) pass2.getLayoutParams();
                    passcon.setMargins(0, 32, 0, 0);

                } else {
                    pass2.setHintTextColor(Color.argb(255, 0, 0, 0));
                    pass2.setTextColor(Color.argb(255, 0, 0, 0));
                    pass2.setBackgroundResource(R.drawable.edittextcentro);
                    pass.setHintTextColor(Color.argb(255, 0, 0, 0));
                    pass.setTextColor(Color.argb(255, 0, 0, 0));
                    pass.setBackgroundResource(R.drawable.edittextcentro);
                    controllopass.setVisibility(View.GONE);
                    controllopass2.setVisibility(View.GONE);
                    controllopassa.setVisibility(View.GONE);
                    controllopassb.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams eye = (RelativeLayout.LayoutParams) occhio.getLayoutParams();
                    eye.setMargins(0, 15, 0, 0);

                    RelativeLayout.LayoutParams passcon = (RelativeLayout.LayoutParams) pass2.getLayoutParams();
                    passcon.setMargins(0, 15, 0, 0);
                }
            }
        });


        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    email.setHintTextColor(Color.argb(75, 255, 255, 255));
                    email.setTextColor(Color.argb(255, 255, 255, 255));
                    email.setBackgroundResource(R.drawable.editcentro);
                } else {
                    email.setHintTextColor(Color.argb(255, 0, 0, 0));
                    email.setTextColor(Color.argb(255, 0, 0, 0));
                    email.setBackgroundResource(R.drawable.edittextcentro);
                }
            }
        });


        telefono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    telefono.setHintTextColor(Color.argb(75, 255, 255, 255));
                    telefono.setTextColor(Color.argb(255, 255, 255, 255));
                    telefono.setBackgroundResource(R.drawable.editcentro);
                } else {
                    telefono.setHintTextColor(Color.argb(255, 0, 0, 0));
                    telefono.setTextColor(Color.argb(255, 0, 0, 0));
                    telefono.setBackgroundResource(R.drawable.edittextcentro);
                }
            }
        });


        nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nome.setHintTextColor(Color.argb(75, 255, 255, 255));
                    nome.setTextColor(Color.argb(255, 255, 255, 255));
                    nome.setBackgroundResource(R.drawable.editcentro);
                } else {
                    nome.setHintTextColor(Color.argb(255, 0, 0, 0));
                    nome.setTextColor(Color.argb(255, 0, 0, 0));
                    nome.setBackgroundResource(R.drawable.edittextcentro);
                }
            }
        });


        cognome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cognome.setHintTextColor(Color.argb(75, 255, 255, 255));
                    cognome.setTextColor(Color.argb(255, 255, 255, 255));
                    cognome.setBackgroundResource(R.drawable.editcentro);
                } else {
                    cognome.setHintTextColor(Color.argb(255, 0, 0, 0));
                    cognome.setTextColor(Color.argb(255, 0, 0, 0));
                    cognome.setBackgroundResource(R.drawable.edittextcentro);
                }
            }
        });


        luogo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    luogo.setHintTextColor(Color.argb(75, 255, 255, 255));
                    luogo.setTextColor(Color.argb(255, 255, 255, 255));
                    luogo.setBackgroundResource(R.drawable.editcentro);
                } else {
                    luogo.setHintTextColor(Color.argb(255, 0, 0, 0));
                    luogo.setTextColor(Color.argb(255, 0, 0, 0));
                    luogo.setBackgroundResource(R.drawable.edittextcentro);
                }
            }
        });


        telefono.addTextChangedListener(new TextWatcher() {

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
                            errortel = "";
                        } else {
                            telefonovalido = false;
                            errortel ="telefonononvalido";
                        }
                    } else {
                        telefonovalido = false;
                    }
                    if ((string.length() > 10)) {
                        string = string.substring(0, 10);
                        telefono.setText(string);
                        telefonovalido = true;
                        errortel ="";
                    }
                    ignoreChange = true;
                    telefono.setText(string);
                    telefono.setSelection(telefono.getText().length());
                    ignoreChange = false;
                }
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                String colelto = spinner.getSelectedItem().toString();

                if (colelto.toLowerCase().contains("colore")) {
                    spinner.setBackgroundResource(R.drawable.edittextcentro);
                }

                if (colelto.toLowerCase().contains("azzurro")) {
                    spinner.setBackgroundResource(R.drawable.azzurrocolor);
                }

                if (colelto.toLowerCase().contains("blu")) {
                    spinner.setBackgroundResource(R.drawable.blucolor);
                }

                if (colelto.toLowerCase().contains("giallo")) {
                    spinner.setBackgroundResource(R.drawable.giallocolor);
                }

                if (colelto.toLowerCase().contains("rosso")) {
                    spinner.setBackgroundResource(R.drawable.rossocolor);
                }

                if (colelto.toLowerCase().contains("verde")) {
                    spinner.setBackgroundResource(R.drawable.verdecolor);
                }

                if (colelto.toLowerCase().contains("granata")) {
                    spinner.setBackgroundResource(R.drawable.granatacolor);
                }

                if (colelto.toLowerCase().contains("arancione")) {
                    spinner.setBackgroundResource(R.drawable.arancionecolor);
                }

                if (colelto.toLowerCase().contains("viola")) {
                    spinner.setBackgroundResource(R.drawable.violacolor);
                }

                if (colelto.toLowerCase().contains("bianco")) {
                    spinner.setBackgroundResource(R.drawable.biancocolor);
                }

                if (colelto.toLowerCase().contains("nero")) {
                    spinner.setBackgroundResource(R.drawable.nerocolor);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


    }


    //FUORI DALL ONCREATE


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addListenerOnSpinnerItemSelection() {
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        colorescelto = spinner.getSelectedItem().toString();
        if (colorescelto.toLowerCase().contains("azzurro")) {
            Drawable x = getDrawable(R.drawable.azzurrocolor);
            spinner.setBackgroundResource(R.drawable.azzurrocolor);
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
        spinner = (Spinner) findViewById(R.id.colorchoose);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colori, R.layout.layoutspinner);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    // get the selected dropdown list value
    public void addListenerOnButton() {
        spinner = (Spinner) findViewById(R.id.colorchoose);
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.maschio:
                if (checked) {
                    sex = 1;
                    ((RadioButton) view).setButtonDrawable(getResources().getDrawable(R.drawable.uomoblu));
                    RadioButton vj = (RadioButton) findViewById(R.id.femmina);
                    ((RadioButton) vj).setButtonDrawable(getResources().getDrawable(R.drawable.sessovuoto));
                    break;
                }
            case R.id.femmina:
                if (checked) {
                    sex = 0;
                    RadioButton vj = (RadioButton) findViewById(R.id.maschio);
                    ((RadioButton) vj).setButtonDrawable(getResources().getDrawable(R.drawable.sessovuoto));
                    ((RadioButton) view).setButtonDrawable(getResources().getDrawable(R.drawable.donnarossa));
                    break;
                }
        }
    }


    /*public void cancellax(View view) {

        switch (view.getId()) {
            case R.id.cancellauser:
               user.setText("");
                    break;
                }
        }*/


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
                if (chooseimage.getVisibility() == View.VISIBLE)
                    chooseimage.setVisibility(View.GONE);
                bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                if (picture.getVisibility() == View.GONE) {
                    picture.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) posttest.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.profilo);
                    params.setMargins(0, 15, 0, 25);
                }
                picture.setImageBitmap(bitmap);
                bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
                isImageSelected = true;
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
                params.put("name", user.getText().toString() + ".jpg");
                params.put("image", imageToString(bitmap));
                return params;
            }
        };

        UploadImage.getInstance(PaginaRegistrazione.this).addtoRequestQue(stringRequest);
    }


    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        giorno = i2;
        mese = i1 + 1;
        anno = i;
        selectdata.setText(giorno + "/" + mese + "/" + anno);
    }


    public void showhide(View view) {
        if (isPasswordVisible) {
            String pass1 = pass.getText().toString();
            pass.setTransformationMethod(new InserimentoCampoPassword());
            pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            pass.setText(pass1);
            //pass.setTextSize(20);
            pass.setTypeface(myfontdoppio);
            controllopassb.setText("VISUALIZZA");
            pass.setSelection(pass.length());
            String pass3 = pass2.getText().toString();
            pass2.setTransformationMethod(new InserimentoCampoPassword());
            pass2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            pass2.setText(pass3);
            pass2.setTypeface(myfontdoppio);
            //pass2.setTextSize(10);
            pass2.setSelection(pass2.length());
            Button y = (Button) findViewById(R.id.occhio);
            Drawable d = (Drawable) getResources().getDrawable(R.drawable.nonvedi);
            y.setBackgroundDrawable(d);
        } else {
            String passxxx = pass.getText().toString();
            pass.setTransformationMethod(new HideReturnsTransformationMethod());
            pass.setInputType(InputType.TYPE_CLASS_TEXT);
            pass.setText(passxxx);
            pass.setTypeface(myfontdoppio);
            //pass.setTextSize(20);
            controllopassb.setText("NASCONDI");
            pass.setSelection(pass.length());

            String passxx = pass2.getText().toString();
            pass2.setTransformationMethod(new HideReturnsTransformationMethod());
            pass2.setInputType(InputType.TYPE_CLASS_TEXT);
            pass2.setText(passxx);
            //pass2.setTextSize(20);
            pass2.setTypeface(myfontdoppio);
            pass2.setSelection(pass2.length());

            Button y = (Button) findViewById(R.id.occhio);
            Drawable d = (Drawable) getResources().getDrawable(R.drawable.vedi);
            y.setBackgroundDrawable(d);
        }
        isPasswordVisible = !isPasswordVisible;
    }


    public synchronized boolean controllonickname() throws InterruptedException, ExecutionException {
        if (!USER_PATTERN.matcher(user.getText().toString()).matches()) {
            if (user.getText().toString().length() < 5 || user.getText().toString().length() > 15) {
                View toastView = getLayoutInflater().inflate(R.layout.toastcaratteriusername, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                user.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                ControlloUsername.asynick = true;
            } else /*caratteristrani */ {
                View toastView = getLayoutInflater().inflate(R.layout.toastspecialiusername, null);
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                user.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                ControlloUsername.asynick = true;
            }
            return false;
        } else {
            String u = user.getText().toString();
            ControlloUsername task = new ControlloUsername(this);
            task.setOnFinishListener(new ControlloUsername.onFinishListener() {
                @Override
                public void onFinish(String res) {
                    // The task has finished and you can now use the result
                    if (res.contains("Scegline")) {
                        errornick = "usernameusato";
                        risultatonick = false;
                        ControlloUsername.asynick = true;
                    }
                    if (res.contains("disponibile")) {
                        risultatonick = true;
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
                toastview(errornick);
            }
            ControlloUsername.asynick = false;
            return risultatonick;
        }
    }

    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("usernameusato"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastusernameusato, null);
            Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            user.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        if (error.equals("telusato"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toasttelusato, null);
            Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            telefono.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        if (error.equals("mailusata"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastmailusata, null);
            Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            email.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        if (error.equals("telefonononvalido"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toasttelcaratteri, null);
            Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            telefono.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }


    private boolean controllosesso() {
        if (sex > 1 || sex < 0) {
            View toastView = getLayoutInflater().inflate(R.layout.toastnientesesso, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            mm.requestFocus(1);
            return false;
        } else return true;
    }


    private boolean controllomail() {
        String e = email.getText().toString();
        if (e.length() == 0) {
            View toastView = getLayoutInflater().inflate(R.layout.toastvuotomail, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            email.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            risultatomail = false;
            ControlloMail.asydata = true;
        } else {
            if (checkEmail(e)) {
                ControlloMail cd = new ControlloMail(this);
                cd.execute(e);
                cd.setOnFinishListener(new ControlloMail.onFinishListener() {
                    @Override
                    public void onFinish(String result) {
                        if (result.contains("java.net.ConnectException")) {
                            errormail = "probserver";
                            segnalaerrori = true;
                            risultatomail = false;
                            ControlloMail.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.UnsupportedEncoding")) {
                            errormail = "probserver";
                            segnalaerrori = true;
                            ControlloMail.asydata = true;
                            risultatomail = false;
                            return;
                        }
                        if (result.contains("java.net.Protocol")) {
                            errormail = "probserver";
                            segnalaerrori = true;
                            ControlloMail.asydata = true;
                            risultatomail = false;
                            return;
                        }
                        if (result.contains("java.net.MalformedUrl")) {
                            errormail = "probserver";
                            segnalaerrori = true;
                            risultatomail = false;
                            ControlloMail.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.IO")) {
                            errormail = "pceserverspento";
                            segnalaerrori = true;
                            risultatomail = false;
                            ControlloMail.asydata = true;
                            return;
                        }
                        if (result.contains("java.net.SocketTimeoutException")) {
                            errormail = "pceserverspento";
                            segnalaerrori = true;
                            risultatomail = false;
                            ControlloMail.asydata = true;
                            return;
                            //stampare risultato asynctask
                        }
                        if (result.contains("utilizzato")) {
                            errormail = "mailusata";
                            segnalaerrori = true;
                            risultatomail = false;
                            ControlloMail.asydata = true;
                            return;
                        }
                        if (result.contains("valida")) {
                            errormail = "";
                            segnalaerrori = false;
                            risultatomail = true;
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
                errormail="";

            } else {
                View toastView = getLayoutInflater().inflate(R.layout.toastnonvalidomail, null);
                Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                risultatomail = false;
                email.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                ControlloMail.asydata = true;
            }
        }
        return risultatomail;
    }

    private boolean controllotelefono() {
        if (telefonovalido) {
            String t = telefono.getText().toString();
            ControlloTelefono cd = new ControlloTelefono(this);
            cd.execute(t);
            cd.setOnFinishListener(new ControlloTelefono.onFinishListener() {
                @Override
                public void onFinish(String result) {
                    if (result.contains("java.net.ConnectException")) {
                        errortel = "probserver";
                        segnalaerrori = true;
                        risultatotel = false;
                        ControlloTelefono.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.UnsupportedEncoding")) {
                        errortel = "probserver";
                        segnalaerrori = true;
                        ControlloTelefono.asydata = true;
                        risultatotel = false;
                        return;
                    }
                    if (result.contains("java.net.Protocol")) {
                        errortel = "probserver";
                        segnalaerrori = true;
                        ControlloTelefono.asydata = true;
                        risultatotel = false;
                        return;
                    }
                    if (result.contains("java.net.MalformedUrl")) {
                        errortel = "probserver";
                        segnalaerrori = true;
                        risultatotel = false;
                        ControlloTelefono.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.IO")) {
                        errortel = "pceserverspento";
                        segnalaerrori = true;
                        risultatotel = false;
                        ControlloTelefono.asydata = true;
                        return;
                    }
                    if (result.contains("java.net.SocketTimeoutException")) {
                        errortel = "pceserverspento";
                        segnalaerrori = true;
                        risultatotel = false;
                        ControlloTelefono.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    if (result.contains("Abbinato")) {
                        errortel = "telusato";
                        segnalaerrori = true;
                        risultatotel = false;
                        ControlloTelefono.asydata = true;
                        return;
                    }
                    if (result.contains("valido")) {
                        errortel = "";
                        segnalaerrori = false;
                        risultatotel = true;
                        ControlloTelefono.asydata = true;
                        return;
                        //stampare risultato asynctask
                    }
                    ControlloTelefono.asydata = true;
                }
            });
            while (ControlloTelefono.asydata == false) {
                if (segnalaerrori == true) {
                    toastview(errortel);
                }
            }
            segnalaerrori = false;
            ControlloTelefono.asydata = false;

        } else {
            if (errortel.equals("telefonononvalido")) {
                toastview("telefonononvalido");
                telefonovalido = false;
                errortel = "";
                ControlloTelefono.asydata = false;
                segnalaerrori = false;
                return false;
            }
            else {
                View toastView = getLayoutInflater().inflate(R.layout.toasttelnonvalido, null);
                Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                telefono.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                risultatotel = false;
                ControlloTelefono.asydata = false;
                segnalaerrori = false;
            }
        }
        return risultatotel;
    }

    private boolean controllopassword() {
        String passs = pass.getText().toString();
        String passs2 = pass2.getText().toString();
        if (passs.equals("") || passs2.equals("")) {
            View toastView = getLayoutInflater().inflate(R.layout.toastpwdvuote, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            pass.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (!passs.equals(passs2)) {
            View toastView = getLayoutInflater().inflate(R.layout.toastpwdco, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            pass.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            return false;
        }
        if (passs.equals(passs2)) {
            if (!PWD_PATTERN.matcher(passs).matches()) {
                if (passs.length() < 5 || passs.length() > 15) {
                    View toastView = getLayoutInflater().inflate(R.layout.toastpwdlung, null);
                    Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    pass.requestFocus(1);
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
                        pass.requestFocus(1);
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
                        pass.requestFocus(1);
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
                        pass.requestFocus(1);
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
                            pass.requestFocus(1);
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
                    pass.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    return false;
                } else return true;
            }
        }
        return true;
    }

    private boolean controllonome() {
        if (!PAROLA_PATTERN.matcher(nome.getText().toString()).matches()) {
            View toastView = getLayoutInflater().inflate(R.layout.toastnomecontrol, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            nome.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        return PAROLA_PATTERN.matcher(nome.getText().toString()).matches();
    }

    private boolean controllocognome() {
        if (!PAROLA_PATTERN.matcher(cognome.getText().toString()).matches()) {
            View toastView = getLayoutInflater().inflate(R.layout.toastcognomecontrol, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            cognome.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        return PAROLA_PATTERN.matcher(cognome.getText().toString()).matches();
    }


    private boolean controllocitta() {
        if (!PAROLA_PATTERN.matcher(luogo.getText().toString()).matches()) {
            View toastView = getLayoutInflater().inflate(R.layout.toastcittacontrol, null);
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
            luogo.requestFocus(1);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        return PAROLA_PATTERN.matcher(luogo.getText().toString()).matches();
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


    private boolean checkEmail(String mail) {
        return EMAIL_ADDRESS_PATTERN.matcher(mail).matches();
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


    public void provapost(View v) throws InterruptedException, ExecutionException {

        boolean ci = false, cs = false, cn = false, cm = false, ct = false, cp = false, cname = false, cc = false, cl = false, cd = false;

            ci = controllointernet();

        if (ci) {
            cn = controllonickname();
            toastview(errornick);
            errornick = "";
         } else {
            risultatonick = risultatomail = risultatotel = false;
            return;
        }


        if (ci && cn) {
                cp = controllopassword();
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }

        if (ci && cn && cp) {
            cm = controllomail();
            toastview(errormail);
            errormail = "";
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }

        if (ci && cn && cp && cm) {
            ct = controllotelefono();
            toastview(errortel);
            errortel = "";
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }

        if (ci && cn && cm && ct && cp) {
                cname = controllonome();
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }

        if (ci && cn && cm && ct && cp && cname) {
                cc = controllocognome();
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }

        if (ci && cn && cm && ct && cp && cname && cc) {
                cs = controllosesso();
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }

        if (ci && cn && cm && ct && cp && cname && cc && cs) {
                cl = controllocitta();
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }

        if (ci && cn && cm && ct && cp && cname && cc && cl && cs) {
                cd = controllodata();
        } else {
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
            return;
        }


        if (controllointernet() && cn && cm && ct && cp && cname && cc && cl && cd && cs) {
            String data = anno + "-" + mese + "-" + giorno;
            String colore = spinner.getSelectedItem().toString();
            String parametri[] = new String[11];
            parametri[0] = nome.getText().toString();
            parametri[1] = cognome.getText().toString();
            parametri[2] = user.getText().toString();
            parametri[3] = data;
            parametri[4] = email.getText().toString();
            parametri[5] = telefono.getText().toString();
            parametri[6] = luogo.getText().toString();
            parametri[7] = colore;
            if (sex == 1)
                parametri[8] = "1";
            else
                parametri[8] = "0";

            String pwd = pass.getText().toString();
            String pwdcrypt = Encryptor.encryptPassword(pwd);


            /*MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
            String encoded = (String) Base64.encodeToString(hash, 0);*/


            parametri[9] = pwdcrypt;
            if (picture.getVisibility() == View.VISIBLE) {
                // Its visible
                parametri[10] = user.getText().toString() + ".jpg";
                bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
                CaricaFoto cf = new CaricaFoto(getApplicationContext(), bitmap, user.getText().toString());
                cf.execute();
            } else {
                // Either gone or invisible
                parametri[10] = "placeholder.png";
            }

            if (controllointernet()) {
                InvioDatiPOST s = new InvioDatiPOST(PaginaRegistrazione.this, Connector.db + "script/submitpost.php?", parametri);
                String res = "";
                s.setOnFinishListener(new InvioDatiPOST.onFinishListener() {
                    @Override
                    public void onFinish(String res) {

                        if (res != null) {
                            if (res.contains("Registrazione avvenuta con successo!") /*&& (res.contains("200"))*/) {
                                View toastView = getLayoutInflater().inflate(R.layout.toastregistrazioneavvenuta, null);
                                Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
                                toast.setView(toastView);
                                toast.show();
                                Intent intent = new Intent(getApplicationContext(), PaginaLogin.class);
                                intent.putExtra("USER", user.getText().toString());
                                intent.putExtra("PASS", pass.getText().toString());
                                startActivity(intent);
                            }
                        if (res.contains("Problemi con l'iscrizione")) {
                            View toastView = getLayoutInflater().inflate(R.layout.toastregistrazioneavvenuta, null);
                            Toast toast = Toast.makeText(PaginaRegistrazione.this, "message", Toast.LENGTH_LONG);
                            toast.setView(toastView);
                            toast.show();
                        }
                        }
                    }


                });
                s.execute();
            } else {
                ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
                risultatonick = risultatomail = risultatotel = false;
                return;
            }
            ci = cm = cn = ct = cp = cl = cname = cc = cd = cs = false;
            risultatonick = risultatomail = risultatotel = false;
        }
    }
}



//PROBLEMA SOSTITUZIONE IMMAGINE




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