package dad.miclienteftp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
public class ClienteController implements Initializable{

	@FXML
    private GridPane view;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuServer;

    @FXML
    private MenuItem conectarItem;

    @FXML
    private MenuItem desconectarItem;

    @FXML
    private TableView<?> clienteTable;

    @FXML
    private TableColumn<?, ?> nombreColumn;

    @FXML
    private TableColumn<?, ?> tamanoColumn;

    @FXML
    private TableColumn<?, ?> tipoColumn;

    @FXML
    private Button descargarButton;

    @FXML
    void onConectarAction(ActionEvent event) {

    	InicioController controller = new InicioController();
		controller.showOnStage(App.getPrimaryStage());
    }

    @FXML
    void onDescargarAction(ActionEvent event) {

    }

    @FXML
    void onDesconectarAction(ActionEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public GridPane getView() {
		// TODO Auto-generated method stub
		return view;
	}
	public ClienteController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClienteView.fxml"));
		loader.setController(this);
		loader.load();
	}


}
