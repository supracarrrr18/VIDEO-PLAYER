#extension GL_OES_EGL_image_external : require
precision mediump float;

varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;

// Filter uniforms
uniform float uBrightness;      // -1.0 to 1.0 (default 0.0)
uniform float uContrast;        // 0.0 to 2.0 (default 1.0)
uniform float uSaturation;      // 0.0 to 2.0 (default 1.0)
uniform float uExposure;        // -2.0 to 2.0 (default 0.0)
uniform float uGamma;           // 0.2 to 3.0 (default 1.0)
uniform float uTemperature;     // -1.0 to 1.0 (default 0.0)
uniform float uTint;            // -1.0 to 1.0 (default 0.0)
uniform float uHue;             // 0.0 to 360.0 (default 0.0)
uniform float uVibrance;        // -1.0 to 1.0 (default 0.0)
uniform float uHighlights;      // 0.0 to 2.0 (default 1.0)
uniform float uShadows;         // 0.0 to 2.0 (default 1.0)
uniform float uSharpness;       // 0.0 to 2.0 (default 0.0)
uniform float uVignette;        // 0.0 to 1.0 (default 0.0)
uniform float uFilmGrain;       // 0.0 to 1.0 (default 0.0)
uniform float uTexWidth;        // texture pixel width
uniform float uTexHeight;       // texture pixel height
uniform float uTime;            // time for animated grain

// Helper functions for color transformation
vec3 rgbToHsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsvToRgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

float random(vec2 p) {
    return fract(sin(dot(p + uTime, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec2 tc = vTextureCoord;
    vec4 rawColor = texture2D(sTexture, tc);
    vec3 color = rawColor.rgb;

    // 1. Sharpness kernel (unsharp mask)
    if (uSharpness > 0.01) {
        float stepX = 1.0 / max(uTexWidth, 1.0);
        float stepY = 1.0 / max(uTexHeight, 1.0);
        vec3 n = texture2D(sTexture, tc + vec2(0.0, -stepY)).rgb;
        vec3 s = texture2D(sTexture, tc + vec2(0.0, stepY)).rgb;
        vec3 e = texture2D(sTexture, tc + vec2(stepX, 0.0)).rgb;
        vec3 w = texture2D(sTexture, tc + vec2(-stepX, 0.0)).rgb;
        vec3 laplacian = (color * 4.0) - (n + s + e + w);
        color = clamp(color + laplacian * uSharpness, 0.0, 1.0);
    }

    // 2. Exposure
    if (uExposure != 0.0) {
        color *= pow(2.0, uExposure);
    }

    // 3. Brightness & Contrast
    color += uBrightness;
    color = (color - 0.5) * uContrast + 0.5;

    // 4. Highlights and Shadows
    float luminance = dot(color, vec3(0.2126, 0.7152, 0.0722));
    float shadowWeight = 1.0 - smoothstep(0.0, 0.5, luminance);
    float highlightWeight = smoothstep(0.5, 1.0, luminance);
    color = mix(color, color * uShadows, shadowWeight);
    color = mix(color, color * uHighlights, highlightWeight);

    // 5. Gamma correction
    color = clamp(color, 0.0, 1.0);
    color = pow(color, vec3(1.0 / max(uGamma, 0.01)));

    // 6. Saturation and Vibrance
    float lum = dot(color, vec3(0.2126, 0.7152, 0.0722));
    color = mix(vec3(lum), color, uSaturation);

    if (uVibrance != 0.0) {
        float maxVal = max(color.r, max(color.g, color.b));
        float minVal = min(color.r, min(color.g, color.b));
        float sat = (maxVal - minVal) / max(maxVal, 0.001);
        float vibranceFactor = (1.0 - sat) * uVibrance;
        color = mix(vec3(lum), color, 1.0 + vibranceFactor);
    }

    // 7. Temperature & Tint
    if (uTemperature != 0.0) {
        color.r += uTemperature * 0.15;
        color.b -= uTemperature * 0.15;
    }
    if (uTint != 0.0) {
        color.g += uTint * 0.15;
    }

    // 8. Hue adjustment
    if (uHue > 0.1) {
        vec3 hsv = rgbToHsv(color);
        hsv.x = fract(hsv.x + uHue / 360.0);
        color = hsvToRgb(hsv);
    }

    // 9. Vignette
    if (uVignette > 0.01) {
        vec2 uv = tc - 0.5;
        float dist = length(uv);
        float vignette = 1.0 - smoothstep(0.4, 0.75, dist) * uVignette;
        color *= vignette;
    }

    // 10. Film grain
    if (uFilmGrain > 0.01) {
        float noise = (random(tc) - 0.5) * uFilmGrain * 0.2;
        color += noise;
    }

    gl_FragColor = vec4(clamp(color, 0.0, 1.0), rawColor.a);
}
