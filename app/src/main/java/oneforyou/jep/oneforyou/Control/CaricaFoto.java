package oneforyou.jep.oneforyou.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import oneforyou.jep.oneforyou.Util.Connector;
import oneforyou.jep.oneforyou.Util.UploadImage;

public class CaricaFoto extends AsyncTask<String, Void, String> {

    // TODO Auto-generated method stub
    Context context;
    Bitmap bitmap;
    String username;
    ProgressDialog a;
    public static boolean asyphoto = false;
    // Create a variable for your interface

    // Define the interface & callbacks

    public CaricaFoto(Context ct, Bitmap b, String u) {
        // TODO Auto-generated constructor stub
        context = ct;
        bitmap = b;
        username = u;
//        a = new ProgressDialog(context, R.style.MyTheme);
//        a.setCancelable(false);
//        a.setCanceledOnTouchOutside(false);
//        a.setTitle("Attendi...");
//        int x = (int)(Math.random()*((8-1)+1))+1;
//        if (x==1)
//            a.setMessage("Stiamo aspettando che Zaza la smetta di saltare!");
//        if (x==2)
//            a.setMessage("Stiamo aspettando che Inzaghi passi la palla a Barone!");
//        if (x==3)
//            a.setMessage("Ti capiamo, anche Mazzarri sta già indicando l'orologio!");
//        if (x==4)
//            a.setMessage("Stiamo aspettando che Grosso calci il rigore decisivo!");
//        if (x==5)
//            a.setMessage("Stiamo aspettando che gli assistenti della VAR diano il responso!");
//        if (x==6)
//            a.setMessage("Stiamo aspettando un autorete di Koulibaly contro la Juventus!");
//        if (x==7)
//            a.setMessage("Stiamo aspettando che la Juventus vinca una finale di Champions!");
//        if (x==8)
//            a.setMessage("Stiamo aspettando che l'Inter vada in B!");
//        a.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        uploadImage();
        return null;
    }

    public void uploadImage(/*View view*/) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connector.db + "script/upload.php?", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
//                    picture.setImageResource(0);
//                    picture.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", username + ".jpg");
                params.put("image", imageToString(bitmap));
                return params;
            }
        };

        UploadImage.getInstance(context).addtoRequestQue(stringRequest);
    }


    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap = ((BitmapDrawable) picture.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
//        a.show();
    }

    @Override
    protected void onPostExecute(String result) { //sync
//        a.dismiss();
        asyphoto = true;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

}

