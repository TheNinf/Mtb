#version 400 core

layout (location = 0) in mat4 modelViewMatrix;

out mat4 transformationViewMatrix;

void main(void) {
	transformationViewMatrix = modelViewMatrix;
}