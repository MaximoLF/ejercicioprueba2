package com.maximo.pokedex.dao;

import com.maximo.pokedex.model.CartaEnergia;
import com.maximo.pokedex.model.CartaPokemon;

import java.util.List;

public interface CartaDao {
    List<CartaPokemon> leerCartasPokemon();
    List<CartaEnergia> leerCartasEnergia();
    void guardarCartasPokemon(List<CartaPokemon> cartas);
    void guardarCartasEnergia(List<CartaEnergia> cartas);
}
