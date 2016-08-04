package graphics.g2d.fonts;

import java.util.ArrayList;

public final class GestorFuentes {

	private static final ArrayList<Fuente> fuentes = new ArrayList<>();

	private GestorFuentes() {

	}

	// Ir rellenando esto a medida de necesitarlo
	public static final void iniciarFuentesBasicas() {
		agregarFuente(new Fuente("Arial", 32, 4).ponerOffsetA('¿', 4f));
		agregarFuente(new Fuente("Source Sans Pro", 32, 6.55f).ponerOffsetA('¿', 5.9f).ponerOffsetA(',', 4.1f)
				.ponerOffsetA(new char[] { 'y', 'p', 'g', 'q', '(', ')' }, 6.55f));
	}

	public static final void agregarFuente(final Fuente fuente) {
		fuentes.add(fuente);
	}

	public static final Fuente obtener(final String nombre) {
		for (final Fuente fuente : fuentes)
			if (fuente.obtenerNombre() == nombre)
				return fuente;
		return null;
	}

	public static final Fuente obtener(final String nombre, final float grandaria) {
		for (final Fuente fuente : fuentes)
			if (fuente.obtenerGrandaria() == grandaria && fuente.obtenerNombre() == nombre)
				return fuente;

		return null;
	}

}
