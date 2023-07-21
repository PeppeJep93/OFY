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
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Control.PrendiFoto;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaRegistrazione;

public class PaginaDashboard extends Activity {

    private Typeface myfont, myfontdoppio, myfontsottile;
    private static String verificato = "";
    private Utenti loggato;
    private RelativeLayout b1, b2;
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
        setContentView(R.layout.layoutdashboard);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        b1 = (RelativeLayout) findViewById(R.id.button1);
        b2 = (RelativeLayout) findViewById(R.id.button2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        String col = loggato.getColore();
        String fot = loggato.getFoto();
        String us = loggato.getUsername();

        RelativeLayout utente = (RelativeLayout) findViewById(R.id.utente);
        TextView username = (TextView) findViewById(R.id.username);
        username.setText(us);
        final ImageView immagine = (ImageView) findViewById(R.id.immagine);

        if (col.toLowerCase().contains("colore")) {
            int x = (int)(Math.random()*((9-1)+1))+1;

            if (x==1)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.azzurros);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.azzurro);
                b1.setBackgroundResource(R.color.azzurro);
                b2.setBackgroundResource(R.color.azzurro);
            }
            if (x==2)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.blus);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.blu);
                b1.setBackgroundResource(R.color.blu);
                b2.setBackgroundResource(R.color.blu);
            }
            if (x==3)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.giallos);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.giallo);
                b1.setBackgroundResource(R.color.giallo);
                b2.setBackgroundResource(R.color.giallo);
            }
            if (x==4)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.rossos);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.rosso);
                b1.setBackgroundResource(R.color.rosso);
                b2.setBackgroundResource(R.color.rosso);
            }
            if (x==5)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.verdes);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.verde);
                b1.setBackgroundResource(R.color.verde);
                b2.setBackgroundResource(R.color.verde);
            }
            if (x==6)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.granatas);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.granata);
                b1.setBackgroundResource(R.color.granata);
                b2.setBackgroundResource(R.color.granata);
            }
            if (x==7)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.aranciones);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.arancione);
                b1.setBackgroundResource(R.color.arancione);
                b2.setBackgroundResource(R.color.arancione);
            }
            if (x==8)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.biancos);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.bianco);
                b1.setBackgroundResource(R.color.bianco);
                b2.setBackgroundResource(R.color.bianco);
            }
            if (x==9)
            {
                ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
                Paint paint = rectShapeDrawable.getPaint();
                int color = ContextCompat.getColor(this, R.color.neros);
                paint.setColor(color);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(15); // you can change the value of 5
                immagine.setBackgroundDrawable(rectShapeDrawable);
                utente.setBackgroundResource(R.color.nero);
                b1.setBackgroundResource(R.color.nero);
                b2.setBackgroundResource(R.color.nero);
            }

        }

        if (col.toLowerCase().contains("azzurro")) {
            ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class
            Paint paint = rectShapeDrawable.getPaint();
            int color = ContextCompat.getColor(this, R.color.azzurros);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(15); // you can change the value of 5
            immagine.setBackgroundDrawable(rectShapeDrawable);
            utente.setBackgroundResource(R.color.azzurro);
            b1.setBackgroundResource(R.color.azzurro);
            b2.setBackgroundResource(R.color.azzurro);
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
            b1.setBackgroundResource(R.color.blu);
            b2.setBackgroundResource(R.color.blu);
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
            b1.setBackgroundResource(R.color.giallo);
            b2.setBackgroundResource(R.color.giallo);
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
            b1.setBackgroundResource(R.color.rosso);
            b2.setBackgroundResource(R.color.rosso);
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
            b1.setBackgroundResource(R.color.verde);
            b2.setBackgroundResource(R.color.verde);
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
            b1.setBackgroundResource(R.color.granata);
            b2.setBackgroundResource(R.color.granata);
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
            b1.setBackgroundResource(R.color.arancione);
            b2.setBackgroundResource(R.color.arancione);
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
            b1.setBackgroundResource(R.color.bianco);
            b2.setBackgroundResource(R.color.bianco);
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
            b1.setBackgroundResource(R.color.nero);
            b2.setBackgroundResource(R.color.nero);
        }



        //colore font scrittura



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



        //immagine.setImageBitmap(prendifoto(fot));


    }

    private void toastview(String error) {
        if (error.equals("probserver"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastprobserver, null);
            Toast toast = Toast.makeText(PaginaDashboard.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
        if (error.equals("pceserverspento"))
        {
            View toastView = getLayoutInflater().inflate(R.layout.toastpceserverspento, null);
            Toast toast = Toast.makeText(PaginaDashboard.this, "message", Toast.LENGTH_LONG);
            toast.setView(toastView);
            toast.show();
        }
    }

    /*protected Bitmap prendifoto(String fot)
    {
        try {
            URL url = new URL(Connector.db + "img/" + fot);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    protected void onResume(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutdashboard);
    }

    public void profilo(View view) {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
    }

    public void passaiscrizione(View view){
        Intent intent = new Intent(getApplicationContext(), PaginaRegistrazione.class);
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
            case R.id.button1:
                intent = new Intent(getApplicationContext(), PaginaGestionePartite.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button1a:
                intent = new Intent(getApplicationContext(), PaginaGestionePartite.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button1b:
                intent = new Intent(getApplicationContext(), PaginaGestionePartite.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button2:
                intent = new Intent(getApplicationContext(), PaginaGestioneUtenti.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button2a:
                intent = new Intent(getApplicationContext(), PaginaGestioneUtenti.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button2b:
                intent = new Intent(getApplicationContext(), PaginaGestioneUtenti.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}

