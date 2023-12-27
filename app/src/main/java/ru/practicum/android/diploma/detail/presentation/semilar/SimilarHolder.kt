package ru.practicum.android.diploma.detail.presentation.semilar

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemSimilarBinding
import ru.practicum.android.diploma.detail.domain.models.SimilarVacancy

class SimilarHolder(private val binding: ItemSimilarBinding) :
    RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(item: SimilarVacancy) {
        Glide
            .with(this.itemView.context)
            .load(item.logoUrlsEmployerOriginal)
            .placeholder(R.drawable.ic_logo)
            .into(binding.similarLogo)
        binding.nameSimilar.text = if (item.address != null) {
            "${item.name}, ${item.address}"
        } else {
            item.name
        }
        binding.nameSimilarEmployer.text = item.nameEmployer.orEmpty()
        binding.salarySimilar.text = item.salary?.getSalaryToText().orEmpty()
    }
}
