package com.jesvardo.app.network.model

import kotlinx.android.parcel.Parcelize

data class ModelResponseVehicleList(
    var id: Int,
    var name: String,
    var description: String,
    var created_at: String,
    var updated_at: String,
    var has_monthly_discount: Boolean,
    var monthly_discount: Float,

    var vehicle: ModelResponseVehicleListVehicle,

    var country: String,
    var security_deposit: Float,

    var cancellation_policy: ModelResponseCancellationPolicy,

    var approval_policy: String,
    var nightly_rate: Float,
    var status: String,
    var has_delivery: Boolean,
    var delivery_distance: Float,
    var delivery_cost_per_mile: Float,
    var min_delivery_fee: Float,
    var has_mileage_rate: Boolean,
    var miles_included_per_day: Float,
    var mileage_overage_charge: Float,
    var has_generator: Boolean,
    var generator_hours_per_day: Float,
    var generator_overage_charge: Float,
    var min_rental_duration_days: Float,
    var has_generator_rate: Boolean,
    var vin_number: String,
    var registration_state_code: String,
    var current_mileage: Float,
    var has_salvage_title: Boolean,
    var insurance_terms_accepted: Boolean,
    var step_vehicle_details: Boolean,
    var step_listing_details: Boolean,
    var step_pricing: Boolean,
    var street_1: String,
    var street_2: String,
    var city: String,
    var state: String,
    var zipcode: String,
    var lat: Double,
    var long: Double,
    var allow_pets: Boolean,
    var allow_tailgating: Boolean,
    var allow_festivals : Boolean,
    var allow_smoking: Boolean,
    var has_weekly_discount: Boolean,
    var weekly_discount: Float,

    var owner: ModelResponseVehicleListOwner,

    var auto_approval_notice_period : Int,

    var images: ArrayList<ModelResponseVehicleListImages>,

    var listing_addons: ArrayList<ModelResponseAddOns>
)

data class ModelResponseVehicleListVehicle(
    var id: Int,
    var body_length: Int,
    var created_at: String,
    var updated_at: String,
    var listing: Int,
    var vehicle_type: Int,
    var vehicle_make: Int,
    var vehicle_model: Int,
    var vehicle_year: Int,
    var transmission: String,
    var fuel_type: String,
    var air_conditioner: Boolean,
    var audio_inputs: Boolean,
    var awning: Boolean,
    var backup_camera: Boolean,
    var bike_rack: Boolean,
    var burning_man_friendly: Boolean,
    var cd_player: Boolean,
    var dining_table: Boolean,
    var extra_storage: Boolean,
    var generator: Boolean,
    var handicap_accessible: Boolean,
    var heater: Boolean,
    var hot_water_tank: Boolean,
    var inside_shower: Boolean,
    var inverter: Boolean,
    var kitchen_sink: Boolean,
    var leveling_jacks: Boolean,
    var microwave: Boolean,
    var outside_shower: Boolean,
    var refrigerator: Boolean,
    var radio: Boolean,
    var satellite: Boolean,
    var skylight: Boolean,
    var solar: Boolean,
    var stove: Boolean,
    var toilet: Boolean,
    var tow_hitch: Boolean,
    var tv_dvd: Boolean,
    var washer_dryer: Boolean,
    var wifi: Boolean,
    var overnight_guests: Int,
    var seatbelts: Int,
    var fuel_capacity: Int,
    var fuel_capacity_unit: String,
    var gray_water_capacity: Int,
    var gray_water_capacity_unit: String,
    var water_capacity: Int,
    var water_capacity_units: String,
    var sewage_capacity: Int,
    var sewage_capacity_units: String
)

data class ModelResponseCancellationPolicy(
    var id: Int,
    var name: String,
    var valid_days: Int,
    var discount_upto_valid_days: Int,
    var discount_post_valid_days: Int,
    var created_at: String,
    var updated_at: String,
    var listing: Int
)

data class ModelResponseVehicleListOwner(
    var id: Int,
    var email: String,
    var last_name: String,
    var avatar_url: String,
    var first_name: String
)

data class ModelResponseAddOns(
var id : Int,
var name : String,
var description : String,
var price : Float,
var quantity : Int,
var created_at : String,
var updated_at : String,
var listing : Int,
var required : Boolean
)

