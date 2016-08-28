package graphics.graphics3D.particles;

import entity.Camara;
import main.Juego;
import maths.Vector3;
import utils.PoolObjetos;

public class Particula {

	private TexturaParticula textura;

	private Vector3 posicion;
	private Vector3 velocidad;

	private float rotacion, escalado, tiempoVida, gravedad, distancia;
	private float vidaActual;

	private boolean muerta = true;

	/*
	 * public Particula(final Vector3 posicion, final Vector3 velocidad, final
	 * float rotacion, final float escalado, final float tiempoVida, final float
	 * gravedad) { this.posicion = posicion; this.velocidad = velocidad;
	 * this.rotacion = rotacion; this.escalado = escalado; this.tiempoVida =
	 * tiempoVida; this.gravedad = gravedad; }
	 */

	public Particula() {
		textura = null;
		posicion = null;
		velocidad = null;
		rotacion = escalado = tiempoVida = gravedad = 0.0f;
	}

	public final boolean actualizar(final Camara camara) {
		if ((vidaActual += Juego.obtenerDelta()) > tiempoVida) {
			ControladorParticulas.morir(this);
			return (muerta = true);
		}

		velocidad.y += Juego.aplicarDelta(-50 * gravedad);
		final Vector3 cambio = PoolObjetos.VECTOR3.solicitar();

		cambio.set(velocidad.x, velocidad.y, velocidad.z).escalar(Juego.obtenerDelta());
		Vector3.sumar(posicion, cambio, posicion);

		PoolObjetos.VECTOR3.devolver(cambio);

		distancia = camara.posicion.distanciaCuadrado(posicion);
		return false;
	}

	public final void set(final TexturaParticula textura, final Vector3 posicion, final Vector3 velocidad,
			final float rotacion, final float escalado, final float tiempoVida, final float gravedad) {
		this.textura = textura;
		this.posicion = posicion;
		this.velocidad = velocidad;
		this.rotacion = rotacion;
		this.escalado = escalado;
		this.tiempoVida = tiempoVida;
		this.gravedad = gravedad;

		muerta = false;
		vidaActual = 0.0f;
	}

	public final TexturaParticula obtenerTextura() {
		return textura;
	}

	public final boolean estaMuerta() {
		return muerta;
	}

	public final Vector3 obtenerPosicion() {
		return posicion;
	}

	public final float obtenerRotacion() {
		return rotacion;
	}

	public final float obtenerEscalado() {
		return escalado;
	}

	public final float obtenerDistancia() {
		return distancia;
	}
}
