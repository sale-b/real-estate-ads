<template>
  <form>
    <md-card style="overflow: hidden">
      <h3 style="text-align: center">Filteri</h3>

      <dialog-map
          :coordinates="filter.coordinates"
          v-on:coordinates="filter.coordinates = $event"
          v-if="1!=1"
      ></dialog-map>

      <md-card-content>
        <div class="md-layout">
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label for="ad-types">Tip oglasa</label>
              <md-select
                  v-model="filter.selectedAdTypes"
                  name="ad-types"
                  id="ad-types"
                  multiple
              >
                <md-option
                    v-for="adType in adTypes"
                    :key="adType"
                    :value="adType"
                >{{ adType }}
                </md-option
                >
              </md-select>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label for="real-estate-types">Tip nekretnine</label>
              <md-select
                  v-model="filter.selectedRealEstateTypes"
                  name="real-estate-types"
                  id="real-estate-types"
                  multiple
              >
                <md-option
                    v-for="realEstateType in realEstateTypes"
                    :key="realEstateType"
                    :value="realEstateType"
                >{{ realEstateType }}
                </md-option
                >
              </md-select>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label for="locations">Mesto</label>
              <md-select
                  v-model="filter.selectedLocation"
                  name="locations"
                  id="locations.id"
              >
                <md-option
                    v-for="location in locations"
                    :key="location.id"
                    :value="location.id"
                >{{ location.name }}
                </md-option
                >
              </md-select>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label for="micro-locations">Mikrolokacije</label>
              <md-select
                  v-model="filter.selectedMicroLocations"
                  name="micro-locations"
                  id="micro-locations.id"
                  multiple
              >
                <md-option
                    v-for="location in microLocations"
                    :key="location.id"
                    :value="location.id"
                >{{ location.name }}
                </md-option
                >
              </md-select>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-50 md-size-50">
            <md-field>
              <label>Min cena &euro;</label>
              <md-input
                  v-model="filter.selectedMinPrice"
                  type="text"
                  style="height: 60px; padding: 30px 0 0 0"
              ></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-50 md-size-50">
            <md-field>
              <label>Max cena&euro;</label>
              <md-input
                  v-model="filter.selectedMaxPrice"
                  type="text"
                  style="height: 60px; padding: 30px 0 0 0"
              ></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-50 md-size-50">
            <md-field>
              <label>Min &zwnj; kvadratura m<sup>2</sup></label>
              <md-input
                  v-model="filter.selectedMinArea"
                  type="text"
                  style="height: 80px; padding: 30px 0 0 0"
              ></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-50 md-size-50">
            <md-field>
              <label>Max kvadratura m<sup>2</sup></label>
              <md-input
                  v-model="filter.selectedMaxArea"
                  type="text"
                  style="height: 80px; padding: 30px 0 0 0"
              ></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-50 md-size-50">
            <md-field>
              <label>Min soba</label>
              <md-input
                  v-model="filter.selectedMinRooms"
                  type="text"
                  style="height: 60px; padding: 30px 0 0 0"
              ></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-50 md-size-50">
            <md-field>
              <label>Max soba</label>
              <md-input
                  v-model="filter.selectedMaxRooms"
                  type="text"
                  style="height: 60px; padding: 30px 0 0 0"
              ></md-input>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label for="heating-types">Grejanje</label>
              <md-select
                  v-model="filter.selectedHatingTypes"
                  name="heating-types"
                  id="heating-types"
                  multiple
              >
                <md-option
                    v-for="heatingType in heatingTypes"
                    :key="heatingType"
                    :value="heatingType"
                >{{ heatingType }}
                </md-option
                >
              </md-select>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label for="floors">Sprat</label>
              <md-select
                  v-model="filter.selectedFloors"
                  name="floors"
                  id="floors"
                  multiple
              >
                <md-option
                    v-for="floor in floors"
                    :key="floor"
                    :value="floor"
                >{{ floor }}
                </md-option
                >
              </md-select>
            </md-field>
          </div>
          <div class="md-layout-item md-small-size-100 md-size-100">
            <md-field>
              <label for="furniture">Nameštenost</label>
              <md-select
                  v-model="filter.selectedFurniture"
                  name="furniture"
                  id="furniture"
                  multiple
              >
                <md-option
                    v-for="furniture in furnitures"
                    :key="furniture"
                    :value="furniture"
                >{{ furniture }}
                </md-option
                >
              </md-select>
            </md-field>
          </div>
          <md-switch class="md-primary" v-model="filter.hasPictures"
          >Samo sa slikama
          </md-switch
          >

          <div class="md-layout-item md-size-33 text-left">
            <span>
              <md-button
                  class="md-icon-button md-raised md-danger"
                  @click="clearFilters"
                  style="height: 41px; font-size: 14px"
              >
                X
              </md-button>
              <md-tooltip md-delay="1000">Resetuj filtere</md-tooltip>
            </span>
          </div>

          <div class="md-layout-item md-size-33 text-left">
            <md-button class="md-raised md-success" @click="filtering"
            >Pretraži
            </md-button
            >
          </div>

          <div v-show="userId" class="md-layout-item md-size-100 text-right">
            <dialog-save-filter :filters="filter"></dialog-save-filter>
          </div>
        </div>
      </md-card-content>
    </md-card>
  </form>
</template>
<script>
import axios from "axios";
import {mapGetters} from "vuex";
import DialogMap from "../../components/Dialogs/DialogMap.vue";
import DialogSaveFilter from "../../components/Dialogs/DialogSaveFilter.vue";
import EventBus from "./../../event-bus";

export default {
  components: {
    DialogMap,
    DialogSaveFilter,
  },
  name: "filters-form",
  props: {
    dataBackgroundColor: {
      type: String,
      default: "",
    },
  },
  computed: {
    selectedLocation() {
      return this.filter.selectedLocation;
    },
    ...mapGetters(["userId", "apiUrl"]),
  },
  watch: {
    selectedLocation: function (val, oldVal) {
        this.microLocations = null;
        this.filter.selectedMicroLocations = null;
        if(val && val.length !== 0) {
          axios
              .post(`${this.apiUrl}/api/v1/city-subregion`, [{id: val}])
              .then((res) => {
                this.microLocations = res.data.map(x => ({
                  'id': x.id,
                  'name': x.name,
                  'toLowerCase': () => x.name.toLowerCase(),
                  'toString': () => x.name
                }))
                this.filter.selectedMicroLocations = this.setArray(this.receivedFilters.microLocations);
              })
              .catch((error) => {
              });
        }
    },
  },
  mounted() {
    this.populateAutocompleteFields();
    EventBus.$on("loadFilters", (filters) => {
      this.receivedFilters = filters;
      this.filter.selectedAdTypes= this.setArray(filters.adType);
      this.filter.selectedRealEstateTypes= this.setArray(filters.type);
      this.filter.selectedLocation = filters.location;
      this.filter.selectedHatingTypes = this.setArray(filters.heatingType);
      this.filter.selectedFloors = this.setArray(filters.floor);
      this.filter.selectedFurniture = this.setArray(filters.furniture);
      this.filter.hasPictures = filters.hasPictures === 'true' || filters.hasPictures;
      this.filter.selectedMinPrice = filters.priceHigher;
      this.filter.selectedMaxPrice = filters.priceLess;
      this.filter.selectedMinArea = filters.spaceAreaHigher;
      this.filter.selectedMaxArea = filters.spaceAreaLess;
      this.filter.selectedMinRooms = filters.roomsNumberHigher;
      this.filter.selectedMaxRooms = filters.roomsNumberLess;
    });
  },
  data() {
    return {
      locations: [],
      microLocations: [],
      adTypes: [],
      realEstateTypes: [],
      heatingTypes: [],
      floors: [],
      furnitures: [],
      filter: {
        selectedLocation: null,
        hasPictures: false,
      },
      receivedFilters:null
    };
  },
  methods: {
    setArray(value) {
      if (Array.isArray(value)) return value;
      if (value != null) return [value];
      return null;
    },
    populateAutocompleteFields() {
        axios
            .get(`${this.apiUrl}/api/v1/real-estate/autocomplete-fields`)
            .then((res) => {
              this.locations = res.data.cities.map(x => ({
                'id': x.id,
                'name': x.name,
                'toLowerCase': () => x.name.toLowerCase(),
                'toString': () => x.name
              }))
              this.adTypes = res.data.adTypes;
              this.realEstateTypes = res.data.realEstateTypes;
              this.heatingTypes = res.data.heatingTypes;
              this.floors = res.data.floors;
              this.furnitures = res.data.furnitureTypes;
            })
            .catch((error) => {
              this.notifyVue("top", "center", error.response.data.message);
            });
    },
    filtering() {
      this.$router
          .push({
            name: "Ads",
            params: {id: 1},
            query: Object.fromEntries(
                Object.entries({
                  type: this.filter.selectedRealEstateTypes,
                  adType: this.filter.selectedAdTypes,
                  location: this.filter.selectedLocation,
                  microLocations: this.filter.selectedMicroLocations,
                  heatingType: this.filter.selectedHatingTypes,
                  floor: this.filter.selectedFloors,
                  furniture: this.filter.selectedFurniture,
                  priceLess: this.filter.selectedMaxPrice,
                  priceHigher: this.filter.selectedMinPrice,
                  spaceAreaLess: this.filter.selectedMaxArea,
                  spaceAreaHigher: this.filter.selectedMinArea,
                  roomsNumberLess: this.filter.selectedMaxRooms,
                  roomsNumberHigher: this.filter.selectedMinRooms,
                  hasPictures: this.filter.hasPictures,
                  // coordinates: this.setCoordinates(this.filter.coordinates),
                }).filter(([_, v]) => v != null)
            ),
          })
          .catch((err) => {
          });
      EventBus.$emit("applyFilters");
      this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    },
    clearFilters() {
      this.filter = {
        selectedLocation: null,
        selectedMicroLocations: null,
        selectedMaxPrice: null,
        selectedMinPrice: null,
        selectedMaxArea: null,
        selectedMinArea: null,
        selectedMaxRooms: null,
        selectedMinRooms: null,
        selectedAdTypes: null,
        selectedRealEstateTypes: null,
        selectedHatingTypes: null,
        selectedFloors: null,
        selectedFurniture: null,
        coordinates: null,
        hasPictures: false,
        subscribed: false,
      };
    },
  },
};
</script>


<style>
.md-ripple > span {
  padding: 15px 0 0 50px;
}

.md-menu-content.md-select-menu {
  z-index: 2000 !important;
}
</style>