package inf311.grupo1.projetopratico;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList; // Import ArrayList
import java.util.Calendar;
import java.util.List; // Import List
import java.util.Locale;

import inf311.grupo1.projetopratico.utils.App_fragment;

public class AcompanhamentoFragment extends App_fragment {

    private static final String TAG = "AcompanhamentoFragment";

    // Keep references to your buttons
    private Button ligacao, email, reuniao, nota;
    private List<Button> actionButtons;

    private int last_pressed = -1;
    private Button calendarioButton;
    private Calendar selectedDateCalendar;
    private Spinner acompanhamentoSpinner1;
    private Spinner acompanhamentoSpinner2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: AcompanhamentoFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_acompanhamento, container, false);

        // Calendar Button Setup
        calendarioButton = view.findViewById(R.id.acompanhamento_calendario_btn);
        selectedDateCalendar = Calendar.getInstance();
        updateCalendarButtonText();
        calendarioButton.setOnClickListener(v -> showDatePickerDialog());

        // Spinners Setup
        acompanhamentoSpinner1 = view.findViewById(R.id.acompanhamento_spinner);
        acompanhamentoSpinner2 = view.findViewById(R.id.acompanhamento_spinner2);
        setupSpinner(acompanhamentoSpinner1, R.array.spinner_options);
        setupSpinner(acompanhamentoSpinner2, R.array.spinner2_options);

        // Action Buttons Setup
        ligacao = view.findViewById(R.id.acompanhamento_ligacao_btn);
        email = view.findViewById(R.id.acompanhamento_email_btn);
        reuniao = view.findViewById(R.id.acompanhamento_reuniao_btn);
        nota = view.findViewById(R.id.acompanhamento_nota_btn);

        // Initialize the list of action buttons
        actionButtons = new ArrayList<>();
        actionButtons.add(ligacao);
        actionButtons.add(email);
        actionButtons.add(reuniao);
        actionButtons.add(nota);

        // Set click listeners for each button
        ligacao.setOnClickListener(v -> handleActionButtonClick(0, ligacao));
        email.setOnClickListener(v -> handleActionButtonClick(1, email));
        reuniao.setOnClickListener(v -> handleActionButtonClick(2, reuniao));
        nota.setOnClickListener(v -> handleActionButtonClick(3, nota));

        // Optional: Set an initial state (e.g., no button pressed or first button pressed)
        updateButtonBackgrounds(); // To set all to default initially

        return view;
    }

    private void setupSpinner(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayResourceId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle spinner item selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void handleActionButtonClick(int buttonIndex, Button clickedButton) {
        last_pressed = buttonIndex;
        updateButtonBackgrounds();
        Log.d(TAG, "Button " + buttonIndex + " pressed.");
    }

    private void updateButtonBackgrounds() {
        for (int i = 0; i < actionButtons.size(); i++) {
            Button button = actionButtons.get(i);
            button.setSelected(i == last_pressed);
        }
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
                    updateCalendarButtonText();
                },
                year,
                month,
                day);
        datePickerDialog.show();
    }

    private void updateCalendarButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = dateFormat.format(selectedDateCalendar.getTime());
        calendarioButton.setText(dateString);
    }
}