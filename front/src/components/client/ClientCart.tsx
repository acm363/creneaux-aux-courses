import React, { useEffect, useState } from 'react';
import Card from 'react-bootstrap/Card';
import { Col, Image, Modal, Row } from 'react-bootstrap';
import { Button } from '@mui/material';
import '../../styles/components/client/ClientCart.scss';
import {VscArrowRight} from "react-icons/vsc";

interface ClientCartProps {
  onConfirm: () => void;
}
export const ClientCart = ({ onConfirm }: ClientCartProps) => {

  const [articles] = useState([
    {
      name: 'Pommes (1 kg)',
      price: 2.50,
      image: './assets/images/apples.jpg',
    },
    {
      name: 'Bananes (1 kg)',
      price: 1.50,
      image: './assets/images/bananas.png',
    },
    {
      name: 'Riz (1 kg)',
      price: 2.30,
      image: './assets/images/rice.jpg',
    },
    {
      name: 'Bières (6 x 33 cl)',
      price: 15,
      image: './assets/images/beers.jpg',
    },
    {
      name: 'Poulet rôti entier',
      price: 10,
      image: './assets/images/poulet_roti.jpg',
    },
    {
      name: 'Boeuf (3kg)',
      price: 35,
      image: './assets/images/boeuf.png',
    },
    {
      name: 'Poissons Frais (1 kg)',
      price: 20,
      image: './assets/images/poisson_frais.jpg',
    },
    {
      name: 'Nettoyant Pour vitres (*1)',
      price: 2,
      image: './assets/images/nettoyant.jpg',
    },
  ]);
  // Calcul du total des articles
  const total = articles.reduce((acc, article) => acc + article.price, 0);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    const hasVisitedClientPageBefore = localStorage.getItem('hasVisitedClientPageBefore');
    if (!hasVisitedClientPageBefore) {
      setShowModal(true);
      localStorage.setItem('hasVisitedClientPageBefore', 'true');
    }
  }, []);

  const handleClose = () => setShowModal(false);

  const renderModal = () => {
    return <Modal show={showModal} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Message Important</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Avant de transformer le panier en commande, le créneau de retrait doit être choisi.
      </Modal.Body>
      <Modal.Footer>
        <Button variant='contained' onClick={handleClose}>
          Compris
        </Button>
      </Modal.Footer>
    </Modal>;
  };

  return (
    <div className='body mt-5'>
      {renderModal()}
      <h1 className='text-center'>Valider votre panier et réserver un créneau de retrait</h1>
      <Row className='mt-5'>
        <Col xs={12} md={8}>
          <Row>
            {articles.map((article, index) => (
              <Col key={index}>
                <Card style={{ height: '15em', marginBottom: '2em' }}>
                  <Card.Body>
                    <Card.Title>{article.name}</Card.Title>
                    <Card.Text>
                      <strong>Prix : </strong> {article.price} €
                    </Card.Text>
                    <Image className='card-image' src={article.image} />
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </Col>
        <Col xs={12} md={4}>
          <div className='text-center'>
            <h2>Total : {total} €</h2>
            <Button variant="outlined" endIcon={<VscArrowRight />} onClick={onConfirm}>Réserver un créneau de retrait</Button>
          </div>
        </Col>
      </Row>
    </div>
  );
};
