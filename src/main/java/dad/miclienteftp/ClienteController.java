package dad.miclienteftp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.commons.net.ftp.FTPFile;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
public class ClienteController implements Initializable{
	InicioController controller = new InicioController();
	
	private StringProperty ruta = new SimpleStringProperty("/");
	private ListProperty<Fichero> ficheros = new SimpleListProperty<Fichero>(FXCollections.observableArrayList());
	private BooleanProperty conectado = new SimpleBooleanProperty();
	
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
    private TableView<Fichero> clienteTable;

    @FXML
    private TableColumn<Fichero, String> nombreColumn;

    @FXML
    private TableColumn<Fichero, Number> tamanoColumn;

    @FXML
    private TableColumn<Fichero, TipoFichero> tipoColumn;

    @FXML
    private Button descargarButton;
   
    @FXML
    private Label rutaLabel;

    @FXML
    void onConectarAction(ActionEvent event) throws IOException {

    	InicioController controller = new InicioController();
		controller.showOnStage(App.getPrimaryStage());
		try {
			
            controller.cliente.changeWorkingDirectory("debian/dists");
            ruta.setValue(controller.cliente.printWorkingDirectory());

            List<FTPFile> listado = Arrays.asList(controller.cliente.listFiles());

            List<Fichero> fs = 
                    listado.
                        stream().
                        map(f1 -> {
                            return new Fichero(f1);
                        }).
                        collect(Collectors.toList());

            ficheros.setAll(fs);

        } catch (IOException e) {
          
            e.printStackTrace();
        }

		}

    @FXML
    void onDescargarAction(ActionEvent event) throws IOException {
    	File descarga = new File("welcome.msg");
		FileOutputStream flujo = new FileOutputStream("welcome.msg");
		controller.cliente.retrieveFile("welcome.msg", flujo);
		flujo.flush();
		flujo.close();
    }

    @FXML
    void onDesconectarAction(ActionEvent event){
    	try {
			controller.cliente.disconnect();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	clienteTable.getItems().clear();
    	ruta.setValue("/");
    	App.info("Desconexion", "Desconexión establecida con éxito.", null);
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		clienteTable.itemsProperty().bind(ficheros);
		rutaLabel.textProperty().bind(ruta);
		nombreColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
		tamanoColumn.setCellValueFactory(v -> v.getValue().tamanoProperty());
		tipoColumn.setCellValueFactory(v -> v.getValue().tipoProperty());

		
		
		}
	
	public GridPane getView() {
		
		return view;
	}
	public ClienteController(){
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ClienteView.fxml"));
		loader.setController(this);
		loader.load();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	

	public final ListProperty<Fichero> ficherosProperty() {
		return this.ficheros;
	}
	

	public final ObservableList<Fichero> getFicheros() {
		return this.ficherosProperty().get();
	}
	

	public final void setFicheros(final ObservableList<Fichero> ficheros) {
		this.ficherosProperty().set(ficheros);
	}
	

	public final BooleanProperty conectadoProperty() {
		return this.conectado;
	}
	

	public final boolean isConectado() {
		return this.conectadoProperty().get();
	}
	

	public final void setConectado(final boolean conectado) {
		this.conectadoProperty().set(conectado);
	}
	


}
