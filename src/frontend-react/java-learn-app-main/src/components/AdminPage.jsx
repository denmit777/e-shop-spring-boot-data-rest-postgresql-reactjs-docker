import React from "react";
import GoodsTable from "./GoodsTable";
import { AppBar, Button, Typography, TextField } from "@material-ui/core";
import { Link } from "react-router-dom";
import GoodService from '../services/GoodService';

class AdminPage extends React.Component {
   constructor(props) {
      super(props);

      this.state = {
         prop: 42,
         allGoods: [],
         filteredByIdGoods: [],
         filteredByTitleGoods: [],
         filteredByPriceGoods: [],
         filteredByDescriptionGoods: [],
         searchValue: '',
         searchError: [],
         total: 0,
         showText: false,
         description: '',
         pageSize: 6,
         pageNumber: 0,
         currentPage: 0
      };

      this.handleMouseEnter = this.handleMouseEnter.bind(this);
      this.handleMouseLeave = this.handleMouseLeave.bind(this);
      this.handleLogout = this.handleLogout.bind(this);
      this.addGoods = this.addGoods.bind(this);
      this.editGoods = this.editGoods.bind(this);
      this.removeGoods = this.removeGoods.bind(this);
   }

   componentDidMount() {
      const { pageNumber, pageSize } = this.state;

      GoodService.getAllGoodsByPages(pageSize, pageNumber).then((res) => {
         this.setState({ allGoods: res.data });
      });

      GoodService.getTotalAmount().then((res) => {
         this.setState({ total: res.data });
      });
   }

   handlePreviousPageNumberChange = () => {
       const { pageSize, pageNumber, total } = this.state;
       const maxAmountOfRowsOn3Pages = 3 * pageSize;
       const numberOfPageChanges = parseInt(total / maxAmountOfRowsOn3Pages);

       if(pageNumber !== 0) {
          GoodService.getAllGoodsByPages(pageSize, pageNumber - 1).then((res) => {
             this.setState({ allGoods: res.data });
             this.setState({ filteredByIdGoods : [] });
             this.setState({ filteredByTitleGoods : [] });
             this.setState({ filteredByPriceGoods : [] });
             this.setState({ filteredByDescriptionGoods : [] });
             this.setState({ pageNumber: pageNumber - 1 });

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

      GoodService.getAllGoodsByPages(pageSize, pageNumber).then((res) => {
         this.setState({ allGoods: res.data });
         this.setState({ filteredByIdGoods : [] });
         this.setState({ filteredByTitleGoods : [] });
         this.setState({ filteredByPriceGoods : [] });
         this.setState({ filteredByDescriptionGoods : [] });
         this.setState({ pageNumber: pageNumber });
      });
   };

   handleNextPageNumberChange = () => {
      const { total, pageSize, pageNumber } = this.state;
      const maxAmountOfRowsOn3Pages = 3 * pageSize;
      const numberOfPageChanges = parseInt(total / maxAmountOfRowsOn3Pages);
      const pagesCount = parseInt(total / pageSize) ;

      if(+pageNumber + 1 <= pagesCount) {
         GoodService.getAllGoodsByPages(pageSize, +pageNumber + 1).then((res) => {
            this.setState({ allGoods: res.data });
            this.setState({ filteredByIdGoods : [] });
            this.setState({ filteredByTitleGoods : [] });
            this.setState({ filteredByPriceGoods : [] });
            this.setState({ filteredByDescriptionGoods : [] });
            this.setState({ pageNumber: +pageNumber + 1 });
            this.setState({ currentPage: pageNumber + numberOfPageChanges });
         });
      };
   };

   handleMouseEnter = (goodId, event) => {
      GoodService.getGoodById(goodId).then((res) => {
         let good = res.data;

         this.setState({
            description: good.description,
         });
      });

      this.setState({ showText: true });
   }

   handleMouseLeave = (event) => {
      this.setState({ showText: false });
   }

   handleLogout = () => {
      window.location.href = "/";
   };

   addGoods() {
      sessionStorage.setItem("goodId", -1);

      this.props.history.push('/add-edit');
   }

   editGoods(goodId) {
      sessionStorage.setItem("goodId", goodId);

      this.props.history.push('/add-edit');
   }

   removeGoods(goodId) {
      const goods = this.state.allGoods;

      GoodService.deleteGood(goodId).then( res => {
         const data = goods.filter(i => i.id !== goodId);

         this.setState({ allGoods : data });
         this.setState({ filteredByIdGoods : [] });
         this.setState({ filteredByTitleGoods : [] });
         this.setState({ filteredByPriceGoods : [] });
         this.setState({ filteredByDescriptionGoods : [] });

         goodId--;
      });
   }

   handleSortGoodsAsc = (event, field) => {
      const { pageSize, pageNumber, searchValue } = this.state;

      if (searchValue === '') {
         GoodService.getAllSortedGoodsByPages(field, pageSize, pageNumber).then((res) => {
            this.setState({ allGoods: res.data })
            this.setState({ filteredByIdGoods : [] });
            this.setState({ filteredByTitleGoods : [] });
            this.setState({ filteredByPriceGoods : [] });
            this.setState({ filteredByDescriptionGoods : [] });
         });
      }

      GoodService.getAllSortedGoodsSearchedById(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByIdGoods: res.data });
      })

      GoodService.getAllSortedGoodsSearchedByTitle(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByTitleGoods: res.data });
      })

      GoodService.getAllSortedGoodsSearchedByPrice(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByPriceGoods: res.data });
      })

      GoodService.getAllSortedGoodsSearchedByDescription(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByDescriptionGoods: res.data });
      })
   }

   handleSortGoodsDesc = (event, field) => {
      const { pageSize, pageNumber, searchValue } = this.state;

      if (searchValue === '') {
         GoodService.getAllDescendingSortedGoodsByPages(field, pageSize, pageNumber).then((res) => {
            this.setState({ allGoods: res.data })
            this.setState({ filteredByIdGoods : [] });
            this.setState({ filteredByTitleGoods : [] });
            this.setState({ filteredByPriceGoods : [] });
            this.setState({ filteredByDescriptionGoods : [] });
         });
      }

      GoodService.getAllDescendingSortedGoodsSearchedById(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByIdGoods: res.data });
      })

      GoodService.getAllDescendingSortedGoodsSearchedByTitle(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByTitleGoods: res.data });
      })

      GoodService.getAllDescendingSortedGoodsSearchedByPrice(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByPriceGoods: res.data });
      })

      GoodService.getAllDescendingSortedGoodsSearchedByDescription(searchValue, field, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByDescriptionGoods: res.data });
      })
   }

   handleSearchGood = (event) => {
      const { pageNumber, pageSize } = this.state;
      const searchValue = event.target.value;

      this.setState({ searchValue: searchValue })

      if (searchValue === '') {
         GoodService.getAllGoodsByPages(pageSize, pageNumber).then((res) => {
            this.setState({ allGoods: res.data });
            this.setState({ filteredByIdGoods : [] });
            this.setState({ filteredByTitleGoods : [] });
            this.setState({ filteredByPriceGoods : [] });
            this.setState({ filteredByDescriptionGoods : [] });
         });
      }

      GoodService.getAllGoodsSearchedById(searchValue, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByIdGoods: res.data });
      })

      GoodService.getAllGoodsSearchedByTitle(searchValue, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByTitleGoods: res.data });
         this.setState({ searchError: [] })
      }).catch(err => {
            if (err.response) {
                this.setState({ searchError: err.response.data })
            }
      })

      GoodService.getAllGoodsSearchedByPrice(searchValue, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByPriceGoods: res.data });
      })

      GoodService.getAllGoodsSearchedByDescription(searchValue, pageSize, pageNumber).then((res) => {
         this.setState({ filteredByDescriptionGoods: res.data });
      })
   }

   render() {
     const { allGoods, filteredByIdGoods, filteredByTitleGoods, filteredByPriceGoods, filteredByDescriptionGoods,
            searchError, showText, description, total, pageNumber, currentPage } = this.state;

     const { handleSearchGood, handleSortGoodsAsc, handleSortGoodsDesc, addGoods, editGoods, removeGoods,
            handleMouseEnter, handleMouseLeave, handlePageNumberChange, handlePreviousPageNumberChange,
            handleNextPageNumberChange, handleLogout} = this;

     return (
        <div>
             <div className="buttons-container">
                <Button component={Link} to="/ordersList" variant="contained" color="primary">
                    Orders list
                </Button>
                <Typography component="h2" variant="h3">
                    Goods table
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
                <div className="container__from-wrapper">
                    <form>
                        <table>
                            <tr className="table">
                                <td>
                                    <TextField
                                        onChange={handleSearchGood}
                                        variant="outlined"
                                        placeholder="Enter text to search"
                                    />
                                </td>
                                {showText &&
                                    <td>
                                        <Typography
                                             component="h5"
                                             variant="h5"
                                             style={{color: 'blue'}}
                                        >
                                             {description}
                                        </Typography>
                                    </td>
                                }
                            </tr>
                        </table>
                        <div>
                            {searchError.length > 0 &&
                                <Typography className="has-error" component="h6" variant="h5">
                                    {searchError}
                                </Typography>
                            }
                        </div>
                    </form>
                </div><br/>
                <AppBar position="static">
                    <GoodsTable
                        handleMouseEnterCallback={handleMouseEnter}
                        handleMouseLeaveCallback={handleMouseLeave}
                        sortAscCallback={handleSortGoodsAsc}
                        sortDescCallback={handleSortGoodsDesc}
                        addCallback={addGoods}
                        editCallback={editGoods}
                        deleteCallback={removeGoods}
                        goods = {
                            filteredByIdGoods.length > 0 ?
                            filteredByIdGoods:
                            filteredByTitleGoods.length > 0 ?
                            filteredByTitleGoods :
                            filteredByPriceGoods.length > 0?
                            filteredByPriceGoods :
                            filteredByDescriptionGoods.length > 0?
                            filteredByDescriptionGoods : allGoods
                        }
                        total = {total}
                        pageNumber = {pageNumber + 1}
                        selected = {
                            filteredByIdGoods.length > 0 ?
                            filteredByIdGoods.length:
                            filteredByTitleGoods.length > 0 ?
                            filteredByTitleGoods.length :
                            filteredByPriceGoods.length > 0?
                            filteredByPriceGoods.length :
                            filteredByDescriptionGoods.length > 0?
                            filteredByDescriptionGoods.length : 0
                        }
                    />
                </AppBar><br/>
                <div>
                    <table>
                        <tr className="table">
                            <td>
                               <Button
                                   size="large"
                                   variant="contained"
                                   color="secondary"
                                   type="reset"
                                   onClick={addGoods}
                               >
                                   Add Goods
                               </Button>
                            </td>
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

export default AdminPage;