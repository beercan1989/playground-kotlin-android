package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.openldbws.faults.Fault.Companion.fault
import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.skip

fun <T> XmlDeserializer.body(tag: String, inner: XmlDeserializer.() -> T): T {
    return parse(tag = "Body", initial = null) { result: T? ->
        when (getName()) {
            "Fault" -> throw fault()
            tag -> inner()
            else -> skip(result)
        }
    } ?: throw Exception("Body was empty or didn't expect it.") // TODO - Provide better exception class
}
