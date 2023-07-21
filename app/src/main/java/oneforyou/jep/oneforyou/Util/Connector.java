package oneforyou.jep.oneforyou.Util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connector {

    public static String db = "http://192.168.178.35/";

    /*
    1.SHALL HELP US ESTABLISH A CONNECTION TO THE NETWORK
    1. WE ARE MAKING A POST REQUEST
    */
    public static HttpURLConnection connect(String urlAddress) {

        try
        {
            URL url=new URL(urlAddress);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            //SET PROPERTIES
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoInput(true);
            con.setDoOutput(true);

            //RETURN
            return con;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}