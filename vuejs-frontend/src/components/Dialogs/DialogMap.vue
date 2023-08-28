<template>
  <div>
    <md-dialog :md-active.sync="showDialog">
      <md-dialog-title>Select area</md-dialog-title>

      <div class="cell">
        <map-container
          :coordinates="coordinates"
          v-on:coordinates="finishSelectingArea"
          ref="map"
        ></map-container>
      </div>

      <md-dialog-actions>
        <md-button class="md-info" @click="showDialog = false">Close</md-button>
        <md-button class="md-info" @click="clear">Clear map</md-button>
        <md-button class="md-info" @click="undo">Undo</md-button>
        <md-button class="md-info" @click="read">Save</md-button>
      </md-dialog-actions>
    </md-dialog>

    <md-card @click.native="openDialog" class="map-button" md-with-hover>
      <img src="@/assets/img/pretraga_na_mapi.jpg" alt="Map search" />
    </md-card>
  </div>
</template>

<script>
import MapContainer from "../../pages/Filters/MapContainer";

export default {
  name: "DialogMap",
  components: {
    MapContainer,
  },
  props: {
    coordinates: Array,
  },
  watch: {
    coordinates() {
      return this.coordinates;
    },
  },
  methods: {
    finishSelectingArea(value) {
      this.$emit("coordinates", value);
      this.showDialog = false;
    },
    read() {
      this.$refs.map.read();
      this.$sidebar.displaySidebar(true);
    },
    undo() {
      this.$refs.map.undo();
    },
    clear() {
      this.$refs.map.clear();
    },
    openDialog() {
      this.showDialog = true;
      this.$sidebar.displaySidebar(false);
    },
  },
  data: () => ({
    showDialog: false,
  }),
};
</script>

<style lang="scss" scoped>
@media only screen and (max-width: 600px) {
  .md-dialog-actions .md-button{
    margin: 5px 0 5px 0;
  }

  .md-dialog-actions {
    display: block;
    min-height: inherit;
  }
}
.md-dialog .md-dialog-container {
  max-width: 768px;
}

.md-dialog {
  width: 90%;
  height: 100%;
}

.cell {
  height: 100%;
}

.map-button {
  margin: 0;
}
</style>
