import React from 'react';
import {Button, Container} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import {BsEmojiDizzyFill} from "react-icons/bs";

const NotFoundPage = () => {
    return (
        <Container className="text-center mt-5 mb-5 not-found-container">
            <h1>404 - Oups!!! <BsEmojiDizzyFill/></h1>
            <p>La page que vous essayer d'accéder n'existe pas.</p>
            <Link to="/">
                <Button variant="primary">Revenir à la page de réservation des créneaux!</Button>
            </Link>
        </Container>
    );
};

export default NotFoundPage;
