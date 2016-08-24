package graphics.graphics2D;

import graphics.Textura;
import maths.Vector2;
import maths.Vector3;
import maths.Vector4;

public abstract class Renderizable2D {
	public Vector3 posicion;
	public Vector2 grandaria;
	public Vector4 color;

	protected Textura textura;

	public boolean perteneceAGrupo = false;

	protected Renderizable2D() {
		posicion = null;
		grandaria = null;
		color = null;
		textura = null;
	}

	public Renderizable2D(final float x, final float y, final float ancho, final float alto, final Vector4 color) {
		this.posicion = new Vector3(x, y, 0);
		this.grandaria = new Vector2(ancho, alto);
		this.color = color;
	}

	public Renderizable2D(final float x, final float y, final float ancho, final float alto, final Textura textura) {
		this.posicion = new Vector3(x, y, 0);
		this.grandaria = new Vector2(ancho, alto);
		this.textura = textura;
		this.color = new Vector4(1, 1, 1, 1);
	}

	public Renderizable2D(final float x, final float y, final float ancho, final float alto, final int textura) {
		this.posicion = new Vector3(x, y, 0);
		this.grandaria = new Vector2(ancho, alto);
		this.textura = new Textura(textura);
		this.color = new Vector4(1, 1, 1, 1);
	}

	public void enviar(final Renderizador2D renderizador) {
		renderizador.enviar(this);
	}

	public final int obtenerTID() {
		return textura == null ? 0 : textura.obtenerID();
	}
}
