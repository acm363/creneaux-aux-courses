import React, { useState } from 'react';
import { Button, Container, Table } from 'react-bootstrap';
import { PublicHolidayResponseDTO } from '../../dto/publicHoliday/public.holiday.dto';
import { BsPencilFill, BsTrash } from 'react-icons/bs';
import Pagination from 'react-bootstrap/Pagination';
import '../../styles/components/admin/PublicHolidayListComponent.scss';


interface HolidayListProps {
  holidays: PublicHolidayResponseDTO[];
  onUpdate: (publicId: string) => void;
  onDelete: (publicId: string) => void;
}

const HolidayList: React.FC<HolidayListProps> = ({ holidays, onUpdate, onDelete }) => {
  const itemsPerPage = 8; // Nombre d'éléments par page
  const [currentPage, setCurrentPage] = useState(1);

  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const currentHolidays = holidays.slice(startIndex, endIndex);

  const totalPages = Math.ceil(holidays.length / itemsPerPage);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  const formatDate = (dateString: string) => {
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' } as any;
    const date = new Date(dateString).toLocaleDateString('fr-FR', options).replace(',', ' ');
    return date.charAt(0).toUpperCase() + date.slice(1);
  };

  const compareByDate = (a: PublicHolidayResponseDTO, b: PublicHolidayResponseDTO) => {
    const dateA = new Date(a.date);
    const dateB = new Date(b.date);
    const timeA = dateA.getTime();
    const timeB = dateB.getTime();

    return timeA - timeB;
  };

  holidays.sort(compareByDate);

  return (
    <Container className={'body'}>
      <h3>Liste des Jours Fériés</h3>
      <Table striped bordered hover responsive>
        <thead>
        <tr>
          <th>Nom</th>
          <th>Date</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        {currentHolidays.map((holiday) => (
          <tr key={holiday.publicId}>
            <td>{holiday.label}</td>
            <td>{formatDate(holiday.date)}</td>
            <td className={'table-buttons col-2'}>
              <Button className={'update-button'} variant='primary' onClick={() => onUpdate(holiday.publicId)}>
                <BsPencilFill />
              </Button>
              <Button className={'delete-button'} variant='danger' onClick={() => onDelete(holiday.publicId)}>
                <BsTrash />
              </Button>
            </td>
          </tr>
        ))}
        </tbody>
      </Table>

      <Pagination>
        {Array.from({ length: totalPages }).map((_, index) => (
          <Pagination.Item
            key={index + 1}
            active={index + 1 === currentPage}
            onClick={() => handlePageChange(index + 1)}
          >
            {index + 1}
          </Pagination.Item>
        ))}
      </Pagination>
    </Container>
  );
};

export default HolidayList;
