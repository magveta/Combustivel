package com.example.controlecombustivel;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FuelRepository {
    private static FuelRepository instance;
    private final List<Abastecimento> listaAbastecimentos;
    private SharedPreferences preferencias;

    private FuelRepository() {
        listaAbastecimentos = new ArrayList<>();
    }

    public static synchronized FuelRepository getInstance() {
        if (instance == null) instance = new FuelRepository();
        return instance;
    }

    public synchronized void iniciar(Context context) {
        if (preferencias != null) return;
        preferencias = context.getApplicationContext().getSharedPreferences("dados_combustivel", Context.MODE_PRIVATE);
        carregar();
    }

    public void adicionar(Abastecimento abastecimento) {
        listaAbastecimentos.add(abastecimento);
        Collections.sort(listaAbastecimentos, Comparator.comparingDouble(Abastecimento::getHodometro));
        salvar();
    }

    public List<Abastecimento> getLista() {
        return listaAbastecimentos;
    }

    private void carregar() {
        if (preferencias == null) return;
        String jsonSalvo = preferencias.getString("lista", "[]");
        try {
            JSONArray array = new JSONArray(jsonSalvo);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                listaAbastecimentos.add(new Abastecimento(
                        obj.getString("data"), obj.getDouble("hodometro"),
                        obj.getDouble("litros"), obj.getString("tipo"),
                        obj.getDouble("valor")));
            }
            Collections.sort(listaAbastecimentos, Comparator.comparingDouble(Abastecimento::getHodometro));
        } catch (Exception e) {
            listaAbastecimentos.clear();
        }
    }

    private void salvar() {
        if (preferencias == null) return;
        JSONArray array = new JSONArray();
        try {
            for (Abastecimento item : listaAbastecimentos) {
                JSONObject obj = new JSONObject();
                obj.put("data", item.getData());
                obj.put("hodometro", item.getHodometro());
                obj.put("litros", item.getLitros());
                obj.put("tipo", item.getTipoCombustivel());
                obj.put("valor", item.getValorPorLitro());
                array.put(obj);
            }
            preferencias.edit().putString("lista", array.toString()).apply();
        } catch (Exception ignored) {
            // Evita que um erro ao salvar feche o aplicativo.
        }
    }

    public Abastecimento getAnterior(Abastecimento atual) {
        int indice = listaAbastecimentos.indexOf(atual);
        return indice > 0 ? listaAbastecimentos.get(indice - 1) : null;
    }

    public double calcularConsumoMedio(Abastecimento atual) {
        Abastecimento anterior = getAnterior(atual);
        if (anterior == null || atual.getLitros() <= 0) return 0.0;
        double km = atual.getHodometro() - anterior.getHodometro();
        return km / atual.getLitros();
    }

    public double calcularCustoPorKm(Abastecimento atual) {
        Abastecimento anterior = getAnterior(atual);
        if (anterior == null) return 0.0;
        double km = atual.getHodometro() - anterior.getHodometro();
        if (km <= 0) return 0.0;
        return (atual.getLitros() * atual.getValorPorLitro()) / km;
    }

    public double calcularKmPorDia(Abastecimento atual) {
        Abastecimento anterior = getAnterior(atual);
        if (anterior == null) return 0.0;
        double km = atual.getHodometro() - anterior.getHodometro();
        long diferenca = Math.abs(atual.getDataAsObject().getTime() - anterior.getDataAsObject().getTime());
        long dias = TimeUnit.DAYS.convert(diferenca, TimeUnit.MILLISECONDS);
        if (dias == 0) dias = 1;
        return km / (double) dias;
    }
}
