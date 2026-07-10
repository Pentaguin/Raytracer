import Slider from "./Slider";

type Props = {
    settings: any;
    updateSetting: (key: string, value: number) => void;
};

function RenderControls({ settings, updateSetting }: Props) {
    return (
        <>
            <h2>Render</h2>

            <Slider
                label="Width"
                value={settings.width}
                min={100}
                max={500}
                onChange={(v) =>
                    updateSetting("width", v)
                }
            />

            <Slider
                label="Height"
                value={settings.height}
                min={100}
                max={500}
                onChange={(v) =>
                    updateSetting("height", v)
                }
            />

            <Slider
                label="Samples per pixel"
                value={settings.samplesPerPixel}
                min={1}
                max={200}
                onChange={(v) =>
                    updateSetting("samplesPerPixel", v)
                }
            />

            <Slider
                label="Max depth"
                value={settings.maxDepth}
                min={1}
                max={50}
                onChange={(v) =>
                    updateSetting("maxDepth", v)
                }
            />

            <h2>Camera</h2>

            <Slider
                label="Camera X"
                value={settings.lookFromX}
                min={-20}
                max={20}
                step={0.1}
                onChange={(v) =>
                    updateSetting("lookFromX", v)
                }
            />

            <Slider
                label="Camera Y"
                value={settings.lookFromY}
                min={-20}
                max={20}
                step={0.1}
                onChange={(v) =>
                    updateSetting("lookFromY", v)
                }
            />

            <Slider
                label="Camera Z"
                value={settings.lookFromZ}
                min={-20}
                max={20}
                step={0.1}
                onChange={(v) =>
                    updateSetting("lookFromZ", v)
                }
            />

            <h2>Lens</h2>

            <Slider
                label="Focus distance"
                value={settings.focusDistance}
                min={0.1}
                max={50}
                step={0.1}
                onChange={(v) =>
                    updateSetting("focusDistance", v)
                }
            />

            <Slider
                label="Defocus angle"
                value={settings.defocusAngle}
                min={0}
                max={10}
                step={0.1}
                onChange={(v) =>
                    updateSetting("defocusAngle", v)
                }
            />
        </>
    );
}

export default RenderControls;