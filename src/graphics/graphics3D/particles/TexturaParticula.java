package graphics.graphics3D.particles;

import graphics.Textura;

public final class TexturaParticula extends Textura {

	private final int numeroColumnas;
	private final boolean additiveBlending;

	public TexturaParticula(final String ruta, final int numeroColumnas, final boolean additiveBlending) {
		super(ruta, TIPO.TEXTURA_3D);
		this.numeroColumnas = numeroColumnas;
		this.additiveBlending = additiveBlending;
	}

	public final int obtenerNumeroColumnas() {
		return numeroColumnas;
	}

	public final boolean usaAdditiveBlending() {
		return additiveBlending;
	}
}
