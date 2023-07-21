package oneforyou.jep.oneforyou.Util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import oneforyou.jep.oneforyou.Model.Utenti;
import oneforyou.jep.oneforyou.R;
import oneforyou.jep.oneforyou.View.ViewAdmin.PaginaGestioneUtenti;

public class CustomAdapterUtenti2 extends ArrayAdapter<Utenti> {
        private int resource;
        private LayoutInflater inflater;
        private ArrayList<Utenti> utenti = new ArrayList<>();

        public CustomAdapterUtenti2(Context context, int resourceId, int username, ArrayList<Utenti> objects) {
            super(context, resourceId, objects);
            resource = resourceId;
            utenti = objects;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                v = inflater.inflate(R.layout.elementoutente, null);
            }



            Utenti c = utenti.get(position);

            TextView usernametv = (TextView) v.findViewById(R.id.username);
            TextView infotv = (TextView) v.findViewById(R.id.info);
            TextView statstv = (TextView) v.findViewById(R.id.stats);
            LinearLayout riga = (LinearLayout) v.findViewById(R.id.riga);
            final ImageView fotoiv = (ImageView) v.findViewById(R.id.foto);


            /*usernametv=((LinearLayout)v).findViewById(R.id.username);
            infotv=((LinearLayout)v).findViewById(R.id.info);
            fotoiv=((LinearLayout)v).findViewById(R.id.foto);
            statstv=((LinearLayout)v).findViewById(R.id.stats);*/


//            PrendiFoto pf = new PrendiFoto(getContext());
//            pf.execute(Connector.db + "img/" + c.getFoto());
//            pf.setOnFinishListener(new PrendiFoto.onFinishListener() {
//                @Override
//                public void onFinish(String res) {
//                    // The task has finished and you can now use the result
//                    try {
//                        byte[] encodeByte = Base64.decode(res, Base64.DEFAULT);
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                        fotoiv.setImageBitmap(bitmap);
//
//                        //immagine.setBackground(getResources().getDrawable(R.drawable.editcentro));
//                    } catch (Exception e) {
//                        e.getMessage();
//                    }
//                }
//            });

            fotoiv.setBackground(PaginaGestioneUtenti.utentino);

            if (c.getRuolo().equalsIgnoreCase("tipster")) {
                usernametv.setText("♚ " + c.getUsername() + " ♚");
            }

            else {
                usernametv.setText(c.getUsername() + "");
            }

            if (c.getColore().toLowerCase().contains("colore")) {
                riga.setBackgroundResource(R.color.biancocc);
            }

            if (c.getColore().toLowerCase().contains("azzurro")) {
                riga.setBackgroundResource(R.color.azzurrocc);
            }

            if (c.getColore().toLowerCase().contains("blu")) {
                riga.setBackgroundResource(R.color.blucc);
            }

            if (c.getColore().toLowerCase().contains("giallo")) {
                riga.setBackgroundResource(R.color.giallocc);
            }

            if (c.getColore().toLowerCase().contains("rosso")) {
                riga.setBackgroundResource(R.color.rossocc);
            }

            if (c.getColore().toLowerCase().contains("verde")) {
                riga.setBackgroundResource(R.color.verdecc);
            }

            if (c.getColore().toLowerCase().contains("granata")) {
                riga.setBackgroundResource(R.color.granatacc);
            }

            if (c.getColore().toLowerCase().contains("arancione")) {
                riga.setBackgroundResource(R.color.arancionecc);
            }

            if (c.getColore().toLowerCase().contains("bianco")) {
                riga.setBackgroundResource(R.color.biancocc);
            }

            if (c.getColore().toLowerCase().contains("nero")) {
                riga.setBackgroundResource(R.color.nerocc);
            }

            if (c.getColore().toLowerCase().contains("viola")) {
                riga.setBackgroundResource(R.color.violacc);
            }

            infotv.setText("Recensioni positive: " + c.getRecensioni() + " - Seguito da " +  c.getSeguaci() + " utenti");
            statstv.setText("Vinte " + c.getVinte() + " su " + c.getScommesse() + " (media del " + String.valueOf(c.getMedia() + "%)"));

            return v;
        }
    }
