<template>
  <div>
    <md-dialog :md-active.sync="showDialog">
      <md-dialog-title>Sačuvaj pretragu</md-dialog-title>

      <div style="padding: 30px">
        <md-field>
          <label>Naziv pretrage:</label>
          <md-input v-model="filterName"></md-input>
        </md-field>
        <md-switch class="md-primary" v-model="subscribed"
          >E-mail obaveštenja
        </md-switch>
      </div>

      <md-dialog-actions>
        <md-button class="md-info" @click="showHideDialog()">Close</md-button>
        <md-button class="md-info" @click="saveFiltering">Save</md-button>
      </md-dialog-actions>
    </md-dialog>

    <md-button class="md-info md-raised" @click="showHideDialog()"
      >Sačuvaj pretragu</md-button
    >
  </div>
</template>

<script>
import axios from "axios";
import { mapActions, mapGetters } from "vuex";
import EventBus from "../../event-bus";

export default {
  computed: {
    ...mapGetters(["userId", "apiUrl"]),
  },
  methods: {
    showHideDialog() {
      this.showDialog = !this.showDialog;
      this.$sidebar.showSidebar = !this.showDialog;
    },
    set(value) {
      if (Array.isArray(value)) return value;
      if (value) return [value];
      return null;
    },
    saveFiltering() {
      this.filters.subscribed = this.subscribed;
      this.filters.tittle = this.filterName;
      axios
        .post(
          `${this.apiUrl}/api/v1/filter`,
          {
              userId: parseInt(this.userId),
              type: this.filters.selectedRealEstateTypes,
              adType: this.filters.selectedAdTypes,
              location: {id: this.filters.selectedLocation},
              microLocation: this.filters.selectedMicroLocations ? this.set(this.filters.selectedMicroLocations).map((element) => ({ id: element })) : null,
              heatingType: this.filters.selectedHatingTypes,
              floor: this.filters.selectedFloors,
              furniture: this.filters.selectedFurniture,
              priceLess: this.filters.selectedMaxPrice,
              priceHigher: this.filters.selectedMinPrice,
              spaceAreaLess: this.filters.selectedMaxArea,
              spaceAreaHigher: this.filters.selectedMinArea,
              roomsNumberLess: this.filters.selectedMaxRooms,
              roomsNumberHigher: this.filters.selectedMinRooms,
              hasPictures: this.filters.hasPictures,
              subscribed: this.filters.subscribed,
              title: this.filters.tittle
          })
        .then((res) => {
          console.log("SAVE RESPPONSE: " + res.data)
          this.filterName = "Nenaslovljeno";
          this.subscribed = false;
          this.showDialog = false;
          this.$notify({
            message: "Filter saved!",
            icon: "done",
            horizontalAlign: "center",
            verticalAlign: "top",
            type: "success",
          });
          EventBus.$emit("filterSaved", res.data);
        })
        .catch((error) => {
          this.filterName = "Nenaslovljeno";
          this.subscribed = false;
          this.showDialog = false;

          if (error.response in window) {
            this.$notify({
              message: error.message,
              icon: "add_alert",
              horizontalAlign: "center",
              verticalAlign: "top",
              type: "danger",
            });
          } else {
            if (error.response.data.message == "Not authorized!") {
              this.removeId();
              this.$router.push({ name: "Login Register" });
            }
            this.$notify({
              message: error.response.data.message,
              icon: "add_alert",
              horizontalAlign: "center",
              verticalAlign: "top",
              type: "danger",
            });
          }
        });
    },
    ...mapActions(["removeId"]),
  },
  props: {
    filters: Object,
  },
  data: () => ({
    filterName: "Nenaslovljeno",
    showDialog: false,
    subscribed: false,
  }),
};
</script>
