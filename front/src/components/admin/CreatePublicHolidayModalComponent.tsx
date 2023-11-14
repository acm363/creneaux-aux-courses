import React, { useState } from 'react';
import { Button, Form, Modal } from 'react-bootstrap';
import { PublicHolidayRequestDTO } from '../../dto/publicHoliday/public.holiday.dto';

interface CreatePublicHolidayModalComponentProps {
  onSave: (holidayRequestDTO: PublicHolidayRequestDTO) => void;
  onCancel: () => void;
}

const CreatePublicHolidayModalComponent: React.FC<CreatePublicHolidayModalComponentProps> = ({ onSave, onCancel }) => {
  const [newHoliday, setNewHoliday] = useState<PublicHolidayRequestDTO>({
    date: '',
    label: '',
  });
  const [showWarning, setShowWarning] = useState(false); // State to control the warning message

  const handleSave = () => {
    if (!newHoliday.date || !newHoliday.label) {
      setShowWarning(true); // Show the warning message
      return;
    }
    onSave(newHoliday);
    onCancel();
  };

  return (
    <Modal show={true} onHide={onCancel}>
      <Modal.Header closeButton>
        <Modal.Title>Créer un jour férié</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form.Group>
          <Form.Label>Nom</Form.Label>
          <Form.Control
            required
            type='text'
            value={newHoliday.label}
            placeholder={'Armistice'}
            onChange={(e) =>
              setNewHoliday({ ...newHoliday, label: e.target.value })
            }
            style={{ borderColor: showWarning && !newHoliday.label ? 'red' : '' }}
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Date</Form.Label>
          <Form.Control
            required
            type='date'
            value={newHoliday.date}
            onChange={(e) =>
              setNewHoliday({ ...newHoliday, date: e.target.value })
            }
            style={{ borderColor: showWarning && !newHoliday.date ? 'red' : '' }}
          />
        </Form.Group>
      </Modal.Body>
      <Modal.Footer>
        {showWarning && (!newHoliday.date || !newHoliday.label) &&
          <p style={{ color: 'red' }}>Veuillez remplir tous les champs.</p>}
        <Button variant='secondary' onClick={onCancel}>
          Annuler
        </Button>
        <Button variant='primary' onClick={handleSave}>
          Enregistrer
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CreatePublicHolidayModalComponent;
