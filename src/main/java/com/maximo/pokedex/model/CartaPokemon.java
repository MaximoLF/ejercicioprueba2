package com.maximo.pokedex.model;

public class CartaPokemon extends Carta implements Evolucionable {

    private static final double VALOR_BASE = 3000;
    private static final double BONO_BRILLANTE = 0.40;

    private String tipoElemental;
    private boolean brillante;
    private boolean evolucionActiva;

    public CartaPokemon() {
    }

    public CartaPokemon(String nombre, int anioEdicion, int cantidad,
                        String tipoElemental, boolean brillante) {
        super(nombre, anioEdicion, cantidad);
        setTipoElemental(tipoElemental);
        setBrillante(brillante);
        this.evolucionActiva = false;
    }

    public String getTipoElemental() {
        return tipoElemental;
    }

    public void setTipoElemental(String tipoElemental) {
        if (tipoElemental == null || tipoElemental.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo elemental no puede ser nulo ni vacío.");
        }
        this.tipoElemental = tipoElemental.trim();
    }

    public boolean isBrillante() {
        return brillante;
    }

    public void setBrillante(boolean brillante) {
        this.brillante = brillante;
    }

    @Override
    public boolean tieneEvolucionActiva() {
        return evolucionActiva;
    }

    @Override
    public void activarEvolucion() {
        this.evolucionActiva = true;
    }

    @Override
    public double calcularValorEstimado() {
        if (brillante) {
            return VALOR_BASE + VALOR_BASE * BONO_BRILLANTE;
        }
        return VALOR_BASE;
    }

    @Override
    public String getCategoria() {
        return "Pokémon";
    }
}