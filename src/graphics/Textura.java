package graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

public class Textura {

	public enum TIPO {
		TEXTURA_3D, TEXTURA_SPRITE
	}

	private int ancho, alto;
	private final int id;

	private final int tipoTextura;

	public Textura(final int id) {
		this.id = id;
		tipoTextura = GL11.GL_TEXTURE_2D;
	}

	public Textura(final String ruta, final TIPO tipo) {
		id = leerTextura(ruta, tipo);
		tipoTextura = GL11.GL_TEXTURE_2D;
	}

	public Textura(final BufferedImage imagen, final TIPO tipo) {
		id = leerTextura(imagen, tipo);
		tipoTextura = GL11.GL_TEXTURE_2D;
	}

	/**
	 * Crea un cubemap.
	 * 
	 * @param rutas
	 */
	public Textura(final String[] rutas) {
		id = leerCubeMap(rutas);
		tipoTextura = GL13.GL_TEXTURE_CUBE_MAP;
	}

	private final int leerTextura(final BufferedImage imagen, final TIPO tipo) {
		ancho = imagen.getWidth();
		alto = imagen.getHeight();

		int texturaID = GL11.glGenTextures();

		final int grandaria = ancho * alto;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturaID);

		if (tipo == TIPO.TEXTURA_3D) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		final IntBuffer buffer = BufferUtils.createIntBuffer(grandaria);

		buffer.put(imagen.getRGB(0, 0, ancho, alto, null, 0, ancho), 0, grandaria);
		buffer.rewind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ancho, alto, 0, GL12.GL_BGRA,
				GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
		desenlazar();

		return texturaID;
	}

	private final int leerCubeMap(final String[] rutas) {
		if (rutas.length != 6)
			return 0;

		final int id = GL11.glGenTextures();
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, id);
		try {
			for (byte i = 0; i < 6; i++) {
				final BufferedImage imagen = ImageIO.read(new FileInputStream(rutas[i]));

				final int ancho = imagen.getWidth(), alto = imagen.getHeight();
				final IntBuffer buffer = BufferUtils.createIntBuffer(ancho * alto);
				buffer.put(imagen.getRGB(0, 0, ancho, alto, null, 0, ancho));
				buffer.rewind();

				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, ancho, alto, 0,
						GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
			}
		} catch (final Exception e) {
		}

		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
		return id;
	}

	private int leerTextura(final String ruta, final TIPO tipo) {
		BufferedImage imagen = null;
		try {
			imagen = ImageIO.read(new FileInputStream(ruta));
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return leerTextura(imagen, tipo);
	}

	public final void actualizarTextura(final BufferedImage imagen) {
		ancho = imagen.getWidth();
		alto = imagen.getHeight();

		final int grandaria = ancho * alto;
		final IntBuffer buffer = BufferUtils.createIntBuffer(grandaria);

		buffer.put(imagen.getRGB(0, 0, ancho, alto, null, 0, ancho), 0, grandaria);
		buffer.rewind();
		enlazar();
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, ancho, alto, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
				buffer);
		desenlazar();
	}

	@Override
	protected final void finalize() {
		GL11.glDeleteTextures(id);
	}

	public final void enlazar() {
		GL11.glBindTexture(tipoTextura, id);
	}

	public final void desenlazar() {
		GL11.glBindTexture(tipoTextura, 0);
	}

	public final int obtenerID() {
		return id;
	}

	public final int obtenerAncho() {
		return ancho;
	}

	public final int obtenerAlto() {
		return alto;
	}

}
