<template>
  <div class="wrapper" :class="{ 'nav-open': $sidebar.showSidebar }">
    <notifications></notifications>

    <side-bar
        :sidebar-item-color="sidebarBackground"
        :sidebar-background-image="sidebarBackgroundImage"
    >
      <!--      <sidebar-link v-show="!userId" to="/login-register">-->
      <!--        <md-icon>person</md-icon>-->
      <!--        <p>Login/Register</p>-->
      <!--      </sidebar-link>-->
      <div v-show="!userId" class="md-layout-item md-size-100 text-left">
        <md-button class="md-raised md-success" style="width: 100%" @click="login">Prijavi se</md-button>
        <md-button class="md-raised md-success" style="width: 100%" @click="register">Registruj se</md-button>
      </div>
      <sidebar-link v-show="userId" to="/ad">
        <md-icon>add</md-icon>
        <p>Dodaj oglas</p>
      </sidebar-link>
      <sidebar-link v-show="userId" to="/user">
        <md-icon>person</md-icon>
        <p>Profil korisnika</p>
        <md-badge v-if="notificationsCount && notificationsCount>0" style="margin-left: 10px"
                  :md-content="notificationsCount"></md-badge>
      </sidebar-link>

      <div class="md-layout-item md-medium-size-100 md-size-100">
        <filters-form data-background-color="blue"></filters-form>
      </div>
    </side-bar>

    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content></dashboard-content>
    </div>
  </div>
</template>

<script>
import TopNavbar from "./TopNavbar.vue";
import DashboardContent from "./Content.vue";
import {mapGetters, mapActions} from "vuex";
import FiltersForm from "@/pages/Filters/FiltersForm.vue";
import EventBus from "./../../event-bus";
import axios from "axios";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default {
  components: {
    TopNavbar,
    DashboardContent,
    FiltersForm,
  },
  computed: mapGetters(["userId", "apiUrl", "websocketUrl"]),
  created() {
    EventBus.$on('notificationsCount', data => {
      this.notificationsCount = data;
    });
    EventBus.$on('notificationsCountDecrees', () => {
      this.notificationsCount--;
    });
    EventBus.$on('userLoggedIn', () => {
      this.connectToSocket();
    });
  },
  mounted() {
    if (this.userId) {
      this.connectToSocket();
    }
  },
  data() {
    return {
      notificationsCount: 0,
      sidebarBackground: "green",
      sidebarBackgroundImage: require("@/assets/img/sidebar.jpg"),
    };
  },
  methods: {
    ...mapActions(["removeId"]),
    connectToSocket(){
      {
        axios
            .get(`${this.apiUrl}/api/v1/notification/user/${this.userId}`)
            .then((res) => {
              this.notificationsCount = res.data.count;
            }).catch((error) => {
              //todo clear cookie
          this.removeId();
          window.location.href = "http://client-server:8081/auth/login";
        });

        // Connect to WebSocket
        const socket = new SockJS(`${this.websocketUrl}/ws`); // Replace with your Spring Boot backend URL
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
          stompClient.subscribe(`/user/${this.userId}/queue/notifications`, () => {
            this.notificationsCount++;
            EventBus.$emit("newNotificationReceived");
          });
        });
      }
    },
    login() {
      this.removeId();
      window.location.href = "http://client-server:8081/auth/login";
    },
    register() {
      window.location.href = `http://auth-server:9000/register/?redirectUrl=${encodeURIComponent('http://client-server:8081/auth/login')}`;
    }
  }
};
</script>
