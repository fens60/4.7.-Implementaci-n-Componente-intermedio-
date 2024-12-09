package ies.losmontecillos.temporizadores;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class Temporizador extends VBox {
    @FXML
    private TextField campoTiempo;
    @FXML
    private Label etiquetaTiempo;

    private final IntegerProperty tiempo = new SimpleIntegerProperty();
    private Timeline timeline;

    public Temporizador() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Temporizador.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo FXML de Temporizador", e);
        }

        tiempo.addListener((observable, oldValue, newValue) -> etiquetaTiempo.setText(newValue.toString()));
    }

    @FXML
    private void handleIniciar(ActionEvent event) {
        try {
            int tiempoInicial = Integer.parseInt(campoTiempo.getText());

            if (tiempoInicial > 0) {
                setTiempo(tiempoInicial);
                iniciar();
            } else {
                etiquetaTiempo.setText("Debe ser > 0");
            }
        } catch (NumberFormatException e) {
            etiquetaTiempo.setText("Número inválido");
        }
    }

    public void setTiempo(int tiempo) {
        this.tiempo.set(tiempo);
    }

    public int getTiempo() {
        return tiempo.get();
    }

    public IntegerProperty tiempoProperty() {
        return tiempo;
    }

    public void iniciar() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                            if (tiempo.get() > 0) {
                                tiempo.set(tiempo.get() - 1);
                            } else {
                                timeline.stop();
                                lanzarEventoFinalizacion();
                            }
                        }
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void lanzarEventoFinalizacion() {
        etiquetaTiempo.setText("¡Tiempo terminado!");
    }
}
