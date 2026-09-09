# Liquid Glass Video Player for Android

A next-generation, hardware-accelerated Android video player featuring an original **Liquid Glass** UI, real-time non-destructive GPU video color grading, Android Media3 / ExoPlayer playback core, and Room persistence.

## Architecture

```
UI (LiquidGlassPlayerScreen, Top/Center/Bottom Bars, GestureOverlay)
  ↓
PlayerViewModel (StateFlow & Coroutines)
  ↓
PlaybackController (Media3 ExoPlayer, MediaSession, SleepTimer)
  ↓
GlVideoRenderer (GLSurfaceView.Renderer, EGL External Texture)
  ↓
GlFilterEngine (OpenGL ES 2.0 / 3.0 Fragment Shaders)
  ↓
Display Output
```

## Features

- **Liquid Glass Interface**: High-translucency frosted capsules, circular glass buttons, ambient reflections, and smooth spring animations inspired directly by modern luxury glass design.
- **Real-Time GPU Video Filters**:
  - Color Grading Pipeline: Exposure → Brightness/Contrast → Highlights/Shadows → Gamma → Saturation/Vibrance → Temperature/Tint → Sharpness → Vignette → Film Grain.
  - Zero-latency shader uniform adjustments without playback interruption or reloading.
  - Presets: Original, Cinema, Vivid, Natural, Night, Film, Warm, Cool.
- **Universal Format Decoding**: MP4, MKV, WebM, MOV, AVI, TS, HLS, DASH, RTSP, etc.
- **Audio & Equalizer**: 10-Band Equalizer, Bass Boost, 3D Spatial Virtualizer, and custom delay sync.
- **Subtitles**: Embedded and external SRT, ASS, SSA, VTT with custom styles.
- **Gesture Controls**:
  - Left vertical swipe: Brightness
  - Right vertical swipe: Volume
  - Horizontal drag: High-precision scrubbing
  - Double tap left/right: 10s seek
  - Double tap center: Play/Pause
  - Long press: 2.0x playback speed
- **Picture-in-Picture & Background Audio**: Full lifecycle-safe PiP and foreground media service.

## Building the Project

```bash
# Debug APK
./gradlew assembleDebug

# Output APK path:
# app/build/outputs/apk/debug/app-debug.apk
```
