package uk.co.baconi.pka.td

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uk.co.baconi.pka.tdb.openldbws.responses.ServiceItem

class SearchResultsAdapter(private val searchResults: MutableList<ServiceItem>) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class SearchResultsViewHolder(
        layout: View,
        val avatar: TextView,
        val destination: TextView,
        val tickerLine: TextView,
        val departureTime: TextView
    ) : RecyclerView.ViewHolder(layout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {

        // create a new view
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)

        val avatar: TextView = layout.findViewById(R.id.search_result_avatar)
        val destination: TextView = layout.findViewById(R.id.search_result_destination)
        val tickerLine: TextView = layout.findViewById(R.id.search_result_ticker_line)
        val departureTime: TextView = layout.findViewById(R.id.search_result_departure_time)

        return SearchResultsViewHolder(layout, avatar, destination, tickerLine, departureTime)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {

        val context = holder.itemView.context

        val service = searchResults[position]
        val platform = service.platform
        val destination = service.destination?.first()
        val destinationName = destination?.locationName
        val destinationCrs = destination?.crs

        val (departureTimeText: String?, statusColourId: Int, actualDepartureTime: String) = when(service.etd) {
            null -> { // Not present
                Triple(
                    service.std,
                    R.color.search_result_departure_time_etd_unknown,
                    context.getString(R.string.search_result_etd_other, service.std)
                )
            }
            "On time" -> {
                Triple(
                    service.std,
                    R.color.search_result_departure_time_on_time,
                    context.getString(R.string.search_result_etd_on_time)
                )
            }
            "Delayed" -> {
                Triple(
                    context.getString(R.string.search_result_delayed),
                    R.color.search_result_departure_time_delayed,
                    context.getString(R.string.search_result_etd_delayed)
                )
            }
            else -> { // Estimated
                Triple(
                    service.etd,
                    R.color.search_result_departure_time_estimated,
                    context.getString(R.string.search_result_etd_other, service.etd)
                )
            }
        }

        val statusColour = context.getColorCompat(statusColourId)
        val defaultStatusColour = context.getColorCompat(R.color.search_result_departure_time_default)

        // TODO - Make up mind on colour by platform or status
        // TODO - Make up mind on coloring just text or background or both
        holder.avatar.text = platform
        if(Settings.EnableColouredAvatars.getSetting(context)) {
            holder.avatar.setTextColor(statusColour)
        } else {
            holder.avatar.setTextColor(defaultStatusColour)
        }

        holder.destination.text = context.getString(
            R.string.search_result_destination, destinationName, destinationCrs
        )

        holder.tickerLine.text = context.getString(
            R.string.search_result_ticker_line, service.std, destinationName, platform, actualDepartureTime
        )

        holder.departureTime.text = departureTimeText
        if(Settings.EnableColouredDepartureTimes.getSetting(context)) {
            holder.departureTime.setTextColor(statusColour)
        } else {
            holder.departureTime.setTextColor(defaultStatusColour)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = searchResults.size
}