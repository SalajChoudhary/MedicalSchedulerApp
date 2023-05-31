import axios from 'axios';

const http = axios.create({
  baseURL:"http://medicalscheduler-env-1.eba-gjvnpgad.us-east-1.elasticbeanstalk.com"
});

export default {

  login(user) {
    return http.post('/login', user)
  },

  register(user) {
    return http.post('/register', user)
  }

}
