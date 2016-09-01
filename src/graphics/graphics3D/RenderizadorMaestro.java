package graphics.graphics3D;

import org.lwjgl.opengl.GL30;

import entity.Camara;
import entity.Entidad;
import graphics.Transform;
import graphics.framebuffer.Framebuffer;
import graphics.graphics3D.particles.GestorParticulas;
import graphics.graphics3D.skybox.RenderizadorSkybox;
import graphics.graphics3D.water.RenderizadorAgua;
import graphics.graphics3D.water.TileAgua;
import graphics.postProcessingFX.PostProceso;
import main.Juego;
import maths.Matrix4;

public class RenderizadorMaestro {

	public static final float CERCA = 0.1f, LEJOS = 1000.0f;

	private final RenderizadorEntidades renderizadorEntidades;
	private final RenderizadorSombras renderizadorSombras;

	private final RenderizadorSkybox renderizadorSkybox;
	private final RenderizadorAgua renderizadorAgua;

	private final Framebuffer escena;

	public RenderizadorMaestro() {
		Transform.setMatrizProyeccion(Matrix4.perspectiva(70, 1280f / 720f, CERCA, LEJOS, null));

		renderizadorEntidades = new RenderizadorEntidades();
		renderizadorSombras = new RenderizadorSombras(1024, 1024);

		renderizadorSkybox = new RenderizadorSkybox();
		renderizadorAgua = new RenderizadorAgua();

		escena = new Framebuffer(Juego.obtenerAncho(), Juego.obtenerAlto(), Framebuffer.TIPO.DEPTH_Y_TEXTURAS, 2);
	}

	public final void mostrar(final Camara camara) {
		Transform.setViewMatrix(camara);

		renderizadorEntidades.crearMatricesTransformacion();
		renderizadorSombras.renderizarSombras(renderizadorEntidades.obtenerEntidades(), camara);
		escena.enlazar();

		renderizadorEntidades.mostrar(renderizadorSombras.obtenerMatrizEspacioLuz(),
				renderizadorSombras.obtenerTexturaSombras());
		renderizadorAgua.render(camara);
		renderizadorSkybox.render();
		GestorParticulas.dibujar(camara);

		escena.desenlazar();

		PostProceso.renderizar(escena.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT0),
				escena.obtenerAttachment(GL30.GL_COLOR_ATTACHMENT1));
	}

	public final void agregarEntidad(final Entidad entidad) {
		renderizadorEntidades.agregar(entidad);
	}

	public final void agregarTileAgua(final TileAgua tile) {
		renderizadorAgua.agregarTileAgua(tile);
	}

}
