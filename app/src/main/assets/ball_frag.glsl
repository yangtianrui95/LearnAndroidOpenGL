# version 300 es
precision mediump float;

uniform float uRadius;
in vec3 vPosition;
out vec4 fragcolor;

void main(){

    int count = 8;
    float span = 2.0 * uRadius / float(count);
    int x = int((vPosition.x + uRadius) / span);
    int y = int((vPosition.y + uRadius) / span);
    int z = int((vPosition.z + uRadius) / span);
    int which = int(mod(float(x+y+z), 2.0));
    if(which == 1){
        fragcolor = vec4(1.0,0,0,1.0);
    } else {
        fragcolor = vec4(1.0,1,1,1.0);
    }
}