package graphics;

import entity.Camara;
import maths.Matrix4;
import maths.Vector3;
import utils.PoolObjetos;

public final class Transform {

	private static Matrix4 matrizProyeccion, matrizOrtografica, viewMatrix;

	private Transform() {
	}

	public static final void setViewMatrix(final Camara camara) {
		final Vector3 posicion = camara.posicion;
		final Vector3 rotacion = camara.rotacion;

		final Vector3 vectorReusable = PoolObjetos.VECTOR3.solicitar();

		final Matrix4 view = new Matrix4(1.0f);
		Matrix4.rotar(rotacion.y, vectorReusable.set(0, 1, 0), view);
		Matrix4.trasladar(vectorReusable.set(-posicion.x, -posicion.y, -posicion.z), view);
		viewMatrix = view;

		PoolObjetos.VECTOR3.devolver(vectorReusable);
	}

	public static final void setMatrizProyeccion(final Matrix4 proyeccion) {
		matrizProyeccion = proyeccion;
	}

	public static final void setOrthoMatrix(final Matrix4 ortografico) {
		matrizOrtografica = ortografico;
	}

	public static final Matrix4 obtenerMatrizProyeccion() {
		return matrizProyeccion;
	}

	public static final Matrix4 obtenerViewMatrix() {
		return viewMatrix;
	}

	public static final Matrix4 obtenerOrthoMatrix() {
		return matrizOrtografica;
	}
}
