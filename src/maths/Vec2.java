package maths;

public class Vec2 {

	public float x, y;

	public Vec2(final float xy) {
		this.x = this.y = xy;
	}

	public Vec2(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2 sumar(final Vec2 otro) {
		this.x += otro.x;
		this.y += otro.y;

		return this;
	}

	public float longitud() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public Vec2 restar(final Vec2 otro) {
		this.x -= otro.x;
		this.y -= otro.y;

		return this;
	}

	public Vec2 multiplicar(final Vec2 otro) {
		this.x *= otro.x;
		this.y *= otro.y;

		return this;
	}

	public Vec2 dividir(final Vec2 otro) {
		this.x /= otro.x;
		this.y /= otro.y;

		return this;
	}

	public float obtenerDistancia(final Vec2 otro) {
		return (float) Math.sqrt(Math.pow(otro.x - x, 2) + Math.pow(otro.y - y, 2));
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
	public float obtenerDistanciaCuadrado(final Vec2 otro) {
		return (float) (Math.pow(otro.x - x, 2) + Math.pow(otro.y - y, 2));
	}

	public final Vec2 normalizar() {
		final float modulo = (float) Math.sqrt(x * y + y * y);
		return new Vec2(x / modulo, y / modulo);
	}

	@Override
	public boolean equals(final Object objeto) {
		return !(objeto instanceof Vec2) ? false : x == ((Vec2) objeto).x && y == ((Vec2) objeto).y;
	}
}
