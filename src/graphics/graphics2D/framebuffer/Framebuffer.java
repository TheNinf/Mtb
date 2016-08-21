package graphics.graphics2D.framebuffer;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import main.Aplicacion;
import utils.exceptions.FramebufferException;

public class Framebuffer {

	// TODO actualizar el alto de los framebuffers al cambiar el tamaño del
	// display
	public enum TIPO {
		SOLO_DEPTH, SOLO_TEXTURAS, DEPTH_Y_TEXTURAS
	}

	private int framebufferID;
	protected final int ancho, alto;

	private int[] texturas;
	private int depth;

	private int clearMask;

	public Framebuffer(final int ancho, final int alto, final TIPO tipo) throws FramebufferException {
		this(ancho, alto, tipo, 1);
	}

	public Framebuffer(final int ancho, final int alto, final TIPO tipo, final int numTexturas)
			throws FramebufferException {
		if (tipo != TIPO.SOLO_DEPTH && numTexturas <= 0)
			throw new FramebufferException(
					"No se puede crear un Framebuffer que no sirva solamente para guardar la información de la profundidad con 0 texturas.");

		this.ancho = ancho;
		this.alto = alto;
		if (ancho <= 0 | alto <= 0)
			throw new FramebufferException("No se puede crear un Framebuffer con una altura y/o anchura de 0 o menor");

		texturas = new int[numTexturas];

		switch (tipo) {
		case DEPTH_Y_TEXTURAS:
			clearMask = GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT;
			break;
		case SOLO_TEXTURAS:
			clearMask = GL11.GL_COLOR_BUFFER_BIT;
			break;
		case SOLO_DEPTH:
			clearMask = GL11.GL_DEPTH_BUFFER_BIT;
		}

		crear(tipo, numTexturas);
	}

	/**
	 * Enlaza el Framebuffer para poder comenzar el renderizado en él.
	 */
	public final void enlazar() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferID);
		GL11.glViewport(0, 0, ancho, alto);

		GL11.glClear(clearMask);
	}

	/**
	 * Enlaza el Framebuffer por defecto (lo que se ve en el Display).
	 */
	public final void desenlazar() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Aplicacion.obtenerAncho(), Aplicacion.obtenerAlto());
	}

	private final void crear(final TIPO tipo, final int numeroTexturas) {
		framebufferID = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferID);

		switch (tipo) {
		case SOLO_TEXTURAS:
			determinarTexturas(numeroTexturas);
			break;
		case DEPTH_Y_TEXTURAS:
			determinarTexturas(numeroTexturas);
			depth = crearDepth();
			break;
		case SOLO_DEPTH:
			determinarTexturas(0);
			depth = crearDepth();
		}

		comprobarFramebuffer();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	private void determinarTexturas(final int numTexturas) {
		if (numTexturas <= 0) {
			GL20.glDrawBuffers(GL11.GL_NONE);
			return;
		}

		final int[] attachments = new int[numTexturas];
		for (byte i = 0; i < numTexturas; i++) {
			attachments[i] = GL30.GL_COLOR_ATTACHMENT0 + i;
			texturas[i] = crearTextura(attachments[i]);
		}

		GL20.glDrawBuffers(attachments);
	}

	private final void comprobarFramebuffer() throws FramebufferException {
		if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE)
			throw new FramebufferException("No se ha podido crear correctamente el Framebuffer.");
	}

	protected final int crearTextura(final int attachment) {
		int textura = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textura);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, ancho, alto, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, textura, 0);
		return textura;
	}

	protected int crearDepth() {
		int depth = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depth);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, ancho, alto, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depth, 0);
		return depth;
	}

	/**
	 * Devuelve la unidad de textura que se le pide. Lanza un
	 * {@link FramebufferException} si se está intentado acceder a una unidad
	 * que no existe.
	 * 
	 * @param attachment
	 *            -> la unidad de textura. Ej: GL30.GL_COLOR_ATTACHMENT0
	 *            accedería a la unidad 0.
	 * @return -> la ID de la unidad de textura solicitada.
	 */
	public final int obtenerAttachment(final int attachment) throws FramebufferException {
		final int unidadTextura = attachment - GL30.GL_COLOR_ATTACHMENT0;
		if (texturas == null || unidadTextura < 0 || unidadTextura > texturas.length - 1)
			throw new FramebufferException("Se ha intentado acceder a una unidad de textura que no existe.");

		return texturas[unidadTextura];
	}

	/**
	 * Devuelve la textura de profundidad del Framebuffer. Si no se ha podido
	 * acceder a la textura porque ésta no ha sido creada, lanza un
	 * {@link FramebufferException}.
	 * 
	 * @return -> la ID de la textura de profundidad.
	 */
	public final int obtenerDepth() throws FramebufferException {
		if (depth == 0)
			throw new FramebufferException(
					"Se ha intentado acceder a la textura de profundidad cuando ésta no ha sido creada.");

		return depth;
	}

	@Override
	protected final void finalize() {
		GL30.glDeleteFramebuffers(framebufferID);
		texturas = null;
	}
}
