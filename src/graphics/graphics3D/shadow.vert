#version 400 core

layout (location = 0) in vec3 position;
layout (location = 5) in mat4 transformationMatrix;

uniform mat4 lightSpaceMatrix;

void main(void) {
	gl_Position = lightSpaceMatrix * transformationMatrix * vec4(position, 1);
}