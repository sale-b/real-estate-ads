<template>
  <div style="width: 100%; height: 300px; border: 3px solid #73ad21">
    <div id="map" style="width: 100%; height: 100%"></div>
  </div>
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
import {OSM, Vector as VectorSource} from "ol/source";
import {Tile as TileLayer, Vector as VectorLayer} from "ol/layer";
import {Icon, Style} from "ol/style";
import {useGeographic} from "ol/proj";
import Select from "ol/interaction/Select";

export default {
  name: "MapContainerPinSelect",
  components: {},
  props: {
    zoom: Number,
    centerCoordinates: Object,
    pointCoordinates: Object
  },
  data: () => ({
    map: null,
    layer: null,
    raster: null,
    source: null,
    vector: null,
    select: null,
  }),
  mounted() {
    useGeographic();
    this.source = new VectorSource({features: []});

    this.raster = new TileLayer({
      source: new OSM(),
    });

    this.vector = new VectorLayer({
      source: this.source,
    });


    this.map = new Map({
      layers: [this.raster],
      target: "map",
      view: new View({
        center: [this.centerCoordinates.x,this.centerCoordinates.y],
        zoom: this.zoom,
      }),
    });

    const vectorSource = new VectorSource();
    const vectorLayer = new VectorLayer({
      source: vectorSource,
      style: new Style({
        image: new Icon({
          anchor: [0.5, 1],
          src: require("@/assets/img/map-marker.png"),
          scale: 0.11,
        }),
      }),
    });

    this.map.addLayer(vectorLayer);

    const selectInteraction = new Select({
      layers: [vectorLayer],
    });

    this.map.addInteraction(selectInteraction);

    if (this.pointCoordinates) {
      vectorSource.addFeature(new Feature(new Point(
          [this.pointCoordinates.x,this.pointCoordinates.y]
      )));
    }

    this.map.on('click', (event) => {
      vectorSource.clear();

      const coordinate = event.coordinate;
      const pointFeature = new Feature(new Point(coordinate));

      vectorSource.addFeature(pointFeature);

      const selectedCoordinates = pointFeature.getGeometry().getCoordinates();
      this.$emit('pointSelected', selectedCoordinates);
    });

  },
  methods: {},
};
</script>
