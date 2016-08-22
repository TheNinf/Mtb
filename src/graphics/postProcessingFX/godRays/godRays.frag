#version 400 core

out vec4 color;

in vec2 textureCoords;

uniform sampler2D textura;
uniform vec2 lightPosition;

const float exposure = 0.034f;
const float decay = 0.97015f;
const float density = 0.926f;
const float weight = 0.58767f;

const int NUM_SAMPLES = 65;

void main(void) {
	vec2 deltaTextCoords = vec2(textureCoords - lightPosition);
	deltaTextCoords *= 1.0f / float(NUM_SAMPLES) * density;
	float illuminationDecay = 1.0f;

	vec2 texCoords = textureCoords;
	for(int i = 0; i < NUM_SAMPLES; i++) {
		texCoords -= deltaTextCoords;
		vec4 col = texture(textura, texCoords);

		col *= illuminationDecay * weight;

		color += col;
		illuminationDecay *= decay;
	}
	color *= exposure;
}