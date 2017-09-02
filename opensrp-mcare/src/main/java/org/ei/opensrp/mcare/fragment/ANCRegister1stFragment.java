package org.ei.opensrp.mcare.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.ei.opensrp.mcare.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ANCRegister1stFragment extends Fragment {

    TextView textDate, textPhone, textDateLNMP;
    LinearLayout layoutDatePick, layoutEditPhone;
    CardView cardDatePickLNMP;

    private Calendar today;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public ANCRegister1stFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        today = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_ancregister1st, container, false);

        textDate = (TextView) fragmentView.findViewById(R.id.textDate);
        textPhone = (TextView) fragmentView.findViewById(R.id.textPhone);
        textDateLNMP = (TextView) fragmentView.findViewById(R.id.textDateLNMP);
        layoutDatePick = (LinearLayout) fragmentView.findViewById(R.id.layoutDatePick);
        layoutEditPhone = (LinearLayout) fragmentView.findViewById(R.id.layoutEditPhone);
        cardDatePickLNMP = (CardView) fragmentView.findViewById(R.id.cardPickDateLNMP);

        // initialize date to today's date
        textDate.setText(dateFormat.format(today.getTimeInMillis()));

        layoutDatePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pick date
                pickDate(R.id.textDate);
            }
        });

        layoutEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show edit phone dialog
                showEditPhoneDialog();
            }
        });

        cardDatePickLNMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(R.id.textDateLNMP);
            }
        });


        return fragmentView;
    }


    private void pickDate(final int id) {
        // listener
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                // get picked date
                // update view
                GregorianCalendar pickedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                if (id == R.id.textDate)
                    textDate.setText(dateFormat.format(pickedDate.getTimeInMillis()));

                else if (id == R.id.textDateLNMP)
                    textDateLNMP.setText(dateFormat.format(pickedDate.getTimeInMillis()));
            }
        };

        // dialog
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                onDateSetListener);

        datePickerDialog.setOkColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light));
        datePickerDialog.setCancelColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(getContext(), R.color.primary));

        // show dialog
        datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }


    public void showEditPhoneDialog() {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_edit_phone, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        final EditText editTextPhone = (EditText) dialogView.findViewById(R.id.editTextLocation);
        // get previously entered location
        if (textPhone.getText() != null)
            editTextPhone.setText(textPhone.getText());

        // positive button
        dialogView.findViewById(R.id.textOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = editTextPhone.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    editTextPhone.setError("Please enter a valid phone number.");
                    return;
                }

                // update view
                textPhone.setText(phone);

                // close dialog
                dialog.dismiss();
            }
        });

        // negative button
        dialogView.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close dialog
                dialog.dismiss();
            }
        });
    }
}
