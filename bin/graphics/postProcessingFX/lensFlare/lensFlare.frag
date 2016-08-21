#version 400 core

out vec4 out_color;

uniform sampler2D samplerTexture;
uniform sampler2D lensColor;

uniform sampler2D normalImage;
uniform sampler2D lensDirtImage;
uniform sampler2D lensStar;

in vec2 textureCoords;

const float ghostDispersal = 0.31f;
const float haloWidth = 0.497f;
const int ghostSamples = 5;
const float fDistortion = 2.79f;

vec3 textureDistorted(sampler2D tex, in vec2 texcoord, in vec2 direction, in vec3 distortion) {

	return vec3(
		texture(tex, texcoord + direction * distortion.r).r,
		texture(tex, texcoord + direction * distortion.g).g,
		texture(tex, texcoord + direction * distortion.b).b
	);

}

const vec2 center = vec2(0.5);

void main(void){
	vec2 texcoord = -textureCoords + vec2(1);
	vec2 texelSize = 1f / vec2(textureSize(samplerTexture, 0));
	vec3 distortion = vec3(-texelSize.x * fDistortion, 0, texelSize.x * fDistortion);

	//ghost vect
	vec2 ghostVector = (center - texcoord) * ghostDispersal;
	vec2 direction = normalize(ghostVector);
	
	//sample ghost
	vec4 result = vec4(0);
	for(int i = 0; i < ghostSamples; i++) {
		vec2 offset = fract(texcoord + ghostVector * float(i));
		float weight = length(center - offset) / length(center);
		weight = pow(1f - weight, 10);
		
		result += textureDistorted(samplerTexture, offset, direction, distortion) * weight;
	 }
		
	//halo
	//vec2 haloVector = direction * haloWidth;
	//float weight = length(center - fract(texcoord + haloVector)) / length(center);
	//weight = pow(1f - weight, 5);
	//result += textureDistorted(samplerTexture, texcoord + haloVector, direction, distortion) * weight;
		
	result *= texture(lensColor, vec2(length(center - texcoord) / length(center), 0)) * 7.34f;

	vec4 lensDirt = texture(lensDirtImage, textureCoords);
	result *= lensDirt;
	out_color = texture(normalImage, textureCoords) + result;
}