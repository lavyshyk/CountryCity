package com.lavyshyk.countrycity

object Constants {

}

const val APP_PREFERENCES: String = "my_preferences_file"
const val SORT_TO_BIG: Boolean = false
const val SORT_TO_SMALL: Boolean = true
const val ITEM_SORT_STATUS: String = "status_item"

/*
for bottom navigation
 */
const val COUNTRY_NAME_FOR_NAV_KEY = "key_country_name"
const val COUNTRY_NAME_KEY_FOR_DIALOG = "key_dialog"

/*
for saveStateInstance
 */
const val CURRENT_POSITION_ADAPTER_RV = "current_position_adapter"

/*
Permissions
 */
const val PERMISSION_ACCESS_LOCATION_REQUEST_STORAGE = 1001

/*
BottomSheet
 */

const val PEEK_HEIGHT = 120
const val MAX_AREA = 17_124_442F
const val MIN_AREA = 0F
const val MAX_POPULATION = 1_377_422_166F
const val MIN_POPULATION = 0F


/*
LiveData keys
 */

const val MY_FILTER_LIVE_DATA_KEY = "myFilterLiveDataKey"
const val COUNTRY_DTO = "countryDto"
const val SORTED_COUNTRY_DTO = "sortedCountryDto"
const val SEARCHED_LIST_COUNTRIES = "searchListCountries"
const val MY_CURRENT_LOCATION = "myCurrentLocation"
const val CAPITAL_DTO = "capitalDto"
const val TO_K_KM = 1000_000

/*
TimeUnits
 */

const val TIME_PAUSE_500 = 500L

/*
adapter
 */

const val LIKE = "like"
const val DISLIKE = "dislike"

/*
dispatchers
 */

const val DISPATCHER_IO = "dispatchers.io"

/*
LocationTrackingService
 */

const val SERVICE_ID = 359548
const val DISTANCE_CHANGE_FOR_UPDATE = 0f
const val TIME_INTERVAL_UPDATES = 500L
const val KILL_SERVICE = "kill service"
const val CHANNEL_ID = "channel_007"
const val LOCATION_KEY= "location"

/*
NEWS API
 */
const val KEY_HEADER = "X-Api-Key"
const val VALUE_HEADER = "a46503b629984dc0a3b9996ea564a340"

/*
Location Permission
 */
const val TAG_RATION = "rationale_tag"
const val RATIONALE_KEY = "rationale_tag"
const val SETTINGS_KEY = "settings_tag"
const val RESULT_KEY = "camera_result_key"
const val TAG_ASK = "dont_ask_tag"