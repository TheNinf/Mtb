package maths;

public class Vector2 {

	public float x, y;

	public Vector2(final float x, final float y) {
		set(x, y);
	}

	public Vector2() {
		set(0, 0);
	}

	public static final Vector2 sumar(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		final float x = derecha.x + izquierda.x, y = derecha.y + izquierda.y;
		return destino == null ? new Vector2(x, y) : destino.set(x, y);
	}

	public static final Vector2 restar(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		final float x = derecha.x - izquierda.x, y = derecha.y - izquierda.y;
		return destino == null ? new Vector2(x, y) : destino.set(x, y);
	}

	public static final Vector2 multiplicar(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		final float x = derecha.x * izquierda.x, y = derecha.y * izquierda.y;
		return destino == null ? new Vector2(x, y) : destino.set(x, y);
	}

	public static final Vector2 dividir(final Vector2 derecha, final Vector2 izquierda, final Vector2 destino) {
		final float x = derecha.x / izquierda.x, y = derecha.y / izquierda.y;
		return destino == null ? new Vector2(x, y) : destino.set(x, y);
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

	public final Vector2 set(final float x, final float y) {
		this.x = x;
		this.y = y;

		return this;
	}

	public final float longitud() {
		return obtenerModulo();
	}

	private final float obtenerModulo() {
		return (float) Math.sqrt(x * x + y * y);
	}

	@Override
	public final String toString() {
		return "x: " + x + ", y: " + y;
	}
}
