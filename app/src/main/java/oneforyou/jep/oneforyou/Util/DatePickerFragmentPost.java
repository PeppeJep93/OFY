package oneforyou.jep.oneforyou.Util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;


public class DatePickerFragmentPost extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datttta = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        Calendar cal = c;
        datttta.getDatePicker().setMinDate(cal.getTimeInMillis());
        cal.add(Calendar.MONTH, +9);
        datttta.getDatePicker().setMaxDate(cal.getTimeInMillis());
        return datttta;
    }
}
