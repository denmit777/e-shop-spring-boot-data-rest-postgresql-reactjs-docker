import React from "react";
import { Button, TextField, Typography } from "@material-ui/core";
import AuthenticationService from '../services/AuthenticationService';

class RegisterPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      nameValue: "",
      passwordValue: "",
      emailValue: "",
      registerValidErrors: [],
      isUserPresent: false,
      userIsPresentErrors: '',
      isUserCheck: null,
      isCheckboxClicked: true,
      checkBoxError: '',
    };
    this.signIn = this.signIn.bind(this);
  }

  handleNameChange = (event) => {
    this.setState({ nameValue: event.target.value });
  };

  handlePasswordChange = (event) => {
    this.setState({ passwordValue: event.target.value });
  };

  handleEmailChange = (event) => {
     this.setState({ emailValue: event.target.value });
  };

  handleCheckBoxChange = (event) => {
     this.setState({ isUserCheck: event.target.value });
  };

  signIn() {
    if (this.state.isUserCheck !== null) {
      AuthenticationService
        .createUser(this.state.nameValue, this.state.passwordValue, this.state.emailValue)
        .then((response) => {
          this.props.history.push('/')
        })
        .catch(err => {
          if (err.response.status === 401) {
            this.setState({ registerValidErrors: err.response.data });
            this.setState({ checkBoxError: '' });
            this.setState({ userIsPresentErrors: '' });
          }
          if (err.response.status === 400) {
            this.setState({ isUserPresent: true })
            this.setState({ userIsPresentErrors: err.response.data.info });
            this.setState({ checkBoxError: '' });
            this.setState({ registerValidErrors: [] });
          }
        })
    } else {
        AuthenticationService
          .registerWithoutCheckbox(this.state.nameValue, this.state.passwordValue, this.state.emailValue)
          .then((response) => {})
          .catch(err => {
              if (err.response.status === 401) {
                  this.setState({ registerValidErrors: err.response.data });
                  this.setState({ userIsPresentErrors: '' });
              }

              if (err.response.status === 400) {
                  this.setState({ isUserPresent: true })
                  this.setState({ userIsPresentErrors: err.response.data.info });
                  this.setState({ registerValidErrors: [] });
              }

              if (err.response.status === 403) {
                  this.setState({ isCheckboxClicked: false })
                  this.setState({ checkBoxError: err.response.data.info });
                  this.setState({ userIsPresentErrors: '' });
                  this.setState({ registerValidErrors: [] })
              }
          })
    }
  }

  render() {

    const { isCheckboxClicked, checkBoxError, isUserPresent, userIsPresentErrors, registerValidErrors } = this.state;

    const { handleNameChange, handlePasswordChange, handleEmailChange, handleCheckBoxChange, signIn } = this;

    return (
      <div className="container">
        <div className="container__title-wrapper">
          <Typography component="h2" variant="h3">
            Register page
          </Typography>
        </div>
        <div className="has-error">
        {
          !isCheckboxClicked &&
             <div>
                 {checkBoxError}
             </div>
        }
        {
          isUserPresent &&
             <div>
                 {userIsPresentErrors}
             </div>
        }
        </div>
        <div className="container__from-wrapper">
          <form>
              <div className="form__input-wrapper">
                <div className="container__title-wrapper">
                   <Typography component="h6" variant="h5">
                      Name
                   </Typography>
                </div>
                <TextField
                  onChange={handleNameChange}
                  label="User name"
                  variant="outlined"
                  placeholder="Enter your name"
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
              <div className="form__input-wrapper">
                <div className="container__title-wrapper">
                   <Typography component="h6" variant="h5">
                      Email
                   </Typography>
                </div>
                <TextField
                  onChange={handleEmailChange}
                  label="Email"
                  variant="outlined"
                  placeholder="Enter your email"
                />
              </div>
              <input className="checkbox"
                 onChange={handleCheckBoxChange}
                 type="checkbox"
                 name="isUserCheck"
                 id="checkbox"
                 value=""/>
              <label for="checkbox"> I agree with the terms of service</label>
          </form>
        </div>
        <div className="container__button-wrapper">
          <Button
            size="large"
            variant="contained"
            color="primary"
            onClick={signIn}
          >
            Sign In
          </Button>
        </div>
        {
          registerValidErrors.length > 0 &&
            <div className="has-error">
              <ol>
                {registerValidErrors.map((key) => {
                  return <li>{key} </li>
                })}
              </ol>
            </div>
        }
      </div>
    );
  }
};

export default RegisterPage;