package graphics.postProcessingFX.lensFlare;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import graphics.Shader;
import graphics.Textura;
import graphics.graphics2D.framebuffer.Framebuffer;
import graphics.postProcessingFX.PostProceso;

public class LensFlare extends PostProceso {

	private final Shader shader;
	private final Shader vblurShader;
	private final Shader hblurShader;

	private final Textura texturaDirt;
	private final Textura lensColor;

	public LensFlare(int ancho, int alto) {
		super(ancho / 2, alto / 2);
		shader = new Shader("src/graphics/postProcessingFX/grises/gris.vert",
				"src/graphics/postProcessingFX/lensFlare/lensFlare.frag");
		vblurShader = new Shader("src/graphics/postProcessingFX/lensFlare/verticalBlurVertex.txt",
				"src/graphics/postProcessingFX/lensFlare/blurFragment.txt");
		hblurShader = new Shader("src/graphics/postProcessingFX/lensFlare/horizontalBlurVertex.txt",
				"src/graphics/postProcessingFX/lensFlare/blurFragment.txt");
		texturaDirt = new Textura("src/lensDirt.png", Textura.TIPO.TEXTURA_SPRITE);
		lensColor = new Textura("src/lenscolor.png", Textura.TIPO.TEXTURA_SPRITE);

		shader.enlazar();
		shader.uniformInt("samplerTexture", 0);
		shader.uniformInt("lensColor", 1);
		shader.uniformInt("normalImage", 2);
		shader.uniformInt("lensDirtImage", 3);
		shader.uniformInt("lensStar", 4);
		shader.desenlazar();

		hblurShader.enlazar();
		hblurShader.uniformFloat("targetWidth", ancho / 2);
		hblurShader.desenlazar();

		vblurShader.enlazar();
		vblurShader.uniformFloat("targetHeight", alto / 2);
		vblurShader.desenlazar();
	}

	@Override
	public void render(Framebuffer buffer) {
		// BLUsR
		framebuffer.enlazar();
		hblurShader.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT1));
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		hblurShader.desenlazar();

		vblurShader.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT0));
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		vblurShader.desenlazar();
		framebuffer.desenlazar();

		// LENS FLARE
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT0));
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, lensColor.obtenerID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT0));
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturaDirt.obtenerID());

		shader.enlazar();
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		shader.desenlazar();

	}
}
