# version 300 es

in vec3 position;
uniform mat4 uMatrix;


void main(){
    gl_Position = uMatrix*vec4(position.x, position.y, position.z, 1.0);
}