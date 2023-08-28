<template>
  <div v-if="!userId" class="content">
    <div class="md-layout md-gutter" style="margin-top: 30vh">
      <div class="md-layout-item md-size-45"></div>
      <div class="md-layout-item">
        <md-progress-spinner :md-diameter="100" :md-stroke="10" md-mode="indeterminate"></md-progress-spinner>
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters, mapActions} from "vuex";
import axios from "axios";

export default {
  computed: mapGetters(["userId", "apiUrl"]),
  methods: {
    ...mapActions(["setId"]),
  },
  created() {
    if (this.userId) {
      this.$router.push("user");
    } else {
      axios.get(`${this.apiUrl}/api/v1/user/`)
          .then((res) => {
            if(res.data.id) {
              this.setId(res.data.id);
              this.$router.push("user");
            } else {
              throw new Error("Not valid user response!")
            }
          })
          .catch((error) => {
            console.log(error)
          });
    }
  }
};
</script>
