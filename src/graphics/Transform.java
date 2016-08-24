package graphics;

import entity.Camara;
import maths.Matrix4;
import maths.Vector3;
import utils.PoolObjeto;

public final class Transform {

	private static Matrix4 matrizProyeccion, matrizOrtografica, viewMatrix, skyboxViewMatrix;

	private Transform() {
	}

	public static final void setViewMatrices(final Camara camara) {
		final Vector3 posicion = camara.posicion;
		final Vector3 rotacion = camara.rotacion;

		final Vector3 vectorReusable = PoolObjeto.VECTOR3.solicitar();
		final Matrix4 matriz = new Matrix4(1.0f);
		Matrix4.rotar(rotacion.y, vectorReusable.set(0, 1, 0), matriz);
		Matrix4.trasladar(vectorReusable.set(-posicion.x, -posicion.y, -posicion.z), matriz);
		// Matrix4.rotar(rotacion.x, vectorReusable.set(1, 0, 0), matriz);
		// Matrix4.rotar(rotacion.z, vectorReusable.set(0, 0, 1), matriz);
		PoolObjeto.VECTOR3.devolver(vectorReusable);
		viewMatrix = matriz;

		// TODO CAMBIAR
		final Matrix4 matrizSkybox = new Matrix4(1.0f);
		Matrix4.rotar(rotacion.y, vectorReusable.set(0, 1, 0), matrizSkybox);
		skyboxViewMatrix = matrizSkybox;
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

	public static final Matrix4 obtenerSkyboxViewMatrix() {
		return skyboxViewMatrix;
	}
}
