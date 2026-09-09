export interface FilterSettings {
  brightness: number;    // -1 to 1, default 0
  contrast: number;      // 0.5 to 2, default 1
  saturation: number;    // 0 to 2, default 1
  exposure: number;      // -2 to 2, default 0
  gamma: number;         // 0.5 to 2, default 1
  temperature: number;   // -1 to 1, default 0
  tint: number;          // -1 to 1, default 0
  vibrance: number;      // -1 to 1, default 0
  sharpness: number;     // 0 to 2, default 0
  vignette: number;      // 0 to 1, default 0
  filmGrain: number;     // 0 to 1, default 0
}

export interface PresetItem {
  id: string;
  name: string;
  settings: Partial<FilterSettings>;
}

export interface SubtitleTrack {
  id: string;
  language: string;
  label: string;
}

export interface InSightActor {
  name: string;
  role: string;
  avatar: string;
  bio: string;
}
