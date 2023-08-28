<template>
  <md-card v-if="email != null" class="md-card-profile">
    <div class="md-card-avatar">
      <img class="img" :src="cardUserImage"/>
    </div>

    <md-card-content>
      <h6 class="category text-gray">korisnički podaci</h6>
      <h4 class="card-title">E-mail: {{ email }}</h4>
      <md-button class="md-round md-success" @click="$emit('fetchUsersAds')">Moji oglasi</md-button>
      <md-button class="md-round md-danger" @click="logout">Odjavi me</md-button>
      <md-list v-if="savedFilters.length > 0" :md-expand-single="false">
        <md-list-item md-expand>
          <md-icon>filter_alt</md-icon>
          <md-badge v-if="totalUnreadNotificationsCount && totalUnreadNotificationsCount>0"
                    style="margin-right: 50px;margin-top: 15px;position: absolute"
                    :md-content="totalUnreadNotificationsCount"></md-badge>
          <span class="md-list-item-text">Sačuvane pretrage</span>
          <md-list slot="md-expand">
            <md-list-item v-for="filter in savedFilters" :key="filter.id">
              <div class="filters-list" @click="getFilter(filter.id)">
                {{ filter.tittle }}
                <md-icon class="mail-icon" :class="{ 'mail-icon-cros': !filter.subscribed }">forward_to_inbox</md-icon>
                <md-badge v-if="filter.newNotificationsCount && filter.newNotificationsCount>0" style="float: right"
                          :md-content="filter.newNotificationsCount"></md-badge>
              </div>
              <dialog-delete-filter :filter="filter"></dialog-delete-filter>
            </md-list-item>
          </md-list>
        </md-list-item>
      </md-list>
    </md-card-content>
  </md-card>
</template>

<script>
import {mapGetters, mapActions} from "vuex";
import axios from "axios";
import DialogDeleteFilter from "../../components/Dialogs/DialogDeleteFilter";
import EventBus from "./../../event-bus";

export default {
  components: {DialogDeleteFilter},
  name: "user-card",
  props: {
    cardUserImage: {
      type: String,
      default: require("@/assets/img/user.jpg"),
    },
  },

  computed: {
    ...mapGetters(["userId", "apiUrl"]),
    totalUnreadNotificationsCount() {
      return this.savedFilters.reduce((sum, filter) => sum + filter.newNotificationsCount, 0);
    }
  },
  watch: {
    totalUnreadNotificationsCount() {
      EventBus.$emit('notificationsCount', this.totalUnreadNotificationsCount);
    }
  },
  methods: {
    ...mapActions(["removeId"]),
    fetchAllFilters() {
      axios
          .get(`${this.apiUrl}/api/v1/filter/${this.userId}`)
          .then((res) => {
            this.savedFilters = res.data.map(item => {
              return {
                ...item,
                tittle: item.title
              };
            });
          })
          .catch(() => {
            // this.removeId();
            // this.$router.push({ name: "Login Register" });
          });
    },
    removeFromList(filterId) {
      this.savedFilters = this.savedFilters.filter(function (
          filter,
          index,
          arr
      ) {
        return filter.id !== filterId;
      });
    },
    getFilter(filterId) {
      // axios
      //   .get("http://localhost:8181/saved-filter/" + filterId, {
      //     headers: {
      //       "user-id": this.userId.toString(),
      //     },
      //   })
      //   .then((res) => {
      //     res.data.savedFilter.id=filterId;
      //
      //   })
      //   .catch(() => {
      //     // this.removeId();
      //     // this.$router.push({ name: "Login Register" });
      //   });
      this.selectedFilter = this.savedFilters.find(filter => filter.id === filterId);
      EventBus.$emit("filterSelected", this.selectedFilter);
    },
    ...mapActions(["removeId"]),
    logout() {
      this.removeId()
      window.location.href = "http://client-server:8081/auth/logout";
    },
    alert(msg) {
      alert(msg);
    },
  },
  data() {
    return {
      savedFilters: [],
      email: null,
      selectedFilter: null
    };
  },
  mounted() {
    axios
        .get(`${this.apiUrl}/api/v1/user/${this.userId}`)
        .then((res) => {
          if(res.data.email) {
            this.email = res.data.email;
            this.$emit('fetchUsersAds');
          } else {
            throw new Error("User is not authenticated!")
          }
        })
        .catch(() => {
          this.removeId()
          window.location.href = "http://client-server:8081/auth/login";
        });

    this.fetchAllFilters();

    EventBus.$on("filterSaved", (filter) => {
      console.log("SAVED: " + JSON.stringify(filter))
      this.savedFilters.push({
        ...filter,
        tittle: filter.title
      });
    });
    EventBus.$on("filterDeleted", data => {
      this.removeFromList(data)
    });

    EventBus.$on("newNotificationReceived", () => {
      this.fetchAllFilters()
      if (this.selectedFilter) {
        this.selectedFilter.newNotificationsCount++;
        EventBus.$emit("filterSelected", this.selectedFilter);
      }
    });

    EventBus.$on('seenNotification', (filterId) => {
      const filterToUpdate = this.savedFilters.find(filter => filter.id === filterId);
      if (filterToUpdate) {
        filterToUpdate.newNotificationsCount--;
      }
    });
    EventBus.$on('seenAllNotifications', (filterId) => {
      const filterToUpdate = this.savedFilters.find(filter => filter.id === filterId);
      if (filterToUpdate) {
        filterToUpdate.newNotificationsCount = 0;
      }
    });
  }
  ,
}
;
</script>
<style scoped>
.filters-list {
  padding: 10px;
  width: 80%;
  height: 40px;
}

:hover.filters-list {
  background-color: #c8c8c8;
  cursor: pointer;
  border-radius: 5px;
}

.mail-icon {
  margin-left: 10px !important;
  padding-bottom: 5px;
  float: right;
}

.mail-icon-cros:after {
  position: absolute;
  content: "/";
  color: rgba(0, 0, 0, 0.54);
  font-weight: 700;
  font-size: 1.7em;
  float: right;
  top: 4px;
}
</style>
