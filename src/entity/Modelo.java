package entity;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

import graphics.Textura;
import maths.Matrix4;

public class Modelo {

	private static final short MAX_ENTIDADES = 1100;
	private static final byte NUM_FLOATS = 16;
	private static final int TAM_ENTIDAD = NUM_FLOATS * 4;
	private int contador;

	private final int vao, vboTransform;
	private final FloatBuffer buffer;

	private final int numeroIndices;

	private Textura textura;
	private Textura reflejo;
	private Textura normalMap;
	private float brillo;

	public Modelo(final float[] posiciones, final float[] normals, final float[] textureCoords, final float[] tangents,
			final float[] bitang, final int[] indices, final Textura textura) {

		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		// POSICIONES
		final int vboPosiciones = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboPosiciones);
		final FloatBuffer bufferPosiciones = BufferUtils.createFloatBuffer(posiciones.length);
		bufferPosiciones.put(posiciones);
		bufferPosiciones.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferPosiciones, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// NORMALS
		final int vboNormals = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboNormals);
		final FloatBuffer bufferNormals = BufferUtils.createFloatBuffer(normals.length);
		bufferNormals.put(normals);
		bufferNormals.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferNormals, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// TEXTURE COORDS
		final int vboTexture = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTexture);
		final FloatBuffer bufferTexture = BufferUtils.createFloatBuffer(textureCoords.length);
		bufferTexture.put(textureCoords);
		bufferTexture.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferTexture, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// TANGENTS
		final int vboTang = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTang);
		final FloatBuffer bufferTang = BufferUtils.createFloatBuffer(tangents.length);
		bufferTang.put(tangents);
		bufferTang.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferTang, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// BITANGENTS
		final int vboBitang = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboBitang);
		final FloatBuffer bufferBitang = BufferUtils.createFloatBuffer(bitang.length);
		bufferBitang.put(bitang);
		bufferBitang.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferBitang, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(4, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// INDICES
		final int vboIndices = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndices);
		final IntBuffer bufferIndices = BufferUtils.createIntBuffer(indices.length);
		bufferIndices.put(indices);
		bufferIndices.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, bufferIndices, GL15.GL_STATIC_DRAW);

		GL20.glEnableVertexAttribArray(8);
		vboTransform = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTransform);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TAM_ENTIDAD * MAX_ENTIDADES, GL15.GL_DYNAMIC_DRAW);
		buffer = BufferUtils.createFloatBuffer(NUM_FLOATS * MAX_ENTIDADES);
		GL20.glVertexAttribPointer(5, 4, GL11.GL_FLOAT, false, TAM_ENTIDAD, 0);
		GL20.glVertexAttribPointer(6, 4, GL11.GL_FLOAT, false, TAM_ENTIDAD, 16);
		GL20.glVertexAttribPointer(7, 4, GL11.GL_FLOAT, false, TAM_ENTIDAD, 32);
		GL20.glVertexAttribPointer(8, 4, GL11.GL_FLOAT, false, TAM_ENTIDAD, 48);
		GL33.glVertexAttribDivisor(5, 1);
		GL33.glVertexAttribDivisor(6, 1);
		GL33.glVertexAttribDivisor(7, 1);
		GL33.glVertexAttribDivisor(8, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		GL20.glEnableVertexAttribArray(7);
		GL30.glBindVertexArray(0);

		this.textura = textura;
		this.numeroIndices = indices.length;
		brillo = 0;
	}

	public final void render() {
		GL30.glBindVertexArray(vao);
		GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, numeroIndices, GL11.GL_UNSIGNED_INT, 0, contador);
		GL30.glBindVertexArray(0);
	}

	public final void comenzar() {
		contador = 0;
		buffer.clear();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTransform);
	}

	public final void agregarTransformation(final Matrix4 transformationMatrix) {
		buffer.put(transformationMatrix.elementos);
		contador++;
	}

	public final void terminar() {
		buffer.flip();
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public final Modelo ponerBrillo(final float brillo) {
		this.brillo = brillo;
		return this;
	}

	public final Modelo ponerTexturaReflejo(final Textura reflejo) {
		this.reflejo = reflejo;
		return this;
	}

	public final Modelo ponerNormalMap(final Textura normalMap) {
		this.normalMap = normalMap;
		return this;
	}

	public final float obtenerBrillo() {
		return brillo;
	}

	public final int obtenerTexturaID() {
		return textura.obtenerID();
	}

	public final int obtenerTexturaReflejoID() {
		return reflejo == null ? 0 : reflejo.obtenerID();
	}

	public final int obtenerNormalMap() {
		return normalMap == null ? 0 : normalMap.obtenerID();
	}

	public final int obtenerNumeroIndices() {
		return numeroIndices;
	}

	public final int obtenerVAO() {
		return vao;
	}

}
