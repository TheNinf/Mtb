package graphics.g2d.fonts;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import graphics.Textura;

public class FontAtlas {

	private final int ancho, alto;
	private final BufferedImage imagen;
	private final ArrayList<Letra> letras;
	private final Textura textura;
	private final Fuente fuente;
	private final Map<Float, List<Character>> offsets;

	private int contadorX;
	private boolean necesitaActualizar = false;

	public FontAtlas(final int ancho, final int alto, final Fuente fuente) {
		this.alto = alto;
		this.ancho = ancho;
		this.fuente = fuente;
		letras = new ArrayList<>();

		contadorX = 0;

		imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < ancho; x++)
			for (int y = 0; y < alto; y++)
				imagen.setRGB(x, y, 0x000000);
		textura = new Textura(imagen);
		offsets = new HashMap<>();
	}

	private final void dibujarLetra(final char simbolo) {
		for (final Letra letra : letras)
			if (letra.simbolo == simbolo)
				return;

		final Font fuente = this.fuente.obtenerFuente();
		final Graphics2D g = (Graphics2D) imagen.getGraphics();

		g.setFont(fuente);

		final String string = String.valueOf(simbolo);
		final double sAlto = g.getFontMetrics(fuente).getStringBounds(string, g).getHeight();
		final int grandaria = g.getFontMetrics().charWidth(simbolo);

		g.setComposite(AlphaComposite.SrcOut);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		float offset = 0;

		if (contadorX + grandaria > imagen.getWidth()) {
			System.err.println(
					"Has superado la longitud del atlas! Añade por parámetro el nuevo ancho. No se hace automático por problemas de rendimiento. No sé si se mantendrá así.");
		}

		final Iterator iterator = offsets.keySet().iterator();
		while (iterator.hasNext()) {
			final float key = (float) iterator.next();
			final List<Character> caracteres = offsets.get(key);
			if (caracteres.contains(simbolo))
				offset += key;
		}
		letras.add(new Letra(simbolo, grandaria, (float) sAlto + offset, (float) contadorX / (float) ancho,
				(float) (contadorX + grandaria) / (float) ancho, 0, ((float) (sAlto + offset)) / alto));

		g.drawString(string, contadorX, fuente.getSize());
		g.dispose();
		contadorX += grandaria;
		necesitaActualizar = true;
	}

	public final Letra obtenerLetra(final char simbolo) {
		for (final Letra letra : letras)
			if (letra.simbolo == simbolo)
				return letra;
		dibujarLetra(simbolo);
		return obtenerLetra(simbolo);
	}

	public final void actualizar() {
		if (necesitaActualizar) {
			necesitaActualizar = false;
			textura.actualizarTextura(imagen);
		}
	}

	public final void ponerOffsetA(final char simbolo, final float offset) {
		final List<Character> batch = offsets.get(offset);

		if (batch == null) {
			final ArrayList<Character> newBatch = new ArrayList<>();
			newBatch.add(simbolo);
			offsets.put(offset, newBatch);
		} else {
			batch.add(simbolo);
		}
	}

	public final void ponerOffsetA(final char[] simbolos, final float offset) {
		final List<Character> batch = offsets.get(offset);

		if (batch == null) {
			final ArrayList<Character> newBatch = new ArrayList<>();
			for (byte i = 0; i < simbolos.length; i++)
				newBatch.add(simbolos[i]);
			offsets.put(offset, newBatch);
		} else {
			for (byte i = 0; i < simbolos.length; i++)
				batch.add(simbolos[i]);
		}
	}

	public final Textura obtenerTextura() {
		return textura;
	}

	public final int obtenerTexturaID() {
		return textura.obtenerID();
	}

	public final BufferedImage obtenerImagen() {
		return imagen;
	}
}
