#version 400 core

out vec4 color;

in vec2 textureCoords;

uniform sampler2D textura;

void main(void) {
	color = texture(textura, textureCoords);
}	