import React from "react";
import PropTypes from "prop-types";
import icons from 'glyphicons'

import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography
} from "@material-ui/core";

import { GOODS_TABLE_COLUMNS } from "../constants/tablesColumns";

class GoodsTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      page: 0,
      rowsPerPage: 15,
    };
  }

  render() {

    const { goods, sortAscCallback, sortDescCallback, editCallback, deleteCallback,
            handleMouseEnterCallback, handleMouseLeaveCallback, total, selected, pageNumber } = this.props;

    const { page, rowsPerPage, showText, description } = this.state;

    return (
       <Paper>
         <TableContainer>
           {showText &&
             <div align = 'center'>
                <Typography
                    component="h5"
                    variant="h5"
                    style={{color: 'blue'}}
                >
                    {description}
                </Typography>
             </div>
           }
           <Table style={{borderTop: '2px solid black'}}>
             <TableHead style={{borderTop: '2px solid black', borderBottom: '2px solid black'}} align="center">
              <TableRow>
                <TableCell style={{visibility: 'none'}}></TableCell>
                {GOODS_TABLE_COLUMNS.map((column) => (
                  <TableCell style={{borderRight: '2px solid black', borderLeft: '2px solid black'}}>
                    <div key={column.id}>
                        {column.id !== 'quantity' ?
                            <div class="field"><b>{column.label}</b></div>
                                :
                            <div><b>{column.label}</b></div>
                        }
                        {column.label !== 'Quantity' &&
                            <div class="up-arrow" onClick={(e) => sortAscCallback(e, column.id)}></div>
                        }
                        {column.label !== 'Quantity' &&
                            <div class="down-arrow" onClick={(e) => sortDescCallback(e, column.id)}></div>
                        }
                    </div>
                  </TableCell>
                ))}
                <TableCell style={{visibility: 'none'}}></TableCell>
              </TableRow>
             </TableHead>
             <TableBody>
              {goods
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row, index) => {
                  return (
                    <TableRow style={{borderTop: '2px solid black', borderBottom: '2px solid black'}}
                        hover role="checkbox"
                        align="center"
                        key={index}>
                        <TableCell>
                            <button
                                onClick={() => editCallback(row.id)}
                                variant="contained"
                                color="primary"
                            >
                                <span>{icons.pencil}</span>
                            </button>
                        </TableCell>
                        {GOODS_TABLE_COLUMNS.map((column) => {
                            const value = row[column.id];
                            if (column.id === "title") {
                                return (
                                    <TableCell style={{borderRight: '2px solid black', borderLeft: '2px solid black'}}
                                        key={row.id}>
                                        <a style={{ textDecoration: 'underline' }}
                                            onMouseEnter={(event) => handleMouseEnterCallback(row.id, event)}
                                            onMouseLeave={handleMouseLeaveCallback}
                                        >
                                            {value}
                                        </a>
                                    </TableCell>
                                );
                            } else {
                                return <TableCell style={{borderRight: '2px solid black', borderLeft: '2px solid black'}}
                                            key={column.id}>{value}
                                        </TableCell>;
                            }
                        })}
                        <TableCell>
                             <button key={row.id}
                                 onClick={() => deleteCallback(row.id)}
                                 variant="contained"
                                 color="primary"
                             >
                                 <span>{icons.cross}</span>
                             </button>
                        </TableCell>
                    </TableRow>
                  );
                })}
            </TableBody>
         </Table><br/>
         <Typography align="right" component="h5" variant="h5">
             Page: {pageNumber}
         </Typography>
         {selected !== 0 &&
             <Typography align="right" component="h5" variant="h5">
                 Selected: {selected}
             </Typography>
         }
         <Typography align="right" component="h5" variant="h5">
             Total: {total}
         </Typography>
       </TableContainer>
      </Paper>
    );
  }
}

GoodsTable.propTypes = {
  handleMouseEnterCallback: PropTypes.func,
  handleMouseLeaveCallback: PropTypes.func,
  addCallback: PropTypes.func,
  editCallback: PropTypes.func,
  deleteCallback: PropTypes.func,
  goods: PropTypes.array,
  total: PropTypes.number,
  selected: PropTypes.number,
  pageNumber: PropTypes.number
};

export default GoodsTable;
