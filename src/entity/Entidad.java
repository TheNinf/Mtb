package entity;

import maths.Vec3;

public class Entidad {

	private final Modelo modelo;

	private final Vec3 posicion;
	private final Vec3 rotacion;
	private final float escalado;

	public Entidad(final Modelo modelo, final Vec3 posicion, final Vec3 rotacion, final float escalado) {
		this.modelo = modelo;
		this.posicion = posicion;
		this.escalado = escalado;
		this.rotacion = rotacion;
	}

	public void incrementarRot(float x, float y, float z) {
		rotacion.sumar(new Vec3(x, y, z));
	}

	public Modelo obtenerModelo() {
		return modelo;
	}

	public Vec3 obtenerPosicion() {
		return posicion;
	}

	public Vec3 obtenerRotacion() {
		return rotacion;
	}

	public final float obtenerEscalado() {
		return escalado;
	}

}
