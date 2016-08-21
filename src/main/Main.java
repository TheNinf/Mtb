package main;

import org.lwjgl.opengl.GL11;

import audio.GestorSonidos;
import entity.Camara;
import entity.Entidad;
import entity.Modelo;
import entity.RenderizadorEntidades;
import graphics.Shader;
import graphics.Textura;
import graphics.layer.CapaTiles;
import graphics.postProcessingFX.PostProceso;
import loader3D.LectorArchivosMTB;
import loader3D.ModeloMTB;
import maths.Vector3;

public class Main extends Aplicacion {

	private final CapaTiles capa;
	private final Camara camara;
	RenderizadorEntidades renderizador;
	Entidad entidad;

	protected Main() {
		Shader shader = new Shader("src/shaders/sprite.vert", "src/shaders/sprite.geom", "src/shaders/sprite.frag");
		// Shader shader = new Shader("src/shaders/basic.vert",
		// "src/shaders/basic.frag");
		capa = new CapaTiles(shader);
		shader.enlazar();
		int[] texIDS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		shader.uniformArrayInt("textures", texIDS);
		shader.desenlazar();

		// capa.agregar(label);

		ModeloMTB m = LectorArchivosMTB.leerObjetoMTB("src/resultado.mtb");
		renderizador = new RenderizadorEntidades();
		final Modelo modelo = new Modelo(m.obtenerVertices(), m.obtenerNormals(), m.obtenerTextureCoords(),
				m.obtenerTangentes(), m.obtenerBitangentes(), m.obtenerIndices(),
				new Textura("src/crate.png", Textura.TIPO.TEXTURA_3D)).ponerBrillo(9)
						.ponerTexturaReflejo(new Textura("src/crateSpecular.png", Textura.TIPO.TEXTURA_3D))
						.ponerNormalMap(new Textura("src/crateNormalMap.png", Textura.TIPO.TEXTURA_3D));

		entidad = new Entidad(modelo, new Vector3(-5, 1, -10), new Vector3(0, 0, 0), 0.01f);
		Entidad entidad2 = new Entidad(modelo, new Vector3(-5, 0, -5), new Vector3(90, 0, 0), 0.01f);

		renderizador.agregar(entidad);
		renderizador.agregar(entidad2);

		Textura t = new Textura("src/crateNormalMap.png", Textura.TIPO.TEXTURA_SPRITE);
		for (int x = -16; x < 16; x++)
			for (int y = 9; y > -9; y--)
				// capa.agregar(new Sprite(x, y, 0.9f, 0.9f, t));
				// capa.agregar(new Sprite(-16, 9, 8, 8, new Vector4(0, 1, 1,
				// 1)));

				GL11.glClearColor(0, 0.5f, 0.9f, 1);
		GestorSonidos.obtener("test").sonar();
		camara = new Camara(new Vector3());

	}

	public static void main(String[] args) {
		final Main juego = new Main();
		juego.correr();
		juego.cerrar();
	}

	@Override
	public final void iniciar() {
		crearVentana("Mataluba Core", 1280, 720);
		PostProceso.iniciar();
	}

	@Override
	public final void tick() {
		System.out.println(fps);
	}

	@Override
	public final void actualizar() {

	}

	@Override
	public final void renderizar() {
		entidad.incrementarRot(0, 0.1f, 0);
		camara.actualizar();
		renderizador.mostrar(camara);

		PostProceso.renderizar(renderizador.obtenerFramebuffer());
		capa.iniciar();
		capa.render();
		capa.terminar();
	}

}
