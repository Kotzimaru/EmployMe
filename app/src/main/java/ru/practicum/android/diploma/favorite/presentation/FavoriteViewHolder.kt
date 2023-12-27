package ru.practicum.android.diploma.favorite.presentation

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemViewBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.ImageScale

class FavoriteViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy) {
        binding.department.text = vacancy.employer
        binding.salary.text = vacancy.salary
        binding.tvVacancyName.text = vacancy.name

        Glide.with(binding.ivCompany).load(vacancy.employerImgUrl).placeholder(R.drawable.placeholder).centerCrop()
            .transform(
                RoundedCorners(
                    ImageScale.roundCorner(
                        itemView.resources.displayMetrics.densityDpi,
                        ROUNDING_OF_CORNERS_PX
                    )
                )
            ).into(binding.ivCompany)
    }

    companion object {
        private const val ROUNDING_OF_CORNERS_PX = 12
    }
}
