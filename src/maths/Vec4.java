package maths;

public class Vec4 {

	public float x, y, z, w;

	public Vec4(final float xyzw) {
		x = y = z = w = xyzw;
	}

	public Vec4(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vec4 sumar(final Vec4 otro) {
		this.x += otro.x;
		this.y += otro.y;
		this.z += otro.z;
		this.w += otro.w;

		return this;
	}

	public Vec4 restar(final Vec4 otro) {
		this.x -= otro.x;
		this.y -= otro.y;
		this.z -= otro.z;
		this.w -= otro.w;

		return this;
	}

	public Vec4 multiplicar(final Vec4 otro) {
		this.x *= otro.x;
		this.y *= otro.y;
		this.z *= otro.z;
		this.w *= otro.w;

		return this;
	}

	public Vec4 dividir(final Vec4 otro) {
		this.x /= otro.x;
		this.y /= otro.y;
		this.z /= otro.z;
		this.w /= otro.w;

		return this;
	}

	@Override
	public boolean equals(final Object objeto) {
		if (!(objeto instanceof Vec4))
			return false;

		final Vec4 cast = (Vec4) objeto;
		return this.x == cast.x && this.y == cast.y && this.z == cast.z && this.w == cast.w;
	}

}
