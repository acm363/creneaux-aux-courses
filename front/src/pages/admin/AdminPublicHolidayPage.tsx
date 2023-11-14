import {UserSessionService} from '../../services';
import React, { useState } from 'react';
import { UserDTO } from '../../dto/user/user.dto';
import PublicHolidayComponent from '../../components/admin/PublicHolidayComponent';
import '../../styles/pages/admin/AdminPublicHolidayPage.scss';
import { Container } from 'react-bootstrap';

const AdminPublicHolidayPage = () => {
  const hasUserSession = UserSessionService.hasUserSession();
  const [userDto] = useState<UserDTO | null>(UserSessionService.getUser());


  if (!hasUserSession || userDto == null) {
    return (
      <div>
        Aucune session trouvée.
      </div>
    );
  }


  return (
    <Container className='admin-holiday-page'>
      <h3 className={"title"}>Gestion des Jours Fériés</h3>
      <PublicHolidayComponent userDto={userDto} />
    </Container>
  );
};

export default AdminPublicHolidayPage;