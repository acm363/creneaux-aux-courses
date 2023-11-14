import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SlotHomePage from './pages/common/SlotHomePage';
import LoginPage from './pages/login/LoginPage';
import NotFoundPage from './pages/common/NotFoundPage';
import {
  ADMIN_PUBLIC_HOLIDAY_ROUTE,
  ADMIN_SLOT_ROUTE, CLIENT_RESERVATIONS_ROUTE,
  CLIENT_ROUTE,
  HOME_ROUTE,
  LOGIN_ROUTE,
  OTHER_ROUTE,
} from './routes/routes';
import ProtectedRoute from './components/ProtectedRoute';
import { UserType } from './dto/user/user.dto';
import ClientPage from './pages/client/ClientPage';
import './styles/App.scss';
import Footer from './components/common/Footer';
import SlotNavBar from './components/common/SlotNavBar';
import AdminSlotPage from './pages/admin/AdminSlotPage';
import AdminPublicHolidayPage from './pages/admin/AdminPublicHolidayPage';
import ClientReservationsPage from "./pages/client/ClientReservationsPage";
import {NotificationContainer} from "react-notifications";


const App: React.FC = () => {
  return (
    <BrowserRouter>
      <div className='App'>
        <SlotNavBar />
        <NotificationContainer/>
        <Routes>
          <Route path={HOME_ROUTE} element={<SlotHomePage />} />
          <Route path={LOGIN_ROUTE} element={<LoginPage />} />
          <Route
            path={ADMIN_SLOT_ROUTE}
            element={<ProtectedRoute allowedTypes={[UserType.ADMIN]}><AdminSlotPage /></ProtectedRoute>}
            errorElement={<NotFoundPage />}
          />
          <Route
            path={ADMIN_PUBLIC_HOLIDAY_ROUTE}
            element={<ProtectedRoute allowedTypes={[UserType.ADMIN]}><AdminPublicHolidayPage /></ProtectedRoute>}
            errorElement={<NotFoundPage />}
          />
          <Route
            path={CLIENT_ROUTE}
            element={<ProtectedRoute allowedTypes={[UserType.CLIENT]}><ClientPage /></ProtectedRoute>}
            errorElement={<NotFoundPage />}
          />
          <Route
            path={CLIENT_RESERVATIONS_ROUTE}
            element={<ProtectedRoute allowedTypes={[UserType.CLIENT]}><ClientReservationsPage /></ProtectedRoute>}
            errorElement={<NotFoundPage />}
          />
          <Route path={OTHER_ROUTE} element={<NotFoundPage />} />
        </Routes>
        <Footer />
      </div>
    </BrowserRouter>
  );
};

export default App;
