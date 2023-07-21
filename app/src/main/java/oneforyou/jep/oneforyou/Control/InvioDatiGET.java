package oneforyou.jep.oneforyou.Control;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Intent;

import java.net.MalformedURLException;

import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.View.OtherViews.PaginaLogin;

/**
 * Created by faiizii on 11-Feb-18.
 */

public class InvioDatiGET extends AsyncTask <String, Void,String> {

    public  String db = Connector.db;

    AlertDialog dialog;
    Context context;
    public Boolean login = false;
    public InvioDatiGET(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Login Status");
    }

    private String getDb(){
        return db;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.setMessage(s);
        dialog.show();
        if(s.contains("avvenuto"))
        {
            Intent intent_name = new Intent();
            intent_name.setClass(context.getApplicationContext(), PaginaLogin.class);
            context.startActivity(intent_name);
        }
    }
    @Override
    protected String doInBackground(String... voids) {
        String result = "";
        String connstr = voids[0];

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoInput(true);
            http.setDoOutput(true);

            /*
            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
            //String data = URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(user,"UTF-8")
            //        +"&&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
            //writer.write(data);
            writer.flush();
            writer.close();
            ops.close();
            */

            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
            String line ="";
            while ((line = reader.readLine()) != null)
            {
                result += line;
            }

            reader.close();
            ips.close();
            http.disconnect();
            return result;

        } catch (MalformedURLException e) {
            result = e.getMessage();
        } catch (IOException e) {
            result = e.getMessage();
        }


        return result;
    }
}