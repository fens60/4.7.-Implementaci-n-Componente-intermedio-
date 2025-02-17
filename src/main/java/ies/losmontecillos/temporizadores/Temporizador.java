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
    /*
    *
    * */
    @FXML
    private TextField campoTiempo;
    @FXML
    private Label etiquetaTiempo;

    private final IntegerProperty tiempo = new SimpleIntegerProperty();
    private Timeline timeline;

    /*
     * Constructor de la clase {@code Temporizador}.
     * Carga la interfaz gráfica definida en el archivo FXML "Temporizador.fxml"
     * y configura este objeto como la raíz y el controlador de la vista.
     * Además, establece un listener en la propiedad {@code tiempo} para
     * actualizar automáticamente la etiqueta de tiempo cada vez que el valor
     * de la propiedad cambia.
     *
     * <p>El archivo FXML debe estar ubicado en la misma ruta de recursos que esta clase.
     * Si el archivo no se encuentra o no puede cargarse, se lanzará una excepción
     * de tipo {@code RuntimeException}.
     *
     * <p>El listener añadido a la propiedad {@code tiempo} actualiza el texto de
     * {@code etiquetaTiempo} con el nuevo valor cada vez que este cambia.
     *
     * @throws RuntimeException Si ocurre un error al cargar el archivo FXML,
     *         por ejemplo, si el archivo no existe o hay un problema de formato.
     *         La excepción incluirá un mensaje descriptivo y la causa original.
    * */
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
    /**
     * Maneja el evento de inicio del temporizador cuando se presiona un botón.
     * Verifica que el tiempo ingresado sea un número válido y mayor que 0 antes de iniciar la cuenta regresiva.
     *
     * @param event El evento de acción generado por el botón de inicio.
     */
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

    /**
     * Establece el tiempo inicial del temporizador.
     *
     * @param tiempo El tiempo en segundos para la cuenta regresiva.
     */
    public void setTiempo(int tiempo) {
        this.tiempo.set(tiempo);
    }

    /**
     * Obtiene el tiempo restante en la cuenta regresiva.
     *
     * @return El tiempo restante en segundos.
     */
    public int getTiempo() {
        return tiempo.get();
    }

    /**
     * Devuelve la propiedad de tiempo para su uso en JavaFX bindings.
     *
     * @return La propiedad {@code IntegerProperty} asociada al tiempo del temporizador.
     */
    public IntegerProperty tiempoProperty() {
        return tiempo;
    }

    /**
     * Inicia la cuenta regresiva del temporizador.
     * Crea una animación con {@code Timeline} que disminuye el tiempo cada segundo hasta llegar a cero.
     * Cuando el tiempo llega a cero, se lanza el evento de finalización.
     */
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

    /**
     * Método que se ejecuta cuando la cuenta regresiva llega a cero.
     * Muestra un mensaje indicando que el tiempo ha terminado.
     */
    private void lanzarEventoFinalizacion() {
        etiquetaTiempo.setText("¡Tiempo terminado!");
    }
}