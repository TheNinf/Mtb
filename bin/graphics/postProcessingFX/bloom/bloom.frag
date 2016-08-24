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
	color = getColor(textura) + getColor(texturaBrillo1) / 18.0f +
		 getColor(texturaBrillo2) / 12.0f + getColor(texturaBrillo3) / 6.0f + 
			getColor(texturaBrillo4) / 2.0f;
}