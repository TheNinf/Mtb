package graphics.graphics2D;

import java.util.ArrayList;

import maths.Matrix4;

public class Grupo extends Renderizable2D {

	private final ArrayList<Renderizable2D> renderizables;
	private Matrix4 matrizTransformacion;

	public Grupo(final Matrix4 matrizTransformacion) {
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

	public void cambiarMatriz(final Matrix4 matriz) {
		this.matrizTransformacion = matriz;
	}
}
