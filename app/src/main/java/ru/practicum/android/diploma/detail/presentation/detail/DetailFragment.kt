package ru.practicum.android.diploma.detail.presentation.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailBinding
import ru.practicum.android.diploma.detail.domain.models.DetailVacancy
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.Error
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.Loading
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.NoConnect
import ru.practicum.android.diploma.detail.presentation.detail.DetailState.Success
import ru.practicum.android.diploma.detail.presentation.semilar.VACANCY

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val viewModel: DetailViewModel by viewModel {
        parametersOf(
            requireArguments().getString(ID)
        )
    }

    private val binding get() = _binding!!
    private var isFavorite = false
    private lateinit var detailVacancy: DetailVacancy

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { result ->
            render(result)
        }

        viewModel.getData()

        binding.back.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    private fun render(state: DetailState) {
        when (state) {
            is Success -> {
                setData(state.data)
                viewModel.isFavorite(state.data.id)
                detailVacancy = state.data
            }

            is Error -> {
                showError(
                    message = getString(R.string.server_error),
                    image = R.drawable.error_server_1
                )
            }

            is Loading -> {
                showProgress()
            }

            is NoConnect -> {
                showError(
                    message = getString(R.string.internet_connection_issue),
                    image = R.drawable.error_server_1
                )
            }

            is DetailState.IsFavorite -> {
                setFavorite(state.isFavorite)
            }
        }
    }

    private fun setFavorite(isFavourite: Boolean?) {
        when (isFavourite) {
            true -> binding.favorite.setImageResource(R.drawable.favorite)
            else -> binding.favorite.setImageResource(R.drawable.not_favorite2)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(detailVacancy: DetailVacancy) {
        binding.vacancyName.text = detailVacancy.name
        binding.salary.text = detailVacancy.salary?.getSalaryToText() ?: getString(R.string.not_salary)

        binding.grExperience.isVisible = detailVacancy.idExperience != null
        binding.experience.text = detailVacancy.nameExperience.orEmpty()

        binding.employment.isVisible = detailVacancy.idEmployment != null
        binding.employment.text = detailVacancy.nameEmployment
        binding.employerName.text = detailVacancy.nameEmployer.orEmpty()
        binding.employerCity.text = detailVacancy.address.orEmpty()

        Glide
            .with(requireContext())
            .load(detailVacancy.logoUrlsEmployerOriginal)
            .placeholder(R.drawable.ic_logo)
            .into(binding.employerLogo)

        if (detailVacancy.description != null && detailVacancy.description != "...") {
            binding.grDescription.isVisible = true
            binding.description.text =
                Html.fromHtml(detailVacancy.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        } else {
            binding.grDescription.isVisible = false
        }

        binding.grSkills.isVisible = !detailVacancy.keySkills.isNullOrEmpty()
        binding.keySkillsContent.text = detailVacancy.keySkills.orEmpty()

        binding.contacts.isVisible = !(
            detailVacancy.comment == null &&
                detailVacancy.phone == null &&
                detailVacancy.email == null &&
                detailVacancy.contactName == null
            )

        binding.grComment.isVisible = !detailVacancy.comment.isNullOrEmpty()
        binding.comment.text = detailVacancy.comment.orEmpty()

        binding.grContact.isVisible = !detailVacancy.contactName.isNullOrEmpty()
        binding.contactName.text = detailVacancy.contactName.orEmpty()

        binding.grPhone.isVisible = !detailVacancy.phone.isNullOrEmpty()
        binding.phone.text = detailVacancy.phone.orEmpty()

        binding.grEmail.isVisible = !detailVacancy.email.isNullOrEmpty()
        binding.email.text = detailVacancy.email.orEmpty()

        setDetailsContentListeners(detailVacancy)

        binding.progress.isVisible = false
        binding.errorPlaceholder.root.isVisible = false
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setDetailsContentListeners(detailVacancy: DetailVacancy) {
        binding.email.setOnClickListener {
            actionSendEmail(detailVacancy.email)
        }

        binding.phone.setOnClickListener {
            actionDial(detailVacancy.phone)
        }

        binding.btSimilar.setOnClickListener {
            val bundle = bundleOf(VACANCY to detailVacancy.id)
            view?.findNavController()
                ?.navigate(R.id.action_detailFragment_to_similarFragment, bundle)
        }

        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, detailVacancy.url)
            val chooserIntent = Intent.createChooser(intent, "Поделиться")
            if (context?.let { it1 -> intent.resolveActivity(it1.packageManager) } != null) {
                startActivity(chooserIntent)
            }
        }

        binding.favorite.setOnClickListener {
            viewModel.onFavoriteClick(detailVacancy, !isFavorite)
        }
    }

    private fun actionSendEmail(email: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        try {
            startActivity(intent)
        } catch (_: Exception) {
        }
    }

    private fun actionDial(phoneNumber: String?) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        try {
            startActivity(intent)
        } catch (_: Exception) {
        }
    }

    private fun showProgress() {
        binding.progress.isVisible = true
        binding.errorPlaceholder.root.isVisible = false
    }

    private fun showError(
        message: String,
        image: Int
    ) {
        with(binding.errorPlaceholder) {
            tvError.text = message
            imError.setImageResource(image)
        }
        binding.errorPlaceholder.root.isVisible = true
        binding.progress.isVisible = false
    }
    companion object {
        private const val ID = "id"
        fun createArgs(id: String): Bundle {
            return bundleOf(ID to id)
        }
    }
}
