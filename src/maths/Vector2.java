package maths;

public class Vector2 {

	public float x, y;

	public Vector2(final float x, final float y) {
		set(x, y);
	}

	public Vector2() {
		this(0, 0);
	}

	public static final Vector2 sumar(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		if (destino == null)
			return new Vector2(derecha.x + izquierda.x, derecha.y + izquierda.y);

		destino.set(derecha.x + izquierda.x, derecha.y + izquierda.y);
		return destino;
	}

	public static final Vector2 restar(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		if (destino == null)
			return new Vector2(derecha.x - izquierda.x, derecha.y - izquierda.y);

		destino.set(derecha.x - izquierda.x, derecha.y - izquierda.y);
		return destino;
	}

	public static final Vector2 multiplicar(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		if (destino == null)
			return new Vector2(derecha.x * izquierda.x, derecha.y * izquierda.y);

		destino.set(derecha.x * izquierda.x, derecha.y * izquierda.y);
		return destino;
	}

	public static final Vector2 dividir(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		if (destino == null)
			return new Vector2(derecha.x / izquierda.x, derecha.y / izquierda.y);

		destino.set(derecha.x / izquierda.x, derecha.y / izquierda.y);
		return destino;
	}

	public final void escalar(final float numero) {
		set(x * numero, y * numero);
	}

	public final void negar() {
		set(-x, -y);
	}

	public final Vector2 normalizar() {
		final float modulo = obtenerModulo();
		set(x / modulo, y / modulo);
		return this;
	}

	public final void set(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public final float longitud() {
		return obtenerModulo();
	}

	private final float obtenerModulo() {
		return (float) Math.sqrt(x * x + y * y);
	}
}
