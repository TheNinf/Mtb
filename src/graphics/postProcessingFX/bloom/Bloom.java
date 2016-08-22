package graphics.postProcessingFX.bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import graphics.Shader;
import graphics.postProcessingFX.gaussianBlur.GaussianBlur5;

public final class Bloom {

	private static final float ESCALADO1 = 5;
	private static final float ESCALADO2 = 10;
	private static final float ESCALADO3 = 20;
	private static final float ESCALADO4 = 40;

	private final Shader bloom;
	private final GaussianBlur5 gaussianBlur1;
	private final GaussianBlur5 gaussianBlur2;
	private final GaussianBlur5 gaussianBlur3;
	private final GaussianBlur5 gaussianBlur4;

	public Bloom(final int ancho, final int alto) {
		bloom = new Shader("src/graphics/postProcessingFX/main.vert", "src/graphics/postProcessingFX/bloom/bloom.frag");
		gaussianBlur1 = new GaussianBlur5((int) (ancho / ESCALADO1), (int) (alto / ESCALADO1));
		gaussianBlur2 = new GaussianBlur5((int) (ancho / ESCALADO2), (int) (alto / ESCALADO2));
		gaussianBlur3 = new GaussianBlur5((int) (ancho / ESCALADO3), (int) (alto / ESCALADO3));
		gaussianBlur4 = new GaussianBlur5((int) (ancho / ESCALADO4), (int) (alto / ESCALADO4));

		bloom.enlazar();
		bloom.uniformInt("textura", 0);
		bloom.uniformInt("texturaBrillo1", 1);
		bloom.uniformInt("texturaBrillo2", 2);
		bloom.uniformInt("texturaBrillo3", 3);
		bloom.uniformInt("texturaBrillo4", 4);
		bloom.desenlazar();
	}

	public final void render(final int textura, final int texturaBrillo) {
		gaussianBlur1.aplicarGaussianBlur(texturaBrillo);
		gaussianBlur2.aplicarGaussianBlur(gaussianBlur1.obtenerTextura());
		gaussianBlur3.aplicarGaussianBlur(gaussianBlur2.obtenerTextura());
		gaussianBlur4.aplicarGaussianBlur(gaussianBlur3.obtenerTextura());

		bloom.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textura);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gaussianBlur1.obtenerTextura());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gaussianBlur2.obtenerTextura());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gaussianBlur3.obtenerTextura());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gaussianBlur4.obtenerTextura());

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		bloom.desenlazar();
	}

}
