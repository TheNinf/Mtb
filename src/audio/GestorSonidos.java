package audio;

import java.util.ArrayList;

public final class GestorSonidos {

	private static final ArrayList<Sonido> sonidos = new ArrayList<>();

	private GestorSonidos() {
	}

	public static final void agregarSonido(final Sonido sonido) {
		if (!sonidos.contains(sonido))
			sonidos.add(sonido);
	}

	public static final Sonido obtener(final String nombre) {
		for (final Sonido sonido : sonidos)
			if (sonido.obtenerNombre() == nombre)
				return sonido;

		DatosWav datos = DatosWav.leerWavDesdeArchivo(nombre + ".wav");
		if (datos == null)
			return null;

		Sonido sonido = new Sonido(nombre, datos);
		sonidos.add(sonido);
		datos = null;

		return sonido;
	}

	public static final void destruir() {
		for (final Sonido sonido : sonidos)
			sonido.destruir();
		sonidos.clear();
	}

}
