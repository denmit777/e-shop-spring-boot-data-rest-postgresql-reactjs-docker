import React from "react";
import {
  Button,
  TextField,
  Typography,
} from "@material-ui/core";

import GoodService from '../services/GoodService';

class AddOrEditGoods extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      title: '',
      price: 100,
      quantity: 1,
      description: 'This is a new product',
      invalidGoodsError: [],
      isInvalidGoods: false,
    };

    this.handleTitleChange = this.handleTitleChange.bind(this);
    this.handlePriceChange = this.handlePriceChange.bind(this);
    this.handleQuantityChange = this.handleQuantityChange.bind(this);
    this.handleDescriptionChange = this.handleDescriptionChange.bind(this);
    this.handleSaveGoods = this.handleSaveGoods.bind(this);
    this.handleCancel = this.handleCancel.bind(this);
  }

  componentDidMount() {
    const goodId = sessionStorage.getItem("goodId");

    if(goodId !== -1) {
      GoodService.getGoodById(goodId).then((res) => {
        let good = res.data;

        this.setState({
          title: good.title,
          price: good.price,
          quantity: good.quantity,
          description: good.description,
        });
      });
    }
  }

  handleTitleChange = (event) => {
    this.setState({
      title: event.target.value,
    });
  };

  handlePriceChange = (event) => {
    this.setState({
      price: event.target.value,
    });
  };

  handleQuantityChange = (event) => {
    this.setState({
      quantity: event.target.value,
    });
  };

  handleDescriptionChange = (event) => {
    this.setState({
      description: event.target.value,
    });
  };

  handleSaveGoods = () => {
    const good = {
       title: this.state.title,
       price: this.state.price,
       quantity: this.state.quantity,
       description: this.state.description
    };

    const goodId = sessionStorage.getItem("goodId");

    if(goodId === -1) {
        GoodService.addGood(good).then(res => {
            this.props.history.push(`/goods`);
        }).catch(err => {
            if (err.response.status === 400) {
                this.setState({ isInvalidGoods: true });
                this.setState({ invalidGoodsError: err.response.data });
            }
        })
    } else {
        GoodService.updateGood(good, goodId).then(res => {
            this.props.history.push(`/goods`);
        }).catch(err => {
            if (err.response.status === 400) {
                this.setState({ isInvalidGoods: true });
                this.setState({ invalidGoodsError: err.response.data });
            }
        })
    }
  }

  handleCancel() {
      this.props.history.push(`/goods`);
  }

  render() {
    const {
      title,
      price,
      quantity,
      description,
      invalidGoodsError,
      isInvalidGoods,
    } = this.state;

    const { handleTitleChange, handlePriceChange, handleQuantityChange,
            handleDescriptionChange, handleSaveGoods, handleCancel } = this;

    return (
      <div className="create-container">
        <div>
          <Typography display="block" variant="h3">
            Add/edit
          </Typography>
        </div><br/>
        {isInvalidGoods &&
            <Typography className="has-error" component="h6" variant="h5">
                {invalidGoodsError.map((error, index) => (
                    <div>
                        {index + 1}
                      . {error.title}
                        {error.price}
                        {error.quantity}
                        {error.description}
                    </div>
                ))}
            </Typography>
        }
        <div className="container__from-wrapper">
             <form>
                <table>
                  <tr className="table">
                      <td>
                        <Typography component="h6" variant="h5">
                            Title *
                        </Typography>
                      </td>
                      <td>
                        <TextField
                            required
                            onChange={handleTitleChange}
                            variant="outlined"
                            placeholder="Title"
                            style = {{width: 300}}
                            value={title}
                        />
                      </td>
                    </tr>
                    <tr className="table">
                        <td>
                            <Typography component="h6" variant="h5">
                                Price *
                            </Typography>
                        </td>
                        <td>
                            <TextField
                                onChange={handlePriceChange}
                                variant="outlined"
                                type="number"
                                value={price}
                                InputProps={{
                                  inputProps: {
                                      max: 10000, min: 1
                                  }
                                }}
                                style = {{width: 150}}
                            />
                         </td>
                    </tr>
                    <tr className="table">
                        <td>
                            <Typography component="h6" variant="h5">
                                Quantity *
                            </Typography>
                        </td>
                        <td>
                            <TextField
                                onChange={handleQuantityChange}
                                variant="outlined"
                                type="number"
                                value={quantity}
                                InputProps={{
                                  inputProps: {
                                      max: 10000, min: 1
                                  }
                                }}
                                style = {{width: 150}}
                            />
                         </td>
                     </tr>
                     <tr className="table">
                        <td>
                          <Typography component="h6" variant="h5">
                              Description
                          </Typography>
                        </td>
                        <td>
                          <TextField
                              onChange={handleDescriptionChange}
                              variant="outlined"
                              multiline
                              rows={4}
                              placeholder="Here is a some text input"
                              style = {{width: 300}}
                              value={description}
                          />
                        </td>
                     </tr><br/>
                     <tr className="table">
                        <td>
                           <div className="container__button-wrapper">
                              <Button
                                 size="large"
                                 variant="contained"
                                 color="primary"
                                 type="reset"
                                 onClick={handleSaveGoods}
                              >
                                 Save
                              </Button>
                           </div>
                        </td>
                        <td>
                            <div className="container__button-wrapper">
                                <Button
                                    size="large"
                                    variant="contained"
                                    color="secondary"
                                    type="reset"
                                    onClick={handleCancel}
                                >
                                    Cancel
                                </Button>
                            </div>
                        </td>
                     </tr>
                </table>
             </form>
           </div>
      </div>
    );
  }
}

export default AddOrEditGoods;