package oneforyou.jep.oneforyou.View.ViewUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import oneforyou.jep.oneforyou.Control.AggiornamentoStato;
import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.R;

public class PaginaTutorial extends Activity {

    private Typeface myfont, myfontdoppio, myfontsottile;
    private String ruolo, colore, foto, user;
    private int scommesse, media, seguaci, vinte, recensioni, saldo;
    private Utenti loggato;
    private int img;
    private Drawable[] immagini = new Drawable[15];
    private Button indietro, avanti, fine;
    private ImageView pagina;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layouttutorial);

        myfont = ResourcesCompat.getFont(this, R.font.cr);
        myfontdoppio = ResourcesCompat.getFont(this, R.font.cb);
        myfontsottile = ResourcesCompat.getFont(this, R.font.cl);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        loggato = (Utenti) bundle.getSerializable("UTENTE");

        /*ruolo = intent.getStringExtra("RUOLO");
        colore = intent.getStringExtra("COLORE");
        foto = intent.getStringExtra("FOTO");
        scommesse = intent.getIntExtra("SCOMMESSE", 0);
        media = intent.getIntExtra("MEDIA" , 0);
        seguaci = intent.getIntExtra("SEGUACI", 0);
        vinte = intent.getIntExtra("VINTE", 0);
        recensioni = intent.getIntExtra("RECENSIONI", 0);
        saldo = intent.getIntExtra("SALDO", 0);
        user = intent.getStringExtra("USER");*/
        img = 0;
        indietro = (Button) findViewById(R.id.indietro);
        avanti = (Button) findViewById(R.id.avanti);
        fine = (Button) findViewById(R.id.fine);
        pagina = (ImageView) findViewById(R.id.pagina);

        immagini[0] = getResources().getDrawable(R.drawable.prima);
        immagini[1] = getResources().getDrawable(R.drawable.seconda);
        immagini[2] = getResources().getDrawable(R.drawable.terza);
        immagini[3] = getResources().getDrawable(R.drawable.quarta);
        immagini[4] = getResources().getDrawable(R.drawable.quinta);
        immagini[5] = getResources().getDrawable(R.drawable.sesta);
        immagini[6] = getResources().getDrawable(R.drawable.settima);
        immagini[7] = getResources().getDrawable(R.drawable.ottava);
        immagini[8] = getResources().getDrawable(R.drawable.nona);
        immagini[9] = getResources().getDrawable(R.drawable.decima);
        immagini[10] = getResources().getDrawable(R.drawable.undicesima);
        immagini[11] = getResources().getDrawable(R.drawable.dodicesima);
        immagini[12] = getResources().getDrawable(R.drawable.tredicesima);
        immagini[13] = getResources().getDrawable(R.drawable.quattordicesima);
        immagini[14] = getResources().getDrawable(R.drawable.quindicesima);

        pagina.setBackground(immagini[0]);

        indietro.setVisibility(View.GONE);
    }


    protected void onResume(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layouttutorial);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //updateUI(currentUser);
    }

    public void passaiscrizione(View view){
        Intent intent = new Intent(this, PaginaHome.class);
        AggiornamentoStato as = new AggiornamentoStato(this);
        as.execute(loggato.getUsername());
        /*intent.putExtra("RUOLO", ruolo);
        intent.putExtra("SALDO", saldo);
        intent.putExtra("COLORE", colore);
        intent.putExtra("FOTO", foto);
        intent.putExtra("SCOMMESSE", scommesse);
        intent.putExtra("MEDIA", media);
        intent.putExtra("SEGUACI", seguaci);
        intent.putExtra("VINTE", vinte);
        intent.putExtra("RECENSIONI", recensioni);
        intent.putExtra("USER", user);*/
        Bundle bundle = new Bundle();
        bundle.putSerializable("UTENTE", loggato);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean cambiaimmagine(View view) {

        if (view.getId() == R.id.indietro) {
            if (img == 1) {
                indietro.setVisibility(View.GONE);
                img--;
                pagina.setBackground(immagini[img]);
                return false;
            }
            if (img == 14) {
                avanti.setVisibility(View.VISIBLE);
                fine.setText("SALTA");
                img--;
                pagina.setBackground(immagini[img]);
                return false;
            }
            img--;
            pagina.setBackground(immagini[img]);
        }

        if (view.getId() == R.id.avanti) {
            if (img == 13) {
                avanti.setVisibility(View.GONE);
                fine.setText("FINE");
                img++;
                pagina.setBackground(immagini[img]);
                return false;
            }
            if (img == 0) {
                indietro.setVisibility(View.VISIBLE);
                img++;
                pagina.setBackground(immagini[img]);
                return false;
            }
            img++;
            pagina.setBackground(immagini[img]);
        }
            return false;
    }
}

