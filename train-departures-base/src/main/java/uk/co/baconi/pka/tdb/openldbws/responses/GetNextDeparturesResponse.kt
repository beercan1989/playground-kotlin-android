package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class GetNextDeparturesResponse(val departuresBoard: DeparturesBoard?) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): GetNextDeparturesResponse {

            parser.require(XmlPullParser.START_TAG, null, "GetNextDeparturesResponse")

            var departuresBoard: DeparturesBoard? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "DeparturesBoard" -> departuresBoard = DeparturesBoard.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "GetNextDeparturesResponse")

            return GetNextDeparturesResponse(departuresBoard)
        }
    }
}