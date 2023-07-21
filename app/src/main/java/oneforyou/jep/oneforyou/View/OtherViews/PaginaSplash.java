package oneforyou.jep.oneforyou.View.OtherViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;

import oneforyou.jep.oneforyou.R;

public class PaginaSplash extends Activity {

    public GyroscopeObserver go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layoutsplash);
        go = new GyroscopeObserver();
        double pg = Math.PI / 3;
        go.setMaxRotateRadian(pg);
        PanoramaImageView piv = (PanoramaImageView) findViewById(R.id.ciao);
        int x = (int)(Math.random()*((7-1)+1))+1;
        if (x==1)
            piv.setImageResource(R.drawable.sanpaolo);
        if (x==2)
            piv.setImageResource(R.drawable.franchi);
        if (x==3)
            piv.setImageResource(R.drawable.fila);
        if (x==4)
            piv.setImageResource(R.drawable.olimpico);
        if (x==5)
            piv.setImageResource(R.drawable.marassi);
        if (x==6)
            piv.setImageResource(R.drawable.meazza);
        if (x==7)
            piv.setImageResource(R.drawable.stadium);
        piv.setGyroscopeObserver(go);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(PaginaSplash.this, PaginaLogin.class);
                PaginaSplash.this.startActivity(mainIntent);
                PaginaSplash.this.finish();
            }
        }, 3000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register GyroscopeObserver.
        go.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister GyroscopeObserver.
        go.unregister();
    }
}