package com.lavyshyk.countrycity
object Constants {

}
const val BASE_URL = "https://restcountries.eu/rest/v2/"
const val APP_PREFERENCES: String = "my_preferences_file"
const val SORT_TO_BIG: Boolean = false
const val SORT_TO_SMALL: Boolean = true
const val ITEM_SORT_STATUS: String = "status_item"
/*
for bottom navigation
 */
const val COUNTRY_NAME_FOR_NAV_KEY =  "key_country_name"
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


/*
LiveData keys
 */

const val MY_FILTER_LIVE_DATA_KEY = "myFilterLiveDataKey"
const val COUNTRY_DTO = "countryDto"
const val SORTED_COUNTRY_DTO = "sortedCountryDto"
const val SEARCHED_LIST_COUNTRIES = "searchListCountries"
const val MY_CURRENT_LOCATION = "myCurrentLocation"