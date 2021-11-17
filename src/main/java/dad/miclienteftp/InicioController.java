package dad.miclienteftp;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class InicioController implements Initializable {
		// instanciar el cliente FTP

		FTPClient cliente = new FTPClient();
		Stage stage;
		private StringProperty user = new SimpleStringProperty();
		private StringProperty passwd = new SimpleStringProperty();
		private StringProperty server = new SimpleStringProperty();
		private StringProperty puerto = new SimpleStringProperty();
	    
		
		@FXML
	    private GridPane view;

	    @FXML
	    private TextField servidorText;

	    @FXML
	    private TextField puertoText;

	    @FXML
	    private TextField userText;

	    @FXML
	    private PasswordField passwdText;

	    @FXML
	    private Button conectarButton;

	    @FXML
	    private Button cancelarButton;

	    @FXML
	    void onCancelarAction(ActionEvent event) {
	    	stage.close();
	    }

	    @FXML
	    void onConectarAction(ActionEvent event) {
	    	try {	
	    	
	    		// conectar con el servidor FTP
	    		cliente.connect(server.getValue(),Integer.parseInt(puerto.getValue()));

	    		// iniciar sesión anónimo (login)
	    		cliente.login(user.getValue(),passwd.getValue());

	    		// cambiar el directorio actual en el servidor
	    		cliente.changeWorkingDirectory("/debian/dists");
	    		
	    		String directorioActual = cliente.printWorkingDirectory();
				System.out.println("Directorio actual: " + directorioActual);
				
				FTPFile [] ficheros = cliente.listFiles();
				
				// recorrer el listado de archivos recuperados
				System.out.format("+------------------------+%n");
				System.out.format("| Archivos del servidor: |%n");
				System.out.format("+------------------------+-----------------+----------------+-----------------+%n");
				System.out.format("| Nombre                                   | Tamaño (bytes) | Tipo            |%n");
				System.out.format("+------------------------------------------+----------------+-----------------+%n");
				Arrays.asList(ficheros)
				
					.forEach(fichero -> {
					    System.out.format("| %-40s | %-14d | %-15s |%n", fichero.getName(), fichero.getSize(), fichero.getType());
					});
				System.out.format("+------------------------------------------+----------------+-----------------+%n");
	    
	    	}catch(IOException e) {
	    	App.error("No se puedo conectar: " + server.getValue());
	    	throw new RuntimeException(e);
	    	
	    	}
	    	App.info("Conexión", "Conexión establecida con éxito.", null);
	    	stage.close();
	    }

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			
			servidorText.textProperty().bindBidirectional(server);
			puertoText.textProperty().bindBidirectional(puerto);
			passwdText.textProperty().bindBidirectional(passwd);
			userText.textProperty().bindBidirectional(user);
		}

		public GridPane getView() {
			
			return view;
		}
		
		public InicioController(){
			try{
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InicioView.fxml"));
				loader.setController(this);
				loader.load();
			}
			catch( IOException e ) {
				throw new RuntimeException(e);
				
			}
		}

		public void showOnStage(Window ventana) {
			
			stage = new Stage();
			stage.getIcons();
			stage.setTitle("Iniciar Conexión");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(ventana);
			stage.setScene(new Scene(view, 400,200));
			stage.showAndWait();
			
		}
}
