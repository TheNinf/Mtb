package graphics.postProcessingFX;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import main.Aplicacion;

public class Framebuffer {

	public enum Tipo {
		SOLO_DEPTH, SOLO_TEXTURAS, DEPTH_Y_TEXTURAS
	}

	private int framebufferID;
	private final int ancho, alto;

	private int[] texturas;
	private int depth;

	public Framebuffer(int ancho, int alto, Tipo tipo, int numTexturas) {
		this.ancho = ancho;
		this.alto = alto;
		texturas = new int[numTexturas];
		crear(tipo, numTexturas);
	}

	public Framebuffer(int ancho, int alto, Tipo tipo) {
		this(ancho, alto, tipo, 1);
	}

	private final void crear(Tipo tipo, int numeroTexturas) {
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
			depth = crearDepth();
		}

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	private void determinarTexturas(final int numTexturas) {
		final int[] attachments = new int[numTexturas];
		for (int i = 0; i < numTexturas; i++) {
			attachments[i] = GL30.GL_COLOR_ATTACHMENT0 + i;
		}

		GL20.glDrawBuffers(attachments);

		for (int i = 0; i < numTexturas; i++) {
			texturas[i] = crearTextura(attachments[i]);
		}
	}

	private final int crearTextura(int attachment) {
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

	private final int crearDepth() {
		int depth = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depth);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, ancho, alto, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL14.GL_COMPARE_R_TO_TEXTURE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		GL11.glTexParameterfv(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, borderColor);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depth, 0);
		return depth;
	}

	public final void enlazar() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferID);
		GL11.glViewport(0, 0, ancho, alto);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public final void desenlazar() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Aplicacion.obtenerAncho(), Aplicacion.obtenerAlto());
	}

	public final int obtenerAttachment(final int attachment) {
		return texturas[attachment - GL30.GL_COLOR_ATTACHMENT0];
	}

	public final int obtenerDepth() {
		return depth;
	}
}
