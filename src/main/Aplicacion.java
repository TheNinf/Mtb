package main;

import audio.GestorOpenAL;
import graphics.Ventana;
import graphics.g2d.fonts.GestorFuentes;
import maths.Vec2;
import utils.PoolObjeto;

public abstract class Aplicacion {

	protected static Ventana ventana;
	private long contador;

	protected short fps;
	protected byte aps;

	protected Aplicacion() {
		iniciar();
		GestorFuentes.iniciarFuentesBasicas();
		agregarFuentesPersonalizadas();
	}

	public final void crearVentana(final String nombre, final int ancho, final int alto) {
		ventana = new Ventana(nombre, ancho, alto);
		ventana.iniciarOpenGL();
		GestorOpenAL.iniciar();

		contador = System.nanoTime();
	}

	public final void correr() {
		final double NANO_POR_ACT = 1000000000 / 60;

		long referenciaActualizacion = System.nanoTime();
		double delta = 0, tiempoTranscurrido = 0;

		short fps = 0;
		byte aps = 0;

		long inicioBucleR = 0;

		while (!ventana.debeCerrarse()) {
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
				PoolObjeto.actualizarPools();

				contador = System.nanoTime();
				this.fps = fps;
				this.aps = aps;
				aps = 0;
				fps = 0;
			}
		}
	}

	public abstract void iniciar();

	public abstract void actualizar();

	public abstract void renderizar();

	public void tick() {

	}

	public void agregarFuentesPersonalizadas() {

	}

	public static final Vec2 posicionRaton() {
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

	public final void cerrar() {
		GestorOpenAL.terminar();
		ventana.terminar();
	}

}
