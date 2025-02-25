package dad.controller;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    //model

    private final ObjectProperty<File> file = new SimpleObjectProperty<>();
    private final ReadOnlyStringWrapper name = new ReadOnlyStringWrapper("Untitled");
    private final StringProperty content = new SimpleStringProperty();
    private final BooleanProperty hasChanges = new SimpleBooleanProperty();


    //view

    @FXML
    private TextArea editArea;

    @FXML
    private AnchorPane root;

    public  EditorController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditorView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //bindings

        //file.addListener(this::onFileChanged);

        editArea.textProperty().bindBidirectional(content);

        content.addListener(this::onContentChanges);

        name.bind(Bindings.createStringBinding(this::updateName, file, hasChanges));


    }

    private void onContentChanges(Observable o, String ov, String nv) {
            hasChanges.set(true);

    }

//    private void onFileChanged(Observable o, File ov, File nv) {
//        if ( nv != null) {
//            open();
//        }
//    }

    private String updateName() {
        return (file.get() == null ? "Untitled" : file.get().getName()) +
                (hasChanges.get() ? "*" : "");
    }

    public void open() {

        try {
            this.content.set(Files.readString(file.get().toPath()));
            this.hasChanges.set(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void  open(File file) {
        setFile(file);
        open();
    }

    public void save() {
        try {
            Files.writeString(file.get().toPath(), content.get());
            this.hasChanges.set(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAs(File file) {

        setFile(file);
        save();

    }

    public AnchorPane getRoot() {
        return root;
    }

    public void cut() {
        editArea.cut();
    }

    public void copy() {
        editArea.copy();
    }

    public void paste() {
        editArea.paste();
    }

    public void undo() {
        editArea.undo();
    }

    public void rendo() {
        editArea.redo();
    }

    //getters & setters


    public File getFile() {
        return file.get();
    }

    public ObjectProperty<File> fileProperty() {
        return file;
    }

    public void setFile(File file) {
        this.file.set(file);
    }

    public String getName() {
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty() {
        return name.getReadOnlyProperty();
    }


}
