package entity;

import maths.Vector3;
import utils.PoolObjetos;

public class Entidad {

	private final Modelo modelo;

	public final Vector3 posicion;
	public final Vector3 rotacion;

	private final float escalado;

	public Entidad(final Modelo modelo, final Vector3 posicion, final Vector3 rotacion, final float escalado) {
		this.modelo = modelo;
		this.posicion = posicion;
		this.escalado = escalado;
		this.rotacion = rotacion;
	}

	public void incrementarRot(float x, float y, float z) {
		Vector3 rotAIncrementar = PoolObjetos.VECTOR3.solicitar().set(x, y, z);
		Vector3.sumar(rotacion, rotAIncrementar, rotacion);
		PoolObjetos.VECTOR3.devolver(rotAIncrementar);
	}

	public Modelo obtenerModelo() {
		return modelo;
	}

	public final float obtenerEscalado() {
		return escalado;
	}

}
