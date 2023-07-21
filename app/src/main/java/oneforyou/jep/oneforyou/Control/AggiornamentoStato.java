package oneforyou.jep.oneforyou.Control;

import android.content.Context;
import android.os.AsyncTask;

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

public class AggiornamentoStato extends AsyncTask<String, Void, String> {

    // TODO Auto-generated method stub
    Context context;
    onFinishListener mListener; // Create a variable for your interface

    // Define the interface & callbacks
    public interface onFinishListener{
        // Replace 'variableType' with the appropriate type for your result
        void onFinish(String myResult);
    }

    public void setOnFinishListener(onFinishListener l){
        mListener = l;
    }

    public AggiornamentoStato(Context ct) {
        // TODO Auto-generated constructor stub
        context = ct;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onPostExecute(String result) { //sync
        if(mListener != null){
            mListener.onFinish(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    @Override
    protected String doInBackground(String... params) { //sync
        // TODO Auto-generated method stub

        String username = params[0];
        String login_url = Connector.db + "script/aggiornastato.php";

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
            String post_data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
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

