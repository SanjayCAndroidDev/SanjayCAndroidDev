package com.jesvardo.app.network.model


data class ModelResponseMyBookingList(
    var id: Int,

    var created_at: String,
    var updated_at: String,
    var destination: String,
    var destination_lat: Double,
    var destination_long: Double,
    var security_deposit: Float,
    var from_date: String,
    var to_date: String,

    var user: ModelResponseMyTripListUser,
    var listing: ModelResponseMyTripListListing,

    var security_deposit_due_date: String,
    var status: String,
    var owner_email: String,

    var price_quote: ModelResponseMyTripListPriceQuota,

    var reservation_amount: Float,
    var reservation_amount_due: Float,
    var reservation_amount_due_date: String,
    var delivery_location: String,
    var delivery_location_lat: Double,
    var delivery_location_long: Double

)

data class ModelResponseMyTripListUser(
    var id: Int,
    var username: String,
    var email: String,
    var provider: String,
    var confirmed: Boolean,
    var role: Int,
    var created_at: String,
    var updated_at: String,
    var first_name: String,
    var last_name: String,
    var user_bank_account: Int,
    var isOwner: Boolean,
    var phone_number: String,
    var gender: String,
    var avatar_url: String,
    var phone_verified: Boolean,
    var stripe_customer_id: String,
    var phone_country_code: String
)

data class ModelResponseMyTripListListing(

    var id: Int,
    var name: String,
    var description: String,
    var created_at: String,
    var updated_at: String,
    var has_monthly_discount: Boolean,
    var vehicle: Int,
    var country: String,
    var user: Int,
    var security_deposit: Int,
    var cancellation_policy: Int,
    var approval_policy: String,
    var nightly_rate: Int,
    var status: String,
    var has_delivery: Boolean,
    var delivery_distance: Int,
    var delivery_cost_per_mile: Float,
    var min_delivery_fee: Int,
    var has_mileage_rate: Boolean,
    var miles_included_per_day: Float,
    var mileage_overage_charge: Float,
    var has_generator: Boolean,
    var generator_hours_per_day: Int,
    var generator_overage_charge: Float,
    var min_rental_duration_days: Int,
    var has_generator_rate: Boolean,
    var vin_number: String,
    var registration_state_code: String,
    var current_mileage: Int,
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
    var allow_festivals: Boolean,
    var allow_smoking: Boolean,
    var has_weekly_discount: Boolean,
    var weekly_discount: Int,
    var owner: ModelResponseMyTripListOwner,
    var auto_approval_notice_period: Int,
    var images: ArrayList<ModelResponseVehicleListImages>
)

data class ModelResponseMyTripListOwner(
    var id: Int,
    var email: String,
    var last_name: String,
    var avatar_url: String,
    var first_name: String
)

data class ModelResponseMyTripListPriceQuota(
    var total: String,
    var end_date: String,
    var owner_fees: Float,
    var start_date: String,
    var service_fee: Float,
    var has_delivery: Boolean,
    var nightly_rate: Float,
    var has_generator: Boolean,
    var duration_in_days: Int,
    var has_mileage_rate: Boolean,
    var min_delivery_fee: Float,
    var security_deposit: Float,
    var delivery_distance: Float,
    var has_generator_rate: Boolean,
    var price_for_duration: Float,
    var reservation_amount: Float,
    var applicable_discount: String,
    var delivery_cost_per_mile: Float,
    var miles_included_per_day: Float,
    var optional_addon_charges: Float,
    var reservation_amount_due: Float,
    var discounted_nightly_rate: Float,
    var generator_hours_per_day: Int,
    var applicable_discount_rate: Float,
    var generator_overage_charge: Float,
    var min_rental_duration_days: Int,
    var security_deposit_due_date: String,
    var reservation_amount_due_date: String
)

