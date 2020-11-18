package window;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void pressMyButtonOnChat(ActionEvent actionEvent) throws IOException {
        server.Server.startServer();
    }

    public void pressMyButtonOffChat(ActionEvent actionEvent) throws IOException {
        server.Server.clouseServer();
    }
}
