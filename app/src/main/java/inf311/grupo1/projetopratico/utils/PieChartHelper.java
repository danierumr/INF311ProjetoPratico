package inf311.grupo1.projetopratico.utils;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import inf311.grupo1.projetopratico.models.ChartData;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para configurar gráficos de pizza de forma reutilizável
 */
public class PieChartHelper {
    
    private static final String TAG = "PieChartHelper";
    
    /**
     * Configura um gráfico de pizza com os dados fornecidos
     * @param pieChart O componente PieChart a ser configurado
     * @param chartData Os dados do gráfico
     * @param isDashboard Se true, aplica configurações específicas para o dashboard
     */
    public static void setupPieChart(PieChart pieChart, ChartData.PieChartData chartData, boolean isDashboard) {
        try {
            if (pieChart == null || chartData == null) {
                Log.w(TAG, "PieChart ou dados são null");
                return;
            }
        
            List<PieEntry> entries = new ArrayList<>();
            List<Integer> colors = new ArrayList<>();
            
            for (ChartData.ConsultorData consultor : chartData.getConsultores()) {
                entries.add(new PieEntry(consultor.getLeads(), consultor.getNome()));
                colors.add(Color.parseColor(consultor.getCor()));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Leads por Consultor");
            dataSet.setColors(colors);
            dataSet.setValueTextSize(isDashboard ? 10f : 12f);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            PieData data = new PieData(dataSet);
            pieChart.setData(data);

            // Configurar aparência
            pieChart.getDescription().setEnabled(false);
            pieChart.setHoleRadius(isDashboard ? 35f : 40f);
            pieChart.setTransparentCircleRadius(isDashboard ? 40f : 45f);
            pieChart.setDrawEntryLabels(false);

            // Configurar legenda
            Legend legend = pieChart.getLegend();
            if (isDashboard) {
                // No dashboard, legenda mais compacta
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                legend.setDrawInside(false);
                legend.setWordWrapEnabled(true);
            } else {
                // Nas métricas, legenda na lateral
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                legend.setDrawInside(false);
            }
            
            pieChart.invalidate();
            
            Log.d(TAG, "Gráfico de pizza configurado com " + entries.size() + " entradas (Dashboard: " + isDashboard + ")");
            
        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar gráfico de pizza", e);
        }
    }
} 