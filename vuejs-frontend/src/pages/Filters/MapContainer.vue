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
import Draw from "ol/interaction/Draw";
import Map from "ol/Map";
import View from "ol/View";
import { OSM, Vector as VectorSource } from "ol/source";
import { Tile as TileLayer, Vector as VectorLayer } from "ol/layer";
import GeoJSON from "ol/format/GeoJSON";
import { useGeographic } from "ol/proj";

export default {
  name: "MapContainer",
  components: {},
  props: {
    coordinates: Array,
  },
  computed: {
    geojson() {
      return {
        type: "Feature",
        properties: {
          name: "default object",
          quality: "top",
        },
        geometry: {
          type: "Polygon",
          coordinates: [this.coordinates],
        },
      };
    },
  },
  data() {
    return {
      raster: null,
      source: null,
      vector: null,
      map: null,
      draw: null,
    };
  },
  mounted() {
    useGeographic();
    this.source = new VectorSource({ features: [] });

    this.raster = new TileLayer({
      source: new OSM(),
    });

    this.vector = new VectorLayer({
      source: this.source,
    });

    this.map = new Map({
      layers: [this.raster, this.vector],
      target: "map",
      view: new View({
        center: [20.457273, 44.787197],
        zoom: 13,
        constrainResolution: true,
      }),
    });

    this.draw = new Draw({
      source: this.source,
      type: "Polygon",
    });

    this.map.addInteraction(this.draw);

    this.draw.on("drawstart", this.clear);

    this.updateSource(this.geojson);
  },
  methods: {
    updateSource(geojson) {
      if (geojson.geometry.coordinates[0] != null) {
        const view = this.map.getView();
        const source = this.vector.getSource();

        const features = new GeoJSON({
          featureProjection: "EPSG:4326",
        }).readFeatures(geojson);

        source.clear();
        source.addFeatures(features);

        view.fit(source.getExtent());
      }
    },
    clear() {
      this.map.getLayers().getArray()[1].getSource().clear();
    },

    read() {
      let feature = this.vector.getSource().getFeatures()[0];
      if (feature != null) {
        this.$emit("coordinates", feature.getGeometry().getCoordinates()[0]);
      } else {
        this.$emit("coordinates", null);
      }
    },
    undo() {
      this.draw.removeLastPoint();
    },
  },
};
</script>
