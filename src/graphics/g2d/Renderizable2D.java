package graphics.g2d;

import graphics.Textura;
import maths.Vec2;
import maths.Vec3;
import maths.Vec4;

public abstract class Renderizable2D {
	protected Vec3 posicion;
	protected Vec2 grandaria;
	protected Vec4 color;

	protected Vec2[] uvs;
	protected Textura textura;

	public boolean perteneceAGrupo = false;

	protected Renderizable2D() {
		posicion = null;
		grandaria = null;
		color = null;
		uvs = null;
		textura = null;
	}

	public Renderizable2D(final float x, final float y, final float ancho, final float alto, final Vec4 color) {
		this.posicion = new Vec3(x, y, 0);
		this.grandaria = new Vec2(ancho, alto);
		this.color = color;
		uvs = new Vec2[4];

		uvsPorDefecto();
	}

	public Renderizable2D(final float x, final float y, final float ancho, final float alto, final Textura textura) {
		this.posicion = new Vec3(x, y, 0);
		this.grandaria = new Vec2(ancho, alto);
		this.textura = textura;
		this.color = new Vec4(1);
		uvs = new Vec2[4];

		uvsPorDefecto();
	}

	public Renderizable2D(final float x, final float y, final float ancho, final float alto, final int textura) {
		this.posicion = new Vec3(x, y, 0);
		this.grandaria = new Vec2(ancho, alto);
		this.textura = new Textura(textura);
		this.color = new Vec4(1);
		uvs = new Vec2[4];

		 uvs[0] = new Vec2(0, 1);
		 uvs[1] = new Vec2(0);
		 uvs[2] = new Vec2(1, 0);
		 uvs[3] = new Vec2(1);
	}

	private final void uvsPorDefecto() {
		uvs[0] = new Vec2(0);
		uvs[1] = new Vec2(0, 1);
		uvs[2] = new Vec2(1);
		uvs[3] = new Vec2(1, 0);
	}

	public void enviar(final Renderizador2D renderizador) {
		renderizador.enviar(this);
	}

	public final int obtenerTID() {
		return textura == null ? 0 : textura.obtenerID();
	}

	public final Vec2[] obtenerUVS() {
		return uvs;
	}

	public final Vec2 obtenerGrandaria() {
		return grandaria;
	}

	public final Vec4 obtenerColor() {
		return color;
	}
}
