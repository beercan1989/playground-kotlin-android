package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class ServiceItem(
    val sta: String?, // Scheduled Time of Arrival [10:41]
    val eta: String?, // Estimated Time of Arrival [On time / 10:54]
    val std: String?, // Scheduled Time of Departure [10:41]
    val etd: String?, // Estimated Time of Departure [On time / 10:54]
    val platform: String?, // null / 1 / bus
    val operator: String?, // Northern
    val operatorCode: String?, // NT
    val serviceType: String?, // [bus / train / ferry]
    val serviceID: String?, // Base64 encoded string
    val rsid: String?, // Retail Service ID [NT044400]
    val origin: Location?,
    val destination: Location?

    /**
     * currentOrigins
     * currentDestinations
     * isCircularRoute // is operating on a circular route [true/false/null]
     * isCancelled // Cancelled at this location (from filter) [true/false/null]
     * filterLocationCancelled // Service is no longer stopping at destination (to filter) [true/false/null]
     * length // Number of train carriages {0 || null means unknown}
     * detachFront // if the service detaches units from the front at this location [true/false/null]
     * isReverseFormation // if the service is operating in the reverse of its normal formation [true/false/null]
     * cancelReason
     * delayReason
     * adhocAlerts // AdhocAlerts
     * formation // FormationData
     */

) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): ServiceItem {

            parser.require(XmlPullParser.START_TAG, null, "service")

            var sta: String? = null
            var eta: String? = null
            var std: String? = null
            var etd: String? = null
            var platform: String? = null
            var operator: String? = null
            var operatorCode: String? = null
            var serviceType: String? = null
            var serviceID: String? = null
            var rsid: String? = null
            var origin: Location? = null
            var destination: Location? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "sta" -> sta = parser.readAsText()
                    "eta" -> eta = parser.readAsText()
                    "std" -> std = parser.readAsText()
                    "etd" -> etd = parser.readAsText()
                    "platform" -> platform = parser.readAsText()
                    "operator" -> operator = parser.readAsText()
                    "operatorCode" -> operatorCode = parser.readAsText()
                    "serviceType" -> serviceType = parser.readAsText()
                    "serviceID" -> serviceID = parser.readAsText()
                    "rsid" -> rsid = parser.readAsText()
                    "origin" -> origin = readLocation(parser, "origin")
                    "destination" -> destination = readLocation(parser, "destination")
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "service")

            return ServiceItem(
                sta,
                eta,
                std,
                etd,
                platform,
                operator,
                operatorCode,
                serviceType,
                serviceID,
                rsid,
                origin,
                destination
            )
        }

        private fun readLocation(parser: XmlPullParser, type: String): Location? {

            parser.require(XmlPullParser.START_TAG, null, type)

            var location: Location? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "location" -> location = Location.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, type)

            return location
        }
    }
}