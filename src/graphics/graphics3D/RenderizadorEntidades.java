package graphics.graphics3D;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import entity.Entidad;
import entity.Modelo;
import graphics.Shader;
import graphics.Transform;
import maths.Matrix4;
import maths.Vector3;
import utils.PoolObjetos;

public class RenderizadorEntidades {

	private static final int MAX_INDICES = 1048576;

	private final HashMap<Modelo, ArrayList<Entidad>> entidades;
	private final Shader shaderEntidades;

	public RenderizadorEntidades() {
		entidades = new HashMap<>();

		shaderEntidades = new Shader("src/shaders/entity.vert", "src/shaders/entity.frag");
		shaderEntidades.enlazar();
		shaderEntidades.uniformMatrix4("projectionMatrix", Transform.obtenerMatrizProyeccion());
		shaderEntidades.uniformInt("samplerTexture", 0);
		shaderEntidades.uniformInt("specularTexture", 1);
		shaderEntidades.uniformInt("normalMap", 2);
		shaderEntidades.uniformInt("shadowMap", 3);
		shaderEntidades.desenlazar();
	}

	public final void mostrar(final Matrix4 matrizEspacioLuz, final int sombras) {
		/***** INICIACION ********/
		final Vector3 vectorReusable = PoolObjetos.VECTOR3.solicitar();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		/********** ESCENA NORMAL ***********/
		shaderEntidades.enlazar();
		shaderEntidades.uniformMatrix4("lightSpaceMatrix", matrizEspacioLuz);
		shaderEntidades.uniformMatrix4("viewMatrix", Transform.obtenerViewMatrix());

		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, sombras);

		for (final Modelo modelo : entidades.keySet()) {
			shaderEntidades.uniformFloat("shininess", modelo.obtenerBrillo());

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelo.obtenerTexturaID());
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelo.obtenerTexturaReflejoID());
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelo.obtenerNormalMap());

			modelo.render();
		}

		/****** CIERRE **********/
		PoolObjetos.VECTOR3.devolver(vectorReusable);
		shaderEntidades.desenlazar();

		GL11.glDisable(GL11.GL_CULL_FACE);
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

	public final void crearMatricesTransformacion() {
		final Vector3 vectorReusable = PoolObjetos.VECTOR3.solicitar();

		for (final Modelo modelo : entidades.keySet()) {
			modelo.comenzar();
			for (final Entidad entidad : entidades.get(modelo)) {

				final float escalado = entidad.obtenerEscalado();
				Matrix4 transformationMatrix = Matrix4.trasladar(entidad.posicion, null);
				Matrix4.rotar(entidad.rotacion.x, vectorReusable.set(1, 0, 0), transformationMatrix);
				Matrix4.rotar(entidad.rotacion.y, vectorReusable.set(0, 1, 0), transformationMatrix);
				Matrix4.rotar(entidad.rotacion.z, vectorReusable.set(0, 0, 1), transformationMatrix);

				vectorReusable.set(escalado, escalado, escalado);
				Matrix4.escalar(vectorReusable, transformationMatrix);
				modelo.agregarTransformation(transformationMatrix);

			}
			modelo.terminar();
		}

		PoolObjetos.VECTOR3.devolver(vectorReusable);
	}

	public final HashMap<Modelo, ArrayList<Entidad>> obtenerEntidades() {
		return entidades;
	}
}
