package entity;

import org.lwjgl.glfw.GLFW;

import main.Juego;
import maths.Vector3;

public class Camara {

	public final Vector3 posicion;
	public final Vector3 rotacion;

	public Camara(final Vector3 posicion) {
		this.posicion = posicion;
		rotacion = new Vector3();
	}

	public final void actualizar() {
		if (Juego.estaTeclaPulsada(GLFW.GLFW_KEY_A)) {
			rotacion.y -= 1f;
		} else if (Juego.estaTeclaPulsada(GLFW.GLFW_KEY_D)) {
			rotacion.y += 1f;
		}

		if (Juego.estaTeclaPulsada(GLFW.GLFW_KEY_W)) {
			posicion.x += Math.sin(Math.toRadians(rotacion.y));
			posicion.z -= Math.cos(Math.toRadians(rotacion.y));
		}
		if (Juego.estaTeclaPulsada(GLFW.GLFW_KEY_S)) {
			posicion.x -= Math.sin(Math.toRadians(rotacion.y));
			posicion.z += Math.cos(Math.toRadians(rotacion.y));
		}
		if (Juego.estaTeclaPulsada(GLFW.GLFW_KEY_UP)) {
			posicion.y++;
		}
		if (Juego.estaTeclaPulsada(GLFW.GLFW_KEY_DOWN)) {
			posicion.y--;
		}
	}
}
