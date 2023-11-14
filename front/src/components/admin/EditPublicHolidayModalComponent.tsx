import React, { useState } from 'react';
import { Button, Form, Modal } from 'react-bootstrap';
import { PublicHolidayRequestDTO, PublicHolidayResponseDTO } from '../../dto/publicHoliday/public.holiday.dto';

interface EditPublicHolidayModalComponentProps {
  currentHoliday: PublicHolidayResponseDTO;
  onSave: (publicId:string, holidayRequestDTO: PublicHolidayRequestDTO) => void;
  onCancel: () => void;
}

const EditPublicHolidayModalComponent : React.FC<EditPublicHolidayModalComponentProps> = ({  currentHoliday, onSave, onCancel }) => {
  const [editedHoliday, setEditedHoliday] = useState(currentHoliday);

  const handleSave = () => {
    onSave(currentHoliday.publicId, editedHoliday);
    onCancel();
  };

  return (
    <Modal show={true} onHide={onCancel}>
      <Modal.Header closeButton>
        <Modal.Title>Modifier le jour férié</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form.Group>
          <Form.Label>Nom</Form.Label>
          <Form.Control
            type='text'
            value={editedHoliday.label}
            onChange={(e) =>
              setEditedHoliday({ ...editedHoliday, label: e.target.value })
            }
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Date</Form.Label>
          <Form.Control
            type='date'
            value={editedHoliday.date}
            onChange={(e) =>
              setEditedHoliday({ ...editedHoliday, date: e.target.value })
            }
          />
        </Form.Group>
      </Modal.Body>
      <Modal.Footer>
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


export default EditPublicHolidayModalComponent;