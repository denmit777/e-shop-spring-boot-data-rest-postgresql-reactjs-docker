import axios from 'axios';

const GOODS_API_BASE_URL_FOR_BUYER = "http://localhost:8081/goods/forBuyer"
const GOODS_API_BASE_URL_FOR_ADMIN = "http://localhost:8081/goods/forAdmin"

class GoodService {

    addGood(good) {
        return axios.post(GOODS_API_BASE_URL_FOR_ADMIN, good);
    }

    getGoodById(goodId) {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '/' + goodId);
    }

    updateGood(good, goodId) {
        return axios.put(GOODS_API_BASE_URL_FOR_ADMIN + '/' + goodId, good);
    }

    deleteGood(goodId) {
        return axios.delete(GOODS_API_BASE_URL_FOR_ADMIN + '/' + goodId);
    }

    getAllGoodsForBuyer() {
        return axios.get(GOODS_API_BASE_URL_FOR_BUYER);
    }

    getAllGoodsByPages = (pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllGoodsSearchedById = (parameter, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=id&parameter=' + parameter + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllGoodsSearchedByTitle = (parameter, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=title&parameter=' + parameter + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllGoodsSearchedByPrice = (parameter, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=price&parameter=' + parameter + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllGoodsSearchedByDescription = (parameter, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=description&parameter=' + parameter + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllSortedGoodsByPages = (sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?&sortField=' + sortField + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllDescendingSortedGoodsByPages = (sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?sortField=' + sortField + '&sortDirection=desc' + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllSortedGoodsSearchedById = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=id&parameter=' + parameter + '&sortField=' + sortField + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllSortedGoodsSearchedByTitle = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=title&parameter=' + parameter + '&sortField=' + sortField + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllSortedGoodsSearchedByPrice = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=price&parameter=' + parameter + '&sortField=' + sortField + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllSortedGoodsSearchedByDescription = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=description&parameter=' + parameter + '&sortField=' + sortField + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllDescendingSortedGoodsSearchedById = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=id&parameter=' + parameter + '&sortField=' + sortField + '&sortDirection=desc' + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllDescendingSortedGoodsSearchedByTitle = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=title&parameter=' + parameter + '&sortField=' + sortField + '&sortDirection=desc' + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllDescendingSortedGoodsSearchedByPrice = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=price&parameter=' + parameter + '&sortField=' + sortField + '&sortDirection=desc' + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getAllDescendingSortedGoodsSearchedByDescription = (parameter, sortField, pageSize, pageNumber) => {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '?searchField=description&parameter=' + parameter + '&sortField=' + sortField + '&sortDirection=desc' + '&pageSize=' + pageSize + '&pageNumber=' + pageNumber);
    }

    getTotalAmount() {
        return axios.get(GOODS_API_BASE_URL_FOR_ADMIN + '/total');
    }
}

export default new GoodService()