package graphics.postProcessingFX.lensFlare;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import graphics.Shader;
import graphics.Textura;
import graphics.postProcessingFX.gaussianBlur.GaussianBlur11;

public class LensFlare {

	private final Shader lensFlareShader;

	private final Textura texturaDirt;
	private final Textura lensColor;

	private final GaussianBlur11 gaussianBlur;

	public LensFlare(int ancho, int alto) {
		gaussianBlur = new GaussianBlur11(ancho / 2, alto / 2);

		lensFlareShader = new Shader("src/graphics/postProcessingFX/main.vert",
				"src/graphics/postProcessingFX/lensFlare/lensFlare.frag");
		texturaDirt = new Textura("src/lensDirt.png", Textura.TIPO.TEXTURA_SPRITE);
		lensColor = new Textura("src/lenscolor.png", Textura.TIPO.TEXTURA_SPRITE);

		lensFlareShader.enlazar();
		lensFlareShader.uniformInt("samplerTexture", 0);
		lensFlareShader.uniformInt("lensColor", 1);
		lensFlareShader.uniformInt("normalImage", 2);
		lensFlareShader.uniformInt("lensDirtImage", 3);
		lensFlareShader.uniformInt("lensStar", 4);
		lensFlareShader.desenlazar();
	}

	public void render(final int textura, final int texturaBrillo) {
		// BLUR
		gaussianBlur.aplicarGaussianBlur(texturaBrillo);

		// LENS FLARE
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gaussianBlur.obtenerTextura());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, lensColor.obtenerID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textura);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturaDirt.obtenerID());

		lensFlareShader.enlazar();
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		lensFlareShader.desenlazar();
	}
}
