package com.maximo.pokedex.model;

public class CartaEnergia extends Carta {

    private static final double VALOR_BASE = 1000;
    private static final double RECARGO_ENERGIA = 0.20;
    private static final double LIMITE_ENERGIA = 3.0;

    private double cantidadEnergia;

    public CartaEnergia() {
    }

    public CartaEnergia(String nombre, int anioEdicion, int cantidad, double cantidadEnergia) {
        super(nombre, anioEdicion, cantidad);
        setCantidadEnergia(cantidadEnergia);
    }

    public double getCantidadEnergia() {
        return cantidadEnergia;
    }

    public void setCantidadEnergia(double cantidadEnergia) {
        if (cantidadEnergia <= 0) {
            throw new IllegalArgumentException("La cantidad de energía debe ser mayor que cero.");
        }
        this.cantidadEnergia = cantidadEnergia;
    }

    @Override
    public double calcularValorEstimado() {
        if (cantidadEnergia > LIMITE_ENERGIA) {
            return VALOR_BASE + VALOR_BASE * RECARGO_ENERGIA;
        }
        return VALOR_BASE;
    }

    @Override
    public String getCategoria() {
        return "Energía";
    }
}