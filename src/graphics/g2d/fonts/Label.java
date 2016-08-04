package graphics.g2d.fonts;

import graphics.g2d.Renderizable2D;
import graphics.g2d.Renderizador2D;
import graphics.g2d.RenderizadorAvanzado2D;
import maths.Vec2;
import maths.Vec3;
import maths.Vec4;

public class Label extends Renderizable2D {

	public enum Alineamiento {
		NINGUNO, IZQUIERDA, CENTRO, DERECHA
	};

	// hacer el alineamiento respecto a la posicion que se quiera.
	private Alineamiento alineamiento;
	private String texto;
	private float ancho;
	private Vec2 limites;

	private final Fuente fuente;

	public Label(final String texto, final int x, final int y, final Fuente fuente, final Vec4 color) {
		this.texto = texto;
		this.posicion = new Vec3(x, y, 0);
		this.color = color;
		limites = new Vec2(0);

		this.fuente = fuente;
		for (int i = 0; i < texto.length(); i++)
			fuente.obtenerAtlas().obtenerLetra(texto.charAt(i));
		actualizarAncho();
		alineamiento = Alineamiento.NINGUNO;
	}

	public Label(final String texto, final int x, final int y, final Fuente fuente, final Vec4 color,
			final Alineamiento alineamiento, final float derecha) {
		this.texto = texto;
		this.posicion = new Vec3(x, y, 0);
		this.color = color;
		this.limites = new Vec2(posicion.x, derecha);

		this.fuente = fuente;
		for (int i = 0; i < texto.length(); i++)
			fuente.obtenerAtlas().obtenerLetra(texto.charAt(i));
		this.alineamiento = alineamiento;
		actualizarAncho();
	}

	public final void texto(final String texto) {
		this.texto = texto;
		actualizarAncho();
	}

	private final void actualizarAncho() {
		this.ancho = 0.0f;
		final FontAtlas atlas = fuente.obtenerAtlas();

		for (final char simbolo : texto.toCharArray()) {
			final Letra letra = atlas.obtenerLetra(simbolo);
			ancho += letra.ancho;
		}

		ancho /= RenderizadorAvanzado2D.escaladoX;

		actualizarPosicion();
	}

	private final void actualizarPosicion() {
		switch (alineamiento) {
		case NINGUNO:
		case IZQUIERDA:
			return;
		case CENTRO:
			posicion.x = ((limites.x + limites.y) / 2 - (ancho / 2));
			break;
		case DERECHA:
			posicion.x = limites.y - ancho;
			break;
		}
	}

	@Override
	public final void enviar(final Renderizador2D renderizador) {
		renderizador.dibujarTexto(this, fuente);
	}

	public final void color(final Vec4 color) {
		this.color = color;
	}

	public final float obtenerAncho() {
		return ancho;
	}

	public final String obtenerTexto() {
		return texto;
	}
}
