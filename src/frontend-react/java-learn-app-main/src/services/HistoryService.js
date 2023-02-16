import axios from 'axios';

const ORDER_API_BASE_URL = "http://localhost:8081/orders/";

class HistoryService {

    getLastFiveHistoryByOrderId(orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/history");
    }

    getAllHistoryByOrderId(orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/history?buttonValue=Show All");
    }
}

export default new HistoryService()