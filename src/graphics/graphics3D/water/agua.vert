#version 400 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;
uniform float time;

const float amplitude = 0.8f;
const float wavelength = 2.9f;
const float speed = 2.95f;
const float frequency = 2 * 3.141592 / wavelength;
const float phase = speed * frequency;
const float steepness = 1 / (frequency  * amplitude);
const vec2 direction = vec2(0, -1);

out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector;

void main(void) {	
	textureCoords = textureCoord;

	float alpha = frequency * dot(direction, position) + time * phase;
	float height = amplitude * sin(alpha);
	
	float cosAlpha = cos(alpha);
	float steepnessAmplitude = steepness * amplitude;
	float x = position.x + steepnessAmplitude * direction.x * cosAlpha;
	float z = position.y + steepnessAmplitude * direction.y * cosAlpha;	


	vec4 worldPos = vec4(x, height, z, 1.0f);
	toCameraVector = cameraPosition - worldPos.xyz;
	fromLightVector = worldPos.xyz - lightPosition;
	textureCoords = textureCoord;

	gl_Position = projectionMatrix * viewMatrix * worldPos;
}