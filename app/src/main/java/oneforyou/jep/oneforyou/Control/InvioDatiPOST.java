package oneforyou.jep.oneforyou.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.DataPackager;
import oneforyou.jep.oneforyou.R;


/**
 * Created by peppe on 11-Feb-18.
 */

public class InvioDatiPOST extends AsyncTask <String, Void,String> {

    public  String db = Connector.db;

    Context c;
    String urlAddress;
    ProgressDialog a;
    String nome, cognome, user, pass, telefono, luogo, data, email, colore, sesso, foto;

    /*
    1.OUR CONSTRUCTOR
    2.RECEIVE CONTEXT,URL ADDRESS AND EDITTEXTS FROM OUR MAINACTIVITY
    */

    onFinishListener mListener; // Create a variable for your interface

    // Define the interface & callbacks
    public interface onFinishListener{
        // Replace 'variableType' with the appropriate type for your result
        void onFinish(String myResult);
    }

    public void setOnFinishListener(onFinishListener l){
        mListener = (onFinishListener) l;
    }

    public InvioDatiPOST(Context ct) {
        // TODO Auto-generated constructor stub
        c = ct;
    }

    public InvioDatiPOST(Context c, String urlAddress, String[] values) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.nome =(String) values[0];
        this.cognome = (String) values[1];
        this.user = (String)values[2];
        this.data = (String) values[3];
        this.email = (String) values[4];
        this.telefono =(String) values[5];
        this.luogo = (String) values[6];
        this.colore = (String) values[7];
        this.sesso = (String) values[8];
        this.pass = (String) values[9];
        this.foto = (String) values[10];


/*//GET TEXTS FROM EDITEXTS
        name=nameTxt.getText().toString();
        pos=posTxt.getText().toString();
        team=teamTxt.getText().toString();*/
    }

    protected void onProgressUpdate(Void... values) {
        a.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        //return this.send();
        HttpURLConnection con = null;
        con = (HttpURLConnection) Connector.connect(urlAddress);
        if(con==null)
        {
            return null;
        }
        try
        {
            OutputStream os=con.getOutputStream();
//WRITE
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new DataPackager(nome, cognome, user, pass, email, data, telefono, colore, luogo, sesso, foto).packData());
            bw.flush();
//RELEASE RES
            bw.close();
            os.close();
//HAS IT BEEN SUCCESSFUL?
            int responseCode=con.getResponseCode();
            if(responseCode==con.HTTP_OK)
            {
//GET EXACT RESPONSE
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response=new StringBuffer();
                String line;
//READ LINE BY LINE
                while ((line=br.readLine()) != null)
                {
                    response.append(line);
                }
//RELEASE RES
                br.close();
                return response.toString() + " --- STATUS: " + responseCode;
            }else
            {
                return "STATUS: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        1.SHOW PROGRESS DIALOG WHILE DOWNLOADING DATA
        */
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        a = new ProgressDialog(c, R.style.MyTheme);
        a.setCancelable(false);
        a.setCanceledOnTouchOutside(false);
        a.setTitle("Attendi...");
        int x = (int)(Math.random()*((8-1)+1))+1;
        if (x==1)
            a.setMessage("Stiamo aspettando che Zaza la smetta di saltare!");
        if (x==2)
            a.setMessage("Stiamo aspettando che Inzaghi passi la palla a Barone!");
        if (x==3)
            a.setMessage("Ti capiamo, anche Mazzarri sta già indicando l'orologio!");
        if (x==4)
            a.setMessage("Stiamo aspettando che Grosso calci il rigore decisivo!");
        if (x==5)
            a.setMessage("Stiamo aspettando che gli assistenti della VAR diano il responso!");
        if (x==6)
            a.setMessage("Stiamo aspettando un autorete di Koulibaly contro la Juventus!");
        if (x==7)
            a.setMessage("Stiamo aspettando che la Juventus vinca una finale di Champions!");
        if (x==8)
            a.setMessage("Stiamo aspettando che l'Inter vada in B!");
        a.show();
    }
    /*
    1.WHERE WE SEND DATA TO NETWORK
    2.RETURNS FOR US A STRING
    */
    /*
    1. CALLED WHEN JOB IS OVER
    2. WE DISMISS OUR PD
    3.RECEIVE A STRING FROM DOINBACKGROUND
    */
    @Override
    protected void onPostExecute(String result) { //sync
        if(mListener != null){
            mListener.onFinish(result);
        }
        a.dismiss();
    }
    /*
    SEND DATA OVER THE NETWORK
    RECEIVE AND RETURN A RESPONSE
    */
    /*private String send()
    {
//CONNECT
        HttpURLConnection con=Connector.connect(urlAddress);
        if(con==null)
        {
            Log.d("CIAO", "NULL");
            return null;
        }
        try
        {
            OutputStream os=con.getOutputStream();
//WRITE
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new DataPackager(nome, cognome, user, pass, email, data, telefono, colore, luogo, sesso, foto).packData());
            bw.flush();
//RELEASE RES
            bw.close();
            os.close();
//HAS IT BEEN SUCCESSFUL?
            int responseCode=con.getResponseCode();
            if(responseCode==con.HTTP_OK)
            {
//GET EXACT RESPONSE
                Log.d("CIAO", "OK");
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response=new StringBuffer();
                String line;
//READ LINE BY LINE
                while ((line=br.readLine()) != null)
                {
                    response.append(line);
                }
//RELEASE RES
                br.close();
                return response.toString() + responseCode;
            }else
            {
                Log.d("CIAO", "NOT");
                return "STATUS: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}