package graphics.graphics3D.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import graphics.Shader;
import graphics.Textura;
import graphics.Transform;
import main.Juego;
import maths.Matrix4;
import maths.Vector3;
import utils.PoolObjetos;

public class RenderizadorSkybox {

	private final Shader shader;
	private final Textura textura;

	private final float velocidadRotacion = 2.45f;

	private float rotacionY = 0;

	public RenderizadorSkybox() {
		final String[] TEXTURE_FILES = { "src/right.png", "src/left.png", "src/top.png", "src/bottom.png",
				"src/back.png", "src/front.png" };
		textura = new Textura(TEXTURE_FILES);
		shader = new Shader("src/graphics/graphics3D/skybox/skybox.vert", "src/graphics/graphics3D/skybox/skybox.geom",
				"src/graphics/graphics3D/skybox/skybox.frag");
		shader.enlazar();
		shader.uniformMatrix4("projectionMatrix", Transform.obtenerMatrizProyeccion());
		shader.desenlazar();
	}

	public final void render() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		rotacionY += Juego.aplicarDelta(velocidadRotacion);
		final Vector3 eje = PoolObjetos.VECTOR3.solicitar().set(0, 1, 0);

		shader.enlazar();
		shader.uniformMatrix4("viewMatrix", Transform.obtenerViewMatrix());
		shader.uniformMatrix4("transformationMatrix", Matrix4.rotar(rotacionY, eje, null));

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		textura.enlazar();
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		shader.desenlazar();

		PoolObjetos.VECTOR3.devolver(eje);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

}
