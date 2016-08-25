#version 400 core

layout (location = 0) out vec4 color;
layout (location = 1) out vec4 brightColor;

in vec3 textureCoords;

uniform samplerCube textura;

void main(void) {
	color = brightColor = texture(textura, textureCoords);
	brightColor /= 1.4f;
}