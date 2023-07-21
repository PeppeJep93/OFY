package oneforyou.jep.oneforyou.Control;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

public class PrendiFoto extends AsyncTask<String, Void, String> {

    // TODO Auto-generated method stub
    Context context;
    ProgressDialog a;
    onFinishListener mListener; // Create a variable for your interface

    // Define the interface & callbacks
    public interface onFinishListener{
        // Replace 'variableType' with the appropriate type for your result
        void onFinish(String myResult);
    }

    public void setOnFinishListener(onFinishListener l){
        mListener = l;
    }

    public PrendiFoto(Context ct) {
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

        String foto = params[0];
        String resultado = "";

        URL u = null;
        try {
            u = new URL(foto);
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
            InputStream input = http.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            http.disconnect();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
        return null;
    }

}

