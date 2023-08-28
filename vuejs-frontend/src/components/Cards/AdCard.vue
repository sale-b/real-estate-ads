<template>
  <stats-card>
    <template slot="header">
      <img
        class="ad-pic"
        :src="
          imgUrl != null ? imgUrl : require('@/assets/img/no-image-found.png')
        "
        alt="Apartment img"
      />
    </template>

    <template slot="content">
      <h4 class="category" style="font-weight: bold">
        {{ tittle }}
      </h4>
      <br />
      <p class="ad-tittle">
        {{ content }}
      </p>
      <div class="price-tag">
        {{ price != null ? price.toLocaleString() : 0 }} &euro;
      </div>
    </template>

    <template slot="footer">
      <div>
        <div class="stats" style="padding: 3px 10px 0px 0px">
          <md-icon>date_range</md-icon>
          {{ date }}
        </div>

        <div class="stats" style="padding: 3px 10px 0px 0px">
          <md-icon>location_on</md-icon>
          {{ location }}
        </div>

        <div class="stats" style="padding: 0px 10px 0px 0px">
          <div>Kvadratura:</div>
          {{ space }} m<sup>2</sup>
        </div>
      </div>
      <div>
        <div class="stats" style="padding: 3px 10px 0px 0px">
          <div>Broj soba:</div>
          {{ rooms }}
        </div>

        <div class="stats" style="padding: 3px 10px 0px 0px">
          <div>Sprat:</div>
          {{ floor }}
        </div>

        <div class="stats" style="padding: 3px 10px 0px 0px">
          <md-icon>bed</md-icon>
          {{ furniture }}
        </div>
      </div>
    </template>
  </stats-card>
</template>

<script>
import StatsCard from "./StatsCard";

export default {
  components: {
    StatsCard,
  },
  methods: {
    openDetails(id) {
      this.$router.push("/ad/" + id).catch((err) => {});
    },
  },
  props: [
    "id",
    "tittle",
    "content",
    "imgUrl",
    "date",
    "location",
    "space",
    "rooms",
    "furniture",
    "floor",
    "price",
  ],
};
</script>


<style>
.pagination li {
  display: inline-block;
}

.pagination {
  padding-left: 0px;
  text-align: center;
}

a.page-link {
  font-size: 30px !important;
}

.pagination-holder {
  margin: auto;
}

a:hover,
a:focus {
  color: #0b63ac !important;
  cursor: pointer;
}

.md-card-header > img {
  margin: 0 !important;
  border-radius: 0 !important;
}

.img-head {
  padding: 0 !important;
}

.md-card-stats .md-card-content {
  text-align: left !important;
}

.ad-tittle {
  position: relative;
  margin-left: 120px;
  width: calc(100% - 240px);
  display: inline-block;
}

.price-tag {
  background-color: #4caf50;
  color: #ffffff;
  text-align: center;
  padding: 5px 15px 5px 15px;
  float: right;
  width: 100px;
}
</style>

<style scoped>
img.ad-pic {
  width: 120px;
  height: 120px;
  object-fit: cover;
}

@media screen and (max-width: 600px) {
  .price-tag {
    margin-top: 30px;
    float: left;
  }

  .ad-tittle {
    margin-left: 30px;
    width: calc(100% - 150px);
  }
}
</style>
