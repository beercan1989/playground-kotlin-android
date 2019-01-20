package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class ServiceLocation(
    val locationName: String? = null, // Station name [Barnsley]
    val crs: String? = null, // CRS Code [BNY]
    val via: String? = null, // [via Leeds]
    val futureChangeTo: String? = null, // [Bus / Ferry / Train]
    val assocIsCancelled: Boolean? = null
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): ServiceLocation {

            parser.require(XmlPullParser.START_TAG, null, "location")

            var result = ServiceLocation()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "locationName" -> result = result.copy(locationName = parser.readAsText())
                    "crs" -> result = result.copy(crs = parser.readAsText())
                    "via" -> result = result.copy(via = parser.readAsText())
                    "futureChangeTo" -> result = result.copy(futureChangeTo = parser.readAsText())
                    "assocIsCancelled" -> result = result.copy(assocIsCancelled = parser.readAsBoolean())
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "location")

            return result
        }
    }
}