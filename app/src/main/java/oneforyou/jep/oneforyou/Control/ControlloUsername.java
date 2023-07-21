package oneforyou.jep.oneforyou.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class ControlloUsername extends AsyncTask<String, Void, String> {

    // TODO Auto-generated method stub
    Context context;
    ProgressDialog a;
    onFinishListener mListener;
    public static boolean asynick = false, puoi = false; // Create a variable for your interface

    // Define the interface & callbacks
    public interface onFinishListener{
        // Replace 'variableType' with the appropriate type for your result
        void onFinish(String myResult);
    }

    public void setOnFinishListener(onFinishListener l){
        mListener = l;
    }

    public ControlloUsername(Context ct) {
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
        super.onPreExecute();
        a.show();
        puoi = true;
    }

    @Override
    protected void onPostExecute(String result) { //sync
        super.onPostExecute(result);
        /*if(mListener != null){
            mListener.onFinish(result);
        }*/
    }


    @Override
    protected synchronized String doInBackground(String... params) { //sync
        // TODO Auto-generated method stub

        a.show();

        String username = params[0];
        String login_url = Connector.db + "script/usercontrol.php";
        String resultado = "";
        String result = "";

        if (puoi == true) {
            puoi = false;

        URL u = null;
        try {
            u = new URL(login_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection) u.openConnection();
        } catch (Exception e) {
                a.dismiss();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                resultado = writer.toString();
                if (mListener != null) {
                    Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                    mListener.onFinish(resultado);
                }
        }
        try {
            http.setRequestMethod("POST");
        } catch (ProtocolException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
            if (mListener != null) {
                Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                mListener.onFinish(resultado);
            }
        }
            http.setDoInput(true);
            http.setDoOutput(true);


        try {
            try {
                http.setConnectTimeout(5000);
            } catch (Exception e) {
                a.dismiss();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                resultado = writer.toString();
                if (mListener != null) {
                    Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                    mListener.onFinish(resultado);
                }
            }

            OutputStream out = null;

            try {
                out = http.getOutputStream();
            } catch (Exception e) {
                Writer writer = new StringWriter();
                a.dismiss();
                e.printStackTrace(new PrintWriter(writer));
                resultado = writer.toString();
                if (mListener != null) {
                    Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                    mListener.onFinish(resultado);
                }
                return resultado;
            }

            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            bf.write(post_data);
            bf.flush();
            bf.close();
            out.close();
            InputStream in = http.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            bf.close();
            in.close();
            http.disconnect();
            if (mListener != null) {
                Log.d("OFY", "STO ENTRANDO NELL'ONFINISH - NICK : " + result);
                mListener.onFinish(result);
            }
            Log.d("OFY", "STO FINENDO");
            try {
                a.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } catch (ConnectException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
            if (mListener != null) {
                Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                mListener.onFinish(resultado);
            }
        } catch (UnsupportedEncodingException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
            if (mListener != null) {
                Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                mListener.onFinish(resultado);
            }
        } catch (ProtocolException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
            if (mListener != null) {
                Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                mListener.onFinish(resultado);
            }
        } catch (MalformedURLException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
            if (mListener != null) {
                Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                mListener.onFinish(resultado);
            }
        } catch (SocketTimeoutException e) {
            Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                resultado = writer.toString();
                if (mListener != null) {
                    Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                    mListener.onFinish(resultado);
            }
        } catch (IOException e) {
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            resultado = writer.toString();
            if (mListener != null) {
                Log.d("OFY", "STO ENTRANDO NELL'ONFINISH NICK - " + resultado);
                mListener.onFinish(resultado);
            }
        }
        a.dismiss();
        return resultado;
    }
        a.dismiss();
        return resultado;
}
}

