package graphics.g2d;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import graphics.g2d.fonts.FontAtlas;
import graphics.g2d.fonts.Fuente;
import graphics.g2d.fonts.Label;
import graphics.g2d.fonts.Letra;
import maths.Vec2;
import maths.Vec3;
import maths.Vec4;
import utils.PoolObjetos;

public class RenderizadorAvanzado2D extends Renderizador2D {

	private static final int MAX_SPRITES = 5499;
	private static final byte TAM_VERTICE = 40;
	private static final int TAM_SPRITE = TAM_VERTICE * 4;
	private static final int TAM_BUFFER = TAM_SPRITE * MAX_SPRITES;
	private static final int TAM_INDICES = MAX_SPRITES * 6;
	// Para qué sirve tener esto si no actualizamos los shaders?? :/
	private static final byte MAX_TEXTURAS = 32 + 1;

	private int VAO;
	private int VBO;
	private int IBO;

	private int verticesARenderizar = 0;
	private FloatBuffer buffer;
	private final ArrayList<Integer> texturas;

	// Texto *reusable
	public static final float escaladoX = 960.0f / 32f, escaladoY = 540.0f / 18f;

	public RenderizadorAvanzado2D() {
		iniciar();
		texturas = new ArrayList<>();
	}

	private void iniciar() {
		VAO = GL30.glGenVertexArrays();
		VBO = GL15.glGenBuffers();

		GL30.glBindVertexArray(VAO);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TAM_BUFFER, GL15.GL_DYNAMIC_DRAW);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, TAM_VERTICE, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, TAM_VERTICE, 12);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, true, TAM_VERTICE, 20);
		GL20.glVertexAttribPointer(3, 4, GL11.GL_FLOAT, false, TAM_VERTICE, 36);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		int[] indices = new int[TAM_INDICES];
		for (int i = 0, contador = 0; i < TAM_INDICES; i += 6, contador += 4) {
			indices[i] = contador + 0;
			indices[i + 1] = contador + 1;
			indices[i + 2] = contador + 2;
			indices[i + 3] = contador + 2;
			indices[i + 4] = contador + 3;
			indices[i + 5] = contador + 0;
		}

		IBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, IBO);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
		GL30.glBindVertexArray(0);
		indices = null;
		buffer = BufferUtils.createFloatBuffer(TAM_BUFFER / 4);
	}

	@Override
	public void comenzar() {
		verticesARenderizar = 0;
		buffer.clear();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
	}

	@Override
	public void acabar() {
		buffer.flip();
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void dibujarTexto(final Label label, final Fuente fuente) {
		final String texto = label.obtenerTexto();
		final FontAtlas atlas = fuente.obtenerAtlas();
		final int tid = atlas.obtenerTexturaID();
		float ts = 0;
		if (tid > 0) {
			boolean encontrado = false;
			for (int i = 0; i < texturas.size(); i++) {
				if (texturas.get(i) == tid) {
					encontrado = true;
					ts = i + 1;
					break;
				}
			}
			if (!encontrado) {
				if (texturas.size() >= MAX_TEXTURAS) {
					acabar();
					mostrar();
					comenzar();
					texturas.clear();
				}
				texturas.add(tid);
				ts = texturas.size();
			}
		}

		final Vec3 posicion = label.posicion;
		final Vec4 color = label.color;

		float x = posicion.x;
		final Vec3 vecAMul = PoolObjetos.solicitar(Vec3.class);

		final short longitud = (short) texto.length();
		final boolean perteneAGrupo = label.perteneceAGrupo;
		for (short i = 0; i < longitud; i++) {
			final char simbolo = texto.charAt(i);
			final Letra letra = atlas.obtenerLetra(simbolo);

			if (simbolo == ' ') {
				x += letra.ancho / escaladoX;
				continue;
			}

			final float x0 = x;
			final float y0 = posicion.y;
			final float posZ = posicion.z;
			final float x1 = x0 + letra.ancho / escaladoX;
			final float y1 = y0 - letra.alto / escaladoY;

			final float u0 = letra.u0;
			final float v0 = letra.v0;
			final float u1 = letra.u1;
			final float v1 = letra.v1;

			if (!perteneAGrupo) {
				vecAMul.xyz(x0, y0, posZ);
				buffer.put(vecAMul.x).put(vecAMul.y).put(vecAMul.z);
				buffer.put(u0).put(v0);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);

				vecAMul.xyz(x0, y1, posZ);
				buffer.put(vecAMul.x).put(vecAMul.y).put(vecAMul.z);
				buffer.put(u0).put(v1);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);

				vecAMul.xyz(x1, y1, posZ);
				buffer.put(vecAMul.x).put(vecAMul.y).put(vecAMul.z);
				buffer.put(u1).put(v1);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);

				vecAMul.xyz(x1, y0, posZ);
				buffer.put(vecAMul.x).put(vecAMul.y).put(vecAMul.z);
				buffer.put(u1).put(v0);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);
			} else {
				vecAMul.xyz(x0, y0, posZ);
				Vec3 posiciones = ultimaMatriz.multiplicar(vecAMul);
				buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
				buffer.put(u0).put(v0);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);

				vecAMul.xyz(x0, y1, posZ);
				posiciones = ultimaMatriz.multiplicar(vecAMul);
				buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
				buffer.put(u0).put(v1);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);

				vecAMul.xyz(x1, y1, posZ);
				posiciones = ultimaMatriz.multiplicar(vecAMul);
				buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
				buffer.put(u1).put(v1);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);

				vecAMul.xyz(x1, y0, posZ);
				posiciones = ultimaMatriz.multiplicar(vecAMul);
				buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
				buffer.put(u1).put(v0);
				buffer.put(color.x).put(color.y).put(color.z).put(color.w);
				buffer.put(ts);
			}

			x += letra.ancho / escaladoX;
			verticesARenderizar += 6;
		}
		PoolObjetos.liberar(vecAMul);
	}

	@Override
	public void enviar(final Renderizable2D renderizable) {
		final Vec3 posicion = renderizable.posicion;
		final Vec2 grandaria = renderizable.grandaria;
		final Vec4 color = renderizable.color;
		final Vec2[] uvs = renderizable.uvs;

		final int tid = renderizable.obtenerTID();
		float ts = 0;
		if (tid > 0) {
			boolean encontrado = false;
			final short longitud = (short) texturas.size();
			for (short i = 0; i < longitud; i++) {
				if (texturas.get(i) == tid) {
					encontrado = true;
					ts = i + 1;
					break;
				}
			}
			if (!encontrado) {
				if (texturas.size() >= MAX_TEXTURAS) {
					acabar();
					mostrar();
					comenzar();
					texturas.clear();
				}
				texturas.add(tid);
				ts = texturas.size();
			}
		}

		final Vec3 vectorReusable = PoolObjetos.solicitar(Vec3.class);

		if (!renderizable.perteneceAGrupo) {
			buffer.put(posicion.x).put(posicion.y).put(posicion.z);
			buffer.put(uvs[0].x).put(uvs[0].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);

			vectorReusable.x(posicion.x).y(posicion.y - grandaria.y).z(posicion.z);
			buffer.put(vectorReusable.x).put(vectorReusable.y).put(vectorReusable.z);
			buffer.put(uvs[1].x).put(uvs[1].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);

			vectorReusable.x(posicion.x + grandaria.x).y(posicion.y - grandaria.y).z(posicion.z);
			buffer.put(vectorReusable.x).put(vectorReusable.y).put(vectorReusable.z);
			buffer.put(uvs[2].x).put(uvs[2].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);

			vectorReusable.x(posicion.x + grandaria.x).y(posicion.y).z(posicion.z);
			buffer.put(vectorReusable.x).put(vectorReusable.y).put(vectorReusable.z);
			buffer.put(uvs[3].x).put(uvs[3].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);
		} else {
			Vec3 posiciones = ultimaMatriz.multiplicar(posicion);
			buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
			buffer.put(uvs[0].x).put(uvs[0].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);

			vectorReusable.xyz(posicion.x, posicion.y - grandaria.y, posicion.z);
			posiciones = ultimaMatriz.multiplicar(vectorReusable);
			buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
			buffer.put(uvs[1].x).put(uvs[1].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);

			vectorReusable.xyz(posicion.x + grandaria.x, posicion.y - grandaria.y, posicion.z);
			posiciones = ultimaMatriz.multiplicar(vectorReusable);
			buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
			buffer.put(uvs[2].x).put(uvs[2].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);

			vectorReusable.xyz(posicion.x + grandaria.x, posicion.y, posicion.z);
			posiciones = ultimaMatriz.multiplicar(vectorReusable);
			buffer.put(posiciones.x).put(posiciones.y).put(posiciones.z);
			buffer.put(uvs[3].x).put(uvs[3].y);
			buffer.put(color.x).put(color.y).put(color.z).put(color.w);
			buffer.put(ts);
		}

		PoolObjetos.liberar(vectorReusable);
		verticesARenderizar += 6;
	}

	@Override
	public void mostrar() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		final short grandaria = (short) texturas.size();
		for (byte i = 0; i < grandaria; i++) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturas.get(i));
		}

		GL30.glBindVertexArray(VAO);
		GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, verticesARenderizar, verticesARenderizar, GL11.GL_UNSIGNED_INT,
				0);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
