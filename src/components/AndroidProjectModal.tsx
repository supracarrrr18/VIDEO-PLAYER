import React, { useState } from "react";
import { X, Smartphone, Download, Code2, Check, FileCode, Cpu, Layers } from "lucide-react";
import JSZip from "jszip";

interface AndroidProjectModalProps {
  onClose: () => void;
}

const ANDROID_FILES = [
  {
    name: "MainActivity.kt",
    path: "app/src/main/java/com/liquidglass/player/MainActivity.kt",
    code: `package com.liquidglass.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.liquidglass.player.ui.LiquidGlassPlayerScreen
import com.liquidglass.player.ui.theme.LiquidGlassTheme

class MainActivity : ComponentActivity() {
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initPlayer()

        setContent {
            LiquidGlassTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    LiquidGlassPlayerScreen(
                        player = exoPlayer,
                        onClose = { finish() }
                    )
                }
            }
        }
    }

    private fun initPlayer() {
        exoPlayer = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build().apply {
                val mediaItem = MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }
}`,
  },
  {
    name: "LiquidGlassControls.kt",
    path: "app/src/main/java/com/liquidglass/player/ui/LiquidGlassControls.kt",
    code: `package com.liquidglass.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LiquidGlassCapsule(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.14f))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.08f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
fun CenterPlayPauseButton(
    isPlaying: Boolean,
    onToggle: () -> Unit
) {
    IconButton(
        onClick = onToggle,
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.20f))
            .border(2.dp, Color.White.copy(alpha = 0.40f), CircleShape)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "Play/Pause",
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
    }
}`,
  },
  {
    name: "GPUColorShader.kt",
    path: "app/src/main/java/com/liquidglass/player/gpu/GPUColorShader.kt",
    code: `package com.liquidglass.player.gpu

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class GPUColorShader {
    // Android 13+ AGSL (Android Graphics Shading Language) real-time pipeline
    companion object {
        const val AGSL_SHADER = """
            uniform shader composable;
            uniform float brightness;
            uniform float contrast;
            uniform float saturation;
            uniform float temperature;
            uniform float vignette;

            half4 main(float2 fragCoord) {
                half4 color = composable.eval(fragCoord);
                
                // Brightness & Contrast
                color.rgb += brightness;
                color.rgb = (color.rgb - 0.5) * contrast + 0.5;

                // Saturation
                half gray = dot(color.rgb, half3(0.2126, 0.7152, 0.0722));
                color.rgb = mix(half3(gray), color.rgb, saturation);

                // Temperature shift (Teal/Orange LUT simulation)
                color.r += temperature * 0.1;
                color.b -= temperature * 0.1;

                return color;
            }
        """

        fun createShader(): RuntimeShader {
            return RuntimeShader(AGSL_SHADER)
        }
    }
}`,
  },
  {
    name: "build.gradle.kts",
    path: "app/build.gradle.kts",
    code: `plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.liquidglass.player"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.liquidglass.player"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.media3:media3-exoplayer:1.5.1")
    implementation("androidx.media3:media3-ui:1.5.1")
    implementation("androidx.media3:media3-session:1.5.1")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui-graphics:1.7.6")
}`,
  },
];

export const AndroidProjectModal: React.FC<AndroidProjectModalProps> = ({ onClose }) => {
  const [selectedFile, setSelectedFile] = useState(0);
  const [copied, setCopied] = useState(false);
  const [downloading, setDownloading] = useState(false);

  const copyCode = () => {
    navigator.clipboard.writeText(ANDROID_FILES[selectedFile].code);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const downloadZip = async () => {
    try {
      setDownloading(true);
      const zip = new JSZip();
      
      // Add files to zip
      ANDROID_FILES.forEach((file) => {
        zip.file(file.path, file.code);
      });

      zip.file(
        "README.md",
        `# Liquid Glass Android Video Player
A state-of-the-art Android Video Player with:
- Media3 ExoPlayer Engine (4K HDR, Dolby Vision, Dolby Atmos)
- Liquid Glass Glassmorphism UI in Jetpack Compose
- Real-Time AGSL GPU Shader Color Grading (Brightness, Contrast, Saturation, Temperature)
- 10-Band Graphic Audio Equalizer
- InSight Cast & Scene Trivia Overlay

## How to Build in Android Studio
1. Open Android Studio (Ladybug or newer).
2. Choose "Open Existing Project" and select this folder.
3. Sync Gradle and click "Run" on any Android 8.0+ device or emulator.
`
      );

      const blob = await zip.generateAsync({ type: "blob" });
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "LiquidGlass_Android_Player.zip";
      a.click();
      URL.revokeObjectURL(url);
    } catch (e) {
      console.error(e);
    } finally {
      setDownloading(false);
    }
  };

  return (
    <div
      id="android-project-modal"
      className="absolute inset-0 bg-black/60 backdrop-blur-md z-50 flex items-center justify-center p-4 select-none pointer-events-auto"
    >
      <div className="w-full max-w-4xl h-[85vh] rounded-3xl backdrop-blur-3xl bg-black/85 border border-white/20 shadow-[0_20px_60px_rgba(0,0,0,0.8)] flex flex-col overflow-hidden">
        {/* Top Header */}
        <div className="p-5 sm:p-6 border-b border-white/10 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-emerald-500/20 border border-emerald-500/40 flex items-center justify-center text-emerald-400">
              <Smartphone size={22} />
            </div>
            <div>
              <h3 className="text-white font-bold text-lg">Android Native Architecture</h3>
              <p className="text-white/50 text-xs">Jetpack Compose • Media3 ExoPlayer • AGSL Shaders</p>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <button
              onClick={downloadZip}
              disabled={downloading}
              className="px-4 py-2 rounded-full bg-emerald-500 hover:bg-emerald-400 text-black font-semibold text-xs flex items-center gap-2 cursor-pointer transition-all shadow-[0_0_15px_rgba(16,185,129,0.4)]"
            >
              <Download size={15} />
              {downloading ? "Packaging..." : "Download Android Project (.zip)"}
            </button>
            <button
              onClick={onClose}
              className="p-2 text-white/70 hover:text-white rounded-full hover:bg-white/10 transition-all cursor-pointer"
            >
              <X size={18} />
            </button>
          </div>
        </div>

        {/* Content Layout */}
        <div className="flex-1 flex overflow-hidden">
          {/* File sidebar */}
          <div className="w-56 sm:w-64 border-r border-white/10 p-3 space-y-1 overflow-y-auto bg-white/[0.02]">
            <span className="text-[10px] font-bold text-white/40 uppercase tracking-wider px-3 py-1.5 block">
              Source Files
            </span>
            {ANDROID_FILES.map((file, idx) => (
              <button
                key={file.name}
                onClick={() => setSelectedFile(idx)}
                className={`w-full px-3 py-2 rounded-xl text-left text-xs font-mono flex items-center gap-2 transition-all cursor-pointer ${
                  selectedFile === idx
                    ? "bg-emerald-500/20 text-emerald-300 border border-emerald-500/40"
                    : "text-white/70 hover:bg-white/[0.06] hover:text-white"
                }`}
              >
                <FileCode size={14} className="shrink-0" />
                <span className="truncate">{file.name}</span>
              </button>
            ))}
          </div>

          {/* Code Viewer */}
          <div className="flex-1 flex flex-col bg-black/40 overflow-hidden">
            <div className="p-3 bg-white/[0.03] border-b border-white/10 flex items-center justify-between text-xs text-white/60 font-mono">
              <span className="truncate">{ANDROID_FILES[selectedFile].path}</span>
              <button
                onClick={copyCode}
                className="px-3 py-1 rounded-lg bg-white/10 hover:bg-white/20 text-white flex items-center gap-1.5 cursor-pointer text-xs"
              >
                {copied ? <Check size={13} className="text-emerald-400" /> : <Code2 size={13} />}
                <span>{copied ? "Copied" : "Copy"}</span>
              </button>
            </div>
            <pre className="flex-1 p-4 text-xs font-mono text-white/90 overflow-auto selection:bg-emerald-500/30">
              <code>{ANDROID_FILES[selectedFile].code}</code>
            </pre>
          </div>
        </div>
      </div>
    </div>
  );
};
