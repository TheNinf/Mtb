package main;

import org.lwjgl.glfw.GLFW;

import audio.GestorOpenAL;
import maths.Vector2;
import utils.PoolObjetos;

public abstract class Juego {

	protected static Ventana ventana;
	private long contador;

	protected short fps;
	protected byte aps;

	private static double ultimoTiempo = 0.0f;
	private static float delta;

	protected Juego() {
		iniciar();
	}

	public final void crearVentana(final String nombre, final int ancho, final int alto) {
		ventana = new Ventana(nombre, ancho, alto);
		ventana.iniciarOpenGL();
		GestorOpenAL.iniciar();
	}

	public final void correr() {
		final double NANO_POR_ACT = 1000000000 / 60;

		long referenciaActualizacion = contador = System.nanoTime();
		double delta = 0, tiempoTranscurrido = 0;

		short fps = 0;
		byte aps = 0;

		long inicioBucleR = 0;

		while (!ventana.debeCerrarse()) {
			Juego.delta = calcularDelta();

			inicioBucleR = System.nanoTime();
			tiempoTranscurrido = inicioBucleR - referenciaActualizacion;
			referenciaActualizacion = inicioBucleR;
			delta += tiempoTranscurrido / NANO_POR_ACT;

			while (delta >= 1) {
				actualizar();
				aps++;
				delta--;
			}

			ventana.limpiar();
			renderizar();
			ventana.actualizar();

			fps++;

			if (System.nanoTime() - contador >= 1000000000L) {
				tick();
				PoolObjetos.actualizarPools();

				contador = System.nanoTime();
				this.fps = fps;
				this.aps = aps;
				fps = aps = 0;
			}
		}
	}

	private static final float calcularDelta() {
		final double tiempo = GLFW.glfwGetTime();
		final float delta = (float) (tiempo - ultimoTiempo);
		ultimoTiempo = tiempo;
		return delta;
	}

	public abstract void iniciar();

	public abstract void actualizar();

	public abstract void renderizar();

	public void tick() {

	}

	public static final Vector2 posicionRaton() {
		return ventana.obtenerPosicionRaton();
	}

	public static final boolean estaTeclaPulsada(final int tecla) {
		return ventana.teclaPresionada(tecla);
	}

	public static final boolean estaBotonRatonPulsado(final int boton) {
		return ventana.botonRatonPresionado(boton);
	}

	public static final boolean haSidoTeclaTipada(final int tecla) {
		return ventana.teclaPulsada(tecla);
	}

	public static final boolean haSidoBotonRatonTipado(final int boton) {
		return ventana.botonRatonPulsado(boton);
	}

	public static final void sincronizacionVertical(final boolean sincronizacion) {
		ventana.sincronizacionVertical(sincronizacion);
	}

	public final int obtenerFPS() {
		return fps;
	}

	public static final int obtenerAncho() {
		return ventana.obtenerAncho();
	}

	public static final int obtenerAlto() {
		return ventana.obtenerAlto();
	}

	public static final float obtenerDelta() {
		return delta;
	}

	public static final float aplicarDelta(final float vel) {
		return vel * delta;
	}

	public final void cerrar() {
		GestorOpenAL.terminar();
		ventana.terminar();
	}

}
