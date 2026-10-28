package com.example.controlecombustivel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etData, etHodometro, etLitros, etValorLitro;
    private RadioGroup rgCombustivel;
    private EditText etPrecoEtanol, etPrecoGasolina;
    private TextView tvResultadoFlex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Controle de Combustível");
        FuelRepository.getInstance().iniciar(this);

        etData = findViewById(R.id.etData);
        etHodometro = findViewById(R.id.etHodometro);
        etLitros = findViewById(R.id.etLitros);
        etValorLitro = findViewById(R.id.etValorLitro);
        rgCombustivel = findViewById(R.id.rgCombustivel);
        etPrecoEtanol = findViewById(R.id.etPrecoEtanol);
        etPrecoGasolina = findViewById(R.id.etPrecoGasolina);
        tvResultadoFlex = findViewById(R.id.tvResultadoFlex);

        Button btnSalvar = findViewById(R.id.btnSalvar);
        Button btnVerHistorico = findViewById(R.id.btnVerHistorico);
        Button btnCalcularFlex = findViewById(R.id.btnCalcularFlex);

        btnSalvar.setOnClickListener(v -> salvarAbastecimento());
        btnVerHistorico.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoricoActivity.class)));
        btnCalcularFlex.setOnClickListener(v -> calcularOpcaoVantajosa());
    }

    private double lerNumero(String valor) {
        return Double.parseDouble(valor.trim().replace(',', '.'));
    }

    private void salvarAbastecimento() {
        String data = etData.getText().toString().trim();
        String hodometroTexto = etHodometro.getText().toString().trim();
        String litrosTexto = etLitros.getText().toString().trim();
        String valorTexto = etValorLitro.getText().toString().trim();

        if (data.isEmpty() || hodometroTexto.isEmpty() || litrosTexto.isEmpty() || valorTexto.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rgCombustivel.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Escolha o tipo de combustível.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formato.setLenient(false);
        try {
            Date dataInformada = formato.parse(data);
            if (dataInformada == null) throw new ParseException("Data inválida", 0);
        } catch (ParseException e) {
            Toast.makeText(this, "Digite a data no formato dd/MM/aaaa.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double hodometro = lerNumero(hodometroTexto);
            double litros = lerNumero(litrosTexto);
            double valorLitro = lerNumero(valorTexto);
            if (hodometro < 0 || litros <= 0 || valorLitro <= 0) {
                Toast.makeText(this, "Use valores maiores que zero para litros e preço.", Toast.LENGTH_SHORT).show();
                return;
            }

            FuelRepository repositorio = FuelRepository.getInstance();
            if (!repositorio.getLista().isEmpty()) {
                double maiorHodometro = repositorio.getLista().get(repositorio.getLista().size() - 1).getHodometro();
                if (hodometro <= maiorHodometro) {
                    Toast.makeText(this, "O hodômetro deve ser maior que o registro anterior.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            int idSelecionado = rgCombustivel.getCheckedRadioButtonId();
            RadioButton radioSelecionado = findViewById(idSelecionado);
            Abastecimento novo = new Abastecimento(data, hodometro, litros,
                    radioSelecionado.getText().toString(), valorLitro);
            repositorio.adicionar(novo);

            Toast.makeText(this, "Abastecimento registrado!", Toast.LENGTH_SHORT).show();
            etHodometro.setText("");
            etLitros.setText("");
            etValorLitro.setText("");
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Confira os números digitados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void calcularOpcaoVantajosa() {
        String etanolTexto = etPrecoEtanol.getText().toString().trim();
        String gasolinaTexto = etPrecoGasolina.getText().toString().trim();

        if (etanolTexto.isEmpty() || gasolinaTexto.isEmpty()) {
            Toast.makeText(this, "Informe os preços dos dois combustíveis.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double precoEtanol = lerNumero(etanolTexto);
            double precoGasolina = lerNumero(gasolinaTexto);
            if (precoEtanol <= 0 || precoGasolina <= 0) {
                Toast.makeText(this, "Os preços devem ser maiores que zero.", Toast.LENGTH_SHORT).show();
                return;
            }

            double relacao = precoEtanol / precoGasolina;
            if (relacao <= 0.70) {
                tvResultadoFlex.setText(String.format(Locale.getDefault(),
                        "Etanol compensa mais. Relação: %.1f%%", relacao * 100));
            } else {
                tvResultadoFlex.setText(String.format(Locale.getDefault(),
                        "Gasolina compensa mais. Relação: %.1f%%", relacao * 100));
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Confira os preços digitados.", Toast.LENGTH_SHORT).show();
        }
    }
}
