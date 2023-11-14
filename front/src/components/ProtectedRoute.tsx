import React from 'react';
import { Navigate } from 'react-router-dom';
import { UserSessionService } from '../services';
import { UserType } from '../dto/user/user.dto';
import { LOGIN_ROUTE } from '../routes/routes';

interface ProtectedRouteProps {
    allowedTypes: UserType[]; // List of allowed client types
    children: React.ReactNode; // Correctly typed children prop
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
                                                           allowedTypes,
                                                           children,
                                                       }) => {
    const userDto = UserSessionService.getUser();

    if (!userDto) {
        // If the client is not logged in, redirect to the login page.
        return <Navigate to={LOGIN_ROUTE} />;
    }

    if (!allowedTypes.includes(userDto.type)) {
        // If the client is not authorized, redirect to the login page.
        return <Navigate to={LOGIN_ROUTE} />;
    }

    return <>{children}</>; // Wrap children in a fragment or use directly.
};

export default ProtectedRoute;
