package oneforyou.jep.oneforyou.Util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.R;

public class CustomAdapterPartite extends ArrayAdapter<Partite> {
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<Partite> partite = new ArrayList<>();

    public CustomAdapterPartite(Context context, int resourceId, int username, ArrayList<Partite> objects) {
        super(context, resourceId, objects);
        resource = resourceId;
        partite = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = inflater.inflate(R.layout.elementopartite, null);
        }



        Partite c = partite.get(position);

        TextView sqcasa = (TextView) v.findViewById(R.id.squadra1);
        TextView sqosp = (TextView) v.findViewById(R.id.squadra2);

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("azzurro")) {
            sqcasa.setBackgroundResource(R.color.azzurrocc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("blu")) {
            sqcasa.setBackgroundResource(R.color.blucc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("giallo")) {
            sqcasa.setBackgroundResource(R.color.giallocc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("rosso")) {
            sqcasa.setBackgroundResource(R.color.rossocc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("verde")) {
            sqcasa.setBackgroundResource(R.color.verdecc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("granata")) {
            sqcasa.setBackgroundResource(R.color.granatacc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("arancione")) {
            sqcasa.setBackgroundResource(R.color.arancionecc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("bianco")) {
            sqcasa.setBackgroundResource(R.color.biancocc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("nero")) {
            sqcasa.setBackgroundResource(R.color.nerocc);
        }

        if (c.getColoreC().toLowerCase().equalsIgnoreCase("viola")) {
            sqcasa.setBackgroundResource(R.color.violacc);
        }





        if (c.getColoreO().toLowerCase().equalsIgnoreCase("azzurro")) {
            sqosp.setBackgroundResource(R.color.azzurrocc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("blu")) {
            sqosp.setBackgroundResource(R.color.blucc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("giallo")) {
            sqosp.setBackgroundResource(R.color.giallocc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("rosso")) {
            sqosp.setBackgroundResource(R.color.rossocc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("verde")) {
            sqosp.setBackgroundResource(R.color.verdecc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("granata")) {
            sqosp.setBackgroundResource(R.color.granatacc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("arancione")) {
            sqosp.setBackgroundResource(R.color.arancionecc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("bianco")) {
            sqosp.setBackgroundResource(R.color.biancocc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("nero")) {
            sqosp.setBackgroundResource(R.color.nerocc);
        }

        if (c.getColoreO().toLowerCase().equalsIgnoreCase("viola")) {
            sqosp.setBackgroundResource(R.color.violacc);
        }



        TextView dataPartita = (TextView) v.findViewById(R.id.datapartita);
        int anno = Integer.parseInt(c.getData().substring(0, 4));
        int mese = Integer.parseInt(c.getData().substring(5, 7));
        int giorno = Integer.parseInt(c.getData().substring(8, 10));
        if (c.getMinuti() < 10)
        dataPartita.setText("" + giorno + "/" +  mese + "/" + anno + " " + c.getOre() + ".0" + c.getMinuti());
        else
            dataPartita.setText("" + giorno + "/" +  mese + "/" + anno + " " + c.getOre() + "." + c.getMinuti());
        sqcasa.setText(c.getCasa() + "   ");
        sqosp.setText( "   " + c.getOspite());
        return v;
    }
}
