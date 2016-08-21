package audio;

import java.util.HashMap;

public final class GestorSonidos {

	private static final HashMap<String, Sonido> sonidos = new HashMap<>();

	private GestorSonidos() {
	}

	public static final void agregarSonido(final Sonido sonido) {
		if (!sonidos.containsKey(sonido))
			sonidos.put(sonido.obtenerNombre(), sonido);
	}

	public static final Sonido obtener(final String nombre) {
		Sonido sonido = sonidos.get(nombre);
		if (sonido != null)
			return sonido;

		DatosWav datos = DatosWav.leerWavDesdeArchivo(nombre + ".wav");
		if (datos == null)
			return null;

		sonido = new Sonido(nombre, datos);
		sonidos.put(nombre, sonido);
		datos = null;

		return sonido;
	}

	public static final void destruir() {
		for (final String nombre : sonidos.keySet())
			sonidos.get(nombre).destruir();
		sonidos.clear();
	}

}
