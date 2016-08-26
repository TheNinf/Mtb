#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normals;
layout (location = 2) in vec2 textCoords;
layout (location = 3) in vec3 tangent;
layout (location = 4) in vec3 bitangent;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 lightSpaceMatrix;

out vec2 textureCoords;
out vec3 fragmentPosition;
out vec3 toCameraVector;
out vec3 lightDirection;
out vec4 worldPositionLightSpace;
out vec3 normal;
out float shouldUseNormalMap;
out float visibility;

const float density = 0.006f;
const float gradient = 1.75f;
const float distance_render_normal_map = 65.0f;

void main(void){
	vec4 worldPosition = transformationMatrix * vec4(position, 1);
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	textureCoords = textCoords;

	float distance = length(positionRelativeToCamera.xyz);
	bool renderNormalMap = distance < distance_render_normal_map;
	if(!renderNormalMap) {
		toCameraVector = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
		lightDirection = vec3(0f, 0f, -0.8f);
		normal = (transformationMatrix *  vec4(normals, 0)).xyz;
	} else {
		normal = normalize((transformationMatrix * vec4(normals, 0)).xyz);
		vec3 tang = normalize((transformationMatrix * vec4(tangent, 0)).xyz);
		vec3 bitang = normalize((transformationMatrix * vec4(bitangent, 0)).xyz);
		
		mat3 toTangentSpace = mat3(
			tang.x, bitang.x, normal.x, 
			tang.y, bitang.y, normal.y, 
			tang.z, bitang.z, normal.z
		);

		toCameraVector = toTangentSpace * (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
		lightDirection = toTangentSpace * vec3(0f, 0f, -0.8f);
	}
	
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0f, 1.0f);
	
	shouldUseNormalMap = renderNormalMap ? 1.0f : 0.0f;
	fragmentPosition = positionRelativeToCamera.xyz;
	worldPositionLightSpace = lightSpaceMatrix * worldPosition;
}