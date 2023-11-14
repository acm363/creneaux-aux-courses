import React, { useState } from 'react';
import {UserSessionService} from '../../services';
import { Container } from 'react-bootstrap';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { UserDTO } from '../../dto/user/user.dto';
import CalendarComponent from '../../components/admin/calendar/CalendarComponent';
import '../../styles/pages/admin/AdminSlotPage.scss';
const AdminSlotPage = () => {
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
    <div>
      {hasUserSession && userDto && (
        <Container className="admin-slot-page">
          <h3 className={"title"}>Gestion des Créneaux</h3>
          <div className="body">
            <CalendarComponent userDto={userDto} />
          </div>
        </Container>
      )}
    </div>
  );
};

export default AdminSlotPage;

