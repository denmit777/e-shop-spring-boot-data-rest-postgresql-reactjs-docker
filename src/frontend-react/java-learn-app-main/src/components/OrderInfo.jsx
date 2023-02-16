import React from "react";
import { Link } from "react-router-dom";
import OrderService from '../services/OrderService';
import AttachmentService from '../services/AttachmentService';
import HistoryService from '../services/HistoryService';
import HistoryTable from "./HistoryTable";
import CommentsTable from "./CommentsTable";
import CommentService from '../services/CommentService';
import TabPanel from "./TabPanel";

import {
  Button,
  TableRow,
  Typography,
  TextField,
  FormControl,
  Tab,
  Tabs,
} from "@material-ui/core";

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}

class OrderInfo extends React.Component {
  constructor(props) {
     super(props)

     this.state = {
        order: {},
        orderGoods: [],
        description: '',
        attachments: [],
        orderHistory: [],
        orderComments: [],
        attachmentId: null,
        selectedFile: null,
        fileName: '',
        comment: '',
        attachmentUploadError: [],
        isInvalidAttachment: false,
        fileNotFoundError: '',
        isFileSelected: true,
        isFilePresent: true,
        createCommentError: [],
        isInvalidComment: false,
        tabValue: 0
     }

      this.addFile = this.addFile.bind(this);
      this.deleteFile = this.deleteFile.bind(this);
      this.downloadFileById = this.downloadFileById.bind(this);
      this.handleAttachmentChange = this.handleAttachmentChange.bind(this);
      this.handleEnterComment = this.handleEnterComment.bind(this);
      this.addComment = this.addComment.bind(this);
      this.handleLogout = this.handleLogout.bind(this);
  }

  componentDidMount() {
      const orderId = sessionStorage.getItem("orderId");
      const orderViewId = sessionStorage.getItem("orderViewId");
      const userRole = sessionStorage.getItem("userRole")

      if (userRole !== 'ROLE_ADMIN') {
          OrderService.getOrderById(orderId).then(res => {
             this.setState({ order: res.data });
             this.setState({ orderGoods: res.data.goods });
          });

          AttachmentService.getAllAttachmentsByOrderId(orderId).then(res => {
             this.setState({ attachments: res.data });
          });

          HistoryService.getLastFiveHistoryByOrderId(orderId).then(res => {
              this.setState({ orderHistory: res.data });
          });

          CommentService.getLastFiveCommentsByOrderId(orderId).then(res => {
              this.setState({ orderComments: res.data });
          });
      } else {
          OrderService.getOrderById(orderViewId).then((res) => {
              this.setState({ description: res.data.description });
          });

          HistoryService.getLastFiveHistoryByOrderId(orderViewId).then(res => {
              this.setState({ orderHistory: res.data });
          });

          CommentService.getLastFiveCommentsByOrderId(orderViewId).then(res => {
              this.setState({ orderComments: res.data });
          });
      }
  }

  handleAttachmentChange = (event) => {
       this.setState({
           selectedFile: event.target.files[0]
       });
  };

  handleEnterComment = (event) => {
      this.setState({
        comment: event.target.value,
      });
  };

  handleTabChange = (event, value) => {
      this.setState({
          tabValue: value,
      });
  };

  addFile = (event) => {
      const orderId = sessionStorage.getItem("orderId");

      const attachment = new FormData();

      attachment.append(
        "file", this.state.selectedFile,
      );

      if (this.state.selectedFile != null) {
          AttachmentService.uploadAttachment(attachment, orderId).then(res => {
              this.setState({ attachments: res.data });
              this.setState({ attachmentUploadError: [] });
              this.setState({ isFileSelected: true });
              this.setState({ fileNotFoundError: '' })
          }).catch(err => {
                this.setState({ isInvalidAttachment: true });
                this.setState({ attachmentUploadError: err.response.data });
                this.setState({ fileNotFoundError: '' });
                this.setState({ isFileSelected: true });
          })
      } else {
           this.setState({ isFileSelected: false });
           this.setState({ attachmentUploadError: [] });
           this.setState({ fileNotFoundError: '' })
      }
  }

  deleteFile = (event) => {
      const orderId = sessionStorage.getItem("orderId");

      if (this.state.selectedFile != null) {
          AttachmentService.deleteAttachment(this.state.selectedFile.name, orderId).then(res => {
              this.setState({ attachmentUploadError: [] });
              this.setState({ attachments: res.data });
              this.setState({ isFileSelected: true });
              this.setState({ fileNotFoundError: '' })
          }).catch(err => {
                if (err.response.status === 400) {
                    this.setState({ isInvalidAttachment: true });
                    this.setState({ attachmentUploadError: err.response.data });
                    this.setState({ fileNotFoundError: '' });
                    this.setState({ isFileSelected: true });
                }
                if (err.response.status === 404) {
                    this.setState({ isFilePresent: false });
                    this.setState({ fileNotFoundError: err.response.data.info });
                    this.setState({ isFileSelected: true });
                }
          })
      } else {
           this.setState({ isFileSelected: false });
           this.setState({ attachmentUploadError: [] });
           this.setState({ fileNotFoundError: '' })
      }
  }

  downloadFileById = (attachmentId, fileName) => {
      const orderId = sessionStorage.getItem("orderId");

      AttachmentService.getAttachmentById(attachmentId, orderId).then(res => {
          const url = window.URL.createObjectURL(new Blob([res.data]));
          const link = document.createElement("a");

          link.href = url;
          link.setAttribute(
             "download",
             fileName
          );

          document.body.appendChild(link);

          link.click();

          link.parentNode.removeChild(link);
      });
  }

  addComment = () => {
      const orderId = sessionStorage.getItem("orderId");

      const comment = {
         text: this.state.comment
      }

      CommentService.createComment(comment, orderId).then(res => {
         this.setState({ createCommentError: [] })
      }).catch(err => {
         this.setState({ isInvalidComment: true });
         this.setState({ createCommentError: err.response.data })
      })
  }

  handleShowAllHistory = (event) => {
      const userRole = sessionStorage.getItem("userRole")
      const orderId = sessionStorage.getItem("orderId");
      const orderViewId = sessionStorage.getItem("orderViewId");

      if (userRole !== 'ROLE_ADMIN') {
          HistoryService.getAllHistoryByOrderId(orderId).then((res) => {
              this.setState({ orderHistory: res.data })
          });
      } else {
          HistoryService.getAllHistoryByOrderId(orderViewId).then((res) => {
              this.setState({ orderHistory: res.data })
          });
      }
  }

  handleShowAllComments = (event) => {
      const userRole = sessionStorage.getItem("userRole")
      const orderId = sessionStorage.getItem("orderId");
      const orderViewId = sessionStorage.getItem("orderViewId");

      if (userRole !== 'ROLE_ADMIN') {
          CommentService.getAllCommentsByOrderId(orderId).then((res) => {
              this.setState({ orderComments: res.data })
          });
      } else {
          CommentService.getAllCommentsByOrderId(orderViewId).then((res) => {
              this.setState({ orderComments: res.data })
          });
      }
  }

  handleLogout = () => {
      const orderId = sessionStorage.getItem("orderId");

      OrderService.logoutOrder(orderId);

      window.location.href = "/";
  };

  render() {
      const { orderGoods, order, description, attachments, comment, isInvalidAttachment,
              attachmentUploadError, isFileSelected, isFilePresent, fileNotFoundError, orderHistory,
              orderComments, isInvalidComment, createCommentError, tabValue } = this.state;

      const { downloadFileById, handleAttachmentChange, addFile, deleteFile, handleEnterComment,
              addComment, handleShowAllHistory, handleShowAllComments, handleTabChange, handleLogout } = this;

      const userName = sessionStorage.getItem("userName");
      const orderId = sessionStorage.getItem("orderId");
      const orderViewId = sessionStorage.getItem("orderViewId");
      const userRole = sessionStorage.getItem("userRole")

      return (
         <div className="container">
            {userRole === "ROLE_BUYER" ?
                <div align='center'>
                    <div className="container__title-wrapper">
                        <Typography component="h2" variant="h4">
                            Dear {userName}, your order:
                        </Typography>
                    </div><br/>
                    <div>
                        <Typography component="h2" variant="h5">
                            {orderGoods.map((good, index) => (
                                <div align='center'>
                                    {index + 1 + ") "}
                                    {good.title + " "}
                                    {good.price + " $"}
                                </div>
                            ))}
                            <br/>
                            <div align='center'>
                                Total: $ {order.totalPrice}
                            </div>
                            <br/>
                        </Typography>
                    </div>
                    <div>
                        {attachments.length > 0 ?
                            <Typography component="h6" variant="h5">
                                Your files:
                            </Typography> :
                            <Typography component="h6" variant="h5">
                                You can add files to your order
                            </Typography>
                        }
                    </div><br/>
                    <div>
                        <Typography variant="subtitle1">
                            {attachments.map((item, index) => {
                                return (
                                    <TableRow key={index}>
                                        <Link to={`orders/${orderId}/attachments/${item.id}`}
                                            onClick = {() => downloadFileById(item.id, item.name)}
                                        >
                                            <table>
                                                <tr>
                                                    <td className="table">
                                                        {index + 1 + ". "}
                                                        {item.name}
                                                    </td>
                                                </tr>
                                            </table>
                                        </Link>
                                    </TableRow>
                                );
                            })}
                        </Typography>
                    </div><br/>
                    {
                        isInvalidAttachment &&
                            <Typography className="has-error" component="h6" variant="h5">
                                {attachmentUploadError}
                            </Typography>
                    }
                    <div>
                        <FormControl>
                            <table>
                                <Typography variant="caption">Select attachment</Typography>
                                <tr className="table">
                                    <td>
                                        <input
                                            className="input"
                                            type="file"
                                            onChange={handleAttachmentChange}
                                        />
                                    </td>
                                </tr><br/>
                                <tr className="table">
                                    <td>
                                        <Button
                                            color="primary"
                                            variant="contained"
                                            size="large"
                                            onClick={(e) => addFile(e)}
                                        >
                                            Add File
                                        </Button>
                                    </td>
                                    <td>
                                        <Button
                                            color="secondary"
                                            variant="contained"
                                            size="large"
                                            onClick={(e) => deleteFile(e)}
                                        >
                                            Delete File
                                        </Button>
                                    </td>
                                </tr>
                            </table>
                            <div>
                                {
                                    !isFileSelected &&
                                        <Typography className="has-error" component="h6" variant="h5" align='center'>
                                            You should select the file first
                                        </Typography>
                                }
                                {
                                    !isFilePresent &&
                                        <Typography className="has-error" component="h6" variant="h5" align='center'>
                                            {fileNotFoundError}
                                        </Typography>
                                }
                            </div>
                            <div>
                                {
                                    isInvalidComment &&
                                        <Typography className="has-error" component="h6" variant="h5">
                                            {createCommentError.map((error) => (
                                                <div align='center'>
                                                    {error.text}
                                                </div>
                                            ))}
                                        </Typography>
                                }
                            </div>
                            <div className="order-data-container__enter-comment-section enter-comment-section">
                                <TextField
                                    label="Enter a comment"
                                    multiline
                                    rows={4}
                                    value={comment}
                                    variant="filled"
                                    className="comment-text-field"
                                    onChange={handleEnterComment}
                                />
                            </div>
                            <div className="submit-button-section">
                                <Button
                                    variant="contained"
                                    color="secondary"
                                    onClick={addComment}
                                >
                                    Add Comment
                                </Button>
                                <Button
                                    component={Link} to={`/feedbacks/${orderId}`}
                                    variant="contained"
                                >
                                    Leave Feedback
                                </Button>
                                <Button
                                    size="large"
                                    color="primary"
                                    variant="contained"
                                    onClick={handleLogout}
                                >
                                    Logout
                                </Button>
                            </div>
                        </FormControl>
                    </div><br/><br/>
                </div>
                    :
                <div>
                    <div>
                        <div className="container__title-wrapper">
                            <Typography component="h2" variant="h4">
                                Order №{orderViewId}
                            </Typography>
                        </div><br/>
                        <Typography component="h2" variant="h5" align='center' >
                            Ordered goods:
                            <tr className="table">
                                <td>
                                    <pre>
                                        {description}
                                    </pre>
                                </td>
                            </tr>
                        </Typography>
                    </div><br/>
                    <div align='center'>
                        <Button
                            component={Link} to={`/feedbacks/${orderViewId}`}
                            variant="contained"
                            color="secondary"
                        >
                            Feedbacks
                        </Button>
                    </div><br/>
                    <div align='center'>
                        <Button
                            component={Link} to="/ordersList"
                            variant="contained"
                            color="primary"
                        >
                            Back to orders
                        </Button>
                    </div><br/>
                </div>
            }
            <div className="order-data-container__comments-section comments-section">
                <div className="">
                    <Tabs
                        variant="fullWidth"
                        onChange={handleTabChange}
                        value={tabValue}
                        indicatorColor="primary"
                        textColor="primary"
                    >
                        <Tab label="History" {...a11yProps(0)} />
                        <Tab label="Comments" {...a11yProps(1)} />
                    </Tabs>
                    <TabPanel value={tabValue} index={0}>
                        <HistoryTable
                            history={orderHistory}
                            showAllHistoryCallback={handleShowAllHistory}
                        />
                    </TabPanel>
                    <TabPanel value={tabValue} index={1}>
                        <CommentsTable
                            comments={orderComments}
                            showAllCommentsCallback={handleShowAllComments}
                        />
                    </TabPanel>
                </div>
            </div>
         </div>
      );
  }
}

export default OrderInfo;