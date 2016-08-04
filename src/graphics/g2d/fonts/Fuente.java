package graphics.g2d.fonts;

import java.awt.Font;

public class Fuente {

	private final Font fuente;
	private FontAtlas atlas;
	private final String nombre;
	private final float grandaria;

	public Fuente(final float offsetAlto) {
		this(32f, offsetAlto);
	}

	public Fuente(final float grandaria, final float offsetAlto) {
		Font fuenteAElegir = Font.getFont("Arial");
		fuente = fuenteAElegir == null ? Font.decode("Arial").deriveFont(grandaria)
				: fuenteAElegir.deriveFont(grandaria);
		fuenteAElegir = null;
		atlas = new FontAtlas(1024, (int) Math.ceil(grandaria + offsetAlto), this);
		nombre = "Arial";
		this.grandaria = grandaria;
	}

	public Fuente(final String nombre, final float offsetAlto) {
		this(nombre, 32f, offsetAlto);
	}

	public Fuente(final String nombre, final float grandaria, final float offsetAlto) {
		Font fuenteAElegir = Font.getFont(nombre);
		fuente = fuenteAElegir == null ? Font.decode(nombre).deriveFont(grandaria)
				: fuenteAElegir.deriveFont(grandaria);
		fuenteAElegir = null;
		atlas = new FontAtlas(1024, (int) Math.ceil(grandaria + offsetAlto), this);
		this.nombre = nombre;
		this.grandaria = grandaria;
	}

	public final Fuente ponerOffsetA(final char simbolo, final float offset) {
		atlas.ponerOffsetA(simbolo, offset);
		return this;
	}

	public final Fuente ponerOffsetA(final char[] simbolos, final float offset) {
		atlas.ponerOffsetA(simbolos, offset);
		return this;
	}

	public final float obtenerGrandaria() {
		return grandaria;
	}

	public final String obtenerNombre() {
		return nombre;
	}

	public final FontAtlas obtenerAtlas() {
		atlas.actualizar();
		return atlas;
	}

	public final Font obtenerFuente() {
		return fuente;
	}

}
