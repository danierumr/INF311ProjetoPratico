package inf311.grupo1.projetopratico;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;
import java.util.List;

public class MetricasDaEquipe extends  Toolbar_activity {

    private BarChart barChart;
    private PieChart pieChart;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metricas_da_equipe);

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);
        backButton = findViewById(R.id.backButton);

        setupBarChart();
        setupPieChart();
        setupClickListeners();
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(26f, "Ana"));
        entries.add(new PieEntry(19f, "Carlos"));
        entries.add(new PieEntry(22f, "Juliana"));
        entries.add(new PieEntry(17f, "Roberto"));
        entries.add(new PieEntry(16f, "Mariana"));

        PieDataSet dataSet = new PieDataSet(entries, "Distribuição de Leads");

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#14b8a6")); // Ana - Verde
        colors.add(Color.parseColor("#F44336")); // Carlos - Vermelho
        colors.add(Color.parseColor("#2196F3")); // Juliana - Azul
        colors.add(Color.parseColor("#FF9800")); // Roberto - Laranja
        colors.add(Color.parseColor("#E91E63")); // Mariana - Rosa

        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        pieChart.setData(pieData);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(50);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);

        pieChart.setTouchEnabled(true);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.animateY(1000);

        pieChart.invalidate();
    }

    private void setupBarChart() {
        // ideal é carregar dados da api, mas por enquanto está hardcoded
        List<BarEntry> leadsEntries = new ArrayList<>();
        List<BarEntry> conversionsEntries = new ArrayList<>();

        leadsEntries.add(new BarEntry(0f, 42f)); // Ana
        leadsEntries.add(new BarEntry(1f, 38f)); // Carlos
        leadsEntries.add(new BarEntry(2f, 45f)); // Juliana
        leadsEntries.add(new BarEntry(3f, 35f)); // Roberto
        leadsEntries.add(new BarEntry(4f, 40f)); // Mariana

        conversionsEntries.add(new BarEntry(0f, 12f)); // Ana
        conversionsEntries.add(new BarEntry(1f, 8f));  // Carlos
        conversionsEntries.add(new BarEntry(2f, 10f)); // Juliana
        conversionsEntries.add(new BarEntry(3f, 7f));  // Roberto
        conversionsEntries.add(new BarEntry(4f, 5f));  // Mariana

        BarDataSet leadsDataSet = new BarDataSet(leadsEntries, "Leads");
        leadsDataSet.setColor(Color.parseColor("#14b8a6"));
        leadsDataSet.setValueTextColor(Color.BLACK);
        leadsDataSet.setValueTextSize(10f);

        BarDataSet conversionsDataSet = new BarDataSet(conversionsEntries, "Conversões");
        conversionsDataSet.setColor(Color.parseColor("#F44336"));
        conversionsDataSet.setValueTextColor(Color.BLACK);
        conversionsDataSet.setValueTextSize(10f);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(leadsDataSet);
        dataSets.add(conversionsDataSet);

        BarData barData = new BarData(dataSets);

        float groupSpace = 0.3f;
        float barSpace = 0.05f;
        float barWidth = 0.3f;

        barData.setBarWidth(barWidth);

        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final String[] names = {"Ana", "Carlos", "Juliana", "Roberto", "Mariana"};

            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < names.length) {
                    return names[index];
                }
                return "";
            }
        });
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#666666"));
        xAxis.setTextSize(12f);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(50f);
        leftAxis.setGranularity(10f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#E0E0E0"));
        leftAxis.setTextColor(Color.parseColor("#666666"));
        leftAxis.setTextSize(10f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        barChart.setDrawGridBackground(false);
        barChart.setDrawBorders(false);
        barChart.getLegend().setEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);

        barChart.groupBars(0f, groupSpace, barSpace);

        barChart.setVisibleXRangeMaximum(5f);
        barChart.moveViewToX(0f);

        barChart.animateY(1000);

        barChart.invalidate();
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}