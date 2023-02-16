import axios from 'axios';

const ORDERS_API_BASE_URL = "http://localhost:8081/orders"

class OrderService {

    addProductToOrder(product) {
        return axios.post(ORDERS_API_BASE_URL + '?buttonValue=Add Goods', product);
    }

    removeProductFromOrder(product) {
        return axios.post(ORDERS_API_BASE_URL + '?buttonValue=Remove Goods', product);
    }

    saveOrder(order) {
        return axios.post(ORDERS_API_BASE_URL + '?buttonValue=Submit', order);
    }

    getOrderById(orderId) {
        return axios.get(ORDERS_API_BASE_URL + '/' + orderId);
    }

    getAllOrdersByPages = (pageSize, pageNumber) => {
        return axios.get(ORDERS_API_BASE_URL + '?pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllSortedOrdersByPages = (sortField, pageSize, pageNumber) => {
        return axios.get(ORDERS_API_BASE_URL + '?sortField=' + sortField + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllDescSortedOrdersByPages = (sortField, pageSize, pageNumber) => {
        return axios.get(ORDERS_API_BASE_URL + '?sortField=' + sortField + '&sortDirection=desc' + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    logoutGoods(product) {
        return axios.post(ORDERS_API_BASE_URL + '?buttonValue=LogOut', product);
    }

    logoutOrder(orderId) {
        return axios.get(ORDERS_API_BASE_URL + '/' + orderId + '?buttonValue=LogOut');
    }

    getTotalAmount() {
        return axios.get(ORDERS_API_BASE_URL + '/total');
    }
}

export default new OrderService()