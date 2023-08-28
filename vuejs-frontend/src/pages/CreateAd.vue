<template>
  <div class="content">
    <div class="md-layout md-alignment-top-center">
      <div class="md-layout-item md-medium-size-100 md-size-80">
      <create-ad-form :mode="mode" :id="id" data-background-color="green"></create-ad-form>
      </div>
    </div>
  </div>
</template>

<script>
import {CreateAdForm} from "@/pages";

export default {
  components: {
    CreateAdForm
  },
  props: {
    mode: String,
    id: String
  },
};
</script>
