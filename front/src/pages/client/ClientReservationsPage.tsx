import React, { useState } from 'react';
import {UserSessionService} from '../../services';
import { UserDTO } from '../../dto/user/user.dto';
import { Container } from 'react-bootstrap';
import '../../styles/pages/user/ClientPage.scss';
import SlotPicker from "../../components/client/SlotPicker";

const ClientReservationsPage = () => {
  // Check if a pickUpOrder session exists
  const hasUserSession = UserSessionService.hasUserSession();

  // Get the pickUpOrder session data (if it exists)
  const [userDto] = useState<UserDTO | null>(UserSessionService.getUser());

  const onConfirm = () => {
    console.log('confirm');
  }

  return (
    <Container className='client-page'>
      <Container className='body, mt-5'>
        {hasUserSession && userDto && (
          <div>
            <SlotPicker userDto={userDto} onConfirm={onConfirm} allowCreation={false}/>
          </div>
        )}
      </Container>
    </Container>
  );
};

export default ClientReservationsPage;