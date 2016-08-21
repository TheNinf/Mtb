package graphics.graphics2D;

import java.util.ArrayList;

import maths.Matrix4;

public abstract class Renderizador2D {

	protected final ArrayList<Matrix4> pilaTransformacion = new ArrayList<>();
	protected Matrix4 ultimaMatriz;

	protected Renderizador2D() {
		pilaTransformacion.add(new Matrix4(1.0f));
		ultimaMatriz = pilaTransformacion.get(0);
	}

	public void push(final Matrix4 matriz) {
		pilaTransformacion
				.add(Matrix4.multiplicar(matriz, pilaTransformacion.get(pilaTransformacion.size() - 1), null));
		ultimaMatriz = pilaTransformacion.get(pilaTransformacion.size() - 1);
	}

	public void sobreescribirPush(final Matrix4 matriz) {
		pilaTransformacion.add(matriz);
		ultimaMatriz = matriz;
	}

	public void pop() {
		final Matrix4 matriz = pilaTransformacion.get(pilaTransformacion.size() - 1);
		if (pilaTransformacion.size() > 1)
			pilaTransformacion.remove(matriz);
		ultimaMatriz = matriz;
	}

	public abstract void comenzar();

	public abstract void acabar();

	public abstract void mostrar();

	public abstract void enviar(final Renderizable2D renderizable);

}
