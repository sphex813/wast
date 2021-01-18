import com.example.wast.api.models.SccData
import com.example.wast.databinding.EpisodeBinding
import com.example.wast.datastore.LocalStorage
import com.example.wast.datastore.PreferenceKeys
import com.example.wast.search.MovieClickListener
import com.example.wast.utils.HelpUtils
import com.example.wast.viewHolders.BaseViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EpisodeViewHolder(
    val binding: EpisodeBinding,
    val listener: MovieClickListener,
) : BaseViewHolder<SccData>(binding.root) {
    val localStorage = object : KoinComponent {
        val localStorage: LocalStorage by inject()
    }.localStorage

    override fun bind(item: SccData) {
        CoroutineScope(Dispatchers.IO).launch {
            val watchedList = localStorage.getValueAsMutableList(PreferenceKeys.WATCHED)
            binding.watched = watchedList.find { id -> id.equals(item._id) } != null
        }

        binding.movie = item
        binding.title =
            (adapterPosition + 1).toString() + " " + HelpUtils.getTitle(item._source.info_labels.originaltitle, item._source.i18n_info_labels)
        binding.movieImage = HelpUtils.getMovieLink(item._source.i18n_info_labels)
        binding.clickListner = listener
        binding.executePendingBindings()
    }
}