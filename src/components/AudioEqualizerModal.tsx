import React, { useState } from "react";
import { X, Volume2, Waves } from "lucide-react";

interface AudioEqualizerModalProps {
  onClose: () => void;
}

const EQ_FREQUENCIES = ["31Hz", "62Hz", "125Hz", "250Hz", "500Hz", "1kHz", "2kHz", "4kHz", "8kHz", "16kHz"];

export const AudioEqualizerModal: React.FC<AudioEqualizerModalProps> = ({ onClose }) => {
  const [enabled, setEnabled] = useState(true);
  const [bassBoost, setBassBoost] = useState(40);
  const [virtualizer, setVirtualizer] = useState(30);
  const [gains, setGains] = useState<number[]>([2, 4, 3, 0, -1, 1, 3, 5, 4, 2]);

  const updateGain = (index: number, val: number) => {
    const next = [...gains];
    next[index] = val;
    setGains(next);
  };

  return (
    <div
      id="audio-equalizer-modal"
      className="absolute inset-0 bg-black/60 backdrop-blur-md z-50 flex items-center justify-center p-4 select-none pointer-events-auto"
    >
      <div className="w-full max-w-xl rounded-3xl backdrop-blur-3xl bg-black/75 border border-white/20 shadow-[0_20px_60px_rgba(0,0,0,0.8)] p-6 flex flex-col space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-cyan-400/20 border border-cyan-400/40 flex items-center justify-center text-cyan-300">
              <Waves size={20} />
            </div>
            <div>
              <h3 className="text-white font-bold text-lg">10-Band Graphic Equalizer</h3>
              <p className="text-white/50 text-xs">Dolby Atmos & Spatial Audio Pipeline</p>
            </div>
          </div>
          <div className="flex items-center gap-3">
            <button
              onClick={() => setEnabled(!enabled)}
              className={`px-3 py-1.5 rounded-full text-xs font-semibold cursor-pointer transition-all ${
                enabled
                  ? "bg-cyan-400 text-black shadow-[0_0_12px_rgba(6,182,212,0.4)]"
                  : "bg-white/10 text-white/50"
              }`}
            >
              {enabled ? "ENABLED" : "BYPASSED"}
            </button>
            <button
              onClick={onClose}
              className="p-2 text-white/70 hover:text-white rounded-full hover:bg-white/10 transition-all cursor-pointer"
            >
              <X size={18} />
            </button>
          </div>
        </div>

        {/* 10 Equalizer Sliders */}
        <div className="grid grid-cols-10 gap-2 h-44 items-center px-2">
          {EQ_FREQUENCIES.map((freq, idx) => (
            <div key={freq} className="flex flex-col items-center h-full justify-between">
              <span className="text-[10px] font-mono text-cyan-300">{gains[idx] > 0 ? `+${gains[idx]}` : gains[idx]}dB</span>
              <div className="relative flex-1 flex items-center justify-center w-full my-2">
                <input
                  type="range"
                  min="-12"
                  max="12"
                  step="1"
                  value={gains[idx]}
                  onChange={(e) => updateGain(idx, parseInt(e.target.value))}
                  disabled={!enabled}
                  className="h-28 -rotate-90 accent-cyan-400 bg-white/20 rounded-lg appearance-none cursor-pointer w-28 disabled:opacity-30"
                />
              </div>
              <span className="text-[9px] font-mono text-white/50 text-center">{freq}</span>
            </div>
          ))}
        </div>

        {/* Bass Boost & 3D Spatializer */}
        <div className="grid grid-cols-2 gap-4 pt-2 border-t border-white/10">
          <div className="bg-white/[0.06] rounded-2xl p-4 space-y-2 border border-white/10">
            <div className="flex justify-between text-xs">
              <span className="text-white font-medium">Bass Boost</span>
              <span className="text-cyan-300 font-bold">{bassBoost}%</span>
            </div>
            <input
              type="range"
              min="0"
              max="100"
              value={bassBoost}
              onChange={(e) => setBassBoost(parseInt(e.target.value))}
              disabled={!enabled}
              className="w-full h-1.5 accent-cyan-400 bg-white/20 rounded-lg cursor-pointer"
            />
          </div>

          <div className="bg-white/[0.06] rounded-2xl p-4 space-y-2 border border-white/10">
            <div className="flex justify-between text-xs">
              <span className="text-white font-medium">3D Spatializer</span>
              <span className="text-cyan-300 font-bold">{virtualizer}%</span>
            </div>
            <input
              type="range"
              min="0"
              max="100"
              value={virtualizer}
              onChange={(e) => setVirtualizer(parseInt(e.target.value))}
              disabled={!enabled}
              className="w-full h-1.5 accent-cyan-400 bg-white/20 rounded-lg cursor-pointer"
            />
          </div>
        </div>
      </div>
    </div>
  );
};
