package neilj.studyroomscheduler;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private final int REQUEST_LIMIT = 3 * 3600;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        OrderActivity order = (OrderActivity) getActivity();
        final Calendar c = Calendar.getInstance();
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        int secDif = (int) ((datetime.getTimeInMillis() - c.getTimeInMillis())/1000);
        if(secDif < 0){
            //next day
            secDif += 3600 * 24;
        }
        if (secDif > REQUEST_LIMIT){
            //invalid overtime
            String msg = "Invalid Time: Over limit of " + REQUEST_LIMIT / 3600 + " hours";
            Toast.makeText(getContext(),
                    msg, Toast.LENGTH_SHORT).show();
        } else {
            order.processTimePickerResult(hourOfDay, minute, secDif);
        }
    }
}
