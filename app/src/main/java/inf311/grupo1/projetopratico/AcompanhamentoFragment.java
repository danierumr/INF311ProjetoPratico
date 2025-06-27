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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList; // Import ArrayList
import java.util.Calendar;
import java.util.List; // Import List
import java.util.Locale;

import inf311.grupo1.projetopratico.services.AtividadeService;
import inf311.grupo1.projetopratico.utils.App_fragment;

public class AcompanhamentoFragment extends App_fragment {

    private static final String TAG = "AcompanhamentoFragment";

    // Keep references to your buttons
    private Button ligacao, email, tarefa, mensagem, visita, cadastrar;
    private EditText descricao, nomeAtividade;
    private List<Button> actionButtons;

    private TextView lead_nome, escola;

    private int last_pressed = -1;
    private Button calendarioButton;
    private Calendar selectedDateCalendar;
    private Contato contato;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: AcompanhamentoFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_acompanhamento, container, false);

        Bundle args = getArguments();
        if(args != null) {
            contato = args.getParcelable("contato");
        }
        if (contato == null) {
            Log.e(TAG, "Contato não encontrado nos argumentos");
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Erro ao carregar dados do lead", Toast.LENGTH_SHORT).show();
            }
        }

        lead_nome = view.findViewById(R.id.acompanhamento_lead_nome);
        escola = view.findViewById(R.id.acompanhamento_escola);
        lead_nome.setText(contato.nome);
        escola.setText(contato.escola + " • " + contato.serie);

        descricao = view.findViewById(R.id.acompanhamento_descricao);
        nomeAtividade = view.findViewById(R.id.acompanhamento_nome_atividade_caixa);

        calendarioButton = view.findViewById(R.id.acompanhamento_calendario_btn);
        selectedDateCalendar = Calendar.getInstance();
        updateCalendarButtonText();
        calendarioButton.setOnClickListener(v -> showDatePickerDialog());

        ligacao = view.findViewById(R.id.acompanhamento_ligacao_btn);
        email = view.findViewById(R.id.acompanhamento_email_btn);
        tarefa = view.findViewById(R.id.acompanhamento_tarefa_btn);
        mensagem = view.findViewById(R.id.acompanhamento_mensagem_btn);
        visita = view.findViewById(R.id.acompanhamento_visita_btn);
        cadastrar = view.findViewById(R.id.acompanhamento_add_activity_btn);

        actionButtons = new ArrayList<>();
        actionButtons.add(ligacao);
        actionButtons.add(email);
        actionButtons.add(mensagem);
        actionButtons.add(visita);
        actionButtons.add(tarefa);

        ligacao.setOnClickListener(v -> handleActionButtonClick(1, ligacao));
        email.setOnClickListener(v -> handleActionButtonClick(2, email));
        mensagem.setOnClickListener(v -> handleActionButtonClick(3, mensagem));
        tarefa.setOnClickListener(v -> handleActionButtonClick(4, tarefa));
        visita.setOnClickListener(v -> handleActionButtonClick(5, visita));
        cadastrar.setOnClickListener(v -> {
            try {
                handleCadastrar();
            } catch (IOException e) {
                Log.e(TAG, "CAIMO NO CATCH", e);
                throw new RuntimeException(e);
            }
        });

        updateButtonBackgrounds();

        return view;
    }

    private void handleActionButtonClick(int buttonIndex, Button clickedButton) {
        last_pressed = buttonIndex;
        updateButtonBackgrounds();
        Log.d(TAG, "Button " + buttonIndex + " pressed.");
    }

    private void handleCadastrar() throws IOException {
        AtividadeService.CadastroAtividade(
                nomeAtividade.getText().toString(),
                "0",
                String.valueOf(last_pressed),
                "1",
                calendarioButton.getText().toString(),
                String.valueOf(contato.id),
                descricao.getText().toString(),
                "",
                "",
                "",
                "",
                "0"
        );
        Toast.makeText(getContext(), "Atividade cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
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