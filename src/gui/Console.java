package gui;

import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Console extends OutputStream {
	
        private final TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void toTextArea(String text) {
            Platform.runLater(() -> console.appendText(text));
        }

        @Override
        public void write(int b) throws IOException {
            toTextArea((char) b + "");
        }
    }
