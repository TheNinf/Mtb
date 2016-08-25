#version 400 core

out vec4 color;

in vec2 textureCoords;

uniform sampler2D textura;
uniform sampler2D texturaBrillo1;
uniform sampler2D texturaBrillo2;
uniform sampler2D texturaBrillo3;
uniform sampler2D texturaBrillo4;

vec4 getColor(sampler2D textura) {
	return texture(textura, textureCoords);
}

void main(void) {
	color = getColor(textura) + getColor(texturaBrillo1) / 12.0f +
		 getColor(texturaBrillo2) / 8.0f + getColor(texturaBrillo3) / 4.0f + 
			getColor(texturaBrillo4) / 1.75f;
}