package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class StationBoardResult(
    val generatedAt: String? = null,
//    val locationName: String? = null, // Station Name [Sheffield]
//    val crs: String? = null, // CRS Code [SHF]
//    val filterLocationName: String? = null,
//    val filtercrs: String? = null,
//    val filterType: String? = null,
//    val nrccMessages: List<String>? = null, // Messages [Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare.]
//    val platformAvailable: Boolean? = null, // [true|false|null]
//    val areServicesAvailable: Boolean? = null, // [true|false|null]
//    val departures: List<DepartureItem>? = null
    val trainServices: List<ServiceItem>? = null,
    val busServices: List<ServiceItem>? = null,
    val ferryServices: List<ServiceItem>? = null
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): StationBoardResult {

            parser.require(XmlPullParser.START_TAG, null, "GetStationBoardResult")

            var result = StationBoardResult()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "generatedAt" -> result = result.copy(generatedAt = parser.readAsText())
                    "locationName" -> result = result.copy(locationName = parser.readAsText())
                    "crs" -> result = result.copy(crs = parser.readAsText())
                    "filterLocationName" -> result = result.copy(filterLocationName = parser.readAsText())
                    "filtercrs" -> result = result.copy(filtercrs = parser.readAsText())
                    "filterType" -> result = result.copy(filterType = parser.readAsText())
                    "nrccMessages" -> result = result.copy(nrccMessages = NRCCMessages.fromXml(parser))
                    "platformAvailable" -> result = result.copy(platformAvailable = parser.readAsBoolean())
                    "areServicesAvailable" -> result = result.copy(areServicesAvailable = parser.readAsBoolean())
                    "departures" -> result = result.copy(departures = Departures.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "GetStationBoardResult")

            return result
        }
    }
}