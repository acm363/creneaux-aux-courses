import React, { useRef, useEffect } from 'react';
import { IconType } from 'react-icons';

import '../../styles/components/common/ContextMenu.scss';

interface ContextMenuItem {
    label: string;
    onClick: (event: any) => void;
    icon: IconType;
}

interface ContextMenuProps {
    items: ContextMenuItem[];
    isVisible: boolean;
    x: number;
    y: number;
    onClose: () => void;
}

const ContextMenu: React.FC<ContextMenuProps> = ({ items, isVisible, x, y, onClose }) => {
    const ref = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (ref.current && !ref.current.contains(event.target as Node)) {
                onClose();
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [onClose]);

    useEffect(() => {
        if (ref.current) {
            const menuWidth = ref.current.getBoundingClientRect().width;
            const menuHeight = ref.current.getBoundingClientRect().height;

            // Check for overflow on the right and adjust the x position
            if (x + menuWidth > window.innerWidth) {
                x -= menuWidth;
            }

            // Check for overflow on the bottom and adjust the y position
            if (y + menuHeight > window.innerHeight) {
                y -= menuHeight;
            }
        }
    }, [x, y]);

    if (!isVisible) return null;

    return (
      <div className="context-menu" style={{ top: y, left: x }} ref={ref}>
          {items.map((item, index) => (
            <div key={index} className="context-menu-item" onClick={item.onClick}>
                <item.icon className="context-menu-icon" />
                {item.label}
            </div>
          ))}
      </div>
    );
};

export default ContextMenu;
