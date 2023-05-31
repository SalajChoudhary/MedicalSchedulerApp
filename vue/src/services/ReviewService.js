import axios from 'axios';
const http = axios.create({
    baseURL:"http://medicalscheduler-env-1.eba-gjvnpgad.us-east-1.elasticbeanstalk.com"
});

export default {
    getReviewByPatientId(id) {
        return http.get(`/reviews/${id}`);
    },
    getReviewsByDoctor(id) {
        return http.get(`/reviews/doctor/${id}`);
    },
    getAllReviews() {
        return http.get('/reviews');
    },
    createReview(review) {
        return http.post('/reviews', review);
    },
    deleteReview(id) {
        return http.delete(`/reviews/${id}`)
    }
}