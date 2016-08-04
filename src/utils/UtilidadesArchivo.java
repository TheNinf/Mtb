package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class UtilidadesArchivo {

	public static String leerArchivo(final String ruta) {
		FileReader lector = null;
		StringBuilder constructor = null;
		BufferedReader lectorBuffer = null;
		try {
			final File archivo = new File(ruta);
			lector = new FileReader(archivo);
			constructor = new StringBuilder();
			lectorBuffer = new BufferedReader(lector);

			String linea;
			while ((linea = lectorBuffer.readLine()) != null) {
				constructor.append(linea).append("\n");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (lector != null)
					lector.close();
				if (lectorBuffer != null)
					lectorBuffer.close();
			} catch (final Exception e2) {
				e2.printStackTrace();
			}
		}
		lector = null;
		lectorBuffer = null;
		return constructor.toString();
	}

}
