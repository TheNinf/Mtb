package graphics.g2d.fonts;

public class Letra {

	public final float ancho, alto, u0, u1, v0, v1;
	public final char simbolo;

	public Letra(final char simbolo, final float ancho, final float alto, final float u0, final float u1,
			final float v0, final float v1) {

		this.alto = alto;
		this.ancho = ancho;
		this.u0 = u0;
		this.u1 = u1;
		this.v0 = v0;
		this.v1 = v1;
		this.simbolo = simbolo;
	}
}
