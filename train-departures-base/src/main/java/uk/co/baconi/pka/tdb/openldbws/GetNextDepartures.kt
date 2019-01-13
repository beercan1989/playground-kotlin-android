package uk.co.baconi.pka.tdb.openldbws

import uk.co.baconi.pka.tdb.Request
import uk.co.baconi.pka.tdb.station.StationCode

/**
 * Created by James Bacon on 12/01/2019.
 */
class GetNextDepartures(accessToken: AccessToken, from: StationCode, to: StationCode) : Request {

    override val headers = mapOf(
        "Content-Type" to "text/xml;charset=UTF-8",
        "SOAPAction" to "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"
    )

    override val body = """
        |<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
        |               xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types"
        |               xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
        |    <soap:Header>
        |        <typ:AccessToken>
        |            <typ:TokenValue>${accessToken.value}</typ:TokenValue>
        |        </typ:AccessToken>
        |    </soap:Header>
        |    <soap:Body>
        |        <ldb:GetNextDeparturesRequest>
        |            <ldb:crs>${from.crsCode}</ldb:crs>
        |            <ldb:filterList>
        |                <ldb:crs>${to.crsCode}</ldb:crs>
        |            </ldb:filterList>
        |            <ldb:timeOffset>0</ldb:timeOffset>
        |            <ldb:timeWindow>120</ldb:timeWindow>
        |        </ldb:GetNextDeparturesRequest>
        |    </soap:Body>
        |</soap:Envelope>
    """.trimMargin()

}