package graphics.postProcessingFX.grises;

import org.lwjgl.opengl.GL11;

import graphics.Shader;
import graphics.graphics2D.framebuffer.Framebuffer;
import graphics.postProcessingFX.PostProceso;

public class EscalaGrises extends PostProceso {

	private final Shader shader;

	public EscalaGrises(int ancho, int alto) {
		super(ancho, alto);
		shader = new Shader("src/graphics/postProcesos/grises/gris.vert", "src/graphics/postProcesos/grises/gris.frag");
	}

	@Override
	public void render(Framebuffer buffer) {
		shader.enlazar();

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

		shader.desenlazar();
	}

}
