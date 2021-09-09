package com.lavyshyk.domain.filter

data class Filter(var minArea: Float = 0F,
                  var maxArea: Float = Float.MAX_VALUE,
                  var minPopulation: Float = 0F,
                  var maxPopulation: Float = Float.MAX_VALUE,
                  var distance: Float = Float.MAX_VALUE,
                  var name: String = ""
)
