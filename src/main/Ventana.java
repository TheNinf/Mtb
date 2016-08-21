package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import maths.Vector2;

public class Ventana {

	private final long id;
	private int ancho;
	private int alto;

	private static final int NUM_TECLAS = 1024, NUM_BOTONES = 32;

	private static final boolean[] teclas = new boolean[NUM_TECLAS];
	private static final boolean[] estadoTeclas = new boolean[NUM_TECLAS];

	private static final boolean[] botonesRaton = new boolean[NUM_BOTONES];
	private static final boolean[] estadoBotonesRaton = new boolean[NUM_BOTONES];

	private final Vector2 posRaton;

	public Ventana(final String titulo, final int ancho, final int alto) {
		GLFWErrorCallback.createPrint(System.out).set();

		if (!GLFW.glfwInit()) {
			GLFW.glfwTerminate();
			throw new IllegalStateException("No se ha podido iniciar OpenGL!");
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);

		id = GLFW.glfwCreateWindow(ancho, alto, titulo, 0, 0);

		if (id == 0) {
			terminar();
			throw new IllegalStateException("No se ha podido crear la ventana!");
		}

		GLFW.glfwMakeContextCurrent(id);
		iniciarCallbacks();
		GLFW.glfwShowWindow(id);

		this.ancho = ancho;
		this.alto = alto;
		this.posRaton = new Vector2();
	}

	private void iniciarCallbacks() {
		GLFW.glfwSetWindowSizeCallback(id, (window, width, height) -> {
			this.ancho = width;
			this.alto = height;

			GL11.glViewport(0, 0, ancho, alto);
		});

		GLFW.glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> {
			if (key >= 0)
				teclas[key] = action != GLFW.GLFW_RELEASE;
		});

		GLFW.glfwSetMouseButtonCallback(id, (window, button, action, mods) -> {
			botonesRaton[button] = action != GLFW.GLFW_RELEASE;
		});

		GLFW.glfwSetCursorPosCallback(id, (window, xpos, ypos) -> {
			posRaton.x = (float) xpos;
			posRaton.y = (float) ypos;
		});
	}

	public final void actualizar() {
		System.arraycopy(teclas, 0, estadoTeclas, 0, NUM_TECLAS);
		System.arraycopy(botonesRaton, 0, estadoBotonesRaton, 0, NUM_BOTONES);

		GLFW.glfwSwapBuffers(id);
		GLFW.glfwPollEvents();

		final int error = GL11.glGetError();
		if (error != GL11.GL_NO_ERROR)
			System.out.println("Error de OpenGL: " + error);
	}

	public final void limpiar() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public final void iniciarOpenGL() {
		GL.createCapabilities();
		GL11.glViewport(0, 0, ancho, alto);

		System.out.println("Versión de OpenGL: " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("Renderizador OpenGL: " + GL11.glGetString(GL11.GL_RENDERER));
		System.out.println("Vendedor OpenGL: " + GL11.glGetString(GL11.GL_VENDOR));
	}

	public final void sincronizacionVertical(final boolean activado) {
		GLFW.glfwSwapInterval(activado ? GL11.GL_TRUE : GL11.GL_FALSE);
	}

	public final boolean debeCerrarse() {
		return GLFW.glfwWindowShouldClose(id);
	}

	public final void terminar() {
		GLFW.glfwDestroyWindow(id);
		GLFW.glfwTerminate();
	}

	public final boolean teclaPresionada(final int tecla) {
		return teclas[tecla];
	}

	public final boolean botonRatonPresionado(final int boton) {
		return botonesRaton[boton];
	}

	public final boolean teclaPulsada(final int tecla) {
		return teclas[tecla] && !estadoTeclas[tecla];
	}

	public final boolean botonRatonPulsado(final int boton) {
		return botonesRaton[boton] && !estadoBotonesRaton[boton];
	}

	public final int obtenerAncho() {
		return ancho;
	}

	public final int obtenerAlto() {
		return alto;
	}

	public final Vector2 obtenerPosicionRaton() {
		return posRaton;
	}
}
