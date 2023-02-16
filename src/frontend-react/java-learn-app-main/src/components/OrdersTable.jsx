import React from "react";
import PropTypes from "prop-types";

import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@material-ui/core";

import { ORDERS_TABLE_COLUMNS } from "../constants/tablesColumns";

class OrdersTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      page: 0,
      rowsPerPage: 10,
    };
  }

  render() {

    const { orders, sortAscCallback, sortDescCallback, handleClickTotalPriceCallback,
            getOrderInfoCallback, total, pageNumber } = this.props;

    const { page, rowsPerPage } = this.state;

    return (
       <Paper>
         <TableContainer>
           <Table style={{borderTop: '2px solid black'}}>
             <TableHead style={{borderTop: '2px solid black', borderBottom: '2px solid black'}} align="center">
              <TableRow>
                {ORDERS_TABLE_COLUMNS.map((column) => (
                  <TableCell style={{borderRight: '2px solid black', borderLeft: '2px solid black'}}>
                     <div key={column.id}>
                        {column.id !== 'moreDetails' ?
                            <div class="field"><b>{column.label}</b></div>
                                :
                            <div><b>{column.label}</b></div>
                        }
                        {column.label !== 'More Details' &&
                            <div class="up-arrow" onClick={(e) => sortAscCallback(e, column.id)}></div>
                        }
                        {column.label !== 'More Details' &&
                            <div class="down-arrow" onClick={(e) => sortDescCallback(e, column.id)}></div>
                        }
                    </div>
                  </TableCell>
                ))}
              </TableRow>
             </TableHead>
             <TableBody>
              {orders
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row, index) => {
                  return (
                    <TableRow style={{borderTop: '2px solid black', borderBottom: '2px solid black'}}
                        hover role="checkbox"
                        align="center"
                        key={index}>
                        {ORDERS_TABLE_COLUMNS.map((column) => {
                            const value = row[column.id];
                            if (column.id === "totalPrice") {
                                return (
                                    <TableCell style={{borderRight: '2px solid black', borderLeft: '2px solid black'}}
                                        key={row.id}>
                                            <a className="box" style={{ textDecoration: 'underline' }}
                                               onClick={(event) => handleClickTotalPriceCallback(row.id, event)}
                                            >
                                                {value}
                                            </a>
                                    </TableCell>
                                );
                            }
                            if (column.id === "moreDetails") {
                              return (<TableCell align="center" style={{borderRight: '2px solid black', borderLeft: '2px solid black'}}
                                key={column.id}>
                                    <button
                                        onClick={() => getOrderInfoCallback(row.id)}
                                        variant="contained"
                                        color="primary"
                                    >
                                        info
                                    </button>
                              </TableCell>);
                            }
                             else {
                                return <TableCell style={{borderRight: '2px solid black', borderLeft: '2px solid black'}}
                                            key={column.id}>{value}
                                        </TableCell>;
                            }
                        })}
                    </TableRow>
                  );
                })}
            </TableBody>
         </Table><br/>
         <Typography align="right" component="h5" variant="h5">
             Page: {pageNumber}
         </Typography>
         <Typography align="right" component="h5" variant="h5">
             Total: {total}
         </Typography>
       </TableContainer>
      </Paper>
    );
  }
}

OrdersTable.propTypes = {
  handleClickTotalPriceCallback: PropTypes.func,
  orders: PropTypes.array,
  total: PropTypes.number,
  pageNumber: PropTypes.number
};

export default OrdersTable;