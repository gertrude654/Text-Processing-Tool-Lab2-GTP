module org.example.textprocessingtool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens org.example.textprocessingtool to javafx.fxml;
    exports org.example.textprocessingtool;
    exports org.example.textprocessingtool.UI;
    opens org.example.textprocessingtool.UI to javafx.fxml;
    // Export the package where Main class resides
   exports org.example.textprocessingtool.dataManager;
    opens org.example.textprocessingtool.dataManager to javafx.fxml;
    exports org.example.textprocessingtool.regex;



}