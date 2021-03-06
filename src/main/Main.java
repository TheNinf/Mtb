package main;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

import audio.GestorSonidos;
import entity.Camara;
import entity.Entidad;
import entity.Modelo;
import graphics.Shader;
import graphics.Textura;
import graphics.graphics3D.RenderizadorMaestro;
import graphics.graphics3D.particles.GestorParticulas;
import graphics.graphics3D.particles.TexturaParticula;
import graphics.graphics3D.water.TileAgua;
import graphics.layer.CapaTiles;
import graphics.postProcessingFX.PostProceso;
import loader3D.LectorArchivosMTB;
import loader3D.ModeloMTB;
import maths.Vector3;

public class Main extends Juego {

	private final CapaTiles capa;
	private final Camara camara;
	RenderizadorMaestro renderizador;
	Entidad entidad;
	TexturaParticula tex;
	TexturaParticula tex2;

	protected Main() {
		tex = new TexturaParticula("src/particleStar.png", 1, true);
		tex2 = new TexturaParticula("src/particleStar.png", 1, false);
		Shader shader = new Shader("src/shaders/sprite.vert", "src/shaders/sprite.geom", "src/shaders/sprite.frag");
		// Shader shader = new Shader("src/shaders/basic.vert",
		// "src/shaders/basic.frag");
		capa = new CapaTiles(shader);
		shader.enlazar();
		int[] texIDS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		shader.uniformArrayInt("textures[0]", texIDS);
		shader.desenlazar();

		// capa.agregar(label);

		ModeloMTB m = LectorArchivosMTB.leerObjetoMTB("src/crate.mtb");
		renderizador = new RenderizadorMaestro();
		final Modelo modelo = new Modelo(m.obtenerVertices(), m.obtenerNormals(), m.obtenerTextureCoords(),
				m.obtenerTangentes(), m.obtenerBitangentes(), m.obtenerIndices(),
				new Textura("src/crate.png", Textura.TIPO.TEXTURA_3D)).ponerBrillo(2.45f)
						.ponerTexturaReflejo(new Textura("src/crateSpecular.png", Textura.TIPO.TEXTURA_3D))
						.ponerNormalMap(new Textura("src/crateNormalMap.png", Textura.TIPO.TEXTURA_3D));

		entidad = new Entidad(modelo, new Vector3(-5, 1, -10), new Vector3(0, 0, 0), 0.01f);
		Entidad entidad2 = new Entidad(modelo, new Vector3(-5, 0, -5), new Vector3(0, 0, 0), 0.01f);

		renderizador.agregarEntidad(entidad);
		renderizador.agregarEntidad(entidad2);
		renderizador.agregarTileAgua(new TileAgua(128, 400, -100, -200));

		// Textura t = new Textura("src/crateNormalMap.png",
		// Textura.TIPO.TEXTURA_SPRITE);
		// for (int x = -16; x < 16; x++)
		// for (int y = 9; y > -9; y--)
		// capa.agregar(new Sprite(x, y, 0.9f, 0.9f, t));
		// capa.agregar(new Sprite(-16, 9, 8, 8, new Vector4(0, 1, 1,
		// 1)));

		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			Entidad e = new Entidad(modelo, new Vector3(r.nextFloat() * 200, r.nextFloat() * 200, r.nextFloat() * 2),
					new Vector3(0, 0, 0), 0.01f);
			renderizador.agregarEntidad(e);
		}
		GestorSonidos.obtener("test").sonar();
		camara = new Camara(new Vector3());
		GestorParticulas.iniciar();
	}

	public static void main(String[] args) {
		final Main juego = new Main();
		// juego.sincronizacionVertical(true);
		juego.correr();
		juego.cerrar();
	}

	@Override
	public final void iniciar() {
		crearVentana("Testeando el engine!", 1280, 720);
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

		if (Juego.estaTeclaPulsada(GLFW.GLFW_KEY_P)) {
			Random r = new Random();
			GestorParticulas.agregarParticula(r.nextBoolean() ? tex : tex2, new Vector3(camara.posicion),
					new Vector3(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1), 45, 1, 10f,
					-0.05f);
		}
		renderizador.mostrar(camara);
		// capa.iniciar();
		// capa.render();
		// capa.terminar();
	}

}
