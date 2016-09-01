#version 400 core

layout (location = 0) out vec4 color;
layout (location = 1) out vec4 brightColor;

in vec3 toCameraVector;
in vec3 fromLightVector;
in vec2 textureCoords;

uniform sampler2D normalMap;
uniform sampler2D dudvMap;
uniform float moveFactor;

const float shininess = 37f;
const float waveStrength = 0.002;

void main(void) {
	vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
	distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
	vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;
	
	vec4 normalMapColor = texture(normalMap, distortedTexCoords);
	vec3 normal = normalize(vec3(normalMapColor.r * 2 - 1, normalMapColor.b, normalMapColor.g * 2 - 1));
	vec3 viewVector = normalize(toCameraVector);

	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0);
	specular = pow(specular, shininess);
	color = vec4(0.08984375, 0.16015625, 0.19921875, 1) + specular;
}