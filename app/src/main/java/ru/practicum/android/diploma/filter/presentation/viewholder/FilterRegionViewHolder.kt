package ru.practicum.android.diploma.filter.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filter.domain.models.Region

class FilterRegionViewHolder(private val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(region: Region) {
        binding.name.text = region.name
    }
}
