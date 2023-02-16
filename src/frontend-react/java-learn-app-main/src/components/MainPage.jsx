import React from "react";
import Select from 'react-select';
import { Button, Typography } from "@material-ui/core";
import GoodService from '../services/GoodService';
import OrderService from '../services/OrderService';

class MainPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
       selectOptions : [],
       chosenGoods: [],
       title: '',
       price: '',
       isProductSelected: true,
       productNotSelectedError: '',
       isProductPresent: true,
       productNotFoundError: '',
       isOrderPlaced: true,
       orderNotPlacedError: '',
    };

    this.handleSelectChange = this.handleSelectChange.bind(this);
    this.handleAddProduct = this.handleAddProduct.bind(this);
    this.handleRemoveProduct = this.handleRemoveProduct.bind(this);
    this.handleSaveOrder = this.handleSaveOrder.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
  }

  componentDidMount() {
    GoodService.getAllGoodsForBuyer().then((res) => {
        const allGoods = res.data;

        const options = allGoods.map(good => ({
           value: good.title + JSON.stringify(good.price),
           label: good.title + " (" + JSON.stringify(good.price) + " $)",
        }));

        this.setState({selectOptions: options})
    });
  };

  handleSelectChange = (e) => {
      const REGEX_LETTERS_FIGURES_POINT = /[^-0-9A-Za-z.]/gi;
      const REGEX_ONLY_LETTERS = /[^A-Za-z]/gi;
      const REGEX_ONLY_FIGURES = /[^-0-9.]/gi;

      const menu = e.label.replace(REGEX_LETTERS_FIGURES_POINT, '');

      const name = menu.replace(REGEX_ONLY_LETTERS, '');
      const price = menu.replace(REGEX_ONLY_FIGURES, '');

      this.setState({title: name, price: price})
  }

  handleAddProduct = () => {
      const product = {
        title: this.state.title,
        price: this.state.price
      };
      OrderService.addProductToOrder(product).then(res => {
         this.setState({ chosenGoods: res.data });
         this.setState({ orderNotPlacedError: '' });
         this.setState({ productNotSelectedError: '' });
         this.setState({ productNotFoundError: '' });
      }).catch(err => {
            this.setState({ isProductSelected: false });
            this.setState({ productNotSelectedError: err.response.data.info });
            this.setState({ orderNotPlacedError: '' });
      });
  };

  handleRemoveProduct = () => {
      const product = {
        title: this.state.title,
        price: this.state.price
      };

      OrderService.removeProductFromOrder(product).then(res => {
         this.setState({ chosenGoods: res.data });
         this.setState({ productNotFoundError: '' });
         this.setState({ productNotSelectedError: '' });
      }).catch(err => {
           if (err.response.status === 400) {
                this.setState({ isProductSelected: false });
                this.setState({ productNotSelectedError: err.response.data.info });
                this.setState({ productNotFoundError: '' });
                this.setState({ orderNotPlacedError: '' });
           }
           if (err.response.status === 404) {
                this.setState({ isProductPresent: false });
                this.setState({ productNotFoundError: err.response.data.info });
                this.setState({ productNotSelectedError: '' });
                this.setState({ orderNotPlacedError: '' });
           }
      });
  };

  handleSaveOrder = () => {
     const product = {
          title: this.state.title,
          price: this.state.price
     };

     OrderService.saveOrder(product).then(res => {
        sessionStorage.setItem("orderId", res.data.id);
        sessionStorage.setItem("orderGoods", res.data.goods)

        this.props.history.push('/orders/' + res.data.id);
     }).catch(err => {
           this.setState({ orderNotPlacedError: err.response.data.info });
           this.setState({ isOrderPlaced: false });
           this.setState({ productNotSelectedError: '' });
           this.setState({ productNotFoundError: '' });
     });
  };

  handleLogout = () => {

     const product = {
        title: this.state.title,
        price: this.state.price
     };

     OrderService.logoutGoods(product);

     window.location.href = "/";
  };

  render() {
    const { selectOptions, chosenGoods, isOrderPlaced, orderNotPlacedError, isProductSelected,
            productNotSelectedError, isProductPresent, productNotFoundError } = this.state;

    const { handleSelectChange, handleAddProduct, handleSaveOrder, handleRemoveProduct, handleLogout } = this;

    const userName = sessionStorage.getItem("userName");

    return (
     <div className="container">
        <div className="container__title-wrapper">
            <Typography component="h2" variant="h4">
               Hello {userName}!
            </Typography>
        </div>
        <br/>
        {chosenGoods.length > 0 ?
            <Typography component="h6" variant="h5">You have already chosen:</Typography> :
            <Typography component="h6" variant="h5">Make your order</Typography>
        }
        <br/>
        <div>
            <Typography component="h2" variant="h5">
                {chosenGoods.map((product, index) => (
                     <div align='center'>
                         {index + 1 + ") "}
                         {product.title + " "}
                         {product.price + " $"}
                     </div>
                ))}
            </Typography>
        </div>
        <br/>
        <form>
            <div>
                <Select
                    options={selectOptions}
                    defaultValue={{label: "Choose a product"}}
                    onChange={handleSelectChange}
                />
            </div>
            <br/>
            <div className="submit-button-section">
                <Button
                  size="large"
                  variant="contained"
                  color="primary"
                  onClick={handleAddProduct}
                >
                  Add Goods
                </Button>
                <Button
                  size="large"
                  variant="contained"
                  color="secondary"
                  onClick={handleSaveOrder}
                >
                  Submit
                </Button>
            </div>
            <br/><br/>
            <div className="submit-button-section">
                <Button
                  size="large"
                  variant="contained"
                  color="primary"
                  onClick={handleRemoveProduct}
                >
                  Remove Goods
                </Button>
                <Button
                    size="large"
                    variant="contained"
                    color="secondary"
                    onClick={handleLogout}
                >
                    Logout
                </Button>
            </div>
        </form>
        <br/>
        {!isOrderPlaced &&
            <Typography className="has-error" component="h6" variant="h5">
                {orderNotPlacedError}
            </Typography>
        }
        {!isProductSelected &&
            <Typography className="has-error" component="h6" variant="h5">
                {productNotSelectedError}
            </Typography>
        }
        {!isProductPresent &&
            <Typography className="has-error" component="h6" variant="h5">
                {productNotFoundError}
            </Typography>
        }
     </div>
    );
  }
}

export default MainPage;
