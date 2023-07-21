package oneforyou.jep.oneforyou.Util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import oneforyou.jep.oneforyou.Model.Premi;
import oneforyou.jep.oneforyou.R;

public class CustomAdapterCoupon extends ArrayAdapter<Premi> {
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<Premi> premi = new ArrayList<>();

    public CustomAdapterCoupon(Context context, int resourceId, int username, ArrayList<Premi> objects) {
        super(context, resourceId, objects);
        resource = resourceId;
        premi = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = inflater.inflate(R.layout.elementocoupon, null);
        }



        Premi c = premi.get(position);

        RelativeLayout riga = (RelativeLayout) v.findViewById(R.id.riga);
        TextView servizio = (TextView) v.findViewById(R.id.servizio);
        TextView valore = (TextView) v.findViewById(R.id.valore);
        TextView costo = (TextView) v.findViewById(R.id.costo);
        TextView data = (TextView) v.findViewById(R.id.data);
        TextView codice = (TextView) v.findViewById(R.id.codice);


        if (c.getServizio().equalsIgnoreCase("NowTv")) {
            riga.setBackgroundResource(R.color.violacc);
            codice.setBackgroundResource(R.color.violas);
        }

        if (c.getServizio().equalsIgnoreCase("Netflix")) {
            riga.setBackgroundResource(R.color.rossocc);
            codice.setBackgroundResource(R.color.rossos);
        }

        if (c.getServizio().equalsIgnoreCase("Spotify")) {
            riga.setBackgroundResource(R.color.verdecc);
            codice.setBackgroundResource(R.color.verdes);
        }

        if (c.getServizio().equalsIgnoreCase("Amazon")) {
            riga.setBackgroundResource(R.color.arancionecc);
            codice.setBackgroundResource(R.color.aranciones);
        }

        if (c.getServizio().equalsIgnoreCase("Dazn")) {
            riga.setBackgroundResource(R.color.nerocc);
            codice.setBackgroundResource(R.color.neros);
        }

        if (c.getServizio().equalsIgnoreCase("PlayStore")) {
            riga.setBackgroundResource(R.color.azzurrocc);
            codice.setBackgroundResource(R.color.azzurros);
        }


        servizio.setText(c.getServizio());
        valore.setText(c.getValore()+"€");
        costo.setText(c.getCosto() + " funnies");

        int anno = Integer.parseInt(c.getData().substring(0, 4));
        int mese = Integer.parseInt(c.getData().substring(5, 7));
        int giorno = Integer.parseInt(c.getData().substring(8, 10));

        data.setText(giorno + "/" + mese + "/" + anno);

        codice.setText(c.getCodice());
        return v;
    }
}
