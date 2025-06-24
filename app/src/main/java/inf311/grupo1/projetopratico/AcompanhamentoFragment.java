package inf311.grupo1.projetopratico;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button; // Assuming you still have the Button for the calendar
import android.widget.DatePicker;
import android.widget.Spinner;
// ... other imports ...
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import inf311.grupo1.projetopratico.utils.App_fragment;

public class AcompanhamentoFragment extends App_fragment {

    private static final String TAG = "AcompanhamentoFragment";

    private Button calendarioButton;
    private Calendar selectedDateCalendar;
    private Spinner acompanhamentoSpinner1;
    private Spinner acompanhamentoSpinner2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: AcompanhamentoFragment onCreateView");

        View view = inflater.inflate(R.layout.fragment_acompanhamento, container, false);

        // Calendar Button Setup (from previous example)
        calendarioButton = view.findViewById(R.id.acompanhamento_calendario_btn);
        selectedDateCalendar = Calendar.getInstance();
        updateButtonText(); // For calendar button
        calendarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(); // For calendar button
            }
        });

        acompanhamentoSpinner1 = view.findViewById(R.id.acompanhamento_spinner);
        acompanhamentoSpinner2 = view.findViewById(R.id.acompanhamento_spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.spinner_options,
                android.R.layout.simple_spinner_item
        );
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.spinner2_options,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        acompanhamentoSpinner1.setAdapter(adapter);
        acompanhamentoSpinner2.setAdapter(adapter2);

        acompanhamentoSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        acompanhamentoSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void showDatePickerDialog() {
        int year = selectedDateCalendar.get(Calendar.YEAR);
        int month = selectedDateCalendar.get(Calendar.MONTH);
        int day = selectedDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedDateCalendar.set(Calendar.YEAR, year1);
                    selectedDateCalendar.set(Calendar.MONTH, monthOfYear);
                    selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateButtonText();
                },
                year,
                month,
                day);
        datePickerDialog.show();
    }

    private void updateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = dateFormat.format(selectedDateCalendar.getTime());
        calendarioButton.setText(dateString);
    }
}