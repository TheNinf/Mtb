package entity;

import org.lwjgl.glfw.GLFW;

import main.Aplicacion;
import maths.Vec3;

public class Camara {

	private final Vec3 posicion;

	public Camara(final Vec3 posicion) {
		this.posicion = posicion;
	}

	public final void actualizar() {
		if (Aplicacion.estaTeclaPulsada(GLFW.GLFW_KEY_W)) {
			posicion.z -= 0.1f;
		} else if (Aplicacion.estaTeclaPulsada(GLFW.GLFW_KEY_S)) {
			posicion.z += 0.1f;
		} else if (Aplicacion.estaTeclaPulsada(GLFW.GLFW_KEY_A)) {
			posicion.x -= 0.1f;
		} else if (Aplicacion.estaTeclaPulsada(GLFW.GLFW_KEY_D)) {
			posicion.x += 0.1f;
		} else if (Aplicacion.estaTeclaPulsada(GLFW.GLFW_KEY_UP)) {
			posicion.y += 0.1f;
		} else if (Aplicacion.estaTeclaPulsada(GLFW.GLFW_KEY_DOWN)) {
			posicion.y -= 0.1f;
		}
	}

	public final Vec3 obtenerPosicion() {
		return posicion;
	}

}
