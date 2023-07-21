package oneforyou.jep.oneforyou.Util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;


public class DatePickerFragmentAnte18 extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datttta = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year-18, month, day-1);
        Calendar cal = c;
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.add(Calendar.YEAR, -18);
        datttta.getDatePicker().setMaxDate(cal.getTimeInMillis());
        return datttta;
    }
}
