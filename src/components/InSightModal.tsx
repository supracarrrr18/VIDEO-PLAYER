import React from "react";
import { X, Film, Award, Clapperboard, Sparkles } from "lucide-react";

interface InSightModalProps {
  onClose: () => void;
}

export const InSightModal: React.FC<InSightModalProps> = ({ onClose }) => {
  return (
    <div
      id="insight-modal"
      className="absolute inset-0 bg-black/60 backdrop-blur-md z-50 flex items-center justify-center p-4 select-none pointer-events-auto"
    >
      <div className="w-full max-w-lg rounded-3xl backdrop-blur-3xl bg-black/80 border border-white/20 shadow-[0_20px_60px_rgba(0,0,0,0.8)] p-6 flex flex-col space-y-5">
        {/* Header */}
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-cyan-400/20 border border-cyan-400/40 flex items-center justify-center text-cyan-300">
              <Sparkles size={20} />
            </div>
            <div>
              <h3 className="text-white font-bold text-lg">InSight</h3>
              <p className="text-white/50 text-xs">Scene Cast & Production Trivia</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-2 text-white/70 hover:text-white rounded-full hover:bg-white/10 transition-all cursor-pointer"
          >
            <X size={18} />
          </button>
        </div>

        {/* Current Scene Cast */}
        <div className="space-y-3">
          <span className="text-[11px] font-bold text-white/50 tracking-wider uppercase block">
            Featured In This Scene
          </span>
          <div className="flex items-center gap-3 p-3 rounded-2xl bg-white/[0.06] border border-white/10">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-tr from-cyan-500 to-indigo-600 flex items-center justify-center text-white font-bold text-lg">
              SR
            </div>
            <div className="flex-1">
              <h4 className="text-white font-bold text-sm">Seth Rogen</h4>
              <p className="text-cyan-300 text-xs">as Matt Remick (Studio Head)</p>
            </div>
            <span className="text-[10px] bg-white/10 px-2.5 py-1 rounded-full text-white/80 font-medium">
              Lead Actor
            </span>
          </div>

          <div className="flex items-center gap-3 p-3 rounded-2xl bg-white/[0.06] border border-white/10">
            <div className="w-12 h-12 rounded-xl bg-gradient-to-tr from-amber-500 to-rose-600 flex items-center justify-center text-white font-bold text-lg">
              CE
            </div>
            <div className="flex-1">
              <h4 className="text-white font-bold text-sm">Catherine O'Hara</h4>
              <p className="text-cyan-300 text-xs">as Patty (Board Member)</p>
            </div>
            <span className="text-[10px] bg-white/10 px-2.5 py-1 rounded-full text-white/80 font-medium">
              Co-Star
            </span>
          </div>
        </div>

        {/* Scene Details */}
        <div className="space-y-2">
          <span className="text-[11px] font-bold text-white/50 tracking-wider uppercase block">
            Behind The Scene
          </span>
          <p className="text-white/80 text-xs leading-relaxed bg-white/[0.04] p-3 rounded-xl border border-white/5">
            Shot on Arri Alexa Mini LF with Cooke Anamorphic/i Full Frame Plus lenses. Color timed in ACES Rec.2020 PQ HDR for true liquid black levels.
          </p>
        </div>
      </div>
    </div>
  );
};
