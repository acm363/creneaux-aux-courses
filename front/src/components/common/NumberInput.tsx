import React, { useRef } from 'react';
import '../../styles/components/common/NumberInput.scss';

interface NumberInputProps {
	defaultValue: number,
	maxValue?: number,
	minValue?: number,
	onChange: (value: number) => void,
}

const NumberInput: React.FC<NumberInputProps> = ({defaultValue, maxValue, minValue, onChange}) => {
    const inputRef = useRef<HTMLInputElement>(null);

    const handleDecrease = () => {
        if (inputRef.current) {
            inputRef.current.stepDown();
        }
		onChange(Number(inputRef.current?.value));
    };

    const handleIncrease = () => {
        if (inputRef.current) {
            inputRef.current.stepUp();
        }
		onChange(Number(inputRef.current?.value));
    };

    return (
        <div className="number-input">
            <button onClick={() => handleDecrease()} className="minus">
                <i className="fas fa-minus"></i>  {/* Font Awesome minus icon */}
            </button>
            <input 
                className="quantity" 
                name="quantity" 
                defaultValue={defaultValue}
				onChange={() => onChange(Number(inputRef.current?.value))}
                type="number"
                ref={inputRef}
				max={maxValue}
				min={minValue}
            />
            <button onClick={() => handleIncrease()} className="plus">
                <i className="fas fa-plus"></i>  {/* Font Awesome plus icon */}
            </button>
        </div>
    );
}

export default NumberInput;
