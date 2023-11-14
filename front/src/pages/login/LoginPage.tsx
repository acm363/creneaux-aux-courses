import React, { useEffect, useState } from 'react';
import { Button, Container, Form } from 'react-bootstrap';
import { CgMail } from 'react-icons/cg';
import { BsArrowRightSquareFill, BsKeyFill } from 'react-icons/bs';
import { HttpStatusCode } from 'axios';
import { UserDTO, UserType } from '../../dto/user/user.dto';
import { useNavigate } from 'react-router-dom';
import {UserSessionService,AuthService} from '../../services';
import { ADMIN_SLOT_ROUTE, CLIENT_ROUTE } from '../../routes/routes';

import '../../styles/pages/login/LoginPage.scss';
import ErrorComponent from '../../components/common/Error';

const LoginPage: React.FC = () => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null); // To store and display error messages.
  const [userDto, setUserDto] = useState<UserDTO | null>(null); // To store the client response from the API.
  const navigate = useNavigate();

  const handleLogin = async (event: React.FormEvent) => {
    event.preventDefault();
    try {
      // No dependency injection in React. Create an instance of the AuthService class.
      const userDto = await AuthService.login(email, password);

      // Update the state with the client data.
      setUserDto(userDto);

      // If successful, reset the error state.
      setErrorMessage(null);
    } catch (error: any) {
      if (error.response && error.response.status === HttpStatusCode.Unauthorized) {
        // The client is not found.
        setErrorMessage('Email ou de mot de passe incorrect.');
      } else {
        // For other errors, display a generic error message.
        setErrorMessage('Oups !!! Une erreur est survenue. Veuillez réessayer plus tard.');
      }
    }
  };

  // Use the useEffect hook to redirect the client to the admin or client page if the client is already logged in.
  useEffect(() => {
    if (userDto && UserSessionService.hasUserSession()) {
      if (userDto.type === UserType.ADMIN) {
        // Redirect to the admin page.
        navigate(ADMIN_SLOT_ROUTE);
      } else if (userDto.type === UserType.CLIENT) {
        // Redirect to the client page.
        navigate(CLIENT_ROUTE);
      }
    }
  });

  return (
    <div className='login-page'>
      <Container className='header'>
      </Container>
      <Container className='body'>
        <div className='login-form'>

          <div className='title'>
            <h2>Se connecter</h2>
          </div>
          <Form onSubmit={handleLogin}>
            <Form.Group className='login-form-field' controlId='loginFormEmail'>
              <Form.Label> <CgMail size={24} /> : </Form.Label>
              <Form.Control
                type='email'
                placeholder='Votre adresse email'
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group className='login-form-field' controlId='loginFormPassword'>
              <Form.Label> <BsKeyFill size={24} /> : </Form.Label>
              <Form.Control
                type='password'
                placeholder='Mot de passe'
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />

            </Form.Group>
            <div className='buttons'>
              <Button variant='primary' type='submit'>
                <BsArrowRightSquareFill /> Se connecter
              </Button>
            </div>
            {errorMessage && <ErrorComponent message={errorMessage} />}
          </Form>
        </div>
      </Container>
    </div>
  );
};

export default LoginPage;
