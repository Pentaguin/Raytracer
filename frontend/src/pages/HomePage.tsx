import { useState } from "react";
import { useApiRequest } from "../hooks/useApiRequest";
import { defaultRenderSettings } from "../config/RenderDefaults";
import { glassScenePreset } from "../config/RenderPresets";
import RenderControls from "../components/RenderControls";

function HomePage() {
    const { renderImage, data, status } = useApiRequest();
    const [settings, setSettings] = useState({
       ...defaultRenderSettings,
    });

    const updateSetting = (
        key: keyof typeof settings,
        value: number
    ) => {
        setSettings(prev => ({
            ...prev,
            [key]: value
        }));
    };

    const resetSettings = () => {
        setSettings({
            ...defaultRenderSettings
        });
    };

    const loadGlassScene = () => {
        setSettings({
            ...glassScenePreset
        });
    };

    return (
        <>
            <h1>Ray Tracer</h1>

            <RenderControls
                settings={settings}
                updateSetting={updateSetting}
            />

            <button onClick={() => renderImage(settings)}>
                Render
            </button>

            <button onClick={loadGlassScene}>
                Glass Scene preset
            </button>

            <button onClick={resetSettings}>
                Reset
            </button>

            <p>{status}</p>

            {data && (
                <img
                    src={data}
                    alt="Rendered scene"
                />
            )}
        </>
    );
}

export default HomePage;