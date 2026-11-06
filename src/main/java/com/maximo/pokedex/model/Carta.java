package com.maximo.pokedex.model;

public abstract class Carta {

    private String nombre;
    private int anioEdicion;
    private int cantidad;

    protected Carta() {
    }

    public Carta(String nombre, int anioEdicion, int cantidad) {
        setNombre(nombre);
        setAnioEdicion(anioEdicion);
        setCantidad(cantidad);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacío.");
        }
        this.nombre = nombre.trim();
    }

    public int getAnioEdicion() {
        return anioEdicion;
    }

    public void setAnioEdicion(int anioEdicion) {
        if (anioEdicion < 1996 || anioEdicion > 2026) {
            throw new IllegalArgumentException("El año de edición debe estar entre 1996 y 2026.");
        }
        this.anioEdicion = anioEdicion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.cantidad = cantidad;
    }

    public abstract double calcularValorEstimado();
    public abstract String getCategoria();

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Año: " + anioEdicion;
    }
}