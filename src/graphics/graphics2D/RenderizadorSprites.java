package graphics.graphics2D;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import maths.Vector2;
import maths.Vector3;
import maths.Vector4;

public class RenderizadorSprites extends Renderizador2D {

	private static final int MAX_SPRITES = 60000;
	private static final byte TAM_SPRITE = 40;
	private static final int TAM_BUFFER = TAM_SPRITE * MAX_SPRITES;

	private static final byte MAX_TEXTURAS = 32;

	private final int VAO;
	private final int VBO;

	private final FloatBuffer buffer;
	private int spritesARenderizar = 0;

	private final int[] texturas;
	private int tamTexturas = 0;

	public RenderizadorSprites() {
		VAO = GL30.glGenVertexArrays();
		VBO = GL15.glGenBuffers();

		GL30.glBindVertexArray(VAO);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);

		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, TAM_BUFFER, GL15.GL_DYNAMIC_DRAW);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, TAM_SPRITE, 0);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, TAM_SPRITE, 12);
		GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, true, TAM_SPRITE, 20);
		GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, TAM_SPRITE, 36);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);

		buffer = BufferUtils.createFloatBuffer(TAM_BUFFER / 4);
		texturas = new int[MAX_TEXTURAS];
	}

	@Override
	public void comenzar() {
		spritesARenderizar = 0;
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
	}

	@Override
	public void acabar() {
		buffer.flip();
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void enviar(Renderizable2D renderizable) {
		final Vector3 posicion = renderizable.posicion;
		final Vector2 grandaria = renderizable.grandaria;
		final Vector4 color = renderizable.color;
		final int tid = renderizable.obtenerTID();

		float ts = 0;
		if (tid > 0) {
			boolean encontrado = false;
			for (byte i = 0; i < MAX_TEXTURAS; i++) {
				if (texturas[i] == tid) {
					encontrado = true;
					ts = i + 1;
					break;
				}
			}
			if (!encontrado) {
				if (tamTexturas >= MAX_TEXTURAS + 1) {
					acabar();
					mostrar();
					comenzar();
					for (byte i = 0; i < MAX_TEXTURAS; i++)
						texturas[i] = 0;
					tamTexturas = 0;
				}
				texturas[tamTexturas++] = tid;
				ts = tamTexturas;
			}
		}

		buffer.put(posicion.x).put(posicion.y).put(posicion.z);
		buffer.put(grandaria.x).put(grandaria.y);
		buffer.put(color.x).put(color.y).put(color.z).put(color.w);
		buffer.put(ts);
		spritesARenderizar++;
	}

	@Override
	public void mostrar() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		for (byte i = 0; i < MAX_TEXTURAS; i++) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturas[i]);
		}

		GL30.glBindVertexArray(VAO);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, spritesARenderizar);

		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
