package com.example.controlecombustivel;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoricoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Histórico de Abastecimentos");

        ListView listaView = new ListView(this);
        listaView.setPadding(12, 12, 12, 12);
        setContentView(listaView);

        FuelRepository repositorio = FuelRepository.getInstance();
        List<Abastecimento> abastecimentos = repositorio.getLista();
        List<String> linhas = new ArrayList<>();

        for (int i = 0; i < abastecimentos.size(); i++) {
            Abastecimento item = abastecimentos.get(i);
            StringBuilder texto = new StringBuilder();
            texto.append("Data: ").append(item.getData()).append("\n")
                 .append("Hodômetro: ").append(item.getHodometro()).append(" km\n")
                 .append("Litros: ").append(item.getLitros()).append(" L\n")
                 .append("Combustível: ").append(item.getTipoCombustivel()).append("\n")
                 .append(String.format(Locale.getDefault(), "Preço por litro: R$ %.2f\n", item.getValorPorLitro()));

            if (i == 0) {
                texto.append("\nPrimeiro abastecimento: ainda não há intervalo para calcular.");
            } else {
                double consumo = repositorio.calcularConsumoMedio(item);
                double custo = repositorio.calcularCustoPorKm(item);
                double mediaDia = repositorio.calcularKmPorDia(item);
                texto.append("\nAnálise do intervalo:\n")
                     .append(String.format(Locale.getDefault(), "Consumo médio: %.2f km/L\n", consumo))
                     .append(String.format(Locale.getDefault(), "Custo por km: R$ %.2f\n", custo))
                     .append(String.format(Locale.getDefault(), "Média por dia: %.2f km/dia", mediaDia));
            }
            linhas.add(texto.toString());
        }

        if (linhas.isEmpty()) {
            linhas.add("Nenhum abastecimento foi registrado ainda.");
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, linhas);
        listaView.setAdapter(adaptador);
    }
}
