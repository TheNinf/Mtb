package graphics.postProcessingFX.godRays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import entity.RenderizadorEntidades;
import graphics.Shader;
import graphics.framebuffer.Framebuffer;
import maths.Matrix4;
import maths.Vector2;
import maths.Vector3;
import maths.Vector4;

public final class GodRays {

	private final Shader shader;
	private final Shader nShader;

	private final Framebuffer framebuffer;

	public GodRays(final int ancho, final int alto) {
		shader = new Shader("src/graphics/postProcessingFX/main.vert",
				"src/graphics/postProcessingFX/godRays/godRays.frag");
		nShader = new Shader("src/graphics/postProcessingFX/main.vert", "src/graphics/postProcessingFX/main.frag");
		framebuffer = new Framebuffer(ancho, alto, Framebuffer.TIPO.SOLO_TEXTURAS, 1);

		shader.enlazar();
		shader.uniformInt("textura", 0);
		shader.desenlazar();

		nShader.enlazar();
		nShader.uniformInt("textura", 0);
		nShader.desenlazar();
	}

	public final void render(final int textura, final int godRaysTextura) {
		framebuffer.enlazar();
		nShader.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textura);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		nShader.desenlazar();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		shader.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		shader.uniformVector2("lightPosition", obtenerPos(new Vector3(10000, 0, 1000000)));
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, godRaysTextura);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		shader.desenlazar();
		GL11.glDisable(GL11.GL_BLEND);
		framebuffer.desenlazar();
	}

	private final Vector2 obtenerPos(final Vector3 posicion) {
		final Vector4 point = new Vector4(posicion.x, posicion.y, posicion.z, 1);
		Matrix4.multiplicar(
				Matrix4.multiplicar(RenderizadorEntidades.matrizPerspectiva, RenderizadorEntidades.viewMatrix, null),
				point, point);

		point.x /= point.w;
		point.y /= -point.w;

		return new Vector2(point.x, point.y);
	}

	public final int obtenerTextura() {
		return framebuffer.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT0);
	}
}
