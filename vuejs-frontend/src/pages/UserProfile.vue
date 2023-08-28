<template>
  <div class="content">
    <div class="md-layout">
      <div class="md-layout-item md-medium-size-100 md-size-33">
        <user-card @fetchUsersAds="fetchUsersAds"></user-card>
      </div>
      <div class="md-layout">
        <div
            class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100"
        >
          <h5 v-if="notifications && notifications.length > 0">{{ heading }}</h5>
          <div style="display: flex">
            <md-button
                v-if="currentMode===mode.notifications && notifications && notifications.length > 0 && selectedFilter.newNotificationsCount > 0"
                class="md-info" style="margin-left: auto" @click.prevent="markAllAsRead">Označi sve kao pročitano
            </md-button>
          </div>
          <div>
            <div v-for="(notification, index) in notifications" :key="index">
              <div v-if="notification.seen === false"
                   style="float:right; display: flex;position: relative; top: 80px;z-index: 100">
                <a href="/" style="margin: -25px 50px 0 0;" @click.prevent="markAsRead(notification.id)">Označi kao pročitano</a>
                <md-badge style="margin: -25px 25px 0 0" class="md-primary md-round"/>
              </div>
              <a
                  :href="generateUrl(notification.realEstate.id)"
                  v-on:click.prevent="openAd(notification.id, notification.realEstate.id, index)"
              >
                <ad-card
                    :tittle="notification.realEstate.tittle"
                    :content="notification.realEstate.description.length <= 400 ? notification.realEstate.description : notification.realEstate.description.substring(0, 400) + '...'"
                    :img-url="notification.realEstate.imageUrl ? notification.realEstate.imageUrl : null"
                    :date="getFormattedDate(notification.realEstate.createdOn)"
                    :location="notification.realEstate.location"
                    :space="notification.realEstate.livingSpaceArea"
                    :rooms="notification.realEstate.roomsNumber"
                    :furniture="notification.realEstate.furniture"
                    :price="notification.realEstate.price"
                    :floor="notification.realEstate.floor"
                    :id="notification.realEstate.id"
                    ref="adDetails"
                ></ad-card>
              </a>
            </div>
          </div>
          <div class="pagination-holder">
            <pagination
                type="info"
                v-if="pageCount > 1"
                v-on:input="fetchUsersNotifications()"
                v-model="infoPagination"
                :page-count="pageCount"
            >
            </pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {UserCard} from "@/pages";
import Pagination from "./../components/Pagination";
import AdCard from "../components/Cards/AdCard";
import EventBus from "./../event-bus";
import axios from "axios";
import {mapGetters} from "vuex";

export default {
  computed: {
    ...mapGetters(["apiUrl", "userId"]),
  },
  components: {
    UserCard,
    Pagination,
    AdCard,
  },
  data() {
    return {
      notifications: null,
      infoPagination: parseInt(1),
      pageCount: 1,
      filter: null,
      selectedFilter: null,
      unseenNotificationsByFilter: null,
      mode: {
        ads: 'ADS',
        notifications: 'Obaveštenja'
      },
      currentMode: 'ADS',
      heading: 'Moji oglasi'

    };
  },
  mounted() {
    EventBus.$on("filterSelected", (filter) => {
      EventBus.$emit("loadFilters", {
        userId: parseInt(filter.userId),
        adType: filter.adType,
        type: filter.type,
        location: filter.location.id,
        microLocations: filter.microLocation ? this.set(filter.microLocation).map((element) => (element.id)) : null,
        heatingType: filter.heatingType,
        floor: filter.floor,
        furniture: filter.furniture,
        priceLess: filter.priceLess,
        priceHigher: filter.priceHigher,
        spaceAreaLess: filter.spaceAreaLess,
        spaceAreaHigher: filter.spaceAreaHigher,
        roomsNumberLess: filter.roomsNumberLess,
        roomsNumberHigher: filter.roomsNumberHigher,
        hasPictures: filter.hasPictures,
        subscribed: filter.subscribed,
        title: filter.title
      });
      //load notiffications
      // this.infoPagination = 1;
      this.selectedFilter = filter;
      this.currentMode = this.mode.notifications;
      this.fetchUsersNotifications();
    });
    EventBus.$on("filterDeleted", () => {
      this.notifications = null,
          this.infoPagination = 1,
          this.pageCount = 1,
          this.filter = null,
          this.selectedFilter = null,
          this.unseenNotificationsByFilter = null,
          this.mode = {
            ads: 'ADS',
            notifications: 'Obaveštenja'
          },
          this.currentMode = 'ADS',
          this.heading = 'Moji oglasi'
    });
  },
  methods: {
    fetchUsersAds() {
      this.infoPagination = 1;
      this.currentMode = this.mode.ads;
      this.fetchUsersNotifications()
    },
    markAllAsRead() {
      axios
          .put(`${this.apiUrl}/api/v1/notification/seen/filter/${this.selectedFilter.id}`)
          .then((response) => {
            if (response.status === 200) {
              EventBus.$emit('seenAllNotifications', this.selectedFilter.id);
              this.notifications = this.notifications.map(notification => ({...notification, seen: true}));
            }
          })
          .catch((error) => {
            console.error('Error marking notification as read:', error);
          });
    },
    markAsRead(id) {
      axios
          .put(`${this.apiUrl}/api/v1/notification/seen/${id}`)
          .then((response) => {
            if (response.status === 200) {
              EventBus.$emit('seenNotification', this.selectedFilter.id);
              const notificationToUpdate = this.notifications.find(notification => notification.id === id);
              if (notificationToUpdate) {
                notificationToUpdate.seen = true;
              }
            }
          })
          .catch((error) => {
            console.error('Error marking notification as read:', error);
          });
    },
    fetchUsersNotifications() {
      const filters = {};
      let pathVariable;

      if (this.currentMode === this.mode.ads) {
        filters.userId = this.userId;
        pathVariable = 'real-estate';
      } else {
        filters.id = this.selectedFilter.id;
        pathVariable = 'notification'
      }
      axios
          .post(`${this.apiUrl}/api/v1/${pathVariable}/page`, {
            filters,
            page: this.infoPagination ? this.infoPagination : 1,
            size: 1
          })
          .then((res) => {
            if (this.currentMode === this.mode.ads) {
              this.notifications = res.data.content.map(ad => ({realEstate: ad}));
              this.heading = 'Moji oglasi';
            } else if (this.currentMode === this.mode.notifications) {
              this.notifications = res.data.content;
              this.heading = 'Obaveštenja';
            }
            this.currentPage = res.data.pageable.pageNumber + 1;
            this.pageCount = res.data.totalPages;
            this.scrollToTop();
          })
          .catch((error) => {
            this.notifyVue("top", "center", error.response.data.message);
          });

    },
    openAd(notificationId, adId, index) {
      if (notificationId) {
        this.markAsRead(notificationId);
        EventBus.$emit('notificationsCountDecrees');
      }
      this.$refs.adDetails[index].openDetails(adId)
    },
    set(value) {
      if (Array.isArray(value)) return value;
      if (value) return [value];
      return null;
    },
    generateUrl(id) {
      return "/ad/" + id.toString();
    },
    bool(val) {
      if (val == true || val == "true") return true;
      return false;
    },
    setCoordinates(value) {
      if (value != null) {
        let rearanged = [];
        if (value[0].constructor === String) {
          for (let i = 0; i < value.length; i++) {
            rearanged.push(value[i].split(",").map(Number));
          }
          return rearanged;
        } else {
          return value;
        }
      } else {
        return null;
      }
    },
    setArray(value) {
      if (Array.isArray(value)) return value;
      if (value != null) return [value];
      return null;
    },
    setNum(value) {
      if (!isNaN(parseFloat(value))) return parseFloat(value);
      return null;
    },
    openDetails(id) {
      this.$refs.adDetails[id - 1].openDetails(id);
    },
    getFormattedDate(date) {
      let year = date[0];
      let month = date[1];
      let day = date[2];

      return day + "/" + month + "/" + year;
    },
    scrollToTop() {
      window.scrollTo(0, 0);
    },
    logout() {
      document.cookie = 'JSESSIONID=; Max-Age=0; path=/; domain=' + location.host;
      this.removeId();
      this.$router.push({name: "Login Register"});
    },
  },
};
</script>
