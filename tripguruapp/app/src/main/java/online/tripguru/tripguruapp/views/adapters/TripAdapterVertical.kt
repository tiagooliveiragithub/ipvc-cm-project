package online.tripguru.tripguruapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.databinding.BoxItemVerticalBinding
import online.tripguru.tripguruapp.models.Trip

class TripAdapterVertical : RecyclerView.Adapter<TripAdapterVertical.TripViewHolder>() {

    private var tripList = listOf<Trip>()

    inner class TripViewHolder(private val binding: BoxItemVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: Trip) {
            binding.textViewTitle.text = trip.tripName
            binding.textViewDate.text = trip.startDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemList = BoxItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.bind(trip)
    }

    override fun getItemCount(): Int = tripList.size

    // Método para atualizar a lista de viagens
    fun setTrips(trips: List<Trip>) {
        tripList = trips
        notifyDataSetChanged()
    }
}