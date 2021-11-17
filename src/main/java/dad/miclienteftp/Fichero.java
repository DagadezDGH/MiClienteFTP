package dad.miclienteftp;
import java.io.File;

import org.apache.commons.net.ftp.FTPFile;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Fichero {
	private StringProperty nombre = new SimpleStringProperty();
	private LongProperty tamano = new SimpleLongProperty();
	private ObjectProperty<TipoFichero> tipo = new SimpleObjectProperty<TipoFichero>();


	public Fichero(FTPFile f) {
		nombre.set(f.getName());
		tamano.set(f.getSize());
//		tipo.set(f.isDirectory() ? TipoFichero.DIRECTORIO : TipoFichero.FICHERO);
		switch (f.getType()) {
		case FTPFile.DIRECTORY_TYPE: setTipo(TipoFichero.DIRECTORIO); break;
		case FTPFile.FILE_TYPE: setTipo(TipoFichero.FICHERO); break;
		case FTPFile.SYMBOLIC_LINK_TYPE: setTipo(TipoFichero.ENLACE); break;
		default: setTipo(TipoFichero.DESCONOCIDO); break;
		}
	}
	
	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	public final String getNombre() {
		return this.nombreProperty().get();
	}

	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final LongProperty tamanoProperty() {
		return this.tamano;
	}

	public final long getTamano() {
		return this.tamanoProperty().get();
	}

	public final void setTamano(final long tamano) {
		this.tamanoProperty().set(tamano);
	}

	public final ObjectProperty<TipoFichero> tipoProperty() {
		return this.tipo;
	}

	public final TipoFichero getTipo() {
		return this.tipoProperty().get();
	}

	public final void setTipo(final TipoFichero tipo) {
		this.tipoProperty().set(tipo);
	}
}