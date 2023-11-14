import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import '../../styles/components/common/Footer.scss';

const Footer: React.FC = () => {
  return (
    <footer className='footer bg-dark text-light py-4'>
      <div className='phantom-footer-top'>
      </div>
      <Container className={"content"}>
        <Row>
          <Col md={6}>
            <h4>Nous Contacter</h4>
            <p>
              Une question ou besoin d'assistance, vous pouvez nous contacter à l'adresse {' '}
              <a href='mailto:support@example.com'>support@auxcourses.com</a>
            </p>
          </Col>
        </Row>
      </Container>
      <div className='text-center mt-3'>
        &copy; {new Date().getFullYear()} Votre drive de courses par excellence. Tout droit réservé.
      </div>
    </footer>
  );
};

export default Footer;
