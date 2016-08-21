package graphics.postProcessingFX.gaussianBlur;

import graphics.Shader;

public final class GaussianBlur11 extends GaussianBlur {

	public GaussianBlur11(final int ancho, final int alto) {
		super(ancho, alto,
				new Shader("src/graphics/postProcessingFX/gaussianBlur/verticalBlurVertex11.vert",
						"src/graphics/postProcessingFX/gaussianBlur/blurFragment11.frag"),
				new Shader("src/graphics/postProcessingFX/gaussianBlur/horizontalBlurVertex11.vert",
						"src/graphics/postProcessingFX/gaussianBlur/blurFragment11.frag"));
	}
}
