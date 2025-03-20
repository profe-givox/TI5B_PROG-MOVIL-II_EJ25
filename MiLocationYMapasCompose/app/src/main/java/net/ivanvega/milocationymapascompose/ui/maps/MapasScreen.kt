package net.ivanvega.milocationymapascompose.ui.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker

import com.utsman.osmandcompose.Marker as MarkerOSMD

import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import com.utsman.osmandcompose.rememberOverlayManagerState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.CopyrightOverlay


@Composable
fun MiMapa(){
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    val singapore = LatLng(20.13933,-101.1506)

    val polilyne = listOf(LatLng(20.13933,-101.1506),
        LatLng(20.14340,-101.14987),
        LatLng(20.14385, -101.15101)
    )
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings

        ) {
            Polyline(points = polilyne, color = Color.Red)
            Marker(
                state = MarkerState(position = singapore),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
        )
    }
}

@Composable
fun MiMapaOSMDroidCompose(){
    val overlayManagerState = rememberOverlayManagerState()

    // define properties with remember with default value
    var mapProperties by remember {
        mutableStateOf(DefaultMapProperties)
    }

    // setup mapProperties in side effect
    SideEffect {
        mapProperties = mapProperties
            .copy(isTilesScaledToDpi = true)
            .copy(tileSources = TileSourceFactory.MAPNIK)
            .copy(isEnableRotationGesture = true)
            .copy(zoomButtonVisibility = ZoomButtonVisibility.ALWAYS)
            .copy(isMultiTouchControls = true)
    }

    // define camera state
    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(20.13933,-101.1506)
        zoom = 16.0 // optional, default is 5.0
    }

    // define polyline
    val geoPointPolyline = remember {
        listOf(GeoPoint(20.13933,-101.1506),
            GeoPoint(20.14340,-101.14987),
            GeoPoint(20.14385, -101.15101))
    }


    // define marker state
    val depokMarkerState = rememberMarkerState(
        geoPoint = GeoPoint(20.13933,-101.1506)
    )

    val context = LocalContext.current
    // add node
    OpenStreetMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState,
        properties = mapProperties,
        overlayManagerState = overlayManagerState, // setup overlay manager state
        onFirstLoadListener = {
            val copyright = CopyrightOverlay(context )
            overlayManagerState.overlayManager.add(copyright) // add another overlay in this listener
        }
    ){
        // add marker here
        MarkerOSMD(
            state = depokMarkerState
        )
        com.utsman.osmandcompose.Polyline(geoPoints = geoPointPolyline)
    }
}


