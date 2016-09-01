package graphics.graphics3D.water;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TileAgua {

	private int vao, contador;

	public TileAgua(final int numeroVertices, final float grandaria, final float x, final float z) {
		generarTile(numeroVertices, grandaria, x, z);
	}

	private final void generarTile(final int numeroVertices, final float grandaria, final float x, final float z) {

		final int count = numeroVertices * numeroVertices;
		final float[] vertices = new float[count * 2];
		final float[] textureCoords = new float[count * 2];
		final int[] indices = new int[6 * (numeroVertices - 1) * (numeroVertices - 1)];

		int cont = 0;
		for (int i = 0; i < numeroVertices; i++) {
			for (int j = 0; j < numeroVertices; j++) {
				vertices[cont * 2] = x + j / ((float) numeroVertices - 1) * grandaria;
				vertices[cont * 2 + 1] = z + i / ((float) numeroVertices - 1) * grandaria;

				textureCoords[cont * 2] = (j / ((float) numeroVertices - 1)) * 6;
				textureCoords[cont * 2 + 1] = (i / ((float) numeroVertices - 1)) * 6;
				cont++;
			}
		}

		cont = 0;
		for (int gz = 0; gz < numeroVertices - 1; gz++) {
			for (int gx = 0; gx < numeroVertices - 1; gx++) {
				final int arribaIzquierda = (gz * numeroVertices) + gx;
				final int arribaDerecha = arribaIzquierda + 1;
				final int abajoIzquierda = ((gz + 1) * numeroVertices) + gx;
				final int abajoDerecha = abajoIzquierda + 1;
				indices[cont++] = arribaIzquierda;
				indices[cont++] = abajoIzquierda;
				indices[cont++] = arribaDerecha;
				indices[cont++] = arribaDerecha;
				indices[cont++] = abajoIzquierda;
				indices[cont++] = abajoDerecha;
			}
		}

		this.contador = indices.length;

		vao = GL30.glGenVertexArrays();
		final int vbo = GL15.glGenBuffers();
		final int vboTextura = GL15.glGenBuffers();
		final int ibo = GL15.glGenBuffers();

		GL30.glBindVertexArray(vao);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		final FloatBuffer bufferVertices = BufferUtils.createFloatBuffer(vertices.length);
		bufferVertices.put(vertices);
		bufferVertices.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferVertices, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTextura);
		final FloatBuffer bufferTexturas = BufferUtils.createFloatBuffer(textureCoords.length);
		bufferTexturas.put(textureCoords);
		bufferTexturas.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferTexturas, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		final IntBuffer bufferIndices = BufferUtils.createIntBuffer(indices.length);
		bufferIndices.put(indices);
		bufferIndices.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, bufferIndices, GL15.GL_STATIC_DRAW);
		GL30.glBindVertexArray(0);
	}

	public final int obtenerVAO() {
		return vao;
	}

	public final int obtenerNumeroIndices() {
		return contador;
	}
}
