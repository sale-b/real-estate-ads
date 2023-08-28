// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueRouter from "vue-router";
import App from "./App";
import store from './store'

// router setup
import routes from "./routes/routes";

// Plugins
import GlobalDirectives from "./globalDirectives";
import Notifications from "./components/NotificationPlugin";

// MaterialDashboard plugin
import MaterialDashboard from "./material-dashboard";


// configure router
const router = new VueRouter({
  mode: 'history',
  routes, // short for routes: routes
  linkExactActiveClass: "nav-item active"
});

Vue.config.productionTip = false

Vue.use(VueRouter);
Vue.use(MaterialDashboard);
Vue.use(GlobalDirectives);
Vue.use(Notifications);



/* eslint-disable no-new */
new Vue({
  el: "#app",
  render: h => h(App),
  store,
  router,
  data: {
  }
});