import React from "react";
import FeedbacksTable from "./FeedbacksTable";
import { Link } from "react-router-dom";
import FeedbackService from '../services/FeedbackService';

import {
    Button,
    Typography,
    TextField,
    FormControl
} from "@material-ui/core";

class FeedbackPage extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            orderId: this.props.match.params.orderId,
            rate: '',
            text: '',
            orderFeedbacks: [],
            accessFeedbackError: '',
            haveAccess: true,
            createFeedbackError: [],
            isInvalidFeedback: false
        }
        this.handleRateChange = this.handleRateChange.bind(this);
        this.handleTextChange = this.handleTextChange.bind(this);
        this.handleSubmitFeedback = this.handleSubmitFeedback.bind(this);
    }

    componentDidMount() {
        FeedbackService.getLastFiveFeedbacksByOrderId(this.state.orderId).then(res => {
            this.setState({ orderFeedbacks: res.data });
        });
    }

    handleRateChange = (event) => {
        this.setState({
            rate: event,
        });
    };

    handleTextChange = (event) => {
        this.setState({
            text: event.target.value,
        });
    };

    handleSubmitFeedback = () => {
        const feedback = {
            rate: this.state.rate,
            text: this.state.text
        }

        FeedbackService.createFeedback(feedback, this.state.orderId).then(res => {
            this.setState({
                orderFeedbacks: [...this.state.orderFeedbacks, feedback],
            });

            this.props.history.push(`/orders/${this.state.orderId}`);
        }).catch(err => {
            if (err.response.status === 403) {
                this.setState({ haveAccess: false });
                this.setState({ accessFeedbackError: err.response.data });
                this.setState({ isInvalidFeedback: false });
            }
            if (err.response.status === 400) {
                this.setState({ isInvalidFeedback: true });
                this.setState({ createFeedbackError: err.response.data })
            }
        })
    };

    handleShowAllFeedbacks = (event) => {
        FeedbackService.getAllFeedbacksByOrderId(this.state.orderId).then((res) => {
            this.setState({ orderFeedbacks: res.data })
        });
    }

    render() {
        const { orderFeedbacks, orderId, haveAccess, accessFeedbackError, isInvalidFeedback } = this.state;

        const { handleTextChange, handleRateChange, handleSubmitFeedback, handleShowAllFeedbacks } = this;

        const userRole = sessionStorage.getItem("userRole")

        return (
            <div className="container">
                <div className="order-data-container__title" align='center'>
                    {userRole === "ROLE_ADMIN" ?
                        <div className="buttons-container">
                            <Button component={Link} to="/ordersList"
                                variant="contained"
                                color="secondary">
                                Orders List
                            </Button>
                            <Button component={Link} to={`/orders/${orderId}`}
                                variant="contained"
                                color="primary"
                            >
                                Back To Order
                            </Button>
                        </div>
                            :
                        <div className="buttons-container">
                            <Button component={Link} to={`/orders/${orderId}`}
                                variant="contained"
                                color="primary"
                            >
                                Back To Order
                            </Button>
                        </div>
                    }
                    <br/>
                    <Typography variant="h4">
                        Order №{orderId}
                    </Typography><br/>
                    <Typography variant="h5">
                        Add Feedback
                    </Typography><br/>
                    {
                        !haveAccess &&
                            <Typography className="has-error" component="h6" variant="h5" align='center'>
                                {accessFeedbackError.info}
                            </Typography>
                    }
                    {
                        isInvalidFeedback &&
                            <Typography className="has-error" component="h6" variant="h5" align='center'>
                                <h2>Please, rate your satisfaction with the solution:</h2>
                            </Typography>
                    }
                    <FormControl>
                        <div className="feedback-rate">
                            <button onClick={() => handleRateChange("terrible")}>Terrible</button>
                            <button onClick={() => handleRateChange("bad")}>Bad</button>
                            <button onClick={() => handleRateChange("medium")}>Medium</button>
                            <button onClick={() => handleRateChange("good")}>Good</button>
                            <button onClick={() => handleRateChange("great")}>Great</button>
                        </div>
                        <TextField
                            label="Comment"
                            multiline
                            rows={4}
                            variant="outlined"
                            className="creation-text-field creation-text-field_width680"
                            onChange={handleTextChange}
                        />
                        <div className="order-creation-form-container__navigation-container" align='right'>
                            <Button
                                onClick={() => handleSubmitFeedback()}
                                variant="contained"
                                color="secondary"
                            >
                                Submit
                            </Button>
                        </div>
                    </FormControl>
                    <Typography align="center" variant="h5">
                        Feedbacks:
                    </Typography>
                    <FeedbacksTable
                        feedbacks={orderFeedbacks}
                        showAllFeedbacksCallback={handleShowAllFeedbacks}
                    />
                </div>
            </div>
        );
    }
}

export default FeedbackPage;