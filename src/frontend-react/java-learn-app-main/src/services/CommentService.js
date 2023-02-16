import axios from 'axios';

const ORDER_API_BASE_URL = "http://localhost:8081/orders/";

class CommentService {

    createComment(comment, orderId) {
        return axios.post(ORDER_API_BASE_URL + orderId + "/comments", comment);
    }

    getLastFiveCommentsByOrderId(orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/comments");
    }

    getAllCommentsByOrderId(orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/comments?buttonValue=Show All");
    }
}

export default new CommentService()