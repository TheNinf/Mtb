package graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

public class Textura {

	public enum TIPO {
		TEXTURA_3D, TEXTURA_SPRITE
	}

	private int ancho, alto;
	private int id;

	public Textura(final int texturaID) {
		id = texturaID;
	}

	public Textura(final String ruta, final TIPO tipo) {
		id = leerTextura(ruta, tipo);
	}

	public Textura(final BufferedImage imagen, final TIPO tipo) {
		id = leerTextura(imagen, tipo);
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
		// GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ancho, alto,
		// 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
		// data);
		final IntBuffer buffer = BufferUtils.createIntBuffer(grandaria);

		buffer.rewind();
		buffer.put(imagen.getRGB(0, 0, ancho, alto, null, 0, ancho), 0, grandaria);
		buffer.rewind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ancho, alto, 0, GL12.GL_BGRA,
				GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
		desenlazar();

		return texturaID;
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

		buffer.rewind();
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public final void desenlazar() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
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
