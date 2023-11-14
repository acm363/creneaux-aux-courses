import React from 'react';
import { Container } from 'react-bootstrap';
import Card from 'react-bootstrap/Card';

import '../../styles/pages/common/SlotHomePage.scss';


const SlotHomePage = () => {
  return (
    <div className='slot-home-page'>
      <Container className='header'>
        <Container className='header-top'>
          <h1>Réservation de Créneaux</h1>
        </Container>
      </Container>
      <Container className='body'>
        <Card className="information-card" bg="dark" >
          <Card.Body>
            <Card.Text>
              Bienvenue sur notre service de réservation de créneaux.
              <br/>
              Réservez votre créneau en quelques clics.
              <br/>
              Profitez de la commodité de récupérer vos achats en toute sécurité et sans quitter votre véhicule.
            </Card.Text>
          </Card.Body>
        </Card>
      </Container>
    </div>
  );
};
export default SlotHomePage;
