import React from "react";
import OrdersTable from "./OrdersTable";
import { AppBar, Button, Typography } from "@material-ui/core";
import { Link } from "react-router-dom";
import OrderService from '../services/OrderService';

class OrdersList extends React.Component {
   constructor(props) {
      super(props);

      this.state = {
         prop: 42,
         allOrders: [],
         orderGoods: '',
         total: 0,
         showText: false,
         pageSize: 4,
         pageNumber: 0,
         currentPage: 0
      };

      this.handleClickTotalPrice = this.handleClickTotalPrice.bind(this);
      this.handleLogout = this.handleLogout.bind(this);
      this.getOrderInfo = this.getOrderInfo.bind(this);
   }

   componentDidMount() {
      const { pageNumber, pageSize } = this.state;

      OrderService.getAllOrdersByPages(pageSize, pageNumber).then((res) => {
          this.setState({ allOrders: res.data });
      });

      OrderService.getTotalAmount().then((res) => {
         this.setState({ total: res.data });
      });
   }

   handlePreviousPageNumberChange = () => {
      const { pageSize, pageNumber, total } = this.state;
      const maxAmountOfRowsOn3Pages = 3 * pageSize;
      const numberOfPageChanges = parseInt(total / maxAmountOfRowsOn3Pages);

      if(pageNumber !== 0) {
         OrderService.getAllOrdersByPages(pageSize, pageNumber - 1).then((res) => {
            this.setState({ allOrders: res.data });
            this.setState({ pageNumber: pageNumber - 1});

            if(this.state.pageNumber - 1 > 0) {
               this.setState({ currentPage: this.state.pageNumber - numberOfPageChanges - 1});
            } else {
               this.setState({ currentPage: 0 });
            }
         });
      }
   };

   handlePageNumberChange = (pageNumber) => {
      const { pageSize } = this.state;

      OrderService.getAllOrdersByPages(pageSize, pageNumber).then((res) => {
         this.setState({ allOrders: res.data });
         this.setState({ pageNumber: pageNumber });
      });
   };

   handleNextPageNumberChange = () => {
      const { total, pageSize, pageNumber } = this.state;
      const maxAmountOfRowsOn3Pages = 3 * pageSize;
      const numberOfPageChanges = parseInt(total / maxAmountOfRowsOn3Pages);
      const pagesCount = parseInt(total / pageSize) ;

      if(+pageNumber + 1 <= pagesCount) {
         OrderService.getAllOrdersByPages(pageSize, +pageNumber + 1).then((res) => {
            this.setState({ allOrders: res.data });
            this.setState({ pageNumber: +pageNumber + 1 });
            this.setState({ currentPage: pageNumber + numberOfPageChanges });
         })
      };
   };

   handleClickTotalPrice = (orderId, event) => {
       switch (event.detail) {
          case 1: { // click
             OrderService.getOrderById(orderId).then((res) => {
                this.setState({ orderGoods: res.data.description });
             });

             this.setState({ showText: true });
             break;
          }
          case 2: { //2 clicks
             this.setState({ showText: false });
             break;
          }
          default: {
             break;
          }
       }
   };

   handleLogout = () => {
      window.location.href = "/";
   };

   handleSortOrdersAsc = (event, field) => {
      const { pageSize, pageNumber } = this.state;

      OrderService.getAllSortedOrdersByPages(field, pageSize, pageNumber).then((res) => {
         this.setState({ allOrders: res.data })
      });
   }

   handleSortOrdersDesc = (event, field) => {
      const { pageSize, pageNumber } = this.state;

      OrderService.getAllDescSortedOrdersByPages(field, pageSize, pageNumber).then((res) => {
         this.setState({ allOrders: res.data })
      });
   }

   getOrderInfo(orderId) {
      sessionStorage.setItem("orderViewId", orderId);

      this.props.history.push('/orders/' + orderId);
   }

   render() {
     const { allOrders, orderGoods, showText, total, pageNumber, currentPage } = this.state;

     const { handleSortOrdersAsc, handleSortOrdersDesc, handleClickTotalPrice, handlePageNumberChange,
             handlePreviousPageNumberChange, handleNextPageNumberChange, handleLogout, getOrderInfo } = this;

     return (
        <div>
             <div className="buttons-container">
                <Button component={Link} to="/goods" variant="contained" color="primary">
                    Back to goods
                </Button>
                <Typography component="h2" variant="h3">
                    Orders table
                </Typography>
                <Button
                    onClick={handleLogout}
                    variant="contained"
                    color="secondary"
                >
                    Logout
                </Button>
             </div>
             <div className="table-container">
                <form>
                    <table align='center'>
                        {showText &&
                            <Typography component="h2" variant="h5" align='center' >
                                Ordered goods:
                                <tr className="table">
                                    <td>
                                        <pre>
                                            {orderGoods}
                                        </pre>
                                    </td>
                                </tr>
                            </Typography>
                        }
                    </table>
                </form><br/>
                <AppBar position="static">
                    <OrdersTable
                        handleClickTotalPriceCallback={handleClickTotalPrice}
                        sortAscCallback={handleSortOrdersAsc}
                        sortDescCallback={handleSortOrdersDesc}
                        orders = {allOrders}
                        getOrderInfoCallback={getOrderInfo}
                        total = {total}
                        pageNumber = {pageNumber + 1}
                    />
                </AppBar><br/>
                <div>
                    <table>
                        <tr className="table">
                            <td>
                                <div  className="container__button-wrapper">
                                    <button
                                        size="large"
                                        variant="contained"
                                        color="primary"
                                        type="reset"
                                        onClick={handlePreviousPageNumberChange}
                                    >
                                        Previous
                                    </button>
                                </div>
                            </td>
                            <td>
                                <div className="container__button-wrapper">
                                    <button
                                        size="large"
                                        variant="contained"
                                        color="primary"
                                        type="reset"
                                        onClick={() => handlePageNumberChange(currentPage)}
                                    >
                                        {currentPage + 1}
                                    </button>
                                </div>
                            </td>
                            <td>
                                <div className="container__button-wrapper">
                                    <button
                                        size="large"
                                        variant="contained"
                                        color="secondary"
                                        type="reset"
                                        onClick={() => handlePageNumberChange(currentPage + 1)}
                                    >
                                        {currentPage + 2}
                                    </button>
                                </div>
                            </td>
                            <td>
                                <div className="container__button-wrapper">
                                    <button
                                        size="large"
                                        variant="contained"
                                        color="primary"
                                        type="reset"
                                        onClick={() => handlePageNumberChange(currentPage + 2)}
                                    >
                                        {currentPage + 3}
                                    </button>
                                </div>
                            </td>
                            <td>
                                <div className="container__button-wrapper">
                                    <button
                                        size="large"
                                        variant="contained"
                                        color="primary"
                                        type="reset"
                                        onClick={handleNextPageNumberChange}
                                    >
                                        Next
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
             </div>
        </div>
     );
   }
 }

export default OrdersList;