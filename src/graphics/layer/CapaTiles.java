package graphics.layer;

import graphics.Shader;
import graphics.g2d.RenderizadorAvanzado2D;
import maths.Mat4;

public class CapaTiles extends Capa {

	public CapaTiles(final Shader shader) {
		super(new RenderizadorAvanzado2D(), Mat4.ortografico(-16.0f, 16.0f, -9.0f, 9.0f, -1.0f, 1.0f), shader);
	}

}
