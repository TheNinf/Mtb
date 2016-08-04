package graphics.g2d;

import java.util.ArrayList;

import graphics.g2d.fonts.Fuente;
import graphics.g2d.fonts.Label;
import maths.Mat4;

public abstract class Renderizador2D {

	protected final ArrayList<Mat4> pilaTransformacion = new ArrayList<>();
	protected Mat4 ultimaMatriz;

	protected Renderizador2D() {
		pilaTransformacion.add(Mat4.identidad());
		ultimaMatriz = pilaTransformacion.get(0);
	}

	public void push(final Mat4 matriz) {
		pilaTransformacion.add(matriz.multiplicar(pilaTransformacion.get(pilaTransformacion.size() - 1)));
		ultimaMatriz = pilaTransformacion.get(pilaTransformacion.size() - 1);
	}

	public void sobreescribirPush(final Mat4 matriz) {
		pilaTransformacion.add(matriz);
		ultimaMatriz = matriz;
	}

	public void pop() {
		final Mat4 matriz = pilaTransformacion.get(pilaTransformacion.size() - 1);
		if (pilaTransformacion.size() > 1)
			pilaTransformacion.remove(matriz);
		ultimaMatriz = matriz;
	}

	public abstract void dibujarTexto(final Label label, final Fuente fuente);

	public abstract void comenzar();

	public abstract void acabar();

	public abstract void mostrar();

	public abstract void enviar(final Renderizable2D renderizable);

}
