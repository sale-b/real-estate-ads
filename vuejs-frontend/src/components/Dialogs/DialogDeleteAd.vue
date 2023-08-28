<template>
  <div>
    <md-dialog :md-active.sync="showDialog">
      <md-dialog-title>Obriši oglas</md-dialog-title>

      <div style="padding: 30px">
        Da li ste sigurni da želite da obrišete oglas pod nazivom: {{ ad.tittle }}
      </div>

      <md-dialog-actions>
        <md-button class="md-info" @click="showHideDialog()">Cancel</md-button>
        <md-button class="md-info" @click="deleteAd()">Delete</md-button>
      </md-dialog-actions>
    </md-dialog>

    <md-button
        style="float:right;"
        class="md-round md-danger"
        @click="showHideDialog()"
    >
      Obriši oglas
    </md-button>
  </div>
</template>

<script>
import axios from "axios";
import {mapActions, mapGetters} from "vuex";

export default {
  computed: {
    ...mapGetters(["userId", "apiUrl"]),
  },
  methods: {
    showHideDialog() {
      this.showDialog = !this.showDialog;
      this.$sidebar.showSidebar = false;
    },
    deleteAd() {
      axios
          .delete(`${this.apiUrl}/api/v1/real-estate/${this.ad.id}`)
          .then((res) => {
            if(res.status ===204) this.$router.push({name: 'Ads', params: {id: 1}});
          })
          .catch((error) => {
            window.scrollTo(0, 0);
            this.showHideDialog();
              this.$notify({
                message: "Not authorized or other error occurred during deleting ad.",
                icon: "add_alert",
                horizontalAlign: "center",
                verticalAlign: "top",
                type: "danger",
              });
          });
    },
    ...mapActions(["removeId"]),
  },
  props: {
    ad: Object,
  },
  data: () => ({
    adName: "Nenaslovljeno",
    showDialog: false,
    subscribed: false,
  }),
};
</script>
