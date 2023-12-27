package ru.practicum.android.diploma.detail.presentation.semilar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemSimilarBinding
import ru.practicum.android.diploma.detail.domain.models.SimilarVacancy

class SimilarAdapter(
    private val similarVacancies: List<SimilarVacancy>,
    private val onClickListener: OnStateClickListener
) : RecyclerView.Adapter<SimilarHolder>() {
    interface OnStateClickListener {
        fun onStateClick(item: SimilarVacancy, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SimilarHolder(ItemSimilarBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return similarVacancies.size
    }

    override fun onBindViewHolder(holder: SimilarHolder, position: Int) {
        val item: SimilarVacancy = similarVacancies[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener.onStateClick(item, position)
        }
    }
}
