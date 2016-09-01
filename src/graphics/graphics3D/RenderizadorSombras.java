package graphics.graphics3D;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import entity.Camara;
import entity.Entidad;
import entity.Modelo;
import graphics.Shader;
import graphics.framebuffer.Framebuffer;
import maths.Matrix4;
import maths.Vector3;
import utils.PoolObjetos;

public final class RenderizadorSombras {

	private static final float GRANDARIA = 50.0f;
	private static final float PROFUNDIDAD = 150.0f;

	private final FramebufferSombras escenaSombras;
	private final Shader shadowShader;

	private final Matrix4 matrizOrtografica;
	private Matrix4 matrizEspacioLuz;

	public RenderizadorSombras(final int ancho, final int alto) {
		escenaSombras = new FramebufferSombras(ancho, alto);

		matrizOrtografica = Matrix4.ortografico(-GRANDARIA, GRANDARIA, -GRANDARIA, GRANDARIA, RenderizadorMaestro.CERCA,
				PROFUNDIDAD, null);
		matrizEspacioLuz = new Matrix4();

		shadowShader = new Shader("src/graphics/graphics3D/shadow.vert", "src/graphics/graphics3D/shadow.frag");
	}

	public final void renderizarSombras(final HashMap<Modelo, ArrayList<Entidad>> entidades, final Camara camara) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_FRONT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		escenaSombras.enlazar();
		final Vector3 vectorReusable = PoolObjetos.VECTOR3.solicitar();

		shadowShader.enlazar();
		matrizEspacioLuz = Matrix4
				.multiplicar(rotarMatrizRespectoDireccionLuz(vectorReusable.set(0, 0, 1000), matrizOrtografica),
						Matrix4.trasladar(
								vectorReusable.set(-camara.posicion.x, -camara.posicion.y, -camara.posicion.z), null),
						null);
		shadowShader.uniformMatrix4("lightSpaceMatrix", matrizEspacioLuz);
		PoolObjetos.VECTOR3.devolver(vectorReusable);

		for (final Modelo modelo : entidades.keySet()) {
			modelo.render();
		}

		shadowShader.desenlazar();

		escenaSombras.desenlazar();

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
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
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, ancho, alto, 0, GL11.GL_DEPTH_COMPONENT,
					GL11.GL_FLOAT, (ByteBuffer) null);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL14.GL_COMPARE_R_TO_TEXTURE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depth, 0);
			return depth;
		}
	}

	public final Matrix4 obtenerMatrizEspacioLuz() {
		return matrizEspacioLuz;
	}

	public final int obtenerTexturaSombras() {
		return escenaSombras.obtenerDepth();
	}

}
