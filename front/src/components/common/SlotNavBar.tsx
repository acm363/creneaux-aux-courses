import { Container, Nav, Navbar } from 'react-bootstrap';
import { Button } from '@mui/material';
import React from 'react';
import {
  ADMIN_PUBLIC_HOLIDAY_ROUTE,
  ADMIN_SLOT_ROUTE, CLIENT_RESERVATIONS_ROUTE,
  CLIENT_ROUTE,
  HOME_ROUTE,
  LOGIN_ROUTE,
} from '../../routes/routes';
import { useNavigate } from 'react-router-dom';
import {UserSessionService,AuthService} from '../../services';


import '../../styles/components/common/SlotNavBar.scss';
import { UserType } from '../../dto/user/user.dto';

const MyNavbar: React.FC = () => {
  const navigate = useNavigate();

  // Check if a client session exists
  const hasUserSession = UserSessionService.hasUserSession();

  // Get the client session data (if it exists)
  const userDto = UserSessionService.getUser();

  const handleLogout = () => {
    // Add a timeout to delay the logout operation (e.g., 1 second)
    setTimeout(() => {
      // Remove the client session.
      AuthService.logout();
      // Redirect to the home page.
      navigate(HOME_ROUTE);
    }, 200);
  };

  const navigateToAdminPage = () => {
    navigate(ADMIN_SLOT_ROUTE);
  };

  const navigateToClientPage = () => {
    navigate(CLIENT_ROUTE);
  };

  const navigateToClientReservationsPage = () => {
    navigate(CLIENT_RESERVATIONS_ROUTE);
  }

  const navigateToHomePage = () => {
    navigate(HOME_ROUTE);
  };

  const navigateToLoginPage = () => {
    navigate(LOGIN_ROUTE);
  };

  const navigateToPublicHolidayPage = () => {
    navigate(ADMIN_PUBLIC_HOLIDAY_ROUTE);
  }


  const renderMainButtons = () => {
    if (hasUserSession) {
      return (
        <>
          <div>
            {userDto?.type === UserType.ADMIN ? (
              <Button variant="contained" onClick={navigateToAdminPage}>Gérer les créneaux</Button>
            ) : (<>
              <Button variant="contained" onClick={navigateToClientPage}>Mon panier</Button>
              <Button variant="contained" onClick={navigateToClientReservationsPage}>Mes réservations</Button>
              </>
            )
            }
          </div>
        </>
      );
    } else {
      // No client session, display login button
      return (
        <Button variant='contained' onClick={navigateToLoginPage}>Se connecter</Button>
      );
    }
  };

  const renderLogoutButton = () => {
    if (UserSessionService.hasUserSession()) {
      // If the client is logged in, display the logout button.
      return (
        <Button variant='contained' color="error" onClick={handleLogout}>
          Se déconnecter
        </Button>
      );
    }
    return null; // Else, don't display anything.
  };

  const renderPublicHolidayButton = () => {
    if (UserSessionService.hasUserSession() && userDto?.type === UserType.ADMIN) {
      return (
        <Button variant='contained' onClick={navigateToPublicHolidayPage}>
          Jours fériés
        </Button>
      );
    }
    return null; // Else, don't display anything.
  }

  return (
    <Navbar className='navbar'>
      <Container className='navbar-left'>
        <Navbar.Brand className='navbar-left-title'>Aux courses !</Navbar.Brand>
      </Container>
      <Container className='navbar-right'>
        <Nav className='mr-auto'>
          <div className='buttons-container'>
            <Button variant='contained' onClick={navigateToHomePage}>Accueil</Button>
          </div>
        </Nav>
        <Nav className='mr-auto'>
          <div className='buttons-container'>
            {renderMainButtons()} {/* Call the separate method for conditional rendering */}
          </div>
        </Nav>
        <Nav className='mr-auto'>
          <div className='buttons-container'>
            {renderPublicHolidayButton()} {/* Call the separate method for conditional rendering */}
          </div>
        </Nav>
        <Nav>
          <div className='buttons-container'>
            {renderLogoutButton()} {/* Affichez le bouton de déconnexion en fonction de la session utilisateur */}
          </div>
        </Nav>
      </Container>
    </Navbar>
  );
};

export default MyNavbar;
