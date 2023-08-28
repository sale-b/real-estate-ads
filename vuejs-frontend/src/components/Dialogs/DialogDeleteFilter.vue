<template>
  <div>
    <md-dialog :md-active.sync="showDialog">
      <md-dialog-title>Obriši filter</md-dialog-title>

      <div style="padding: 30px">
        Da li ste sigurni da želite da obrišete filter pod nazivom: {{ filter.tittle }}
      </div>

      <md-dialog-actions>
        <md-button class="md-info" @click="showHideDialog()">Cancel</md-button>
        <md-button class="md-info" @click="deleteFilter()">Delete</md-button>
      </md-dialog-actions>
    </md-dialog>

    <md-button
      class="md-icon-button md-raised md-danger"
      @click="showHideDialog()"
      style="height: 40px; font-size: 18px; margin: 0"
    >
      X
    </md-button>
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
      this.$sidebar.showSidebar = false;
    },
    deleteFilter() {
      axios
        .delete( `${this.apiUrl}/api/v1/filter/${this.filter.id}`)
        .then((res) => {
          this.showDialog = false;
          if (res.status == 204){
            EventBus.$emit("filterDeleted", this.filter.id);
          }
        
        })
        .catch((error) => {
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
    filter: Object,
  },
  data: () => ({
    filterName: "Nenaslovljeno",
    showDialog: false,
    subscribed: false,
  }),
};
</script>
