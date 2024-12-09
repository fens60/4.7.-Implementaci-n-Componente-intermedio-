module ies.losmontecillos.temporizadores {
    requires javafx.controls;
    requires javafx.fxml;


    opens ies.losmontecillos.temporizadores to javafx.fxml;
    exports ies.losmontecillos.temporizadores;
}