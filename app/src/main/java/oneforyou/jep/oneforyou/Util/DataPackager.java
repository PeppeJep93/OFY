package oneforyou.jep.oneforyou.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * 1.BASICALLY PACKS DATA WE WANNA SEND
 */
public class DataPackager {

    String nome, cognome, user, pass, telefono, luogo, data, email, colore, sesso, foto;

    /*
    SECTION 1.RECEIVE ALL DATA WE WANNA SEND
     */
    public DataPackager(String nome, String cognome, String user, String pass, String email, String data, String telefono, String colore, String luogo, String sesso, String foto) {
        this.nome = nome;
        this.cognome = cognome;
        this.user = user;
        this.email = email;
        this.data = data;
        this.telefono = telefono;
        this.colore = colore;
        this.luogo = luogo;
        this.sesso = sesso;
        this.pass = pass;
        this.foto = foto;
    }

    /*
   SECTION 2
   1.PACK THEM INTO A JSON OBJECT
   1. READ ALL THIS DATA AND ENCODE IT INTO A FROMAT THAT CAN BE SENT VIA NETWORK
    */
    public String packData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer packedData=new StringBuffer();

        try
        {
            jo.put("nome", nome);
            jo.put("cognome",cognome);
            jo.put("user",user);
            jo.put("email",email);
            jo.put("data",data);
            jo.put("telefono",telefono);
            jo.put("colore",colore);
            jo.put("luogo",luogo);
            jo.put("sesso", sesso);
            jo.put("pass", pass);
            jo.put("foto", foto);


            Boolean firstValue=true;

            Iterator it=jo.keys();

            do {
                String key=it.next().toString();
                String value=jo.get(key).toString();

                if(firstValue)
                {
                    firstValue=false;
                }else
                {
                    packedData.append("&");
                }

                packedData.append(URLEncoder.encode(key,"UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value,"UTF-8"));

            }while (it.hasNext());

            return packedData.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}