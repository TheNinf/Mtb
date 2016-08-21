package graphics.postProcessingFX.gaussianBlur;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import graphics.Shader;
import graphics.framebuffer.Framebuffer;

public abstract class GaussianBlur {

	private final Shader vBlurShader;
	private final Shader hBlurShader;

	private final Framebuffer framebuffer;

	protected GaussianBlur(final int ancho, final int alto, final Shader vShader, final Shader hShader) {
		framebuffer = new Framebuffer(ancho, alto, Framebuffer.TIPO.SOLO_TEXTURAS, 1);

		this.vBlurShader = vShader;
		this.hBlurShader = hShader;

		hBlurShader.enlazar();
		hBlurShader.uniformFloat("targetWidth", ancho);
		hBlurShader.desenlazar();

		vBlurShader.enlazar();
		vBlurShader.uniformFloat("targetHeight", alto);
		vBlurShader.desenlazar();
	}

	public void aplicarGaussianBlur(final int textura) {
		framebuffer.enlazar();
		hBlurShader.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textura);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		hBlurShader.desenlazar();

		vBlurShader.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, obtenerTextura());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		vBlurShader.desenlazar();
		framebuffer.desenlazar();
	}

	public final int obtenerTextura() {
		return framebuffer.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT0);
	}

}
