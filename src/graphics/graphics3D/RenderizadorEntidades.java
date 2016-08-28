package graphics.graphics3D;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entity.Camara;
import entity.Entidad;
import entity.Modelo;
import graphics.Shader;
import graphics.Transform;
import graphics.framebuffer.Framebuffer;
import graphics.graphics3D.particles.ControladorParticulas;
import main.Juego;
import maths.Matrix4;
import maths.Vector3;
import utils.PoolObjetos;

public class RenderizadorEntidades {
	// TODO hacer qye solocse rendericen con normal map entidades cercanas
	private static final float CERCA = 0.1f, LEJOS = 1000.0f;
	private static final float MAX_INDICES = 1048576.0f;

	private final HashMap<Modelo, ArrayList<Entidad>> entidades;
	private final Shader shaderEntidades;

	private final Framebuffer framebuffer;
	private final RenderizadorSkybox r;

	private final RenderizadorSombrasEntidades renderizadorSombras;

	public RenderizadorEntidades() {
		Transform.setMatrizProyeccion(Matrix4.perspectiva(70, 1280f / 720f, CERCA, LEJOS, null));
		r = new RenderizadorSkybox();
		renderizadorSombras = new RenderizadorSombrasEntidades(1024, 1024);

		entidades = new HashMap<>();
		shaderEntidades = new Shader("src/shaders/entity.vert", "src/shaders/entity.frag");
		framebuffer = new Framebuffer(Juego.obtenerAncho(), Juego.obtenerAlto(), Framebuffer.TIPO.DEPTH_Y_TEXTURAS, 2);

		shaderEntidades.enlazar();
		shaderEntidades.uniformMatrix4("projectionMatrix", Transform.obtenerMatrizProyeccion());
		shaderEntidades.uniformInt("samplerTexture", 0);
		shaderEntidades.uniformInt("specularTexture", 1);
		shaderEntidades.uniformInt("normalMap", 2);
		shaderEntidades.uniformInt("shadowMap", 3);
		shaderEntidades.desenlazar();
	}

	public final void mostrar(final Camara camara) {
		/***** INICIACION ********/
		Transform.setViewMatrices(camara);
		final Vector3 vectorReusable = PoolObjetos.VECTOR3.solicitar();
		final Vector3 ejes = PoolObjetos.VECTOR3.solicitar();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		renderizadorSombras.renderizarSombras(camara);

		/********** ESCENA NORMAL ***********/
		GL11.glCullFace(GL11.GL_BACK);
		framebuffer.enlazar();

		shaderEntidades.enlazar();
		shaderEntidades.uniformMatrix4("lightSpaceMatrix", renderizadorSombras.matrizEspacioLuz);
		shaderEntidades.uniformMatrix4("viewMatrix", Transform.obtenerViewMatrix());
		shaderEntidades.uniformMatrix4("invertedViewMatrix", Transform.obtenerInvertedViewMatrix());

		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderizadorSombras.framebufferSombras.obtenerDepth());

		for (final Modelo modelo : entidades.keySet()) {
			final int numeroIndices = modelo.obtenerNumeroIndices();
			shaderEntidades.uniformFloat("shininess", modelo.obtenerBrillo());

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelo.obtenerTexturaID());
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelo.obtenerTexturaReflejoID());
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelo.obtenerNormalMap());

			GL30.glBindVertexArray(modelo.obtenerVAO());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL20.glEnableVertexAttribArray(3);
			GL20.glEnableVertexAttribArray(4);

			for (final Entidad entidad : entidades.get(modelo)) {
				final float escalado = entidad.obtenerEscalado();
				Matrix4 transformationMatrix = Matrix4.trasladar(entidad.posicion, null);
				Matrix4.rotar(entidad.rotacion.x, ejes.set(1, 0, 0), transformationMatrix);
				Matrix4.rotar(entidad.rotacion.y, ejes.set(0, 1, 0), transformationMatrix);
				Matrix4.rotar(entidad.rotacion.z, ejes.set(0, 0, 1), transformationMatrix);

				vectorReusable.set(escalado, escalado, escalado);
				Matrix4.escalar(vectorReusable, transformationMatrix);
				shaderEntidades.uniformMatrix4("transformationMatrix", transformationMatrix);

				if (numeroIndices < MAX_INDICES)
					GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, numeroIndices, numeroIndices, GL11.GL_UNSIGNED_INT,
							0);
				else
					GL11.glDrawElements(GL11.GL_TRIANGLES, numeroIndices, GL11.GL_UNSIGNED_INT, 0);
			}

			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL20.glDisableVertexAttribArray(3);
			GL20.glDisableVertexAttribArray(4);
			GL30.glBindVertexArray(0);
		}

		/****** CIERRE **********/
		PoolObjetos.VECTOR3.devolver(vectorReusable);
		PoolObjetos.VECTOR3.devolver(ejes);
		shaderEntidades.desenlazar();

		GL11.glDisable(GL11.GL_CULL_FACE);
		r.render();

		ControladorParticulas.dibujar(camara);
		framebuffer.desenlazar();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	public final void agregar(final Entidad entidad) {
		final Modelo modelo = entidad.obtenerModelo();
		ArrayList<Entidad> lista = entidades.get(modelo);
		if (lista != null) {
			lista.add(entidad);
		} else {
			lista = new ArrayList<>();
			lista.add(entidad);
			entidades.put(modelo, lista);
		}
	}

	public final class RenderizadorSombrasEntidades {

		private static final float GRANDARIA = 50.0f;
		private static final float PROFUNDIDAD = 150.0f;

		private final FramebufferSombras framebufferSombras;
		private final Shader shadowShader;

		private final Matrix4 matrizOrtografica;
		private Matrix4 matrizEspacioLuz;

		public RenderizadorSombrasEntidades(final int ancho, final int alto) {
			framebufferSombras = new FramebufferSombras(ancho, alto);
			matrizOrtografica = Matrix4.ortografico(-GRANDARIA, GRANDARIA, -GRANDARIA, GRANDARIA, CERCA, PROFUNDIDAD,
					null);
			matrizEspacioLuz = new Matrix4();
			shadowShader = new Shader("src/graphics/graphics3D/shadow.vert", "src/graphics/graphics3D/shadow.frag");
		}

		public final void renderizarSombras(final Camara camara) {
			GL11.glCullFace(GL11.GL_FRONT);

			framebufferSombras.enlazar();
			shadowShader.enlazar();
			matrizEspacioLuz = Matrix4.multiplicar(
					rotarMatrizRespectoDireccionLuz(new Vector3(0, 0, 1000), matrizOrtografica),
					Matrix4.trasladar(new Vector3(-camara.posicion.x, -camara.posicion.y, -camara.posicion.z), null),
					null);
			shadowShader.uniformMatrix4("lightSpaceMatrix", matrizEspacioLuz);

			final Vector3 vectorReusable = PoolObjetos.VECTOR3.solicitar();
			final Vector3 ejes = PoolObjetos.VECTOR3.solicitar();

			for (final Modelo modelo : entidades.keySet()) {
				final int numeroIndices = modelo.obtenerNumeroIndices();
				GL30.glBindVertexArray(modelo.obtenerVAO());
				GL20.glEnableVertexAttribArray(0);

				for (final Entidad entidad : entidades.get(modelo)) {
					final float escalado = entidad.obtenerEscalado();
					Matrix4 transformacion = Matrix4.trasladar(entidad.posicion, null);
					Matrix4.rotar(entidad.rotacion.x, ejes.set(1, 0, 0), transformacion);
					Matrix4.rotar(entidad.rotacion.y, ejes.set(1, 0, 0), transformacion);
					Matrix4.rotar(entidad.rotacion.z, ejes.set(1, 0, 0), transformacion);
					vectorReusable.set(escalado, escalado, escalado);
					Matrix4.escalar(vectorReusable, transformacion);

					shadowShader.uniformMatrix4("transformationMatrix", transformacion);

					if (numeroIndices < GL12.GL_MAX_ELEMENTS_INDICES)
						GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, numeroIndices, numeroIndices,
								GL11.GL_UNSIGNED_INT, 0);
					else
						GL11.glDrawElements(GL11.GL_TRIANGLES, numeroIndices, GL11.GL_UNSIGNED_INT, 0);
				}

				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);
			}

			PoolObjetos.VECTOR3.devolver(vectorReusable);
			PoolObjetos.VECTOR3.devolver(ejes);

			shadowShader.desenlazar();
			framebufferSombras.desenlazar();
		}

		private final Matrix4 rotarMatrizRespectoDireccionLuz(final Vector3 direccion, final Matrix4 destino) {
			direccion.normalizar();

			final Matrix4 matriz = new Matrix4();

			final Vector3 UP = PoolObjetos.VECTOR3.solicitar().set(0, 1, 0);
			final Vector3 xAxis = Vector3.cross(UP, direccion).normalizar();
			PoolObjetos.VECTOR3.devolver(UP);

			final Vector3 yAxis = Vector3.cross(direccion, xAxis).normalizar();

			matriz.elementos[Matrix4.m00] = xAxis.x;
			matriz.elementos[Matrix4.m10] = yAxis.x;
			matriz.elementos[Matrix4.m20] = -direccion.x;

			matriz.elementos[Matrix4.m01] = xAxis.y;
			matriz.elementos[Matrix4.m11] = yAxis.y;
			matriz.elementos[Matrix4.m21] = -direccion.y;

			matriz.elementos[Matrix4.m02] = xAxis.z;
			matriz.elementos[Matrix4.m12] = yAxis.z;
			matriz.elementos[Matrix4.m22] = -direccion.z;

			matriz.elementos[Matrix4.m33] = 1.0f;
			return Matrix4.multiplicar(destino, matriz, matriz);
		}

		public final class FramebufferSombras extends Framebuffer {
			public FramebufferSombras(final int ancho, final int alto) {
				super(ancho, alto, TIPO.SOLO_DEPTH, 0);
			}

			@Override
			protected final int crearDepth() {
				int depth = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, depth);
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, ancho, alto, 0,
						GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL14.GL_COMPARE_R_TO_TEXTURE);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depth,
						0);
				return depth;
			}
		}
	}

	public int obtenerShadowTexture() {
		return renderizadorSombras.framebufferSombras.obtenerDepth();
	}

	public Framebuffer obtenerFramebuffer() {
		return framebuffer;
	}

}
