package graphics.postProcessingFX;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import graphics.postProcessingFX.bloom.Bloom;
import main.Aplicacion;

public final class PostProceso {

	private static final float[] posiciones = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static int vaoID;

	private static Bloom bloom;

	private PostProceso() {
	}

	public static final void iniciar() {
		iniciarVAO();

		bloom = new Bloom(Aplicacion.obtenerAncho(), Aplicacion.obtenerAlto());
	}

	private static final void iniciarVAO() {
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);

		final int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		final FloatBuffer buffer = BufferUtils.createFloatBuffer(posiciones.length);
		buffer.put(posiciones);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	public static final void renderizar(final int textura, final int texturaBrillo) {
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		bloom.render(textura, texturaBrillo);

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
