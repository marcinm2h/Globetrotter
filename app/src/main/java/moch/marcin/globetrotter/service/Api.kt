package moch.marcin.globetrotter.service

object Api {
    val authService: AuthService by lazy { retrofit.create(AuthService::class.java) }
    val placesService: PlacesService by lazy { retrofit.create(PlacesService::class.java) }
}
