package dad.controller;

import dad.PepencilTab;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    private ObjectProperty<Tab> selectedTab = new SimpleObjectProperty<>();

//    private final MapProperty<Tab, EditorController> controllers = new SimpleMapProperty<>(FXCollections.observableHashMap());

    @FXML
    private TabPane editionTabPane;

    @FXML
    private BorderPane root;

    public  RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //eliminación de las pestañas iniciales
        editionTabPane.getTabs().clear();

        newTab();

        selectedTab.bind(editionTabPane.getSelectionModel().selectedItemProperty());

    }

    public BorderPane getRoot() {
        return root;
    }

    private EditorController getSelectedEditor() {
        return ((PepencilTab) selectedTab.get()).getController();
    }

    @FXML
    void onCloseAction(ActionEvent event) {

    }

    @FXML
    void onCloseAllAction(MouseEvent event) {

    }

    @FXML
    void onCopyAction(MouseEvent event) {

        getSelectedEditor().copy();

    }

    @FXML
    void onCutAction(ActionEvent event) {

        getSelectedEditor().cut();

    }

    @FXML
    void onNewAction(ActionEvent event) {

        newTab();
    }

    private PepencilTab newTab() {

        PepencilTab newTab = new PepencilTab();
        editionTabPane.getTabs().add(newTab);
        editionTabPane.getSelectionModel().select(newTab);//selecciona la pestaña nueva

        return newTab;
    }

    @FXML
    void onOpenAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos (*.*)", "*.*"));
        File file = fileChooser.showOpenDialog(getRoot().getScene().getWindow());
        if (file != null) {

            PepencilTab tab = newTab();
            tab.getController().open();
        }

    }

    @FXML
    void onPasteAction(ActionEvent event) {

        getSelectedEditor().paste();

    }

    @FXML
    void onRedoAction(ActionEvent event) {

        getSelectedEditor().rendo();

    }

    @FXML
    void onSaveAction(ActionEvent event) {

        if (getSelectedEditor().getFile() != null)
            getSelectedEditor().save();
        else
            onSaveAsAction(event);
    }

    @FXML
    void onSaveAsAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos (*.*)", "*.*"));
        File file = fileChooser.showSaveDialog(getRoot().getScene().getWindow());
        if (file != null) {
            getSelectedEditor().setFile(file);
            getSelectedEditor().save();
        }

    }

    @FXML
    void onUndoAction(ActionEvent event) {

        getSelectedEditor().undo();

    }
}
