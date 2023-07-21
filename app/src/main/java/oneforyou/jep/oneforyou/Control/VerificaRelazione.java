package oneforyou.jep.oneforyou.Control;

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

public class VerificaRelazione extends AsyncTask<String, Void, String> {

    // TODO Auto-generated method stub
    Context context;
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

    public VerificaRelazione(Context ct) {
        // TODO Auto-generated constructor stub
        context = ct;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onPostExecute(String result) { //sync
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected String doInBackground(String... params) { //sync
        // TODO Auto-generated method stub

        String amico = params[0];
        String follow = params[1];
        String login_url = Connector.db + "script/verificarelazione.php";
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
            String post_data = URLEncoder.encode("amico", "UTF-8") + "=" + URLEncoder.encode(amico, "UTF-8") + "&" + URLEncoder.encode("follow", "UTF-8") + "=" + URLEncoder.encode(follow, "UTF-8");
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
            try {
                if (mListener != null) {
                    Log.d("OFY", "STO ENTRANDO NELL'ONFINISH - DATI");
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
        return resultado;
    }
}

