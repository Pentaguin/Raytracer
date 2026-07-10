type SliderProps = {
    label: string;
    value: number;
    min: number;
    max: number;
    step?: number;
    onChange: (value: number) => void;
};

function Slider({
                    label,
                    value,
                    min,
                    max,
                    step = 1,
                    onChange
                }: SliderProps) {
    return (
        <div>
            <label>
                {label}: {value}
            </label>

            <input
                type="range"
                min={min}
                max={max}
                step={step}
                value={value}
                onChange={(e) =>
                    onChange(Number(e.target.value))
                }
            />
        </div>
    );
}

export default Slider;