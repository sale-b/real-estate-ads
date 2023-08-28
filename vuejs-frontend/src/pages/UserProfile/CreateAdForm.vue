<template>
  <form>
    <md-card>
      <md-card-header data-background-color="green">
        <h4 class="title">{{ mode === 'create' ? 'Novi oglas' : 'Izmena oglasa' }}</h4>
        <p class="category">{{ mode === 'create' ? 'Predajte novi oglas' : 'Izmenite postojeći oglas' }}</p>
      </md-card-header>

      <md-card-content>
        <div class="md-layout">
          <div class="md-layout-item md-small-size-100 md-size-50">
            <md-field>
              <label>Naslov</label>
              <md-input v-model="title" required></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-20">
            <md-autocomplete v-model="selectedAdType" :md-options="autocompleteFields.adTypes">
              <label>Tip oglasa</label>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-30">
            <md-field>
              <label>Cena</label>
              <md-input class="right" v-model="price" type="number" required></md-input>
              <span class="md-suffix">EUR</span>
            </md-field>
          </div>

          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedCity" :md-options="cities" @md-changed="getCityRegions">
              <label>Mesto</label>
              <template slot="md-autocomplete-item" slot-scope="{ item, term }">
                <md-highlight-text :md-term="term">{{ item }}</md-highlight-text>
              </template>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedCityRegion" :md-options="cityRegions" @md-changed="getCitySubregions">
              <label>Deo mesta</label>
              <template slot="md-autocomplete-item" slot-scope="{ item, term }">
                <md-highlight-text :md-term="term">{{ item }}</md-highlight-text>
              </template>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedCitySubregion" :md-options="citySubregions">
              <label>Lokacija</label>
              <template slot="md-autocomplete-item" slot-scope="{ item, term }">
                <md-highlight-text :md-term="term">{{ item }}</md-highlight-text>
              </template>
            </md-autocomplete>
          </div>
          <div v-if="selectedCitySubregion&&selectedCitySubregion.geoLocation"
               class="md-layout-item md-small-size-100 md-size-20">
            <p class="md-caption" style="font-size: 14px; font-weight: 400;">Obeležite lokaciju Vaše nekretnine na mapi.
              Da biste uneli poziciju na mapi, kliknite mišem. *</p>
          </div>
          <div v-if="selectedCitySubregion&&selectedCitySubregion.geoLocation"
               class="md-layout-item md-small-size-100 md-size-80">
            <map-container-pin-select :zoom="zoom" :centerCoordinates="centerCoordinates"
                                      :pointCoordinates="pointCoordinates"
                                      @pointSelected="handlePointSelected"></map-container-pin-select>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedRealEstateType" :md-options="autocompleteFields.realEstateTypes">
              <label>Tip nekretnine</label>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-field>
              <label>Kvadratura</label>
              <md-input class="right" v-model="livingSpaceArea" type="number" required></md-input>
              <span class="md-suffix">m<sup>2</sup></span>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedRoomsNumber" :md-options="autocompleteFields.roomsNumbers">
              <label>Broj soba</label>
            </md-autocomplete>
          </div>

          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedFloor" :md-options="autocompleteFields.floors">
              <label>Sprat</label>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedFurnishType" :md-options="autocompleteFields.furnitureTypes">
              <label>Nameštenost</label>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-33">
            <md-autocomplete v-model="selectedHeatingType" :md-options="autocompleteFields.heatingTypes">
              <label>Grejanje</label>
            </md-autocomplete>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label>Detaljan opis</label>
              <md-textarea v-model="description" required></md-textarea>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-30">
            <md-field>
              <label>Telefon</label>
              <md-input class="right" v-model="phone" type="number" required></md-input>
            </md-field>
          </div>

          <div class="md-layout-item md-small-size-100 md-size-70">
            <md-field>
              <label>Fotografije</label>
              <md-file v-model="images" multiple accept="image/*" @change="handleImageChange"/>
              <button type="button" class="md-button md-icon-button md-dense md-input-action md-clear md-theme-default"
                      tabindex="-1" v-if="images" @click="images=null;files=null">
                <div class="md-ripple">
                  <div class="md-button-content"><i class="md-icon md-icon-font md-icon-image md-theme-default">
                    <svg height="24" viewBox="0 0 24 24" width="24" xmlns="http://www.w3.org/2000/svg">
                      <path
                          d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"></path>
                      <path d="M0 0h24v24H0z" fill="none"></path>
                    </svg>
                  </i></div>
                </div>
              </button>
            </md-field>
          </div>
          <div class="md-layout-item md-size-100 text-right">
            <md-button class="md-raised md-success" @click="submitAd"
            >{{ mode === 'create' ? 'Dodaj oglas' : 'Sačuvaj izmene' }}
            </md-button
            >
          </div>
        </div>
      </md-card-content>
    </md-card>
  </form>
</template>
<script>
import axios from "axios";
import {mapActions, mapGetters} from "vuex";
import MapContainerPinSelect from "../Filters/MapContainerPinSelect";

export default {
  name: "create-ad-form",
  components: {MapContainerPinSelect},
  props: {
    mode: String,
    id: String,
    dataBackgroundColor: {
      type: String,
      default: "",
    },
  },
  computed: {
    ...mapGetters(["apiUrl", "userId"]),
  },
  watch: {
    mode(newMode) {
      if (newMode === 'create') {
        this.title = null;
        this.price = null;
        this.phone = null;
        this.images = null;
        this.files = null;
        this.description = null;
        this.livingSpaceArea = null;
        this.autocompleteFields = {
          adTypes: [],
          realEstateTypes: [],
          roomsNumbers: [],
          floors: [],
          furnitureTypes: [],
          heatingTypes: []
        };
        this.selectedRoomsNumber = null;
        this.selectedRealEstateType = null;
        this.selectedFloor = null;
        this.selectedHeatingType = null;
        this.selectedFurnishType = null;
        this.selectedAdType = null;
        this.selectedCity = null;
        this.cities = [];
        this.selectedCityRegion = null;
        this.cityRegions = [];
        this.selectedCitySubregion = null;
        this.citySubregions = [];
        this.selectedRealEstateGeoLocation = null;
        this.pointCoordinates = null;
        this.centerCoordinates = null;
        this.zoom = null;
        this.realEstateData = null;
        this.getAutocompleteFields();
      } else {
        if (newMode === 'edit') {
          this.getRealEstateDataById();
        }
      }
    },
    selectedCity(newCity) {
      if (!newCity) {
        this.selectedCityRegion = "";
        this.cityRegions = [];
        this.selectedCitySubregion = "";
        this.citySubregions = [];
      }
    },
    selectedCityRegion(newCityRegion) {
      if (!newCityRegion) {
        this.selectedCitySubregion = "";
        this.citySubregions = [];
      }
    },
    selectedCitySubregion(newSubregion, oldSubregion) {
      if (!newSubregion) {
        if (oldSubregion) {
          this.pointCoordinates = null;
          this.selectedRealEstateGeoLocation = null;
        }
      } else {
        this.centerCoordinates = this.pointCoordinates ? this.centerCoordinates : newSubregion.geoLocation;
        this.zoom = newSubregion.zoom;
      }
    }
  }
  ,
  mounted() {
    if (this.mode === 'create') {
      this.getAutocompleteFields();
    } else {
      if (this.mode === 'edit') {
        this.getRealEstateDataById();
      }
    }

  }
  ,
  data() {
    return {
      title: null,
      price: null,
      phone: null,
      images: null,
      files: null,
      description: null,
      livingSpaceArea: null,
      autocompleteFields: {
        adTypes: [],
        realEstateTypes: [],
        roomsNumbers: [],
        floors: [],
        furnitureTypes: [],
        heatingTypes: []
      },
      selectedRoomsNumber: null,
      selectedRealEstateType: null,
      selectedFloor: null,
      selectedHeatingType: null,
      selectedFurnishType: null,
      selectedAdType: null,
      selectedCity: null,
      cities: [],
      selectedCityRegion: null,
      cityRegions: [],
      selectedCitySubregion: null,
      citySubregions: [],
      selectedRealEstateGeoLocation: null,
      pointCoordinates: null,
      centerCoordinates: null,
      zoom: null,
      realEstateData: null,
    };
  }
  ,
  methods: {
    ...mapActions(["setId", "removeId"]),
    notifyVue(verticalAlign, horizontalAlign, message) {
      this.$notify({
        message: message,
        icon: "add_alert",
        horizontalAlign: horizontalAlign,
        verticalAlign: verticalAlign,
        type: "danger",
      });
    }
    ,
    handleImageChange(event) {
      this.files = Array.from(event.target.files);
    }
    ,
    submitAd() {
      let error = false;
      const formData = new FormData();

      let data = {
        id: this.id,
        title: this.title,
        phone: this.phone,
        price: this.price,
        hasPictures: this.images != null && this.images.length > 0,
        description: this.description,
        roomsNumber: this.selectedRoomsNumber,
        realEstateType: this.selectedRealEstateType,
        floor: this.selectedFloor,
        heatingType: this.selectedHeatingType,
        furnitureType: this.selectedFurnishType,
        adType: this.selectedAdType,
        livingSpaceArea: this.livingSpaceArea,
        location: this.selectedCitySubregion.id,
        geoLocation: this.selectedRealEstateGeoLocation ? this.selectedRealEstateGeoLocation : this.pointCoordinates
      };

      formData.append("model", JSON.stringify(data));
      if (data.hasPictures && this.files != null && this.files.length > 0) {
        for (let i = 0; i < this.files.length; i++) {
          formData.append("images", this.files[i]);
        }
      }

      if (!error) {
        axios
            .put(`${this.apiUrl}/api/v1/real-estate`, formData)
            .then((res) => {
                this.$router.push({name: 'Ad Details', params: {id: res.data.id}});
              // this.setAuthToken(res.headers["x-auth-token"]);
              // this.setId(res.data.id);
              //
            })
            .catch((error) => {
              // this.emailadress = null;
              // this.password = null;
              // this.confirmPassword = null;
              this.notifyVue("top", "center", "Not authorized, redirecting to login...");
              this.removeId();
              window.location.href = "http://client-server:8081/auth/login";
            });
      }
    }
    ,
    async getAutocompleteFields() {
      await axios.get(`${this.apiUrl}/api/v1/real-estate/autocomplete-fields`)
          .then((res) => {
            this.autocompleteFields = res.data
            this.cities = res.data.cities.map(x => ({
              'id': x.id,
              'name': x.name,
              'toLowerCase': () => x.name.toLowerCase(),
              'toString': () => x.name
            }))
          }).catch((error) => {
            // console.log(error)
          });
    }
    ,
    async getCityRegions() {
      if (this.selectedCity.id) {
        await axios.post(`${this.apiUrl}/api/v1/city-region/`, [this.selectedCity])
            .then((res) => {
              this.cityRegions = res.data.map(x => ({
                'id': x.id,
                'name': x.name,
                'toLowerCase': () => x.name.toLowerCase(),
                'toString': () => x.name
              }))
            }).catch((error) => {
              // console.log(error)
            });
      }
    }
    ,
    async getCitySubregions() {
      if (this.selectedCity.id) {
        await axios.get(`${this.apiUrl}/api/v1/city-subregion/${this.selectedCityRegion.id}`)
            .then((res) => {
              this.citySubregions = res.data.map(x => ({
                'id': x.id,
                'name': x.name,
                'geoLocation': x.geoLocation,
                'zoom': x.zoom,
                'toLowerCase': () => x.name.toLowerCase(),
                'toString': () => x.name
              }))
            }).catch((error) => {
              // console.log(error)
            });
      }
    }
    ,
    async getRealEstateDataById() {
      await axios.get(`${this.apiUrl}/api/v1/real-estate/${this.id}`)
          .then((res) => {
            if (this.mode === 'edit' && parseInt(this.userId) !== res.data.userId) {
              throw new Error('You do not have permission to edit this ad.');
            }
            this.realEstateData = res.data;
            this.zoom = this.realEstateData.geoLocationZoom;
            this.centerCoordinates = this.realEstateData.geoLocationCoordinates;
            this.pointCoordinates = this.realEstateData.geoLocationCoordinates;
            this.title = this.realEstateData.tittle;
            this.selectedAdType = this.realEstateData.adType;
            this.price = this.realEstateData.price;
            this.selectedRealEstateType = this.realEstateData.realEstateType;
            this.livingSpaceArea = this.realEstateData.livingSpaceArea;
            this.selectedRoomsNumber = this.realEstateData.roomsNumber;
            this.selectedFloor = this.realEstateData.floor;
            this.selectedFurnishType = this.realEstateData.furnitureType;
            this.selectedHeatingType = this.realEstateData.heatingType;
            this.description = this.realEstateData.description;
            this.phone = this.realEstateData.phone;
            this.images = this.realEstateData.images.map(imagePath => imagePath.split('_')[1]).join(', ');
            console.log("GEOLOCATION: " + JSON.stringify(this.pointCoordinates))
          })
          .catch(error => {
            this.$notify({
              message: error.message,
              icon: "add_alert",
              horizontalAlign: "center",
              verticalAlign: "top",
              type: "danger",
            });
            console.error('Error fetching real estate data:', error);
            this.$router.push('/');
          });
      await this.getAutocompleteFields();
      this.selectedCity = this.cities.find(item => item.name === this.realEstateData.city);
      await this.getCityRegions();
      this.selectedCityRegion = this.cityRegions.find(item => item.name === this.realEstateData.cityRegion);
      await this.getCitySubregions();
      this.selectedCitySubregion = this.citySubregions.find(item => item.name === this.realEstateData.citySubregion);
    }
    ,
    handlePointSelected(selectedCoordinates) {
      console.log("SELECTED LOCATION: " + JSON.stringify(selectedCoordinates))
      this.selectedRealEstateGeoLocation = {
        "x": selectedCoordinates[0],
        "y": selectedCoordinates[1]
      }
    }
  }
  ,
}
;
</script>
<style scoped>
.right {
  text-align: right;
  padding-right: 15px !important;
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type=number] {
  -moz-appearance: textfield;
}

.centered {
  margin: auto;
}
</style>
