package com.example.controlecombustivel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Abastecimento implements Serializable {
    private String data;
    private double hodometro;
    private double litros;
    private String tipoCombustivel;
    private double valorPorLitro;

    public Abastecimento(String data, double hodometro, double litros, String tipoCombustivel, double valorPorLitro) {
        this.data = data;
        this.hodometro = hodometro;
        this.litros = litros;
        this.tipoCombustivel = tipoCombustivel;
        this.valorPorLitro = valorPorLitro;
    }

    public String getData() { return data; }
    public double getHodometro() { return hodometro; }
    public double getLitros() { return litros; }
    public String getTipoCombustivel() { return tipoCombustivel; }
    public double getValorPorLitro() { return valorPorLitro; }

    public Date getDataAsObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return sdf.parse(data);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
