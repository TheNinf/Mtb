package graphics.graphics2D;

import graphics.Textura;
import maths.Vector4;

public class Sprite extends Renderizable2D {

	public Sprite(final float x, final float y, final float ancho, final float alto, final Vector4 color) {
		super(x, y, ancho, alto, color);
	}

	public Sprite(float x, float y, float ancho, float alto, final Textura textura) {
		super(x, y, ancho, alto, textura);
	}

	public Sprite(float x, float y, float ancho, float alto, final int textura) {
		super(x, y, ancho, alto, textura);
	}
}
