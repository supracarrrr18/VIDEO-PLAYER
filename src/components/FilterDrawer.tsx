import React from "react";
import { FilterSettings, PresetItem } from "../types";
import { X, RotateCcw } from "lucide-react";

interface FilterDrawerProps {
  filters: FilterSettings;
  onChange: (filters: FilterSettings) => void;
  onClose: () => void;
  activePreset: string;
  onSelectPreset: (preset: PresetItem) => void;
}

export const PRESETS: PresetItem[] = [
  { id: "original", name: "Original", settings: { brightness: 0, contrast: 1, saturation: 1, exposure: 0, gamma: 1, temperature: 0, tint: 0, sharpness: 0, vignette: 0, filmGrain: 0 } },
  { id: "cinema", name: "Cinema Teal/Orange", settings: { contrast: 1.15, saturation: 1.1, temperature: 0.15, tint: -0.1, vignette: 0.25, sharpness: 0.4 } },
  { id: "vivid", name: "Vivid HDR", settings: { contrast: 1.25, saturation: 1.4, brightness: 0.05, sharpness: 0.6 } },
  { id: "natural", name: "Natural Warm", settings: { temperature: 0.25, contrast: 1.05, saturation: 1.05 } },
  { id: "night", name: "Night Vision", settings: { brightness: 0.35, gamma: 1.3, contrast: 0.9, exposure: 0.5 } },
  { id: "film", name: "35mm Grain", settings: { contrast: 1.1, filmGrain: 0.35, vignette: 0.3, saturation: 0.95 } },
  { id: "cool", name: "Cyber Cool", settings: { temperature: -0.3, tint: 0.15, contrast: 1.2, saturation: 1.2 } },
];

export const FilterDrawer: React.FC<FilterDrawerProps> = ({
  filters,
  onChange,
  onClose,
  activePreset,
  onSelectPreset,
}) => {
  const updateField = (key: keyof FilterSettings, value: number) => {
    onChange({ ...filters, [key]: value });
  };

  const resetAll = () => {
    onSelectPreset(PRESETS[0]);
  };

  return (
    <div
      id="gpu-filter-drawer"
      className="absolute top-0 right-0 bottom-0 w-80 sm:w-96 z-40 p-4 sm:p-6 flex flex-col pointer-events-auto select-none"
    >
      <div className="w-full h-full rounded-3xl backdrop-blur-3xl bg-black/60 border border-white/20 shadow-[0_16px_50px_rgba(0,0,0,0.8)] flex flex-col overflow-hidden">
        {/* Header */}
        <div className="p-5 border-b border-white/10 flex items-center justify-between">
          <div>
            <h3 className="text-white font-bold text-base sm:text-lg tracking-tight">
              GPU Color Grading
            </h3>
            <p className="text-cyan-400 text-xs font-medium">Real-Time Shader Pipeline</p>
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={resetAll}
              title="Reset Filters"
              className="p-2 text-white/70 hover:text-white rounded-full hover:bg-white/10 transition-all cursor-pointer"
            >
              <RotateCcw size={16} />
            </button>
            <button
              onClick={onClose}
              className="p-2 text-white/70 hover:text-white rounded-full hover:bg-white/10 transition-all cursor-pointer"
            >
              <X size={18} />
            </button>
          </div>
        </div>

        {/* Body content */}
        <div className="flex-1 overflow-y-auto p-5 space-y-6">
          {/* Presets */}
          <div>
            <span className="text-[11px] font-bold text-white/50 tracking-wider uppercase block mb-3">
              Cinematic LUT Presets
            </span>
            <div className="grid grid-cols-2 gap-2">
              {PRESETS.map((preset) => {
                const isSelected = activePreset === preset.id;
                return (
                  <button
                    key={preset.id}
                    onClick={() => onSelectPreset(preset)}
                    className={`py-2 px-3 rounded-xl text-xs font-medium transition-all text-left truncate cursor-pointer ${
                      isSelected
                        ? "bg-cyan-400 text-black font-semibold shadow-[0_0_15px_rgba(6,182,212,0.5)]"
                        : "bg-white/[0.08] text-white/80 hover:bg-white/[0.16] hover:text-white"
                    }`}
                  >
                    {preset.name}
                  </button>
                );
              })}
            </div>
          </div>

          {/* Sliders */}
          <div className="space-y-4">
            <span className="text-[11px] font-bold text-white/50 tracking-wider uppercase block">
              Grading Parameters
            </span>

            <SliderRow
              label="Brightness"
              value={filters.brightness}
              min={-0.5}
              max={0.5}
              step={0.01}
              onChange={(v) => updateField("brightness", v)}
            />

            <SliderRow
              label="Contrast"
              value={filters.contrast}
              min={0.5}
              max={2.0}
              step={0.05}
              onChange={(v) => updateField("contrast", v)}
            />

            <SliderRow
              label="Saturation"
              value={filters.saturation}
              min={0}
              max={2.5}
              step={0.05}
              onChange={(v) => updateField("saturation", v)}
            />

            <SliderRow
              label="Exposure"
              value={filters.exposure}
              min={-1.5}
              max={1.5}
              step={0.05}
              onChange={(v) => updateField("exposure", v)}
            />

            <SliderRow
              label="Temperature (Warm/Cool)"
              value={filters.temperature}
              min={-0.6}
              max={0.6}
              step={0.02}
              onChange={(v) => updateField("temperature", v)}
            />

            <SliderRow
              label="Tint (Magenta/Green)"
              value={filters.tint}
              min={-0.5}
              max={0.5}
              step={0.02}
              onChange={(v) => updateField("tint", v)}
            />

            <SliderRow
              label="Vignette"
              value={filters.vignette}
              min={0}
              max={0.8}
              step={0.02}
              onChange={(v) => updateField("vignette", v)}
            />

            <SliderRow
              label="Film Grain"
              value={filters.filmGrain}
              min={0}
              max={0.6}
              step={0.02}
              onChange={(v) => updateField("filmGrain", v)}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

const SliderRow: React.FC<{
  label: string;
  value: number;
  min: number;
  max: number;
  step: number;
  onChange: (val: number) => void;
}> = ({ label, value, min, max, step, onChange }) => (
  <div className="space-y-1.5">
    <div className="flex justify-between text-xs">
      <span className="text-white/80">{label}</span>
      <span className="text-cyan-300 font-mono">{value.toFixed(2)}</span>
    </div>
    <input
      type="range"
      min={min}
      max={max}
      step={step}
      value={value}
      onChange={(e) => onChange(parseFloat(e.target.value))}
      className="w-full h-1.5 accent-cyan-400 bg-white/20 rounded-lg appearance-none cursor-pointer"
    />
  </div>
);
