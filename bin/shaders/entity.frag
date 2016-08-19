#version 400 core

layout (location = 0) out vec4 color;
layout (location = 1) out vec4 brightColor;

in vec2 textureCoords;
in vec3 fragmentPosition;
in vec3 toCameraVector;
in vec3 lightDirection;
in vec4 worldPositionLightSpace;

uniform sampler2D samplerTexture;
uniform sampler2D specularTexture;
uniform sampler2D normalMap;
uniform sampler2DShadow shadowMap;

uniform float shininess;

const float minDiffuse = 0.4f;

float shadowCalculation(vec4 posLightSpace){
	//vec3 projCoords = posLightSpace.xyz / posLightSpace.w;
	vec3 projCoords = posLightSpace.xyz / 2f + 0.5f;
	if(projCoords.z > 1.0f)
		return 1.0f;
	
	vec2 texelSize = 1.0f / textureSize(shadowMap, 0);
	float shadow = 0;
	float bias = 0.005f;
	
	for(int x = -1; x <= 1; x++) {
		for(int y = -1; y <= 1; y++) {
			shadow += texture(shadowMap, vec3(projCoords.xy + vec2(x, y) * texelSize, projCoords.z - bias));
		}
	}

	return (shadow / 9.0f);
}

void main(void){
	vec4 textureColor = texture(samplerTexture, textureCoords);
	if(textureColor.a == 0){
		color = vec4(0);
		return;
	}
	
	vec4 normalMapValue = 2.0f * texture(normalMap, textureCoords) - 1.0f;
	vec3 unitNormal = normalize(normalMapValue.rgb);
	brightColor = vec4(0, 0, 0, 1);

	float shadow = shadowCalculation(worldPositionLightSpace);
	float diffuse = max(dot(unitNormal, -lightDirection) * shadow, minDiffuse);

	float specularFactor = texture(specularTexture, textureCoords).r;
	if(shininess > 0.0f && specularFactor > 0.0f) {
		vec3 unitCamVector = normalize(toCameraVector - fragmentPosition);
		vec3 halfwayDirection = normalize(-lightDirection + unitCamVector);
		float specular = pow(dot(unitNormal, halfwayDirection), shininess) * specularFactor; 
		
		vec3 finalColor = diffuse * textureColor.xyz + (specular * diffuse);
		brightColor = vec4(finalColor, textureColor.a);
		
		color.xyz = finalColor;
		color.a = textureColor.a;
	} else {
		color.xyz = diffuse * textureColor.xyz;
		color.a = textureColor.a;
	}
	color.xyz = vec3(shadow, shadow, shadow);
}