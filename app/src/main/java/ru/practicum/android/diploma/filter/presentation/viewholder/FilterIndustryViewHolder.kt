package ru.practicum.android.diploma.filter.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterIndustryViewHolder(private val binding: ItemIndustryBinding) : RecyclerView.ViewHolder(binding.root) {

    val rbIndustry = binding.cbSelect

    fun bind(industry: Industry) {
        binding.tvIndustryName.text = industry.name
        binding.cbSelect.isChecked = industry.isChecked
    }
}
