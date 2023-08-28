<template>
  <div id="map" style="width: 100%; height: 100%"></div>
</template>

<style>
.ol-attribution.ol-uncollapsible {
  visibility: hidden;
}
</style>

<script>
import "ol/ol.css";
import Feature from "ol/Feature";
import Map from "ol/Map";
import View from "ol/View";
import Point from "ol/geom/Point";
import { OSM, Vector as VectorSource } from "ol/source";
import { Tile as TileLayer, Vector as VectorLayer } from "ol/layer";
import { Icon, Style } from "ol/style";
import { useGeographic } from "ol/proj";

export default {
  name: "MapContainerPin",
  components: {},
  props: {
    geolocation: Array,
    zoom: {
      type: Number,
      default: 13,
    }
  },
  data: () => ({
    map: null,
    layer: null,
  }),
  mounted() {
    useGeographic();

    this.map = new Map({
      target: "map",
      layers: [
        new TileLayer({
          source: new OSM(),
        }),
      ],
      view: new View({
        center: this.geolocation,
        zoom: this.zoom,
      }),
    });

    this.layer = new VectorLayer({
      source: new VectorSource({
        features: [
          new Feature({
            geometry: new Point(this.geolocation),
          }),
        ],
      }),
      style: new Style({
        image: new Icon({
          anchor: [0.5, 1],
          src: require("@/assets/img/map-marker.png"),
          scale: 0.2,
        }),
      }),
    });

    this.map.addLayer(this.layer);
  },
  methods: {},
};
</script>
