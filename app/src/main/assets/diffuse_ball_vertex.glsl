# version 300 es

in vec3 position;
out vec3 vPosition;
out vec4 vDiffuse;
uniform mat4 uMatrix;
uniform vec3 uLocation;

// 散射光照计算
void pointLight(
        in vec3 normal, // 法向量
        inout vec4 diffuse, // 散射光计算结果
        in vec3 lightLocation, // 光源位置
        in vec4 lightDiffuse // 散射光强度
        ) {

    vec3 normalTarget = position + normal;
    vec3 newNormal = (uMatrix * vec4(normalTarget, 1)).xyz - (uMatrix * vec4(position, 1)).xyz;
    newNormal = normalize(normal);
    // 表面点到光源位置的距离
    vec3 vp = normalize(lightLocation - (uMatrix * vec4(position, 1)).xyz);
    vp = normalize(vp);

    // 计算点乘结果
    float result = max(0.0, dot(newNormal, vp));
    diffuse = result * lightDiffuse;
   // diffuse = vec4(newNormal, 1.0);
}

void main() {
    gl_Position = uMatrix * vec4(position, 1.0);
    vPosition = position;

    // 计算光照
    vec4 diffuse = vec4(0.0, 0.0, 0.0, 0.0);
    pointLight(normalize(position), diffuse, uLocation, vec4(0.8, 0.8, 0.8, 1));
    vDiffuse = diffuse;
}


