package oneforyou.jep.oneforyou.View.ViewAdmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
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
import android.widget.Button;
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

public class PaginaGestionePartite extends Activity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Typeface myfont, myfontdoppio, myfontsottile;
    private Utenti loggato;
    private RelativeLayout barramenu, voce1, voce2;
    private Button voce1a, voce2a;
    private boolean segnalaerrori;
    private String errorpwd;
    private RelativeLayout baggiorna, bpubblica, binserisci;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutgestionepartite);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        barramenu = findViewById(R.id.barramenusotto);

        baggiorna = (RelativeLayout) findViewById(R.id.button1);
        bpubblica = (RelativeLayout) findViewById(R.id.button2);
        binserisci = (RelativeLayout) findViewById(R.id.button3);

        voce1 = findViewById(R.id.buttonutenti1);
        voce2 = findViewById(R.id.buttonpartite1);
        voce1a = findViewById(R.id.buttonutenti2);
        voce2a = findViewById(R.id.buttonpartite2);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        RelativeLayout utente = (RelativeLayout) findViewById(R.id.utente);
        TableLayout tabella = (TableLayout) findViewById(R.id.tableLayout1);

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

        if (col.toLowerCase().contains("azzurro")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.azzurros);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.azzurro);
            tabella.setBackgroundResource(R.color.azzurro);
            barramenu.setBackgroundResource(R.color.azzurros);
            voce2.setBackgroundResource(R.color.azzurroc);
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
            tabella.setBackgroundResource(R.color.blu);
            barramenu.setBackgroundResource(R.color.blus);
            voce2.setBackgroundResource(R.color.bluc);
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
            tabella.setBackgroundResource(R.color.giallo);
            barramenu.setBackgroundResource(R.color.giallos);
            voce2.setBackgroundResource(R.color.gialloc);
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
            tabella.setBackgroundResource(R.color.rosso);
            barramenu.setBackgroundResource(R.color.rossos);
            voce2.setBackgroundResource(R.color.rossoc);
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
            tabella.setBackgroundResource(R.color.verde);
            barramenu.setBackgroundResource(R.color.verdes);
            voce2.setBackgroundResource(R.color.verdec);
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
            tabella.setBackgroundResource(R.color.granata);
            barramenu.setBackgroundResource(R.color.granatas);
            voce2.setBackgroundResource(R.color.granatac);
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
            tabella.setBackgroundResource(R.color.arancione);
            barramenu.setBackgroundResource(R.color.aranciones);
            voce2.setBackgroundResource(R.color.arancionec);
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
            tabella.setBackgroundResource(R.color.bianco);
            barramenu.setBackgroundResource(R.color.biancos);
            voce2.setBackgroundResource(R.color.biancoc);
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
            tabella.setBackgroundResource(R.color.nero);
            barramenu.setBackgroundResource(R.color.neros);
            voce2.setBackgroundResource(R.color.neroc);
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

    }
        //colore e foto




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
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaGestionePartite.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaGestionePartite.this, "message", Toast.LENGTH_LONG);
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

    public void vaiattivita(View view) {
        Intent intent;
        Bundle bundle;
        bundle = new Bundle();
        bundle.putSerializable("UTENTE", loggato);
        switch (view.getId()) {
            case R.id.button1:
                intent = new Intent(getApplicationContext(), PaginaAggiornaEliminaPartita.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button1a:
                intent = new Intent(getApplicationContext(), PaginaAggiornaEliminaPartita.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button1b:
                intent = new Intent(getApplicationContext(), PaginaAggiornaEliminaPartita.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button2:
                intent = new Intent(getApplicationContext(), PaginaPubblicaRisultati.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button2a:
                intent = new Intent(getApplicationContext(), PaginaPubblicaRisultati.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button2b:
                intent = new Intent(getApplicationContext(), PaginaPubblicaRisultati.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button3:
                intent = new Intent(getApplicationContext(), PaginaInserisciPartita.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button3a:
                intent = new Intent(getApplicationContext(), PaginaInserisciPartita.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button3b:
                intent = new Intent(getApplicationContext(), PaginaInserisciPartita.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}

