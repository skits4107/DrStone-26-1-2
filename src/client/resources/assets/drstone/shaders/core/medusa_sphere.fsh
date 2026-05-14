#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:projection.glsl>

layout(std140) uniform Sphere {
    vec3 sphereCenter;
    float sphereRadius;
};

uniform sampler2D DepthSampler;

out vec4 fragColor;

void main() {
    float rawDepth = texture(DepthSampler, gl_FragCoord.xy /ScreenSize).r;
    float near = ProjMat[3][2] / (ProjMat[2][2] - 1.0);
    float far  = ProjMat[3][2] / (ProjMat[2][2] + 1.0);
    float linearDepth = near * far / (far - rawDepth * (far - near));
    float normalizedDepth = ((linearDepth - near) / (far - near))  * 10.0;
    fragColor = vec4(normalizedDepth, normalizedDepth, normalizedDepth, 1.0);
}
