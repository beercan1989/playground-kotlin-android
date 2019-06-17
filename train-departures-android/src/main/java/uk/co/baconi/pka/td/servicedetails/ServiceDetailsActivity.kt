package uk.co.baconi.pka.td.servicedetails

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import arrow.core.Try.Failure
import arrow.core.Try.Success
import kotlinx.android.synthetic.main.activity_service_details.*
import kotlinx.android.synthetic.main.content_service_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.printStackTraceAsString
import uk.co.baconi.pka.td.provideAccessToken
import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.Actions
import uk.co.baconi.pka.tdb.openldbws.responses.servicedetails.ServiceDetailsResult

class ServiceDetailsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ServiceDetailsActivity"
        const val SERVICE_ID = "uk.co.baconi.pka.td.servicedetails.SERVICE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)
        setSupportActionBar(toolbar)

        // Initialise with nothing
        updateView()
        updateErrorView()

        provideAccessToken(service_details_scroll_view) { accessToken ->
            val serviceId = intent.getStringExtra(SERVICE_ID)
            searchForServiceDetails(accessToken, serviceId)
        }

        // TODO - Implement a back other than device back button?
    }

    private fun searchForServiceDetails(accessToken: AccessToken, serviceId: String) = GlobalScope.launch {
        when(val results = Actions.getServiceDetails(accessToken, serviceId)) {
            is Success<ServiceDetailsResult> -> {
                // Update with no error
                updateErrorView()

                // Update with results
                updateView(results.value)
            }
            is Failure -> {
                // Update with no result
                updateView()

                // Update with error
                updateErrorView(results.exception)
                Log.e(TAG, "Unable to get service details", results.exception)
            }
        }
    }

    private fun updateErrorView(error: Throwable? = null) = GlobalScope.launch(Dispatchers.Main)  {
        updateRow(error_class_row, error_class_value, if (error == null) null else error::class)
        updateRow(error_message_row, error_message_value, error?.message)
        updateRow(error_stacktrace_row, error_stacktrace_value, error?.printStackTraceAsString())
    }

    private fun updateView(result: ServiceDetailsResult? = null) = GlobalScope.launch(Dispatchers.Main) {
        updateRow(generated_at_row, generated_at_value, result?.generatedAt)
        updateRow(service_type_row, service_type_value, result?.serviceType)
        updateRow(location_row, location_value, result?.locationName)
        updateRow(crs_row, crs_value, result?.crs)
        updateRow(operator_row, operator_value, result?.operator)
        updateRow(operator_code_row, operator_code_value, result?.operatorCode)
        updateRow(rsid_row, rsid_value, result?.rsid)
        updateRow(cancelled_row, cancelled_value, result?.isCancelled)
        updateRow(cancel_reason_row, cancel_reason_value, result?.cancelReason)
        updateRow(delay_reason_row, delay_reason_value, result?.delayReason)
        updateRow(overdue_message_row, overdue_message_value, result?.overdueMessage)
        updateRow(length_row, length_value, result?.length)
        updateRow(detach_front_row, detach_front_value, result?.detachFront)
        updateRow(reverse_formation_row, reverse_formation_value, result?.isReverseFormation)
        updateRow(platform_row, platform_value, result?.platform)
        updateRow(sta_row, sta_value, result?.sta)
        updateRow(eta_row, eta_value, result?.eta)
        updateRow(ata_row, ata_value, result?.ata)
        updateRow(std_row, std_value, result?.std)
        updateRow(etd_row, etd_value, result?.etd)
        updateRow(atd_row, atd_value, result?.atd)

        // TODO - Improve if we ever start getting this data
        updateRow(adhoc_alerts_row, result?.adhocAlerts) { messages ->
            adhoc_alerts_value.text = messages.joinToString("\n")
        }

        // TODO - Improve if we ever start getting this data
        updateRow(formation_row, formation_value, result?.formation)

        // TODO - Update more
    }

    private inline fun <reified A> updateRow(row: TableRow, cell: TextView, value: A?) {
        updateRow(row, value) { nullSafeValue ->
            cell.text = when(nullSafeValue) {
                is String -> nullSafeValue
                else -> nullSafeValue.toString()
            }
        }
    }

    private inline fun <reified A> updateRow(row: TableRow, value: A?, update: (A) -> Unit) {
        when (value) {
            is A -> {
                row.visibility = View.VISIBLE
                update(value)
            }
            else -> row.visibility = View.GONE
        }
    }
}