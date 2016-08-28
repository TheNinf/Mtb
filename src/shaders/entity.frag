#version 400 core

layout (location = 0) out vec4 color;
layout (location = 1) out vec4 brightColor;

in vec2 textureCoords;
in vec3 fragmentPosition;
in vec3 toCameraVector;
in vec3 lightDirection;
in vec4 worldPositionLightSpace;
in vec3 normal;
in float shouldUseNormalMap;
in float visibility;

uniform sampler2D samplerTexture;
uniform sampler2D specularTexture;
uniform sampler2D normalMap;
uniform sampler2DShadow shadowMap;
uniform float shininess;

const float minDiffuse = 0.4f;

float shadowCalculation(vec4 posLightSpace){
	vec3 projCoords = posLightSpace.xyz / 2.0f + 0.5f;
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
		color = brightColor = vec4(0);
		return;
	}
	
	vec3 unitNormal = shouldUseNormalMap == 0.0f ? normalize(normal) :
		normalize((2.0f * texture(normalMap, textureCoords) - 1.0f).rgb); 

	float shadow = shadowCalculation(worldPositionLightSpace);
	float diffuse = max(dot(unitNormal, -lightDirection) * shadow, minDiffuse);

	bool hasSpecular = shininess > 0.0f;
	float specularFactor = hasSpecular ? 
		texture(specularTexture, textureCoords).r : 0.0f;
	if(hasSpecular && specularFactor > 0.0f) {
		vec3 unitCamVector = normalize(toCameraVector - fragmentPosition);
		vec3 halfwayDirection = normalize(-lightDirection + unitCamVector);
		float specular = pow(dot(unitNormal, halfwayDirection), shininess) * specularFactor; 

		vec3 finalColor = (textureColor.rgb + specular) * diffuse;
		color = vec4(finalColor, textureColor.a);
		brightColor = diffuse > minDiffuse ? color * visibility : vec4(0);
	} else {
		brightColor = vec4(0);
		color.rgb = diffuse * textureColor.rgb;
		color.a = textureColor.a;
	}		
	color = mix(vec4(0.0f, 0.4f, 0.7f, 1.0f), color, visibility);
}