import React, { useEffect, useState } from 'react';
import { UserDTO } from '../../dto/user/user.dto';
import {PublicHolidayService} from '../../services';
import HolidayListComponent from './PublicHolidayListComponent';
import { NotificationContainer, NotificationManager } from 'react-notifications';
import { PublicHolidayRequestDTO, PublicHolidayResponseDTO } from '../../dto/publicHoliday/public.holiday.dto';
import EditPublicHolidayModalComponent from './EditPublicHolidayModalComponent';
import { Button } from 'react-bootstrap';
import CreatePublicHolidayModalComponent from './CreatePublicHolidayModalComponent';
import '../../styles/components/admin/PublicHolidayComponent.scss';

interface PublicHolidayComponentProps {
  userDto: UserDTO;
}

const PublicHolidayComponent: React.FC<PublicHolidayComponentProps> = ({ userDto }) => {
  const publicHolidayRequestHandlerService = new PublicHolidayService(userDto);
  const [holidays, setHolidays] = useState<PublicHolidayResponseDTO[]>([] as PublicHolidayResponseDTO[]);
  const [currentHoliday, setCurrentHoliday] = useState<PublicHolidayResponseDTO | null>(null);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const createNotification = (type: String, msg: String) => {
    switch (type) {
      case 'info':
        return NotificationManager.info(msg);
      case 'success':
        return NotificationManager.success(msg, 'Succès', 3000);
      case 'warning':
        return NotificationManager.warning(msg, 'Attention', 3000);
      case 'error':
        return NotificationManager.error(msg, 'Erreur', 5000);
    }
  };

  useEffect(() => {
    const fetchHolidays = async () => {
      try {
        const fetchedHolidays = await publicHolidayRequestHandlerService.getPublicHolidays();
        setHolidays(fetchedHolidays);
      } catch (error) {
        createNotification('error', 'Erreur lors du chargement des jours fériés');
      }
    };

    fetchHolidays().then();
  }, []);

  const setCurrentHolidayById = (publicId: string) => {
    const selectedHoliday = holidays.find((holiday) => holiday.publicId === publicId);
    if (selectedHoliday) {
      setCurrentHoliday(selectedHoliday);
    }
  };

  const onAddEvent = () => {
    setShowCreateModal(true);
  };

  const onUpdateEvent = async (publicId: string) => {
    // Mise à jour de `currentHoliday` lorsque l'utilisateur clique sur "Modifier"
    setCurrentHolidayById(publicId);

    setShowEditModal(true);
  };

  const onCreate = async (newHoliday: PublicHolidayRequestDTO) => {
    try {
      const createdHoliday = await publicHolidayRequestHandlerService.createPublicHoliday(newHoliday);
      setHolidays([...holidays, createdHoliday]);
      createNotification('success', 'Jour férié créé');
    } catch (error: any) {
      if (error.response.status === 400) {
        createNotification('error', error.response.data.message);
        return;
      }
      createNotification('error', 'Erreur lors de la création du jour férié');
    }
  };

  const onUpdate = async (publicId: string, editedHoliday: PublicHolidayRequestDTO) => {
    try {
      await publicHolidayRequestHandlerService.updatePublicHoliday(publicId, editedHoliday);
      const updatedHolidays = holidays.map((holiday) => {
        if (holiday.publicId === publicId) {
          return {
            publicId,
            ...editedHoliday,
          };
        }
        return holiday;
      });
      setHolidays(updatedHolidays);
      createNotification('success', 'Jour férié modifié');
    } catch (error: any) {
      if (error.response.status === 404) {
        createNotification('error', 'Impossible de modifier un jour qui n\'existe pas');
        return;
      } else if (error.response.status === 400) {
        createNotification('error', error.response.data.message);
        return;
      }
      createNotification('error', 'Erreur lors de la modification du jour férié');
    }
  };

  const onDelete = async (publicId: string) => {
    try {
      await publicHolidayRequestHandlerService.deletePublicHoliday(publicId);
      const updatedHolidays = holidays.filter((holiday) => holiday.publicId !== publicId);
      setHolidays(updatedHolidays);
      createNotification('success', 'Jour férié supprimé');
    } catch (error: any) {
      if (error.response.status === 404) {
        createNotification('error', 'Impossible de supprimer un jour qui n\'existe pas');
      }
      createNotification('error', 'Erreur lors de la suppression du jour férié');
    }
  };


  return (
    <div className={'body'}>
      <Button variant='primary' className={"create-button"} onClick={onAddEvent}>
        Créer un jour férié
      </Button>
      {showCreateModal && (
        <CreatePublicHolidayModalComponent
          onSave={onCreate}
          onCancel={() => {
            // Gérez l'annulation ici
            setShowCreateModal(false); // Fermez le modal de création sans sauvegarde.
          }}
        />)
      }
      <HolidayListComponent holidays={holidays} onDelete={onDelete} onUpdate={onUpdateEvent} />
      {showEditModal && currentHoliday && (
        <EditPublicHolidayModalComponent
          currentHoliday={currentHoliday}
          onSave={onUpdate}
          onCancel={() => {
            // Gérez l'annulation ici
            setShowEditModal(false); // Fermez la modal sans sauvegarde
          }}
        />
      )}
      <NotificationContainer />
    </div>
  );
};
export default PublicHolidayComponent;