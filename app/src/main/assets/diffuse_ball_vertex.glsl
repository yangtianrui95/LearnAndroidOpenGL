# version 300 es

in vec3 position;
out vec3 vPosition;
out vec4 vDiffuse;
uniform mat4 uMatrix;
uniform mat4 uTransformMatrix;
uniform vec3 uLocation;

// 散射光照计算
void pointLight(
        inout vec4 diffuse, // 散射光计算结果
        in vec3 lightLocation, // 光源位置
        in vec4 lightDiffuse // 散射光强度
        ) {
    vec3 newNormal = (uTransformMatrix * vec4(position, 1)).xyz;
    newNormal = normalize(newNormal);
    // 表面点到光源位置的距离
    vec3 vp = normalize(lightLocation - (uTransformMatrix * vec4(position, 1)).xyz);
    vp = normalize(vp);

    // 计算点乘结果
    float result = max(0.0, dot(newNormal, vp));
    diffuse = result * lightDiffuse;
}

void main() {
    gl_Position = uMatrix * vec4(position, 1.0);
    vPosition = position;

    // 计算光照
    vec4 diffuse = vec4(0.0, 0.0, 0.0, 0.0);
    pointLight(diffuse, uLocation, vec4(0.8, 0.8, 0.8, 1));
    vDiffuse = diffuse;
}


