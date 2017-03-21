package gui;

import folder.DrivenComposition;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import folder.NotDrivenComposition;
import folder.EditFile;

/**
 * FXML ovladac ke gui
 * @author Jakub Vašta
 */
public class FXMLController implements Initializable {

    /** Konstanta pro pronasobeni hodnoty ze slideru */
    private static final int MULT_CONST = 100000;
    /** Seznam vsech souboru pro skladani - bez vedeni */
    private List<File> obj = new ArrayList<File>();
    /** Seznam vsech souboru pro editaci */
    private List<File> el = new ArrayList<File>();
    /** Soubor se scriptem k rizenemu (vedenemu) skladani */
    private File script_file;

    @FXML
    public Button scriptGo;

    @FXML
    public Button script_file_chooser;

    @FXML
    public TextArea textArea;

    @FXML
    public ChoiceBox<String> choiceBox;

    @FXML
    public Slider slider;

    @FXML
    public void compFC() {

        FileChooser dChooser = new FileChooser();
        obj = dChooser.showOpenMultipleDialog(Main.getPrimaryStage());
        textArea.appendText(obj.toString() + "\n");

    }

    @FXML
    public void choose_script_file() {
        FileChooser dChooser = new FileChooser();
        script_file = dChooser.showOpenDialog(Main.getPrimaryStage());
    }

    @FXML
    public void scriptGo() {

        if (script_file != null && script_file.exists()) {
            DrivenComposition dc = new DrivenComposition(script_file);
            dc.start();
        }
    }

    @FXML
    public void compG() {

        if (!obj.isEmpty()) {

            String files[] = new String[obj.size()];
            int i = 0;

            for (File names : obj) {
                files[i] = names.getName();
                i++;
            }

            NotDrivenComposition pr = new NotDrivenComposition(files, obj.get(0).getParentFile().toString(), Integer.parseInt(choiceBox.getValue()));
            pr.start();
        }

    }

    @FXML
    public void editFC() {
        FileChooser dChooser = new FileChooser();
        this.el = dChooser.showOpenMultipleDialog(Main.getPrimaryStage());

        textArea.appendText(el.toString() + "\n");
    }

    /**
     * Obsluha pro tlacitko Go vola editaci .obj souboru
     */
    @FXML
    public void editG() {

        el.stream()
                .forEach(e -> EditFile.editFile(e, slider.getValue() * MULT_CONST));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        OutputStream os = new Console(textArea);
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

    }
}
