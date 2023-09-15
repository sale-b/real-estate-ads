<template>
  <div class="content">
    <div class="md-layout">
      <div
          class="md-layout-item md-medium-size-100 md-xsmall-size-100 md-size-100"
      >
        <a
            v-for="(ad, index) in ads"
            :key="index"
            :href="generateUrl(ad.id)"
            v-on:click.prevent="$refs.adDetails[index].openDetails(ad.id)"
        >
          <ad-card
              :tittle="ad.tittle"
              :content="ad.description.length <= 400 ? ad.description : ad.description.substring(0, 400) + '...'"
              :img-url="ad.imageUrl ? ad.imageUrl : null"
              :date="getFormattedDate(ad.createdOn)"
              :location="ad.location"
              :space="ad.livingSpaceArea"
              :rooms="ad.roomsNumber"
              :furniture="ad.furnitureType"
              :price="ad.price"
              :floor="ad.floor"
              :id="ad.id"
              ref="adDetails"
          ></ad-card>
        </a>
      </div>
    </div>
    <div class="pagination-holder">
      <pagination
          type="info"
          v-if="pageCount > 1"
          v-on:input="change"
          v-model="currentPage"
          :page-count="pageCount"
      >
      </pagination>
    </div>
  </div>
</template>

<script>
import Pagination from "./../components/Pagination";
import AdCard from "../components/Cards/AdCard.vue";
import axios from "axios";
import {mapGetters, mapActions} from "vuex";
import EventBus from "./../event-bus";

export default {
  components: {
    Pagination,
    AdCard,
  },
  computed: {
    ...mapGetters(["apiUrl"]),
  },
  data() {
    return {
      ads: null,
      currentPage: parseInt(this.$route.params.id),
      pageCount: 1,
    };
  },
  mounted() {
    EventBus.$on("applyFilters", () => {
      this.currentPage = 1;
      this.fetchData(this.currentPage);
    });
    this.fetchData(this.currentPage);
    EventBus.$emit("loadFilters", this.$route.query)
  },
  methods: {
    ...mapActions(["removeId"]),
    bool(val) {
      if (val == true || val == "true") return true;
      return false;
    },
    set(value) {
      if (Array.isArray(value)) return value;
      if (value) return [value];
      return null;
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
    openDetails(index, id) {
      this.$refs.adDetails[index].openDetails(id);
    },
    generateUrl(id) {
      return "/ad/" + id.toString();
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
    notifyVue(verticalAlign, horizontalAlign, message) {
      this.$notify({
        message: message,
        icon: "add_alert",
        horizontalAlign: horizontalAlign,
        verticalAlign: verticalAlign,
        type: "danger",
      });
    },
    change() {
      this.$router
          .push({
            name: "Ads",
            params: {id: this.currentPage.toString()},
            query: this.$route.query,
          })
          .catch((err) => {
          });
      this.fetchData(this.currentPage);
    },
    fetchData(page) {
      axios
          .post(`${this.apiUrl}/api/v1/real-estate/page`, {
            filters: {
              adType: this.set(this.$route.query.adType),
              type: this.set(this.$route.query.type),
              location: {id: this.$route.query.location},
              microLocation: this.$route.query.microLocations ? this.set(this.$route.query.microLocations).map((element) => ({ id: element })) : null,
              hasPictures: this.$route.query.hasPictures,
              heatingType : this.set(this.$route.query.heatingType),
              floor : this.set(this.$route.query.floor),
              furniture : this.set(this.$route.query.furniture),
              priceLess: this.$route.query.priceLess,
              priceHigher: this.$route.query.priceHigher,
              spaceAreaLess: this.$route.query.spaceAreaLess,
              spaceAreaHigher: this.$route.query.spaceAreaHigher,
              roomsNumberLess: this.$route.query.roomsNumberLess,
              roomsNumberHigher: this.$route.query.roomsNumberHigher
            },
            page: page,
            size: 10
          }, {
            withCredentials: true
          })
          .then((res) => {
            if(typeof res.data.content === 'undefined'){
              this.removeId();
              window.location.href = "http://client-server:8081/auth/login";
            }
            this.ads = res.data.content;
            this.currentPage = res.data.pageable.pageNumber + 1;
            this.pageCount = res.data.totalPages;
            this.scrollToTop();
          })
          .catch((error) => {
            this.notifyVue("top", "center", error.message);
          });
    },
  },
};
</script>
