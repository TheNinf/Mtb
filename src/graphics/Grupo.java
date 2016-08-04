package graphics;

import java.util.ArrayList;

import graphics.g2d.Renderizable2D;
import graphics.g2d.Renderizador2D;
import maths.Mat4;

public class Grupo extends Renderizable2D {

	private final ArrayList<Renderizable2D> renderizables;
	private Mat4 matrizTransformacion;

	public Grupo(final Mat4 matrizTransformacion) {
		renderizables = new ArrayList<>();
		this.matrizTransformacion = matrizTransformacion;
	}

	public void agregar(final Renderizable2D renderizable) {
		renderizables.add(renderizable);
		renderizable.perteneceAGrupo = true;
	}

	@Override
	public void enviar(final Renderizador2D renderizador) {
		renderizador.push(matrizTransformacion);
		for (final Renderizable2D renderizable : renderizables) {
			renderizable.enviar(renderizador);
		}
		renderizador.pop();
	}

	public void cambiarMatriz(final Mat4 matriz) {
		this.matrizTransformacion = matriz;
	}
}
