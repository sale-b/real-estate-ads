<template>
  <div class="content">
    <div v-if="ad != null" class="md-layout">
      <div
        class="md-layout-item md-xsmall-size-100 md-small-size-100 md-medium-size-50 md-size-50"
      >
        <div class="gallery">
          <div
            v-for="(image, index) in images"
            :key="index"
            class="mySlides"
            v-bind:style="isBlock(index + 1)"
          >
            <div class="numbertext">{{ index + 1 }} / {{ images.length }}</div>
            <img :src="image" style="width: 100%" />
          </div>

          <a class="prev" @click="plusSlides(-1)">❮</a>
          <a class="next" @click="plusSlides(1)">❯</a>

          <div class="row">
            <div v-for="(image, index) in images" :key="index" class="column">
              <img
                :class="
                  'demo' +
                  (isActive(index + 1) == true ? ' active' : '') +
                  ' cursor'
                "
                :src="image"
                style="width: 100%"
                @click="currentSlide(index + 1)"
                alt="Apartment img"
              />
            </div>
          </div>
        </div>
      </div>
      <div
        class="md-layout-item md-xsmall-size-100 md-small-size-100 md-medium-size-50 md-size-50"
      >
        <md-card>
          <md-card-header data-background-color="green">
            <div class="md-title">{{ ad.tittle }}</div>
          </md-card-header>

          <md-card-content>
            <div>
              <md-list>
                <md-subheader>Detalji oglasa</md-subheader>
                <div v-if="ad.createdOn">
                  <md-list-item>Datum objave: {{ getFormattedDate(ad.createdOn) }}</md-list-item>
                  <md-divider></md-divider>
                </div>
                <div v-if="ad.location">
                  <md-list-item
                  ><div style="white-space: normal">
                    Lokacija: {{ ad.location }}
                  </div></md-list-item
                  >
                  <md-divider></md-divider>
                </div>

                <div v-if="ad.adType">
                  <md-list-item>Tip oglasa: {{ ad.adType }}</md-list-item>
                  <md-divider></md-divider>
                </div>
                <div v-if="ad.realEstateType">
                  <md-list-item>Tip nekretnine: {{ ad.realEstateType }}</md-list-item>
                  <md-divider></md-divider>
                </div>
                <div v-if="ad.livingSpaceArea">
                  <md-list-item
                  ><div>
                    Kvadratura: {{ ad.livingSpaceArea }} m<sup
                  >2</sup
                  >
                  </div></md-list-item
                  >
                  <md-divider></md-divider>
                </div>
                <div v-if="ad.roomsNumber">
                  <md-list-item
                    >Broj soba: {{ ad.roomsNumber }}</md-list-item
                  >
                  <md-divider></md-divider>
                </div>
                <div v-if="ad.floor">
                  <md-list-item>Sprat: {{ ad.floor }}</md-list-item>
                  <md-divider></md-divider>
                </div>
                <div v-if="ad.furnitureType">
                  <md-list-item>Nameštenost: {{ ad.furnitureType }}</md-list-item>
                  <md-divider></md-divider>
                </div>
                <div v-if="ad.heatingType">
                  <md-list-item>Grejanje: {{ ad.heatingType }}</md-list-item>
                  <md-divider></md-divider>
                </div>
                <md-divider></md-divider>

                <md-subheader>Telefon</md-subheader>

                <md-list-item>
                  <md-icon style="margin-right: 12px" class="md-primary"
                    >phone</md-icon
                  >

                  <div class="md-list-item-text">
                    <a
                      style="font-size: 18px"
                      :href="'tel:' + ad.phone.replace(/\D/g, '')"
                      >{{ ad.phone }}</a
                    >
                  </div>
                </md-list-item>
                <md-divider></md-divider>
                <md-subheader>Cena</md-subheader>
                <md-list-item>
                  <div class="md-list-item-text">{{ ad.price }} &euro;</div>
                </md-list-item>
                <md-divider></md-divider>
              </md-list>
            </div>
            <br />
            {{ ad.description }}
            <br />
            <md-button style="float:right;" v-if="ad.userId === parseInt(userId)" class="md-round md-success" @click="goToEditPage">Izmeni oglas</md-button>
            <dialog-delete-ad :ad="ad"></dialog-delete-ad>
          </md-card-content>
        </md-card>

        <div style="width: 100%; height: 300px; border: 3px solid #73ad21">
          <map-container-pin
            :geolocation="getCoordinates()"
            :zoom="this.ad.geoLocationZoom"
          ></map-container-pin>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import MapContainerPin from "../../src/pages/Filters/MapContainerPin";
import {mapGetters} from "vuex";
import DialogDeleteAd from "../components/Dialogs/DialogDeleteAd";

export default {
  components: {
    DialogDeleteAd,
    MapContainerPin,
  },
  data() {
    return {
      slideIndex: 1,
      ad: null,
      infoPagination: parseInt(this.$route.params.id),
    };
  },
  computed: {
    ...mapGetters(["apiUrl", "userId"]),
    images() {
      if (this.ad.images.length > 0) {
        return this.ad.images;
      } else {
        return [require("@/assets/img/no-image-found.png")];
      }
    },
  },
  mounted() {
    this.fetchData(this.infoPagination);
  },
  methods: {
    deleteAd(){

    },
    goToEditPage() {
      const adId = this.$route.params.id;
      this.$router.push({ name: 'Edit Ad', params: { id: adId } });
    },
    getFormattedDate(date) {
      let year = date[0];
      let month = date[1];
      let day = date[2];

      return day + "/" + month + "/" + year;
    },
    getCoordinates() {
      return [this.ad.geoLocationCoordinates.x, this.ad.geoLocationCoordinates.y];
    },
    isBlock(index) {
      if (index == this.slideIndex) return "display: block;";
    },
    isActive(index) {
      return index == this.slideIndex;
    },
    plusSlides(n) {
      this.showSlides((this.slideIndex += n));
    },
    currentSlide(n) {
      this.showSlides((this.slideIndex = n));
    },
    showSlides(n) {
      var i;
      var slides = document.getElementsByClassName("mySlides");
      var dots = document.getElementsByClassName("demo");
      if (n > slides.length) {
        this.slideIndex = 1;
      }
      if (n < 1) {
        this.slideIndex = slides.length;
      }
      for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
      }
      for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
      }
      slides[this.slideIndex - 1].style.display = "block";
      dots[this.slideIndex - 1].className += " active";
    },
    fetchData(id) {
      axios
        .get(`${this.apiUrl}/api/v1/real-estate/${id}`)
        .then((res) => {
          this.ad = res.data;
        })
        .catch((error) => {
          this.notifyVue("top", "center", error.response.data.message);
        });
    },
  },
};
</script>

<style scoped>
.md-card-header {
  padding: 0;
}
body {
  font-family: Arial;
  margin: 0;
}

* {
  box-sizing: border-box;
}

img {
  vertical-align: middle;
}

/* Position the image container (needed to position the left and right arrows) */
.gallery {
  position: relative;
}

/* Hide the images by default */
.mySlides {
  display: none;
}

/* Add a pointer when hovering over the thumbnail images */
.cursor {
  cursor: pointer;
}

/* Next & previous buttons */
.prev,
.next {
  cursor: pointer;
  position: absolute;
  top: 40%;
  width: auto;
  padding: 16px;
  margin-top: -50px;
  color: white;
  font-weight: bold;
  font-size: 20px;
  border-radius: 0 3px 3px 0;
  user-select: none;
  -webkit-user-select: none;
}

/* Position the "next button" to the right */
.next {
  right: 0;
  border-radius: 3px 0 0 3px;
}

/* On hover, add a black background color with a little bit see-through */
.prev:hover,
.next:hover {
  background-color: rgba(0, 0, 0, 0.8);
}

/* Number text (1/3 etc) */
.numbertext {
  color: #f2f2f2;
  font-size: 12px;
  padding: 8px 12px;
  position: absolute;
  top: 0;
}

.row:after {
  content: "";
  display: table;
  clear: both;
}

/* Six columns side by side */
.column {
  float: left;
  width: 16.66%;
}

/* Add a transparency effect for thumnbail images */
.demo {
  opacity: 0.6;
}

.active,
.demo:hover {
  opacity: 1;
}
</style>