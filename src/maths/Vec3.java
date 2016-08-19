package maths;

public class Vec3 {

	public float x, y, z;

	public Vec3() {
		x = y = z = 0;
	}

	public Vec3(final float xyz) {
		x = y = z = xyz;
	}

	public Vec3(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 sumar(final Vec3 otro) {
		this.x += otro.x;
		this.y += otro.y;
		this.z += otro.z;

		return this;
	}

	public Vec3 cross(Vec3 otro) {
		return new Vec3(y * otro.z - z * otro.y, z * otro.x - x * otro.z, x * otro.y - y * otro.x);
	}

	public Vec3 restar(final Vec3 otro) {
		this.x -= otro.x;
		this.y -= otro.y;
		this.z -= otro.z;

		return this;
	}

	public Vec3 multiplicar(final Vec3 otro) {
		this.x *= otro.x;
		this.y *= otro.y;
		this.z *= otro.z;

		return this;
	}

	public Vec3 dividir(final Vec3 otro) {
		this.x /= otro.x;
		this.y /= otro.y;
		this.z /= otro.z;

		return this;
	}

	public Vec3 escalar(final float escalado) {
		this.x *= escalado;
		this.y *= escalado;
		this.z *= escalado;
		return this;
	}

	public float obtenerDistancia(final Vec3 otro) {
		return (float) Math.sqrt(Math.pow(otro.x - x, 2) + Math.pow(otro.y - y, 2) + Math.pow(otro.z - z, 2));
	}

	public float longitud() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Este método devuelve la distancia entre dos puntos al cuadrado. Aunque
	 * esta distancia no se puede utilizar para obtener las medidas entre dos
	 * puntos porque daría, como su nombre indica, la distancia al cuadrado, es
	 * más eficiente y da el mismo resultado para comparar distancias entre
	 * objetos.
	 * 
	 * @param otro
	 * @return -> la distancia
	 */
	public float obtenerDistanciaCuadrado(final Vec3 otro) {
		return (float) (Math.pow(otro.x - x, 2) + Math.pow(otro.y - y, 2) + Math.pow(otro.z - z, 2));
	}

	public final Vec3 x(final float x) {
		this.x = x;
		return this;
	}

	public final Vec3 y(final float y) {
		this.y = y;
		return this;
	}

	public final Vec3 z(final float z) {
		this.z = z;
		return this;
	}

	public final Vec3 xyz(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public final Vec3 normalizar() {
		final float modulo = (float) Math.sqrt(x * x + y * y + z * z);
		this.x /= modulo;
		this.y /= modulo;
		this.z /= modulo;
		return this;
	}

	@Override
	public boolean equals(final Object objeto) {
		if (!(objeto instanceof Vec3))
			return false;

		final Vec3 cast = (Vec3) objeto;
		return this.x == cast.x && this.y == cast.y && this.z == cast.z;
	}

}
