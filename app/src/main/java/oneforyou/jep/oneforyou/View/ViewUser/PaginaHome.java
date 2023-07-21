package oneforyou.jep.oneforyou.View.ViewUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

public class PaginaHome extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private boolean segnalaerrori;
    private String errorpwd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layouthome);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        /*String ruol = intent.getStringExtra("RUOLO");
        String col = intent.getStringExtra("COLORE");
        String fot = intent.getStringExtra("FOTO");
        int scomm = intent.getIntExtra("SCOMMESSE", 0);
        int med = intent.getIntExtra("MEDIA" , 0);
        int seg = intent.getIntExtra("SEGUACI", 0);
        int vin = intent.getIntExtra("VINTE", 0);
        int recens = intent.getIntExtra("RECENSIONI", 0);
        int sald = intent.getIntExtra("SALDO", 0);
        String us = intent.getStringExtra("USER");*/


        RelativeLayout utente = (RelativeLayout) findViewById(R.id.utente);
        TableLayout tabella = (TableLayout) findViewById(R.id.tableLayout1);
        RelativeLayout alsx = (RelativeLayout) findViewById(R.id.gioca);
        RelativeLayout aldx = (RelativeLayout) findViewById(R.id.stats);
        RelativeLayout bssx = (RelativeLayout) findViewById(R.id.amici);
        RelativeLayout bsdx = (RelativeLayout) findViewById(R.id.premi);

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
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.azzurros);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.azzurro);
            tabella.setBackgroundResource(R.color.azzurro);
        }

        if (loggato.getColore().toLowerCase().contains("blu")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.blus);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.blu);
            tabella.setBackgroundResource(R.color.blu);
        }

        if (loggato.getColore().toLowerCase().contains("giallo")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.giallos);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.giallo);
            tabella.setBackgroundResource(R.color.giallo);
        }

        if (loggato.getColore().toLowerCase().contains("rosso")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.rossos);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.rosso);
            tabella.setBackgroundResource(R.color.rosso);
        }

        if (loggato.getColore().toLowerCase().contains("verde")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.verdes);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.verde);
            tabella.setBackgroundResource(R.color.verde);
        }

        if (loggato.getColore().toLowerCase().contains("granata")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.granatas);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.granata);
            tabella.setBackgroundResource(R.color.granata);
        }

        if (loggato.getColore().toLowerCase().contains("arancione")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.aranciones);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.arancione);
            tabella.setBackgroundResource(R.color.arancione);
        }

        if (loggato.getColore().toLowerCase().contains("bianco")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.biancos);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.bianco);
            tabella.setBackgroundResource(R.color.bianco);
        }

        if (loggato.getColore().toLowerCase().contains("nero")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.neros);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.nero);
            tabella.setBackgroundResource(R.color.nero);
        }

        if (loggato.getColore().toLowerCase().contains("viola")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.violas);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            alsx.setBackgroundDrawable(rectShapeDrawable);
            aldx.setBackgroundDrawable(rectShapeDrawable);
            bssx.setBackgroundDrawable(rectShapeDrawable);
            bsdx.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.viola);
            tabella.setBackgroundResource(R.color.viola);
        }


        //colore font scrittura


        TextView scommesse = (TextView) findViewById(R.id.scommess);
        scommesse.setTypeface(myfontsottile);
        TextView media = (TextView) findViewById(R.id.media);
        media.setTypeface(myfontsottile);
        TextView vinte = (TextView) findViewById(R.id.vinte);
        vinte.setTypeface(myfontsottile);
        TextView recensioni = (TextView) findViewById(R.id.recensioni);
        recensioni.setTypeface(myfontsottile);
        TextView seguaci = (TextView) findViewById(R.id.seguaci);
        seguaci.setTypeface(myfontsottile);
        TextView saldo = (TextView) findViewById(R.id.saldo);
        saldo.setTypeface(myfont);
        TextView username = (TextView) findViewById(R.id.username);
        username.setTypeface(myfont);

        recensioni.setText(loggato.getRecensioni() + " recensioni - ");
        seguaci.setText(loggato.getSeguaci() + " seguaci");
        saldo.setText("" + loggato.getSaldo() + "◍ funnies");
        media.setText(loggato.getMedia() + "% ");
        vinte.setText("[" + loggato.getVinte() + " su ");
        scommesse.setText(loggato.getScommesse() + "]");
        if (loggato.getRuolo().equalsIgnoreCase("tipster"))
            username.setText("♚ " + loggato.getUsername() + " ♚");
        else
            username.setText(loggato.getUsername() + "");

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


        //colore e foto

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            case R.id.gioca:
                if (controllointernet()) {
                    intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.stats:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.amici:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaUtentiUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.premi:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaPremiUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.gioca0:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.stats0:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.amici0:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaUtentiUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.premi0:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaPremiUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.butpa:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaGiocaUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.butsc:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaStatsUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.butut:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaUtentiUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
            case R.id.butpr:
                if (controllointernet()) {
                intent = new Intent(getApplicationContext(), PaginaPremiUtente.class);
                intent.putExtras(bundle);
                startActivity(intent);
                }
                break;
        }
    }

    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaHome.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaHome.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
    }
}

