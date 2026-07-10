import { defaultRenderSettings } from "../config/RenderDefaults";

export const glassScenePreset = {
    ...defaultRenderSettings,
    vfov: 20,
    samplesPerPixel: 100,
    lookFromX: 13,
    lookFromY: 2,
    lookFromZ: 3
};

// TODO example preset