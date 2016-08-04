package graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

public class Textura {

	private int ancho, alto;
	private int id;

	public Textura(final int texturaID) {
		id = texturaID;
	}

	public Textura(final String ruta) {
		id = leerTextura(ruta);
	}

	public Textura(final String ruta, boolean d1) {
		id = !d1 ? leerTextura(ruta) : leerTextura1D(ruta);
	}

	public Textura(final BufferedImage imagen) {
		id = leerTextura(imagen);
	}

	private int leerTextura(final BufferedImage imagen) {
		ancho = imagen.getWidth();
		alto = imagen.getHeight();

		final int[] pixeles = new int[ancho * alto];
		imagen.getRGB(0, 0, ancho, alto, pixeles, 0, ancho);

		int[] data = new int[pixeles.length];
		for (int i = 0; i < pixeles.length; i++) {
			int color = pixeles[i];
			int alpha = (color & 0xff000000) >> 24;
			int rojo = (color & 0xff0000) >> 16;
			int verde = (color & 0xff00) >> 8;
			int azul = (color & 0xff);

			data[i] = alpha << 24 | azul << 16 | verde << 8 | rojo;
		}

		int texturaID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturaID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ancho, alto, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				data);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		data = null;

		return texturaID;
	}

	private int leerTextura(final String ruta) {
		int[] pixeles = null;
		try {
			BufferedImage imagen = ImageIO.read(new FileInputStream(ruta));
			ancho = imagen.getWidth();
			alto = imagen.getHeight();
			pixeles = new int[ancho * alto];
			imagen.getRGB(0, 0, ancho, alto, pixeles, 0, ancho);
			imagen = null;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		int[] data = new int[ancho * alto];
		for (int i = 0; i < pixeles.length; i++) {
			int color = pixeles[i];
			int alpha = (color & 0xff000000) >> 24;
			int rojo = (color & 0xff0000) >> 16;
			int verde = (color & 0xff00) >> 8;
			int azul = (color & 0xff);

			data[i] = alpha << 24 | azul << 16 | verde << 8 | rojo;
		}

		int texturaID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturaID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		final float amount = Math.min(4f,
				GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ancho, alto, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				data);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		data = pixeles = null;

		return texturaID;
	}

	public int leerTextura1D(final String ruta) {
		int[] pixeles = null;
		try {
			BufferedImage imagen = ImageIO.read(new FileInputStream(ruta));
			ancho = imagen.getWidth();
			alto = 0;
			pixeles = new int[ancho];
			imagen.getRGB(0, 0, ancho, 0, pixeles, 0, ancho);
			imagen = null;
		} catch (final Exception e) {
			e.printStackTrace();
		}

		int[] data = new int[ancho];
		for (int i = 0; i < pixeles.length; i++) {
			int color = pixeles[i];
			int alpha = (color & 0xff000000) >> 24;
			int rojo = (color & 0xff0000) >> 16;
			int verde = (color & 0xff00) >> 8;
			int azul = (color & 0xff);

			data[i] = alpha << 24 | azul << 16 | verde << 8 | rojo;
		}

		int texturaID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_1D, texturaID);
		GL11.glTexImage1D(GL11.GL_TEXTURE_1D, 0, GL11.GL_RGBA, ancho, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		GL11.glBindTexture(GL11.GL_TEXTURE_1D, 0);
		data = pixeles = null;

		return texturaID;
	}

	public final void actualizarTextura(final BufferedImage imagen) {
		ancho = imagen.getWidth();
		alto = imagen.getHeight();

		final int[] pixeles = new int[ancho * alto];
		imagen.getRGB(0, 0, ancho, alto, pixeles, 0, ancho);

		int[] data = new int[pixeles.length];
		for (int i = 0; i < pixeles.length; i++) {
			int color = pixeles[i];
			int alpha = (color & 0xff000000) >> 24;
			int rojo = (color & 0xff0000) >> 16;
			int verde = (color & 0xff00) >> 8;
			int azul = (color & 0xff);

			data[i] = alpha << 24 | azul << 16 | verde << 8 | rojo;
		}
		enlazar();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ancho, alto, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				data);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, ancho, alto, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		desenlazar();
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
