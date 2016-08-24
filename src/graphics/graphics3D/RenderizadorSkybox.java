package graphics.graphics3D;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import graphics.Shader;
import graphics.Textura;
import graphics.Transform;

public class RenderizadorSkybox {

	private static final float SIZE = 500;

	private final int vaoID;
	private final short numVertices;

	private final Shader shader;
	private final Textura textura;

	public RenderizadorSkybox() {
		vaoID = GL30.glGenVertexArrays();
		final int vboID = GL15.glGenBuffers();
		GL30.glBindVertexArray(vaoID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE,
				SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

				-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE,
				-SIZE, -SIZE, SIZE,

				SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE,
				-SIZE, -SIZE,

				-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
				-SIZE, SIZE,

				-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE,
				SIZE, -SIZE,

				-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE,
				SIZE, -SIZE, SIZE };
		numVertices = (short) (VERTICES.length / 3);

		final FloatBuffer buffer = BufferUtils.createFloatBuffer(VERTICES.length);
		buffer.put(VERTICES);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);

		final String[] TEXTURE_FILES = { "src/right.png", "src/left.png", "src/top.png", "src/bottom.png",
				"src/back.png", "src/front.png" };
		textura = new Textura(TEXTURE_FILES);
		shader = new Shader("src/graphics/graphics3D/skybox.vert", "src/graphics/graphics3D/skybox.frag");
		shader.enlazar();
		shader.uniformMatrix4("projectionMatrix", Transform.obtenerMatrizProyeccion());
		shader.desenlazar();
	}

	public final void render() {
		shader.enlazar();
		shader.uniformMatrix4("viewMatrix", Transform.obtenerSkyboxViewMatrix());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		textura.enlazar();

		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, numVertices);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.desenlazar();
	}

}
