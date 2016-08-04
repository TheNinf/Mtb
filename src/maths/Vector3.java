package maths;

public class Vector3 {

	public float x, y, z;

	public Vector3(final float x, final float y, final float z) {
		set(x, y, z);
	}

	public Vector3() {
		this(0, 0, 0);
	}

	public static final Vector3 sumar(final Vector3 derecha, final Vector3 izquierda, final Vector3 destino) {
		if (destino == null)
			return new Vector3(derecha.x + izquierda.x, derecha.y + izquierda.y, +derecha.z + izquierda.z);

		destino.set(derecha.x + izquierda.x, derecha.y + izquierda.y, derecha.z + izquierda.z);
		return destino;
	}

	public static final Vector3 restar(final Vector3 derecha, final Vector3 izquierda, final Vector3 destino) {
		if (destino == null)
			return new Vector3(derecha.x - izquierda.x, derecha.y - izquierda.y, derecha.z - izquierda.z);

		destino.set(derecha.x - izquierda.x, derecha.y - izquierda.y, derecha.z - izquierda.z);
		return destino;
	}

	public static final Vector3 multiplicar(final Vector3 derecha, final Vector3 izquierda, final Vector3 destino) {
		if (destino == null)
			return new Vector3(derecha.x * izquierda.x, derecha.y * izquierda.y, derecha.z * izquierda.z);

		destino.set(derecha.x * izquierda.x, derecha.y * izquierda.y, derecha.z * izquierda.z);
		return destino;
	}

	public static final Vector3 dividir(final Vector3 derecha, final Vector3 izquierda, final Vector3 destino) {
		if (destino == null)
			return new Vector3(derecha.x / izquierda.x, derecha.y / izquierda.y, derecha.z / izquierda.z);

		destino.set(derecha.x / izquierda.x, derecha.y / izquierda.y, derecha.z / izquierda.z);
		return destino;
	}

	public static final Vector3 cross(final Vector3 derecha, final Vector3 izquierda) {
		return new Vector3(derecha.y * izquierda.z - derecha.z * izquierda.y,
				derecha.z * izquierda.x - derecha.x * izquierda.z, derecha.x * izquierda.y - derecha.y * izquierda.x);
	}

	public final void escalar(final float numero) {
		set(x * numero, y * numero, z * numero);
	}

	public final void negar() {
		set(-x, -y, -z);
	}

	public final Vector3 normalizar() {
		final float modulo = obtenerModulo();
		set(x / modulo, y / modulo, z / modulo);
		return this;
	}

	public final void set(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final float longitud() {
		return obtenerModulo();
	}

	private final float obtenerModulo() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
}
