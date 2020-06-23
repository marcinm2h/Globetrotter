package moch.marcin.globetrotter.service

import android.app.Activity

object Api {
    val authService: AuthService by lazy { retrofit.create(AuthService::class.java) }
    val placesService: PlacesService by lazy { retrofit.create(PlacesService::class.java) }
}
