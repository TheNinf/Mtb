package graphics.postProcessingFX;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import graphics.framebuffer.Framebuffer;

public abstract class PostProceso {

	private static final float[] posiciones = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static final ArrayList<PostProceso> postProcesos = new ArrayList<>();
	private static int vaoID;

	protected final int ancho, alto;
	protected Framebuffer framebuffer;

	public PostProceso(final int ancho, final int alto) {
		this.ancho = ancho;
		this.alto = alto;
		framebuffer = new Framebuffer(ancho, alto, Framebuffer.Tipo.SOLO_TEXTURAS);
	}

	public static final void iniciar() {
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);

		// final int vboID = GL15.glGenBuffers();

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

	public static final void renderizar(Framebuffer buffer) {
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		final byte grandaria = (byte) postProcesos.size();
		for (byte i = 0; i < grandaria; i++) {
			final PostProceso postProceso = postProcesos.get(i);
			if (postProceso.ancho != 0 && postProceso.alto != 0 && grandaria > 1) {
				postProceso.framebuffer.enlazar();

				postProceso.render(buffer);
				postProceso.framebuffer.desenlazar();
			} else {
				postProceso.render(buffer);
			}

		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public abstract void render(Framebuffer buffer);

	public static final void agregarPostProceso(final PostProceso postProceso) {
		postProcesos.add(postProceso);
	}

	public final int obtenerTextura(int attachment) {
		return framebuffer.obtenerAttachment(attachment);
	}

}
