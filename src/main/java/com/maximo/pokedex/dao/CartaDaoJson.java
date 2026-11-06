package com.maximo.pokedex.dao;

import com.maximo.pokedex.model.CartaEnergia;
import com.maximo.pokedex.model.CartaPokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CartaDaoJson implements CartaDao {

    private static final Path ARCHIVO_POKEMON = Paths.get("pokemons.json");
    private static final Path ARCHIVO_ENERGIA = Paths.get("energias.json");

    private static final Type LISTA_POKEMON = new TypeToken<List<CartaPokemon>>() {
    }.getType();
    private static final Type LISTA_ENERGIA = new TypeToken<List<CartaEnergia>>() {
    }.getType();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public List<CartaPokemon> leerCartasPokemon() {
        return leer(ARCHIVO_POKEMON, LISTA_POKEMON);
    }

    @Override
    public List<CartaEnergia> leerCartasEnergia() {
        return leer(ARCHIVO_ENERGIA, LISTA_ENERGIA);
    }

    @Override
    public void guardarCartasPokemon(List<CartaPokemon> cartas) {
        escribir(ARCHIVO_POKEMON, cartas, LISTA_POKEMON);
    }

    @Override
    public void guardarCartasEnergia(List<CartaEnergia> cartas) {
        escribir(ARCHIVO_ENERGIA, cartas, LISTA_ENERGIA);
    }

    private <T> List<T> leer(Path archivo, Type tipoLista) {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(archivo)) {
            List<T> datos = gson.fromJson(reader, tipoLista);
            return datos != null ? datos : new ArrayList<>();
        } catch (IOException | JsonParseException e) {
            throw new RuntimeException("No se pudo leer el archivo " + archivo.toAbsolutePath(), e);
        }
    }

    private void escribir(Path archivo, Object datos, Type tipoLista) {
        try (Writer writer = Files.newBufferedWriter(archivo)) {
            gson.toJson(datos, tipoLista, writer);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo " + archivo.toAbsolutePath(), e);
        }
    }
}