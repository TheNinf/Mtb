package graphics.layer;

import java.util.ArrayList;

import graphics.Shader;
import graphics.graphics2D.Renderizable2D;
import graphics.graphics2D.Renderizador2D;
import maths.Matrix4;

public abstract class Capa {

	protected Renderizador2D renderizador;
	protected ArrayList<Renderizable2D> renderizables;
	protected Shader shader;

	protected Matrix4 matrizProyeccion;

	protected Capa(final Renderizador2D renderizador, final Matrix4 matrizProyeccion, final Shader shader) {
		this.renderizador = renderizador;
		this.shader = shader;
		this.matrizProyeccion = matrizProyeccion;

		this.renderizables = new ArrayList<>();
		iniciar();
		shader.uniformMatrix4("pr_matrix", matrizProyeccion);
		terminar();
	}

	public void iniciar() {
		shader.enlazar();
	}

	public void terminar() {
		shader.desenlazar();
	}

	public void render() {
		renderizador.comenzar();
		final short grandaria = (short) renderizables.size();
		for (short i = 0; i < grandaria; i++)
			renderizables.get(i).enviar(renderizador);
		renderizador.acabar();
		renderizador.mostrar();
	}

	public void agregar(final Renderizable2D renderizable) {
		renderizables.add(renderizable);
	}
}
