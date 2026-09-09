import React, { useState, useRef, useEffect } from "react";
import { LiquidGlassControls } from "./components/LiquidGlassControls";
import { FilterDrawer, PRESETS } from "./components/FilterDrawer";
import { AudioEqualizerModal } from "./components/AudioEqualizerModal";
import { InSightModal } from "./components/InSightModal";
import { InfoModal } from "./components/InfoModal";
import { SubtitlesModal } from "./components/SubtitlesModal";
import { AndroidProjectModal } from "./components/AndroidProjectModal";
import { FilterSettings, PresetItem } from "./types";
import {
  Upload,
  Link as LinkIcon,
  Smartphone,
  Maximize,
  Minimize,
  Film,
  CheckCircle2,
  Sliders,
  Sparkles,
} from "lucide-react";

interface VideoSample {
  id: string;
  title: string;
  subtitle: string;
  url: string;
}

const SAMPLE_VIDEOS: VideoSample[] = [
  {
    id: "the-studio",
    title: "The Studio",
    subtitle: "The Promotion (Episode 1)",
    url: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
  },
  {
    id: "tears-of-steel",
    title: "Tears of Steel",
    subtitle: "Sci-Fi VFX Benchmark",
    url: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
  },
  {
    id: "sintel",
    title: "Sintel",
    subtitle: "Fantasy 4K Master",
    url: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
  },
  {
    id: "elephants-dream",
    title: "Elephant's Dream",
    subtitle: "3D Animation HDR",
    url: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
  },
];

export default function App() {
  const videoRef = useRef<HTMLVideoElement>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  // Playback state
  const [currentVideo, setCurrentVideo] = useState<VideoSample>(SAMPLE_VIDEOS[0]);
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  const [currentTime, setCurrentTime] = useState<number>(0);
  const [duration, setDuration] = useState<number>(0);
  const [volume, setVolume] = useState<number>(0.85);
  const [isMuted, setIsMuted] = useState<boolean>(false);
  const [isFullscreen, setIsFullscreen] = useState<boolean>(false);

  // Controls visibility & auto-hide
  const [controlsVisible, setControlsVisible] = useState<boolean>(true);
  const hideTimerRef = useRef<NodeJS.Timeout | null>(null);

  // Modals & Panels
  const [showFilterDrawer, setShowFilterDrawer] = useState<boolean>(false);
  const [showAudioEq, setShowAudioEq] = useState<boolean>(false);
  const [showInSight, setShowInSight] = useState<boolean>(false);
  const [showInfo, setShowInfo] = useState<boolean>(false);
  const [showSubtitles, setShowSubtitles] = useState<boolean>(false);
  const [showAndroidModal, setShowAndroidModal] = useState<boolean>(false);
  const [showStreamPicker, setShowStreamPicker] = useState<boolean>(false);

  // Filter grading state
  const [activePreset, setActivePreset] = useState<string>("cinema");
  const [filters, setFilters] = useState<FilterSettings>({
    brightness: 0,
    contrast: 1.15,
    saturation: 1.1,
    exposure: 0,
    gamma: 1,
    temperature: 0.15,
    tint: -0.1,
    vibrance: 0,
    sharpness: 0.4,
    vignette: 0.25,
    filmGrain: 0.1,
  });

  // Subtitles & Audio
  const [selectedSubtitle, setSelectedSubtitle] = useState<string>("en-cc");
  const [selectedAudio, setSelectedAudio] = useState<string>("en-atmos");

  // Notification Toast
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  // Auto-hide controls handler
  const resetHideTimer = () => {
    setControlsVisible(true);
    if (hideTimerRef.current) clearTimeout(hideTimerRef.current);
    if (isPlaying) {
      hideTimerRef.current = setTimeout(() => {
        if (!showFilterDrawer && !showAudioEq && !showInSight && !showInfo && !showSubtitles && !showAndroidModal) {
          setControlsVisible(false);
        }
      }, 3500);
    }
  };

  // Play/Pause
  const togglePlayPause = () => {
    if (!videoRef.current) return;
    if (videoRef.current.paused) {
      videoRef.current.play();
      setIsPlaying(true);
    } else {
      videoRef.current.pause();
      setIsPlaying(false);
    }
    resetHideTimer();
  };

  // Seek
  const handleSeek = (targetTime: number) => {
    if (!videoRef.current) return;
    const clamped = Math.max(0, Math.min(targetTime, duration));
    videoRef.current.currentTime = clamped;
    setCurrentTime(clamped);
    resetHideTimer();
  };

  const handleRewind = () => {
    handleSeek(currentTime - 10);
    showToast("Rewind 10s");
  };

  const handleForward = () => {
    handleSeek(currentTime + 10);
    showToast("Forward 10s");
  };

  // Volume
  const handleVolumeChange = (newVal: number) => {
    setVolume(newVal);
    if (videoRef.current) {
      videoRef.current.volume = newVal;
      if (newVal > 0 && isMuted) {
        setIsMuted(false);
        videoRef.current.muted = false;
      }
    }
  };

  const toggleMute = () => {
    if (!videoRef.current) return;
    const nextMuted = !isMuted;
    setIsMuted(nextMuted);
    videoRef.current.muted = nextMuted;
  };

  // PiP
  const handlePip = async () => {
    if (!videoRef.current) return;
    try {
      if (document.pictureInPictureElement) {
        await document.exitPictureInPicture();
        showToast("Exited Picture-in-Picture");
      } else if (document.pictureInPictureEnabled) {
        await videoRef.current.requestPictureInPicture();
        showToast("Entered Picture-in-Picture");
      }
    } catch (err) {
      showToast("Picture-in-Picture not supported in this window");
    }
  };

  // Cast
  const handleCast = () => {
    showToast("Scanning for Google Cast & AirPlay targets...");
  };

  // Share
  const handleShare = () => {
    if (navigator.clipboard) {
      navigator.clipboard.writeText(window.location.href);
      showToast("Video link copied to clipboard");
    }
  };

  // Fullscreen
  const toggleFullscreen = () => {
    if (!containerRef.current) return;
    if (!document.fullscreenElement) {
      containerRef.current.requestFullscreen().catch(() => {});
      setIsFullscreen(true);
    } else {
      document.exitFullscreen().catch(() => {});
      setIsFullscreen(false);
    }
  };

  // Preset Selection
  const handleSelectPreset = (preset: PresetItem) => {
    setActivePreset(preset.id);
    setFilters({
      brightness: 0,
      contrast: 1,
      saturation: 1,
      exposure: 0,
      gamma: 1,
      temperature: 0,
      tint: 0,
      vibrance: 0,
      sharpness: 0,
      vignette: 0,
      filmGrain: 0,
      ...preset.settings,
    });
    showToast(`LUT applied: ${preset.name}`);
  };

  // Custom File Upload
  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const blobUrl = URL.createObjectURL(file);
      setCurrentVideo({
        id: "local-file",
        title: file.name.replace(/\.[^/.]+$/, ""),
        subtitle: "Local Media File",
        url: blobUrl,
      });
      showToast(`Loaded: ${file.name}`);
      setTimeout(() => {
        videoRef.current?.play();
        setIsPlaying(true);
      }, 300);
    }
  };

  // Keyboard Shortcuts
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.target instanceof HTMLInputElement) return;
      switch (e.key.toLowerCase()) {
        case " ":
        case "k":
          e.preventDefault();
          togglePlayPause();
          break;
        case "arrowleft":
        case "j":
          e.preventDefault();
          handleRewind();
          break;
        case "arrowright":
        case "l":
          e.preventDefault();
          handleForward();
          break;
        case "m":
          e.preventDefault();
          toggleMute();
          break;
        case "f":
          e.preventDefault();
          toggleFullscreen();
          break;
        case "c":
          setShowSubtitles((p) => !p);
          break;
        case "e":
          setShowAudioEq((p) => !p);
          break;
        case "g":
          setShowFilterDrawer((p) => !p);
          break;
      }
    };
    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [isPlaying, currentTime, duration, isMuted]);

  // Video Time Update
  const handleTimeUpdate = () => {
    if (videoRef.current) {
      setCurrentTime(videoRef.current.currentTime);
    }
  };

  const handleLoadedMetadata = () => {
    if (videoRef.current) {
      setDuration(videoRef.current.duration);
      videoRef.current.volume = volume;
    }
  };

  // Construct CSS Filter string for GPU Shader pipeline
  const cssFilter = `
    brightness(${1 + filters.brightness})
    contrast(${filters.contrast})
    saturate(${filters.saturation})
    hue-rotate(${filters.temperature * 30 + filters.tint * 25}deg)
    sepia(${Math.max(0, filters.temperature * 0.3)})
  `;

  return (
    <div
      id="liquid-glass-app-root"
      ref={containerRef}
      onMouseMove={resetHideTimer}
      onClick={resetHideTimer}
      className="relative w-screen h-screen bg-black overflow-hidden font-sans text-white select-none flex flex-col items-center justify-center"
    >
      {/* Hidden File Input for Custom Videos */}
      <input
        type="file"
        ref={fileInputRef}
        onChange={handleFileUpload}
        accept="video/*"
        className="hidden"
      />

      {/* Persistent Top Utility Bar (Subtle, glassmorphic pill) */}
      <div
        id="top-quick-bar"
        className={`absolute top-4 z-40 flex items-center gap-2 sm:gap-3 transition-opacity duration-300 pointer-events-auto ${
          controlsVisible ? "opacity-100" : "opacity-0 pointer-events-none"
        }`}
      >
        <button
          onClick={() => setShowStreamPicker(!showStreamPicker)}
          className="h-8 px-3.5 rounded-full text-xs font-semibold backdrop-blur-2xl bg-white/[0.12] border border-white/20 text-white/90 hover:bg-white/[0.22] hover:text-white transition-all flex items-center gap-1.5 cursor-pointer shadow-lg"
        >
          <Film size={13} />
          <span>Switch Stream</span>
        </button>

        <button
          onClick={() => fileInputRef.current?.click()}
          className="h-8 px-3.5 rounded-full text-xs font-semibold backdrop-blur-2xl bg-white/[0.12] border border-white/20 text-white/90 hover:bg-white/[0.22] hover:text-white transition-all flex items-center gap-1.5 cursor-pointer shadow-lg"
        >
          <Upload size={13} />
          <span>Load Local File</span>
        </button>

        <button
          onClick={() => setShowAndroidModal(true)}
          className="h-8 px-3.5 rounded-full text-xs font-semibold backdrop-blur-2xl bg-emerald-500/20 border border-emerald-500/40 text-emerald-300 hover:bg-emerald-500/30 transition-all flex items-center gap-1.5 cursor-pointer shadow-lg"
        >
          <Smartphone size={13} />
          <span className="hidden sm:inline">Android Media3 Architecture</span>
          <span className="sm:hidden">Android Code</span>
        </button>

        <button
          onClick={toggleFullscreen}
          title="Toggle Fullscreen (F)"
          className="w-8 h-8 rounded-full backdrop-blur-2xl bg-white/[0.12] border border-white/20 text-white/90 hover:bg-white/[0.22] flex items-center justify-center transition-all cursor-pointer shadow-lg"
        >
          {isFullscreen ? <Minimize size={13} /> : <Maximize size={13} />}
        </button>
      </div>

      {/* Stream Selector Dropdown */}
      {showStreamPicker && (
        <div
          id="stream-picker-dropdown"
          className="absolute top-16 z-50 p-2 rounded-2xl backdrop-blur-3xl bg-black/85 border border-white/20 shadow-2xl space-y-1 w-72 pointer-events-auto"
        >
          <span className="text-[10px] font-bold text-white/40 uppercase tracking-wider px-3 py-1 block">
            Select 4K Sample Stream
          </span>
          {SAMPLE_VIDEOS.map((item) => (
            <button
              key={item.id}
              onClick={() => {
                setCurrentVideo(item);
                setShowStreamPicker(false);
                showToast(`Loaded: ${item.title}`);
                setTimeout(() => {
                  videoRef.current?.play();
                  setIsPlaying(true);
                }, 300);
              }}
              className={`w-full px-3 py-2 rounded-xl text-left text-xs transition-all flex items-center justify-between cursor-pointer ${
                currentVideo.id === item.id
                  ? "bg-cyan-400/25 text-cyan-200 font-semibold"
                  : "text-white/80 hover:bg-white/10"
              }`}
            >
              <div className="truncate">
                <p className="truncate">{item.title}</p>
                <p className="text-[10px] text-white/50">{item.subtitle}</p>
              </div>
              {currentVideo.id === item.id && <CheckCircle2 size={14} className="text-cyan-400 shrink-0" />}
            </button>
          ))}
        </div>
      )}

      {/* Video Canvas Container with GPU Filter Pipeline */}
      <div id="video-display-stage" className="relative w-full h-full flex items-center justify-center overflow-hidden">
        <video
          ref={videoRef}
          src={currentVideo.url}
          onTimeUpdate={handleTimeUpdate}
          onLoadedMetadata={handleLoadedMetadata}
          onEnded={() => setIsPlaying(false)}
          onClick={togglePlayPause}
          playsInline
          style={{
            filter: cssFilter,
            transform: filters.sharpness > 0 ? `scale(${1 + filters.sharpness * 0.005})` : undefined,
          }}
          className="w-full h-full object-contain cursor-pointer transition-[filter] duration-150"
        />

        {/* Dynamic Vignette GPU Shader Overlay */}
        {filters.vignette > 0 && (
          <div
            id="vignette-layer"
            style={{
              background: `radial-gradient(ellipse at center, rgba(0,0,0,0) 35%, rgba(0,0,0,${filters.vignette}) 100%)`,
            }}
            className="absolute inset-0 pointer-events-none transition-all duration-200"
          />
        )}

        {/* Film Grain Texture Simulation */}
        {filters.filmGrain > 0 && (
          <div
            id="film-grain-layer"
            style={{ opacity: filters.filmGrain }}
            className="absolute inset-0 pointer-events-none mix-blend-overlay bg-[radial-gradient(#fff_1px,transparent_1px)] [background-size:16px_16px] animate-pulse"
          />
        )}

        {/* Dynamic Subtitle Display */}
        {selectedSubtitle !== "off" && (
          <div
            id="subtitles-display-box"
            className="absolute bottom-24 sm:bottom-28 pointer-events-none z-20 px-6 max-w-2xl text-center"
          >
            <span className="inline-block px-4 py-1.5 rounded-lg bg-black/75 backdrop-blur-md text-white font-medium text-sm sm:text-base tracking-wide border border-white/10 shadow-lg">
              {currentTime < 4
                ? "Continental Studios is facing unprecedented quarterly turbulence."
                : currentTime < 8
                ? "We need bold pictures that redefine cinema, not just safe algorithms."
                : currentTime < 14
                ? "Let's commit the greenlight right here, right now."
                : "The production schedule starts tomorrow morning."}
            </span>
          </div>
        )}
      </div>

      {/* Liquid Glass UI Controls Overlay */}
      <LiquidGlassControls
        isPlaying={isPlaying}
        onPlayPause={togglePlayPause}
        onRewind={handleRewind}
        onForward={handleForward}
        volume={volume}
        isMuted={isMuted}
        onVolumeChange={handleVolumeChange}
        onToggleMute={toggleMute}
        currentTime={currentTime}
        duration={duration}
        onSeek={handleSeek}
        onClose={() => showToast("Exiting player session")}
        onPip={handlePip}
        onCast={handleCast}
        onShare={handleShare}
        onInfo={() => setShowInfo(true)}
        onInSight={() => setShowInSight(true)}
        onContinueWatching={() => showToast("Next Episode queued")}
        onSubtitles={() => setShowSubtitles(true)}
        onAudioEq={() => setShowAudioEq(true)}
        onMoreFilters={() => setShowFilterDrawer(true)}
        title={currentVideo.title}
        subtitle={currentVideo.subtitle}
        visible={controlsVisible}
      />

      {/* GPU Filter Color Grading Drawer */}
      {showFilterDrawer && (
        <FilterDrawer
          filters={filters}
          onChange={setFilters}
          onClose={() => setShowFilterDrawer(false)}
          activePreset={activePreset}
          onSelectPreset={handleSelectPreset}
        />
      )}

      {/* 10-Band Graphic Audio Equalizer Modal */}
      {showAudioEq && <AudioEqualizerModal onClose={() => setShowAudioEq(false)} />}

      {/* InSight Scene Cast & Trivia Modal */}
      {showInSight && <InSightModal onClose={() => setShowInSight(false)} />}

      {/* Stream Info & Codec Modal */}
      {showInfo && (
        <InfoModal
          title={currentVideo.title}
          subtitle={currentVideo.subtitle}
          onClose={() => setShowInfo(false)}
        />
      )}

      {/* Subtitles & Audio Selector Modal */}
      {showSubtitles && (
        <SubtitlesModal
          selectedSubtitle={selectedSubtitle}
          onSelectSubtitle={(id) => {
            setSelectedSubtitle(id);
            showToast(`Subtitles: ${id.toUpperCase()}`);
          }}
          selectedAudio={selectedAudio}
          onSelectAudio={(id) => {
            setSelectedAudio(id);
            showToast(`Audio Track updated`);
          }}
          onClose={() => setShowSubtitles(false)}
        />
      )}

      {/* Android Native Architecture & Zip Export Modal */}
      {showAndroidModal && <AndroidProjectModal onClose={() => setShowAndroidModal(false)} />}

      {/* Toast Notification */}
      {toastMessage && (
        <div
          id="toast-notification"
          className="absolute bottom-8 z-50 px-5 py-2.5 rounded-full backdrop-blur-2xl bg-black/80 border border-white/20 text-white text-xs font-medium shadow-2xl flex items-center gap-2 animate-fade-in pointer-events-none"
        >
          <span className="w-2 h-2 rounded-full bg-cyan-400 animate-ping" />
          <span>{toastMessage}</span>
        </div>
      )}
    </div>
  );
}
