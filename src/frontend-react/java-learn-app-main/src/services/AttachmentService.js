import axios from 'axios';

const ORDER_API_BASE_URL = "http://localhost:8081/orders/";

class AttachmentService {

    getAllAttachmentsByOrderId(orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/attachments");
    }

    uploadAttachment(attachment, orderId) {
        return axios.post(ORDER_API_BASE_URL + orderId + "/attachments", attachment);
    }

    getAttachmentById(attachmentId, orderId) {
        return axios.get(ORDER_API_BASE_URL + orderId + "/attachments/" + attachmentId, {
            responseType: "blob"
        });
    }

    deleteAttachment(attachmentName, orderId) {
        return axios.delete(ORDER_API_BASE_URL + orderId + "/attachments/" + attachmentName);
    }
}

export default new AttachmentService()