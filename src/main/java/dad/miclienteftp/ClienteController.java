package dad.miclienteftp;
import java.io.File;
import java.io.FileInputStream;
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
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
public class ClienteController implements Initializable{
	InicioController controller= new InicioController();;
	FileOutputStream salida;
	FileInputStream entrada;
	private StringProperty ruta = new SimpleStringProperty();
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

//    	controller = new InicioController();
		controller.showOnStage(App.getPrimaryStage());
		controller.cliente.changeWorkingDirectory("/");
		cargarFicheros();

		}

	private void cargarFicheros() {
		try {
			
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
    	System.out.println(controller.cliente.isConnected());
    	System.out.println(controller.isConectado());
    	
    	Fichero seleccionado = clienteTable.getSelectionModel().getSelectedItem();
    	System.out.println(seleccionado.getNombre());
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Guardar");
    	fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Todos los archivos", "*.*"));
    	fileChooser.setInitialFileName(seleccionado.getNombre());
    	File file = fileChooser.showSaveDialog(App.getPrimaryStage());
    	  if(file != null){
             SaveFile(seleccionado.getNombre(), file);
             App.info("Archivo Guardado","El fichero ha sido descargado", null);
          }
    }

	public void SaveFile(String nombre, File file) throws IOException {
    	File descarga = new File(nombre);
		FileOutputStream flujo = new FileOutputStream(descarga);
		controller.cliente.retrieveFile(nombre, flujo);
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
    	
    	ruta.setValue("");
    	App.info("Desconexion", "Desconexión establecida con éxito.", null);
    	
    }
	@FXML
	void onTablaMouseClicked(MouseEvent event) {
	    // si se ha pulsado dos veces y hay un elemento seleccionado en la tabla
//	    if (event.getClickCount() == 2 && clienteTable.getSelectionModel().getSelectedItem() != null) {
//	        // TODO implementar aquí la acción del doble click
//	    	System.out.println("Hay doble click");
//	    	try {
//	    		if(clienteTable.getSelectionModel().getSelectedItem().getNombre() == "..") {
//	    			controller.cliente.changeToParentDirectory();
//	    			cargarFicheros();
//	    			System.out.println(ruta.getValue());
//	    		}
//	    		controller.cliente.changeWorkingDirectory(clienteTable.getSelectionModel().getSelectedItem().getNombre());
//	    		controller.cliente.changeWorkingDirectory("/"+ ruta.getValue()+"/"+clienteTable.getSelectionModel().getSelectedItem().getNombre()+"/");
//	    		controller.cliente.changeWorkingDirectory(clienteTable.getSelectionModel().getSelectedItem().getNombre());
//	    		cargarFicheros();
//	    		System.out.println(ruta.getValue());
//	    		
//	    		
//	    	} catch (IOException e) {
//				
//				e.printStackTrace();
//			}
//	    }	
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//binds
		clienteTable.itemsProperty().bind(ficheros);
		rutaLabel.textProperty().bind(ruta);
		conectado.bindBidirectional(controller.conectadoProperty());
		
		nombreColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
		tamanoColumn.setCellValueFactory(v -> v.getValue().tamanoProperty());
		tipoColumn.setCellValueFactory(v -> v.getValue().tipoProperty());
		
		clienteTable.setRowFactory( tv -> {
		    TableRow<Fichero> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		    		try {
						controller.cliente.changeWorkingDirectory(row.getItem().getNombre());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		cargarFicheros();
		        }
		    });
		    return row;
		});
		
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
