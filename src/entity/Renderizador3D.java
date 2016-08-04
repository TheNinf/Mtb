package entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import graphics.Shader;
import graphics.postProcessingFX.Framebuffer;
import maths.Mat4;
import maths.Vec3;
import utils.PoolObjetos;

public class Renderizador3D {
	// TODO hacer qye solocse rendericen con normal map entidades cercanas

	private final HashMap<Modelo, ArrayList<Entidad>> entidades;
	private Shader shader;
	private Mat4 proyeccion;

	private Framebuffer shadow;
	private Shader shadowShader;
	private Mat4 ortho;
	Mat4 lightSpaceMatrix;

	private Framebuffer buffer = new Framebuffer(1280, 720, Framebuffer.Tipo.DEPTH_Y_TEXTURAS, 2);

	public Renderizador3D() {
		entidades = new HashMap<>();
		shader = new Shader("src/shaders/entity.vert", "src/shaders/entity.frag");
		proyeccion = Mat4.perspectiva(70, 1280f / 720f, 0.1f, 1000);

		ortho = Mat4.ortografico(-30, 30, -30, 30, -8f, 100f);
		// Mat4 lightSpaceMatrix = ortho.multiplicar(crearLightViewMatrix(new
		// Vec3(0, 0, -1)));
		lightSpaceMatrix = ortho.multiplicar(crearLightViewMatrix(new Vec3(0, -1000, -1000)));

		shader.enlazar();
		shader.uniformMat4("projectionMatrix", proyeccion);
		shader.uniformInt("samplerTexture", 0);
		shader.uniformInt("specularTexture", 1);
		shader.uniformInt("normalMap", 2);
		shader.uniformInt("shadowMap", 3);
		shader.uniformMat4("lightSpaceMatrix", lightSpaceMatrix);
		shader.desenlazar();

		shadow = new Framebuffer(1024, 1024, Framebuffer.Tipo.SOLO_DEPTH);
		shadowShader = new Shader("src/entity/shadow.vert", "src/entity/shadow.frag");
		shadowShader.enlazar();
		shadowShader.uniformMat4("lightSpaceMatrix", lightSpaceMatrix);
		shadowShader.desenlazar();
	}

	private Mat4 crearLightViewMatrix(Vec3 direccion) {
		direccion.normalizar();
		Mat4 lightViewM = Mat4.identidad();
		// float pitch = (float) Math.acos(new Vec2(direccion.x,
		// direccion.z).longitud());
		// lightViewM.multiplicar(Mat4.rotar(pitch, 1, 0, 0));
		// float yaw = (float) Math.toDegrees((float) Math.atan(direccion.x /
		// direccion.z));
		// yaw = direccion.z > 0 ? yaw - 180 : yaw;
		// lightViewM.multiplicar(Mat4.rotar(yaw, 0, 1, 0));
		// lightViewM.multiplicar(Mat4.trasladar(new Vec3(0)));
		Vec3 xaxis = new Vec3(0, 1, 0).cross(direccion);
		xaxis.normalizar();
		Vec3 yaxis = direccion.cross(xaxis);
		yaxis.normalizar();

		lightViewM.elementos[0] = xaxis.x;
		lightViewM.elementos[1 + 0 * 4] = yaxis.x;
		lightViewM.elementos[2 + 0 * 4] = -direccion.x;
		lightViewM.elementos[0 + 1 * 4] = xaxis.y;
		lightViewM.elementos[1 + 1 * 4] = yaxis.y;
		lightViewM.elementos[2 + 1 * 4] = -direccion.y;
		lightViewM.elementos[0 + 2 * 4] = xaxis.z;
		lightViewM.elementos[1 + 2 * 4] = yaxis.z;
		lightViewM.elementos[2 + 2 * 4] = -direccion.z;
		return lightViewM;
	}

	public void agregar(final Entidad entidad) {
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

	private void renderShadowMap(final Camara camara) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_FRONT);
		shadow.enlazar();
		shadowShader.enlazar();
		final Vec3 cam = camara.obtenerPosicion();

		ortho = Mat4.ortografico(-30, 30, -30, 30, .1f, 100f);
		lightSpaceMatrix = ortho.multiplicar((Mat4.trasladar(new Vec3(-cam.x, -cam.y, -cam.z)))
				.multiplicar(crearLightViewMatrix(new Vec3(0, -1000, -1000))));

		shadowShader.uniformMat4("lightSpaceMatrix", lightSpaceMatrix);
		final Vec3 vecReusable = PoolObjetos.solicitar(Vec3.class);

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		for (final Modelo modelo : entidades.keySet()) {
			final int numeroIndices = modelo.obtenerNumeroIndices();
			GL30.glBindVertexArray(modelo.obtenerVAO());
			GL20.glEnableVertexAttribArray(0);

			for (final Entidad entidad : entidades.get(modelo)) {
				Mat4 transformacion = Mat4.trasladar(entidad.obtenerPosicion());
				transformacion.multiplicar(Mat4.rotar(entidad.obtenerRotacion().x, 1, 0, 0));
				transformacion.multiplicar(Mat4.rotar(entidad.obtenerRotacion().y, 0, 1, 0));
				transformacion.multiplicar(Mat4.rotar(entidad.obtenerRotacion().z, 0, 0, 1));
				final float escalado = entidad.obtenerEscalado();
				vecReusable.xyz(escalado, escalado, escalado);
				transformacion.multiplicar(Mat4.escalar(vecReusable));

				shadowShader.uniformMat4("transformationMatrix", transformacion);

				if (numeroIndices < GL12.GL_MAX_ELEMENTS_INDICES)
					GL12.glDrawRangeElements(GL11.GL_TRIANGLES, 0, numeroIndices, numeroIndices, GL11.GL_UNSIGNED_INT,
							0);
				else
					GL11.glDrawElements(GL11.GL_TRIANGLES, numeroIndices, GL11.GL_UNSIGNED_INT, 0);
			}

			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
		}

		PoolObjetos.liberar(vecReusable);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shadowShader.desenlazar();
		shadow.desenlazar();
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void mostrar(final Camara camara) {
		renderShadowMap(camara);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		// buffer.enlazar();
		shader.enlazar();
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadow.obtenerDepth());
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		shader.uniformMat4("lightSpaceMatrix", lightSpaceMatrix);
		final Vec3 vecReusable = PoolObjetos.solicitar(Vec3.class);
		final Vec3 posicionCamara = camara.obtenerPosicion();
		vecReusable.xyz(-posicionCamara.x, -posicionCamara.y, -posicionCamara.z);
		Mat4 transformacion = Mat4.trasladar(vecReusable);
		shader.uniformMat4("viewMatrix", transformacion);

		for (final Modelo modelo : entidades.keySet()) {
			final int numeroIndices = modelo.obtenerNumeroIndices();
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

			shader.uniformFloat("shininess", modelo.obtenerBrillo());

			for (final Entidad entidad : entidades.get(modelo)) {
				transformacion = Mat4.trasladar(entidad.obtenerPosicion());
				transformacion.multiplicar(Mat4.rotar(entidad.obtenerRotacion().x, 1, 0, 0));
				transformacion.multiplicar(Mat4.rotar(entidad.obtenerRotacion().y, 0, 1, 0));
				transformacion.multiplicar(Mat4.rotar(entidad.obtenerRotacion().z, 0, 0, 1));
				final float escalado = entidad.obtenerEscalado();
				vecReusable.xyz(escalado, escalado, escalado);
				transformacion.multiplicar(Mat4.escalar(vecReusable));

				shader.uniformMat4("transformationMatrix", transformacion);

				if (numeroIndices < GL12.GL_MAX_ELEMENTS_INDICES)
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

		PoolObjetos.liberar(vecReusable);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		shader.desenlazar();
		// buffer.desenlazar();
	}

	public int obtenerShadowTexture() {
		return shadow.obtenerDepth();
	}

	public Framebuffer obtenerFramebuffer() {
		return buffer;
	}

}
