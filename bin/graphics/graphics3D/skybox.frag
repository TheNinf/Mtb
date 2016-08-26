#version 400 core

layout (location = 0) out vec4 color;
layout (location = 1) out vec4 brightColor;

in vec3 textureCoords;

uniform samplerCube textura;

const float lowerLimit = -1.0f;
const float upperLimit = 100.0f;
const float skybox_size = 500.0f;

void main(void) {
	float factor = clamp((textureCoords.y * skybox_size - lowerLimit) / (upperLimit - lowerLimit), 
		0.0f, 1.0f);
	if(factor == 1.0f) {
		color = brightColor = texture(textura, textureCoords);
	} else if(factor == 0.0f) {
		color = vec4(0.0f, 0.4f, 0.7f, 1.0f);
	} else {
		color = brightColor = texture(textura, textureCoords);
		brightColor *= factor;
		color = mix(vec4(0.0f, 0.4f, 0.7f, 1.0f), color, factor);
	}
}