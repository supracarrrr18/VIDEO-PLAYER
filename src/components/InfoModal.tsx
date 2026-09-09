import React from "react";
import { X, Film, Info, Disc, Tv, Layers, Calendar, Clock, Monitor } from "lucide-react";

interface InfoModalProps {
  onClose: () => void;
  title: string;
  subtitle: string;
}

export const InfoModal: React.FC<InfoModalProps> = ({
  onClose,
  title,
  subtitle,
}) => {
  return (
    <div
      id="info-modal-backdrop"
      className="absolute inset-0 bg-black/60 backdrop-blur-md z-50 flex items-center justify-center p-4 select-none pointer-events-auto"
    >
      <div
        id="info-modal-card"
        className="w-full max-w-xl rounded-3xl backdrop-blur-3xl bg-black/80 border border-white/20 shadow-[0_20px_60px_rgba(0,0,0,0.8)] p-6 sm:p-7 flex flex-col space-y-6"
      >
        {/* Header */}
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-cyan-400/20 border border-cyan-400/40 flex items-center justify-center text-cyan-300">
              <Info size={20} />
            </div>
            <div>
              <h3 className="text-white font-bold text-lg">{title}</h3>
              <p className="text-white/50 text-xs">{subtitle} • Episode 1</p>
            </div>
          </div>
          <button
            id="btn-close-info"
            onClick={onClose}
            className="p-2 text-white/70 hover:text-white rounded-full hover:bg-white/10 transition-all cursor-pointer"
          >
            <X size={18} />
          </button>
        </div>

        {/* Badges */}
        <div className="flex flex-wrap items-center gap-2">
          <span className="px-3 py-1 rounded-lg bg-cyan-500/20 text-cyan-300 border border-cyan-500/30 text-xs font-semibold">
            4K ULTRA HD
          </span>
          <span className="px-3 py-1 rounded-lg bg-purple-500/20 text-purple-300 border border-purple-500/30 text-xs font-semibold">
            DOLBY VISION
          </span>
          <span className="px-3 py-1 rounded-lg bg-amber-500/20 text-amber-300 border border-amber-500/30 text-xs font-semibold">
            DOLBY ATMOS
          </span>
          <span className="px-3 py-1 rounded-lg bg-white/10 text-white/80 border border-white/15 text-xs font-semibold">
            HDR10+
          </span>
          <span className="px-3 py-1 rounded-lg bg-white/10 text-white/80 border border-white/15 text-xs font-semibold">
            60 FPS
          </span>
        </div>

        {/* Synopsis */}
        <div className="space-y-1.5">
          <span className="text-[11px] font-bold text-white/50 tracking-wider uppercase block">
            Synopsis
          </span>
          <p className="text-white/85 text-xs sm:text-sm leading-relaxed bg-white/[0.04] p-3.5 rounded-2xl border border-white/5">
            Matt Remick is newly appointed head of Continental Studios, a legacy film company fighting for relevance in a streaming-dominated Hollywood. When creative compromise clashes with corporate budgets, Matt must make a decisive choice that could jeopardize his entire slate.
          </p>
        </div>

        {/* Stream Metrics Table */}
        <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
          <div className="p-3 rounded-xl bg-white/[0.05] border border-white/10 flex flex-col">
            <span className="text-[10px] text-white/40 uppercase font-semibold">Video Codec</span>
            <span className="text-white font-mono text-xs font-medium mt-0.5">HEVC (H.265 Main 10)</span>
          </div>
          <div className="p-3 rounded-xl bg-white/[0.05] border border-white/10 flex flex-col">
            <span className="text-[10px] text-white/40 uppercase font-semibold">Audio Channel</span>
            <span className="text-white font-mono text-xs font-medium mt-0.5">E-AC-3 JOC (Spatial 7.1)</span>
          </div>
          <div className="p-3 rounded-xl bg-white/[0.05] border border-white/10 flex flex-col">
            <span className="text-[10px] text-white/40 uppercase font-semibold">Native Bitrate</span>
            <span className="text-white font-mono text-xs font-medium mt-0.5">24.8 Mbps (CBR)</span>
          </div>
          <div className="p-3 rounded-xl bg-white/[0.05] border border-white/10 flex flex-col">
            <span className="text-[10px] text-white/40 uppercase font-semibold">Aspect Ratio</span>
            <span className="text-white font-mono text-xs font-medium mt-0.5">2.39:1 (Cinemascope)</span>
          </div>
          <div className="p-3 rounded-xl bg-white/[0.05] border border-white/10 flex flex-col">
            <span className="text-[10px] text-white/40 uppercase font-semibold">Color Primaries</span>
            <span className="text-white font-mono text-xs font-medium mt-0.5">BT.2020 PQ (ST 2084)</span>
          </div>
          <div className="p-3 rounded-xl bg-white/[0.05] border border-white/10 flex flex-col">
            <span className="text-[10px] text-white/40 uppercase font-semibold">Hardware Acceleration</span>
            <span className="text-cyan-300 font-mono text-xs font-medium mt-0.5">Vulkan MediaCodec GPU</span>
          </div>
        </div>
      </div>
    </div>
  );
};
