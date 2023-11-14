import React from 'react';
import { Alert } from 'react-bootstrap';

interface ErrorComponentProps {
    message: string;
}

const ErrorComponent: React.FC<ErrorComponentProps> = ({ message }) => {
    return (
        <Alert className={"m-2"} variant="danger">
            {message}
        </Alert>
    );
};

export default ErrorComponent;
