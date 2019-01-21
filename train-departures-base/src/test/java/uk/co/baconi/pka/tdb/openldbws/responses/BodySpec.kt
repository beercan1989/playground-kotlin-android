package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class BodySpec : StringSpec({

    "Should decode with fault even no fault or action present" {

        val result = Body.fromXml(
            XmlParser.fromReader(
                "<Body/>".reader()
            )
        )

        result should beInstanceOf<BodyFailure>() // Because no action was provided
    }

    "Should decode with fault when present" {

        Body.fromXml(
            XmlParser.fromReader(
                "<Body><Fault/></Body>".reader()
            )
        ) should beInstanceOf<BodyFailure>()

        Body.fromXml(
            XmlParser.fromReader(
                "<Body><Fault></Fault></Body>".reader()
            )
        ) should beInstanceOf<BodyFailure>()

        Body.fromXml(
            XmlParser.fromReader(
                "<Body><Fault/><GetNextDeparturesResponse/></Body>".reader()
            )
        ) should beInstanceOf<BodyFailure>()

        Body.fromXml(
            XmlParser.fromReader(
                "<Body><GetNextDeparturesResponse/><Fault/></Body>".reader()
            )
        ) should beInstanceOf<BodyFailure>()
    }

    "Should decode with action present" {

        val result = Body.fromXml(
            XmlParser.fromReader(
                "<Body><GetNextDeparturesResponse/></Body>".reader()
            )
        )

        result should beInstanceOf<BodySuccess>()
    }
})