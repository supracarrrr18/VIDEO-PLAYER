import React from "react";
import {
  X,
  PictureInPicture2,
  Cast,
  Share2,
  Volume2,
  VolumeX,
  RotateCcw,
  RotateCw,
  Play,
  Pause,
  Subtitles,
  Sliders,
  Sparkles,
} from "lucide-react";

interface LiquidGlassControlsProps {
  isPlaying: boolean;
  onPlayPause: () => void;
  onRewind: () => void;
  onForward: () => void;
  volume: number;
  isMuted: boolean;
  onVolumeChange: (val: number) => void;
  onToggleMute: () => void;
  currentTime: number;
  duration: number;
  onSeek: (time: number) => void;
  onClose: () => void;
  onPip: () => void;
  onCast: () => void;
  onShare: () => void;
  onInfo: () => void;
  onInSight: () => void;
  onContinueWatching: () => void;
  onSubtitles: () => void;
  onAudioEq: () => void;
  onMoreFilters: () => void;
  title?: string;
  subtitle?: string;
  visible: boolean;
}

export const LiquidGlassControls: React.FC<LiquidGlassControlsProps> = ({
  isPlaying,
  onPlayPause,
  onRewind,
  onForward,
  volume,
  isMuted,
  onVolumeChange,
  onToggleMute,
  currentTime,
  duration,
  onSeek,
  onClose,
  onPip,
  onCast,
  onShare,
  onInfo,
  onInSight,
  onContinueWatching,
  onSubtitles,
  onAudioEq,
  onMoreFilters,
  title = "The Studio",
  subtitle = "The Promotion",
  visible,
}) => {
  const progressPercent = duration > 0 ? (currentTime / duration) * 100 : 0;

  return (
    <div
      id="liquid-glass-overlay"
      className={`absolute inset-0 pointer-events-none transition-opacity duration-300 flex flex-col justify-between p-6 sm:p-8 z-30 select-none ${
        visible ? "opacity-100" : "opacity-0"
      }`}
    >
      {/* TOP CONTROLS BAR */}
      <div id="top-controls-bar" className="flex items-center justify-between w-full pointer-events-auto">
        {/* TOP LEFT: [ Close ] [ PiP | Cast | Share ] */}
        <div className="flex items-center gap-3 sm:gap-4">
          {/* Circular Close Button */}
          <button
            id="btn-close-player"
            onClick={onClose}
            aria-label="Close"
            className="w-11 h-11 rounded-full flex items-center justify-center text-white/90 hover:text-white transition-all duration-200 cursor-pointer backdrop-blur-2xl bg-white/[0.12] border border-white/20 shadow-[0_8px_32px_rgba(0,0,0,0.37)] hover:bg-white/[0.22] hover:scale-105 active:scale-95"
          >
            <X size={18} strokeWidth={2.2} />
          </button>

          {/* Capsule Pill: PiP | Cast | Share */}
          <div
            id="capsule-tools"
            className="h-11 px-5 rounded-full flex items-center gap-5 sm:gap-6 text-white/90 backdrop-blur-2xl bg-white/[0.12] border border-white/20 shadow-[0_8px_32px_rgba(0,0,0,0.37)]"
          >
            <button
              id="btn-pip"
              onClick={onPip}
              title="Picture in Picture"
              className="hover:text-cyan-300 hover:scale-110 active:scale-95 transition-all cursor-pointer"
            >
              <PictureInPicture2 size={18} strokeWidth={2} />
            </button>
            <button
              id="btn-cast"
              onClick={onCast}
              title="Google Cast"
              className="hover:text-cyan-300 hover:scale-110 active:scale-95 transition-all cursor-pointer"
            >
              <Cast size={18} strokeWidth={2} />
            </button>
            <button
              id="btn-share"
              onClick={onShare}
              title="Share Video"
              className="hover:text-cyan-300 hover:scale-110 active:scale-95 transition-all cursor-pointer"
            >
              <Share2 size={18} strokeWidth={2} />
            </button>
          </div>
        </div>

        {/* TOP RIGHT: [ Volume Slider | Speaker ] Capsule */}
        <div
          id="capsule-volume"
          className="h-11 px-4 rounded-full flex items-center gap-3 text-white/90 backdrop-blur-2xl bg-white/[0.12] border border-white/20 shadow-[0_8px_32px_rgba(0,0,0,0.37)]"
        >
          <input
            id="slider-volume"
            type="range"
            min="0"
            max="1"
            step="0.01"
            value={isMuted ? 0 : volume}
            onChange={(e) => onVolumeChange(parseFloat(e.target.value))}
            className="w-16 sm:w-20 h-1.5 accent-white bg-white/30 rounded-lg appearance-none cursor-pointer"
          />
          <button
            id="btn-toggle-mute"
            onClick={onToggleMute}
            className="hover:text-cyan-300 hover:scale-110 active:scale-95 transition-all cursor-pointer"
          >
            {isMuted || volume === 0 ? <VolumeX size={18} /> : <Volume2 size={18} />}
          </button>
        </div>
      </div>

      {/* CENTER TRANSPORT CONTROLS: [ (10) ] [ ▶/|| ] [ (10) ] */}
      <div
        id="center-transport-controls"
        className="flex items-center justify-center gap-7 sm:gap-9 pointer-events-auto my-auto"
      >
        {/* Rewind 10s */}
        <button
          id="btn-rewind-10"
          onClick={onRewind}
          aria-label="Rewind 10 seconds"
          className="w-14 h-14 rounded-full flex flex-col items-center justify-center text-white/95 backdrop-blur-2xl bg-white/[0.14] border border-white/25 shadow-[0_8px_32px_rgba(0,0,0,0.4)] hover:bg-white/[0.24] hover:scale-105 active:scale-95 transition-all cursor-pointer relative group"
        >
          <RotateCcw size={22} strokeWidth={2.2} />
          <span className="text-[10px] font-bold absolute text-white top-[18px]">10</span>
        </button>

        {/* Center Large Glowing Play/Pause */}
        <button
          id="btn-center-play-pause"
          onClick={onPlayPause}
          aria-label={isPlaying ? "Pause" : "Play"}
          className="w-20 h-20 sm:w-22 sm:h-22 rounded-full flex items-center justify-center text-white backdrop-blur-3xl bg-white/[0.20] border-2 border-white/35 shadow-[0_0_40px_rgba(255,255,255,0.25),0_12px_40px_rgba(0,0,0,0.5)] hover:bg-white/[0.30] hover:scale-105 active:scale-95 transition-all cursor-pointer relative"
        >
          <div className="absolute inset-0 rounded-full bg-gradient-to-tr from-white/10 to-transparent pointer-events-none" />
          {isPlaying ? (
            <Pause size={34} strokeWidth={2.5} className="fill-white" />
          ) : (
            <Play size={36} strokeWidth={2.5} className="fill-white ml-1" />
          )}
        </button>

        {/* Forward 10s */}
        <button
          id="btn-forward-10"
          onClick={onForward}
          aria-label="Forward 10 seconds"
          className="w-14 h-14 rounded-full flex flex-col items-center justify-center text-white/95 backdrop-blur-2xl bg-white/[0.14] border border-white/25 shadow-[0_8px_32px_rgba(0,0,0,0.4)] hover:bg-white/[0.24] hover:scale-105 active:scale-95 transition-all cursor-pointer relative group"
        >
          <RotateCw size={22} strokeWidth={2.2} />
          <span className="text-[10px] font-bold absolute text-white top-[18px]">10</span>
        </button>
      </div>

      {/* BOTTOM TIMELINE & ACTIONS */}
      <div id="bottom-controls-bar" className="flex flex-col gap-3 pointer-events-auto w-full">
        {/* Title & Subtitle */}
        <div className="flex flex-col">
          <span id="video-subtitle-text" className="text-white/80 text-xs sm:text-sm font-medium tracking-wide">
            {subtitle}
          </span>
          <h2 id="video-title-text" className="text-white text-xl sm:text-2xl font-bold tracking-tight">
            {title}
          </h2>
        </div>

        {/* Glowing Progress Timeline */}
        <div
          id="timeline-container"
          onClick={(e) => {
            const rect = e.currentTarget.getBoundingClientRect();
            const clickPos = (e.clientX - rect.left) / rect.width;
            onSeek(clickPos * duration);
          }}
          className="w-full h-4 py-1.5 flex items-center cursor-pointer group"
        >
          <div className="w-full h-1 bg-white/25 rounded-full overflow-hidden relative group-hover:h-1.5 transition-all">
            <div
              id="timeline-progress-bar"
              style={{ width: `${progressPercent}%` }}
              className="h-full bg-white relative rounded-full shadow-[0_0_12px_rgba(255,255,255,0.8)]"
            />
          </div>
        </div>

        {/* Actions Row Below Timeline */}
        <div className="flex items-center justify-between w-full pt-1">
          {/* Left Action Pills */}
          <div className="flex items-center gap-2 sm:gap-3">
            <button
              id="btn-info"
              onClick={onInfo}
              className="h-9 px-4 rounded-full text-xs sm:text-sm font-medium text-white/95 backdrop-blur-2xl bg-white/[0.12] border border-white/20 shadow-[0_4px_20px_rgba(0,0,0,0.3)] hover:bg-white/[0.22] active:scale-95 transition-all cursor-pointer"
            >
              Info
            </button>
            <button
              id="btn-insight"
              onClick={onInSight}
              className="h-9 px-4 rounded-full text-xs sm:text-sm font-medium text-white/95 backdrop-blur-2xl bg-white/[0.12] border border-white/20 shadow-[0_4px_20px_rgba(0,0,0,0.3)] hover:bg-white/[0.22] active:scale-95 transition-all cursor-pointer"
            >
              InSight
            </button>
            <button
              id="btn-continue-watching"
              onClick={onContinueWatching}
              className="h-9 px-4 rounded-full text-xs sm:text-sm font-medium text-white/95 backdrop-blur-2xl bg-white/[0.12] border border-white/20 shadow-[0_4px_20px_rgba(0,0,0,0.3)] hover:bg-white/[0.22] active:scale-95 transition-all cursor-pointer hidden sm:block"
            >
              Continue Watching
            </button>
          </div>

          {/* Right Action Capsule */}
          <div
            id="capsule-actions"
            className="h-9 px-4 rounded-full flex items-center gap-4 text-white/90 backdrop-blur-2xl bg-white/[0.12] border border-white/20 shadow-[0_4px_20px_rgba(0,0,0,0.3)]"
          >
            <button
              id="btn-subtitles"
              onClick={onSubtitles}
              title="Subtitles & Audio Tracks"
              className="hover:text-cyan-300 hover:scale-110 active:scale-95 transition-all cursor-pointer"
            >
              <Subtitles size={16} />
            </button>
            <button
              id="btn-audio-eq"
              onClick={onAudioEq}
              title="10-Band Audio Equalizer"
              className="hover:text-cyan-300 hover:scale-110 active:scale-95 transition-all cursor-pointer"
            >
              <Sliders size={16} />
            </button>
            <button
              id="btn-gpu-filters"
              onClick={onMoreFilters}
              title="Real-Time GPU Color Grading"
              className="hover:text-cyan-300 hover:scale-110 active:scale-95 transition-all cursor-pointer"
            >
              <Sparkles size={16} />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
