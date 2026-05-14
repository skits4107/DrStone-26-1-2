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

float stepSize = 0.01;
float stepCount = 1000.0;

float sphere_sample(in vec3 p){
    float dist = length(p - sphereCenter);
    float distFromShell = abs(dist - sphereRadius); // 0 at shell surface
    float sharpness = .05; // higher = thinner shell
    float density = exp(-distFromShell * distFromShell * sharpness);
    return density;
}

void main() {
        vec2 ndc = (gl_FragCoord.xy / ScreenSize) * 2.0 - 1.0;
        vec4 clipPos = vec4(ndc, 1.0, 1.0);
        vec4 viewPos = inverse(ProjMat) * clipPos;
        viewPos /= viewPos.w;
        vec4 worldDir = inverse(ModelViewMat) * vec4(viewPos.xyz, 0.0);

        vec3 rayDir = worldDir.xyz;
        vec3 rayOrigin = vec3(CameraBlockPos) + CameraOffset;

        float rawDepth = texture(DepthSampler, gl_FragCoord.xy /ScreenSize).r;
        float near = ProjMat[3][2] / (ProjMat[2][2] - 1.0);
        float far  = ProjMat[3][2] / (ProjMat[2][2] + 1.0);
        float linearDepth = near * far / (far - rawDepth * (far - near));
        float visualDepth = linearDepth / 64.0;

        float total = 0.0;

        for (float i=0.0; i < stepSize*stepCount; i+=stepSize){
            total += sphere_sample(rayOrigin + rayDir*i);
        }

        total = clamp(total, 0.0, 1.0);

        fragColor = vec4(0.0, total, 0.0, 1.0);
}

