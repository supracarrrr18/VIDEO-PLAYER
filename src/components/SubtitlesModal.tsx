import React, { useState } from "react";
import { X, Check, Subtitles, Volume2, Globe } from "lucide-react";
import { SubtitleTrack } from "../types";

interface SubtitlesModalProps {
  onClose: () => void;
  selectedSubtitle: string;
  onSelectSubtitle: (id: string) => void;
  selectedAudio: string;
  onSelectAudio: (id: string) => void;
}

export const SUBTITLE_TRACKS: SubtitleTrack[] = [
  { id: "off", language: "None", label: "Off" },
  { id: "en-cc", language: "English", label: "English [CC] (SDH)" },
  { id: "es", language: "Spanish", label: "Español (Latinoamérica)" },
  { id: "fr", language: "French", label: "Français (Canada)" },
  { id: "de", language: "German", label: "Deutsch" },
  { id: "ja", language: "Japanese", label: "日本語" },
  { id: "ko", language: "Korean", label: "한국어" },
];

export const AUDIO_TRACKS = [
  { id: "en-atmos", label: "English [Original] (Dolby Atmos 7.1)" },
  { id: "en-descriptive", label: "English (Audio Description)" },
  { id: "es-51", label: "Español (5.1 Surround)" },
  { id: "fr-51", label: "Français (5.1 Surround)" },
  { id: "de-51", label: "Deutsch (5.1 Surround)" },
  { id: "ja-51", label: "日本語 (5.1 Surround)" },
];

export const SubtitlesModal: React.FC<SubtitlesModalProps> = ({
  onClose,
  selectedSubtitle,
  onSelectSubtitle,
  selectedAudio,
  onSelectAudio,
}) => {
  const [tab, setTab] = useState<"subtitles" | "audio">("subtitles");
  const [syncOffset, setSyncOffset] = useState<number>(0);

  return (
    <div
      id="subtitles-modal-backdrop"
      className="absolute inset-0 bg-black/60 backdrop-blur-md z-50 flex items-center justify-center p-4 select-none pointer-events-auto"
    >
      <div
        id="subtitles-modal-card"
        className="w-full max-w-lg rounded-3xl backdrop-blur-3xl bg-black/80 border border-white/20 shadow-[0_20px_60px_rgba(0,0,0,0.8)] p-6 flex flex-col space-y-5"
      >
        {/* Header */}
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-cyan-400/20 border border-cyan-400/40 flex items-center justify-center text-cyan-300">
              <Subtitles size={20} />
            </div>
            <div>
              <h3 className="text-white font-bold text-lg">Audio & Subtitles</h3>
              <p className="text-white/50 text-xs">Multi-language streams & sync adjustment</p>
            </div>
          </div>
          <button
            id="btn-close-subtitles"
            onClick={onClose}
            className="p-2 text-white/70 hover:text-white rounded-full hover:bg-white/10 transition-all cursor-pointer"
          >
            <X size={18} />
          </button>
        </div>

        {/* Tab switch */}
        <div className="flex bg-white/[0.08] p-1 rounded-xl border border-white/10">
          <button
            onClick={() => setTab("subtitles")}
            className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all cursor-pointer ${
              tab === "subtitles"
                ? "bg-cyan-400 text-black shadow-[0_0_12px_rgba(6,182,212,0.4)]"
                : "text-white/70 hover:text-white"
            }`}
          >
            Subtitles
          </button>
          <button
            onClick={() => setTab("audio")}
            className={`flex-1 py-2 text-xs font-semibold rounded-lg transition-all cursor-pointer ${
              tab === "audio"
                ? "bg-cyan-400 text-black shadow-[0_0_12px_rgba(6,182,212,0.4)]"
                : "text-white/70 hover:text-white"
            }`}
          >
            Audio Tracks
          </button>
        </div>

        {/* Track List */}
        <div className="max-h-60 overflow-y-auto space-y-1.5 pr-1">
          {tab === "subtitles" ? (
            SUBTITLE_TRACKS.map((track) => {
              const active = selectedSubtitle === track.id;
              return (
                <button
                  key={track.id}
                  onClick={() => onSelectSubtitle(track.id)}
                  className={`w-full px-4 py-3 rounded-xl text-left flex items-center justify-between text-xs sm:text-sm font-medium transition-all cursor-pointer ${
                    active
                      ? "bg-cyan-400/20 border border-cyan-400/50 text-cyan-200"
                      : "bg-white/[0.04] hover:bg-white/[0.1] text-white/80 border border-transparent"
                  }`}
                >
                  <span>{track.label}</span>
                  {active && <Check size={16} className="text-cyan-400" />}
                </button>
              );
            })
          ) : (
            AUDIO_TRACKS.map((track) => {
              const active = selectedAudio === track.id;
              return (
                <button
                  key={track.id}
                  onClick={() => onSelectAudio(track.id)}
                  className={`w-full px-4 py-3 rounded-xl text-left flex items-center justify-between text-xs sm:text-sm font-medium transition-all cursor-pointer ${
                    active
                      ? "bg-cyan-400/20 border border-cyan-400/50 text-cyan-200"
                      : "bg-white/[0.04] hover:bg-white/[0.1] text-white/80 border border-transparent"
                  }`}
                >
                  <span>{track.label}</span>
                  {active && <Check size={16} className="text-cyan-400" />}
                </button>
              );
            })
          )}
        </div>

        {/* Subtitle timing offset adjuster */}
        {tab === "subtitles" && selectedSubtitle !== "off" && (
          <div className="pt-2 border-t border-white/10 flex items-center justify-between">
            <span className="text-xs text-white/60">Subtitle Sync Offset</span>
            <div className="flex items-center gap-2">
              <button
                onClick={() => setSyncOffset((prev) => Math.max(-2, Number((prev - 0.25).toFixed(2))))}
                className="px-2.5 py-1 rounded-lg bg-white/10 hover:bg-white/20 text-white text-xs font-mono"
              >
                -0.25s
              </button>
              <span className="text-xs font-mono text-cyan-300 w-16 text-center">
                {syncOffset > 0 ? `+${syncOffset.toFixed(2)}s` : `${syncOffset.toFixed(2)}s`}
              </span>
              <button
                onClick={() => setSyncOffset((prev) => Math.min(2, Number((prev + 0.25).toFixed(2))))}
                className="px-2.5 py-1 rounded-lg bg-white/10 hover:bg-white/20 text-white text-xs font-mono"
              >
                +0.25s
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
