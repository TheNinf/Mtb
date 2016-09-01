package graphics.graphics3D.water;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import entity.Camara;
import graphics.Shader;
import graphics.Textura;
import graphics.Transform;
import main.Juego;
import maths.Vector3;

public class RenderizadorAgua {

	private Shader shader;

	private Textura normalMap;
	private Textura dudvMap;

	private float moveFactor = 0, tiempo = 0;
	private final ArrayList<TileAgua> tiles;

	public RenderizadorAgua() {
		tiles = new ArrayList<>();
		shader = new Shader("src/graphics/graphics3D/water/agua.vert", "src/graphics/graphics3D/water/agua.frag");

		shader.enlazar();
		shader.uniformMatrix4("projectionMatrix", Transform.obtenerMatrizProyeccion());
		shader.uniformInt("normalMap", 0);
		shader.uniformInt("dudvMap", 1);
		shader.uniformVector3("lightPosition", new Vector3(0, 0, 100000));
		shader.desenlazar();

		normalMap = new Textura("src/waterNormal.png", Textura.TIPO.TEXTURA_3D);
		dudvMap = new Textura("src/waterDUDV.png", Textura.TIPO.TEXTURA_3D);
	}

	public void render(final Camara cam) {
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		shader.enlazar();
		shader.uniformMatrix4("viewMatrix", Transform.obtenerViewMatrix());
		shader.uniformFloat("time", tiempo);
		shader.uniformVector3("cameraPosition", cam.posicion);
		shader.uniformFloat("moveFactor", moveFactor);

		moveFactor += Juego.aplicarDelta(0.04f);
		tiempo += Juego.aplicarDelta(0.33f);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap.obtenerID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvMap.obtenerID());

		for (final TileAgua tile : tiles) {
			final int indices = tile.obtenerNumeroIndices();
			GL30.glBindVertexArray(tile.obtenerVAO());
			GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, indices, indices, GL11.GL_UNSIGNED_INT, 0);
			GL30.glBindVertexArray(0);
		}

		shader.desenlazar();

		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	public final void agregarTileAgua(final TileAgua tile) {
		tiles.add(tile);
	}
}
