package graphics.postProcessingFX.gaussianBlur;

import graphics.Shader;

public class GaussianBlur5 extends GaussianBlur {

	public GaussianBlur5(int ancho, int alto) {
		super(ancho, alto,
				new Shader("src/graphics/postProcessingFX/gaussianBlur/verticalBlurVertex5.vert",
						"src/graphics/postProcessingFX/gaussianBlur/blurFragment5.frag"),
				new Shader("src/graphics/postProcessingFX/gaussianBlur/horizontalBlurVertex5.vert",
						"src/graphics/postProcessingFX/gaussianBlur/blurFragment5.frag"));
	}

}
