package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

class NRCCMessages {

    companion object {

        internal fun fromXml(parser: XmlPullParser): List<String> {
            val entries = mutableListOf<String>()

            parser.require(XmlPullParser.START_TAG, null, "nrccMessages")

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "message" -> parser.readAsText()?.let(entries::add)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "nrccMessages")

            return entries.toList()
        }
    }
}