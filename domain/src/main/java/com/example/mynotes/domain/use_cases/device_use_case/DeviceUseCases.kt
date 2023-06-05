package com.example.mynotes.domain.use_cases.device_use_case

import javax.inject.Inject

class DeviceUseCases @Inject constructor(
    var getRemoteDevice: GetDevice,
    var getLocalDeviceId: GetLocalDeviceId,
    var saveDevice: SaveDevice,
    var observeDevice: ObserveDevice,
    var stopObservingDevice: StopObservingDevice

)
