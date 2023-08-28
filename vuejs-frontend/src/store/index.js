import Vuex from 'vuex';
import Vue from 'vue';
import authentication from './modules/authentication';

// Load Vuex
Vue.use(Vuex);

// Create store
export default new Vuex.Store({
    modules: {
        authentication
    }
});