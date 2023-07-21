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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import oneforyou.jep.oneforyou.Control.ControlloMail;
import oneforyou.jep.oneforyou.Control.ControlloTelefono;
import oneforyou.jep.oneforyou.R;

public class PaginaRecuperoPassword extends Activity {

    private Typeface myfont, myfontdoppio, myfontsottile;

    private EditText campomail, campotel;
    private int cosahascelto = 0;
    private TextView cosa, info1, info2, fine;
    private Button pulsanterecupera;
    private String errorpwd = "", errortel = "";
    private boolean segnalaerrori;
    private EditText dgtm;
    private EditText dgtt;
    private boolean emailvalida = false, telefonovalido = false;

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
    private String errormail;

    /*private VideoView vv;*/


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutrecupero);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        final EditText dgtm = (EditText) findViewById(R.id.inseriscimail);
        final EditText dgtt = (EditText) findViewById(R.id.inseriscitel);

        campomail = (EditText) findViewById(R.id.inseriscimail);
        campotel = (EditText) findViewById(R.id.inseriscitel);
        fine = (TextView) findViewById(R.id.istruzioni);
        cosa = (TextView) findViewById(R.id.digita);
        info1 = (TextView) findViewById(R.id.tel);
        info2 = (TextView) findViewById(R.id.mail);
        pulsanterecupera = (Button) findViewById(R.id.recuperiamo);

        campomail.setTypeface(myfontdoppio);
        campotel.setTypeface(myfontdoppio);
        fine.setTypeface(myfontdoppio);
        cosa.setTypeface(myfontdoppio);
        info1.setTypeface(myfontdoppio);
        info2.setTypeface(myfontdoppio);
        pulsanterecupera.setTypeface(myfontdoppio);

        campotel.setTextColor(Color.argb(125, 0, 0, 0));
        campomail.setTextColor(Color.argb(125, 0, 0, 0));
        campotel.setHintTextColor(Color.argb(255, 0, 0, 0));
        campomail.setHintTextColor(Color.argb(255, 0, 0, 0));


        campomail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    campomail.setHintTextColor(Color.argb(75, 255, 255, 255));
                    campomail.setTextColor(Color.argb(255, 255, 255, 255));
                    campomail.setBackgroundResource(R.drawable.editcentro);

                } else {
                    campomail.setHintTextColor(Color.argb(255, 0, 0, 0));
                    campomail.setTextColor(Color.argb(255, 0, 0, 0));
                }
            }
        });

        campotel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    campotel.setHintTextColor(Color.argb(75, 255, 255, 255));
                    campotel.setTextColor(Color.argb(255, 255, 255, 255));
                    campotel.setBackgroundResource(R.drawable.editcentro);

                } else {
                    campotel.setHintTextColor(Color.argb(255, 0, 0, 0));
                    campotel.setTextColor(Color.argb(255, 0, 0, 0));
                }
            }
        });

        campomail.requestFocus(1);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        dgtt.setSelection(dgtt.getText().length());
        dgtt.addTextChangedListener(new TextWatcher() {

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
                    dgtt.setText(string);
                    telefonovalido = true;
                    errorpwd ="";
                }
                ignoreChange = true;
                dgtt.setText(string);
                dgtt.setSelection(dgtt.getText().length());
                ignoreChange = false;
            }
        }
    });



}



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        RadioButton telefono = (RadioButton) findViewById(R.id.sceltotel);
        RadioButton mail = (RadioButton) findViewById(R.id.sceltomail);
        TextView tel = (TextView) findViewById(R.id.tel);
        TextView ema = (TextView) findViewById(R.id.mail);
        EditText dgtt = (EditText) findViewById(R.id.inseriscitel);
        EditText dgtm = (EditText) findViewById(R.id.inseriscimail);
        TextView istruzioni = (TextView) findViewById(R.id.istruzioni);

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.sceltomail:
                if (checked) {
                    cosahascelto = 0;
                    ema.setVisibility(View.VISIBLE);
                    dgtm.setVisibility(View.VISIBLE);
                    dgtt.setVisibility(View.GONE);
                    tel.setVisibility(View.GONE);
                    ((RadioButton) telefono).setButtonDrawable(getResources().getDrawable(R.drawable.telefono));
                    ((RadioButton) mail).setButtonDrawable(getResources().getDrawable(R.drawable.emailb));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) istruzioni.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.mail);
                    dgtm.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    break;
                }
            case R.id.sceltotel:
                if (checked) {
                    cosahascelto = 1;
                    ema.setVisibility(View.GONE);
                    dgtm.setVisibility(View.GONE);
                    dgtt.setVisibility(View.VISIBLE);
                    tel.setVisibility(View.VISIBLE);
                    ((RadioButton) telefono).setButtonDrawable(getResources().getDrawable(R.drawable.telefonob));
                    ((RadioButton) mail).setButtonDrawable(getResources().getDrawable(R.drawable.email));
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) istruzioni.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.tel);
                    dgtt.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    break;
                }
        }
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

    private boolean checkEmail(String mail) {
        return EMAIL_ADDRESS_PATTERN.matcher(mail).matches();
    }

    public void recuperoAccesso(View view) {
        EditText dgtm = (EditText) findViewById(R.id.inseriscimail);
        EditText dgtt = (EditText) findViewById(R.id.inseriscitel);
        errortel = "";
        errormail = "";
        segnalaerrori = false;
        ControlloMail.asydata = false;
        ControlloTelefono.asydata = false;

        if (controllointernet()) {



            if (cosahascelto == 0) {
                if (checkEmail(dgtm.getText().toString())) {
                    ControlloMail cd = new ControlloMail(this);
                    cd.execute(dgtm.getText().toString());
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
                                errormail = "tuttookmail";
                                segnalaerrori = true;
                                ControlloMail.asydata = true;
                                return;
                            }
                            if (result.contains("valida")) {
                                errormail = "mailnontrovata";
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
                    errormail = "";
                }
                else {
                    View toastView = getLayoutInflater().inflate(R.layout.toastnonvalidomail, null);
                    Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    dgtm.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    ControlloMail.asydata = false;
                    segnalaerrori = false;
                    errormail = "";
                }
            }



            if (cosahascelto == 1) {
                if (telefonovalido) {
                    ControlloTelefono cd = new ControlloTelefono(this);
                    cd.execute(dgtt.getText().toString());
                    cd.setOnFinishListener(new ControlloTelefono.onFinishListener() {
                        @Override
                        public void onFinish(String result) {
                            if (result.contains("java.net.ConnectException")) {
                                errortel = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.UnsupportedEncoding")) {
                                errortel = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.Protocol")) {
                                errortel = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.MalformedUrl")) {
                                errortel = "probserver";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.IO")) {
                                errortel = "pceserverspento";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("java.net.SocketTimeoutException")) {
                                errortel = "pceserverspento";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                                //stampare risultato asynctask
                            }
                            if (result.contains("Abbinato")) {
                                errortel = "tuttooktel";
                                segnalaerrori = true;
                                ControlloTelefono.asydata = true;
                                return;
                            }
                            if (result.contains("valido")) {
                                errortel = "telnontrovato";
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
                            toastview(errortel);
                        }
                    }
                    segnalaerrori = false;
                    ControlloTelefono.asydata = false;
                    errortel = "";
                }
                else {
                    View toastView = getLayoutInflater().inflate(R.layout.toasttelnonvalido, null);
                    Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                    toast.setView(toastView);
                    toast.show();
                    dgtt.requestFocus(1);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    segnalaerrori = false;
                    ControlloTelefono.asydata = false;
                    errortel = "";
                }
            }
        }

    }

        private void toastview(String error) {
            EditText dgtm = (EditText) findViewById(R.id.inseriscimail);
            EditText dgtt = (EditText) findViewById(R.id.inseriscitel);
            if (error.equals("probserver"))
            {
                View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
                Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            if (error.equals("tuttooktel")) {
                View toastView = getLayoutInflater().inflate(R.layout.toasttelefonook, null);
                Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), PaginaLogin.class);
                startActivity(intent);
            }
            if (error.equals("tuttookmail")) {
                View toastView = getLayoutInflater().inflate(R.layout.toastmailok, null);
                Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), PaginaLogin.class);
                startActivity(intent);
            }
            if (error.equals("pceserverspento"))
            {
                View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
                Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
            }
            if (error.equals("telnontrovato"))
            {
                View toastView = getLayoutInflater().inflate(R.layout.toasttelnontrovato, null);
                Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                dgtt.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
            if (error.equals("mailnontrovata"))
            {
                View toastView = getLayoutInflater().inflate(R.layout.toastmailnontrovata, null);
                Toast toast = Toast.makeText(PaginaRecuperoPassword.this, "message", Toast.LENGTH_LONG);
                toast.setView(toastView);
                toast.show();
                dgtm.requestFocus(1);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }
        //controllointernet
        //controlloformatoemail
        //controlloformatocell
        //vedereseesistescriptphp
        //contattareetornareallalogin





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

