package moch.marcin.globetrotter.service

object Api {
    val placesService: PlacesService by lazy { retrofit.create(PlacesService::class.java) }
}
