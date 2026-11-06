package com.maximo.pokedex.repository;

import com.maximo.pokedex.dao.CartaDao;
import com.maximo.pokedex.dao.CartaDaoJson;
import com.maximo.pokedex.model.Carta;
import com.maximo.pokedex.model.CartaEnergia;
import com.maximo.pokedex.model.CartaPokemon;

import java.util.ArrayList;
import java.util.List;

public class CartaRepository {

    private final CartaDao dao;
    private final List<Carta> cartas = new ArrayList<>();

    public CartaRepository() {
        this(new CartaDaoJson());
    }

    public CartaRepository(CartaDao dao) {
        this.dao = dao;
    }

    public void cargar() {
        List<Carta> cargadas = new ArrayList<>();
        cargadas.addAll(dao.leerCartasPokemon());
        cargadas.addAll(dao.leerCartasEnergia());

        cartas.clear();
        cartas.addAll(cargadas);
    }

    public List<Carta> listar() {
        return new ArrayList<>(cartas);
    }

    public void registrar(Carta carta) {
        List<Carta> actualizadas = new ArrayList<>(cartas);
        actualizadas.add(carta);
        guardar(actualizadas);

        cartas.clear();
        cartas.addAll(actualizadas);
    }

    public void eliminar(Carta carta) {
        List<Carta> actualizadas = new ArrayList<>(cartas);
        if (!actualizadas.remove(carta)) {
            return;
        }
        guardar(actualizadas);

        cartas.clear();
        cartas.addAll(actualizadas);
    }

    private void guardar(List<Carta> lista) {
        List<CartaPokemon> pokemones = new ArrayList<>();
        List<CartaEnergia> energias = new ArrayList<>();

        for (Carta carta : lista) {
            if (carta instanceof CartaPokemon pokemon) {
                pokemones.add(pokemon);
            } else if (carta instanceof CartaEnergia energia) {
                energias.add(energia);
            }
        }

        dao.guardarCartasPokemon(pokemones);
        dao.guardarCartasEnergia(energias);
    }
}