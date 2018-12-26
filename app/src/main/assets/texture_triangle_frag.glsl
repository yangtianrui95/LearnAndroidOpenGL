#version 300 es

// 设置全局float类型参数
precision mediump float;
// 纹理采样单元
uniform sampler2D sTexture;
uniform sampler2D sTexture2;
in vec2 textureCoord;
out vec4 color;


void main() {
    // 进行纹理采样，两个纹理混合
    color = mix(texture(sTexture, textureCoord), texture(sTexture2, textureCoord), 0.2);
}