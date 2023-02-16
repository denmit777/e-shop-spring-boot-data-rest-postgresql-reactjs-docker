import axios from 'axios';

const ORDER_API_BASE_URL = "http://localhost:8081/orders/";

class FeedbackService {

    createFeedback(feedback, orderId) {
        return axios.post(ORDER_API_BASE_URL + orderId + "/feedbacks", feedback);
    }

    getLastFiveFeedbacksByOrderId(orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/feedbacks");
    }

    getAllFeedbacksByOrderId(orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/feedbacks?buttonValue=Show All");
    }
}

export default new FeedbackService()