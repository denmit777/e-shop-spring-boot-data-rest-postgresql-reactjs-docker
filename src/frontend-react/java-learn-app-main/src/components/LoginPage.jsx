import React from "react";
import { Button, TextField, Typography } from "@material-ui/core";
import AuthenticationService from '../services/AuthenticationService';

class LoginPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      loginValue: '',
      passwordValue: '',
      isForbiddenLogin: false,
      isForbiddenPassword: false,
      validErrors: [],
      accessDeniedLoginErrors: '',
      accessDeniedPasswordErrors: ''
    };

    this.login = this.login.bind(this);
    this.register = this.register.bind(this);
  }

  handleLoginChange = (event) => {
    this.setState({ loginValue: event.target.value });
  };

  handlePasswordChange = (event) => {
    this.setState({ passwordValue: event.target.value });
  };

  register() {
    this.props.history.push(`/register`)
  }

  login() {
    AuthenticationService
      .executeJwtAuthenticationService(this.state.loginValue, this.state.passwordValue)
      .then((response) => {
        AuthenticationService.registerSuccessfulLoginForJwt(this.state.loginValue, response.data.token);

          const userRole = response.data.role;

          sessionStorage.setItem("userName", response.data.userName);
          sessionStorage.setItem("userRole", userRole);

          if (userRole === "ROLE_ADMIN") {
            this.props.history.push(`/goods`);
          }
          else {
            this.props.history.push(`/orders`);
          }
      })
      .catch(err => {
        if (err.response.status === 401) {
            this.setState({ validErrors: err.response.data });
            this.setState({ accessDeniedLoginErrors: '' });
            this.setState({ accessDeniedPasswordErrors: '' });
        }

        if (err.response.status === 403) {
            this.setState({ isForbiddenLogin: true });
            this.setState({ accessDeniedLoginErrors: err.response.data });
            this.setState({ accessDeniedPasswordErrors: '' });
            this.setState({ validErrors: [] });
        }

        if (err.response.status === 400) {
            this.setState({ isForbiddenPassword: true });
            this.setState({ accessDeniedPasswordErrors: err.response.data.info });
            this.setState({ accessDeniedLoginErrors: '' });
            this.setState({ validErrors: [] });
        }
      })
  }

  render() {
    const { isForbiddenLogin, accessDeniedLoginErrors, isForbiddenPassword, accessDeniedPasswordErrors,
            validErrors } = this.state;

    const { handleLoginChange, handlePasswordChange, login, register } = this;

    return (
      <div className="container">
        <div className="container__title-wrapper">
          <Typography component="h2" variant="h3">
             Welcome to Online Shop!
          </Typography>
        </div>
        {isForbiddenLogin &&
            <Typography className="has-error" component="h6" variant="h5">
                {accessDeniedLoginErrors}
            </Typography>
        }
        {isForbiddenPassword &&
            <Typography className="has-error" component="h6" variant="h5">
                {accessDeniedPasswordErrors}
            </Typography>
        }
        <div className="container__from-wrapper">
          <form>
            <div className="form__input-wrapper">
              <div className="container__title-wrapper">
                <Typography component="h6" variant="h5">
                  Login
                </Typography>
              </div>
              <TextField
                  onChange={handleLoginChange}
                  label="Email"
                  variant="outlined"
                  placeholder="Enter your email"
              />
            </div>
            <div className="form__input-wrapper">
              <div className="container__title-wrapper">
                 <Typography component="h6" variant="h5">
                   Password
                 </Typography>
              </div>
              <TextField
                  onChange={handlePasswordChange}
                  label="Password"
                  variant="outlined"
                  type="password"
                  placeholder="Enter your password"
              />
            </div>
          </form>
        </div>
        <div className="container__button-wrapper">
          <Button
            size="large"
            variant="contained"
            color="primary"
            onClick={login}
          >
            Enter
          </Button>
         </div>
        <div className="container__button-wrapper">
          <Button
            size="large"
            variant="contained"
            color="secondary"
            onClick={register}
          >
            Register
          </Button>
        </div>
        {
          validErrors.length > 0 &&
            <div className="has-error">
              <ol>
                {validErrors.map((key) => {
                  return <li>{key} </li>
                })}
              </ol>
            </div>
        }
      </div>
    );
  }
};

export default LoginPage;
