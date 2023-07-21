package oneforyou.jep.oneforyou.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.R;

public class AggiornamentoPartita extends AsyncTask<String, Void, String> {

    // TODO Auto-generated method stub
    Context context;
    ProgressDialog a;
    onFinishListener mListener; // Create a variable for your interface
    public static boolean asydata = false;

    // Define the interface & callbacks
    public interface onFinishListener{
        // Replace 'variableType' with the appropriate type for your result
        void onFinish(String myResult) throws JSONException;
    }

    public void setOnFinishListener(onFinishListener l){
        mListener = l;
    }

    public AggiornamentoPartita(Context ct) {
        // TODO Auto-generated constructor stub
        context = ct;
        a = new ProgressDialog(context, R.style.MyTheme);
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
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        a.show();
    }

    @Override
    protected void onPostExecute(String result) { //sync
        a.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        a.show();
    }

    @Override
    protected String doInBackground(String... params) { //sync
        // TODO Auto-generated method stub
        String scasa = params[0];
        String sospite = params[1];
        String ccasa = params[2];
        String cospite = params[3];
        String quota1 = params[4];
        String quotaX = params[5];
        String quota2 = params[6];
        String data = params[7];
        String ora = params[8];
        String minuti = params[9];
        String id = params[10];
        String golcasa = params[11];
        String golospite = params[12];

        String login_url = Connector.db + "script/aggiornapartita.php";
        String resultado = "";

        URL u = null;
        try {
            u = new URL(login_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection) u.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            http.setRequestMethod("POST");
        } catch (ProtocolException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
        }
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setConnectTimeout(5000);


        try {
            OutputStream out = http.getOutputStream();
            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            String post_data = URLEncoder.encode("scasa", "UTF-8") + "=" + URLEncoder.encode(scasa, "UTF-8") + "&" + URLEncoder.encode("sospite", "UTF-8") + "=" + URLEncoder.encode(sospite, "UTF-8") + "&" + URLEncoder.encode("ccasa", "UTF-8") + "=" + URLEncoder.encode(ccasa, "UTF-8") + "&" + URLEncoder.encode("cospite", "UTF-8") + "=" + URLEncoder.encode(cospite, "UTF-8") + "&" + URLEncoder.encode("quota1", "UTF-8") + "=" + URLEncoder.encode(quota1, "UTF-8") + "&" + URLEncoder.encode("quotaX", "UTF-8") + "=" + URLEncoder.encode(quotaX, "UTF-8")  + "=" + URLEncoder.encode(quotaX, "UTF-8") + "&" + URLEncoder.encode("quota2", "UTF-8") + "=" + URLEncoder.encode(quota2, "UTF-8") + "&" + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8") + "&" + URLEncoder.encode("ora", "UTF-8") + "=" + URLEncoder.encode(ora, "UTF-8") + "&" + URLEncoder.encode("minuti", "UTF-8") + "=" + URLEncoder.encode(minuti, "UTF-8") + "&" + URLEncoder.encode("id", "UTF-8")  + "=" + URLEncoder.encode(id, "UTF-8")  + "&" + URLEncoder.encode("golcasa", "UTF-8")  + "=" + URLEncoder.encode(golcasa, "UTF-8")  + "&" + URLEncoder.encode("golospite", "UTF-8")  + "=" + URLEncoder.encode(golospite, "UTF-8");
            bf.write(post_data);
            bf.flush();
            bf.close();
            out.close();
            InputStream in = http.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            bf.close();
            in.close();
            http.disconnect();
            a.dismiss();
            try {
                if (mListener != null) {
                    Log.d("OFY", "STO ENTRANDO NELL'ONFINISH - DATI");
                    Log.d("OFY", result);
                    mListener.onFinish(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        } catch (ConnectException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
        } catch (UnsupportedEncodingException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
        } catch (ProtocolException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
        } catch (MalformedURLException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
        } catch (SocketTimeoutException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
        } catch (IOException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
        }
        a.dismiss();
        return resultado;
    }
}

