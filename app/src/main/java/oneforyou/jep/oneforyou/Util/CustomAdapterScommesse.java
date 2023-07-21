package oneforyou.jep.oneforyou.Util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import oneforyou.jep.oneforyou.Model.Partite;
import oneforyou.jep.oneforyou.Model.Scommesse;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.ViewUser.PaginaStatsUtente;

public class CustomAdapterScommesse extends ArrayAdapter<Scommesse> {
    private int resource;
    private LayoutInflater inflater;
    private ArrayList<Scommesse> scommesse = new ArrayList<>();
    private Partite match;
    private String errorpwd;
    private boolean segnalaerrori;

    public CustomAdapterScommesse(Context context, int resourceId, int username, ArrayList<Scommesse> objects) {
        super(context, resourceId, objects);
        resource = resourceId;
        scommesse = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = inflater.inflate(R.layout.elementoscommesse, null);
        }


        Scommesse c = scommesse.get(position);
        Partite match = PaginaStatsUtente.apgp.get(position);

        TextView sqcasa = (TextView) v.findViewById(R.id.squadra1);
        TextView sqosp = (TextView) v.findViewById(R.id.squadra2);

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("azzurro")) {
            sqcasa.setBackgroundResource(R.color.azzurrocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("blu")) {
            sqcasa.setBackgroundResource(R.color.blucc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("giallo")) {
            sqcasa.setBackgroundResource(R.color.giallocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("rosso")) {
            sqcasa.setBackgroundResource(R.color.rossocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("verde")) {
            sqcasa.setBackgroundResource(R.color.verdecc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("granata")) {
            sqcasa.setBackgroundResource(R.color.granatacc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("arancione")) {
            sqcasa.setBackgroundResource(R.color.arancionecc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("bianco")) {
            sqcasa.setBackgroundResource(R.color.biancocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("nero")) {
            sqcasa.setBackgroundResource(R.color.nerocc);
        }

        if (match.getColoreC().toLowerCase().equalsIgnoreCase("viola")) {
            sqcasa.setBackgroundResource(R.color.violacc);
        }





        if (match.getColoreO().toLowerCase().equalsIgnoreCase("azzurro")) {
            sqosp.setBackgroundResource(R.color.azzurrocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("blu")) {
            sqosp.setBackgroundResource(R.color.blucc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("giallo")) {
            sqosp.setBackgroundResource(R.color.giallocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("rosso")) {
            sqosp.setBackgroundResource(R.color.rossocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("verde")) {
            sqosp.setBackgroundResource(R.color.verdecc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("granata")) {
            sqosp.setBackgroundResource(R.color.granatacc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("arancione")) {
            sqosp.setBackgroundResource(R.color.arancionecc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("bianco")) {
            sqosp.setBackgroundResource(R.color.biancocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("nero")) {
            sqosp.setBackgroundResource(R.color.nerocc);
        }

        if (match.getColoreO().toLowerCase().equalsIgnoreCase("viola")) {
            sqosp.setBackgroundResource(R.color.violacc);
        }



        TextView dataPartita = (TextView) v.findViewById(R.id.datascommessa);
        int anno = Integer.parseInt(match.getData().substring(0, 4));
        int mese = Integer.parseInt(match.getData().substring(5, 7));
        int giorno = Integer.parseInt(match.getData().substring(8, 10));
        String ajk = "";
        if (match.getMinuti() < 10) {
            ajk = "" + giorno + "/" + mese + "/" + anno + " " + match.getOre() + ".0" + match.getMinuti();
            dataPartita.setText(ajk);
        }
        else {
            ajk = "" + giorno + "/" + mese + "/" + anno + " " + match.getOre() + "." + match.getMinuti();
            dataPartita.setText(ajk);
        }
        ajk = "";
        sqcasa.setText(match.getCasa() + "   ");
        sqosp.setText( "   " + match.getOspite());

        TextView prono = (TextView) v.findViewById(R.id.prono);
        TextView esito = (TextView) v.findViewById(R.id.esito);

        if (c.getPronostico() == 1)
            prono.setText("PRONO: 1");
        if (c.getPronostico() == 0)
            prono.setText("PRONO: X");
        if (c.getPronostico() == 2)
            prono.setText("PRONO: 2");

        if (match.getEsito() == 1)
            esito.setText("ESITO: 1");
        if (match.getEsito() == 0)
            esito.setText("ESITO: X");
        if (match.getEsito() == 2)
            esito.setText("ESITO: 2");
        if (match.getEsito() == -1)
            esito.setText("ESITO: N");

        ImageView simbolo = (ImageView) v.findViewById(R.id.simbolo);
        if (c.getStato().equalsIgnoreCase("Persa")){
            simbolo.setBackground(simbolo.getResources().getDrawable(R.drawable.persa));
        }
        if (c.getStato().equalsIgnoreCase("Nulla")){
            simbolo.setBackground(simbolo.getResources().getDrawable(R.drawable.annullata));
            esito.setText("ESITO: N");
        }
        if (c.getStato().equalsIgnoreCase("Vinta")){
            simbolo.setBackground(simbolo.getResources().getDrawable(R.drawable.vinta));
        }
        if (c.getStato().equalsIgnoreCase("Attesa")){
            simbolo.setBackground(simbolo.getResources().getDrawable(R.drawable.attesa));
            esito.setText("ESITO: N");
        }
        return v;
    }
}
