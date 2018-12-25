# version 300 es

in vec3 position;
// 顶点纹理坐标
in vec2 aTextureCoord;
out vec2 textureCoord;

void main() {
    gl_Position = vec4(position.x, position.y, position.z, 1.0);
    textureCoord = aTextureCoord;
}