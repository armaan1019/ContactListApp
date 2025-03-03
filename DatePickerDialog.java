package edu.lakeland.mycontactlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialog extends DialogFragment {
    Calendar selectedDate;

    public interface SaveDateListener{
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Create the view from the the xml.
        final View view = inflater.inflate(R.layout.select_date, container);

        getDialog().setTitle(R.string.selectdate);
        selectedDate = Calendar.getInstance();

        final CalendarView cv = view.findViewById(R.id.calendarView);

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate.set(year, month, dayOfMonth);
            }
        });

        Button selectButton = view.findViewById(R.id.buttonSelect);
        Button cancelButton = view.findViewById(R.id.buttonCancel);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(selectedDate);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void saveItem(Calendar selectedDate) {
        SaveDateListener activity = (SaveDateListener)getActivity();
        activity.didFinishDatePickerDialog(selectedDate);
        getDialog().dismiss();
    }
}
