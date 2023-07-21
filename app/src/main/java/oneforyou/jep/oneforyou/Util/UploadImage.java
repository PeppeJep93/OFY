package oneforyou.jep.oneforyou.Util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class UploadImage {
    private static UploadImage mInstance;
    private com.android.volley.RequestQueue requestQueue;
    private static Context mctx;

    private UploadImage(Context context)  {
        mctx = context;
        requestQueue = getRequestQueue();
    }

    private com.android.volley.RequestQueue getRequestQueue() {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        return requestQueue;
    }

    public static UploadImage getInstance(Context context)
    {
        if (mInstance == null)
            mInstance = new UploadImage(context);
        return mInstance;
    }

    public<T> void addtoRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
