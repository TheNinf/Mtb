package graphics.layer;

import graphics.Shader;
import graphics.graphics2D.RenderizadorSprites;
import maths.Matrix4;

public class CapaTiles extends Capa {

	public CapaTiles(final Shader shader) {
		super(new RenderizadorSprites(), Matrix4.ortografico(-16.0f, 16.0f, -9.0f, 9.0f, -1.0f, 1.0f, null), shader);
	}

}
