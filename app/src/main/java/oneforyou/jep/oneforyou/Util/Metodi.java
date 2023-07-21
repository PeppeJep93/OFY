package oneforyou.jep.oneforyou.Util;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
/*** Created by visco on 14.2.18.
 */

public class Metodi {

    public static String prendiTestoEditato(View view) {
        EditText campo = (EditText) view;
        return ((EditText) view).getText().toString();
    }

    public static String prendiTestoView(View view) {
        TextView campo = (TextView) view;
        return ((TextView) view).getText().toString();
    }

    public static void settaTestoEditato(View view, String s) {
        view = (EditText) view;
        ((EditText) view).setText(s);
    }

    public static void settaTestoView(View view, String s) {
        view = (TextView) view;
        ((TextView) view).setText(s);
    }

}
