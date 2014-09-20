#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_brightness;

void main() {
	gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	float a = gl_FragColor.a;
	gl_FragColor += u_brightness;
	gl_FragColor = clamp(gl_FragColor, 0.0, 1.0);
	gl_FragColor.a = a;
}
