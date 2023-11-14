import React, { useState } from 'react';
import {UserSessionService} from '../../services';
import { UserDTO } from '../../dto/user/user.dto';
import { Container } from 'react-bootstrap';
import {Button} from "@mui/material";
import '../../styles/pages/user/ClientPage.scss';
import SlotPicker from "../../components/client/SlotPicker";
import {ClientCart} from "../../components/client/ClientCart";
import {VscArrowLeft} from "react-icons/vsc";
import {useNavigate} from "react-router-dom";
import {HOME_ROUTE} from "../../routes/routes";


const ClientPage = () => {
  // Check if a pickUpOrder session exists
  const hasUserSession = UserSessionService.hasUserSession();

  // Get the pickUpOrder session data (if it exists)
  const [userDto] = useState<UserDTO | null>(UserSessionService.getUser());
  const [showReservation, setShowReservation] = useState(false);
  const navigate = useNavigate();

  const onConfirm = () => {
    navigate(HOME_ROUTE);
  }

  const renderContent = () => {
    if (userDto && showReservation) {
      return ( <SlotPicker userDto={userDto} onConfirm={onConfirm} allowCreation={true}/> );
    }
    return ( <ClientCart onConfirm={() => setShowReservation(true)}/>);
  }

  return (
    <Container className='client-page'>
      <Container className='body, mt-5'>
        { showReservation && <Button variant="outlined" startIcon={<VscArrowLeft />} onClick={() => setShowReservation(false)}>Panier</Button>}
        {hasUserSession && userDto && (
          <div>
            {renderContent()}
          </div>
        )}
      </Container>
    </Container>
  );
};

export default ClientPage;