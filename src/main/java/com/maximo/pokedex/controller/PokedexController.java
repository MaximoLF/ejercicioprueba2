package com.maximo.pokedex.controller;

import com.maximo.pokedex.model.Carta;
import com.maximo.pokedex.model.CartaEnergia;
import com.maximo.pokedex.model.CartaPokemon;
import com.maximo.pokedex.repository.CartaRepository;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.util.Locale;

public class PokedexController {

    private static final String CATEGORIA_POKEMON = "Pokémon";
    private static final String CATEGORIA_ENERGIA = "Energía";

    @FXML
    private ComboBox<String> categoriaComboBox;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField anioField;
    @FXML
    private TextField cantidadField;
    @FXML
    private TextField tipoElementalField;
    @FXML
    private CheckBox brillanteCheckBox;
    @FXML
    private CheckBox evolucionActivaCheckBox;
    @FXML
    private TextField cantidadEnergiaField;

    @FXML
    private Button registrarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Label mensajeLabel;

    @FXML
    private TableView<Carta> cartasTable;
    @FXML
    private TableColumn<Carta, String> categoriaColumn;
    @FXML
    private TableColumn<Carta, String> nombreColumn;
    @FXML
    private TableColumn<Carta, Integer> anioColumn;
    @FXML
    private TableColumn<Carta, Integer> cantidadColumn;
    @FXML
    private TableColumn<Carta, String> valorColumn;

    private final ObservableList<Carta> cartas = FXCollections.observableArrayList();
    private final CartaRepository repository = new CartaRepository();

    /**
     * Se ejecuta automáticamente al cargar la vista.
     */
    @FXML
    public void initialize() {
        configurarTabla();

        categoriaComboBox.setItems(FXCollections.observableArrayList(CATEGORIA_POKEMON, CATEGORIA_ENERGIA));
        categoriaComboBox.valueProperty().addListener((observable, anterior, nuevo) -> actualizarCamposPorCategoria());
        categoriaComboBox.getSelectionModel().selectFirst();

        cargarDatos();
    }

    private void configurarTabla() {
        categoriaColumn.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getCategoria()));
        nombreColumn.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombre()));
        anioColumn.setCellValueFactory(dato ->
                new SimpleIntegerProperty(dato.getValue().getAnioEdicion()).asObject());
        cantidadColumn.setCellValueFactory(dato ->
                new SimpleIntegerProperty(dato.getValue().getCantidad()).asObject());
        valorColumn.setCellValueFactory(dato ->
                new SimpleStringProperty(formatearMoneda(dato.getValue().calcularValorEstimado())));

        cartasTable.setItems(cartas);
    }

    /**
     * Habilita únicamente los campos específicos de la categoría seleccionada.
     */
    private void actualizarCamposPorCategoria() {
        boolean esPokemon = CATEGORIA_POKEMON.equals(categoriaComboBox.getValue());

        tipoElementalField.setDisable(!esPokemon);
        brillanteCheckBox.setDisable(!esPokemon);
        evolucionActivaCheckBox.setDisable(!esPokemon);
        cantidadEnergiaField.setDisable(esPokemon);

        if (esPokemon) {
            cantidadEnergiaField.clear();
        } else {
            tipoElementalField.clear();
            brillanteCheckBox.setSelected(false);
            evolucionActivaCheckBox.setSelected(false);
        }
    }

    private void cargarDatos() {
        try {
            repository.cargar();
            cartas.setAll(repository.listar());

            if (cartas.isEmpty()) {
                mostrarMensaje("No hay datos guardados. Registre una carta para comenzar.", false);
            } else {
                mostrarMensaje("Datos cargados correctamente.", false);
            }
        } catch (RuntimeException e) {
            mostrarMensaje("Error al cargar los datos: " + e.getMessage(), true);
        }
    }

    @FXML
    private void registrarCarta() {
        try {
            String categoria = categoriaComboBox.getValue();
            if (categoria == null) {
                throw new IllegalArgumentException("Debe seleccionar una categoría de carta.");
            }

            String nombre = nombreField.getText();
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío ni contener solo espacios.");
            }

            int anio = leerEntero(anioField.getText(), "año de edición");
            if (anio < 1996 || anio > 2026) {
                throw new IllegalArgumentException("El año de edición debe estar entre 1996 y 2026.");
            }

            int cantidad = leerEntero(cantidadField.getText(), "cantidad");
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            }

            Carta carta;
            if (CATEGORIA_POKEMON.equals(categoria)) {
                String tipoElemental = tipoElementalField.getText();
                if (tipoElemental == null || tipoElemental.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "El tipo elemental no puede estar vacío ni contener solo espacios.");
                }

                CartaPokemon pokemon = new CartaPokemon(nombre, anio, cantidad, tipoElemental.trim(),
                        brillanteCheckBox.isSelected());
                if (evolucionActivaCheckBox.isSelected()) {
                    pokemon.activarEvolucion();
                }
                carta = pokemon;
            } else {
                double cantidadEnergia = leerDecimal(cantidadEnergiaField.getText());
                if (cantidadEnergia <= 0) {
                    throw new IllegalArgumentException("La cantidad de energía debe ser mayor que cero.");
                }
                carta = new CartaEnergia(nombre, anio, cantidad, cantidadEnergia);
            }

            repository.registrar(carta);
            cartas.add(carta);

            limpiarFormulario();
            mostrarMensaje(nombre + " (" + carta.getCategoria() + ") registrada correctamente.", false);
        } catch (RuntimeException e) {
            mostrarMensaje("Error: " + e.getMessage(), true);
        }
    }

    @FXML
    private void eliminarCarta() {
        Carta seleccionada = cartasTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje("Debe seleccionar una carta de la tabla para eliminarla.", true);
            return;
        }

        try {
            repository.eliminar(seleccionada);
            cartas.remove(seleccionada);
            mostrarMensaje(seleccionada.getNombre() + " (" + seleccionada.getCategoria()
                    + ") eliminada correctamente.", false);
        } catch (RuntimeException e) {
            mostrarMensaje("Error al eliminar: " + e.getMessage(), true);
        }
    }

    private void limpiarFormulario() {
        nombreField.clear();
        anioField.clear();
        cantidadField.clear();
        tipoElementalField.clear();
        cantidadEnergiaField.clear();
        brillanteCheckBox.setSelected(false);
        evolucionActivaCheckBox.setSelected(false);
        nombreField.requestFocus();
    }

    private int leerEntero(String texto, String nombreCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio.");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " debe ser un número entero.");
        }
    }

    private double leerDecimal(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La cantidad de energía es obligatoria para una carta de Energía.");
        }
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La cantidad de energía debe ser un número válido.");
        }
    }

    private String formatearMoneda(double valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(Locale.of("es", "CL"));
        return formato.format(valor);
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle(esError
                ? "-fx-text-fill: #b00020; -fx-font-weight: bold;"
                : "-fx-text-fill: #1b5e20; -fx-font-weight: bold;");
    }
}